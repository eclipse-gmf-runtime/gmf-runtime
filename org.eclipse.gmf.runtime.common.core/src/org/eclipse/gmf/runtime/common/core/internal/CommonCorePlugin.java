/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.Platform;

import org.eclipse.gmf.runtime.common.core.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin;

/**
 * The common core plug-in.
 * 
 * @author khussey
 */
public class CommonCorePlugin
	extends XToolsPlugin {

	/**
	 * Extension point name for the log listeners extension point.
	 */
	protected static final String LOG_LISTENER_EXT_P_NAME = "logListeners"; //$NON-NLS-1$
	
	/**
	 * This plug-in's shared instance.
	 */
	private static CommonCorePlugin plugin;

//	/**
//	 * The navigator category registry for loading and retrieving the navigator
//	 * category extensions in the workspace.
//	 */
//	private NavigatorCategoryRegistry navigatorCategoryRegistry;
//
//	/**
//	 * The navigator content type registry for loading and retrieving the
//	 * navigator content type extensions in the workspace.
//	 */
//	private NavigatorContentTypeRegistry navigatorContentTypeRegistry;

	/**
	 * Creates a new plug-in runtime object.
	 */
	public CommonCorePlugin() {
		super();

		plugin = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static CommonCorePlugin getDefault() {
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
	 * @see org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return ResourceManager.getInstance();
	}

	/**
	 * Starts up this plug-in.
	 */
	protected void doStartup() {
		configureLogListeners();
		//configureIconProviders();
		//configureViewerContentProviders();
		//configureViewerSorterProviders();
		//configureViewerFilterProviders();
	}
	
	/**
	 * Configure log listeners for log listeners extension.
	 */
	private void configureLogListeners() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(getPluginId(), LOG_LISTENER_EXT_P_NAME);
		ILogListener listener = null;
	
		try {
			for (int i = 0; i < elements.length; i++) {
				listener = (ILogListener) elements[i].createExecutableExtension("class"); //$NON-NLS-1$
				Platform.getLog(getDefault().getBundle()).addLogListener(listener);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * Configures icon providers based on icon provider extension
//	 */
//	private void configureIconProviders() {
//		IconService.getInstance().configureProviders(
//			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
//				ICON_PROVIDERS_EXT_P_NAME).getConfigurationElements());
//	}


//	/**
//	 * Configures content providers based on content provider extension
//	 * configurations.
//	 *  
//	 */
//	private void configureViewerContentProviders() {
//		ViewerContentService.getInstance().configureProviders(
//			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
//				ViewerContentService.getInstance().getExtensionPointId())
//				.getConfigurationElements());
//	}
//
//	/**
//	 * Configures content providers based on sorter provider extension
//	 * configurations.
//	 *  
//	 */
//	private void configureViewerSorterProviders() {
//		ViewerSorterService.getInstance().configureProviders(
//			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
//				ViewerSorterService.getInstance().getExtensionPointId())
//				.getConfigurationElements());
//	}
//
//	/**
//	 * Configures content providers based on sorter provider extension
//	 * configurations.
//	 *  
//	 */
//	private void configureViewerFilterProviders() {
//		ViewerFilterService.getInstance().configureProviders(
//			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
//				ViewerFilterService.getInstance().getExtensionPointId())
//				.getConfigurationElements());
//	}
//
//	/**
//	 * Returns the navigator category registry.
//	 * 
//	 * @return the navigator category registry.
//	 */
//	public NavigatorCategoryRegistry getNavigatorCategoryRegistry() {
//		if (navigatorCategoryRegistry == null) {
//			navigatorCategoryRegistry = new NavigatorCategoryRegistry();
//			navigatorCategoryRegistry.load();
//		}
//		return navigatorCategoryRegistry;
//	}
//
//	/**
//	 * Returns the navigator content type registry.
//	 * 
//	 * @return the navigator content type registry.
//	 */
//	public NavigatorContentTypeRegistry getNavigatorContentTypeRegistry() {
//		if (navigatorContentTypeRegistry == null) {
//			navigatorContentTypeRegistry = new NavigatorContentTypeRegistry();
//			navigatorContentTypeRegistry.load();
//		}
//		return navigatorContentTypeRegistry;
//	}
}