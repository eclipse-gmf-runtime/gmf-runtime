/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.l10n;

import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Bundle of all images used by this plugin. Image descriptors can be retrieved
 * by referencing the public image descriptor variable directly. The public
 * strings represent images that will be cached and can be retrieved using
 * {@link #get(String)} which should <b>not</b> be disposed by the client.
 * 
 * @author cmahoney
 */
public class Draw2dUIPluginImages {

	/**
	 * The icons root directory.
	 */
	private static final String PREFIX_ROOT = "icons/"; //$NON-NLS-1$

	// Cached images that can be retrieved using the get method. The
	// corresponding image descriptor must be initialized using the
	// createAndCache() method.
	
	public static final String IMG_UP_PRESSED_ARROW = PREFIX_ROOT
		+ "uppressedarrow.gif"; //$NON-NLS-1$

	public static final String IMG_DOWN_PRESSED_ARROW = PREFIX_ROOT
		+ "downpressedarrow.gif"; //$NON-NLS-1$

	public static final String IMG_LEFT_PRESSED_ARROW = PREFIX_ROOT
		+ "leftpressedarrow.gif";//$NON-NLS-1$

	public static final String IMG_RIGHT_PRESSED_ARROW = PREFIX_ROOT
		+ "rightpressedarrow.gif";//$NON-NLS-1$

	// Image descriptors.
	
	public static final ImageDescriptor DESC_LEFT_BOTTOM = create(PREFIX_ROOT
		+ "leftbottom.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_RIGHT_BOTTOM = create(PREFIX_ROOT
		+ "rightbottom.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_RIGHT = create(PREFIX_ROOT
		+ "right.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_TOP_RIGHT = create(PREFIX_ROOT
		+ "topright.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_BOTTOM = create(PREFIX_ROOT
		+ "bottom.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_DOWN_ARROW = create(PREFIX_ROOT
		+ "downarrow.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_UP_ARROW = create(PREFIX_ROOT
		+ "uparrow.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_LEFT_ARROW = create(PREFIX_ROOT
		+ "leftarrow.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_RIGHT_ARROW = create(PREFIX_ROOT
		+ "rightarrow.gif"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_UP_PRESSED_ARROW = createAndCache(IMG_UP_PRESSED_ARROW);

	public static final ImageDescriptor DESC_DOWN_PRESSED_ARROW = createAndCache(IMG_DOWN_PRESSED_ARROW);

	public static final ImageDescriptor DESC_LEFT_PRESSED_ARROW = createAndCache(IMG_LEFT_PRESSED_ARROW);

	public static final ImageDescriptor DESC_RIGHT_PRESSED_ARROW = createAndCache(IMG_RIGHT_PRESSED_ARROW);

	public static final ImageDescriptor DESC_UP_GRAY_ARROW = create(PREFIX_ROOT
		+ "upgrayarrow.gif");//$NON-NLS-1$

	public static final ImageDescriptor DESC_DOWN_GRAY_ARROW = create(PREFIX_ROOT
		+ "downgrayarrow.gif");//$NON-NLS-1$

	public static final ImageDescriptor DESC_LEFT_GRAY_ARROW = create(PREFIX_ROOT
		+ "leftgrayarrow.gif");//$NON-NLS-1$

	public static final ImageDescriptor DESC_RIGHT_GRAY_ARROW = create(PREFIX_ROOT
		+ "rightgrayarrow.gif");//$NON-NLS-1$

	/**
	 * Creates the image descriptor from the filename given.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor create(String imageName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Draw2dPlugin
			.getPluginId(), imageName);
	}

	/**
	 * Creates the image descriptor from the filename given and caches it in the
	 * plugin's image registry.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor createAndCache(String imageName) {
		ImageDescriptor result = create(imageName);
		Draw2dPlugin.getInstance().getImageRegistry().put(imageName, result);
		return result;
	}

	/**
	 * Gets an image from the image registry. This image should not be disposed
	 * of, that is handled in the image registry. The image descriptor must have
	 * previously been cached in the image registry. The cached images for the
	 * public image names defined in this file can be retrieved using this
	 * method.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the image or null if it has not been cached in the registry
	 */
	public static Image get(String imageName) {
		return Draw2dPlugin.getInstance().getImageRegistry().get(imageName);
	}

}
