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


package org.eclipse.gmf.tests.runtime.draw2d.ui;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;



/**
 * This TestCase is designed to test the functionality of the MapMode
 * translations.  (i.e. HiMetrics)
 *
 * @author jschofie
 */
public class MapModeTest  extends TestCase {

	public void testTranslationOne() {

		for( int index = 0; index < 10000; index++ ) {
			int val1 = index;
			
			int val2 = MapMode.DPtoLP(index);
			Point ptLP = (Point)MapMode.translateToLP(new Point(index, 0));
			assertEquals(val2, ptLP.x);
			
			int val3 = MapMode.LPtoDP(val2);
			Point ptDP = (Point)MapMode.translateToDP(new Point(val2, 0));
			assertEquals(val3, ptDP.x);
			
			assertTrue(Math.abs(val1 - val3) <= 1);
		}
	}

}
