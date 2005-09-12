/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.core.internal;

import org.eclipse.core.runtime.Platform;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticService;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Yasser Lulu
 */
public class DiagramPlugin
	extends XToolsPlugin {

	/**
	 * The semantic service extension point id
	 */
	private final static String SEMANTIC_SERVICE_EXTENSION_POINT = "semanticProviders"; //$NON-NLS-1$

	/**
	 * The view service extension point id
	 */
	private final static String VIEW_SERVICE_EXTENSION_POINT = "viewProviders"; //$NON-NLS-1$

	//The shared instance.
	private static DiagramPlugin plugin;

	/**
	 * The constructor.
	 */
	public DiagramPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static DiagramPlugin getInstance() {
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
		return DiagramResourceManager.getInstance();
	}

	/**
	 * Configures semantic providers for the presentation plug-in based on
	 * semantic provider extension configurations.
	 */
	private void configureSemanticProviders() {
		SemanticService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				SEMANTIC_SERVICE_EXTENSION_POINT).getConfigurationElements());
	}

	/**
	 * Configures view providers for the presentation plug-in based on view
	 * provider extension configurations.
	 */
	private void configureViewProviders() {
		ViewService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				VIEW_SERVICE_EXTENSION_POINT).getConfigurationElements());
	}

	/** Starts up this presentatuin plug-in. */
	protected void doStartup() {
		super.doStartup();
		configureSemanticProviders();
		configureViewProviders();
	}

}