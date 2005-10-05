/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.internal;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.emf.ui.internal.l10n.ResourceManager;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * The MSL UI plug-in.
 * 
 * @author khussey
 *  
 */
public class MslUIPlugin
	extends XToolsUIPlugin {

	/**
	 * This plug-in's shared instance.
	 */
	private static MslUIPlugin plugin;

	/**
	 * Creates a new plug-in runtime object.
	 */
	public MslUIPlugin() {
		super();

		plugin = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static MslUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getDefault().getSymbolicName();
	}

	/**
	 * Retrieves the resource manager for this plug-in.
	 * 
	 * @return The resource manager for this plug-in.
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return ResourceManager.getInstance();
	}

	/**
	 * Returns the currently active window for the workbench (if any).
	 * 
	 * @return The active workbench window, or null if the currently active
	 *         window is not a workbench window
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	/**
	 * Returns the currently active page for the active workbench window.
	 * 
	 * @return The active page, or null if none
	 */
	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}
		return window.getActivePage();
	}
}