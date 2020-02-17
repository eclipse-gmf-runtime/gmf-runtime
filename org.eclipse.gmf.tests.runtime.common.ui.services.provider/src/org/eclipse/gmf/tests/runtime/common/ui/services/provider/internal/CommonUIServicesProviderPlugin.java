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

package org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal;

import org.eclipse.core.runtime.Plugin;

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