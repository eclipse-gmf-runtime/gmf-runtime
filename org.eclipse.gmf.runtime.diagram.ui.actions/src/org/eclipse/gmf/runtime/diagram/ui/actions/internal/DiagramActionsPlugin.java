/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Yasser Lulu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.actions.*
 */
public class DiagramActionsPlugin
	extends XToolsUIPlugin {

	//The shared instance.
	private static DiagramActionsPlugin plugin;

	/**
	 * The constructor.
	 */
	public DiagramActionsPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static DiagramActionsPlugin getInstance() {
		return plugin;
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
	 * Retrieves the resource manager for this plug-in.
	 * 
	 * @return The resource manager for this plug-in.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return DiagramActionsResourceManager.getInstance();
	}

}