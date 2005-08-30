/****************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.graphics;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;


/**
 * The MapModeGraphics class is used to translate the various map modes.
 *
 * @author jschofie
 */
public class MapModeGraphics
	extends Graphics {

	/**
	 * The internal state of the MapMode graphics.
	 */
	protected static class State {
		private double appliedX;
		private double appliedY;


		/**
		 * Constructs a new, uninitialized State object.
		 */
		protected State() {
			// intentionally left blank
		}
		
		/**
		 * Constructs a new State object and initializes the properties based on the given 
		 * values.
		 *
		 * @param x the x offset
		 * @param y the y offset
		 */
		protected State(double x, double y) {
			this.appliedX = x;
			this.appliedY = y;
		}

		/**
		 * Sets all the properties of the state object.
		 * @param x the x offset
		 * @param y the y offset
		 */
		protected void setValues(double x, double y) {
			this.appliedX = x;
			this.appliedY = y;
		}
	}
	
	private List stack = new ArrayList();
	private int stackPointer = 0;


	private static final Rectangle TEMP = new Rectangle();

	private Graphics graphics;
	private double fractionalX;
	private double fractionalY;
	private double mapModeScale = 1.0;

	/**
	 * Private Constructor used to prevent clients from accidently creating
	 * objects via the default constructor
	 */
	private MapModeGraphics() {
		// Prevent clients from creating objects of this class with 
		// the default constructor
	}

	/**
	 * Constructs a new ScaledGraphics based on the given Graphics object.
	 * @param g the base graphics object
	 */
	public MapModeGraphics(Graphics g) {
		// Force an exception if the Graphics is null
		Assert.isNotNull(g);

		graphics = g;
		mapModeScale = MapMode.getScale();
	}

	/** @see Graphics#clipRect(Rectangle) */
	public void clipRect(Rectangle r) {
		graphics.clipRect(mapClipRect(r));
	}

	/** @see Graphics#dispose() */
	public void dispose() {

		stack.clear();
	}

	/** @see Graphics#drawArc(int, int, int, int, int, int) */
	public void drawArc(int x, int y, int w, int h, int offset, int sweep) {
		Rectangle z = mapRect(x, y, w, h);
		if (z.isEmpty() || sweep == 0)
			return;
		graphics.drawArc(z, offset, sweep);
	}

	/** @see Graphics#fillArc(int, int, int, int, int, int) */
	public void fillArc(int x, int y, int w, int h, int offset, int sweep) {
		Rectangle z = mapFillRect(x, y, w, h);
		if (z.isEmpty() || sweep == 0)
			return;
		graphics.fillArc(z, offset, sweep);
	}

	/** @see Graphics#fillGradient(int, int, int, int, boolean) */
	public void fillGradient(int x, int y, int w, int h, boolean vertical) {
		graphics.fillGradient(mapFillRect(x, y, w, h), vertical);
	}

	/** @see Graphics#drawFocus(int, int, int, int) */
	public void drawFocus(int x, int y, int w, int h) {
		graphics.drawFocus(mapRect(x, y, w, h));
	}

	/** @see Graphics#drawImage(Image, int, int) */
	public void drawImage(Image srcImage, int x, int y) {
		org.eclipse.swt.graphics.Rectangle size = srcImage.getBounds();
		
		int targetWidth  = MapMode.DPtoLP( size.width );
		int targetHeight = MapMode.DPtoLP( size.height );
		graphics.drawImage(srcImage, 0, 0, size.width, size.height,
			(int)(Math.floor((x * mapModeScale + fractionalX))), 
			(int)(Math.floor((y * mapModeScale + fractionalY))),
			(int)(Math.floor((targetWidth * mapModeScale + fractionalX))), 
			(int)(Math.floor((targetHeight * mapModeScale + fractionalY))));
	}

	/** @see Graphics#drawImage(Image, int, int, int, int, int, int, int, int) */
	public void drawImage(Image srcImage, int sx, int sy, int sw, int sh,
											int tx, int ty, int tw, int th) {
		//"t" == t, "s" = source
		tw = MapMode.DPtoLP(tw);
		th = MapMode.DPtoLP(th);
		Rectangle t = mapRect(tx, ty, tw, th);
		graphics.drawImage(srcImage, sx, sy, sw, sh, t.x, t.y, t.width, t.height);
	}

	/** @see Graphics#drawLine(int, int, int, int) */
	public void drawLine(int x1, int y1, int x2, int y2) {
		graphics.drawLine(
			(int)(Math.floor((x1 * mapModeScale + fractionalX))),
			(int)(Math.floor((y1 * mapModeScale + fractionalY))),
			(int)(Math.floor((x2 * mapModeScale + fractionalX))),
			(int)(Math.floor((y2 * mapModeScale + fractionalY))));
	}

	/** @see Graphics#drawOval(int, int, int, int) */
	public void drawOval(int x, int y, int w, int h) {
		graphics.drawOval(mapRect(x, y, w, h));
	}

	/** @see Graphics#fillOval(int, int, int, int) */
	public void fillOval(int x, int y, int w, int h) {
		graphics.fillOval(mapFillRect(x, y, w, h));
	}

	/**
	 * @see Graphics#drawPolygon(int[])
	 */
	public void drawPolygon(int[] points) {
		graphics.drawPolygon(mapPointList(points));
	}

	/** @see Graphics#drawPolygon(PointList) */
	public void drawPolygon(PointList points) {
		Assert.isNotNull(points);
		graphics.drawPolygon(mapPointList(points.toIntArray()));
	}

	/** @see Graphics#drawPoint(int, int) */
	public void drawPoint(int x, int y) {
		graphics.drawPoint((int)Math.floor(x * mapModeScale + fractionalX),(int)Math.floor(y * mapModeScale + fractionalY));
	}

	/**
	 * @see Graphics#fillPolygon(int[])
	 */
	public void fillPolygon(int[] points) {
		graphics.fillPolygon(mapPointList(points));
	}

	/** @see Graphics#fillPolygon(PointList) */
	public void fillPolygon(PointList points) {
		Assert.isNotNull(points);
		graphics.fillPolygon(mapPointList(points.toIntArray()));
	}

	/**
	 * @see Graphics#drawPolyline(int[])
	 */
	public void drawPolyline(int[] points) {
		graphics.drawPolyline(mapPointList(points));
	}

	/** @see Graphics#drawPolyline(PointList) */
	public void drawPolyline(PointList points) {
		Assert.isNotNull(points);
		graphics.drawPolyline(mapPointList(points.toIntArray()));
	}

	/** @see Graphics#drawRectangle(int, int, int, int) */
	public void drawRectangle(int x, int y, int w, int h) {
		graphics.drawRectangle(mapRect(x, y, w, h));
	}

	/** @see Graphics#fillRectangle(int, int, int, int) */
	public void fillRectangle(int x, int y, int w, int h) {
		graphics.fillRectangle(mapFillRect(x, y, w, h));
	}

	/** @see Graphics#drawRoundRectangle(Rectangle, int, int) */
	public void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
		Assert.isNotNull(r);
		graphics.drawRoundRectangle(mapRect(r.x, r.y, r.width, r.height),
			(int)(arcWidth * mapModeScale),
			(int)(arcHeight * mapModeScale));
	}

	/** @see Graphics#fillRoundRectangle(Rectangle, int, int) */
	public void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
		Assert.isNotNull(r);
		graphics.fillRoundRectangle(mapFillRect(r.x, r.y, r.width, r.height),
			(int)(arcWidth * mapModeScale),
			(int)(arcHeight * mapModeScale));
	}

	/** @see Graphics#drawString(String, int, int) */
	public void drawString(String s, int x, int y) {
		Assert.isNotNull(s);
		graphics.drawString(s, mapPoint(x, y));
	}

	/** @see Graphics#fillString(String, int, int) */
	public void fillString(String s, int x, int y) {
		Assert.isNotNull(s);
		graphics.fillString(s, mapPoint(x, y));
	}

	/** @see Graphics#drawText(String, int, int) */
	public void drawText(String s, int x, int y) {
		Assert.isNotNull(s);
		graphics.drawText(s, mapPoint(x, y));
	}

	/** @see Graphics#fillText(String, int, int) */
	public void fillText(String s, int x, int y) {
		Assert.isNotNull(s);
		graphics.fillText(s, mapPoint(x, y));
	}

	/** @see Graphics#getBackgroundColor() */
	public Color getBackgroundColor() {
		return graphics.getBackgroundColor();
	}

	/** @see Graphics#getClip(Rectangle) */
	public Rectangle getClip(Rectangle rect) {
		graphics.getClip(rect);
		int x = (int)(rect.x / mapModeScale);
		int y = (int)(rect.y / mapModeScale);
		/*
		 * If the clip rectangle is queried, perform an inverse zoom, and take the ceiling of
		 * the resulting double. This is necessary because forward scaling essentially performs
		 * a floor() function. Without this, figures will think that they don't need to paint
		 * when actually they do.
		 */
		rect.width = (int)Math.ceil(rect.right() / mapModeScale) - x;
		rect.height = (int)Math.ceil(rect.bottom() / mapModeScale) - y;
		rect.x = x;
		rect.y = y;
		return rect;
	}

	/** @see Graphics#getFont() */
	public Font getFont() {
		return graphics.getFont();
	}

	/** @see Graphics#getFontMetrics() */
	public FontMetrics getFontMetrics() {
		return graphics.getFontMetrics();
	}

	/** @see Graphics#getForegroundColor() */
	public Color getForegroundColor() {
		return graphics.getForegroundColor();
	}

	/** @see Graphics#getLineStyle() */
	public int getLineStyle() {
		return graphics.getLineStyle();
	}

	/** @see Graphics#getLineWidth() */
	public int getLineWidth() {
		return graphics.getLineWidth();
	}

	/** @see Graphics#getXORMode() */
	public boolean getXORMode() {
		return graphics.getXORMode();
	}

	/** @see Graphics#popState() */
	public void popState() {
		graphics.popState();
		stackPointer--;
		restoreLocalState((State)stack.get(stackPointer));
	}

	/** @see Graphics#pushState() */
	public void pushState() {
		State s;
		if (stack.size() > stackPointer) {
			s = (State)stack.get(stackPointer);
			s.setValues(fractionalX, fractionalY);
		} else {
			stack.add(new State(fractionalX, fractionalY));
		}
		stackPointer++;

		graphics.pushState();
	}

	/** @see Graphics#restoreState() */
	public void restoreState() {
		graphics.restoreState();
		restoreLocalState((State)stack.get(stackPointer - 1));
	}

	private void restoreLocalState(State state) {
		this.fractionalX = state.appliedX;
		this.fractionalY = state.appliedY;
	}

	/** @see Graphics#scale(double) */
	public void scale(double amount) {
		graphics.scale(amount);
	}

	/** @see Graphics#setBackgroundColor(Color) */
	public void setBackgroundColor(Color rgb) {
		graphics.setBackgroundColor(rgb);
	}

	/** @see Graphics#setClip(Rectangle) */
	public void setClip(Rectangle r) {
		graphics.setClip(mapClipRect(r));
	}

	/** @see Graphics#setFont(Font) */
	public void setFont(Font f) {
		graphics.setFont(f);
	}

	/** @see Graphics#setForegroundColor(Color) */
	public void setForegroundColor(Color rgb) {
		graphics.setForegroundColor(rgb);
	}

	/** @see Graphics#setLineStyle(int) */
	public void setLineStyle(int style) {
		graphics.setLineStyle(style);
	}

	/** @see Graphics#setLineWidth(int) */
	public void setLineWidth(int width) {
		graphics.setLineWidth(width);
	}

	/** @see Graphics#setXORMode(boolean) */
	public void setXORMode(boolean b) {
		graphics.setXORMode(b);
	}

	/** @see Graphics#translate(int, int) */
	public void translate(int dx, int dy) {
		// fractionalX/Y is the fractional part left over from previous 
		// translates that gets lost in the integer approximation.
		double dxFloat = dx * mapModeScale + fractionalX;
		double dyFloat = dy * mapModeScale + fractionalY;
		fractionalX = dxFloat - Math.floor(dxFloat);
		fractionalY = dyFloat - Math.floor(dyFloat);
		graphics.translate((int)Math.floor(dxFloat), (int)Math.floor(dyFloat));
	}

	
	
	private Point mapPoint(int x, int y) {
		return new Point(((int)(Math.floor((x * mapModeScale) + fractionalX))),
						  (int)(Math.floor((y * mapModeScale) + fractionalY)));
	}

	private int[] mapPointList(int[] points) {

		if( points == null || points.length < 2 )
			return points;
		
		int[] mapped = new int[points.length];

		// Scale the points
		for (int i = 0; (i + 1) < points.length; i+= 2) {
			mapped[i] = (int)(Math.floor((points[i] * mapModeScale + fractionalX)));
			mapped[i + 1] = (int)(Math.floor((points[i + 1] * mapModeScale + fractionalY)));
		}
		return mapped;
	}

	private Rectangle mapFillRect(int x, int y, int w, int h) {
		TEMP.x = (int)(Math.floor((x * mapModeScale + fractionalX)));
		TEMP.y = (int)(Math.floor((y * mapModeScale + fractionalY)));
		TEMP.width = (int)(Math.floor(((x + w - 1) * mapModeScale + fractionalX))) - TEMP.x + 1;
		TEMP.height = (int)(Math.floor(((y + h - 1) * mapModeScale + fractionalY))) - TEMP.y + 1;
		return TEMP;
	}

	private Rectangle mapClipRect(Rectangle r) {
		TEMP.x = (int)(Math.floor(r.x * mapModeScale + fractionalX));
		TEMP.y = (int)(Math.floor(r.y * mapModeScale + fractionalY));
		TEMP.width = (int)(Math.ceil(((r.x + r.width) * mapModeScale + fractionalX))) - TEMP.x;
		TEMP.height = (int)(Math.ceil(((r.y + r.height) * mapModeScale + fractionalY))) - TEMP.y;
		return TEMP;
	}

	/**
	 * Maps the given rectangle through the map mode scaling factor and returns a new
	 * <code>Rectangle</code>.
	 * 
	 * @param x the top left x coordinate of the rectangle
	 * @param y the top left y coordinate of the rectangle
	 * @param w the width of the rectangle
	 * @param h the height of the rectangle
	 * @return a <code>Rectangle</code> that has been scaled by the map mode scaling factor.
	 */
	final protected Rectangle mapRect(int x, int y, int w, int h) {
		TEMP.x = (int)(Math.floor(x * mapModeScale + fractionalX));
		TEMP.y = (int)(Math.floor(y * mapModeScale + fractionalY));
		TEMP.width = (int)(Math.floor(((x + w) * mapModeScale + fractionalX))) - TEMP.x;
		TEMP.height = (int)(Math.floor(((y + h) * mapModeScale + fractionalY))) - TEMP.y;
		return TEMP;
	}
	
	/**
	 * @return Returns the graphics.
	 */
	final protected Graphics getGraphics() {
		return graphics;
	}
	/**
	 * @return Returns the mapModeScale.
	 */
	final protected double getMapModeScale() {
		return mapModeScale;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.Graphics#setLineDash(int[])
	 */
	public void setLineDash(int[] dash) {
		getGraphics().setLineDash(dash);
	}

}
