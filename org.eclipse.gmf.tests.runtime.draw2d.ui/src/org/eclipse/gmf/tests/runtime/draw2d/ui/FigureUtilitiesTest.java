/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
/*
 * Created on Mar 12, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.gmf.tests.runtime.draw2d.ui;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.PointList;

import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;

/**
 * @author jschofie
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FigureUtilitiesTest extends TestCase {
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testNormalizeSegements() {
		
		PointList points = new PointList();
		points.addPoint( 10, 10 );

		// Nominal case - point size <= 1
		assertFalse( PointListUtilities.normalizeSegments( points, 1 ) );
		
		// 2 points - should ignore any tolerance
		points.addPoint( 11, 11 );
		assertFalse( PointListUtilities.normalizeSegments( points, 1 ) );
		
		// Length less than LENGTH THRESHOLD with another point
		points.addPoint( 12, 12 );
		assertTrue( PointListUtilities.normalizeSegments( points, 1 ) );
		assertEquals( "The point list didn't get flattened", 2, points.size() ); //$NON-NLS-1$
		
		points.addPoint( 30, 30 );
		assertTrue( PointListUtilities.normalizeSegments( points, 1 ) );
		assertEquals( "The point list didn't get flattened", 2, points.size() ); //$NON-NLS-1$
	}
}
