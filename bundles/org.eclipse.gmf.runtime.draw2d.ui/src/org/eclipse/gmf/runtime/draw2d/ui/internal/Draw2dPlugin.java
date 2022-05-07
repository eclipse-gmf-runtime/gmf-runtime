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

package org.eclipse.gmf.runtime.draw2d.ui.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * @author melaasar
 */
public class Draw2dPlugin
	extends AbstractUIPlugin {

	/** the plugin singleton */
	private static Draw2dPlugin singleton;

	/**
	 * Method getInstance.
	 * 
	 * @return Plugin
	 */
	public static Draw2dPlugin getInstance() {
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
	 * Creates the Draw2d plugin instance
	 * 
	 * @see org.eclipse.core.runtime.Plugin#Plugin()
	 */
	public Draw2dPlugin() {
		super();
		if (singleton == null)
			singleton = this;
	}

}