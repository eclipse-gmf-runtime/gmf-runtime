/******************************************************************************
 * Copyright (c) 2002, 2003, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal;

import java.awt.Color;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author melaasar
 */
public class Draw2dRenderPlugin
	extends AbstractUIPlugin {

	/** the plugin singleton */
	private static Plugin singleton;

	/**
	 * Method getInstance.
	 * 
	 * @return Plugin
	 */
	public static Plugin getInstance() {
		return singleton;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string and is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getInstance().getBundle().getSymbolicName();
	}

	public Draw2dRenderPlugin() {
		super();
		if (singleton == null)
			singleton = this;
		
		// force loading of AWT - bugzilla 119649
		initAWT();
	}

	private void initAWT() {
		Color initColor = new Color(0, 0, 0);
		initColor.getRed();
	}

}