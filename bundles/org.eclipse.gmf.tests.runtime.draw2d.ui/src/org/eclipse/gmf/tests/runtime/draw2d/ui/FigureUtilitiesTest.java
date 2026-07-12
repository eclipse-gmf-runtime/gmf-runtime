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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author jschofie
 *
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class FigureUtilitiesTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@BeforeEach
	public void setUp() throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@AfterEach
	public void tearDown() {
	}

	@Test
	public void testNormalizeSegements() {

		PointList points = new PointList();
		points.addPoint(10, 10);

		// Nominal case - point size <= 1
		assertFalse(PointListUtilities.normalizeSegments(points, 1));

		// 2 points - should ignore any tolerance
		points.addPoint(11, 11);
		assertFalse(PointListUtilities.normalizeSegments(points, 1));

		// Length less than LENGTH THRESHOLD with another point
		points.addPoint(12, 12);
		assertTrue(PointListUtilities.normalizeSegments(points, 1));
		assertEquals(2, points.size(), "The point list didn't get flattened"); //$NON-NLS-1$

		points.addPoint(30, 30);
		assertTrue(PointListUtilities.normalizeSegments(points, 1));
		assertEquals(2, points.size(), "The point list didn't get flattened"); //$NON-NLS-1$
	}
}
