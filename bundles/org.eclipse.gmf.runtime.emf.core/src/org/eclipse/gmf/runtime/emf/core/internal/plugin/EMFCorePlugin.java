/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.plugin;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * EMF Core plugin class.
 * 
 * @author rafikj
 */
public class EMFCorePlugin
	extends Plugin {

	private static EMFCorePlugin plugin;
		
	/**
	 * Constructor.
	 */
	public EMFCorePlugin() {
		super();
		plugin = this;
	}

	/**
	 * Get one instance of MSL plugin.
	 */
	public static EMFCorePlugin getDefault() {
		return plugin;
	}

	/**
	 * Get plugin ID.
	 */
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}
}