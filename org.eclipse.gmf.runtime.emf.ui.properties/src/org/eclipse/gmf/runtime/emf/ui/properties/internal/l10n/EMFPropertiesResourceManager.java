/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
