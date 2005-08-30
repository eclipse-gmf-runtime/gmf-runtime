/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.Border;

import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.TransparentBorder;

/**
 * @author choang
 *
 * Interface to define all border that have the ability to draw a shadow
 * provides ability for client to toggle whether on not the shadow should be 
 * drawn
 */
public interface DropShadowBorder extends TransparentBorder, Border {

	
	/**
	 * Sets whether the border will draw a drop shadow with the border edge.
	 * @param drawDropShadow 
	 */
	public void setShouldDrawDropShadow(boolean drawDropShadow);
	
	/**
	 * @return true iff the border painting method will draw a shadow
	 */
	public boolean shouldDrawDropShadow();
	
}
