package org.eclipse.gmf.runtime.common.ui.action.internal;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class CommonUIActionPlugin extends XToolsUIPlugin {
	/**
	 * The shared instance.
	 */
	private static CommonUIActionPlugin _instance;
	
	/**
	 * The constructor.
	 */
	public CommonUIActionPlugin() {
		_instance = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return a shared instance of <code>CommonUIActionPlugin</code>
	 */
	public static CommonUIActionPlugin getDefault() {
		return _instance;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return null;
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
