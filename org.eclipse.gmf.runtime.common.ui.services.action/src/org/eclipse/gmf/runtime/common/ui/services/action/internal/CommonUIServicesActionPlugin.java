/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.action.internal;

import org.eclipse.core.runtime.Platform;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.ActionFilterService;
import org.eclipse.gmf.runtime.common.ui.services.action.global.GlobalActionHandlerService;

/**
 * The Action plugin for Common UI Services.
 */
public class CommonUIServicesActionPlugin
	extends XToolsUIPlugin {

	/**
	 * Extension point name for the action filter providers extension point.
	 */
	protected static final String ACTION_FILTER_PROVIDERS_EXT_P_NAME = "actionFilterProviders"; //$NON-NLS-1$

	/**
	 * Extension point name for global action handler providers extension point.
	 */
	protected static final String GLOBAL_ACTION_HANDLER_PROVIDERS_EXT_P_NAME = "globalActionHandlerProviders"; //$NON-NLS-1$

	/**
	 * Extension point name for the contribution item providers extension point.
	 */
	protected static final String CONTRIBUTION_ITEM_PROVIDERS_EXT_P_NAME = "contributionItemProviders"; //$NON-NLS-1$

	/**
	 * The singleton instance.
	 */
	private static CommonUIServicesActionPlugin _instance;

	/**
	 * Creates a new plug-in runtime object.
	 */
	public CommonUIServicesActionPlugin() {
		_instance = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#doStartup()
	 */
	protected void doStartup() {
		configureActionFilterProviders();
		configureGlobalActionHandlerProviders();
		configureContributionItemProviders();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return null;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static CommonUIServicesActionPlugin getDefault() {
		return _instance;
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
	 * Configures action filter providers based on action filter provider
	 * extension configurations.
	 */
	private void configureActionFilterProviders() {
		ActionFilterService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				ACTION_FILTER_PROVIDERS_EXT_P_NAME).getConfigurationElements());
	}

	/**
	 * Configures global action handler providers based on global action handler
	 * provider extension configurations.
	 */
	private void configureGlobalActionHandlerProviders() {
		GlobalActionHandlerService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				GLOBAL_ACTION_HANDLER_PROVIDERS_EXT_P_NAME)
				.getConfigurationElements());

	}

	/**
	 * Configures marker navigation providers based on marker navigation
	 * provider extension configurations.
	 */
	private void configureContributionItemProviders() {
		ContributionItemService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				CONTRIBUTION_ITEM_PROVIDERS_EXT_P_NAME)
				.getConfigurationElements());
	}
}