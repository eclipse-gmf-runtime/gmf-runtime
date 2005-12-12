/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.osgi.framework.BundleContext;

/**
 * The common core plug-in.
 * 
 * @author khussey
 */
public class CommonCorePlugin
	extends Plugin {

	/**
	 * Extension point name for the log listeners extension point.
	 */
	protected static final String LOG_LISTENER_EXT_P_NAME = "logListeners"; //$NON-NLS-1$

	/**
	 * This plug-in's shared instance.
	 */
	private static CommonCorePlugin plugin;

	/**
	 * Creates a new plug-in runtime object.
	 */
	public CommonCorePlugin() {
		super();

		plugin = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static CommonCorePlugin getDefault() {
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
	public void start(BundleContext context)
		throws Exception {
		super.start(context);
		configureLogListeners();
	}

	/**
	 * Configure log listeners for log listeners extension.
	 */
	private void configureLogListeners() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(
			getPluginId(), LOG_LISTENER_EXT_P_NAME);
		ILogListener listener = null;

		try {
			for (int i = 0; i < elements.length; i++) {
				listener = (ILogListener) elements[i]
					.createExecutableExtension("class"); //$NON-NLS-1$
				Platform.getLog(getDefault().getBundle()).addLogListener(
					listener);
			}
		} catch (CoreException e) {
			Trace.catching(CommonCorePlugin.getDefault(),
				CommonCoreDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"configureLogListeners", e); //$NON-NLS-1$
			Log.error(CommonCorePlugin.getDefault(),
				CommonCoreStatusCodes.SERVICE_FAILURE, e.getMessage());
		}
	}

}