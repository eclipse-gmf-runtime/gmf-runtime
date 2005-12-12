/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;



/**
 * @author melaasar
 * 
 * A request to create a relationship element betweem two model elements
 * 
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest}
 *             to get move commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 */
public class CreateRelationshipElementRequest extends CreateElementRequest implements SuppressibleUIRequest{

	private boolean suppressUI = false;

	/**
	 * @return true if the command will suppress all ui prompting and just use default data
	 * @see com.ibm.xtools.uml.ui.internal.commands.SuppressibleUICommand#isUISupressed()
	 */
	public boolean isUISupressed() {
		
		return suppressUI;
	}
	
	/**
	 * @param suppressUI true if you do not wish the command to prompt with UI but instead
	 * take the default value that it would have prompt for.
	 * @see com.ibm.xtools.uml.ui.internal.commands.SuppressibleUICommand#setSuppressibleUI(boolean)
	 */
	public void setSuppressibleUI(boolean suppressUI) {
		this.suppressUI = suppressUI; 

	}
	
	/** the relationship source */
	private EObject _sourceElement;
	/** the relationship target */
	private EObject _targetElement;

	
	/**
	 * Constructor for CreateRelationshipElementRequest.
	 * @param elementType
	 */
	public CreateRelationshipElementRequest(IElementType elementType) {
		this( SemanticRequestTypes.SEMREQ_CREATE_RELATIONSHIP_ELEMENT, elementType);
	}
	
	/**
	 * Constructor for CreateRelationshipElementRequest with semantic hint.
	 * @param elementTypeInfo
	 * @param semanticHint
	 */
	public CreateRelationshipElementRequest( IElementType elementType, String semanticHint) {
		super(SemanticRequestTypes.SEMREQ_CREATE_RELATIONSHIP_ELEMENT, elementType, semanticHint);
	}	

	/**
	 * Constructor for CreateRelationshipElementRequest.
	 * 
	 * @param requestType
	 * @param elementTypeInfo
	 */
	public CreateRelationshipElementRequest( Object requestType, IElementType elementType) {
		super(requestType, elementType);
	}
	
	/**
	 * Constructor for CreateRelationshipElementRequest.
	 * 
	 * @param elementTypeInfo
	 * @param sourceElement
	 * @param targetElement
	 */
	public CreateRelationshipElementRequest( IElementType elementType, EObject sourceElement, EObject targetElement) {
		this( SemanticRequestTypes.SEMREQ_CREATE_RELATIONSHIP_ELEMENT, elementType, sourceElement, targetElement );
	}
	
	/**
	 * Constructor for CreateRelationshipElementRequest.
	 * 
	 * @param requestType
	 * @param elementTypeInfo
	 * @param sourceElement The sourceElement to set
	 * @param targetElement The targetElement to set
	 */
	public CreateRelationshipElementRequest( Object requestType, IElementType elementType, EObject sourceElement, EObject targetElement) {
		this(requestType, elementType);
		setSourceElement(sourceElement);
		setTargetElement(targetElement);
	}

	/** Returns the sourceElement. */
	public final EObject getSource() {
		return _sourceElement;
	}

	/** Return the target element. */
	public final EObject getTarget() {
		return _targetElement;
	}

	/** Sets the source element.	*/
	public final void setSourceElement(EObject sourceElement) {
		_sourceElement = sourceElement;
	}

	/** Sets the target element. */
	public final void setTargetElement(EObject targetElement) {
		_targetElement = targetElement;
	}

}
