/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
