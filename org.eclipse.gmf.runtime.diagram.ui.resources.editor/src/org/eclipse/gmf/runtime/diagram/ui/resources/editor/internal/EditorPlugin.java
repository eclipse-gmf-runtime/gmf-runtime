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

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Editor Plug-in
 * 
 * @author qili
 * @author mgoyal
 */
public class EditorPlugin
	extends AbstractUIPlugin {

	/**
	 * The shared instance.
	 */
	private static EditorPlugin plugin;

	/**
	 * The constructor.
	 * 
	 * @see XToolsUIPlugin#XToolsUIPlugin()
	 */
	public EditorPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the plugin instance
	 */
	public static EditorPlugin getInstance() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getInstance().getBundle().getSymbolicName();
	}

}