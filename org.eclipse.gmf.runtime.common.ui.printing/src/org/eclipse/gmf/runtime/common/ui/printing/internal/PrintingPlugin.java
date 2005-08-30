/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.printing.internal;

import org.eclipse.core.runtime.Plugin;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author wdiu Wayne Diu
 */
public class PrintingPlugin
	extends Plugin {

	/**
	 * The shared instance.
	 */
	private static PrintingPlugin plugin;

	/**
	 * The constructor.
	 */
	public PrintingPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return PrintPreviewPlugin
	 */
	public static PrintingPlugin getDefault() {
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