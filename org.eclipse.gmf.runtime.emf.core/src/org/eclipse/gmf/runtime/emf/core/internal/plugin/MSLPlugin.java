/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.plugin;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLAdapterFactoryManager;

/**
 * MSL plugin class.
 * 
 * @author rafikj
 */
public class MSLPlugin
	extends XToolsPlugin {

	private static MSLPlugin plugin;
		
	/**
	 * Constructor.
	 */
	public MSLPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Get one instance of MSL plugin.
	 */
	public static MSLPlugin getDefault() {
		return plugin;
	}

	/**
	 * Get plugin ID.
	 */
	public static String getPluginId() {
		return getDefault().getSymbolicName();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin#doStartup()
	 */
	protected void doStartup() {
		MSLAdapterFactoryManager.init();

		// This event listener must be registered _after_ all of the rest of
		//  the MSL stuff is initialized. Otherwise, strange problems occur when
		//  tracing is turned on.
		if (Trace.shouldTrace(MSLPlugin.getDefault(),
			MSLDebugOptions.EVENTS)) {
			new TraceEventListener();
		}
	}
}