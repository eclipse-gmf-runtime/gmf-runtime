/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.edit;

/**
 * Enumeration that describes options that can be passed to create, load and
 * save of a resource.
 * 
 * @author rafikj
 */
public final class MResourceOption {

	/**
	 * Indicates that the path string passed to the method is a URI string not a
	 * file system path.
	 */
	public static final int URI = 1;

	/**
	 * Indicates that a resource should be saved even if it is readonly.
	 */
	public static final int OVERWRITE_READONLY = 2;

	/**
	 * Indicates that a resource should be loaded/saved with forward
	 * compatibility support.
	 */
	public static final int COMPATIBILITY_MODE = 4;

	/**
	 * Indicates that a resource should be saved using a file buffer (i.e.
	 * XMLResource.OPTION_USE_FILE_BUFFER).
	 */
	public static final int USE_FILE_BUFFER = 8;

	/**
	 * Indicates that we want sub-units to be loaded immediately,
	 * if the resource that we are loading is a
	 * {@linkplain org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource logical resource}.
	 * 
	 * @see #DONT_AUTO_LOAD_SUBUNITS
	 * 
	 * @deprecated Use the cross-resource containment support provided by EMF,
	 *     instead, by defining containment features that are capable of storing
	 *     proxies.
	 */
	public static final int LOAD_ALL_SUBUNITS = 16;

	/**
	 * Indicates that we do not want to load sub-units automatically, if we are
	 * not loading all of the units initially.
	 * This option is ignored if all units are loaded initially.
	 * 
	 * @see #LOAD_ALL_SUBUNITS
	 * 
	 * @deprecated Use the cross-resource containment support provided by EMF,
	 *     instead, by defining containment features that are capable of storing
	 *     proxies.
	 */
	public static final int DONT_AUTO_LOAD_SUBUNITS = 32;
	
	private MResourceOption() {
		// private
	}
}