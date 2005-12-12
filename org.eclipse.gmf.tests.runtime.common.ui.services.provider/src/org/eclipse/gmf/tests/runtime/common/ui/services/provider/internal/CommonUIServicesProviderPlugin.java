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

package org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin;

/**
 * Main plugin class. 
 * 
 * @author Wayne Diu, wdiu
 */
public class CommonUIServicesProviderPlugin
	extends Plugin {

	/**
	 * This plug-in's shared instance.
	 */
	private static CommonUIServicesProviderPlugin plugin;

	/**
	 * Creates a new plug-in runtime object.
	 */
	public CommonUIServicesProviderPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static CommonUIServicesProviderPlugin getDefault() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}
}