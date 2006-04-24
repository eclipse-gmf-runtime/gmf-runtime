/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * Set of color constants that are commonly used.
 * <p>
 * This interface defines constants only, it is <EM>not</EM> intended to be
 * implemented by clients.
 * </p>
 * 
 * @author sshaw
 */
public interface DiagramColorConstants
	extends ColorConstants{
    
	/**
	 * Constant <code>Color</code> representing green.
	 */
	public final static Color 
		diagramGreen = new Color(null, 40,100,70);
    
	/**
	 * Constant <code>Color</code> representing light red.
	 */
	public final static Color
		diagramLightRed = new Color(null, 255, 203, 203);
    
	/**
	 * Constant <code>Color</code> representing red.
	 */
	public final static Color
		diagramRed = new Color(null, 255, 128, 128);
    
	/**
	 * Constant <code>Color</code> representing light blue.
	 */
    public final static Color
		diagramLightBlue = new Color(null, 202, 203, 255);

	/**
	 * Constant <code>Color</code> representing blue.
	 */
    public final static Color
		diagramBlue = new Color(null, 128, 128, 255);
	
    /**
	 * Constant <code>Color</code> representing light gray.
	 */
	public final static Color
		diagramLightGray = new Color(null, 250, 250, 254);
	
	/**
	 * Constant <code>Color</code> representing gray.
	 */
	public final static Color
		diagramGray = new Color(null, 176, 176, 176);
	
	/**
	 * Constant <code>Color</code> representing dark gray.
	 */
	public final static Color
		diagramDarkGray = new Color(null, 131, 122, 133);
	
	/**
	 * Constant <code>Color</code> representing light yellow.
	 */
	public final static Color
		diagramLightYellow = new Color(null, 255, 255, 203);
	
	/**
	 * Constant <code>Color</code> representing dark yellow.
	 */ 
	public final static Color
		diagramDarkYellow = new Color(null, 255, 204, 102);	
	
	/**
	 * Constant <code>Color</code> representing a light gold yellow.
	 */
	public final static Color
		diagramLightGoldYellow = new Color(null, 255, 255, 204);

	/**
	 * Constant <code>Color</code> representing burgundy red.
	 */
	public final static Color
		diagramBurgundyRed = new Color(null, 153, 0, 51);
}
