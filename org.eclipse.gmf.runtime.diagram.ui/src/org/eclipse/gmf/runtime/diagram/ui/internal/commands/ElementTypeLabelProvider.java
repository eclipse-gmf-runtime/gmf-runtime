/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              	   |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * A label provider that provides the name and image for 
 * <code>IElementType</code> objects. 
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class ElementTypeLabelProvider
	extends LabelProvider {

	/**
	 * Retrieves the image for <code>IElementType</code> objects using
	 * the <code>IconService</code>.
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object object) {
		if (object instanceof IElementType) {
			return IconService.getInstance()
				.getIcon((IElementType) object);
		}
		return null;
	}

	/**
	 * Uses <code>IElementType.getDisplayName()</code> for the text.
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object object) {
		if (object instanceof IElementType) {
			return ((IElementType) object).getDisplayName();
		} else {
			return object.toString();
		}
	}
}
