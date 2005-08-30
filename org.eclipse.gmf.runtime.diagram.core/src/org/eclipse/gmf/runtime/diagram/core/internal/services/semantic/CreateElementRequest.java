/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.core.internal.util.SemanticRequestTranslator;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * 
 * An abstract implementation of a create element request that takes a create
 * element kind enumeration.
 * 
 * @author melaasar
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest}
 *             to get move commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 */
public abstract class CreateElementRequest extends SemanticRequest {

	/**
	 * An element desciptor containing the element's <code>CreateElementRequest</code>
	 * The class is also a mutable adapter that initially adapts only to <code>CreateElementKind</code>
	 * but can later adapt to <code>IElement</code> and <code>IReference</code> after the element has been
	 * created (by executing the creation command returned from semantic providers in response to this request)
	 * This pattern in similar to <code>ViewDescriptor</code> defined by the <code>CreateViewRequest</code>
	 * 
	 * @deprecated Use {@link CreateElementRequestAdapter} instead.
	 */
	public static class ElementDescriptor extends CreateElementRequestAdapter {
		/** the create element request */
		private final CreateElementRequest _createElementRequest;

		/**
		 * Creates a new element descriptor
		 * @param createElementRequest
		 */
		public ElementDescriptor(CreateElementRequest createElementRequest) {
			
			super(SemanticRequestTranslator.translate(createElementRequest));
			
			assert null != createElementRequest : "Null createElementRequest in ElementDescriptor";//$NON-NLS-1$
			_createElementRequest = createElementRequest;
			setElement(null);
		}

		/**
		 * Adapts to <code>CreateElementRequest</code> and <code>Request</code>
		 * It can also adapt to <code>IReference</code> and <code>IElement</code>
		 * once the element is created, but can also adapter to <code>Integer<code>
		 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
		 */
		public Object getAdapter(Class adapter) {
			if (adapter.isInstance(_createElementRequest)) {
				return _createElementRequest;
			}
			return super.getAdapter(adapter); 
		}

		/** Set the element. */
		public void setElement( EObject element ) {
			super.setNewElement(element);
		}
	}

	private final IElementType _elementType;
	
	/** the request's semantic hint */
	private String _semanticHint = null;

	
	/**
	 * Creates a new Element request
	 * @param requestType
	 * @param elementType
	 * @param semanticHint
	 */	
	public CreateElementRequest(Object requestType,
								IElementType elementType,
								String semanticHint) {
		super(requestType);
		_elementType = elementType;
		_semanticHint = semanticHint;
	}
	
	/**
	 * Creates a new Element request
	 * @param requestType
	 * @param elementTypeInfo
	 */
	public CreateElementRequest(
		Object requestType,
		IElementType elementType) { 
		this( requestType, elementType, null );
	}
	

	/**
	 * Creates a new Element request
	 * @param requesttype
	 * @param semanticHint
	 */
	public CreateElementRequest(
			Object requestType,
			String semanticHint) {
		this( requestType, (IElementType)null, semanticHint );
	}
			
	/**
	 * Returns the semantic hint for the request.
	 * @return the semantic hint.
	 */
	public String getSemanticHint() {
		return _semanticHint;
	}

	/** Return the cached element type data. */
	public IElementType getElementType() {
		return _elementType;
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest#getContext()
	 */
	public Object getContext() {
		return _elementType;
	}

}
