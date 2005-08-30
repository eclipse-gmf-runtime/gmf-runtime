/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
