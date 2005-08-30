/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.ui;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * Image descriptor for an <code>IElementType</code> that gets its image data
 * from the icon service.
 * 
 * @author ldamus
 */
public class ElementTypeImageDescriptor
	extends ImageDescriptor {

	/**
	 * The element type.
	 */
	private final IAdaptable elementType;

	/**
	 * Constructs a new image descriptor for <code>elementType</code>.
	 * 
	 * @param elementType
	 *            the element type
	 */
	public ElementTypeImageDescriptor(final IElementType elementType) {

		this.elementType = elementType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
	 */
	public ImageData getImageData() {
		Image image = IconService.getInstance().getIcon(elementType);
		if (image != null) {
			return image.getImageData();
		}
		return null;
	}

}