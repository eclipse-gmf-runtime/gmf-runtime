/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;

/*
 * @canBeSeenBy %level0
 */
public interface TransparentBorder extends Border {

	/**
	 * Method getTransparentInsets.
	 * This method allows a border to indicate which part of it's inset
	 * is transparent.  This is important so that if certain methods wish to
	 * ignore this part of the border they can. i.e. When placing resize
	 * handles around a shape.
	 * @param figure IFigure to calculate the insets of
	 * @return Insets object containing the border offsets which are 
	 * 		transparent.
	 */
	public Insets getTransparentInsets(IFigure figure);
}

