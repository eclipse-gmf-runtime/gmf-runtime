/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;

/**
 * The presentation plugin defines all the artifacts needed for the
 * visualization of modelling diagrams
 * 
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public class Draw2dRenderPlugin
	extends XToolsUIPlugin {

	/** the plugin singleton */
	private static Plugin singleton;

	/**
	 * Method getInstance.
	 * 
	 * @return Plugin
	 */
	public static Plugin getInstance() {
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
	 * Creates the presentation plugin instance
	 * 
	 * @see org.eclipse.core.runtime.Plugin#Plugin()
	 */
	public Draw2dRenderPlugin() {
		super();
		if (singleton == null)
			singleton = this;
	}

	/**
	 * Starts up this presentatuin plug-in.
	 * 
	 * @exception CoreException
	 *                If this presentation plug-in did not start up properly.
	 * 
	 * @see AbstractUIPlugin#startup()
	 */
	protected void doStartup() {
		// empty method
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return null; // TBD
	}

}