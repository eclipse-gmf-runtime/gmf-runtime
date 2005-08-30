/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorResourceManager;


/**
 * A singleton resource manager object that manages string, image, font and
 * cursor types of resources for this plug-in
 * 
 * @author qili
 */
public class LogicResourceManager extends EditorResourceManager{
	/**
	 * Constructor for LogicResourceManager.
	 */
	private LogicResourceManager() {
		super();
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
	 * Singleton instance for the resource manager
	 */
	private static EditorResourceManager logicResourceManager =
		new LogicResourceManager();

	/**
	 * Return singleton instance of the resource manager
	 * @return AbstractResourceManager
	 */
	public static AbstractUIResourceManager getInstance() {
		return logicResourceManager;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager#getPlugin()
	 */
	protected Plugin getPlugin() {
		return LogicDiagramPlugin.getInstance();
	}
}
