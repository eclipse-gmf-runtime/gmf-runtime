/******************************************************************************
 * Copyright (c) 2002, 2003, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author melaasar
 */
public class GefPlugin
	extends AbstractUIPlugin {

	/** the plugin singleton */
	private static GefPlugin singleton;

	/**
	 * Method getInstance.
	 * 
	 * @return Plugin
	 */
	public static GefPlugin getInstance() {
		return singleton;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string and is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getInstance().getBundle().getSymbolicName();
	}

	/**
	 * Creates the Gef plugin instance
	 * 
	 * @see org.eclipse.core.runtime.Plugin#Plugin()
	 */
	public GefPlugin() {
		super();
		if (singleton == null)
			singleton = this;
	}

}