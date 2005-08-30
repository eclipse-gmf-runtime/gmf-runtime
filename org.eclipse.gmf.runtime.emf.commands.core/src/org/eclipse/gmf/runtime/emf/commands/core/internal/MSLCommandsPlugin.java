/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.commands.core.internal;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin;
import org.eclipse.gmf.runtime.emf.commands.core.internal.l10n.ResourceManager;

/**
 * The main plugin class to be used in the desktop.
 */
public class MSLCommandsPlugin extends XToolsPlugin {
	//The shared instance.
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

	public AbstractResourceManager getResourceManager() {
		return ResourceManager.getInstance();
	}
}
