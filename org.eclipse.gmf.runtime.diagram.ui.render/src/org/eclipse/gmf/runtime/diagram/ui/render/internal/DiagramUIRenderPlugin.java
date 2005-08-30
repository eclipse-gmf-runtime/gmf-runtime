package org.eclipse.gmf.runtime.diagram.ui.render.internal;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.render.l10n.ResourceManager;

/**
 * The Diagram UI Render plug-in.
 * 
 * @author cmahoney
 */
public class DiagramUIRenderPlugin
	extends XToolsUIPlugin {

	/**
	 * The shared instance.
	 */
	private static DiagramUIRenderPlugin plugin;

	/**
	 * The constructor.
	 * 
	 * @see org.eclipse.core.runtime.Plugin#Plugin()
	 */
	public DiagramUIRenderPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the plugin instance
	 */
	public static DiagramUIRenderPlugin getInstance() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getInstance().getBundle().getSymbolicName();
	}

	/**
	 * Retrieves the resource manager for this plug-in.
	 * 
	 * @return The resource manager for this plug-in.
	 */
	public AbstractResourceManager getResourceManager() {
		return ResourceManager.getInstance();
	}

}