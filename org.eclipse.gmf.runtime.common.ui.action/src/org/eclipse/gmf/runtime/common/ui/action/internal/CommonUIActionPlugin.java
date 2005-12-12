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
package org.eclipse.gmf.runtime.common.ui.action.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class CommonUIActionPlugin
	extends AbstractUIPlugin {

	/**
	 * The shared instance.
	 */
	private static CommonUIActionPlugin INSTANCE;

	/**
	 * The constructor.
	 */
	public CommonUIActionPlugin() {
		INSTANCE = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return a shared instance of <code>CommonUIActionPlugin</code>
	 */
	public static CommonUIActionPlugin getDefault() {
		return INSTANCE;
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
