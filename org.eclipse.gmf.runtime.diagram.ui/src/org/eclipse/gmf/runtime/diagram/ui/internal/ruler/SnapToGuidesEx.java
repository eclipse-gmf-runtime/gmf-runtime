/***************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004.  All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
***************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.ruler;

import java.util.Map;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGuides;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;


/**
 * Override to support HiMetrics
 *
 * @author jschofie
 */
public class SnapToGuidesEx
	extends SnapToGuides {

	public SnapToGuidesEx(GraphicalEditPart container) {
		super(container);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.SnapToGuides#getCorrectionFor(int[], double, java.util.Map, boolean, int)
	 */
	protected double getCorrectionFor(int[] guides, double value,
			Map extendedData, boolean vert, int side) {

		double resultMag = MapMode.DPtoLP((int)THRESHOLD);
		double result = THRESHOLD;

		for (int i = 0; i < guides.length; i++) {
			int offset = MapMode.DPtoLP(guides[i]);
			double magnitude;

			magnitude = Math.abs(value - offset);
			if (magnitude < resultMag) {
				extendedData.put(vert ? KEY_VERTICAL_GUIDE
					: KEY_HORIZONTAL_GUIDE, new Integer(MapMode.DPtoLP(guides[i])));
				extendedData.put(vert ? KEY_VERTICAL_ANCHOR
					: KEY_HORIZONTAL_ANCHOR, new Integer(side));
				resultMag = magnitude;
				result = offset - value;
			}
		}
		return result;
	}	
}
