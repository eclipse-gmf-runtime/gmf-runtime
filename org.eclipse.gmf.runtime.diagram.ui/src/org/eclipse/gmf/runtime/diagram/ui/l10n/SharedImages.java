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

package org.eclipse.gmf.runtime.diagram.ui.l10n;

import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Bundle of all shared images available for other plugins. Image descriptors
 * can be retrieved by referencing the public image descriptor variable
 * directly. The public strings represent images that will be cached and can be
 * retrieved using {@link #get(String)} which should <b>not</b> be disposed by
 * the client.
 * 
 * @author cmahoney
 */
public final class SharedImages {

	/**
	 * The icons root directory.
	 */
	private static final String PREFIX_ROOT = "icons/"; //$NON-NLS-1$

	/**
	 * The name of the error icon whose cached image is available by using
	 * {@link #get(String)}
	 */
	public static final String IMG_ERROR = PREFIX_ROOT + "error.gif"; //$NON-NLS-1$

	/**
	 * The name of the note icon whose cached image is available by using
	 * {@link #get(String)}
	 */
	public static final String IMG_NOTE = PREFIX_ROOT + "note.gif";//$NON-NLS-1$

	/**
	 * The name of the text icon whose cached image is available by using
	 * {@link #get(String)}
	 */
	public static final String IMG_TEXT = PREFIX_ROOT + "text.gif";//$NON-NLS-1$

	/**
	 * Image descriptor for the error icon.
	 */
	public static final ImageDescriptor DESC_ERROR = createAndCache(IMG_ERROR);

	/**
	 * Image descriptor for the note icon.
	 */
	public static final ImageDescriptor DESC_NOTE = createAndCache(IMG_NOTE);

	/**
	 * Image descriptor for the text icon.
	 */
	public static final ImageDescriptor DESC_TEXT = createAndCache(IMG_TEXT);

	/**
	 * Image descriptor for the note attachment icon.
	 */
	public static final ImageDescriptor DESC_NOTE_ATTACHMENT = create(PREFIX_ROOT
		+ "noteattachment.gif");//$NON-NLS-1$

	/**
	 * Creates the image descriptor from the filename given.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor create(String imageName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(DiagramUIPlugin
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
		DiagramUIPlugin.getInstance().getImageRegistry().put(imageName, result);
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
		return DiagramUIPlugin.getInstance().getImageRegistry().get(imageName);
	}

}
