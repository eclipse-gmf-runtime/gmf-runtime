/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.commands;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * Command to create a new model element.
 * 
 * @author ldamus
 */
public class CreateElementCommand
	extends EditElementCommand {

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

	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		// Do the default element creation
		newElement = doDefaultElementCreation();

		// Configure the new element
		ConfigureRequest configureRequest = createConfigureRequest();

		ICommand configureCommand = elementType
			.getEditCommand(configureRequest);

		if (configureCommand != null && configureCommand.isExecutable()) {
			configureCommand.execute(progressMonitor);
		}

		// Put the newly created element in the request so that the
		// 'after' commands have access to it.
		getCreateRequest().setNewElement(newElement);

		return newOKCommandResult(newElement);
	}

	/**
	 * Creates the request to configure the new element.
	 * 
	 * @return the request
	 */
	protected ConfigureRequest createConfigureRequest() {

		ConfigureRequest configureRequest = new ConfigureRequest(newElement,
			getElementType());
		configureRequest.addParameters(getRequest().getParameters());

		return configureRequest;
	}

	/**
	 * Subclasses should implement this to override the basic element creation.
	 * 
	 * @return the new model element that has been created
	 */
	protected EObject doDefaultElementCreation() {

		EClass eClass = getElementType().getEClass();

		EObject element = eClass.getEPackage().getEFactoryInstance().create(
			eClass);

		if (getContainmentFeature() != null) {
			EObject container = getElementToEdit();

			if (container != null) {
				if (getContainmentFeature().isMany()) {
					((Collection) container.eGet(getContainmentFeature()))
						.add(element);

				} else {
					container.eSet(getContainmentFeature(), element);
				}
			}
		}

		return element;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#isExecutable()
	 */
	public boolean isExecutable() {

		boolean result = true;

		if (getContainmentFeature() != null) {
			EClassifier eClassifier = getContainmentFeature().getEType();

			if (eClassifier instanceof EClass) {
				result = ((EClass) eClassifier).isSuperTypeOf(getElementType()
					.getEClass());
			}
			return result && super.isExecutable();
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