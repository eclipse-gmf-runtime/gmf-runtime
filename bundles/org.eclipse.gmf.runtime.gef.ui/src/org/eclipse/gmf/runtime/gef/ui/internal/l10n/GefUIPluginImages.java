/******************************************************************************
 * Copyright (c) 2005, 2024 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.l10n;

import org.eclipse.gmf.runtime.gef.ui.internal.GefPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * Bundle of all images used by this plugin. Image descriptors can be retrieved
 * by referencing the public image descriptor variable directly. The public
 * strings represent images that will be cached and can be retrieved using
 * {@link #get(String)} which should <b>not</b> be disposed by the client.
 *
 * @author cmahoney
 */
public class GefUIPluginImages {

	/**
	 * The icons root directory.
	 */
	private static final String PREFIX_ROOT = "icons/"; //$NON-NLS-1$

	// Image descriptors.

	public static final ImageDescriptor DESC_SEG_ADD = create(PREFIX_ROOT + "Seg_Add.png"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_SEG_MOVE = create(PREFIX_ROOT + "Seg_Move.png"); //$NON-NLS-1$

	/**
	 * Creates the image descriptor from the filename given.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor create(String imageName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(GefPlugin.getPluginId(), imageName);
	}

}
