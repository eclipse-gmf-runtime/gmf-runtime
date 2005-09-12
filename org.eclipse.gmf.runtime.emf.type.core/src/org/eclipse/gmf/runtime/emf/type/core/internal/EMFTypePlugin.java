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

package org.eclipse.gmf.runtime.emf.type.core.internal;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.ResourceManager;

/**
 * Plug-in class for the Model Element Type framework.
 * <p>
 * This class is not intended to be used by clients.
 * 
 * @author ldamus
 */
public class EMFTypePlugin
	extends XToolsPlugin {

	/**
	 * The shared instance.
	 */
	private static EMFTypePlugin plugin;

	/**
	 * The constructor.
	 */
	public EMFTypePlugin() {
		super();
		plugin = this;
	}

	/**
	 * @return Returns the plugin.
	 */
	public static EMFTypePlugin getPlugin() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getPlugin().getBundle().getSymbolicName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.internal.plugin.XToolsPlugin#doStartup()
	 */
	protected void doStartup() {
		configureElementTypeRegistry();
	}

	/**
	 * Configures validation constraint providers based on the
	 * <tt>constraintProvider</tt> extension configurations.
	 */
	protected void configureElementTypeRegistry() {
		ElementTypeRegistry.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.internal.plugin.XToolsPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return ResourceManager.getInstance();
	}

}