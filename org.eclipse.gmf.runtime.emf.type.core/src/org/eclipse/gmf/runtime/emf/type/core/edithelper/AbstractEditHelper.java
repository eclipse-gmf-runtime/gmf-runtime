/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.edithelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.IdentityCommand;
import org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateRelationshipCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.GetEditContextCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.MoveElementsCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.internal.InternalRequestParameters;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyDependentsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;

/**
 * Abstract edit helper implementation. Implements the default edit command
 * algorithm, which returns a composite command containing the following:
 * <OL>
 * <LI>'before' commands from matching element types and  specializations</LI>
 * <LI>'instead' command from this edit helper</LI>
 * <LI>'after' commands from matching element types and specializations</LI>
 * </OL>
 * <P>
 * The before and after commands are obtained by consulting the edit helper advice that
 * is bound to the edit helper context in the edit request. Edit helper advice can
 * be inherited from supertypes.
 * <P>
 * Clients should subclass this class when creating new edit helpers.
 * 
 * @author ldamus
 */
public abstract class AbstractEditHelper
	implements IEditHelper {
	
	/**
	 * Map of the default containment features keyed on EClass. Each value is an
	 * EReference.
	 */
	private Map defaultContainmentFeatures = new HashMap();
    
    /**
     * Checks that I can get an executable edit command.
     * <P>
     * Subclasses should override if they have a different way to decide whether
     * or not the edit is allowed.
     */
    public boolean canEdit(IEditCommandRequest req) {

        // Get the matching edit helper advice
        IEditHelperAdvice[] advice = getEditHelperAdvice(req);
        
        // Consult advisors to allow them to configure the request
        configureRequest(req, advice);
        
        // Consult advisors to allow them approve the request
        boolean approved = approveRequest(req, advice);
        
        if (!approved) {
            return false;
        }
        
        ICommand command = getEditCommand(req, advice);
        return command != null && command.canExecute();
    }

	/**
     * Builds and returns the edit command, which is a composite command
     * containing the following:
     * <OL>
     * <LI>'before' commands from matching element type specializations</LI>
     * <LI>'instead' command from this edit helper</LI>
     * <LI>'after' commands from matching element type specializations</LI>
     * </OL>
     * <P>
     * Verifies that the edit request is approved before constructing the edit
     * command.
     */
	public ICommand getEditCommand(IEditCommandRequest req) { 
		
		// Get the matching edit helper advice
		IEditHelperAdvice[] advice = getEditHelperAdvice(req);
        
		// Consult advisors to allow them to configure the request
        configureRequest(req, advice);
        
		// Consult advisors to allow them approve the request
        boolean approved = approveRequest(req, advice);
        
        if (!approved) {
            return null;
        }
        
        return getEditCommand(req, advice);
	}
    
    /**
     * Template method that implements the default edit command algorithm, which
     * returns a composite command containing the following:
     * <OL>
     * <LI>'before' commands from matching element type specializations</LI>
     * <LI>'instead' command from this edit helper</LI>
     * <LI>'after' commands from matching element type specializations</LI>
     * </OL>
     */
    private ICommand getEditCommand(IEditCommandRequest req, IEditHelperAdvice[] advice) { 
        ICompositeCommand command = createCommand(req);

        // Get 'before' commands from matching element type
        // specializations
        if (advice != null) {
            for (int i = 0; i < advice.length; i++) {
                IEditHelperAdvice nextAdvice = advice[i];

                // Before commands
                ICommand beforeAdvice = nextAdvice.getBeforeEditCommand(req);
                
                if (beforeAdvice != null) {

                    if (!beforeAdvice.canExecute()) {
                        // The operation is not permitted
                        return UnexecutableCommand.INSTANCE;
                    }
                    command.compose(beforeAdvice);
                }
            }
        }
        
        // Check if the parameter has been set to ignore the default edit command.
        Object replaceParam = req
                .getParameter(IEditCommandRequest.REPLACE_DEFAULT_COMMAND);

        if (replaceParam != Boolean.TRUE) {
            // Get 'instead' command from this edit helper
            ICommand insteadCommand = getInsteadCommand(req);

            if (insteadCommand != null) {
                
                if (!insteadCommand.canExecute()) {
                    // The operation is not permitted
                	return UnexecutableCommand.INSTANCE;
                }
                command.compose(insteadCommand);
            }
        }
        
        // Get 'after' commands from matching element type
        // specializations
        if (advice != null) {
            for (int i = 0; i < advice.length; i++) {
                IEditHelperAdvice nextAdvice = advice[i];

                // After commands
                ICommand afterAdvice = nextAdvice.getAfterEditCommand(req);

                if (afterAdvice != null) {
                    
                    if (!afterAdvice.canExecute()) {
                        // The operation is not permitted
                    	return UnexecutableCommand.INSTANCE;
                    }
                    command.compose(afterAdvice);
                }
            }
        }
        
        return command.isEmpty() ? null
            : command;
    }
    
    /**
     * Template method that consults the edit helper advice to configure the
     * edit request.
     * 
     * @param req
     *            the edit request
     * @param advice
     *            array of applicable edit helper advice
     */
    private void configureRequest(IEditCommandRequest req,
            IEditHelperAdvice[] advice) {

        if (advice != null) {

            for (int i = 0; i < advice.length; i++) {
                IEditHelperAdvice nextAdvice = advice[i];
                nextAdvice.configureRequest(req);
            }
        }
        // All advice has configured the request. Now consult this edit helper.
        configureRequest(req);
    }
    
    /**
     * Template method that consults the edit helper advice to see whether or
     * not they approve the request. If all advice approves the request, then
     * {@link #approveRequest(IEditCommandRequest)} is called to determine if
     * this edit helper approves the request.
     * 
     * @param req
     *            the edit request
     * @param advice
     *            array of applicable edit helper advice
     * @return <code>true</code> if the edit request is approved,
     *         <code>false</code> otherwise. No edit command will be
     *         constructed if the request is not approved.
     */
    private boolean approveRequest(IEditCommandRequest req,
            IEditHelperAdvice[] advice) {

        if (advice != null) {

            for (int i = 0; i < advice.length; i++) {
                IEditHelperAdvice nextAdvice = advice[i];
                boolean approved = nextAdvice.approveRequest(req);

                if (!approved) {
                    // An advice doesn't approve this request
                    return false;
                }
            }
        }
        // All advice has approved the request. Now consult this edit helper.
        return approveRequest(req);
    }
   
    /**
     * Approves the edit gesture described in the <code>request</code>. This
     * method will be consulted before the edit request is approved.
     * <P>
     * The default implementation does nothing. Subclasses should override if
     * they wish to change the request parameters.
     * 
     * @param request
     *            the edit request
     */
    protected void configureRequest(IEditCommandRequest request) {
        // does nothing, by default
    }
    
    /**
     * Approves the edit gesture described in the <code>request</code>. This
     * method will be consulted before the edit command is constructed.
     * <P>
     * The default implementation returns <code>true</code>. Subclasses
     * should override if they wish to provide a different answer.
     * 
     * @param req
     *            the edit request
     * @return <code>true</code> if the edit request is approved,
     *         <code>false</code> otherwise. No edit command will be
     *         constructed if the request is not approved.
     */
    protected boolean approveRequest(IEditCommandRequest request) {
        return true;
    }
	
	/**
	 * Gets the array of edit helper advice for this request.
	 * 
	 * @param req the edit request
	 * @return the edit helper advice, or <code>null</code> if there is none
	 */
	protected IEditHelperAdvice[] getEditHelperAdvice(IEditCommandRequest req) {

		Object editHelperContext = req.getEditHelperContext();
		return ElementTypeRegistry.getInstance().getEditHelperAdvice(
			editHelperContext);
	}

	/**
	 * Creates a new composite command.
	 * <P>
	 * Subclasses may override to provide their own kind of composite command.
	 * 
	 * @param req the edit request
	 * @return a new composite command
	 */
	protected ICompositeCommand createCommand(IEditCommandRequest req) {
		
		CompositeTransactionalCommand result = new CompositeTransactionalCommand(
				req.getEditingDomain(),
				req.getLabel()) {
			
			/**
			 * Extracts the first return value out of the collection of return
			 * values from the superclass command result.
			 */
			public CommandResult getCommandResult() {
				CommandResult result = super.getCommandResult();
				
				IStatus status = result.getStatus();
				
				if (status.getSeverity() == IStatus.OK) {
					Object returnObject = null;
					
					Object returnValue = result.getReturnValue();
					
					if (returnValue instanceof Collection) {
						Collection collection = (Collection) returnValue;
						
						if (!collection.isEmpty()) {
							returnObject = collection.iterator().next();
						}
						
					} else {
						returnObject = returnValue;
					}
					result = new CommandResult(status, returnObject);
				}
				
				return result;
			};
		};
		
		// commands (esp. destroy) are expected to be large nested structures,
		//   because there can be many discrete particles of advice
		result.setTransactionNestingEnabled(false);
		
		return result;
	}

	/**
	 * Gets my command to do the work described in <code>req</code>.
	 * <P>
	 * Delegates to the more specific methods in this class to actually get the
	 * command. Subclasses should override these more specific methods.
	 * 
	 * @param req
	 *            the edit request
	 * @return the command to do the requested work, or <code>null</code> if I
	 *         don't support the requested work.
	 */
	protected ICommand getInsteadCommand(IEditCommandRequest req) {

		if (req instanceof CreateRelationshipRequest) {
			initializeDefaultFeature((CreateElementRequest) req);
			return getCreateRelationshipCommand((CreateRelationshipRequest) req);

		} else if (req instanceof CreateElementRequest) {
			initializeDefaultFeature((CreateElementRequest) req);
			return getCreateCommand((CreateElementRequest) req);

		} else if (req instanceof ConfigureRequest) {
			return getConfigureCommand((ConfigureRequest) req);

		} else if (req instanceof DestroyElementRequest) {
			return getDestroyElementCommand((DestroyElementRequest) req);
			
		} else if (req instanceof DestroyDependentsRequest) {
			return getDestroyDependentsCommand((DestroyDependentsRequest) req);
			
		} else if (req instanceof DestroyReferenceRequest) {
			return getDestroyReferenceCommand((DestroyReferenceRequest) req);

		} else if (req instanceof DuplicateElementsRequest) {
			return getDuplicateCommand((DuplicateElementsRequest) req);

		} else if (req instanceof GetEditContextRequest) {
			return getEditContextCommand((GetEditContextRequest) req);

		} else if (req instanceof MoveRequest) {
			return getMoveCommand((MoveRequest) req);

		} else if (req instanceof ReorientReferenceRelationshipRequest) {
			return getReorientReferenceRelationshipCommand((ReorientReferenceRelationshipRequest) req);

		} else if (req instanceof ReorientRelationshipRequest) {
			return getReorientRelationshipCommand((ReorientRelationshipRequest) req);

		} else if (req instanceof SetRequest) {
			return getSetCommand((SetRequest) req);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.type.IEditHelper#getContainedValues(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EReference)
	 */
	public List getContainedValues(EObject eContainer, EReference feature) {
		return Arrays.asList(ElementTypeRegistry.getInstance()
			.getContainedTypes(eContainer, feature));
	}

	/**
	 * Gets the command to configure a new element of my kind. By default,
	 * returns <code>null</code>. Subclasses may override to provide their
	 * command.
	 * 
	 * @param req
	 *            the configure request
	 * @return the configure command
	 */
	protected ICommand getConfigureCommand(ConfigureRequest req) {
		return null;
	}

	/**
     * Gets the command to create a new relationship in an element of my kind.
     * <P>
     * Returns the {@link IdentityCommand} if the request does not have a source
     * or a target. This ensures that the create relationship gesture is enabled
     * until the request can be completely specified.
     * <P>
     * Subclasses may override to provide their own command.
     * 
     * @param req
     *            the create relationship request
     * @return the create relationship command
     */
	protected ICommand getCreateRelationshipCommand(
			CreateRelationshipRequest req) {
        
        EObject source = req.getSource();
        EObject target = req.getTarget();

        boolean noSourceOrTarget = (source == null || target == null);
        boolean noSourceAndTarget = (source == null && target == null);

        if (noSourceOrTarget && !noSourceAndTarget) {
            // The request isn't complete yet. Return the identity command so
            // that the create relationship gesture is enabled.
            return IdentityCommand.INSTANCE;
        }
        
		return new CreateRelationshipCommand(req);
	}

	/**
	 * Gets the command to create a new element in an element of my kind.
	 * Subclasses may override to provide their command.
	 * 
	 * @param req
	 *            the create request
	 * @return the create command
	 */
	protected ICommand getCreateCommand(CreateElementRequest req) {
		return new CreateElementCommand(req);
	}
	
	/**
	 * Sets the default feature in <code>req</code>, if there is no
	 * containment feature already in the request.
	 * 
	 * @param req
	 *            the create request
	 */
	public void initializeDefaultFeature(CreateElementRequest req) {

		if (req.getContainmentFeature() == null) {

			// First, try to find the feature from the element type
			ISpecializationType specializationType = (ISpecializationType) req.getElementType().getAdapter(ISpecializationType.class);
			
			if (specializationType != null) {
				IContainerDescriptor containerDescriptor = specializationType.getEContainerDescriptor();

				if (containerDescriptor != null) {
					EReference[] features = containerDescriptor
						.getContainmentFeatures();
					
					if (features != null) {

						for (int i = 0; i < features.length; i++) {

							Object editHelperContext = req
								.getEditHelperContext();
							EClass eClass = null;

							if (editHelperContext instanceof EClass) {
								eClass = (EClass) editHelperContext;
								
							} else if (editHelperContext instanceof EObject) {
								eClass = ((EObject) editHelperContext).eClass();
								
							} else if (editHelperContext instanceof IElementType) {
								eClass = ((IElementType) editHelperContext)
									.getEClass();
							}

							if (eClass != null
								&& eClass.getEAllReferences().contains(
									features[i])) {
								// Use the first feature
								req.initializeContainmentFeature((features[i]));
								return;
							}
						}
					}
				}
			}

			// Next, try to get a default feature
			EClass eClass = req.getElementType().getEClass();
			
			if (eClass != null) {
				req.initializeContainmentFeature(getDefaultContainmentFeature(eClass));
			}
		}
	}
	
	/**
	 * Gets the default feature to contain the <code>eClass</code>.
	 * <P>
	 * Returns <code>null</code> by default. Subclasses should override to
	 * provide the default feature, if there is one.
	 * 
	 * @param eClass
	 *            the EClass
	 * @return the default feature
	 */
	protected EReference getDefaultContainmentFeature(EClass eClass) {
		EReference result = (EReference) getDefaultContainmentFeatures().get(
				eClass);

		if (result == null) {
			List superTypes = new ArrayList(eClass.getEAllSuperTypes());
			Collections.reverse(superTypes);

			Iterator i = superTypes.iterator();
			
			while (i.hasNext() && result == null) {
				EClass nextSuperType = (EClass) i.next();
				result = (EReference) getDefaultContainmentFeatures().get(
						nextSuperType);
			}
		}
		return result;
	}
	
	protected Map getDefaultContainmentFeatures() {
		return defaultContainmentFeatures;
	}

	/**
	 * Gets the command to set a value of an element of my kind. By default,
	 * returns <code>null</code>. Subclasses may override to provide their
	 * command.
	 * 
	 * @param req
	 *            the set request
	 * @return the set command
	 */
	protected ICommand getSetCommand(SetRequest req) {
		return new SetValueCommand(req);
	}

	/**
	 * Gets the command to create or return the edit context element for the
	 * creation of a new element of my kind (e.g., when creating a relationship,
	 * the relationship may be owned by the source or target, or some ancestor
	 * of one or the other, or both). By default, returns a command
	 * that returns a <code>null</code> edit context.
	 * Subclasses may override to provide their command.
	 * 
	 * @param req
	 *            the get edit context request
	 * @return the get edit context command
	 */
	protected ICommand getEditContextCommand(GetEditContextRequest req) {
		return new GetEditContextCommand(req);
	}

	/**
	 * Gets the command to destroy a single child of an element of my kind, and
	 * only it. By default, returns a {@link DestroyElementCommand}. Subclasses
	 * may override to provide their own command.
	 * 
	 * @param req
	 *            the destroy request
	 * @return a command that destroys only the element specified as the request's
	 *    {@linkplain DestroyElementRequest#getElementToDestroy() element to destroy}
	 */
	protected ICommand getBasicDestroyElementCommand(DestroyElementRequest req) {
		ICommand result = req.getBasicDestroyCommand();
		
		if (result == null) {
			result = new DestroyElementCommand(req);
		} else {
			// ensure that re-use of this request will not accidentally
			//    propagate this command, which would destroy the wrong object
			req.setBasicDestroyCommand(null);
		}
		
		return result;
	}
	
	/**
	 * Gets the command to destroy a single child of an element of my kind along
	 * with its dependents (not related by containment). By default, returns a
	 * composite that destroys the elements and zero or more dependents.
	 * 
	 * @param req
	 *            the destroy request
	 * @return a command that destroys the element specified as the request's
	 *    {@linkplain DestroyElementRequest#getElementToDestroy() element to destroy}
	 *    and its non-containment dependents
	 */
	protected ICommand getDestroyElementWithDependentsCommand(DestroyElementRequest req) {
		ICommand result = getBasicDestroyElementCommand(req);
		
		// get elements dependent on the element we are destroying, that
		//   must also be destroyed
		DestroyDependentsRequest ddr = (DestroyDependentsRequest) req.getParameter(
				InternalRequestParameters.DESTROY_DEPENDENTS_REQUEST_PARAMETER);
		if (ddr == null) {
			// create the destroy-dependents request that will be propagated to
			//    destroy requests for all elements destroyed in this operation
			ddr = new DestroyDependentsRequest(
				req.getEditingDomain(),
				req.getElementToDestroy(),
				req.isConfirmationRequired());
			ddr.addParameters(req.getParameters());
			ddr.setClientContext(req.getClientContext());
			req.setParameter(
					InternalRequestParameters.DESTROY_DEPENDENTS_REQUEST_PARAMETER,
					ddr);
		} else {
			ddr.setElementToDestroy(req.getElementToDestroy());
		}
		
		IElementType typeToDestroy = ElementTypeRegistry.getInstance().getElementType(
				req.getElementToDestroy());
		
		if (typeToDestroy != null) {
			ICommand command = typeToDestroy.getEditCommand(ddr);
		
			if (command != null) {
				result = result.compose(command);
			}
		}
		
		return result;
	}
	
	/**
	 * Gets the command to destroy a child of an element of my kind. By
	 * default, returns a composite command that destroys the element specified
	 * by the request and all of its contents.
	 * 
	 * @param req
	 *            the destroy request
	 * @return a command that destroys the element specified as the request's
	 *    {@link DestroyElementRequest#getElementToDestroy() element to destroy}
	 *    along with its contents and other dependents
	 */
	protected ICommand getDestroyElementCommand(DestroyElementRequest req) {
		ICommand result = null;
		
		ICommand destroyParent = getDestroyElementWithDependentsCommand(req);
		
		EObject parent = req.getElementToDestroy();
		IElementType parentType = ElementTypeRegistry.getInstance().getElementType(
				parent);

		if (parentType != null) {
			for (Iterator iter = parent.eContents().iterator(); iter.hasNext();) {
				EObject next = (EObject) iter.next();
				
				DestroyDependentsRequest ddr = (DestroyDependentsRequest) req.getParameter(
						InternalRequestParameters.DESTROY_DEPENDENTS_REQUEST_PARAMETER);
				
				// if another object is already destroying this one because it
				//    is (transitively) a dependent, then don't destroy it again
				if ((ddr == null) || !ddr.getDependentElementsToDestroy().contains(next)) {
					// set the element to be destroyed
					req.setElementToDestroy(next);
					
					ICommand command = parentType.getEditCommand(req);
				
					if (command != null) {
						if (result == null) {
							result = command;
						} else {
							result = result.compose(command);
						}
						
						if (!command.canExecute()) {
							// no point in continuing if we're abandoning the works
							break;
						}
					}
				}
			}
		}
		
		// bottom-up destruction:  destroy children before parent
		if (result == null) {
			result = destroyParent;
		} else {
			result = result.compose(destroyParent);
		}
		
		// restore the elementToDestroy in the original request
		req.setElementToDestroy(parent);
		
		return result;
	}

	/**
	 * Gets the command to destroy dependents of an element of my kind. By
	 * default, returns <code>null</code>. Subclasses may override to provide
	 * a command.
	 * 
	 * @param req
	 *            the destroy dependents request
	 * @return a command to destroy dependents, or <code>null</code>
	 */
	protected ICommand getDestroyDependentsCommand(DestroyDependentsRequest req) {
		return null;
	}

	/**
	 * Gets the command to remove a reference from an element of my kind. By
	 * default, returns <code>null</code>. Subclasses may override to provide
	 * their command.
	 * 
	 * @param req
	 *            the destroy reference request
	 * @return the destroy reference command
	 */
	protected ICommand getDestroyReferenceCommand(DestroyReferenceRequest req) {
		return new DestroyReferenceCommand(req);
	}

	/**
	 * Gets the command to duplicate a child in an element of my kind. By
	 * default, returns <code>null</code>. Subclasses may override to provide
	 * their command.
	 * 
	 * @param req
	 *            the duplicate request
	 * @return the duplicate command
	 */
	protected ICommand getDuplicateCommand(DuplicateElementsRequest req) {
		return null;
	}

	/**
	 * Gets the command to move an element into an element of my kind. By
	 * default, returns <code>null</code>. Subclasses may override to provide
	 * their command.
	 * 
	 * @param req
	 *            the move request
	 * @return the move command
	 */
	protected ICommand getMoveCommand(MoveRequest req) {
		return new MoveElementsCommand(req);
	}

	/**
	 * Gets the command to change the source or target of a reference in an
	 * element of my kind. By default, returns <code>null</code>. Subclasses
	 * may override to provide their command.
	 * 
	 * @param req
	 *            the reorient reference request
	 * @return the reorient reference command
	 */
	protected ICommand getReorientReferenceRelationshipCommand(
			ReorientReferenceRelationshipRequest req) {
		return null;
	}

	/**
	 * Gets the command to change the source or target of a relationship in an
	 * element of my kind. By default, returns <code>null</code>. Subclasses
	 * may override to provide their command.
	 * 
	 * @param req
	 *            the reorient relationship request
	 * @return the reorient relationship command
	 */
	protected ICommand getReorientRelationshipCommand(
			ReorientRelationshipRequest req) {
		return null;
	}
}