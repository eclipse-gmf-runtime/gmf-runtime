/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.requests;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.EditHelperContext;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.internal.InternalRequestParameters;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Request to destroy the dependents of a model element.  It is expected that
 * the request will be served primarily by advice, which can invoke the
 * {@link #getDestroyDependentCommand(EObject)} to obtain a command to destroy
 * an object dependent on the {@linkplain #getElementToDestroy() element being destroyed}
 * and have it added to the edit command.  However, an edit helper can also
 * provide an "instead" command by overriding its
 * <code>getDestroyDependentsCommand(DestroyDependentsRequest)</code> method
 * to do the same.
 * <p>
 * The destruction of dependents is an edit requested of the element being
 * destroyed, unlike the {@link DestroyElementRequest}, which is requested of
 * the <em>container</em> of the element being destroyed.
 * </p>
 * 
 * @author Christian W. Damus (cdamus)
 */
public class DestroyDependentsRequest extends DestroyRequest {

	/**
	 * The element to destroy.
	 */
	private EObject elementToDestroy;
	
	/**
	 * Other objects dependent on the primary object that should also be destroyed.
	 */
	private final Set dependentElementsToDestroy = new java.util.HashSet();
	private final Set immutableViewOfDependents = Collections.unmodifiableSet(dependentElementsToDestroy);
	
	/**
	 * Constructs a new request to destroy the dependents of a model element.
	 * 
	 * @param editingDomain
	 *            the editing domain in which I am requesting to make model
	 * @param elementToDestroy
	 *            the element to be destroyed
	 * @param confirmationRequired
	 *            <code>true</code> if the user should be prompted to confirm
	 *            the element deletion, <code>false</code> otherwise.
	 */
	public DestroyDependentsRequest(TransactionalEditingDomain editingDomain,
			EObject elementToDestroy, boolean confirmationRequired) {

		super(editingDomain, confirmationRequired);
		this.elementToDestroy = elementToDestroy;
	}
    
	/**
	 * Gets the element to be destroyed.
	 * 
	 * @return the element to be destroyed
	 */
	public final EObject getElementToDestroy() {
		return elementToDestroy;
	}

	/**
	 * Sets the element to be destroyed.
	 * 
	 * @param elementToDestroy
	 *            the element to be destroyed
	 */
	public final void setElementToDestroy(EObject elementToDestroy) {
		this.elementToDestroy = elementToDestroy;
	}

	public EObject getContainer() {
		if (getElementToDestroy() != null) {
			return getElementToDestroy().eContainer();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
	 */
	public List getElementsToEdit() {
		if (getElementToDestroy() != null) {
			return Collections.singletonList(getElementToDestroy());
		}
		return super.getElementsToEdit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditCommandRequest#getDefaultLabel()
	 */
	protected String getDefaultLabel() {
		return EMFTypeCoreMessages.Request_Label_DestroyDependents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getEditHelperContext()
	 */
	public Object getEditHelperContext() {
		IClientContext context = getClientContext();
		
		if (context == null) {
			return getElementToDestroy();
		} else {
			return new EditHelperContext(getElementToDestroy(), context);
		}
	}

    /**
     * Derives the editing domain from the object to be destroyed, if it hasn't
     * already been specified.
     */
    public TransactionalEditingDomain getEditingDomain() {
        TransactionalEditingDomain result = super.getEditingDomain();

        if (result == null) {
            result = TransactionUtil.getEditingDomain(getElementToDestroy());
        }
        return result;
    }

    /**
     * Obtains the <em>mutable</em> set of dependent elements to destroy.
     * 
     * @return the set of dependent elements
     */
    protected final Set internalGetDependentElementsToDestroy() {
    	return dependentElementsToDestroy;
    }
    
    /**
     * Obtains an immutable view of the set of dependent elements to destroy.
     * 
     * @return the immutable set of dependent elements
     */
    public final Set getDependentElementsToDestroy() {
    	return immutableViewOfDependents;
    }
	
    /**
     * Obtains a command that destroys the specified <code>dependent</code> of
     * the {@linkplain #getElementToDestroy() element to be destroyed}, if it
     * is not already being destroyed by the processing of the current
     * {@link DestroyElementRequest}.  This command can then be composed with
     * others by the edit helper processing the <code>DestroyDependentsRequest</code>.
     * 
     * @param dependent an object dependent on the element being destroyed,
     *     which must also be destroyed
     *     
     * @return a command to destroy the <code>dependent</code>, or
     *     <code>null</code> if the element is already being destroyed
     *     
     * @throws IllegalArgumentException on an attempt to destroy the
     *    {@linkplain #getElementToDestroy() element to be destroyed} (as a
     *    dependent of itself)
     * @throws NullPointerException on attempt to destroy a <code>null</code>
     *    dependent
     *    
     * @see #getDestroyDependentsCommand(Collection)
     */
	public ICommand getDestroyDependentCommand(EObject dependent) {
		ICommand result = null;
		
		if (addDependentElementToDestroy(dependent)) {
			// record the element that we are destroying, for later restoration
			EObject elementBeingDestroyed = getElementToDestroy();
			
			try {
				DestroyElementRequest destroy = new DestroyElementRequest(
						getEditingDomain(),
						dependent,
						isConfirmationRequired());
				
				// propagate my parameters
				destroy.addParameters(getParameters());
				
				// propagate the dependents information to detect cycles
				destroy.setParameter(
						InternalRequestParameters.DESTROY_DEPENDENTS_REQUEST_PARAMETER,
						this);
				setElementToDestroy(dependent);
				
				IElementType context = ElementTypeRegistry.getInstance().getElementType(
						destroy.getEditHelperContext());
				
				if (context != null) {
					result = context.getEditCommand(destroy);
				}
			} finally {
				// restore the element that we are destroying
				setElementToDestroy(elementBeingDestroyed);
			}
		}
		
		return result;
	}
	
	/**
     * Obtains a command that destroys the specified <code>dependents</code> of
     * the {@linkplain #getElementToDestroy() element to be destroyed}, if they
     * are not already being destroyed by the processing of the current
     * {@link DestroyElementRequest}.  This command can then be composed with
     * others by the edit helper processing the <code>DestroyDependentsRequest</code>.
	 * 
	 * @param dependents dependents of the element being destroyed
	 * 
	 * @return a command to destroy all of the specified <code>dependents</code>,
	 *     or <code>null</code> if they are all already being destroyed
	 *     
	 * @see #getDestroyDependentCommand(EObject)
	 */
	public ICommand getDestroyDependentsCommand(Collection dependents) {
		ICommand result = null;
		
        for (Iterator i = dependents.iterator(); i.hasNext();) {
        	result = CompositeCommand.compose(
        			result,
        			getDestroyDependentCommand((EObject) i.next()));
        }
        
        return result;
	}
   
    /**
     * Indicates that the command that fulfils this request will also destroy
     * the specified dependent of the
     * {@link #getElementToDestroy() element to be destroyed}.  Note that
     * contained elements are implicitly considered to be dependent; they need
     * not be handled by this mechanism.
     * <p>
     * Advice that provides a command to destroy a dependent element
     * <em>must</em> indicate that fact by calling this method (only after
     * checking whether it isn't already
     * {@link #isElementToBeDestroyed(EObject) being destroyed}, anyway).
     * </p>
     *  
     * @param dependent another object to destroy, which is dependent on the
     *     element for which we are requesting destruction
     * 
     * @return <code>true</code> if the <code>dependent</code> was not already
     *     in the set of elements being destroyed; <code>false</code>, otherwise
     * 
     * @throws IllegalArgumentException on an attempt to add the
     *    {@link #getElementToDestroy() element to be destroyed} as a dependent
     *    of itself
     * @throws NullPointerException on attempt to add a <code>null</code> object
     *    
     * @see #isElementToBeDestroyed(EObject)
     * @see #getElementToDestroy()
     * @see #getDestroyDependentCommand(EObject)
     */
    protected boolean addDependentElementToDestroy(EObject dependent) {
    	if (dependent == null) {
    		throw new NullPointerException("dependent is null"); //$NON-NLS-1$
    	}
    	
    	if (dependent == getElementToDestroy()) {
    		throw new IllegalArgumentException("dependent is the element being destroyed"); //$NON-NLS-1$
    	}
    	
    	if (!isElementToBeDestroyed(dependent)) {
    		return internalGetDependentElementsToDestroy().add(dependent);
    	}
    	
    	return false;
    }
    
    /**
     * Queries whether the specified element will be destroyed as a result of
     * the fulfillment of this request.  An element will be destroyed if it
     * or any of its ancestors is the
     * {@linkplain #getElementToDestroy() element to be destroyed} or one of its
     * {@linkplain #getDependentElementsToDestroy() dependents}.
     * 
     * @param eObject an element
     * 
     * @return <code>true</code> if the command that fulfils this request
     *     would destroy the specified element; <code>false</code> if a new
     *     command would have to be composed with it to destroy the element
     */
    protected boolean isElementToBeDestroyed(EObject eObject) {
    	boolean result = false;
    	
    	while (!(result || (eObject == null))) {
    		result = (eObject == getElementToDestroy())
    			|| internalGetDependentElementsToDestroy().contains(eObject);
    		
    		eObject = eObject.eContainer();
    	}
    	
    	return result;
    }
}