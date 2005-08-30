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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;

/*
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 */
public class PolylineAnchor extends SingleDimensionalAnchor {

	protected int percFromEnd;

	public PolylineAnchor(PolylineConnectionEx polyline, int percFromEnd) {
		super(polyline);
		this.percFromEnd = percFromEnd;
	}

	public PolylineAnchor(PolylineConnectionEx polyline, Object terminal) {
		super(polyline);
		Assert.isTrue(terminal instanceof String);
		this.percFromEnd = terminalToDistance((String)terminal);
	}

	public Point getLocation(Point reference) {

		// reference is not used here

        PolylineConnectionEx polyline = (PolylineConnectionEx) getOwner();

		Point p =
			PointListUtilities.calculatePointRelativeToLine(
				polyline.getSmoothPoints(),
				0,
				percFromEnd,
				true);

		getOwner().translateToAbsolute(p);

		return p;
	}

}
