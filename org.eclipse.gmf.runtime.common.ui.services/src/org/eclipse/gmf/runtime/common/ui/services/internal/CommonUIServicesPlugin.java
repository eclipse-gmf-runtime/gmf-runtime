/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.internal;

import org.eclipse.core.runtime.Platform;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionService;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.common.ui.services.marker.MarkerNavigationService;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;

/**
 * The Common UI Services plug-in.
 * 
 * @author Wayne Diu, wdiu
 */
public class CommonUIServicesPlugin
	extends XToolsUIPlugin {

	/**
	 * Extension point name for the editor providers extension point.
	 */
	protected final static String EDITOR_PROVIDERS_EXT_P_NAME = "editorProviders"; //$NON-NLS-1$ 

	/**
	 * Extension point name for the marker navigation providers extension point.
	 */
	protected static final String MARKER_NAVIGATION_PROVIDERS_EXT_P_NAME = "markerNavigationProviders"; //$NON-NLS-1$

	/**
	 * Extension point name for the icon decriptor providers extension point.
	 */
	protected static final String ICON_PROVIDERS_EXT_P_NAME = "iconProviders"; //$NON-NLS-1$

	/**
	 * Extension point name for the parser providers extension point.
	 */
	protected static final String PARSER_PROVIDERS_EXT_P_NAME = "parserProviders"; //$NON-NLS-1$ 

	/**
	 * Extension point name for the element selection extension point.
	 */
	protected static final String ELEMENT_SELECTION_PROVIDERS_EXT_P_NAME = "elementSelectionProviders"; //$NON-NLS-1$ 

	/**
	 * This plug-in's shared instance.
	 */
	private static CommonUIServicesPlugin plugin;

	/**
	 * Creates a new plug-in runtime object for the given plug-in descriptor.
	 */
	public CommonUIServicesPlugin() {
		super();

		plugin = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static CommonUIServicesPlugin getDefault() {
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

	/**
	 * Retrieves the resource manager for this plug-in.
	 * 
	 * @return The resource manager for this plug-in.
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		// TODO ResourceManager.getInstance();
		return null;
	}

	/**
	 * Starts up this plug-in.
	 */
	protected void doStartup() {
		configureEditorProviders();
		configureMarkerNavigationProviders();
		configureIconProviders();
		configureParserProviders();
		configureElementSelectionProviders();
	}

	/**
	 * Configures editor providers based on editor provider extension
	 * configurations.
	 * 
	 */
	private void configureEditorProviders() {
		EditorService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				EDITOR_PROVIDERS_EXT_P_NAME).getConfigurationElements());

	}

	/**
	 * Configures marker navigation providers based on marker navigation
	 * provider extension configurations.
	 * 
	 */
	private void configureMarkerNavigationProviders() {
		MarkerNavigationService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				MARKER_NAVIGATION_PROVIDERS_EXT_P_NAME)
				.getConfigurationElements());
	}

	/**
	 * Configures icon providers based on icon provider extension
	 */
	private void configureIconProviders() {
		IconService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				ICON_PROVIDERS_EXT_P_NAME).getConfigurationElements());
	}

	/**
	 * Configures parser providers based on parser provider extension
	 * configurations.
	 * 
	 */
	private void configureParserProviders() {
		ParserService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				PARSER_PROVIDERS_EXT_P_NAME).getConfigurationElements());
	}

	/**
	 * Configures element selection providers based on element selection
	 * provider extension configurations.
	 * 
	 */
	private void configureElementSelectionProviders() {
		ElementSelectionService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				ELEMENT_SELECTION_PROVIDERS_EXT_P_NAME)
				.getConfigurationElements());
	}
}