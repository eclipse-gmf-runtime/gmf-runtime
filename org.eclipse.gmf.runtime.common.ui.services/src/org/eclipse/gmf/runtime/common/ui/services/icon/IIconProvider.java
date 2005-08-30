/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.icon;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The interface for providers of the "iconProvider" extension point.  Implement this interface
 * to contribute an extension to the "iconProvider" extension point.  Icon provider is responsible
 * for retrieving an icon for a specified element.
 * 
 * @author Michael Yee
 */
public interface IIconProvider extends IProvider {

	/**
     * Gets the icon for the given object.
     * 
     * @param hint argument adaptable to IElement  
     * @param flags optional icon flags
     * @return Image the icon 
     */
    Image getIcon(IAdaptable hint, int flags);
}

