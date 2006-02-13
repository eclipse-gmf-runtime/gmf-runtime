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

package org.eclipse.gmf.runtime.diagram.core.internal.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.CreateComponentElementRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.CreateRelationshipElementRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.DestroyElementRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.DestroyRequestViaKeyboard;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.MoveElementRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequestTypes;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.AbstractEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;

/**
 * Translates semantic requests (for the SemanticService) to edit element
 * command requests (for the new Element Type API), and vice versa.
 * 
 * @author ldamus
 * @canBeSeenBy %level1
 * 
 * @deprecated This is a temporary class to support the SemanticService during
 *             the migration to the Element Type API.
 */
public class SemanticRequestTranslator {

	// TODO remove this field when the uml domain migration is complete
	private static final IElementType semanticServiceElementType = ElementTypeRegistry
		.getInstance()
		.getElementType(EcorePackage.eINSTANCE.getEModelElement());
	
	/**
	 * Preserves custom semantic requests (those not defined in the diagram
	 * domain).
	 */
	private static class RequestWrapper
		extends AbstractEditCommandRequest {

		private SemanticRequest semanticRequest;

		public RequestWrapper(TransactionalEditingDomain editingDomain, SemanticRequest semanticRequest) {
			super(editingDomain);
			this.semanticRequest = semanticRequest;
		}

		public Object getEditHelperContext() {
			return semanticServiceElementType;
		}

		public SemanticRequest getSemanticRequest() {
			return semanticRequest;
		}
	}
	
	/**
	 * Preserves custom semantic create requests (those not defined in the
	 * diagram domain).
	 */
	private static class CreateElementRequestWrapper
		extends CreateElementRequest {

		private org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.CreateElementRequest semanticRequest;

		public CreateElementRequestWrapper(
				org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.CreateElementRequest semanticRequest) {
			super(TransactionUtil
                .getEditingDomain(semanticRequest.getContext()),
                semanticRequest.getElementType());
			this.semanticRequest = semanticRequest;
		}

		public Object getEditHelperContext() {
			return semanticServiceElementType;
		}

		public SemanticRequest getSemanticRequest() {
			return semanticRequest;
		}
	}

	/**
	 * Preserves custom semantic create requests (those not defined in the
	 * diagram domain).
	 */
	private static class CreateComponentElementRequestWrapper
		extends org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest {

		private CreateComponentElementRequest semanticRequest;

		public CreateComponentElementRequestWrapper(
				CreateComponentElementRequest semanticRequest) {
			super(TransactionUtil.getEditingDomain(semanticRequest
                .getContextObject()), semanticRequest.getContextObject(),
                semanticRequest.getElementType());
			this.semanticRequest = semanticRequest;
		}

		public Object getEditHelperContext() {
			return semanticRequest.getContextObject();
		}

		public SemanticRequest getSemanticRequest() {
			return semanticRequest;
		}

		public void setContainer(EObject container) {
			super.setContainer(container);
			semanticRequest.setContextElement(container);
		}

		public EObject getContainer() {
			return semanticRequest.getContextObject();
		}
	}

	/**
	 * Preserves custom semantic create requests (those not defined in the
	 * diagram domain).
	 */
	private static class CreateRelationshipElementRequestWrapper
		extends CreateRelationshipRequest {

		private CreateRelationshipElementRequest semanticRequest;

		public CreateRelationshipElementRequestWrapper(TransactionalEditingDomain editingDomain,
				CreateRelationshipElementRequest semanticRequest) {
			super(editingDomain, null, null, null, semanticRequest.getElementType());
			this.semanticRequest = semanticRequest;
		}

		public Object getEditHelperContext() {
			return semanticRequest.getSource();
		}

		public EObject getContainer() {
			// Arbitrary
			return semanticRequest.getSource();
		}

		public SemanticRequest getSemanticRequest() {
			return semanticRequest;
		}

		public void setSource(EObject source) {
			super.setSource(source);
			semanticRequest.setSourceElement(source);
		}

		public EObject getSource() {
			return semanticRequest.getSource();
		}

		public void setTarget(EObject target) {
			super.setTarget(target);
			semanticRequest.setTargetElement(target);
		}

		public EObject getTarget() {
			return semanticRequest.getTarget();
		}

		public void setPrompt(boolean prompt) {
			super.setPrompt(prompt);
			semanticRequest.setSuppressibleUI(!prompt);
		}

		public boolean isPrompt() {
			return !semanticRequest.isUISupressed();
		}
	}

