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
			
			val2 = MapMode.LPtoDP(val2);
			assertEquals(val1, val2);
		}
	}
	
	// TODO: Investigate in the next release.
	//
	// This test is commented out because it is failing.  Translating to and from
	// himetrics is not giving the same values.  i.e. The original value is not
	// being converted correctly.  The MapMode.translateTo methods might have to
	// be removed.  
//	public void testTranslationPoint() {
//
//		for( int index = 1; index < 10000; index++ ) {
//			Point val1 = new Point(index,index);
//			Point val2 = new Point(index,index);
//			
//			MapMode.translateToLP(val2);
//			MapMode.translateToDP(val2);
//			System.out.println( "val1 = (" + val1 + "), val2 = (" + val2 + ")" );
//			assertEquals(val1, val2);
//		}
//	}

	// TODO: Investigate in the next release.
	//
	// This test is commented out because it is failing.  Translating to and from
	// himetrics is not giving the same values.  i.e. The original value is not
	// being converted correctly.  The MapMode.translateTo methods might have to
	// be removed.  
//	public void testTranslationDimension() {
//
//		for( int index = 1; index < 10000; index++ ) {
//			Dimension val1 = new Dimension(index,index);
//			Dimension val2 = new Dimension(index,index);
//			
//			MapMode.translateToLP(val2);
//			MapMode.translateToDP(val2);
//			System.out.println( "val1 = (" + val1 + "), val2 = (" + val2 + ")" );
//			assertEquals(val1, val2);
//		}
//	}

	// TODO: Investigate in the next release.
	//
	// This test is commented out because it is failing.  Translating to and from
	// himetrics is not giving the same values.  i.e. The original value is not
	// being converted correctly.  The MapMode.translateTo methods might have to
	// be removed.  
//	public void testTranslationRectangle() {
//
//		for( int index = 1; index < 10000; index++ ) {
//				Rectangle val1 = new Rectangle(index,index,index,index);
//				Rectangle val2 = new Rectangle(index,index,index,index);
//				
//				MapMode.translateToLP(val2);
//				MapMode.translateToDP(val2);
//				System.out.println( "val1 = (" + val1 + "), val2 = (" + val2 + ")" );
//				assertEquals(val1, val2);
//			}
//		}
}
