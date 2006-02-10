/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;

import org.apache.batik.ext.awt.g2d.DefaultGraphics2D;
import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.image.ImageConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Transform;

/**
 * Objects of this class can be used with awt to render to a SWT Graphics
 * object.
 * 
 * @author sshaw
 * 
 */
public class Graphics2DToGraphicsAdaptor
	extends DefaultGraphics2D {

	private Graphics swtGraphics;
	private org.eclipse.swt.graphics.Font currentFont = null;
	private org.eclipse.swt.graphics.Color currentColor = null;
	private Transform currentTransform = null;
	private GC swtGC;
	private RGB transparency;
	private RGB transparency_replace;
	
	/**
	 * Constructor
	 * 
	 * @param swtGC the <code>GC</code> to render to
	 * @param transparency the <code>RGB</code> that will be the transparent color
	 * @param transparency_replace the <code>RGB</code> that will replace the transparent color
	 * in the rendering.
	 */
	public Graphics2DToGraphicsAdaptor(GC swtGC, RGB transparency, RGB transparency_replace) {
		super(true);
		gc = new GraphicContext();
		this.swtGC = swtGC;
		this.swtGraphics = new SWTGraphics(swtGC);
		this.transparency = transparency;
		this.transparency_replace = transparency_replace;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.apache.batik.ext.awt.g2d.DefaultGraphics2D#dispose()
	 */
	public void dispose() {
		super.dispose();
		
		if (currentColor != null)
			currentColor.dispose();
		currentColor = null;
		if (currentFont != null)
			currentFont.dispose();
		currentFont = null;
		if (currentTransform != null) 
			currentTransform.dispose();
		currentTransform = null;
		
		if (swtGraphics != null)
			swtGraphics.dispose();
		swtGraphics = null;
	}

	/**
	 * Setup the swt graphics object with the appropriate configuration details.
	 */
	protected final boolean configureGraphics() {
		swtGraphics.pushState();

		boolean supported = true;
		
		supported &= configureStroke(getStroke());
		supported &= configureTransformation(getTransform());
		supported &= configureClipping(getClip());
		supported &= configureComposite(getComposite());
		supported &= configurePaintMode(getPaint());
		supported &= configureRenderingHints();

		return supported;
	}

	private boolean configureRenderingHints() {
		Object antiAlias = getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		if (antiAlias != null) {
			if (antiAlias.equals(RenderingHints.VALUE_ANTIALIAS_ON))
				swtGraphics.setAntialias(SWT.ON);
			else if (antiAlias.equals(RenderingHints.VALUE_ANTIALIAS_OFF))
				swtGraphics.setAntialias(SWT.OFF);
			else if (antiAlias.equals(RenderingHints.VALUE_ANTIALIAS_DEFAULT))
				swtGraphics.setAntialias(SWT.DEFAULT);
		}

		Object textAntiAlias = getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
		if (textAntiAlias != null) {
			if (textAntiAlias.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_ON))
				swtGraphics.setTextAntialias(SWT.ON);
			else if (textAntiAlias
				.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF))
				swtGraphics.setTextAntialias(SWT.OFF);
			else if (textAntiAlias
				.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT))
				swtGraphics.setTextAntialias(SWT.DEFAULT);
		}

		Object interpolation = getRenderingHint(RenderingHints.KEY_INTERPOLATION);
		if (interpolation != null) {
			if (interpolation
				.equals(RenderingHints.VALUE_INTERPOLATION_BICUBIC))
				swtGraphics.setInterpolation(SWT.HIGH);
			else if (interpolation
				.equals(RenderingHints.VALUE_INTERPOLATION_BILINEAR))
				swtGraphics.setInterpolation(SWT.LOW);
			else if (interpolation
				.equals(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR))
				swtGraphics.setInterpolation(SWT.NONE);
		}
		return true;
	}

	private boolean configureStroke(Stroke s) {
		if (s instanceof BasicStroke) {
			BasicStroke basicStroke = (BasicStroke) s;
			switch (basicStroke.getEndCap()) {
				case BasicStroke.CAP_BUTT:
					swtGC.setLineCap(SWT.CAP_FLAT);
					break;
				case BasicStroke.CAP_ROUND:
					swtGC.setLineCap(SWT.CAP_ROUND);
					break;
				case BasicStroke.CAP_SQUARE:
					swtGC.setLineCap(SWT.CAP_SQUARE);
					break;
				default:
					return false;
			}

			if (basicStroke.getDashArray() != null) {
				swtGC.setLineStyle(SWT.LINE_CUSTOM);
				float[] currentDash = basicStroke.getDashArray();
				int[] dash = new int[currentDash.length];
				for (int i = 0; i < currentDash.length; i++)
					dash[i] = Math.round(currentDash[i]);
				swtGC.setLineDash(dash);
			} else {
				swtGC.setLineStyle(SWT.LINE_SOLID);
				swtGC.setLineDash(null);
			}

			switch (basicStroke.getLineJoin()) {
				case BasicStroke.JOIN_BEVEL:
					swtGC.setLineJoin(SWT.JOIN_BEVEL);
					break;
				case BasicStroke.JOIN_MITER:
					swtGC.setLineJoin(SWT.JOIN_MITER);
					break;
				case BasicStroke.JOIN_ROUND:
					swtGC.setLineJoin(SWT.JOIN_ROUND);
					break;
				default:
					return false;
			}

			// since we don't have precision of less then 1 pixel in
			// swt, if the image is defined as very small in the original
			// file, then the linewidth gets scaled out of proportion.
			if (basicStroke.getLineWidth() < 1 &&
				basicStroke.getLineWidth() > 0)
				throw new UnsupportedOperationException();
			
			swtGC.setLineWidth(Math.round(basicStroke.getLineWidth()));
			return true;
		}

		return false;
	}

	private boolean configurePaintMode(Paint p) {
		if (p instanceof Color) {
			Color c = (Color) p;
			
			if (c.getAlpha() != 255) {
				// swt graphics doesn't support blitting onto transparency mask explicitly.
				throw new UnsupportedOperationException();
			}
		
			RGB rgb = new RGB(c.getRed(), c.getGreen(), c.getBlue());
			if (rgb.equals(transparency))
				rgb = transparency_replace;
			
			if (currentColor != null)
				currentColor.dispose();
			currentColor = new org.eclipse.swt.graphics.Color(
				null, rgb.red, rgb.green, rgb.blue);
			swtGraphics.setBackgroundColor(currentColor);
			swtGraphics.setForegroundColor(currentColor);
			return true;
		} 

		return false;
	}

	private boolean configureComposite(Composite c) {
		if (c instanceof AlphaComposite) {
			AlphaComposite ac = (AlphaComposite)c;
			
			// swt graphics doesn't support blitting onto transparency mask explicitly.
			if (ac.getAlpha() != 1.0)
				throw new UnsupportedOperationException();
			
			swtGC.setAlpha(Math.round(ac.getAlpha() * 255));
			if (ac.getRule() == AlphaComposite.SRC_OVER)
				return true;
		} 

		return false;
	}

	private boolean configureTransformation(AffineTransform cxPrime) {
		double[] values = new double[6];
		cxPrime.getMatrix(values);
		
		float[] fltVals = new float[6];
		for (int i=0; i<values.length; i++)
			fltVals[i] = (float)values[i];
		
		if (currentTransform != null)
			currentTransform.dispose();
		currentTransform = new Transform(null, fltVals);
		swtGC.setTransform(currentTransform);
		
		return true;
	}

	private boolean configureClipping(Shape clipShape) {
		Path path = getPath(clipShape);
		java.awt.Rectangle rectClip = clipShape.getBounds();
		if (path != null) {
			swtGraphics.setClip(new Rectangle(rectClip.x, rectClip.y,
				rectClip.width, rectClip.height));
			swtGC.setClipping(path);
		}
		return true;
	}

	private final AffineTransform identityTransform = new AffineTransform();
	private final Stroke defaultStroke = new BasicStroke();
	
	/**
	 * Reset the graphics objects back to defaults
	 */
	protected final void resetGraphics() {
		configureTransformation(identityTransform);
		configurePaintMode(Color.WHITE);
		configureStroke(defaultStroke);
		configureComposite(AlphaComposite.SrcOver);
		
		if (currentColor != null)
			currentColor.dispose();
		currentColor = null;
		if (currentFont != null)
			currentFont.dispose();
		currentFont = null;
		if (currentTransform != null) 
			currentTransform.dispose();
		currentTransform = null;
		
		swtGC.setClipping((Path)null);
		swtGraphics.popState();
	}

	private void drawImageBase(BufferedImage bufImg, int x, int y) {
		drawImageBase(bufImg, 0, 0, bufImg.getWidth(), bufImg.getHeight(), x, y, 
			bufImg.getWidth(), bufImg.getHeight());
	}
	
	private void drawImageBase(BufferedImage bufImg, int x, int y, int width, int height, 
			int tx, int ty, int twidth, int theight) {
		org.eclipse.swt.graphics.Image swtImage = ImageConverter.convert(bufImg);
		swtGraphics.drawImage(swtImage, x, y, width, height, tx, ty, twidth, theight);
		swtImage.dispose();
	}
	
	private BufferedImage createImage(Shape s) {
		java.awt.Rectangle rect = s.getBounds();
		if (rect.width == 0)
			rect.width = 1;
		if (rect.height == 0)
			rect.height = 1;
		return new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
	}
	
	private Graphics2D configureGraphics2D(BufferedImage bufImg, int x, int y) {
		Graphics2D g = bufImg.createGraphics();
		g.setPaint(getPaint());
		g.setComposite(getComposite());
		g.setStroke(getStroke());
		g.setRenderingHints(getRenderingHints());
		g.setFont(getFont());
		g.translate(-x, -y);
		return g;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.batik.ext.awt.g2d.DefaultGraphics2D#draw(java.awt.Shape)
	 */
	public void draw(Shape s) {
		boolean supportedByDraw2d = configureGraphics();
		if (supportedByDraw2d) {
			Path path = getPath(s);
			if (path != null)
				swtGraphics.drawPath(path);
		} else {
			java.awt.Rectangle rect = s.getBounds();
			BufferedImage bufImg = createImage(s);
			Graphics2D g = configureGraphics2D(bufImg, rect.x, rect.y);
			g.draw(s);
			
			drawImageBase(bufImg, rect.x, rect.y);
		}

		resetGraphics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.batik.ext.awt.g2d.DefaultGraphics2D#fill(java.awt.Shape)
	 */
	public void fill(Shape s) {
		boolean supportedByDraw2d = configureGraphics();
		if (supportedByDraw2d) {
			Path path = getPath(s);
			if (path != null)
				swtGraphics.fillPath(path);
		} else {
			java.awt.Rectangle rect = s.getBounds();
			BufferedImage bufImg = createImage(s);
			Graphics2D g = configureGraphics2D(bufImg, rect.x, rect.y);
			g.fill(s);
			
			drawImageBase(bufImg, rect.x, rect.y);
		}

		resetGraphics();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.batik.ext.awt.g2d.DefaultGraphics2D#drawImage(java.awt.Image,
	 *      int, int, java.awt.image.ImageObserver)
	 */
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		boolean supportedByDraw2d = configureGraphics();

		if (img instanceof BufferedImage && supportedByDraw2d) {
			drawImageBase((BufferedImage)img, x, y);
		} else {
			throw new UnsupportedOperationException();
		}

		resetGraphics();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.batik.ext.awt.g2d.DefaultGraphics2D#drawImage(java.awt.Image,
	 *      int, int, int, int, java.awt.image.ImageObserver)
	 */
	public boolean drawImage(Image img, int x, int y, int width, int height,
			ImageObserver observer) {
		boolean supportedByDraw2d = configureGraphics();

		if (img instanceof BufferedImage && supportedByDraw2d) {
			BufferedImage bufImg = (BufferedImage)img;
			drawImageBase((BufferedImage)img, 0, 0, bufImg.getWidth(), bufImg.getHeight(),
					x, y, width, height);
		} else {
			throw new UnsupportedOperationException();
		}

		resetGraphics();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.batik.ext.awt.g2d.DefaultGraphics2D#drawRenderableImage(java.awt.image.renderable.RenderableImage,
	 *      java.awt.geom.AffineTransform)
	 */
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.batik.ext.awt.g2d.DefaultGraphics2D#drawRenderedImage(java.awt.image.RenderedImage,
	 *      java.awt.geom.AffineTransform)
	 */
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.batik.ext.awt.g2d.DefaultGraphics2D#drawString(java.text.AttributedCharacterIterator,
	 *      float, float)
	 */
	public void drawString(AttributedCharacterIterator iterator, float x,
			float y) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.batik.ext.awt.g2d.DefaultGraphics2D#drawString(java.lang.String,
	 *      float, float)
	 */
	public void drawString(String s, float x, float y) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Used to create proper font metrics
	 */
	private GraphicsConfiguration gconfig;

	{
		BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		gconfig = bi.createGraphics().getDeviceConfiguration();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.batik.ext.awt.g2d.DefaultGraphics2D#getDeviceConfiguration()
	 */
	public GraphicsConfiguration getDeviceConfiguration() {
		return gconfig;
	}

	private Path getPath(Shape s) {
		PathIterator pi = s.getPathIterator(null);
		Path path = new Path(null);
		float seg[] = new float[6];
		int segType = 0;
		while (!pi.isDone()) {
			segType = pi.currentSegment(seg);
			switch (segType) {
				case PathIterator.SEG_LINETO:
					path.lineTo(seg[0], seg[1]);
					break;
				case PathIterator.SEG_CLOSE:
					path.close();
					break;
				case PathIterator.SEG_MOVETO:
					path.moveTo(seg[0], seg[1]);
					break;
				case PathIterator.SEG_QUADTO:
					path.quadTo(seg[0], seg[1], seg[2], seg[3]);
					break;
				case PathIterator.SEG_CUBICTO:
					path
						.cubicTo(seg[0], seg[1], seg[2], seg[3], seg[4], seg[5]);
					break;
				default:
					throw new Error();
			}
			pi.next();
		}

		return path;
	}

}
