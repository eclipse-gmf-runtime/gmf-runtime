/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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