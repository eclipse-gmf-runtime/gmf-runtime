/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.tests.runtime.draw2d.ui;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeTypes;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

import junit.framework.TestCase;



/**
 * This TestCase is designed to test the functionality of the MapMode
 * translations.  (i.e. HiMetrics)
 *
 * @author jschofie / sshaw
 */
public class MapModeUtilTest  extends TestCase {

	public void testTranslationDefault() {
		verifyMapMode(MapModeUtil.getMapMode());
	}

	public void testTranslationHiMetric() {
		verifyMapMode(MapModeTypes.HIMETRIC_MM);
	}
	
	public void testTranslationIdentity() {
		verifyMapMode(MapModeTypes.IDENTITY_MM);
	}
	
	static class MapModeFigure extends Figure implements IMapMode {

		public int DPtoLP(int deviceUnit) {
			return MapModeTypes.DEFAULT_MM.DPtoLP(deviceUnit);
		}

		public Translatable DPtoLP(Translatable t) {
			return MapModeTypes.DEFAULT_MM.DPtoLP(t);
		}

		public int LPtoDP(int logicalUnit) {
			return MapModeTypes.DEFAULT_MM.LPtoDP(logicalUnit);
		}

		public Translatable LPtoDP(Translatable t) {
			return MapModeTypes.DEFAULT_MM.LPtoDP(t);
		}
	}
	
	public void testTranslationFromFigure() {
		MapModeFigure mmFig = new MapModeFigure();
		verifyMapMode(MapModeUtil.getMapMode(mmFig));
		
		Figure fig = new Figure();
		fig.setParent(mmFig);
		verifyMapMode(MapModeUtil.getMapMode(fig));
		
		verifyMapMode(MapModeUtil.getMapMode(null));
	}
	
	private void verifyMapMode(IMapMode mm) {
		for( int index = 0; index < 1000; index++ ) {
			int val1 = index;
			
			int val2 = mm.DPtoLP(index);
			Point ptLP = (Point)mm.DPtoLP(new Point(index, 0));
			assertEquals(val2, ptLP.x);
			
			int val3 = mm.LPtoDP(val2);
			Point ptDP = (Point)mm.LPtoDP(new Point(val2, 0));
			assertEquals(val3, ptDP.x);
			
			assertTrue(Math.abs(val1 - val3) <= 1);
		}
	}

}
