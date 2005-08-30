/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.internal.l10n;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager;
import org.eclipse.gmf.runtime.gef.ui.internal.GefPlugin;

/**
 * A singleton resource manager object that manages string, image, font and
 * cursor types of resources for this plug-in
 *
 * @author chmahone
 * @canBeSeenBy org.eclipse.gmf.runtime.gef.ui.*
 *
 */
public class GefResourceManager extends AbstractUIResourceManager {
		 
	public static final String SEG_ADD_MASK_IMAGE = "Seg_Add_Mask.gif"; //$NON-NLS-1$
	public static final String SEG_ADD_IMAGE = "Seg_Add.gif"; //$NON-NLS-1$
	public static final String SEG_MOVE_MASK_IMAGE = "Seg_Move_Mask.gif"; //$NON-NLS-1$
	public static final String SEG_MOVE_IMAGE = "Seg_Move.gif"; //$NON-NLS-1$

	/**
	 * Singleton instance for the resource manager
	 */
	private static AbstractUIResourceManager resourceManager =
		new GefResourceManager();

	/**
	 * Constructor for PresentationResourceManager.
	 */
	private GefResourceManager() {
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
        initializeImageResources();
	}	

	protected void initializeImageResources() {
		super.initializeImageResources();
	}

    /**
     * @see org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager#getPlugin()
     */
    protected Plugin getPlugin() {
        return GefPlugin.getInstance();
    }

}
