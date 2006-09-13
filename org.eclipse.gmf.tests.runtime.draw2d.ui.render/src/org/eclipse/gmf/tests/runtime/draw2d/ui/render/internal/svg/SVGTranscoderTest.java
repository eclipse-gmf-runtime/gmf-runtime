/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.draw2d.ui.render.internal.svg;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.export.GraphicsSVG;



/**
 * @author sshaw
 *
 * Test case for the SVG transcoder
 */
public class SVGTranscoderTest extends TestCase {

	protected void setUp() {
		try { 
			// do nothing for now
		} catch (Exception e) {
			fail( "The SVGTranscoderTest.setUp method caught an exception - " + e ); //$NON-NLS-1$
		}
	}
	
	/**
	 * SVG export should be resiliant to the size of the image since it is
	 * in vector format.
	 */
	public void testSVGGraphicsOverflow() {
		Rectangle viewBox = new Rectangle(0,0,100000,100000);
		GraphicsSVG svgG = GraphicsSVG.getInstance(viewBox);
		assertNotNull(svgG);
		
		svgG.drawRectangle(10, 10, 500, 500);
		svgG.dispose();
	}
}
