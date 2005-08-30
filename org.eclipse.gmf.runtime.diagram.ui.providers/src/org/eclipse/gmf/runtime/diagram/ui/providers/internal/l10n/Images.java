/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.providers.internal.l10n;

import org.eclipse.swt.graphics.Image;

/*
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 */
public class Images {

	/**
	 * Enabled subdirectory off of root icon directory
	 */
	//private static final String ENABLED_PREFIX = "elcl16/"; //$NON-NLS-1$

	/**
	 * Disabled subdirectory off of root icon directory
	 */
	//private static final String DISABLED_PREFIX = "dlcl16/"; //$NON-NLS-1$

	// shared images start with ICON_
	public static final Image ICON_BLANK_LISTITEM;

	public static final Image ICON_ERROR;

	static {
		// shared images
		ICON_BLANK_LISTITEM = create("blank.gif"); //$NON-NLS-1$ 

		ICON_ERROR = create("error.gif"); //$NON-NLS-1$

	}

	static private Image create(String filename) {
		return DiagramProvidersResourceManager.getInstance().createImage(
			filename);
	}

//	static private ImageDescriptor createDescriptor(String filename) {
//		return DiagramProvidersResourceManager.getInstance()
//			.createImageDescriptor(filename);
//	}
}