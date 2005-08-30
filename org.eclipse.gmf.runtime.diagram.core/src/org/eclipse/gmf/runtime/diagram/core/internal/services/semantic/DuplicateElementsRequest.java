/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * The request used to duplicate a list of semantic elements. A map that will
 * hold all the duplicated elements after the command is executed can be
 * retrieved via <code>getDuplicatedElementsMap()</code>.
 * 
 * @author cmahoney
 * 
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest}
 *             to get duplicate commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 */
public class DuplicateElementsRequest
	extends SemanticRequest {

	/**
	 * The list of primary elements to be duplicated.
	 */
	private List elementsToBeDuplicated;

	/**
	 * This will be populated with all the elements that are duplicated after
	 * the command executes. The key is the original element and the value is
	 * the new duplicated element. There may be more elements duplicated than
	 * the original list of elements passed in (e.g. contained elements) --
	 * these will appear in this map.
	 */
	private Map allDuplicatedElementsMap = new HashMap();

	/**
	 * Creates a new <code>DuplicateElementsRequest</code>.
	 * 
	 * @param orignalElements
	 *            the original elements to be duplicated
	 */
	public DuplicateElementsRequest() {
		super(SemanticRequestTypes.SEMREQ_DUPLICATE_ELEMENTS);
	}

	/**
	 * Creates a new <code>DuplicateElementsRequest</code>.
	 * 
	 * @param elements
	 *            the elements to be duplicated
	 * @param duplicatedElements
	 */
	public DuplicateElementsRequest(List elementsToBeDuplicated) {
		this();
		setElementsToBeDuplicated(elementsToBeDuplicated);
	}

	/**
	 * Returns a map of all duplicated elements. This will be populated with all
	 * the elements that are duplicated after the command executes. The key is
	 * the original element and the value is the new duplicated element. There
	 * may be more elements duplicated than the original list of elements passed
	 * in (e.g. contained elements) -- these will appear in this map.
	 * 
	 * @return Returns the allDuplicatedElementsMap.
	 */
	public Map getAllDuplicatedElementsMap() {
		return allDuplicatedElementsMap;
	}
	
	/**
	 * Sets the map of all duplicated elements.
	 * @param allDuplicatedElementsMap the new map
	 */
	public void setAllDuplicatedElementsMap(Map allDuplicatedElementsMap) {
		this.allDuplicatedElementsMap = allDuplicatedElementsMap;
	}

	/**
	 * Sets the primary elements to be duplicated.
	 * 
	 * @param elements
	 *            The elements to be duplicated.
	 */
	public void setElementsToBeDuplicated(List elements) {
		this.elementsToBeDuplicated = elements;
	}

	/**
	 * Returns the primary elements to be duplicated.
	 * 
	 * @return Returns the elements to be duplicated.
	 */
	public List getElementsToBeDuplicated() {
		return elementsToBeDuplicated;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest#getContext()
	 */
	public Object getContext() {
		return getElementsToBeDuplicated();
	}

}