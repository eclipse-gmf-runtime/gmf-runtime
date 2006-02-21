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

package org.eclipse.gmf.runtime.diagram.ui.image;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * An enumeration of image formats supported by the copy diagram to image
 * file utility class <code>CopyToImageUtil</code>. 
 * <p>We use this enumeration rather than int as in SWT.IMAGE_GIF to enforce 
 * strict supported format type checking.</p>
 * 
 * @author Anthony Hunter 
 * <a href="mailto:ahunter@rational.com">ahunter@rational.com</a>
 */
public class ImageFileFormat extends EnumeratedType {

	
	private static final int IMAGE_SVG = 100;
	static final long serialVersionUID = 1;
	
	/**
	 * supported format Graphics Interchange Format (GIF).
	 */
	public static final ImageFileFormat GIF = new ImageFileFormat("GIF", SWT.IMAGE_GIF); //$NON-NLS-1$

	/**
	 * supported format Joint Photographic Experts Group format (JPEG).
	 */
	public static final ImageFileFormat JPEG = new ImageFileFormat("JPEG", SWT.IMAGE_JPEG); //$NON-NLS-1$
	
/**
	 * supported format Joint Photographic Experts Group format (JPG).
	 */
	public static final ImageFileFormat JPG = new ImageFileFormat("JPG", SWT.IMAGE_JPEG); //$NON-NLS-1$

	/**
	 * supported format Windows Bitmap format (BMP).
	 */
	public static final ImageFileFormat BMP = new ImageFileFormat("BMP", SWT.IMAGE_BMP); //$NON-NLS-1$

	/**
	 * supported format Scalable Vector Graphics (SVG).
	 */
	public static final ImageFileFormat SVG = new ImageFileFormat("SVG", IMAGE_SVG); //$NON-NLS-1$

    /**
     * supported format PNG.
     */
    public static final ImageFileFormat PNG = new ImageFileFormat("PNG", SWT.IMAGE_PNG); //$NON-NLS-1$

	/**
	 * The list of values for this enumerated type.
	 */
	public static final ImageFileFormat[] VALUES = { GIF, BMP, JPEG, JPG, SVG, PNG };

	/**
	 * Constructs a new type with the specified
	 * name and ordinal.
	 * 
	 * @param name The name of the new type.
	 * @param ordinal The ordinal for the new type.
	 */
	private ImageFileFormat(String name, int ordinal) {
		super(name, ordinal);
	}

	/**
	 * Retrieves the list of constants for this enumerated type.
	 * 
	 * @return The list of constants for this enumerated type.
	 * 
	 * @see EnumeratedType#getValues()
	 */
	protected List getValues() {
		return Collections.unmodifiableList(Arrays.asList(VALUES));
	}

	/**
	 * Retrieves the default image format.
	 * @return the default image format.
	 */
	public static ImageFileFormat getDefaultImageFormat() {
		return GIF;
	}

	/**
	 * resolve the selected image format to an enumerated type.
	 * @param ordinal the selected format in the pulldown
	 * @return the image format enumerated type
	 */
	public static ImageFileFormat resolveImageFormat(int ordinal) {
		return ImageFileFormat.VALUES[ordinal];
	}

	/**
	 * Resolve the selected image format to an enumerated type.
	 * @param imageFormat the selected format.
	 * @return the image format enumerated type
	 */
	public static ImageFileFormat resolveImageFormat(String imageFormat) {
		for (int i = 0; i < ImageFileFormat.VALUES.length; i++) {
			if (ImageFileFormat
				.VALUES[i]
				.getName()
				.toLowerCase()
				.equals(imageFormat.toLowerCase())) {
				return ImageFileFormat.VALUES[i];
			}
		}

		return ImageFileFormat.getDefaultImageFormat();
	}

}