	/**
	 * Private constructor.
	 */
	private SemanticRequestTranslator() {
		// Not meant to be instanciated.
	}

	// Translate semantic request to edit command request

	public static IEditCommandRequest translate(SemanticRequest semanticRequest) {

		IEditCommandRequest result = null;
		
		Class requestClass = semanticRequest.getClass();

		if (semanticRequest instanceof org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.CreateElementRequest) {
			result = translate((org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.CreateElementRequest) semanticRequest);

		} else if (requestClass == DestroyElementRequest.class
			|| requestClass == DestroyRequestViaKeyboard.class) {
			
			result = translate((DestroyElementRequest) semanticRequest);

		} else if (requestClass == DuplicateElementsRequest.class) {
			result = translate((DuplicateElementsRequest) semanticRequest);

		} else if (requestClass == MoveElementRequest.class) {
			result = translate((MoveElementRequest) semanticRequest);

		} else if (requestClass == ReorientRelationshipRequest.class) {
			result = translate((ReorientRelationshipRequest) semanticRequest);

		} else if (requestClass == ReorientReferenceRelationshipRequest.class) {
			result = translate((ReorientReferenceRelationshipRequest) semanticRequest);

		} else {
			// This is a custom request subclass not defined in the diagram domain.
			// Return a wrapper for the request.
			result = new RequestWrapper(MEditingDomain.INSTANCE, semanticRequest);
		}
		result.addParameters(semanticRequest.getParameters());
		return result;
	}

	public static CreateElementRequest translate(
			org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.CreateElementRequest semanticRequest) {

		Class requestClass = semanticRequest.getClass();

		if (requestClass == CreateComponentElementRequest.class) {
			return translate((CreateComponentElementRequest) semanticRequest);

		} else if (requestClass == CreateRelationshipElementRequest.class) {
			return translate((CreateRelationshipElementRequest) semanticRequest);

		} else if (semanticRequest instanceof CreateRelationshipElementRequest) {
			// Return a wrapper for the request
			return new CreateRelationshipElementRequestWrapper(
					(TransactionalEditingDomain) MEditingDomain.INSTANCE,
					(CreateRelationshipElementRequest) semanticRequest);

		} else if (semanticRequest instanceof CreateComponentElementRequest) {
			// Return a wrapper for the request
			return new CreateComponentElementRequestWrapper(
				(CreateComponentElementRequest) semanticRequest);
		}
		// This is a custom request subclass not defined in the diagram domain.
		// Return a wrapper for the request.
		return new CreateElementRequestWrapper(semanticRequest);
	}

	public static CreateElementRequest translate(
			CreateComponentElementRequest semanticRequest) {

		EObject container = semanticRequest.getContextObject();
		IElementType elementType = semanticRequest.getElementType();
		CreateElementRequest result = new CreateElementRequest(TransactionUtil
            .getEditingDomain(container), container, elementType);

		return result;
	}

	public static CreateRelationshipRequest translate(
			CreateRelationshipElementRequest semanticRequest) {

		EObject source = semanticRequest.getSource();
		EObject target = semanticRequest.getTarget();
		IElementType elementType = semanticRequest.getElementType();

		CreateRelationshipRequest createRelationshipRequest = new CreateRelationshipRequest(
			TransactionUtil.getEditingDomain(source), source, target, elementType);
		createRelationshipRequest.setPrompt(!semanticRequest.isUISupressed());
		return createRelationshipRequest;
	}

	public static DestroyRequest translate(DestroyElementRequest semanticRequest) {

		DestroyRequest request = null;

		boolean confirmationRequired = false;

		if (semanticRequest instanceof DestroyRequestViaKeyboard) {
			confirmationRequired = ((DestroyRequestViaKeyboard) semanticRequest)
				.isShowInformationDialog();
		}

		EObject referenceOwner = semanticRequest.getReferenceObjectOwner();
		EObject referenceObject = semanticRequest.getReferenceObject();

		if (referenceOwner != null && referenceObject != null) {
			request = new DestroyReferenceRequest(TransactionUtil
                .getEditingDomain(referenceOwner), referenceOwner, null,
                referenceObject, confirmationRequired);

		} else {
			EObject elementToDestroy = semanticRequest.getObject();
            request = new org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest(
                TransactionUtil.getEditingDomain(referenceOwner),
                elementToDestroy, confirmationRequired);
        }

		return request;
	}

