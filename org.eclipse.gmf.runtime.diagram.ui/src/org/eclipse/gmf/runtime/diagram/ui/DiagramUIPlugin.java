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

package org.eclipse.gmf.runtime.diagram.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.diagram.core.listener.PresentationListener;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.DecoratorService;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpolicy.EditPolicyService;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutService;
import org.eclipse.gmf.runtime.diagram.ui.preferences.AppearancePreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.preferences.ConnectorsPreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.preferences.DiagramsPreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.preferences.PrintingPreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.preferences.RulerGridPreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteService;

/**
 * The DiagramUI  plugin defines all the artifacts needed for the
 * visualization of modelling diagrams
 * 
 * @author melaasar
 */
public class DiagramUIPlugin
	extends XToolsUIPlugin {

	/**
	 * The layout service extension point id
	 */
	private final static String LAYOUT_SERVICE_EXTENSION_POINT = "layoutProviders"; //$NON-NLS-1$ 

	/**
	 * The editpart service extension point id
	 */
	private final static String EDITPART_SERVICE_EXTENSION_POINT = "editpartProviders"; //$NON-NLS-1$ 

	 

	/**
	 * The palette service extension point id
	 */
	private final static String PALETTE_SERVICE_EXTENSION_POINT = "paletteProviders"; //$NON-NLS-1$ 

	
	/**
	 * The decoration service extension point id
	 */
	private final static String DECORATOR_SERVICE_EXTENSION_POINT = "decoratorProviders"; //$NON-NLS-1$ 

	/**
	 * The editpolicy service extension point id
	 */
	private final static String EDITPOLICY_SERVICE_EXTENSION_POINT = "editpolicyProviders"; //$NON-NLS-1$

	/**
	 * the plugin singleton
	 */
	private static Plugin singleton;

	/**
	 * gets the plugin singleton
	 * 
	 * @return the plugin singleton
	 */
	public static XToolsUIPlugin getInstance() {
		return (XToolsUIPlugin) singleton;
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
	 * Creates the Diagram plugin instance
	 * 
	 * @see org.eclipse.core.runtime.Plugin#Plugin()
	 */
	public DiagramUIPlugin() {
		super();
		if (singleton == null) {
			singleton = this;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#doStartup()
	 */
	protected void doStartup() {
		super.doStartup();

		PresentationListener.getInstance().startListening();
		
		configureEditPartProviders();
		configurePaletteProviders();
		configureDiagramLayoutProviders();
		configureDecorationProviders();
		configureEditPolicyProviders();		
		
		initializeDefaultDiagramPreferenceStore();
		
		// TODO: Move to Modeler/Visualizer plug-in.
		PreferencesHint.registerPreferenceStore(
			PreferencesHint.MODELING, getPreferenceStore());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#doShutdown()
	 */
	protected void doShutdown() {
		PresentationListener.getInstance().stopListening();
		super.doShutdown();
	}

	/**
	 * Configures diagram layout providers for the presentation plug-in based on
	 * layout provider extension configurations.
	 */
	private void configureDiagramLayoutProviders() {
		LayoutService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(
				getPluginId(), LAYOUT_SERVICE_EXTENSION_POINT)
				.getConfigurationElements());
	}

	/**
	 * Configures editpart providers for the presentation plug-in based on
	 * editpart provider extension configurations.
	 */
	private void configureEditPartProviders() {
		EditPartService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				EDITPART_SERVICE_EXTENSION_POINT).getConfigurationElements());
	}

	

	/**
	 * Configures palette providers for the presentation plug-in based on
	 * palette provider extension configurations.
	 */
	private void configurePaletteProviders() {
		PaletteService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				PALETTE_SERVICE_EXTENSION_POINT).getConfigurationElements());
	}

	

	/**
	 * Configures decoration providers for the presentation plug-in based on
	 * decoration provider extension configurations.
	 */
	private void configureDecorationProviders() {
		DecoratorService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(
				getPluginId(), DECORATOR_SERVICE_EXTENSION_POINT)
				.getConfigurationElements());				
	}

	/**
	 * Configures editpolicy providers for the presentation plug-in based on
	 * editpolicy provider extension configurations.
	 */
	private void configureEditPolicyProviders() {
		EditPolicyService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				EDITPOLICY_SERVICE_EXTENSION_POINT).getConfigurationElements());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return null; // TBD
	}
	

	/**
	 * Initializes the preference store to be used as default values when a
	 * diagram editor does not have any preferences hooked up. This is the
	 * preference store registered with {@link PreferencesHint#USE_DEFAULTS}.
	 */
	private void initializeDefaultDiagramPreferenceStore() {
		IPreferenceStore defaultStore = new PreferenceStore();

		DiagramsPreferencePage.initDefaults(defaultStore);
		RulerGridPreferencePage.initDefaults(defaultStore);
		AppearancePreferencePage.initDefaults(defaultStore);
		ConnectorsPreferencePage.initDefaults(defaultStore);
		PrintingPreferencePage.initDefaults(defaultStore);

		PreferencesHint.registerPreferenceStore(
			PreferencesHint.USE_DEFAULTS, defaultStore);
	}

}