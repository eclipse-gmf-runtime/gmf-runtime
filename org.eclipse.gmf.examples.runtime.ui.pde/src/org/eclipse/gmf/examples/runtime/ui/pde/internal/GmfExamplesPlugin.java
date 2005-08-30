/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.ui.pde.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class GmfExamplesPlugin
	extends AbstractUIPlugin {

	/** The shared instance. */
	private static GmfExamplesPlugin fPlugin;

	/**
	 * Constructor.
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin
	 */
	public GmfExamplesPlugin() {
		super();
		fPlugin = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return The plugin instance of <code>GmfExamplesPlugin</code>
	 */
	public static GmfExamplesPlugin getDefault() {
		return fPlugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string and is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}
}