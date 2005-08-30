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
