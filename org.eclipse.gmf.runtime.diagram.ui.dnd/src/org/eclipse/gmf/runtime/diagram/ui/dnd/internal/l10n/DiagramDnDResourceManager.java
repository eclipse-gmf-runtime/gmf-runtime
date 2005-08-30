/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.dnd.internal.l10n;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.dnd.internal.DiagramDnDPlugin;

/**
 * @author tmacdoug
 *
 * A singleton resource manager object that manages string, image, font and
 * cursor types of resources for this plug-in
 */

public class DiagramDnDResourceManager extends AbstractUIResourceManager {

	// Images

	/**
	 * Singleton instance for the resource manager
	 */
	private static AbstractResourceManager resourceManager =
		new DiagramDnDResourceManager();

	/**
	 * Constructor for DiagramDnDResourceManager.
	 */
	private DiagramDnDResourceManager() {
		super();
	}

	/**
	 * Return singleton instance of the resource manager
	 * @return AbstractResourceManager
	 */
	public static AbstractResourceManager getInstance() {
		return resourceManager;
	}

	/**
	 * A shortcut method to get localized string
	 * @param key - resource bundle key.
	 * @return - localized string value or a key if the bundle does not contain
	 * 			  this entry
	 */
	public static String getI18NString(String key) {
		return getInstance().getString(key);
	}

	/**
	 * @see com.ibm.xtools.common.l10n.AbstractResourceManager#initializeResources()
	 */
	protected void initializeResources() {
		//do nothing
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager#initializeUIResources()
	 */
	protected void initializeUIResources() {
		initializeImageResources();
		initializeMessageResources();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.internal.l10n.AbstractResourceManager#getPlugin()
	 */
	protected Plugin getPlugin() {
		return DiagramDnDPlugin.getInstance();
	}

}