	public static org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest translate(
			DuplicateElementsRequest semanticRequest) {

		List elementsToDuplicate = semanticRequest.getElementsToBeDuplicated();
		Map duplicatedElementsMap = semanticRequest.getAllDuplicatedElementsMap();
        
        TransactionalEditingDomain editingDomain = null;
        if (elementsToDuplicate != null && elementsToDuplicate.size() > 0) {
            editingDomain = TransactionUtil
                .getEditingDomain(elementsToDuplicate.get(0));
        }

		org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest result = new org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest(
            editingDomain, elementsToDuplicate);
		result.setAllDuplicatedElementsMap(duplicatedElementsMap);
		return result;
	}

	public static MoveRequest translate(MoveElementRequest semanticRequest) {

		EObject targetContainer = semanticRequest.getNewContainerElement();
		EObject elementToMove = semanticRequest.getMoveElement();

		MoveRequest request = new MoveRequest(TransactionUtil
            .getEditingDomain(targetContainer), targetContainer, elementToMove);
        return request;
	}

	public static org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest translate(
			ReorientRelationshipRequest semanticRequest) {

		EObject relationship = semanticRequest.getRelationshipObject();
		EObject newRelationshipEnd = semanticRequest.getRelationshipEndPoint();
		EObject oldRelationshiEnd = semanticRequest
			.getOldRelationshipEndPoint();

		int direction = org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest.REORIENT_SOURCE;
		if (semanticRequest.getRequestType().equals(
			SemanticRequestTypes.SEMREQ_REORIENT_RELATIONSHIP_TARGET)) {
			direction = org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest.REORIENT_TARGET;
		}

		org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest request = new org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest(
            TransactionUtil.getEditingDomain(relationship), relationship,
            newRelationshipEnd, oldRelationshiEnd, direction);

		return request;
	}

	public static org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest translate(
			ReorientReferenceRelationshipRequest semanticRequest) {

		EObject referenceOwner = semanticRequest.getReferenceObjectOwner();
		EObject newRelationshipEnd = semanticRequest.getNewTarget();
		EObject oldRelationshiEnd = semanticRequest.getOldTarget();

		int direction = org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest.REORIENT_SOURCE;
		if (semanticRequest.getRequestType().equals(
			SemanticRequestTypes.SEMREQ_REORIENT_RELATIONSHIP_TARGET)) {
			direction = org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest.REORIENT_TARGET;
		}

		org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest request = new org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest(
            TransactionUtil.getEditingDomain(referenceOwner), referenceOwner,
            newRelationshipEnd, oldRelationshiEnd, direction);

		return request;
	}

	// Translate edit command request to semantic request

	public static SemanticRequest translate(
			IEditCommandRequest editCommandRequest) {

		SemanticRequest result = null;
		
		if (editCommandRequest instanceof RequestWrapper) {
			result = ((RequestWrapper) editCommandRequest).getSemanticRequest();

		} else if (editCommandRequest instanceof CreateRelationshipElementRequestWrapper) {
			result = ((CreateRelationshipElementRequestWrapper) editCommandRequest)
				.getSemanticRequest();

		} else if (editCommandRequest instanceof CreateComponentElementRequestWrapper) {
			result = ((CreateComponentElementRequestWrapper) editCommandRequest)
				.getSemanticRequest();
			
		} else if (editCommandRequest instanceof CreateElementRequestWrapper) {
			result = ((CreateElementRequestWrapper) editCommandRequest)
				.getSemanticRequest();


		} else if (editCommandRequest instanceof CreateRelationshipRequest) {
			result = translate((CreateRelationshipRequest) editCommandRequest);

		} else if (editCommandRequest instanceof CreateElementRequest) {
			result = translate((CreateElementRequest) editCommandRequest);

		} else if (editCommandRequest instanceof org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest) {
			result = translate((org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest) editCommandRequest);

		} else if (editCommandRequest instanceof DestroyReferenceRequest) {
			result = translate((DestroyReferenceRequest) editCommandRequest);

		} else if (editCommandRequest instanceof org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest) {
			result = translate((org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest) editCommandRequest);

		} else if (editCommandRequest instanceof MoveRequest) {
			result = translate((MoveRequest) editCommandRequest);

		} else if (editCommandRequest instanceof org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest) {
			result = translate((org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest) editCommandRequest);

		} else if (editCommandRequest instanceof org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest) {
			result = translate((org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest) editCommandRequest);

		}
		if (result != null) {
			result.addParameters(editCommandRequest.getParameters());
		}
		return result;
	}

