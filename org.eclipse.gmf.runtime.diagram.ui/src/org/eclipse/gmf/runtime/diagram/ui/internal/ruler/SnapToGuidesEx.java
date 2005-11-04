/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.ruler;

import java.util.Map;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;


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

		IMapMode mm = MapModeUtil.getMapMode(container.getFigure());
		double resultMag = mm.DPtoLP((int)THRESHOLD);
		double result = THRESHOLD;

		for (int i = 0; i < guides.length; i++) {
			int offset = mm.DPtoLP(guides[i]);
			double magnitude;

			magnitude = Math.abs(value - offset);
			if (magnitude < resultMag) {
				extendedData.put(vert ? KEY_VERTICAL_GUIDE
					: KEY_HORIZONTAL_GUIDE, new Integer(mm.DPtoLP(guides[i])));
				extendedData.put(vert ? KEY_VERTICAL_ANCHOR
					: KEY_HORIZONTAL_ANCHOR, new Integer(side));
				resultMag = magnitude;
				result = offset - value;
			}
		}
		return result;
	}	
}
