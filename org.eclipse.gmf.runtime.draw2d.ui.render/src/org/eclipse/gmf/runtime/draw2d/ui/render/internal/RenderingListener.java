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
