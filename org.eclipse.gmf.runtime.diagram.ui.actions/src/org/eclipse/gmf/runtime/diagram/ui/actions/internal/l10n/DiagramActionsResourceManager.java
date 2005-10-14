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

package org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DiagramActionsPlugin;

/**
 * A singleton resource manager object that manages string, image, font and
 * cursor types of resources for this plug-in
 *
 * @author chmahone
 *
 */
public class DiagramActionsResourceManager
	extends AbstractUIResourceManager {

	public static final String DESC_NOTE_ATTACHMENT = "noteattachment.gif";//$NON-NLS-1$
	/**
	 * Singleton instance for the resource manager
	 */
	private static AbstractUIResourceManager resourceManager = new DiagramActionsResourceManager();
	

	/**
	 * Constructor for PresentationResourceManager.
	 */
	private DiagramActionsResourceManager() {
		super();
	}

	/**
	 * Return singleton instance of the resource manager
	 * @return AbstractResourceManager
	 */
	public static AbstractUIResourceManager getInstance() {
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
		initializeMessageResources();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager#getPlugin()
	 */
	protected Plugin getPlugin() {
		return DiagramActionsPlugin.getInstance();
	}

}