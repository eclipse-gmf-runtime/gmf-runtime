/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.draw2d.ui.render.internal.svg;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.image.ImageConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.ImageTranscoderEx;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.SVGImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.RenderHelper;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.RenderingListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * @author sshaw
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SVGImageTest
	extends TestCase {

	/**
	 * The icons root directory.
	 */
	private static final String PREFIX_ROOT = "resources/svg/"; //$NON-NLS-1$

	private static final String SVG_BLACKWHITE = PREFIX_ROOT + "blackwhite.svg"; //$NON-NLS-1$
	private static final String SVG_SHAPES = PREFIX_ROOT + "shapes.svg";//$NON-NLS-1$
	private static final String SVG_UMLSHAPES = PREFIX_ROOT + "uml.svg";//$NON-NLS-1$
    private static final String SVG_TRANSPARENCY_TEST = PREFIX_ROOT + "presenter.svg";//$NON-NLS-1$

	private final int WIDTH = 200;

	private final int HEIGHT = 200;

	private final int NEW_WIDTH = 120;

	private final int NEW_HEIGHT = 140;

	private final RGB NEW_FILL = new RGB(255, 0, 0);

	private final RGB NEW_OUTLINE = new RGB(0, 255, 0);

	private RenderedImage fixture1;

	private RenderedImage fixture2;

	private RenderedImage fixture3;
    
    private RenderedImage fixture4;

	public SVGImageTest(String name) {
		super(name);
	}

	private RenderedImage getFixture1() {
		return fixture1;
	}

	private RenderedImage getFixture2() {
		return fixture2;
	}

	private RenderedImage getFixture3() {
		return fixture3;
	}
    
    private RenderedImage getFixture4() {
        return fixture4;
    }

	protected void setUp() {
		try {

			// Initialize the path the the resources.
			Bundle bundle = Platform.getBundle("org.eclipse.gmf.tests.runtime.draw2d.ui.render" ); //$NON-NLS-1$
			
			URL url = FileLocator.find(bundle, new Path(SVG_UMLSHAPES), null);
            fixture1 = RenderedImageFactory.getInstance(url); 
            
            assertNotNull("Fixture1 shouldn't be null", fixture1); //$NON-NLS-1$

			RenderInfo info = RenderedImageFactory.createInfo(WIDTH, HEIGHT,
				false, false, (RGB) null, (RGB) null);
            url = FileLocator.find(bundle, new Path(SVG_SHAPES), null);
            fixture2 = RenderedImageFactory.getInstance(url, info); 
			assertNotNull("Fixture2 shouldn't be null", fixture2); //$NON-NLS-1$

            url = FileLocator.find(bundle, new Path(SVG_BLACKWHITE), null);
			fixture3 = RenderedImageFactory.getInstance(url);	
			assertNotNull("Fixture3 shouldn't be null", fixture3); //$NON-NLS-1$
            
            url = FileLocator.find(bundle, new Path(SVG_TRANSPARENCY_TEST), null);
            fixture4 = RenderedImageFactory.getInstance(url);   
            assertNotNull("Fixture3 shouldn't be null", fixture4); //$NON-NLS-1$

		} catch (Exception e) {
			fail("The SVGImageTest.setUp method caught an exception - " + e); //$NON-NLS-1$
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

		RenderInfo info = RenderedImageFactory.createInfo(NEW_WIDTH,
			NEW_HEIGHT, true, true, NEW_FILL, NEW_OUTLINE);
		assertTrue(info != null);
		info = RenderedImageFactory.createInfo(NEW_WIDTH, NEW_HEIGHT, false, false, null,
			NEW_OUTLINE);
		assertTrue(info != null);
	}

	private void performBatikRendering(SVGImage svg1, int width, int height) {
		InputStream in = new ByteArrayInputStream(svg1.getBuffer());
		RenderInfo info = svg1.getRenderInfo();
		ImageTranscoderEx transcoder = new ImageTranscoderEx();

		if (width > 0)
			transcoder.addTranscodingHint(ImageTranscoderEx.KEY_WIDTH,
				new Float(width));
		if (height > 0)
			transcoder.addTranscodingHint(ImageTranscoderEx.KEY_HEIGHT,
				new Float(height));

		transcoder.addTranscodingHint(
			ImageTranscoderEx.KEY_MAINTAIN_ASPECT_RATIO, Boolean.valueOf(info
				.shouldMaintainAspectRatio()));

		transcoder.addTranscodingHint(ImageTranscoderEx.KEY_ANTI_ALIASING,
			Boolean.valueOf(info.shouldAntiAlias()));

		if (info.getBackgroundColor() != null) {
			transcoder
				.addTranscodingHint(ImageTranscoderEx.KEY_FILL_COLOR, new RGB(
					info.getBackgroundColor().red,
					info.getBackgroundColor().green,
					info.getBackgroundColor().blue));
		}

		if (info.getForegroundColor() != null) {
			transcoder
				.addTranscodingHint(ImageTranscoderEx.KEY_OUTLINE_COLOR,
					new RGB(info.getForegroundColor().red, info
						.getForegroundColor().green,
						info.getForegroundColor().blue));
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
		assertNotNull("swtImage fixture 1 Image invalid", swtImage); //$NON-NLS-1$
	}

	private void performRenderedImageRendering(SVGImage svg1, int width,
			int height) {
		RenderInfo info = svg1.getRenderInfo();

		info.setValues(width, height, true, true, info.getBackgroundColor(), info
			.getForegroundColor());

		RenderedImage ri = svg1.getNewRenderedImage(info);
		Image img = ri.getSWTImage();
		assertNotNull("getSWTImage fixture 1 Image invalid", img); //$NON-NLS-1$
	}

	final static int START_DIM = 100;

	final static int END_DIM = 1000;

	final static int INC_DIM = 10;

	public void testRenderedImagePerformance() {

		SVGImage svg1 = (SVGImage) getFixture1();

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
		System.out
			.println("Percentage difference: " + (renderTime - batikTime) / (float) batikTime * 100 + "%"); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(renderTime < batikTime);
	}

	static private class RenderingListenerImpl
		implements RenderingListener {

		boolean done = false;

		public RenderingListenerImpl() {
			super();
		}

		boolean isDone() {
			return done;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.draw2d.ui.render.internal.RenderingListener#paintFigureWhileRendering(org.eclipse.draw2d.Graphics)
		 */
		public void paintFigureWhileRendering(Graphics g) {
			// do nothing
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.draw2d.ui.render.RenderingListener#imageRendered(org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage)
		 */
		synchronized public void imageRendered(RenderedImage rndImg) {
			done = true;
			assertTrue(rndImg.isRendered());
			assertTrue(rndImg.getSWTImage() != null);
		}
	}

	public void testRenderHelper() {

		Display display = Display.getDefault();

		Image image = new Image(display, new Rectangle(0, 0, 500, 500));
		GC gc = new GC(image);
		SWTGraphics swtG = new SWTGraphics(gc);

		org.eclipse.draw2d.geometry.Rectangle target = new org.eclipse.draw2d.geometry.Rectangle(
			50, 50, 94, 94);
		RenderInfo info = getFixture1().getRenderInfo();
		info.setValues(target.width, target.height, true, true, info.getBackgroundColor(),
			info.getForegroundColor());

		RenderedImage ri = getFixture1().getNewRenderedImage(info);

		// first test with delay render turned off
		RenderHelper renderHelper = RenderHelper.getInstance(1.0, false, false,
			null);
		RenderingListenerImpl listener = new RenderingListenerImpl();
		RenderedImage retImage = renderHelper.drawRenderedImage(swtG, ri,
			target, listener);
		assertTrue(retImage.isRendered());
		assertTrue(retImage.getSWTImage() != null);

		// second test with delay render turned on
		target.width -= 10;
		target.height -= 10;
		listener = new RenderingListenerImpl();
		renderHelper = RenderHelper.getInstance(1.0, false, true, null);

		retImage = renderHelper.drawRenderedImage(swtG, retImage, target,
			listener);

		int i = 0;
		while (!retImage.isRendered() && i < 100) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			i++;
		}

		assertTrue(retImage.isRendered() && i < 100);
	}

	static final private Dimension MAX_RENDER_SIZE = new Dimension(200, 200);

	public void testRenderMaxSize() {
		Display display = PlatformUI.getWorkbench().getDisplay();

		Image image = new Image(display, new Rectangle(0, 0, 500, 500));
		GC gc = new GC(image);
		SWTGraphics swtG = new SWTGraphics(gc);

		org.eclipse.draw2d.geometry.Rectangle target = new org.eclipse.draw2d.geometry.Rectangle(
			0, 0, MAX_RENDER_SIZE.width * 2, MAX_RENDER_SIZE.height * 2);
		RenderInfo info = getFixture1().getRenderInfo();
		info.setValues(MAX_RENDER_SIZE.width * 2, MAX_RENDER_SIZE.height * 2,
			false, true, info.getBackgroundColor(), info.getForegroundColor());

		RenderedImage ri = getFixture1().getNewRenderedImage(info);

		// first test with no maximum render size
		RenderHelper renderHelper = RenderHelper.getInstance(1.0, false, false,
			null);
		RenderingListenerImpl listener = new RenderingListenerImpl();
		RenderedImage retImage = renderHelper.drawRenderedImage(swtG, ri,
			target, listener);
		assertTrue(retImage.isRendered());
		assertTrue(retImage.getSWTImage() != null);
		assertTrue(Math.abs(retImage.getSWTImage().getBounds().width - (MAX_RENDER_SIZE.width * 2)) <= 5);
		assertTrue(Math.abs(retImage.getSWTImage().getBounds().height - (MAX_RENDER_SIZE.height * 2)) <=5);
		
		// now test with maximum render size
		renderHelper = RenderHelper.getInstance(1.0, false, false,
			MAX_RENDER_SIZE);
		retImage = renderHelper.drawRenderedImage(swtG, ri,
			target, listener);
		assertTrue(retImage.isRendered());
		assertTrue(retImage.getSWTImage() != null);
		assertTrue(retImage.getSWTImage().getBounds().width <= MAX_RENDER_SIZE.width);
		assertTrue(retImage.getSWTImage().getBounds().height <= MAX_RENDER_SIZE.height);
	}

	private boolean findColor(Image srcImage, RGB colorToFind) {
		BufferedImage bufImg = ImageConverter.convert(srcImage);

		int width = bufImg.getWidth();
		int height = bufImg.getHeight();

		// loop over the imagedata and set each pixel in the BufferedImage to
		// the appropriate color.
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int color = bufImg.getRGB(x, y);
				Color swtColor = integerToColor(color);
				RGB checkColor = new RGB(swtColor.getRed(),
					swtColor.getGreen(), swtColor.getBlue());
				swtColor.dispose();
				if (checkColor.equals(colorToFind))
					return true;
			}
		}

		return false;
	}

	/**
	 * Method integerToColor. converts from an int (from BufferedImage) to a
	 * Color representation
	 * 
	 * Note: Normally, colors should be instantiated using the
	 * AbstractResourceManager.
	 * 
	 * @param i
	 * @return Color
	 */
	private Color integerToColor(int color) {
		return new Color(null, (color & 0x00FF0000) >> 16,
			(color & 0x0000FF00) >> 8, (color & 0x000000FF));
	}

	private static RGB BLACK = new RGB(0, 0, 0);

	private static RGB WHITE = new RGB(255, 255, 255);

	private static RGB RED = new RGB(255, 0, 0);

	private static RGB GREEN = new RGB(0, 255, 0);

	public void testChangeColors() {
		RenderedImage fixture = getFixture3();
		RenderInfo info = fixture.getRenderInfo();
		Image swtImg1 = fixture.getSWTImage();
		assertTrue(findColor(swtImg1, BLACK));
		assertTrue(findColor(swtImg1, WHITE));
		assertFalse(findColor(swtImg1, RED));
		assertFalse(findColor(swtImg1, GREEN));
		
//		ImageLoader imageLoader = new ImageLoader();
//		imageLoader.data = new ImageData[] {swtImg1.getImageData()};
//		imageLoader.logicalScreenHeight = swtImg1.getBounds().width;
//		imageLoader.logicalScreenHeight = swtImg1.getBounds().height;
//		imageLoader.save("C:\\originalImg.bmp", SWT.IMAGE_BMP);
		
		// set color first time - red fill, red outline
		info.setValues(info.getWidth(), info.getHeight(), true, true, RED, RED);
		RenderedImage colorImg1 = fixture.getNewRenderedImage(info);
		Image swtImg2 = colorImg1.getSWTImage();
		assertNotNull("getSWTImage colorImg1 Image invalid", swtImg1); //$NON-NLS-1$
		
//		imageLoader = new ImageLoader();
//		imageLoader.data = new ImageData[] {swtImg2.getImageData()};
//		imageLoader.logicalScreenHeight = swtImg2.getBounds().width;
//		imageLoader.logicalScreenHeight = swtImg2.getBounds().height;
//		imageLoader.save("C:\\redImg.bmp", SWT.IMAGE_BMP);
		
		assertTrue(findColor(swtImg2, RED));
		assertFalse(findColor(swtImg2, GREEN));

		// set color second time - green fill, green outline
		info.setValues(info.getWidth(), info.getHeight(), true,
			true, GREEN, GREEN);
		RenderedImage colorImg2 = fixture.getNewRenderedImage(info);
		Image swtImg3 = colorImg2.getSWTImage();
		assertNotNull("getSWTImage colorImg1 Image invalid", swtImg1); //$NON-NLS-1$
		assertNotNull("getSWTImage colorImg2 Image invalid", swtImg2); //$NON-NLS-1$
		
//		imageLoader = new ImageLoader();
//		imageLoader.data = new ImageData[] {swtImg3.getImageData()};
//		imageLoader.logicalScreenHeight = swtImg3.getBounds().width;
//		imageLoader.logicalScreenHeight = swtImg3.getBounds().height;
//		imageLoader.save("C:\\greenImg.bmp", SWT.IMAGE_BMP);
		
		assertTrue(colorImg1 != colorImg2);
		assertTrue(findColor(swtImg3, GREEN));
		assertFalse(findColor(swtImg3, RED));
	}

	// cropping not supported yet

	// static final int SHRINK_EXT = 50;
	// static final int CROPTEST_START_SIZE = 400;
	//
	// public void testCroppedImage() {
	// org.eclipse.draw2d.geometry.Rectangle target = new
	// org.eclipse.draw2d.geometry.Rectangle(0, 0, CROPTEST_START_SIZE,
	// CROPTEST_START_SIZE);
	// RenderInfo info = getFixture1().getRenderInfo();
	// info.setValues(target.width, target.height, info.getBackgroundColor(),
	// info.getForegroundColor(), false, true);
	//		
	// RenderedImage first = getFixture1().getNewRenderedImage(info);
	// Image firstImage = first.getSWTImage();
	// Rectangle firstBounds = firstImage.getBounds();
	// assertTrue(firstBounds.width == CROPTEST_START_SIZE && firstBounds.height
	// == CROPTEST_START_SIZE);
	// ImageLoader imageLoader = new ImageLoader();
	// imageLoader.data = new ImageData[] {firstImage.getImageData()};
	// imageLoader.logicalScreenHeight = firstImage.getBounds().width;
	// imageLoader.logicalScreenHeight = firstImage.getBounds().height;
	// imageLoader.save("C:\\originalImg.bmp", SWT.IMAGE_BMP);
	//		
	// RenderedImage cropped = first.getCroppedImage(target.shrink(SHRINK_EXT,
	// SHRINK_EXT));
	//		
	// Image croppedImage = cropped.getSWTImage();
	// Rectangle croppedBounds = croppedImage.getBounds();
	// //assertTrue(croppedBounds.width == CROPTEST_START_SIZE - (SHRINK_EXT *
	// 2)
	// // && croppedBounds.height == CROPTEST_START_SIZE - (SHRINK_EXT * 2));
	//		
	// imageLoader = new ImageLoader();
	// imageLoader.data = new ImageData[] {croppedImage.getImageData()};
	// imageLoader.logicalScreenHeight = croppedImage.getBounds().width;
	// imageLoader.logicalScreenHeight = croppedImage.getBounds().height;
	// imageLoader.save("C:\\croppedImg.bmp", SWT.IMAGE_BMP);
	// }

	public void testGetSWTImage() {
		Image img = getFixture1().getSWTImage();
		assertNotNull("getSWTImage fixture 1 Image invalid", img); //$NON-NLS-1$

		img = getFixture2().getSWTImage();
		assertNotNull("getSWTImage fixture 2 Image invalid", img); //$NON-NLS-1$
	}
    
    public void testTransparency() {
        Image img = getFixture4().getSWTImage();
        ImageData imgData = img.getImageData();
        Rectangle bounds = img.getBounds();
        
        // check 4 corners
        assertTrue(isTransparentAt(imgData, bounds.x + 1, bounds.y + 1));
        assertTrue(isTransparentAt(imgData, bounds.x + bounds.width - 2, bounds.y + 1));
        assertTrue(isTransparentAt(imgData, bounds.x + 1, bounds.y + bounds.height - 2));
        assertTrue(isTransparentAt(imgData, bounds.x + bounds.width - 2, bounds.y + bounds.height - 2));
    }
    
    protected boolean isTransparentAt(ImageData data, int x, int y) {
        // boundary checking
        if (x < 0 || x >= data.width || y < 0
            || y >= data.height)
            return true;

        ImageData transMaskData = data.getTransparencyMask();
        // check for alpha channel
        int transValue = 255;
        // check for transparency mask
        if (transMaskData != null) {
            transValue = transMaskData.getPixel(x, y) == 0 ? 0
                : 255;
        }

        if (transValue != 0) {
            if (data.alphaData != null) {
                transValue = data.getAlpha(x, y);
            }
        }

        // use a tolerance
        boolean trans = false;
        if (transValue < 10) {
            trans = true;
        }

        return trans;
    }
}
