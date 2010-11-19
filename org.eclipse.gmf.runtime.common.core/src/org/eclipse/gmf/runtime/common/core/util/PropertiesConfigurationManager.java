/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.osgi.framework.Bundle;

/**
 * Manages the properties files declared in the textConfiguration extensions
 * 
 * @author myee
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
				if (extensions[i].getContributor().getName().equals(pluginId)) {
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
			
			Bundle bundle = Platform.getBundle(
					element.getDeclaringExtension().getContributor().getName());
			
			assert bundle != null;
			
			URL url = FileLocator.find(bundle, new Path(relativePath), null);
			
			if (url == null) {
				Log.error(CommonCorePlugin.getDefault(),
					CommonCoreStatusCodes.SERVICE_FAILURE,
					"Couldn't find relative path " + relativePath + " in " //$NON-NLS-1$ //$NON-NLS-2$
						+ element.getDeclaringExtension().getContributor().getName());
			}
			
			InputStream is = null;
			// get the file
			try {
				is = url.openStream();
				Properties properties = new Properties();
				properties.load(is);
				propertiesMap.putAll(properties);
				
			} catch (IOException e) {
				handleException(e);
			}
			finally {
				
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						handleException(e);
					}
					
				}
				
			}

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
