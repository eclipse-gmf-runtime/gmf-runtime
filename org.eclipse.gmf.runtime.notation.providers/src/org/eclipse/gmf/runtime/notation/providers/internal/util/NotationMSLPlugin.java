/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.notation.providers.internal.util;

import org.eclipse.core.runtime.IPluginDescriptor;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin;
import org.eclipse.gmf.runtime.notation.providers.internal.l10n.ResourceManager;

/**
 * The plug-in for EMF implementation of UML.
 * 
 * @author mgoyal
 */
public class NotationMSLPlugin
	extends XToolsPlugin {

	/**
	 * Extension point names.
	 */
	protected static final String UML2_PROFILES_EXT_P_NAME = "UMLProfiles"; //$NON-NLS-1$

	protected static final String UML2_LIBRARIES_EXT_P_NAME = "UMLLibraries"; //$NON-NLS-1$

	/**
	 * This plug-in's shared instance.
	 */
	private static NotationMSLPlugin plugin;

	/**
	 * Creates a new plug-in runtime object for the given plug-in descriptor.
	 * 
	 * @param descriptor
	 *            The plug-in descriptor.
	 */
	public NotationMSLPlugin(IPluginDescriptor descriptor) {
		super(descriptor);

		plugin = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static NotationMSLPlugin getDefault() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getDefault().getDescriptor().getUniqueIdentifier();
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
		NotationAdapterFactoryManager.init();
	}
}