	public static CreateComponentElementRequest translate(
			CreateElementRequest editCommandRequest) {

		EObject contextObject = editCommandRequest.getContainer();
		IElementType elementType = editCommandRequest.getElementType();

		return new CreateComponentElementRequest(elementType, contextObject);
	}

	public static CreateRelationshipElementRequest translate(
			CreateRelationshipRequest editCommandRequest) {

		EObject source = editCommandRequest.getSource();
		EObject target = editCommandRequest.getTarget();
		IElementType elementType = editCommandRequest.getElementType();

		CreateRelationshipElementRequest result = new CreateRelationshipElementRequest(
			elementType, source, target);
		result.setSuppressibleUI(!editCommandRequest.isPrompt());
		return result;
	}

	public static DestroyElementRequest translate(
			org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest editCommandRequest) {

		EObject element = editCommandRequest.getElementToDestroy();

		if (editCommandRequest.isConfirmationRequired()) {
			DestroyRequestViaKeyboard result = new DestroyRequestViaKeyboard(
				element);
			result.setShowInformationDialog(true);
			return result;

		} else {
			return new DestroyElementRequest(element);
		}
	}

	public static DestroyElementRequest translate(
			DestroyReferenceRequest editCommandRequest) {

		EObject referenceObject = editCommandRequest.getReferencedObject();
		EObject referenceOwner = editCommandRequest.getContainer();

		DestroyElementRequest result;
		if (editCommandRequest.isConfirmationRequired()) {
			result = new DestroyRequestViaKeyboard();
			((DestroyRequestViaKeyboard) result).setShowInformationDialog(true);

		} else {
			result = new DestroyElementRequest();
		}

		result.setReference(referenceObject);
		result.setReferenceOwner(referenceOwner);
		return result;
	}

	public static DuplicateElementsRequest translate(
			org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest editCommandRequest) {

		DuplicateElementsRequest result = new DuplicateElementsRequest(
			editCommandRequest.getElementsToBeDuplicated());
		result.setAllDuplicatedElementsMap(
			editCommandRequest.getAllDuplicatedElementsMap());
		return result;
	}

	public static MoveElementRequest translate(MoveRequest editCommandRequest) {
		EObject targetContainer = editCommandRequest.getTargetContainer();
		EObject elementToMove = null;
		Map elementsToMove = editCommandRequest.getElementsToMove();
		
		if (elementsToMove != null) {
			Iterator i = elementsToMove.keySet().iterator();

			if (i.hasNext()) {
				elementToMove = (EObject) i.next();
			}
		}
		
		return new MoveElementRequest(elementToMove, targetContainer);
	}

	public static ReorientRelationshipRequest translate(
			org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest editCommandRequest) {

		EObject relationship = editCommandRequest.getRelationship();
		EObject newRelationshipEnd = editCommandRequest.getNewRelationshipEnd();
		EObject oldRelationshiEnd = editCommandRequest.getOldRelationshipEnd();

		String requestType = SemanticRequestTypes.SEMREQ_REORIENT_RELATIONSHIP_SOURCE;
		if (editCommandRequest.getDirection() == org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest.REORIENT_TARGET) {
			requestType = SemanticRequestTypes.SEMREQ_REORIENT_RELATIONSHIP_TARGET;
		}

		return new ReorientRelationshipRequest(requestType, relationship,
			newRelationshipEnd, oldRelationshiEnd);
	}

	public static ReorientReferenceRelationshipRequest translate(
			org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest editCommandRequest) {
		EObject referenceOwner = editCommandRequest.getReferenceOwner();
		EObject newRelationshipEnd = editCommandRequest.getNewRelationshipEnd();
		EObject oldRelationshiEnd = editCommandRequest.getOldRelationshipEnd();

		String requestType = SemanticRequestTypes.SEMREQ_REORIENT_RELATIONSHIP_SOURCE;
		if (editCommandRequest.getDirection() == org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest.REORIENT_TARGET) {
			requestType = SemanticRequestTypes.SEMREQ_REORIENT_RELATIONSHIP_TARGET;
		}

		return new ReorientReferenceRelationshipRequest(requestType,
			referenceOwner, oldRelationshiEnd, newRelationshipEnd);
	}

}