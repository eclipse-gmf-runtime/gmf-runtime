/*
 * Copyright (c) 2015 Christian W. Damus and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Christian W. Damus - initial API and implementation 
 */

package org.eclipse.gmf.runtime.emf.type.core;

/**
 * A notification that an {@linkplain #getElementTypeId() element type} has been
 * removed from the {@linkplain ElementTypeRegistry registry}.
 * 
 * @since 1.9
 */
public class ElementTypeRemovedEvent {

	private IElementType elementType;

	/**
	 * Initializes me with the element type that was removed.
	 * 
	 * @param elementType
	 *            the removed element type
	 */
	ElementTypeRemovedEvent(IElementType elementType) {
		super();

		this.elementType = elementType;
	}

	/**
	 * Queries the unique identifier of the element type that was removed. Note
	 * that, because the element type is no longer actually available in the
	 * registry to be looked up, it is {@linkplain #getElementType() provided}
	 * by this event object.
	 * 
	 * @return the removed element type's unique identifier
	 * 
	 * @see #getElementType()
	 */
	public String getElementTypeId() {
		return elementType.getId();
	}

	/**
	 * Queries the element type that was removed.
	 * 
	 * @return the removed element type
	 */
	public IElementType getElementType() {
		return elementType;
	}
}
