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

package org.eclipse.gmf.runtime.common.ui.action.ide.internal;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;

/**
 * The main class for the plugin.
 * 
 * @author Wayne Diu, wdiu
 */
public class CommonUIActionIDEPlugin
	extends XToolsUIPlugin {

	/**
	 * The shared instance.
	 */
	private static CommonUIActionIDEPlugin _instance;

	/**
	 * The constructor.
	 */
	public CommonUIActionIDEPlugin() {
		_instance = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance of <code>CommonUIActionIDEPlugin</code>
	 */
	public static CommonUIActionIDEPlugin getDefault() {
		return _instance;
	}

	/**
	 * Returns null because no resource manager is associated with this plugin.
	 * 
	 * @return null
	 */
	public AbstractResourceManager getResourceManager() {
		return null;
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