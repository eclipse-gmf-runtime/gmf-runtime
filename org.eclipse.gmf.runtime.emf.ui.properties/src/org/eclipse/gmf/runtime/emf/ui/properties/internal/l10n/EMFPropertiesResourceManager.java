/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.properties.internal.l10n;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager;
import org.eclipse.gmf.runtime.emf.ui.properties.internal.EMFPropertiesPlugin;

/**
 * @author nbalaba
 */
public class EMFPropertiesResourceManager extends AbstractUIResourceManager {
	
	
	/**
	 * Singleton instance of the resource manager.
	 */
	private static EMFPropertiesResourceManager instance = new EMFPropertiesResourceManager();

	/**
	 * Initializes me.
	 */
	private EMFPropertiesResourceManager() {
		super();
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.internal.l10n.AbstractResourceManager#getPlugin()
	 */
	protected Plugin getPlugin() {
		return EMFPropertiesPlugin.getDefault();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.internal.l10n.AbstractResourceManager#initializeResources()
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
	 * Retrieves the singleton instance of this resource manager.
	 * 
	 * @return The singleton resource manager.
	 */
	public static EMFPropertiesResourceManager getInstance() {
		return instance;
	}

}
