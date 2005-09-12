/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.emf.type.ui.internal;

import java.util.MissingResourceException;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.emf.type.ui.internal.l10n.ResourceManager;

/**
 * Plug-in class for the UI portion of the Model Element Type framework.
 * <p>
 * This class is not intended to be used by clients.
 * 
 * @author ldamus
 */
public class EMFTypeUIPlugin
	extends XToolsUIPlugin {

	/**
	 * The singleton instance.
	 */
	private static EMFTypeUIPlugin plugin;

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		AbstractResourceManager resourceManager = EMFTypeUIPlugin.getDefault()
			.getResourceManager();
		try {
			return (resourceManager != null) ? resourceManager.getString(key)
				: key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Creates new plug-in runtime object.
	 */
	public EMFTypeUIPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static EMFTypeUIPlugin getDefault() {
		return plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return ResourceManager.getInstance();
	}
}