package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n.GeoshapesResourceManager;

/**
 * The main plugin class to be used in the desktop.
 */
/*
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.geoshapes.*
 */
public class GeoshapesPlugin extends XToolsUIPlugin {

	//The shared instance.
	private static GeoshapesPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public GeoshapesPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static GeoshapesPlugin getDefault() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getDefault().getDescriptor().getUniqueIdentifier();
	}
	
	/* (non-Javadoc)
	 * @see com.rational.xtools.common.ui.plugin.XToolsUIPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return GeoshapesResourceManager.getInstance();
	}

}
