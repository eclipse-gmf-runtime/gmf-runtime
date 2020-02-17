/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;

/**
 * Plug-in class for the Model Element Type framework.
 * <p>
 * This class is not intended to be used by clients.
 * 
 * @author ldamus
 */
public class EMFTypePlugin
	extends Plugin {
	
	/**
	 * Flag indicating whether or not the <code>ElementTypeRegistry</code> and
	 * the <code>ClientContextManager</code> should add types, contexts and
	 * bindings declared in extensions from dynamically loaded plugins.
	 * <P>
	 * This feature can only be enabled when running JUnit tests.
	 */
	private static boolean DYNAMIC_AWARE_MODE = false;

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
	
	/**
	 * <B>FOR INTERNAL USE ONLY.  CLIENTS MUST NEVER CALL THIS METHOD.</B>
	 */
	public static void startDynamicAwareMode() {
		// Discourage use of this method by ensuring that it only works when
		// JUnits are running.

		String[] args = Platform.getCommandLineArgs();
		String applicationId = null;

		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].equalsIgnoreCase("-application")) { //$NON-NLS-1$
				applicationId = args[i + 1];
			}
		}

		if (applicationId != null
				&& applicationId.startsWith("org.eclipse.pde.junit")) { //$NON-NLS-1$
			DYNAMIC_AWARE_MODE = true;
		}
	}
	
	/**
	 * <B>FOR INTERNAL USE ONLY.  CLIENTS MUST NEVER CALL THIS METHOD.</B>
	 */
	public static boolean isDynamicAware() {
		return DYNAMIC_AWARE_MODE;
	}
}