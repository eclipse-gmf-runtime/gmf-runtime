/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.ui.pde.internal.l10n;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesPlugin;


/**
 * A singleton resource manager object that manages string, image, font and
 * cursor types of resources for this plug-in.
 */
public final class ResourceManager
	extends AbstractResourceManager {

	/**
	 * Singleton instance for the resource manager.
	 */
	private static AbstractResourceManager resourceManager = new ResourceManager();

	/**
	 * Constructs a new resource manager.
	 */
	private ResourceManager() {
		super();
	}

	/**
	 * Retrieves the singleton instance of this resource manager.
	 * 
	 * @return The singleton resource manager.
	 */
	public static AbstractResourceManager getInstance() {
		return resourceManager;
	}

	/**
	 * Retrieves a localized string for the specified key.
	 * 
	 * @return A localized string value, or a key if the bundle does not contain
	 *         this entry.
	 * @param key
	 *            The resource bundle key.
	 */
	public static String getI18NString(String key) {
		return getInstance().getString(key);
	}

	/**
	 * Initializes this resource manager's resources.
	 * 
	 * @see com.rational.xtools.common.core.l10n.AbstractResourceManager#initializeResources()
	 */
	protected void initializeResources() {
		initializeMessageResources();
	}

	/**
	 * @see com.rational.xtools.common.core.l10n.AbstractResourceManager#getPlugin()
	 */
	protected Plugin getPlugin() {
		return GmfExamplesPlugin.getDefault();
	}

}
