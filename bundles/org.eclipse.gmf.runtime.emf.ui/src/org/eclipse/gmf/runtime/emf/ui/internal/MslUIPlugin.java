/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.internal;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The MSL UI plug-in.
 * 
 * @author khussey
 * 
 */
public class MslUIPlugin
	extends AbstractUIPlugin {

    /**
     * Characters in a URI that separate segments that individually may contain
     * BiDi text.  Use with the {@link org.eclipse.osgi.util.TextProcessor} API
     * for handling URIs in bi-directional locales.
     * 
     * @since 2.0
     */
    public static final String URI_BIDI_SEPARATORS = ":/?#@.!"; //$NON-NLS-1$

	/**
	 * This plug-in's shared instance.
	 */
	private static MslUIPlugin INSTANCE;

	/**
	 * Creates a new plug-in runtime object.
	 */
	public MslUIPlugin() {
		super();
		INSTANCE = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static MslUIPlugin getDefault() {
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