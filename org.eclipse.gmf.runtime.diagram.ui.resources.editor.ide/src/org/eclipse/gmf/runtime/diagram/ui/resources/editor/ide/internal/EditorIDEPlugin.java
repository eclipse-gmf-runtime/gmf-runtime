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

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


/**
 * Editor IDE Plug-in
 * 
 * @author Wayne Diu, wdiu
 */
public class EditorIDEPlugin extends XToolsUIPlugin {

	/**
	 * The shared instance.
	 */
	private static EditorIDEPlugin plugin;

	/**
	 * The constructor.
	 *
	 * @see XToolsUIPlugin#XToolsUIPlugin()
	 */
	public EditorIDEPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the plugin instance
	 */
	public static EditorIDEPlugin getInstance() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 * 
	 * @return the workspace
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
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
	 * Returns active workbench window
	 * 
	 * @return the active workbench window
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	/**
	 * Returns active page
	 * 
	 * @return the active page
	 */
	public static IWorkbenchPage getActivePage() {
		return getActiveWorkbenchWindow().getActivePage();
	}

	/**
	 * Returns active editor
	 * 
	 * @return the active editor
	 */
	public static IEditorPart getActiveEditor() {
		return getActivePage().getActiveEditor();
	}

	/**
	 * Starts up this wizards plug-in.
	 */
	public void doStartup() {
		super.doStartup();
	}

}
