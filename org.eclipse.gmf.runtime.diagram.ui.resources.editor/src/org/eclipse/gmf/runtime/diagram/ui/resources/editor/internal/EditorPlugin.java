/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorResourceManager;

/**
 * Editor Plug-in
 * 
 * @author qili
 * @author mgoyal
 * @canBeSeenBy %level1
 */
public class EditorPlugin
	extends XToolsUIPlugin {

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

	/**
	 * Retrieves the resource manager for this plug-in.
	 * 
	 * @return The resource manager for this plug-in.
	 */
	public AbstractResourceManager getResourceManager() {
		return EditorResourceManager.getInstance();
	}
}