/******************************************************************************
 * Copyright (c) 2004 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.draw2d.ui.mapmode;

import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode.DiagramMapModeUtil;


/**
 * This class provides methods that convert from logical units
 * to device units.  Will be deprecated soon...
 * 
 * @author jschofie / sshaw
 */
public class MapMode {

	/**
	 * Get the scale factor to apply to the Logical Units to get the
	 * Device Units
	 * 
	 * @return scale factor
	 */
	public static double getScale() {
		return DiagramMapModeUtil.getScale(MapModeUtil.getMapMode());
	}

	/**
	 * Convert a Logical Unit (i.e. Hi-Metrics) into a
	 * Device Unit (pixels)
	 * 
	 * @param logicalUnit the value to be converted
	 * @return the value represented in device units
	 */
	public static int LPtoDP( int logicalUnit ) {
		return MapModeUtil.getMapMode().LPtoDP(logicalUnit);
	}

	/**
	 * Convert a Device Unit (pixels) into a
	 * Logical Unit (i.e. Hi-Metrics)
	 *  
	 * @param deviceUnit the value to be converted
	 * @return the value represented in logical units
	 */
	public static int DPtoLP( int deviceUnit ) {
		return MapModeUtil.getMapMode().DPtoLP(deviceUnit);
	}
	
	/**
	 * Convert a <code>Translatable</code> to Device Units (pixels)
	 * 
	 * @param t the <code>Translatable</code> to convert
	 * @return the parameter <code>t</code> that was scaled for convenience.
	 */
	public static Translatable translateToDP( Translatable t ) {
    	t.performScale( getScale() );
    	return t;
	}
	
	/**
	 * Convert a Translatable to Logical Units (i.e. Hi-Metrics)
	 * @param t the Translatable to convert
	 * @return the parameter <code>t</code> that was scaled for convenience.
	 */
	public static Translatable translateToLP( Translatable t ) {
    	t.performScale( 1 / getScale() );
    	return t;
	}
}
