/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core;

/**
 * Notification that an element type has been added to the
 * {@link org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry}.
 * 
 * @author ldamus
 * 
 */
public class ElementTypeAddedEvent {

	/**
	 * The ID of the element type that has been added.
	 */
	private String elementTypeId;

	/**
	 * Constructs a new event.
	 * 
	 * @param elementTypeId
	 *            the element type ID
	 */
	public ElementTypeAddedEvent(String elementTypeId) {
		this.elementTypeId = elementTypeId;
	}

	/**
	 * Gets the ID of the element type that was added.
	 * 
	 * @return the element type ID
	 */
	public String getElementTypeId() {
		return elementTypeId;
	}

}
