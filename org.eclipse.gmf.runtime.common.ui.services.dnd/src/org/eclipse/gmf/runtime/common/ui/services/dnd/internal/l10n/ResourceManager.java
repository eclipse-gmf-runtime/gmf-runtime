///******************************************************************************
// * Copyright (c) 2005 IBM Corporation and others.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// *    IBM Corporation - initial API and implementation 
// ****************************************************************************/
//
//package org.eclipse.gmf.runtime.common.ui.services.dnd.internal.l10n;
//
//import org.eclipse.core.runtime.Plugin;
//
//import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
//import org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager;
//import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDPlugin;
//
///**
// * A singleton resource manager object that manages string, image, font and
// * cursor types of resources for this plug-in.
// * 
// * @author wdiu, based on common.ui's resource manager.
// */
//public final class ResourceManager
//	extends AbstractUIResourceManager {
//
//	/**
//	 * Singleton instance for the resource manager.
//	 */
//	private static AbstractResourceManager resourceManager = new ResourceManager();
//
//	/**
//	 * Constructs a new resource manager.
//	 */
//	private ResourceManager() {
//		super();
//	}
//
//	/**
//	 * Retrieves the singleton instance of this resource manager.
//	 * 
//	 * @return The singleton resource manager.
//	 */
//	//public static AbstractResourceManager getInstance() {
//	//	return resourceManager;
//	//}
//
//	/**
//	 * Retrieves a localized string for the specified key.
//	 * 
//	 * @return A localized string value, or a key if the bundle does not contain
//	 *         this entry.
//	 * @param key
//	 *            The resource bundle key.
//	 */
//	//public static String getI18NString(String key) {
//	//	return getInstance().getString(key);
//	//}
//
//	/**
//	 * Initializes this resource manager's resources.
//	 * 
//	 * @see org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager#initializeResources()
//	 */
//	protected void initializeResources() {
//		//do nothing
//	}
//	
//	/* (non-Javadoc)
//	 * @see org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager#initializeUIResources()
//	 */
//	protected void initializeUIResources() {
//		initializeMessageResources();
//	}	
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.eclipse.gmf.runtime.common.core.internal.l10n.AbstractResourceManager#getPlugin()
//	 */
//	protected Plugin getPlugin() {
//		return CommonUIServicesDNDPlugin.getDefault();
//	}
//
//}
