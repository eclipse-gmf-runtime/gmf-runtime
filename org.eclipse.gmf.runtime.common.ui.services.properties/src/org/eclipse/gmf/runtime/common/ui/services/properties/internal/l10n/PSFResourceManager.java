/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
/*
 * Created on Apr 28, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.gmf.runtime.common.ui.services.properties.internal.l10n;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.services.properties.internal.PSFCommonUIPlugin;


/**
 * Common UI Properties Service Framework Resource manager object
 * 
 * @author nbalaba
 */
public final class PSFResourceManager
	extends AbstractResourceManager {

	/**
	 * Singleton instance of the resource manager.
	 */
	private static PSFResourceManager instance = new PSFResourceManager();

	/**
	 * Initializes me.
	 */
	private PSFResourceManager() {
		super();
	}

	/**
	 * Retrieves the singleton instance of this resource manager.
	 * 
	 * @return The singleton resource manager.
	 */
	public static PSFResourceManager getInstance() {
		return instance;
	}
	
	/**
	 * Retrieves internationalized string.
	 * 
	 * @param key string key
	 * @return internationalized string
	 */
	public static String getI18NString(String key){
	    return getInstance().getString(key);
	
	}

	// implements the inherited method
	protected void initializeResources() {
	    initializeMessageResources();
	}
	
	/**
	 * Convenience method to obtain my plug-in instance.
	 *  
	 * @return my plug-in
	 */
	protected Plugin getPlugin() {
		return PSFCommonUIPlugin.getDefault();
	}

}
