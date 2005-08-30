/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.draw2d.ui.render.internal.svg;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.export.GraphicsSVG;



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
