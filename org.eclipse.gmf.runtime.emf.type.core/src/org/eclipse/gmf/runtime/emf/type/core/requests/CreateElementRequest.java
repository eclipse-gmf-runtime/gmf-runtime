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

package org.eclipse.gmf.runtime.emf.type.core.requests;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypeDebugOptions;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Request to create a new model element.
 * <P>
 * If the request is not constructed with the editing domain through which to
 * create the new model element, it will be derived from the container element.
 * 
 * @author ldamus
 */
public class CreateElementRequest extends AbstractEditCommandRequest {

	/**
	 * The container for the new model element.
	 */
	private EObject container;

	/**
	 * The feature in the container which will hold the new model element. Can
	 * be <code>null</code>, in which case a default feature will be used.
	 */
	private EReference containmentFeature;

	/**
	 * The element type of the new model element.
	 */
	private final IElementType elementType;

	/**
	 * The new model element. Will be <code>null</code> until the new element
	 * has been created. Once the new element has been created, it will be set
	 * so that 'after' advice can further manipulate the new element.
	 */
	private EObject newElement;

	/**
	 * The edit context command.
	 */
	private ICommand editContextCommand;

	/**
	 * The edit context request.
	 */
	private GetEditContextRequest editContextRequest;

	/**
	 * Creates a request to create a new model element.
	 * 
	 * @param editingDomain
	 *            the editing domain in which I am requesting to make model
	 *            changes.
	 * @param container
	 *            the container for the new model element
	 * @param elementType
	 *            the element type of the new model element
	 */
	public CreateElementRequest(TransactionalEditingDomain editingDomain,
			EObject container, IElementType elementType) {

		this(editingDomain, container, elementType, null);
	}
    
    /**
     * Creates a request to create a new model element. The editing domain will
     * be derived from the <code>container</code>.
     * 
     * @param container
     *            the container for the new model element
     * @param elementType
     *            the element type of the new model element
     */
    public CreateElementRequest(EObject container, IElementType elementType) {
        
        this(TransactionUtil.getEditingDomain(container), container, elementType, null);
    }
    
	/**
     * Creates a request to create a new model element. The editing domain will
     * be derived from the result of {@link #getContainer()}.
     * 
     * @param elementType
     *            the element type of the new model element
     */
    public CreateElementRequest(IElementType elementType) {

        this(null, null, elementType, null);
    }
    
    /**
     * Creates a request to create a new model element.
     * 
     * @param editingDomain
     *            the editing domain in which I am requesting to make model
     *            changes.
     * @param elementType
     *            the element type of the new model element
     */
    public CreateElementRequest(TransactionalEditingDomain editingDomain,
            IElementType elementType) {

        this(editingDomain, null, elementType, null);
    }

	/**
	 * Creates a request to create a new model element.
	 * 
	 * @param editingDomain
	 *            the editing domain in which I am requesting to make model
	 *            changes.
	 * @param container
	 *            the container for the new model element
	 * @param elementType
	 *            the element type of the new model element
	 * @param containmentFeature
	 *            The feature in the container which will hold the new model
	 *            element. Can be <code>null</code>, in which case a default
	 *            feature will be used.
	 */
	public CreateElementRequest(TransactionalEditingDomain editingDomain,
			EObject container, IElementType elementType,
			EReference containmentFeature) {

		super(editingDomain);
		this.container = container;
		this.elementType = elementType;
		this.containmentFeature = containmentFeature;
	}
    
    /**
     * Creates a request to create a new model element.  The editing domain will
     * be derived from the <code>container</code>.
     * 
     * @param container
     *            the container for the new model element
     * @param elementType
     *            the element type of the new model element
     * @param containmentFeature
     *            The feature in the container which will hold the new model
     *            element. Can be <code>null</code>, in which case a default
     *            feature will be used.
     */
    public CreateElementRequest(EObject container, IElementType elementType,
            EReference containmentFeature) {

        this(TransactionUtil.getEditingDomain(container), container,
            elementType, containmentFeature);
    }

	/**
	 * Gets the new element that has been created by this request.
	 * 
	 * @return the newly created element
	 */
	public EObject getNewElement() {
		return newElement;
	}

	/**
	 * Sets the element that has been created by this request.
	 * 
	 * @param element
	 *            the newly created element
	 */
	public void setNewElement(EObject element) {
		this.newElement = element;
	}

	/**
	 * Gets the containment feature in which to create the new element. May be
	 * <code>null</code>.
	 * 
	 * @return the containment feature or <code>null</code> if one has not
	 *         been specified.O
	 */
	public EReference getContainmentFeature() {
		return containmentFeature;
	}

