/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n;

import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.gmf.runtime.common.core.l10n.EmptyResourceBundle;
import org.eclipse.gmf.runtime.common.ui.l10n.AbstractUIResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.GeoshapesPlugin;

/**
 * @author jschofie
 *
 * The GeoshapesResourceManager is a singleton used to obtain internationalization
 * strings.
 */
public class GeoshapesResourceManager extends AbstractUIResourceManager {

	private static GeoshapesResourceManager self = null;

	/**
	 * Constructor made private to limit creatation
	 */
	private GeoshapesResourceManager() {
		super();
	}

	/**
	 * This is a static method used to get the ResourceManager singleton
	 * @return GeoshapesResourceManager
	 */
	public static GeoshapesResourceManager getInstance() {
		
		// IF the singleton hasn't been created...
		if( self == null ) {

			// Create it
			self = new GeoshapesResourceManager();
		}

		// Return the reference to the singleton
		return self;
	}

	/* (non-Javadoc)
	 * @see com.rational.xtools.common.core.l10n.AbstractResourceManager#getPlugin()
	 */
	protected Plugin getPlugin() {
		return GeoshapesPlugin.getDefault();
	}

	/* (non-Javadoc)
	 * @see com.rational.xtools.common.core.l10n.AbstractResourceManager#initializeResources()
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

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.internal.l10n.AbstractResourceManager#createMessagesBundle()
	 */
	protected ResourceBundle createMessagesBundle() {
		try {
			return PropertyResourceBundle.getBundle(getMessagesBundleName());
		} catch (MissingResourceException mre) {
			return new EmptyResourceBundle(getMessagesBundleName());
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.internal.l10n.AbstractResourceManager#getMessagesBundleDefaultName()
	 */
	protected String getMessagesBundleDefaultName() {
		return getClass().getPackage().getName() + ".messages"; //$NON-NLS-1$
	}

}
