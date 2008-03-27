/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
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

	public static final String IMG_TEXT_ALIGNMENT_LEFT = PREFIX_ROOT
			+ "alignleft.gif"; //$NON-NLS-1$

	public static final String IMG_TEXT_ALIGNMENT_CENTER = PREFIX_ROOT
			+ "aligncenter.gif"; //$NON-NLS-1$

	public static final String IMG_TEXT_ALIGNMENT_RIGHT = PREFIX_ROOT
			+ "alignright.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_SOLID = PREFIX_ROOT
			+ "line_solid.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_DASH = PREFIX_ROOT
			+ "line_dash.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_DOT = PREFIX_ROOT
			+ "line_dot.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_DASH_DOT = PREFIX_ROOT
	+ "line_dash_dot.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_DASH_DOT_DOT = PREFIX_ROOT
	+ "line_dash_dot_dot.gif"; //$NON-NLS-1$

	public static final String IMG_ARROW_SOLID_BOTH = PREFIX_ROOT
	+ "arrow_solid_both.gif"; //$NON-NLS-1$

	public static final String IMG_ARROW_SOLID_SOURCE = PREFIX_ROOT
	+ "arrow_solid_source.gif"; //$NON-NLS-1$

	public static final String IMG_ARROW_SOLID_TARGET = PREFIX_ROOT
	+ "arrow_solid_target.gif"; //$NON-NLS-1$

	public static final String IMG_ARROW_OPEN_BOTH = PREFIX_ROOT
	+ "arrow_open_both.gif"; //$NON-NLS-1$

	public static final String IMG_ARROW_OPEN_SOURCE = PREFIX_ROOT
	+ "arrow_open_source.gif"; //$NON-NLS-1$

	public static final String IMG_ARROW_OPEN_TARGET = PREFIX_ROOT
	+ "arrow_open_target.gif"; //$NON-NLS-1$

	public static final String IMG_ARROW_NONE = PREFIX_ROOT
	+ "arrow_none.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_WIDTH = PREFIX_ROOT
	+ "line_width.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_TYPE = PREFIX_ROOT
	+ "line_type.gif"; //$NON-NLS-1$

	public static final String IMG_ARROW_TYPE = PREFIX_ROOT
	+ "arrow_type.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_WIDTH_ONE = PREFIX_ROOT
	+ "line_width_one.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_WIDTH_TWO = PREFIX_ROOT
	+ "line_width_two.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_WIDTH_THREE = PREFIX_ROOT
	+ "line_width_three.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_WIDTH_FOUR = PREFIX_ROOT
	+ "line_width_four.gif"; //$NON-NLS-1$

	public static final String IMG_LINE_WIDTH_FIVE = PREFIX_ROOT
	+ "line_width_five.gif"; //$NON-NLS-1$

	/**
	 * Image descriptors.
	 */
	public static final ImageDescriptor DESC_FILL_COLOR = createAndCache(IMG_FILL_COLOR);

	public static final ImageDescriptor DESC_BOLD = createAndCache(IMG_BOLD);

	public static final ImageDescriptor DESC_ITALIC = createAndCache(IMG_ITALIC);

	public static final ImageDescriptor DESC_FONT_COLOR = createAndCache(IMG_FONT_COLOR);

	public static final ImageDescriptor DESC_LINE_COLOR = createAndCache(IMG_LINE_COLOR);

	public static final ImageDescriptor DESC_TEXT_ALIGNMENT_LEFT = createAndCache(IMG_TEXT_ALIGNMENT_LEFT);

	public static final ImageDescriptor DESC_TEXT_ALIGNMENT_CENTER = createAndCache(IMG_TEXT_ALIGNMENT_CENTER);

	public static final ImageDescriptor DESC_TEXT_ALIGNMENT_RIGHT = createAndCache(IMG_TEXT_ALIGNMENT_RIGHT);

	public static final ImageDescriptor DESC_LINE_SOLID = createAndCache(IMG_LINE_SOLID);

	public static final ImageDescriptor DESC_LINE_DASH = createAndCache(IMG_LINE_DASH);

	public static final ImageDescriptor DESC_LINE_DOT = createAndCache(IMG_LINE_DOT);

	public static final ImageDescriptor DESC_LINE_DASH_DOT = createAndCache(IMG_LINE_DASH_DOT);

	public static final ImageDescriptor DESC_LINE_DASH_DOT_DOT = createAndCache(IMG_LINE_DASH_DOT_DOT);

	public static final ImageDescriptor DESC_ARROW_NONE = createAndCache(IMG_ARROW_NONE);

	public static final ImageDescriptor DESC_ARROW_SOLID_BOTH = createAndCache(IMG_ARROW_SOLID_BOTH);

	public static final ImageDescriptor DESC_ARROW_SOLID_SOURCE = createAndCache(IMG_ARROW_SOLID_SOURCE);

	public static final ImageDescriptor DESC_ARROW_SOLID_TARGET = createAndCache(IMG_ARROW_SOLID_TARGET);

	public static final ImageDescriptor DESC_ARROW_OPEN_BOTH = createAndCache(IMG_ARROW_OPEN_BOTH);

	public static final ImageDescriptor DESC_ARROW_OPEN_SOURCE = createAndCache(IMG_ARROW_OPEN_SOURCE);

	public static final ImageDescriptor DESC_ARROW_OPEN_TARGET = createAndCache(IMG_ARROW_OPEN_TARGET);

	public static final ImageDescriptor DESC_LINE_WIDTH = createAndCache(IMG_LINE_WIDTH);
	
	public static final ImageDescriptor DESC_LINE_TYPE = createAndCache(IMG_LINE_TYPE);
	
	public static final ImageDescriptor DESC_ARROW_TYPE = createAndCache(IMG_ARROW_TYPE);
	
	public static final ImageDescriptor DESC_LINE_WIDTH_ONE = createAndCache(IMG_LINE_WIDTH_ONE);
	
	public static final ImageDescriptor DESC_LINE_WIDTH_TWO = createAndCache(IMG_LINE_WIDTH_TWO);
	
	public static final ImageDescriptor DESC_LINE_WIDTH_THREE = createAndCache(IMG_LINE_WIDTH_THREE);
	
	public static final ImageDescriptor DESC_LINE_WIDTH_FOUR = createAndCache(IMG_LINE_WIDTH_FOUR);
	
	public static final ImageDescriptor DESC_LINE_WIDTH_FIVE = createAndCache(IMG_LINE_WIDTH_FIVE);
	
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
