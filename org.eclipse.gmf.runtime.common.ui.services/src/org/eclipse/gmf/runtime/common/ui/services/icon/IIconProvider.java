/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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

