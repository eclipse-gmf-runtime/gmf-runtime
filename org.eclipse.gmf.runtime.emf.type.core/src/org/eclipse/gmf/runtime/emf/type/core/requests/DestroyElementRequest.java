/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.requests;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextManager;
import org.eclipse.gmf.runtime.emf.type.core.EditHelperContext;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.DefaultMetamodelType;

/**
 * Request to destroy a model element.
 * 
 * @author ldamus
 */
public class DestroyElementRequest extends DestroyRequest {
	
	/**
	 * Request parameter specifying the associated destroy-dependents request
	 * that is propagated recursively through the processing of destroy
	 * requests for an element, its contents, and its dependents.
	 * <p>
	 * The expected parameter value is an instance of type {@link DestroyDependentsRequest}. 
	 * </p>
	 */
	public static final String DESTROY_DEPENDENTS_REQUEST_PARAMETER = "DestroyElementRequest.destroyDependentsRequest"; //$NON-NLS-1$
	
	/**
	 * Request parameter specifying the initial element that was requested to be
	 * destroyed.
	 * <P>
	 * The expected parameter value is an instance of type {@link EObject}, or
	 * <code>null</code>.
	 */
	public static final String INITIAL_ELEMENT_TO_DESTROY_PARAMETER = "DestroyElementRequest.initialElementToDestroy"; //$NON-NLS-1$

	/**
	 * The element to destroy.
	 */
	private EObject elementToDestroy;
	
	/**
	 * A command to override the basic destroy command that would be created by
	 * default by the edit helper to perform the object destruction.
	 */
	private DestroyElementCommand basicDestroyCommand;

	/**
	 * Constructs a new request to destroy a model element.
	 * 
	 * @param editingDomain
	 *            the editing domain in which I am requesting to make model
	 * @param confirmationRequired
	 *            <code>true</code> if the user should be prompted to confirm
	 *            the element deletion, <code>false</code> otherwise.
	 */
	public DestroyElementRequest(TransactionalEditingDomain editingDomain,
			boolean confirmationRequired) {

		this(editingDomain, null, confirmationRequired);
	}

	/**
	 * Constructs a new request to destroy a model element.
	 * 
	 * @param editingDomain
	 *            the editing domain in which I am requesting to make model
	 * @param elementToDestroy
	 *            the element to be destroyed
	 * @param confirmationRequired
	 *            <code>true</code> if the user should be prompted to confirm
	 *            the element deletion, <code>false</code> otherwise.
	 */
	public DestroyElementRequest(TransactionalEditingDomain editingDomain,
			EObject elementToDestroy, boolean confirmationRequired) {

		super(editingDomain, confirmationRequired);
		this.elementToDestroy = elementToDestroy;
	}
    
    /**
     * Constructs a new request to destroy a model element. The editing domain will
     * be derived from the result of {@link #getElementToDestroy()}.
     * 
     * @param confirmationRequired
     *            <code>true</code> if the user should be prompted to confirm
     *            the element deletion, <code>false</code> otherwise.
     */
    public DestroyElementRequest(boolean confirmationRequired) {

        this(null, null, confirmationRequired);
    }
    
    /**
     * Constructs a new request to destroy a model element.
     * 
     * @param elementToDestroy
     *            the element to be destroyed
     * @param confirmationRequired
     *            <code>true</code> if the user should be prompted to confirm
     *            the element deletion, <code>false</code> otherwise.
     */
    public DestroyElementRequest(EObject elementToDestroy,
            boolean confirmationRequired) {

        this(TransactionUtil.getEditingDomain(elementToDestroy), elementToDestroy,
                confirmationRequired);
    }

	/**
	 * Gets the element to be destroyed.
	 * 
	 * @return the element to be destroyed
	 */
	public EObject getElementToDestroy() {
		return elementToDestroy;
	}

	/**
	 * Sets the element to be destroyed.
	 * 
	 * @param elementToDestroy
	 *            the element to be destroyed
	 */
	public void setElementToDestroy(EObject elementToDestroy) {
		this.elementToDestroy = elementToDestroy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.DestroyRequest#getContainer()
	 */
	public EObject getContainer() {
		if (getElementToDestroy() != null) {
			return getElementToDestroy().eContainer();
		}
		return null;
	}
	
	/**
	 * Extends the inherited method to return the default element type when
	 * destroying a root element of a resource.
	 */
	public Object getEditHelperContext() {
		Object result = super.getEditHelperContext();
		
		if (result == null) {
			EObject element = getElementToDestroy();
			
			if ((element != null) && (element.eResource() != null)) {
				IClientContext context = ClientContextManager.getInstance()
						.getClientContextFor(element);
				result = new EditHelperContext(DefaultMetamodelType
						.getInstance(), context);
			}
		}
		
		return result;
	}

    /**
     * Derives the editing domain from the object to be destroyed, if it hasn't
     * already been specified.
     */
    public TransactionalEditingDomain getEditingDomain() {
        TransactionalEditingDomain result = super.getEditingDomain();

        if (result == null) {
            result = TransactionUtil.getEditingDomain(getElementToDestroy());
            if (result != null) {
				setEditingDomain(result);
			}
        }
        return result;
    }

    /**
	 * Assigns a command to override the basic destroy command that would be created
	 * by default by the edit helper to perform the object destruction.  This
	 * can be used by before advice to replace the basic destruction behaviour.
	 * <p>
	 * This is similar to the facility provided via the
	 * {@link IEditCommandRequest#REPLACE_DEFAULT_COMMAND} parameter for
	 * indicating that an advice has taken over the "instead" command, except
	 * that this is applies only to the basic single-object destruction, whereas
	 * the edit helper's command also performs recursion and destruction of
	 * dependents.
	 * </p>
	 * 
     * @param command the basic destroy command to use for destruction of an
     *     element, or <code>null</code> to use the edit helper's default
     *     implementation
     *     
     * @see AbstractEditHelper#getBasicDestroyElementCommand(DestroyElementRequest)
     */
    public void setBasicDestroyCommand(DestroyElementCommand command) {
    	basicDestroyCommand = command;
    }

    /**
	 * Obtains a command to override the basic destroy command that would be created
	 * by default by the edit helper to perform the object destruction.
	 * 
     * @return the basic destroy command to use for destruction of an
     *     element, or <code>null</code> to use the edit helper's default
     *     implementation
     * 
     * @see #setBasicDestroyCommand(DestroyElementCommand)
     * @see AbstractEditHelper#getBasicDestroyElementCommand(DestroyElementRequest)
     */
    public DestroyElementCommand getBasicDestroyCommand() {
    	return basicDestroyCommand;
    }
}