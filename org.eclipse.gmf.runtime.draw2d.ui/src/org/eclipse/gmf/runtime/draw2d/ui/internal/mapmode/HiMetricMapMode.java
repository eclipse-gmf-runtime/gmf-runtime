/****************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode;

import org.eclipse.swt.widgets.Display;


/**
 * This class implements the MapMode interface to provide support for
 * HiMetric values.  HIMetric values are represented as 1/100 of an inch.
 * 
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 */
public class HiMetricMapMode
	implements IMapMode {

	private float dpi;
	private double scale = 1;
	private static final double UNITS_PER_INCH = 2540.0;

	public HiMetricMapMode() {
		Display.getDefault().syncExec(new Runnable() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				dpi = Display.getDefault().getDPI().x;
			}
		});
		
		scale = dpi / UNITS_PER_INCH;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.figure.surface.mapmode.IMapMode#getScale()
	 */
	public double getScale() {
		return scale;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.figure.surface.mapmode.IMapMode#LPtoDP(int)
	 */
	public int LPtoDP(int logicalUnit) {
		return (int)Math.round((logicalUnit * dpi ) / UNITS_PER_INCH);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.figure.surface.mapmode.IMapMode#DPtoLP(int)
	 */
	public int DPtoLP(int deviceUnit) {
		return (int)Math.round((deviceUnit * UNITS_PER_INCH) / dpi);
	}

}
