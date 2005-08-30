/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;

/**
 * Manages the properties files declared in the textConfiguration extensions
 * 
 * @author myee
 * @canBeSeenBy %partners
 */
public class PropertiesConfigurationManager {

	/**
	 * Extension point name for the properties configuration extension point.
	 */
	private static final String PROPERTIES_CONFIGURATION_EXT_P_NAME = "propertiesConfiguration"; //$NON-NLS-1$ 

	/**
	 * The path attribute describing the relative location to the properties
	 * file
	 */
	private static final String PATH_ATTRIBUTE = "path";//$NON-NLS-1$

	/**
	 * Maps plug-in id to its properties map
	 */
	private static Map pluginIdToPropertiesMap = null;

	/**
	 * private constructor.
	 */
	private PropertiesConfigurationManager() {
		super();
	}
	
	/**
	 * Gets the string for the given key
	 * 
	 * @param pluginId
	 *            the id of the plug-in defining the string
	 * @param key
	 *            the key
	 * @return the string, or <code>null</code> if the mapping is not found
	 */
	public static String getString(String pluginId, String key) {
		Map propertiesMap = (Map) getPluginIdToPropertiesMap().get(pluginId);
		if (propertiesMap == null) {
			// lazy initialize the properties map for the given plug-in
			propertiesMap = new HashMap();
			getPluginIdToPropertiesMap().put(pluginId, propertiesMap);

			// load the properties files for all extensions in the plug-in
			IExtension[] extensions = Platform.getExtensionRegistry()
				.getExtensionPoint(CommonCorePlugin.getPluginId(),
					PROPERTIES_CONFIGURATION_EXT_P_NAME).getExtensions();
			for (int i = 0; i < extensions.length; ++i) {
				if (extensions[i].getNamespace().equals(pluginId)) {
					loadProperties(propertiesMap, extensions[i]
						.getConfigurationElements());
				}
			}
		}

		return (String) propertiesMap.get(key);
	}

	/**
	 * Gets the pluginIdToPropertiesMap map
	 * 
	 * @return the pluginIdToPropertiesMap map
	 */
	private static Map getPluginIdToPropertiesMap() {
		if (pluginIdToPropertiesMap == null) {
			// lazy initialize the map
			pluginIdToPropertiesMap = new HashMap();
		}
		return pluginIdToPropertiesMap;
	}

	/**
	 * Lods the properties files described by the configuration elements.
	 * 
	 * @param propertiesMap
	 *            the map to hold the properties files
	 * @param elements
	 *            The configuration elements describing the properties files.
	 */
	private static void loadProperties(Map propertiesMap,
			IConfigurationElement[] elements) {

		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];

			// get the relative path of the properties file
			String relativePath = element.getAttribute(PATH_ATTRIBUTE);

			// get the file
			URL installURL = Platform.getBundle(
				element.getDeclaringExtension().getNamespace()).getEntry("/");//$NON-NLS-1$
			URL resolveURL = null;
			try {
				resolveURL = Platform.resolve(installURL);
			} catch (IOException e1) {
				// shouldn't happen
				assert (false);
			}
			String fullPath = resolveURL.getFile() + relativePath;

			// load the properties file
			Properties properties = new Properties();
			try {
				FileInputStream stream = new FileInputStream(fullPath);
				properties.load(stream);
			} catch (FileNotFoundException e) {
				handleException(e);
				continue;
			} catch (IOException e) {
				handleException(e);
				continue;
			}

			// add the properties map
			propertiesMap.putAll(properties);
		}
	}

	/**
	 * Log and trace the exception
	 * 
	 * @param e
	 *            the exception
	 */
	private static void handleException(Exception e) {
		Trace.catching(CommonCorePlugin.getDefault(),
			CommonCoreDebugOptions.EXCEPTIONS_CATCHING,
			PropertiesConfigurationManager.class, "configureProperties", e); //$NON-NLS-1$
		Log.error(CommonCorePlugin.getDefault(),
			CommonCoreStatusCodes.SERVICE_FAILURE, e.getMessage(), e);
	}

}