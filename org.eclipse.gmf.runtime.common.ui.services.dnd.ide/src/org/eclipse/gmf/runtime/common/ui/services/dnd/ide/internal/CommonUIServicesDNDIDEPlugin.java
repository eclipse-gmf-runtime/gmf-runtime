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

package org.eclipse.gmf.runtime.common.ui.services.dnd.ide.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The common UI Services DND IDE plug-in.
 * 
 * @author wdiu, Wayne Diu
 */
public class CommonUIServicesDNDIDEPlugin
	extends AbstractUIPlugin {

	/**
	 * This plug-in's shared instance.
	 */
	private static CommonUIServicesDNDIDEPlugin INSTANCE;

	/**
	 * Creates a new plug-in runtime object.
	 */
	public CommonUIServicesDNDIDEPlugin() {
		super();
		INSTANCE = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static CommonUIServicesDNDIDEPlugin getDefault() {
		return INSTANCE;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}
}