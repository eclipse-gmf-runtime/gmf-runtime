package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;


/**
 * Grid Layer extension that provides grid color, origin and line style
 * settings to the GridLayer
 * 
 * @author tmacdoug
 *
 */
public class GridLayerEx
	extends GridLayer {

	// Grid line style (for example dotted, dashed, solid, etc.)
	private int lineStyle = SWT.LINE_CUSTOM;
	private int[] dashes = new int[]{1,5};
	
	public GridLayerEx() {
		super();
	}
	
	/**
	 * Constructs grid layer with the specified color
	 * @param color
	 */
	public GridLayerEx(Color color) {
		super();
		setForegroundColor(color);
	}

	/**
	 * Constructs grid layer with the specified color and origin
	 * @param color
	 * @param p
	 */
	public GridLayerEx(Color color, Point p) {
		super();
		setForegroundColor(color);
		setOrigin(p);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.GridLayer#paintGrid(org.eclipse.draw2d.Graphics)
	 */
	protected void paintGrid(Graphics g) {
		g.setLineStyle(lineStyle);
		if ((dashes != null) && (lineStyle == SWT.LINE_CUSTOM)) g.setLineDash(dashes);		
		FigureUtilities.paintGrid(g, this, origin, gridX, gridY);
	}

	
	public void setLineStyle(int lineStyle) {
		this.lineStyle = lineStyle;
	}	
	
	public void setLineDash(int[] dashSet) {
		this.dashes = dashSet;
	}	

	
}
