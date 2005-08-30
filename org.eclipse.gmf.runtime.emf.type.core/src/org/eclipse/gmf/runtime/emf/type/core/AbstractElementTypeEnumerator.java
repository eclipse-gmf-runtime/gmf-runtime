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
 * Abstract superclass for types wishing to define constants that represent
 * element types contributed in the
 * <code>org.eclipse.gmf.runtime.emf.type.core.elementTypes</code> extension point.
 * <P>
 * Provides a convenience method that will access the
 * <code>ElementTypeRegistry</code> to get element types by ID.
 * 
 * @author ldamus
 */
public abstract class AbstractElementTypeEnumerator {

	/**
	 * Gets the element type for <code>id</code> from the
	 * <code>ElementTypeRegistry</code>.
	 * 
	 * @param id
	 *            the element type identifier
	 * @return element type for <code>id</code> from the
	 *         <code>ElementTypeRegistry</code>, or <code>null</code> if
	 *         there is no element type with that ID.
	 */
	protected static final IElementType getElementType(String id) {
		return ElementTypeRegistry.getInstance().getType(id);
	}
}