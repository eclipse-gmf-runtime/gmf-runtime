/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.notation.providers.internal.util;

import org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin;

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
	public NotationMSLPlugin() {
		super();
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
		return getDefault().getBundle().getSymbolicName();
	}

	/**
	 * Starts up this plug-in.
	 */
	protected void doStartup() {
		NotationAdapterFactoryManager.init();
	}
}