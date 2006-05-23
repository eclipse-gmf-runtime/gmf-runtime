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

package org.eclipse.gmf.runtime.emf.type.core.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * Command to create a new model element.
 * 
 * @author ldamus
 */
public class CreateElementCommand extends EditElementCommand {

	/**
	 * The newly created element.
	 */
	private EObject newElement;

	/**
	 * The element type to be created.
	 */
	private final IElementType elementType;

	/**
	 * The containment feature in which the new element will be created.
	 */
	private EReference containmentFeature;

	/**
	 * Constructs a new element creation command for the <code>request</code>.
	 * 
	 * @param request
	 *            the element creation request
	 */
	public CreateElementCommand(CreateElementRequest request) {

		super(request.getLabel(), null, request);

		elementType = request.getElementType();

		containmentFeature = request.getContainmentFeature();
	}

	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
            IAdaptable info)
        throws ExecutionException {

        // Do the default element creation
        newElement = doDefaultElementCreation();

        // Configure the new element
        ConfigureRequest configureRequest = createConfigureRequest();

        ICommand configureCommand = elementType
            .getEditCommand(configureRequest);
        
        IStatus configureStatus = null;
        
        if (configureCommand != null && configureCommand.canExecute()) {
        	configureStatus = configureCommand.execute(monitor, info);
        }

        // Put the newly created element in the request so that the
        // 'after' commands have access to it.
        getCreateRequest().setNewElement(newElement);

        return (configureStatus == null) ? 
        		CommandResult.newOKCommandResult(newElement) : 
        		new CommandResult(configureStatus, newElement);
	}

	/**
	 * Creates the request to configure the new element.
	 * 
	 * @return the request
	 */
	protected ConfigureRequest createConfigureRequest() {

		ConfigureRequest configureRequest = new ConfigureRequest(
            getEditingDomain(), newElement, getElementType());
        
		// pass along the client context
		configureRequest.setClientContext(getCreateRequest().getClientContext());
		
        configureRequest.addParameters(getRequest().getParameters());

		return configureRequest;
	}

	/**
	 * Subclasses should implement this to override the basic element creation.
	 * 
	 * @return the new model element that has been created
	 */
	protected EObject doDefaultElementCreation() {
		EReference containment = getContainmentFeature();
		EClass eClass = getElementType().getEClass();

		if (containment != null) {
			EObject element = getElementToEdit();

			if (element != null)
				return EMFCoreUtil.create(element, containment, eClass);
		}

		return null;
	}

	/**
	 * Initializes the container of the new element by asking the create to
	 * create the container, if necessary.
	 */
	protected EObject getElementToEdit() {

		if (super.getElementToEdit() == null) {
			CreateElementRequest request = (CreateElementRequest) getRequest();
			setElementToEdit(request.createContainer());
		}
		return super.getElementToEdit();
	}

	/**
	 * Gets the EClass of the element to be edited.
	 * 
	 * @return the EClass
	 */
	protected EClass getEClassToEdit() {

		CreateElementRequest request = (CreateElementRequest) getRequest();

		Object context = request.getEditHelperContext();

		if (context instanceof EObject) {
			return ((EObject) context).eClass();

		} else {
			IElementType type = ElementTypeRegistry.getInstance()
					.getElementType(context);

			if (type != null) {
				return type.getEClass();
			}
		}
		return null;
	}

	/**
	 * Gets the containment feature for the new element.
	 * 
	 * @return the containment feature
	 */
	protected EReference getContainmentFeature() {

		if (containmentFeature == null) {
			EClass classToEdit = getEClassToEdit();

			if (classToEdit != null) {
				IElementType type = getElementType();

				if (type != null && type.getEClass() != null) {
					containmentFeature = PackageUtil.findFeature(classToEdit,
							type.getEClass());
				}
			}
		}

		return containmentFeature;
	}

	/**
	 * Sets the containment feature for the new element.
	 * 
	 * @param containmentFeature
	 *            the containment feature for the new element
	 */
	protected void setContainmentFeature(EReference containmentFeature) {
		this.containmentFeature = containmentFeature;
	}

	/**
	 * Gets the element type for the new element.
	 * 
	 * @return the element type
	 */
	protected IElementType getElementType() {
		return elementType;
	}

	/**
	 * Gets the create request.
	 * 
	 * @return the create request
	 */
	public CreateElementRequest getCreateRequest() {
		return (CreateElementRequest) getRequest();
	}

	public boolean canExecute() {

		if (getEClassToEdit() == null) {
			return false;
		}

		if (getContainmentFeature() != null) {
			EClassifier eClassifier = getContainmentFeature().getEType();
			boolean result = true;

			if (eClassifier instanceof EClass) {
				result = ((EClass) eClassifier).isSuperTypeOf(getElementType()
						.getEClass());
			}

			result = result
					&& PackageUtil.canContain(getEClassToEdit(),
							getContainmentFeature(), getElementType()
									.getEClass(), false);

			return result && super.canExecute();
		}
		return false;
	}

	/**
	 * Gets the new element that was created by this command. Will be
	 * <code>null</code> if the command has not been executed.
	 * 
	 * @return the new element
	 */
	public EObject getNewElement() {
		return newElement;
	}

}