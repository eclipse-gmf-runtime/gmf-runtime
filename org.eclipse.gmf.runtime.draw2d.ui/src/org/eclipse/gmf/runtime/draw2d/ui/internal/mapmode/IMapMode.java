/****************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode;


/**
 * Objects implementing this interface are responsible for mapping the units
 * in the model to pixles.  For an example of how to use this interface
 * @see org.eclipse.gmf.runtime.gef.ui.internal.figure.surface.mapmode.HiMetricMapMode
 *
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 */
public interface IMapMode {

	
	/**
	 * Get the scale factor used when translating from Logical Units to
	 * Device Units.
	 * @return the scale factor applied to Logical Units to get Device Units
	 */
	public double getScale();

	/**
	 * Convert a Logical Unit into a Device Unit
	 *  
	 * @param logicalUnit the value to be converted
	 * @return the value represented in device units
	 */
	public int LPtoDP(int logicalUnit);

	/**
	 * Convert a Device Unit into a Logical Unit
	 *  
	 * @param deviceUnit the value to be converted
	 * @return the value represented in logical units
	 */
	public int DPtoLP(int deviceUnit);

}
