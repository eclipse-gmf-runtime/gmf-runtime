/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.commands.core.internal;

import org.eclipse.core.runtime.Plugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class MSLCommandsPlugin
	extends Plugin {

	// The shared instance.
	private static MSLCommandsPlugin plugin;

	/**
	 * The constructor.
	 */
	public MSLCommandsPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance of <code>MSLCommandsPlugin</code>
	 */
	public static MSLCommandsPlugin getDefault() {
		return plugin;
	}

	/**
	 * Get plugin ID.
	 * 
	 * @return plugin id as <code>String</code>
	 */
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

}
