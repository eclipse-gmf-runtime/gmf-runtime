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

package org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n;

import org.eclipse.gmf.runtime.diagram.ui.properties.internal.DiagramPropertiesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Bundle of all images used by this plugin. Image descriptors can be retrieved
 * by referencing the public image descriptor variable directly. The public
 * strings represent images that will be cached and can be retrieved using
 * {@link #get(String)}.
 *
 * @author cmahoney
 */
public class DiagramUIPropertiesImages {

	/**
	 * The icons root directory.
	 */
	private static final String PREFIX_ROOT = "icons/"; //$NON-NLS-1$
	
	/**
	 * Images that will be cached and can be retrieved using
	 * {@link #get(String)}.
	 */
	public static final String IMG_FILL_COLOR = PREFIX_ROOT + "fill_color.gif"; //$NON-NLS-1$

	public static final String IMG_BOLD = PREFIX_ROOT + "bold.gif"; //$NON-NLS-1$

	public static final String IMG_ITALIC = PREFIX_ROOT + "italic.gif"; //$NON-NLS-1$

	public static final String IMG_FONT_COLOR = PREFIX_ROOT + "font_color.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_COLOR = PREFIX_ROOT + "line_color.gif"; //$NON-NLS-1$

	/**
	 * Image descriptors.
	 */
	public static final ImageDescriptor DESC_FILL_COLOR = createAndCache(IMG_FILL_COLOR);

	public static final ImageDescriptor DESC_BOLD = createAndCache(IMG_BOLD);

	public static final ImageDescriptor DESC_ITALIC = createAndCache(IMG_ITALIC);

	public static final ImageDescriptor DESC_FONT_COLOR = createAndCache(IMG_FONT_COLOR);

	public static final ImageDescriptor DESC_LINE_COLOR = createAndCache(IMG_LINE_COLOR);

	/**
	 * Creates the image descriptor from the filename given.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor create(String imageName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(DiagramPropertiesPlugin
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
		DiagramPropertiesPlugin.getDefault().getImageRegistry().put(imageName, result);
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
		return DiagramPropertiesPlugin.getDefault().getImageRegistry().get(imageName);
	}
}
