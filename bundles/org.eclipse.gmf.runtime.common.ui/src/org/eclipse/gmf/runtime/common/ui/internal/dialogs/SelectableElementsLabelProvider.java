/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.internal.dialogs;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.dialogs.SelectableElement;
import org.eclipse.gmf.runtime.common.ui.util.ShiftedImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for SelectableElement viewer
 * 
 * @author Wayne Diu, wdiu
 */
public class SelectableElementsLabelProvider
	extends LabelProvider
	implements ILabelProviderListener {

	/**
	 * Image pool
	 */
	private Hashtable baseImagePool = new Hashtable();

	/**
	 * Width to shift image by, default 0
	 */
	private short width = 0;

	/**
	 * Height to shift image by, default 0
	 */
	private short height = 0;

	/**
	 * Constructor does nothing
	 */
	public SelectableElementsLabelProvider() {
		/* Constructor */
	}

	/**
	 * Constructor which you can specify the width and height to shift the image
	 * by
	 * 
	 * @param aWidth
	 *            to shift by
	 * @param aHeight
	 *            to shift by
	 */
	public SelectableElementsLabelProvider(short aWidth, short aHeight) {
		this.width = aWidth;
		this.height = aHeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if (element instanceof SelectableElement) {

			SelectableElement selectableElement = (SelectableElement) element;

			String key = Integer.toString(selectableElement
				.getIconImageDescriptor().hashCode());
			Image image = (Image) baseImagePool.get(key);

			if (image == null) {

				ImageDescriptor imageDescriptor = selectableElement
					.getIconImageDescriptor();
				if (imageDescriptor != null) {
					image = new ShiftedImageDescriptor(width, height,
						imageDescriptor).createImage();
				}

				baseImagePool.put(key, image);
			}

			return image;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(final Object element) {
		if (element instanceof SelectableElement) {
			return ((SelectableElement) element).getName();
		}

		return StringStatics.BLANK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProviderListener#labelProviderChanged(org.eclipse.jface.viewers.LabelProviderChangedEvent)
	 */
	public void labelProviderChanged(LabelProviderChangedEvent event) {
		fireLabelProviderChanged(event);
	}

	/**
	 * Also frees up the images that were created Copied from
	 * ModelExplorerDecorator, may move to public static
	 * 
	 * @param imagePool
	 *            a HashTable that acts as an imagePool. It will have its images
	 *            disposed.
	 *  
	 */
	public void dispose(Hashtable imagePool) {
		Collection c = imagePool.values();
		Iterator i = c.iterator();
		while (i.hasNext()) {
			Image image = (Image) i.next();
			if (image.isDisposed() == false) {
				image.dispose();
			}
		}

	}

	/**
	 * Also frees up the images that were created
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#dispose()
	 */
	public void dispose() {
		dispose(baseImagePool);
		super.dispose();
	}
}