/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.plugin;

import org.eclipse.core.runtime.Platform;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MSLPathmap;
import org.eclipse.gmf.runtime.emf.core.internal.services.metamodel.MetamodelSupportService;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLAdapterFactoryManager;

/**
 * MSL plugin class.
 * 
 * @author rafikj
 */
public class MSLPlugin
	extends XToolsPlugin {

	private static MSLPlugin plugin;

	// extension points.
	protected static final String METAMODEL_PROVIDERS_EXT_P_NAME = "MetaModelProviders"; //$NON-NLS-1$

	protected static final String PATHMAPS_EXT_P_NAME = "Pathmaps"; //$NON-NLS-1$
		
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
		return getDefault().getBundle().getSymbolicName();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return ResourceManager.getInstance();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin#doStartup()
	 */
	protected void doStartup() {
		MetamodelSupportService.configure(Platform.getExtensionRegistry()
			.getExtensionPoint(getPluginId(), METAMODEL_PROVIDERS_EXT_P_NAME)
			.getConfigurationElements());

		MSLPathmap.configure(Platform.getExtensionRegistry().getExtensionPoint(
			getPluginId(), PATHMAPS_EXT_P_NAME).getConfigurationElements());
		
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