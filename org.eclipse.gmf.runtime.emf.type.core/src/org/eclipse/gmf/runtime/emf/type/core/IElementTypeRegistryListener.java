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
 * Interface implemented by clients wishing to know when element types are added
 * to the element type registry.
 * 
 * @author ldamus
 */
public interface IElementTypeRegistryListener {

	/**
	 * Notifies listeners that the new element type has been added to the element type registry.
	 * 
	 * @param elementTypeAddedEvent
	 *            the event
	 */
	public abstract void elementTypeAdded(
			ElementTypeAddedEvent elementTypeAddedEvent);
}
