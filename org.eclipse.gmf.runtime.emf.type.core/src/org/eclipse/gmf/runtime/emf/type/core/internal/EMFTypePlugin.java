/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal;

import org.eclipse.core.runtime.Plugin;

/**
 * Plug-in class for the Model Element Type framework.
 * <p>
 * This class is not intended to be used by clients.
 * 
 * @author ldamus
 */
public class EMFTypePlugin
	extends Plugin {

	/**
	 * The shared instance.
	 */
	private static EMFTypePlugin plugin;

	/**
	 * The constructor.
	 */
	public EMFTypePlugin() {
		super();
		plugin = this;
	}

	/**
	 * @return Returns the plugin.
	 */
	public static EMFTypePlugin getPlugin() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getPlugin().getBundle().getSymbolicName();
	}

}