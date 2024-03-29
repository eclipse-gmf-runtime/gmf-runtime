/******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
