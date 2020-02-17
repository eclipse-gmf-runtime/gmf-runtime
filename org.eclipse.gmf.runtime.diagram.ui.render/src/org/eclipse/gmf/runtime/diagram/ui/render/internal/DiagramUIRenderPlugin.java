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
package org.eclipse.gmf.runtime.diagram.ui.render.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The Diagram UI Render plug-in.
 * 
 * @author cmahoney
 */
public class DiagramUIRenderPlugin
	extends AbstractUIPlugin {

	/**
	 * The shared instance.
	 */
	private static DiagramUIRenderPlugin plugin;

	/**
	 * The constructor.
	 * 
	 * @see org.eclipse.core.runtime.Plugin#Plugin()
	 */
	public DiagramUIRenderPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the plugin instance
	 */
	public static DiagramUIRenderPlugin getInstance() {
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