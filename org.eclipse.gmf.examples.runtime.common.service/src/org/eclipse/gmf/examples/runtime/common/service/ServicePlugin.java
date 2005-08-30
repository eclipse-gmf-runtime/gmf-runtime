/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.common.service;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import org.eclipse.gmf.examples.runtime.common.service.application.WidgetService;

/**
 * The main plugin class to be used in the desktop.
 */
public class ServicePlugin extends AbstractUIPlugin {
	//The shared instance.
	private static ServicePlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	/**
	 * The widget service extension point id
	 */
	private final static String WIDGET_SERVICE_EXTENSION_POINT = "widgetProviders"; //$NON-NLS-1$	
	
	
	/**
	 * The constructor.
	 */
	public ServicePlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("org.eclipse.gmf.examples.runtime.common.service.ServicePluginResources");//$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		configureWidgetProviders();		
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance of <code>ServicePlugin</code>
	 */
	public static ServicePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 * 
	 * @param key resource key
	 * @return resource, 'key' if not found
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ServicePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
<<<<<<< ServicePlugin.java
	 * Returns the plugin's resource bundle.
	 * 
	 * @return resourceBundle
=======
	 * Returns the plugin's resource bundle.
	 * 
	 * @return plugin's resource bundle
>>>>>>> 1.2
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string and is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}
	
	/**
	 * Configures Widget providers based in the Widget
	 * providers extension configurations.
	 *  
	 */
	public void configureWidgetProviders() {
		WidgetService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				WIDGET_SERVICE_EXTENSION_POINT).getConfigurationElements());		
	}	
}
