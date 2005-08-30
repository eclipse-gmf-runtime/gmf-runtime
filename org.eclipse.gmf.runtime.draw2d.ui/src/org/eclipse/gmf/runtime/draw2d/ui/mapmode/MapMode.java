/****************************************************************************
Licensed Materials - Property of IBM
(C) Copyright IBM Corp. 2004. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.mapmode;

import org.eclipse.draw2d.geometry.Translatable;

import org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode.HiMetricMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode.IMapMode;


/**
 * This class provides methods that convert from logical units
 * to device units.
 * 
 * @author jschofie
 */
public class MapMode {

	private static IMapMode currentImpl = new HiMetricMapMode();

	/**
	 * Sets the current mode map that will be utilized to perform the scaling from logical 
	 * to device units.
	 * 
	 * @param mapMode the {@link IMapMode} object that the mapping will be delegated to.
	 */
	public static void setMapMode( IMapMode mapMode ) {
		if( mapMode != null )
			currentImpl = mapMode;
		return;
	}
	
	/**
	 * Get the scale factor to apply to the Logical Units to get the
	 * Device Units
	 * 
	 * @return scale factor
	 */
	public static double getScale() {
		return currentImpl.getScale();
	}

	/**
	 * Convert a Logical Unit (i.e. Hi-Metrics) into a
	 * Device Unit (pixels)
	 * 
	 * @param logicalUnit the value to be converted
	 * @return the value represented in device units
	 */
	public static int LPtoDP( int logicalUnit ) {
		return currentImpl.LPtoDP(logicalUnit);
	}

	/**
	 * Convert a Device Unit (pixels) into a
	 * Logical Unit (i.e. Hi-Metrics)
	 *  
	 * @param deviceUnit the value to be converted
	 * @return the value represented in logical units
	 */
	public static int DPtoLP( int deviceUnit ) {
		return currentImpl.DPtoLP(deviceUnit);
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
