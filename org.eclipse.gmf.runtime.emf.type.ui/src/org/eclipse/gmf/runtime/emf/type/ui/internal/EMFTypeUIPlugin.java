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

import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;

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
}