/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.factory;

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory.RenderedImageKey;


/**
 * This interface allows the RenderedImageFactory to auto-defect an image type through
 * an extension point.  Implementors need to know how to auto-defect the type of image
 * given a byte buffer representing the contents of the image.
 * 
 * @author sshaw
 */
public interface RenderedImageType {

	/**
	 * @param buffer the <code>byte[]</code> array that is the contents of the image file.
	 * This is used as the input in order to detect the type of file format.
	 * @param key the <code>RenderedImageKey</code> that defines the parameters of how
	 * to render the particular image.
	 * @return a <code>RenderedImage</code> if the <code>buffer</code> was auto-detected as
	 * being this type.  <code>null</code> is returned if the buffer stream is not detected
	 * as this type.
	 */
	public RenderedImage autoDetect(byte[] buffer,
			final RenderedImageKey key);
}
