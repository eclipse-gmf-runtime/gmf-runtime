/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