	/**
	 * Sets the containment feature in which to create the new element.
	 * <p>
	 * Does nothing of the feature has not changed. Othewise, invalidates the
	 * edit helper context.
	 * 
	 * @param containmentFeature
	 *            the containment feature
	 */
	public void setContainmentFeature(EReference containmentFeature) {
		if (this.containmentFeature != containmentFeature) {
			this.containmentFeature = containmentFeature;
			invalidateEditHelperContext();
		}
	}

	/**
	 * Gets the original context in which the new element will be created. This
	 * may not be the actual container of the new element until the
	 * <code>createContainer</code> method is called.
	 * 
	 * @return the container for the new element.
	 */
	public EObject getContainer() {
		return container;
	}

	/**
	 * Sets the appropriate <code>container</code> for the new element. May
	 * prompt the user to create the container, so this method should only be
	 * called when the command honouring the request is executed.
	 * <P>
	 * Updates the value returned by the <code>getContainer</code>, if
	 * appropriate.
	 * 
	 * @return the container for the new element
	 */
	public EObject createContainer() {

		Object result = null;

		ICommand contextCommand = getEditContextCommand();

		if (contextCommand != null && contextCommand.canExecute()) {
            try {
                contextCommand.execute(new NullProgressMonitor(), null);

                CommandResult commandResult = contextCommand.getCommandResult();

                if (commandResult.getStatus().getCode() == IStatus.OK) {
                    result = commandResult.getReturnValue();
                }
            } catch (ExecutionException e) {
                Trace.catching(EMFTypePlugin.getPlugin(),
                    EMFTypeDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                    "createContainer", e); //$NON-NLS-1$
                Log.error(EMFTypePlugin.getPlugin(),
                    EMFTypePluginStatusCodes.COMMAND_FAILURE, e
                        .getLocalizedMessage(), e);
            }
		}
		if (result == null || result instanceof EObject) {
			container = (EObject) result;
		}
		return getContainer();
	}

	/**
	 * Gets a command that will return the edit context for the request.
	 * 
	 * @return the command
	 */
	private ICommand getEditContextCommand() {

		if (editContextCommand == null) {
			editContextCommand = getElementType().getEditCommand(
					getEditContextRequest());
		}
		return editContextCommand;
	}

	/**
	 * Gets the edit context request.
	 * 
	 * @return the edit context request
	 */
	private GetEditContextRequest getEditContextRequest() {

		if (editContextRequest == null) {
			editContextRequest = new GetEditContextRequest(getEditingDomain(), this,
					getElementType());
			// Initialize the context with the container
			editContextRequest.setEditContext(getContainer());
		}
		return editContextRequest;
	}

	/**
	 * Sets the container for the new element.
	 * <p>
	 * Does nothing of the container has not changed. Othewise, invalidates the
	 * edit helper context and containment feature.
	 * 
	 * @param container
	 *            the container for the new element.
	 */
	public void setContainer(EObject container) {
		if (this.container != container) {
			this.container = container;
			invalidateEditHelperContext();
			invalidateContainmentFeature();
		}
	}

	/**
	 * Gets the element type for the new model element.
	 * 
	 * @return the element type
	 */
	public IElementType getElementType() {
		return elementType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
	 */
	public List getElementsToEdit() {
		if (getContainer() != null) {
			return Collections.singletonList(getContainer());
		}
		return super.getElementsToEdit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditCommandRequest#getDefaultLabel()
	 */
	protected String getDefaultLabel() {

		String label = MessageFormat.format(
				EMFTypeCoreMessages.Request_Label_Create,
				new Object[] { getElementType().getDisplayName() });

		return label;
	}

	/**
	 * Makes a request for the context of the new element from its element type.
	 * Allows specializations and advice bindings to return the appropriate
	 * context, if necessary.
	 * <P>
	 * The <code>GetContextCommand</code> will not be executed. This method
	 * relies on the command to return the new context element type until it is
	 * executed, at which point it can return the real context element, if a new
	 * one needs to be created.
	 * 
	 * @return the edit helper context
	 */
	public Object getEditHelperContext() {

		// Get, but don't execute the command. Gives clients a chance to set the
		// edit context in the request, if they need to.
		ICommand contextCommand = getEditContextCommand();

		// The request should now have the correct edit context.
		if (contextCommand != null && contextCommand.canExecute()) {
			return getEditContextRequest().getEditContext();
		}
		return null;
	}

	public void setParameter(String parameterName, Object value) {
		super.setParameter(parameterName, value);
	}

	/**
	 * Invalidates the cached edit context request and command.
	 */
	protected void invalidateEditHelperContext() {
		editContextCommand = null;
		editContextRequest = null;
	}

	/**
	 * Invalidates the cached containment feature.
	 */
	protected void invalidateContainmentFeature() {
		containmentFeature = null;
	}
    
    public TransactionalEditingDomain getEditingDomain() {
        TransactionalEditingDomain result = super.getEditingDomain();

        if (result == null) {
            result = TransactionUtil.getEditingDomain(getContainer());
        }
        return result;
    }
}