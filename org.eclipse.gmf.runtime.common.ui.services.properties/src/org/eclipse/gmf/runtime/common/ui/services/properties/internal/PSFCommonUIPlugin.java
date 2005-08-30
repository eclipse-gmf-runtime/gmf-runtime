/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004,2005.  All Rights Reserved.               |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties.internal;

import org.eclipse.core.runtime.Platform;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.common.ui.services.properties.PropertiesService;
import org.eclipse.gmf.runtime.common.ui.services.properties.internal.l10n.PSFResourceManager;

/**
 * The main plugin class to be used in the desktop.
 * @canBeSeenBy org.eclipse.gmf.runtime.common.ui.services.properties.*
 */
public class PSFCommonUIPlugin
	extends XToolsUIPlugin {

	/**
	 * Extension point name for the properties providers extension point.
	 */
	protected static final String PROPERTY_PROVIDERS_EXT_P_NAME = "propertiesProviders"; //$NON-NLS-1$

	/**
	 * Extension point name for the properties modifiers extension point.
	 */
	protected static final String PROPERTY_MODIFIERS_EXT_P_NAME = "propertyModifiers"; //$NON-NLS-1$	

	//The shared instance.
	private static PSFCommonUIPlugin plugin;

	/**
	 * Constructor.
	 */
	public PSFCommonUIPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static PSFCommonUIPlugin getDefault() {
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
		return PSFResourceManager.getInstance();
	}

	/**
	 * Starts up this plug-in.
	 */
	protected void doStartup() {
		configurePropertiesProviders();
	}

	/**
	 * Configures properties providers based on properties provider extension
	 * configurations.
	 */
	private void configurePropertiesProviders() {
		PropertiesService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				PROPERTY_PROVIDERS_EXT_P_NAME).getConfigurationElements());

		PropertiesService.getInstance().configureModifiers(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				PROPERTY_MODIFIERS_EXT_P_NAME).getConfigurationElements());
	}
}