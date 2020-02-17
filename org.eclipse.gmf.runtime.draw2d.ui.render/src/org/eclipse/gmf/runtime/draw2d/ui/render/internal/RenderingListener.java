/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.internal;

import org.eclipse.draw2d.Graphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;

/**
 * A listener interface for receiving notification that an RenderedImage has completed 
 * rendering.
 */
public interface RenderingListener {

	/**
	 * While the rendering is occuring on a separate thread, this method is a hook to draw a temporary
	 * image onto the drawing surface.
	 * 
	 * @param g the <code>Graphics</code> object to paint the temporary image to
	 */
	public void paintFigureWhileRendering(Graphics g);
	
	/**
	 * Called when the given <code>RenderedImage</code> has completed rendering
	 * to the swt image.
	 * 
	 * @param source The <code>RenderedImage</code> that was being rendered.
	 */
	public void imageRendered(RenderedImage rndImg);
}
