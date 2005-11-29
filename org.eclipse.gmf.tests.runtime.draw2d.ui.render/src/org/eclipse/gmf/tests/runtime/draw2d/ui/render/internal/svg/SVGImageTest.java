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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;
import org.eclipse.gmf.runtime.draw2d.ui.render.image.ImageConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.ImageTranscoderEx;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.SVGImage;
import org.eclipse.swt.graphics.Image;

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
	
	private void performBatikRendering(SVGImage svg1, int width, int height) {
		InputStream in = new ByteArrayInputStream(svg1.getBuffer());
		RenderInfo info = svg1.getRenderInfo();
		ImageTranscoderEx transcoder = new ImageTranscoderEx();

		if (width > 0)
			transcoder.addTranscodingHint(
				ImageTranscoderEx.KEY_WIDTH,
				new Float(width));
		if (height > 0)
			transcoder.addTranscodingHint(
				ImageTranscoderEx.KEY_HEIGHT,
				new Float(height));
		
		transcoder.addTranscodingHint(
				ImageTranscoderEx.KEY_MAINTAIN_ASPECT_RATIO,
				Boolean.valueOf(info.shouldMaintainAspectRatio()));
	
		transcoder.addTranscodingHint(
				ImageTranscoderEx.KEY_ANTI_ALIASING,
				Boolean.valueOf(info.shouldAntiAlias()));
				
		if (info.getFillColor() != null) {
			transcoder.addTranscodingHint(
				ImageTranscoderEx.KEY_FILL_COLOR,
				new Color(info.getFillColor().getRed(), 
						  info.getFillColor().getGreen(),
						  info.getFillColor().getBlue()));
		}
		
		if (info.getOutlineColor() != null) {
					transcoder.addTranscodingHint(
						ImageTranscoderEx.KEY_OUTLINE_COLOR,
						new Color(info.getOutlineColor().getRed(), 
								  info.getOutlineColor().getGreen(),
								  info.getOutlineColor().getBlue()));
		}
		
		TranscoderInput input = new TranscoderInput(in);
		TranscoderOutput output = new TranscoderOutput();
		
		try {
			transcoder.transcode(input, output);
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedImage bufImg = transcoder.getBufferedImage();
		Image swtImage = ImageConverter.convert(bufImg);
		assertNotNull("swtImage fixture 1 Image invalid", swtImage ); //$NON-NLS-1$
	}
	
	private void performRenderedImageRendering(SVGImage svg1, int width, int height) {
		RenderInfo info = svg1.getRenderInfo();
		
		//info.setValues(width, height, info.getFillColor(), info.getOutlineColor(), true, true);
		info.setValues(width, height, null, null, true, true);
		
		RenderedImage ri = svg1.getNewRenderedImage(info);
		Image img = ri.getSWTImage();
		assertNotNull("getSWTImage fixture 1 Image invalid", img ); //$NON-NLS-1$
	}
	
	final static int START_DIM = 100;
	final static int END_DIM = 1000;
	final static int INC_DIM = 10;
	
	public void testRenderedImagePerformance() {
		
		SVGImage svg1 = (SVGImage)getFixture1();
		
		int width = START_DIM;
		int height = START_DIM;
		long batikTime = 0;
		long renderTime = 0;
		
		// do one rendering outside to initialize Batik;
		performBatikRendering(svg1, width, height);
		
		long startTime = System.currentTimeMillis();
		
		while (width < END_DIM && height < END_DIM) {
			performBatikRendering(svg1, width, height);
			width += INC_DIM;
			height += INC_DIM;
		}
		
		long endTime = System.currentTimeMillis();
		batikTime = endTime - startTime;
		
		startTime = System.currentTimeMillis();
		
		width = START_DIM;
		height = START_DIM;
		while (width < END_DIM && height < END_DIM) {
			performRenderedImageRendering(svg1, width, height);
			width += INC_DIM;
			height += INC_DIM;
		}
		
		endTime = System.currentTimeMillis();
		renderTime = endTime - startTime;
		
		System.out.println("Batik rendering time was: " + batikTime); //$NON-NLS-1$
		System.out.println("RenderedImage rendering time was: " + renderTime); //$NON-NLS-1$
		System.out.println("Percentage difference: " + (renderTime - batikTime) / (float)batikTime * 100 + "%"); //$NON-NLS-1$ //$NON-NLS-2$
		
		assertTrue(renderTime < batikTime);
	}
	
	public void testGetSWTImage() {

		Image img = getFixture1().getSWTImage();
		assertNotNull("getSWTImage fixture 1 Image invalid", img ); //$NON-NLS-1$
							
		img = getFixture2().getSWTImage();
		assertNotNull("getSWTImage fixture 2 Image invalid", img ); //$NON-NLS-1$
	}
}
