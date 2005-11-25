/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal;

import org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Yasser Lulu
 */
public class DiagramPlugin
	extends XToolsPlugin {

	//The shared instance.
	private static DiagramPlugin plugin;

	/**
	 * The constructor.
	 */
	public DiagramPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static DiagramPlugin getInstance() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string and is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getInstance().getSymbolicName();
	}

}