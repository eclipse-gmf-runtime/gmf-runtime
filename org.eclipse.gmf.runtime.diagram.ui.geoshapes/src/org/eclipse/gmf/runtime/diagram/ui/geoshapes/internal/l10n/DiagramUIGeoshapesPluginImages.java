/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n;

import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.GeoshapesPlugin;
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
public class DiagramUIGeoshapesPluginImages {

	/**
	 * The icons root directory.
	 */
	private static final String PREFIX_ROOT = "icons/"; //$NON-NLS-1$

	// Cached images that can be retrieved using the get method. The
	// corresponding image descriptor must be initialized using the
	// createAndCache() method.
	
	// Geometric Shape Icons
	public static final String IMG_OVAL = PREFIX_ROOT + "IconEllipse.gif"; //$NON-NLS-1$

	public static final String IMG_TRIANGLE = PREFIX_ROOT + "IconTriangle.gif"; //$NON-NLS-1$

	public static final String IMG_RECTANGLE = PREFIX_ROOT
		+ "IconRectangle.gif"; //$NON-NLS-1$

	public static final String IMG_SHADOWRECTANGLE = PREFIX_ROOT
		+ "IconShadowRectangle.gif"; //$NON-NLS-1$

	public static final String IMG_3DRECTANGLE = PREFIX_ROOT
		+ "Icon3DRectangle.gif"; //$NON-NLS-1$

	public static final String IMG_ROUNDRECTANGLE = PREFIX_ROOT
		+ "IconRoundRectangle.gif"; //$NON-NLS-1$

	public static final String IMG_HEXAGON = PREFIX_ROOT + "IconHexagon.gif"; //$NON-NLS-1$

	public static final String IMG_OCTAGON = PREFIX_ROOT + "IconOctagon.gif"; //$NON-NLS-1$

	public static final String IMG_PENTAGON = PREFIX_ROOT + "IconPentagon.gif"; //$NON-NLS-1$

	public static final String IMG_DIAMOND = PREFIX_ROOT + "IconDiamond.gif"; //$NON-NLS-1$

	public static final String IMG_CYLINDER = PREFIX_ROOT + "IconCylinder.gif"; //$NON-NLS-1$

	// Connection Icons
	public static final String IMG_LINE = PREFIX_ROOT + "IconLine.gif"; //$NON-NLS-1$

	// Image descriptors.

	// Geometric Shape Icons
	public static final ImageDescriptor DESC_OVAL = createAndCache(IMG_OVAL);

	public static final ImageDescriptor DESC_TRIANGLE = createAndCache(IMG_TRIANGLE);

	public static final ImageDescriptor DESC_RECTANGLE = createAndCache(IMG_RECTANGLE);

	public static final ImageDescriptor DESC_SHADOWRECTANGLE = createAndCache(IMG_SHADOWRECTANGLE);

	public static final ImageDescriptor DESC_3DRECTANGLE = createAndCache(IMG_3DRECTANGLE);

	public static final ImageDescriptor DESC_ROUNDRECTANGLE = createAndCache(IMG_ROUNDRECTANGLE);

	public static final ImageDescriptor DESC_HEXAGON = createAndCache(IMG_HEXAGON);

	public static final ImageDescriptor DESC_OCTAGON = createAndCache(IMG_OCTAGON);

	public static final ImageDescriptor DESC_PENTAGON = createAndCache(IMG_PENTAGON);

	public static final ImageDescriptor DESC_DIAMOND = createAndCache(IMG_DIAMOND);

	public static final ImageDescriptor DESC_CYLINDER = createAndCache(IMG_CYLINDER);

	// Connection Icons
	public static final ImageDescriptor DESC_LINE = createAndCache(IMG_LINE);

	/**
	 * Creates the image descriptor from the filename given.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor create(String imageName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(GeoshapesPlugin
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
		GeoshapesPlugin.getDefault().getImageRegistry().put(imageName, result);
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
		return GeoshapesPlugin.getDefault().getImageRegistry().get(imageName);
	}
}
