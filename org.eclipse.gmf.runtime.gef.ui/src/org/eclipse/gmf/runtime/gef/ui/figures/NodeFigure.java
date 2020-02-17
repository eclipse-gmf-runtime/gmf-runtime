/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.figures;

import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gmf.runtime.draw2d.ui.figures.BaseSlidableAnchor;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.graphics.ColorRegistry;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.TransparentBorder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * Base class that most shape figures should extend from to gain default connection anchor behavior.
 * 
 * @author melaasar
 */
public class NodeFigure 
	extends Figure
		implements HandleBounds, IAnchorableFigure, IPolygonAnchorableFigure {

	private Hashtable connectionAnchors;
	
	/** 
	 * The width of this shape's outline. (a field from GEF Shape). Must be
	 * greater than zero. 
	 * TODO: NodeFigure should have extended org.eclipse.draw2d.Shape
	 */
	private int lineWidth = 1;
	
	/**
	 * The line style to be used for this shape's outline. 
	 * TODO: NodeFigure should have extended org.eclipse.draw2d.Shape
	 */
	private int lineStyle = Graphics.LINE_SOLID;
	
	private boolean isUsingGradient = false;
	
	private int gradientColor1 = -1;
	
	private int gradientColor2 = -1;
	
	private int gradientStyle = 0;
		
	/**
	 * The transparency of this shape in percent.
	 * Must be in [0, 100] range.
	 */
	private int transparency = 0;
	
	/**
	 * <code>String</code> that is the identifier for the default anchor
	 */
	static public final String szAnchor = ""; //$NON-NLS-1$

	/** 
	 * Constructor - sets the default colors for all node figures.
	 */
	public NodeFigure() {
		// empty constructor
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		Insets insets = new Insets(0, 0, 0, 0);
		if (getBorder() instanceof TransparentBorder) {
			insets =
				((TransparentBorder) getBorder()).getTransparentInsets(this);
		}
        
		// Ignore the insets when placing the handles
		return new Rectangle(
				getBounds().x + insets.left,
				getBounds().y + insets.top,
				getBounds().width - (insets.right + insets.left),
				getBounds().height - (insets.bottom + insets.top));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure#getConnectionAnchor(java.lang.String)
	 */
	public ConnectionAnchor getConnectionAnchor(String terminal) {

		ConnectionAnchor connectAnchor =
			(ConnectionAnchor) getConnectionAnchors().get(terminal);
		if (connectAnchor == null) {
			if (terminal.equals(szAnchor)) {
				// get a new one - this figure doesn't support static anchors
				connectAnchor = createDefaultAnchor();
				getConnectionAnchors().put(terminal,connectAnchor);
			}
			else {
				connectAnchor = createAnchor(SlidableAnchor.parseTerminalString(terminal));
			}
		}

		return connectAnchor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure#getConnectionAnchorTerminal(org.eclipse.draw2d.ConnectionAnchor)
	 */
	public String getConnectionAnchorTerminal(ConnectionAnchor c) {
		if (c instanceof SlidableAnchor) {
			return ((SlidableAnchor) c).getTerminal();
		}
		if (getConnectionAnchors().containsValue(c)) {
			Iterator iter = getConnectionAnchors().keySet().iterator();
			String key;
			while (iter.hasNext()) {
				key = (String) iter.next();
				if (getConnectionAnchors().get(key).equals(c))
					return key;
			}
		}
		getConnectionAnchor(szAnchor);
		return szAnchor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure#getSourceConnectionAnchorAt(org.eclipse.draw2d.geometry.Point)
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		return createConnectionAnchor(p);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure#getTargetConnectionAnchorAt(org.eclipse.draw2d.geometry.Point)
	 */
	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		return createConnectionAnchor(p);
	}

	/**
	 * Returns a new anchor for this node figure.
	 * 
	 * @param p <code>Point</code> on the figure that gives a hint which anchor to return.
	 * @return <code>ConnectionAnchor</code> reference to an anchor associated with the 
	 * given point on the figure.
	 */
	protected ConnectionAnchor createConnectionAnchor(Point p) {
		if (p == null) {
			return getConnectionAnchor(szAnchor);
		}
		else {
			Point temp = p.getCopy();
			translateToRelative(temp);
			PrecisionPoint pt = BaseSlidableAnchor.getAnchorRelativeLocation(temp, getBounds());
			if (isDefaultAnchorArea(pt))
				return getConnectionAnchor(szAnchor);
			return createAnchor(pt);
		}
	} 
	
	/**
	 * Checks whether the <PrecisionPoint> p which is a candidate for a relative reference
	 * for the <Code>SlidableAnchor</Code> belongs to the area where the default anchor
	 * must be created
	 * 
	 * @param p
	 * @return <code>boolean</code> <code>true</code> if <PrecisionPoint> belongs to the area where the default anchor must be 
	 * created, <code>false</code> otherwise
	 */
	protected boolean isDefaultAnchorArea(PrecisionPoint p) {
		return p.preciseX >= getSlidableAnchorArea()/2 && p.preciseX <= 1 - getSlidableAnchorArea()/2 &&
			p.preciseY >= getSlidableAnchorArea()/2 && p.preciseY <= 1 - getSlidableAnchorArea()/2;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		if (isOpaque() && getBorder() != null) {
			Rectangle tempRect = new Rectangle(getBounds());
			tempRect.crop(getBorder().getInsets(this));
			graphics.fillRectangle(tempRect);
			return;
		}

		super.paintFigure(graphics);
	}
	

	/**
	 * Returns the connectionAnchors.
	 * @return Hashtable
	 */
	protected Hashtable getConnectionAnchors() {
		if (connectionAnchors == null)
			connectionAnchors = new Hashtable(1);
		return connectionAnchors;
	}
		

	/**
	 * Specifies how large the area of the figure's bounds where <Code>SlidableAnchor</Code>
	 * will be created. The result number: 0<=result<=1
	 * 
	 * @return  the size of the area of the figure's bounds
	 */
	protected double getSlidableAnchorArea() {
		return 0.5;
	}
	
	/**
	 * Creates the default Slidable anchor with a reference point at the center
	 * of the figure's bounds
	 * 
	 * @return - default SlidableAnchor, relative reference the center of the figure
	 */
	protected ConnectionAnchor createDefaultAnchor() {
		return new SlidableAnchor(this);
	}
	
	/**
	 * Creates a slidable anchor at the specified point (from the ratio of the
	 * reference's coordinates and bounds of the figure
	 * 
	 * @param p - relative reference for the <Code>SlidableAnchor</Code>
	 * @return a <code>SlidableAnchor</code> for this figure with relative reference at p
	 */
	protected ConnectionAnchor createAnchor(PrecisionPoint p) {
		if (p==null)
			// If the old terminal for the connection anchor cannot be resolved (by SlidableAnchor) a null
			// PrecisionPoint will passed in - this is handled here
			return createDefaultAnchor();
		return new SlidableAnchor(this, p);
	}

	/**
	 * @return <code>String</code> that is the identifier for the default anchor
	 */
	public static String getDefaultAnchorID() {
		return szAnchor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure#getPolygonPoints()
	 */
	public PointList getPolygonPoints() {
		PointList points = new PointList(5);
		Rectangle anchorableRectangle = getHandleBounds();
		points.addPoint(anchorableRectangle.x, anchorableRectangle.y);
		points.addPoint(anchorableRectangle.x + anchorableRectangle.width,
				anchorableRectangle.y);
		points.addPoint(anchorableRectangle.x + anchorableRectangle.width,
				anchorableRectangle.y + anchorableRectangle.height);
		points.addPoint(anchorableRectangle.x, anchorableRectangle.y
				+ anchorableRectangle.height);
		points.addPoint(anchorableRectangle.x, anchorableRectangle.y);
		return points;
	}

	/**
	 * Returns the line style used to outline this shape.
	 * @return the line style
	 * @since 2.1
	 */
	public int getLineStyle() {
		return lineStyle;
	}

	/**
	 * Returns the line width of this shape's outline.
	 * @return the line width
	 * @since 2.1
	 */
	public int getLineWidth() {
		return lineWidth;
	}

	/**
	 * Sets the line width to be used to outline the shape.
	 *
	 * @param w the new width
	 * @since 2.1
	 */
	public void setLineWidth(int w) {
		if ((lineWidth == w) || (w < 0))
			return;
		lineWidth = w;
		repaint();
	}

	/**
	 * Sets the style of line to be used by this shape.
	 *
	 * @param s the new line style
	 * @since 2.1
	 */
	public void setLineStyle(int s) {
		if (lineStyle == s) 
			return;
		lineStyle = s;
		repaint();
	}
	
	/**
	 * @return the gradientColor1
	 * @since 1.2
	 */
	public int getGradientColor1() {
		return gradientColor1;
	}
	
	/**
	 * @return the gradientColor2
	 * @since 1.2
	 */
	public int getGradientColor2() {
		return gradientColor2;	
	}

	/**
	 * @return the gradientStyle 
	 * @since 1.2
	 */
	public int getGradientStyle() {
		return gradientStyle;
	}	

	/**
	 * Sets values defining gradient data.
	 * 
	 * @param gradientColor1
	 * @param gradientColor2
	 * @param gradientStyle
	 * @since 1.2
	 */
	public void setGradientData(int gradientColor1, int gradientColor2, int gradientStyle) {
		boolean doRepaint = false;
		// When in high contrast mode, there is no gradient, so set bg color to null to allow inheriting
		// the system color.
		Display display = Display.getCurrent();
		if (display == null && PlatformUI.isWorkbenchRunning()) {
			display = PlatformUI.getWorkbench().getDisplay();
		}
		if (display != null && display.getHighContrast()) {
			setBackgroundColor(null);
			return;
		}		
		if (gradientColor1 != this.gradientColor1 && gradientColor1 > -1) {
			this.gradientColor1 = gradientColor1;
			doRepaint = true;
		}
		if (gradientColor2 != this.gradientColor2 && gradientColor2 > -1) {
			this.gradientColor2 = gradientColor2;
			doRepaint = true;
		}
		if (gradientStyle != this.gradientStyle) {
			this.gradientStyle = gradientStyle;
			doRepaint = true;
		}
		if (doRepaint) {
			repaint();
		}
	}
	
	/**
	 * Utility method that indicates if gradient should be used as a fill style or not.
	 * 
	 * @return true if gradient should be used, false otherwise (fill color should be used)
	 * @since 1.2
	 */
	public boolean isUsingGradient() {
		// When in high contrast mode, there is no gradient, so return false.
		Display display = Display.getCurrent();
		if (display == null && PlatformUI.isWorkbenchRunning()) {
			display = PlatformUI.getWorkbench().getDisplay();
		}
		if (display != null && display.getHighContrast()) {		
			return false;
		}
		return isUsingGradient && gradientColor1 > -1 && gradientColor2 > -1;
	}
		
	/**
	 * Sets the value of isUsingGradient
	 * 
	 * @param b value for isUsingGradient
	 * @since 1.2
	 */
	public void setIsUsingGradient(boolean b) {
		if (b != isUsingGradient) {
			isUsingGradient = b;
			// this is needed, e.g. when undoing gradient clearing from Advanced tab;
			// in cases when repaint() is already called, it doesn't matter since
			// the actual paint happens only once
			repaint();
		}
	}	
	
	/**
	 * Fills given path by gradient using given fillMode
	 * 
	 * @param g The Graphics used to paint
	 * @param path Path of shape to be filled with gradient
	 * @param fillMode One of SWT.FILL_EVEN_ODD and SWT.FILL_WINDING
	 * @since 1.2
	 */
	protected void fillGradient(Graphics g, Path path, int fillMode) {
		if (path != null) {
			g.pushState();
			g.setForegroundColor(ColorRegistry.getInstance().getColor(Integer.valueOf(getGradientColor1())));
			g.setBackgroundColor(ColorRegistry.getInstance().getColor(Integer.valueOf(getGradientColor2())));
			if (fillMode == SWT.FILL_EVEN_ODD || fillMode == SWT.FILL_WINDING) {
				g.setFillRule(fillMode);
			}
			g.clipPath(path);
			g.fillGradient(getBounds(), getGradientStyle() == 0);	
			path.dispose();
			g.popState();
		}
	}
	
	/**
	 * Fills gradient using default mode SWT.FILL_EVEN_ODD and getPath() to
	 * obtain path to fill.
	 * 
	 * @param g The Graphics used to paint
	 * @since 1.2
	 */
	protected void fillGradient(Graphics g) {
		// use the default mode if one is not provided
		fillGradient(g, getPath(), SWT.FILL_EVEN_ODD);
	}

	/**
	 * Fills gradient using default mode SWT.FILL_EVEN_ODD. Use this method when
	 * getPath() doesn't return desired path.
	 * 
	 * @param g The Graphics used to paint
	 * @param path Path of shape to be filled with gradient
	 * @since 1.2
	 */
	protected void fillGradient(Graphics g, Path path) {
		// use the default mode if one is not provided
		fillGradient(g, path, SWT.FILL_EVEN_ODD);
	}

	/**
	 * Fills gradient using getPath() to obtain path to fill. Use this method
	 * when default fill mode SWT.FILL_EVEN_ODD is not appropriate.
	 * 
	 * @param g The Graphics used to paint
	 * @param fillMode One of SWT.FILL_EVEN_ODD and SWT.FILL_WINDING
	 * @since 1.2
	 */
	protected void fillGradient(Graphics g, int fillMode) {
		fillGradient(g, getPath(), fillMode);
	}
	
	/**
	 * This method creates and returns figure's path. Default implementation defines path
	 * based on figure's bounds and insets. Subclasses should override if
	 * needed.
	 * 
	 * @return Created path
	 * @since 1.2
	 */
	protected Path getPath() {
		if (!isOpaque()) {
			return null;
		}
		Path path = new Path(null);
		Rectangle tempRect = getClientArea();
		path.addRectangle(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
		return path;
	}

	
	/**
	 * Returns transparency value (belongs to [0, 100] interval)
	 * 
	 * @return transparency
	 * @since 1.2
	 */
	public int getTransparency() {
		return transparency;
	}

	/**
	 * Sets the transparency if the given parameter is in [0, 100] range
	 * 
	 * @param transparency The transparency to set
	 * @since 1.2
	 */
	public void setTransparency(int transparency) {
		if (transparency != this.transparency &&
				transparency >= 0 && transparency <= 100) {
			this.transparency = transparency;
			repaint();
		}
	}
			
	/**
	 * Converts transparency value from percent range [0, 100] to alpha range
	 * [0, 255] and applies converted value. 0% corresponds to alpha 255 and
	 * 100% corresponds to alpha 0.
	 * 
	 * @param g The Graphics used to paint
	 * @since 1.2
	 */
	protected void applyTransparency(Graphics g) {
		g.setAlpha(255 - transparency * 255 / 100);
	}

}
