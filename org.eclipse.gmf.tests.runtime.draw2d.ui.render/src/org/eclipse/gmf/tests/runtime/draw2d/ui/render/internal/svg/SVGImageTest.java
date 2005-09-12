/******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.draw2d.ui.render.internal.svg;

import java.awt.Color;
import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;

/**
 * @author sshaw
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SVGImageTest extends TestCase {

	private final String LADY_SVG = "lady.svg"; //$NON-NLS-1$
	private final String SHAPES_SVG = "shapes.svg"; //$NON-NLS-1$

	private final int WIDTH = 200;
	private final int HEIGHT = 200;
	
	private final int NEW_WIDTH = 120;
	private final int NEW_HEIGHT = 140;
	private final Color NEW_FILL = new Color(255, 0, 0);
	private final Color NEW_OUTLINE = new Color(0, 255, 0);

	private URL PERSON;
	private URL SHAPES;

	private RenderedImage fixture1;
	private RenderedImage fixture2;


	public SVGImageTest(String name) {
		super(name);

		PERSON = getURL( LADY_SVG );
		SHAPES = getURL( SHAPES_SVG );
	}

	private RenderedImage getFixture1() {
		return fixture1;
	}
	
	private RenderedImage getFixture2() {
		return fixture2;
	}
	
	private URL getURL( String fileName ) {
		
		return getClass().getResource( fileName );
	}

	protected void setUp() {
		try { 
			
			fixture1 = RenderedImageFactory.getInstance( PERSON );
			assertNotNull( "Fixture1 shouldn't be null", fixture1 ); //$NON-NLS-1$
			
			RenderInfo info = RenderedImageFactory.createInfo(WIDTH, HEIGHT, null, null, false, false);
			fixture2 = RenderedImageFactory.getInstance( SHAPES, info );
			assertNotNull( "Fixture2 shouldn't be null", fixture2 ); //$NON-NLS-1$
			
		} catch (Exception e) {
			fail( "The SVGImageTest.setUp method caught an exception - " + e ); //$NON-NLS-1$
		}
	}
	
	public void testGetRenderInfo() {

		RenderInfo info = getFixture1().getRenderInfo();
		assertTrue("getRenderInfo fixture 1 return null", info != null); //$NON-NLS-1$
		
		info = getFixture2().getRenderInfo();
		assertTrue("getRenderInfo fixture 2 return null", info != null); //$NON-NLS-1$
		assertTrue("getRenderInfo fixture 2 width / height not correct", //$NON-NLS-1$
				info.getWidth() == WIDTH && info.getHeight() == HEIGHT);
	}
	
	public void testGetNewRenderedImage() {

		RenderInfo info = RenderedImageFactory.createInfo(NEW_WIDTH, NEW_HEIGHT, NEW_FILL, NEW_OUTLINE, true, true);
		assertTrue(info != null);
		info = RenderedImageFactory.createInfo(NEW_WIDTH, NEW_HEIGHT, null, NEW_OUTLINE, false, false);
		assertTrue(info != null);
	}
	
	public void testGetSWTImage() {

		Image img = getFixture1().getSWTImage();
		assertNotNull("getSWTImage fixture 1 Image invalid", img ); //$NON-NLS-1$
							
		img = getFixture2().getSWTImage();
		assertNotNull("getSWTImage fixture 2 Image invalid", img ); //$NON-NLS-1$
	}
}
