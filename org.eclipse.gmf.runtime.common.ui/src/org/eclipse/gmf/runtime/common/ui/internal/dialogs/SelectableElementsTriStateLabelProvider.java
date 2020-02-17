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

import java.util.Hashtable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.dialogs.SelectableElement;
import org.eclipse.gmf.runtime.common.ui.dialogs.SelectedType;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIIconNames;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;
import org.eclipse.gmf.runtime.common.ui.util.OverlayImageDescriptor;

/**
 * This label provider overlays a selected or unselected icon on another icon.
 * It supports three states as defined in SelectedType, which are SELECTED,
 * UNSELECTED. and LEAVE. It decorates SELECTED with a green plus sign. It
 * decorates UNSELECTED with a red x. It doesn't do anything with LEAVE.
 * 
 * A mix of SELECTED, UNSELECTED, and LEAVE gives a greyed out parent. You could
 * call this this a fourth state, but it is not a real state.
 * 
 * @author wdiu, Wayne Diu
 */

public class SelectableElementsTriStateLabelProvider
	extends SelectableElementsLabelProvider {

	/**
	 * Image pool, this is not reusing the superclass' image pool because they
	 * are supposed to be different.
	 */
	private Hashtable imagePool = new Hashtable();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		Image image = super.getImage(element);

		// overlay if image is not null
		if (image != null) {

			SelectableElement selectableElement = (SelectableElement) element;
			// super asserted on this

			if ((selectableElement.getNumberOfChildren() > 0 && areAllChildrenOfType(
				selectableElement, SelectedType.SELECTED))
				|| (selectableElement.getNumberOfChildren() == 0 && selectableElement
					.getSelectedType() == SelectedType.SELECTED)) {

				String key = "s" + image.hashCode(); //$NON-NLS-1$ 
				Image overlayImage = (Image) imagePool.get(key);

				if (overlayImage == null) {
					try {
						overlayImage = overlayImage(image, AbstractUIPlugin
							.imageDescriptorFromPlugin(CommonUIPlugin
								.getPluginId(),
								CommonUIIconNames.IMG_CHECKBOX_SELECTED));
						imagePool.put(key, overlayImage);
					} catch (Exception e) {
						Trace.catching(CommonUIPlugin.getDefault(),
							CommonUIDebugOptions.EXCEPTIONS_CATCHING,
							getClass(), "getImage", e); //$NON-NLS-1$
						// don't just return the image,
						// that will be more confusing in case of failure
						Log.error(CommonUIPlugin.getDefault(),
							CommonUIStatusCodes.RESOURCE_FAILURE,
							"Failed to load SELECTED_ICON overlay", e); //$NON-NLS-1$
						return null;
					}
				}
				return overlayImage;

			}

			else if ((selectableElement.getNumberOfChildren() > 0 && areAllChildrenOfType(
				selectableElement, SelectedType.UNSELECTED))
				|| (selectableElement.getNumberOfChildren() == 0 && selectableElement
					.getSelectedType() == SelectedType.UNSELECTED)) {
				String key = "u" + image.hashCode(); //$NON-NLS-1$ 
				Image overlayImage = (Image) imagePool.get(key);

				if (overlayImage == null) {
					try {
						overlayImage = overlayImage(image, AbstractUIPlugin
							.imageDescriptorFromPlugin(CommonUIPlugin
								.getPluginId(),
								CommonUIIconNames.IMG_CHECKBOX_UNSELECTED));
						imagePool.put(key, overlayImage);
					} catch (Exception e) {
						// don't just return the image,
						// that will be more confusing in case of failure
						Trace.catching(CommonUIPlugin.getDefault(),
							CommonUIDebugOptions.EXCEPTIONS_CATCHING,
							getClass(), "getImage", e); //$NON-NLS-1$
						Log.error(CommonUIPlugin.getDefault(),
							CommonUIStatusCodes.RESOURCE_FAILURE,
							"Failed to load UNSELECTED_ICON overlay", e); //$NON-NLS-1$
						return null;
					}
				}
				return overlayImage;

			}

			else if ((selectableElement.getNumberOfChildren() > 0 && areAllChildrenOfType(
				selectableElement, SelectedType.LEAVE))
				|| (selectableElement.getNumberOfChildren() == 0 && selectableElement
					.getSelectedType() == SelectedType.LEAVE)) {

				String key = "c" + image.hashCode(); //$NON-NLS-1$ 
				Image overlayImage = (Image) imagePool.get(key);

				if (overlayImage == null) {
					try {
						overlayImage = overlayImage(image, AbstractUIPlugin
							.imageDescriptorFromPlugin(CommonUIPlugin
								.getPluginId(),
								CommonUIIconNames.IMG_CHECKBOX_CLEARED));
						imagePool.put(key, overlayImage);
					} catch (Exception e) {
						// don't just return the image,
						// that will be more confusing in case of failure
						Trace.catching(CommonUIPlugin.getDefault(),
							CommonUIDebugOptions.EXCEPTIONS_CATCHING,
							getClass(), "getImage", e); //$NON-NLS-1$
						Log.error(CommonUIPlugin.getDefault(),
							CommonUIStatusCodes.RESOURCE_FAILURE,
							"Failed to load CHECKBOX_ICON overlay", e); //$NON-NLS-1$
						return null;
					}
				}
				return overlayImage;
			}

			else {// mix, since has children

				assert (selectableElement.getNumberOfChildren() > 0);

				String key = "g" + image.hashCode(); //$NON-NLS-1$ 
				Image overlayImage = (Image) imagePool.get(key);

				if (overlayImage == null) {
					try {
						overlayImage = overlayImage(image, AbstractUIPlugin
							.imageDescriptorFromPlugin(CommonUIPlugin
								.getPluginId(),
								CommonUIIconNames.IMG_CHECKBOX_GREYED));
						imagePool.put(key, overlayImage);
					} catch (Exception e) {
						// don't just return the image,
						// that will be more confusing in case of failure
						Trace.catching(CommonUIPlugin.getDefault(),
							CommonUIDebugOptions.EXCEPTIONS_CATCHING,
							getClass(), "getImage", e); //$NON-NLS-1$
						Log.error(CommonUIPlugin.getDefault(),
							CommonUIStatusCodes.RESOURCE_FAILURE,
							"Failed to load GREYED_ICON overlay", e); //$NON-NLS-1$
						return null;
					}
				}
				return overlayImage;

			}
		}

		return image;
	}

	/**
	 * Overlays given base image with given overlay image ImageDescriptor
	 * 
	 * Based on overlayImage from ModelExplorerDecorator
	 * 
	 * @param srcImage
	 *            base image
	 * @param imageDesc
	 *            overlay ImageDescriptor
	 * @return Image the new overlay image
	 */
	private Image overlayImage(Image srcImage, ImageDescriptor imageDesc) {
		OverlayImageDescriptor overlayDesc = new OverlayImageDescriptor(
			srcImage, imageDesc, srcImage.getImageData().width, srcImage
				.getImageData().height);

		Image destImage = overlayDesc.createImage();
		assert null != destImage;

		return destImage;
	}

	/**
	 * Returns if the SelectableElement's children all have the given type. This
	 * function is recursive.
	 * 
	 * @param selectableElement
	 *            the element to check if all children have the selectedType
	 * @param selectedType
	 *            the SelectedType that all children must have to return true
	 * @return boolean true if all children are selectedType, false if they are
	 *         not
	 */
	private boolean areAllChildrenOfType(SelectableElement selectableElement,
			SelectedType selectedType) {
		int numberOfChildren = selectableElement.getNumberOfChildren();
		assert (numberOfChildren > 0);
		for (int i = 0; i < numberOfChildren; i++) {
			SelectableElement element = selectableElement.getChild(i);
			if ((element.getNumberOfChildren() == 0 && element
				.getSelectedType() != selectedType)
				|| (element.getNumberOfChildren() > 0 && !areAllChildrenOfType(
					element, selectedType)))
				return false;
		}
		return true;
	}

	/**
	 * Also frees up the images that were created
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#dispose()
	 */
	public void dispose() {
		dispose(imagePool);
		super.dispose();
	}

	/**
	 * Constructor that calls its superclass
	 */
	public SelectableElementsTriStateLabelProvider() {
		// randomly select an image, assume they're all the same size
		// if your icon size is > short, then that would be very strange
		// change it to int if you think that it will be a problem
		super(
			(AbstractUIPlugin.imageDescriptorFromPlugin(CommonUIPlugin
				.getPluginId(), CommonUIIconNames.IMG_CHECKBOX_SELECTED) == null) ? (short) 0
				: (short) (AbstractUIPlugin.imageDescriptorFromPlugin(
					CommonUIPlugin.getPluginId(),
					CommonUIIconNames.IMG_CHECKBOX_SELECTED).getImageData().width),
			(short) 0);
	}
}