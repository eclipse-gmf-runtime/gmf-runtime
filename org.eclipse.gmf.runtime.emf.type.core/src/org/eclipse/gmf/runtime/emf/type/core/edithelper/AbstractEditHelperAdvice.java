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

import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypeDebugOptions;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
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
 * Abstract superclass for edit helper advice classes that provide 'before' and
 * 'after' advice for modifying model elements.
 * <P>
 * Subclasses can override the implementation for only the specific methods for
 * the kinds of requests that they provide advice for. For convenience, these
 * methods all return <code>null</code> by default.
 * <P>
 * Edit helper advice can be registered against one or more element types using
 * the <code>org.eclipse.gmf.runtime.emf.type.core.elementTypes</code>
 * extension point.
 * 
 * @author ldamus
 */
public class AbstractEditHelperAdvice implements IEditHelperAdvice {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice#getBeforeEditCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest)
	 */
	public ICommand getBeforeEditCommand(IEditCommandRequest request) {

		if (request instanceof CreateRelationshipRequest) {
			return getBeforeCreateRelationshipCommand((CreateRelationshipRequest) request);

		} else if (request instanceof CreateElementRequest) {
			return getBeforeCreateCommand((CreateElementRequest) request);

		} else if (request instanceof ConfigureRequest) {
			return getBeforeConfigureCommand((ConfigureRequest) request);

		} else if (request instanceof DestroyElementRequest) {
			return getBeforeDestroyElementCommand((DestroyElementRequest) request);

		} else if (request instanceof DestroyDependentsRequest) {
			return getBeforeDestroyDependentsCommand((DestroyDependentsRequest) request);

		} else if (request instanceof DestroyReferenceRequest) {
			return getBeforeDestroyReferenceCommand((DestroyReferenceRequest) request);

		} else if (request instanceof DuplicateElementsRequest) {
			return getBeforeDuplicateCommand((DuplicateElementsRequest) request);

		} else if (request instanceof GetEditContextRequest) {
			return getBeforeEditContextCommand((GetEditContextRequest) request);

		} else if (request instanceof MoveRequest) {
			return getBeforeMoveCommand((MoveRequest) request);

		} else if (request instanceof ReorientReferenceRelationshipRequest) {
			return getBeforeReorientReferenceRelationshipCommand((ReorientReferenceRelationshipRequest) request);

		} else if (request instanceof ReorientRelationshipRequest) {
			return getBeforeReorientRelationshipCommand((ReorientRelationshipRequest) request);

		} else if (request instanceof SetRequest) {
			return getBeforeSetCommand((SetRequest) request);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice#getAfterEditCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest)
	 */
	public ICommand getAfterEditCommand(IEditCommandRequest request) {

		if (request instanceof CreateRelationshipRequest) {
			return getAfterCreateRelationshipCommand((CreateRelationshipRequest) request);

		} else if (request instanceof CreateElementRequest) {
			return getAfterCreateCommand((CreateElementRequest) request);

		} else if (request instanceof ConfigureRequest) {
			return getAfterConfigureCommand((ConfigureRequest) request);

		} else if (request instanceof DestroyElementRequest) {
			return getAfterDestroyElementCommand((DestroyElementRequest) request);

		} else if (request instanceof DestroyDependentsRequest) {
			return getAfterDestroyDependentsCommand((DestroyDependentsRequest) request);

		} else if (request instanceof DestroyReferenceRequest) {
			return getAfterDestroyReferenceCommand((DestroyReferenceRequest) request);

		} else if (request instanceof DuplicateElementsRequest) {
			return getAfterDuplicateCommand((DuplicateElementsRequest) request);

		} else if (request instanceof GetEditContextRequest) {
			return getAfterEditContextCommand((GetEditContextRequest) request);

		} else if (request instanceof MoveRequest) {
			return getAfterMoveCommand((MoveRequest) request);

		} else if (request instanceof ReorientReferenceRelationshipRequest) {
			return getAfterReorientReferenceRelationshipCommand((ReorientReferenceRelationshipRequest) request);

		} else if (request instanceof ReorientRelationshipRequest) {
			return getAfterReorientRelationshipCommand((ReorientRelationshipRequest) request);

		} else if (request instanceof SetRequest) {
			return getAfterSetCommand((SetRequest) request);
		}

		return null;
	}

	/**
	 * Gets my 'before' advice for creating the new relationship.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeCreateRelationshipCommand(
			CreateRelationshipRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for creating the new relationship.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterCreateRelationshipCommand(
			CreateRelationshipRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for creating the new element.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeCreateCommand(CreateElementRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for creating the new element.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterCreateCommand(CreateElementRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for configuring a new element.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeConfigureCommand(ConfigureRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for configuring a new element.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for destroying an element.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeDestroyElementCommand(
			DestroyElementRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for destroying an element.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterDestroyElementCommand(
			DestroyElementRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for destroying the dependents of an element that
	 * is being destroyed.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeDestroyDependentsCommand(
			DestroyDependentsRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for destroying the dependents of an element that
	 * is being destroyed.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterDestroyDependentsCommand(
			DestroyDependentsRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for destroying a reference.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeDestroyReferenceCommand(
			DestroyReferenceRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for destroying an reference.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterDestroyReferenceCommand(
			DestroyReferenceRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for duplicating an element.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeDuplicateCommand(
			DuplicateElementsRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for duplicating an element.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterDuplicateCommand(DuplicateElementsRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for getting the edit context for an edit request.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeEditContextCommand(GetEditContextRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for getting the edit context for an edit request.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterEditContextCommand(GetEditContextRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for moving an element into a new container.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeMoveCommand(MoveRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for moving an element into a new container.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterMoveCommand(MoveRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for changing the source or target of a reference
	 * relationship.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeReorientReferenceRelationshipCommand(
			ReorientReferenceRelationshipRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for changing the source or target of a reference
	 * relationship.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterReorientReferenceRelationshipCommand(
			ReorientReferenceRelationshipRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for changing the source or target of a
	 * relationship.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeReorientRelationshipCommand(
			ReorientRelationshipRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for changing the source or target of a
	 * relationship.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterReorientRelationshipCommand(
			ReorientRelationshipRequest request) {
		return null;
	}

	/**
	 * Gets my 'before' advice for setting the value of a feature in an element.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute before the edit helper work is done
	 */
	protected ICommand getBeforeSetCommand(SetRequest request) {
		return null;
	}

	/**
	 * Gets my 'after' advice for setting the value of a feature in an element.
	 * 
	 * @param request
	 *            the request
	 * @return the command to execute after the edit helper work is done
	 */
	protected ICommand getAfterSetCommand(SetRequest request) {
		return null;
	}

	/**
	 * Convenience method to create a new element of kind <codeO>typeToCreate</code>
	 * in the context of <code>container</code>.
	 * 
	 * @param container
	 *            the container element
	 * @param typeToCreate
	 *            the kind of element to create
	 * @return the newly created element, or <code>null</code> if it wasn't
	 *         created
	 */
	protected EObject createType(EObject container, IElementType typeToCreate,
            IProgressMonitor progressMonitor) {
        
        return createType(container, typeToCreate, null, progressMonitor);
    }

	/**
	 * Convenience method to create a new element of kind <codeO>typeToCreate</code>
	 * in the context of <code>container</code>.
	 * 
	 * @param container
	 *            the container element
	 * @param typeToCreate
	 *            the kind of element to create
	 * @param requestParameters
	 *            parameters to be set in the creation request
	 * @return the newly created element, or <code>null</code> if it wasn't
	 *         created
	 */
	protected EObject createType(EObject container, IElementType typeToCreate,
            Map requestParameters, IProgressMonitor progressMonitor) {

		if (typeToCreate.getEClass().isAbstract()) {
			return null;
		}
        
        TransactionalEditingDomain editingDomain = TransactionUtil
            .getEditingDomain(container);

		CreateElementRequest request = new CreateElementRequest(editingDomain,
				container, typeToCreate);

		if (requestParameters != null) {
			// Set the request parameters
			request.addParameters(requestParameters);
		}

		IElementType containerElementType = ElementTypeRegistry.getInstance()
				.getElementType(request.getEditHelperContext());
		ICommand createTypeCommand = containerElementType
				.getEditCommand(request);

		if (createTypeCommand != null && createTypeCommand.canExecute()) {
            
            try {
                createTypeCommand.execute(progressMonitor, null);
                
            } catch (ExecutionException e) {
                Trace.catching(EMFTypePlugin.getPlugin(),
                    EMFTypeDebugOptions.EXCEPTIONS_CATCHING,
                    AbstractEditHelperAdvice.class, "createType", e); //$NON-NLS-1$
                Log.error(EMFTypePlugin.getPlugin(),
                    EMFTypePluginStatusCodes.COMMAND_FAILURE, e
                        .getMessage(), e);
                return null;
            }

            if (createTypeCommand.getCommandResult().getStatus().isOK()) {
                return (EObject) createTypeCommand.getCommandResult()
                    .getReturnValue();
            }
        }
		return null;
	}
    
    /**
     * Does nothing by default. Subclasses should override if they want to add,
     * modify or remove parameters in the request.
     */
    public void configureRequest(IEditCommandRequest request) {
        // does nothing, by default
    }
    
    /**
     * Returns <code>true</code> by default. Subclasses should override if
     * they want to give a different answer.
     */
    public boolean approveRequest(IEditCommandRequest request) {
        return true;
    }
}