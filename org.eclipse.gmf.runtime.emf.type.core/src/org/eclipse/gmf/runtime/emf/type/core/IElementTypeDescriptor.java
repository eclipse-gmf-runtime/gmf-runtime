/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core;

import java.net.URL;

/**
 * Descriptor for an element type that has been defined in XML using the
 * <code>elementTypes</code> extension point.
 * 
 * @author ldamus
 */
public interface IElementTypeDescriptor {

	/**
	 * Gets the element type identifier.
	 * 
	 * @return the element type identifier.
	 */
	public abstract String getId();

	/**
	 * Gets the element type icon URL.
	 * 
	 * @return the element type icon URL
	 */
	public abstract URL getIconURL();

	/**
	 * Gets the element type display name.
	 * 
	 * @return the element type display name.
	 */
	public abstract String getName();

	/**
	 * Gets the name of the element factory kind. Identifies the factory that
	 * should be used to create the new element type.
	 * 
	 * @return the element factory kind name
	 */
	public abstract String getKindName();

	/**
	 * Gets the value for the parameter named <code>paramName</code>.
	 * 
	 * @param paramName
	 *            the parameter name
	 * @return the parameter value
	 */
	public abstract String getParamValue(String paramName);
}