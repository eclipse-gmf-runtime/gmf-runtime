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

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author choang
 *
 * Defines interface for any figure whose shape is oval or circular.  This is commonly used to provide
 * an anchor with type information that it can restrict itself to figures that support the oval shape.
 */
public interface IOvalAnchorableFigure extends IFigure {
	
	/**
	 * Gets the rectangular boundary for the oval shape that implements this interface.
	 * @return the <code>Rectangle</code> that is the boundary rectangle for the oval shape.
	 */
	public Rectangle getOvalBounds();

}
