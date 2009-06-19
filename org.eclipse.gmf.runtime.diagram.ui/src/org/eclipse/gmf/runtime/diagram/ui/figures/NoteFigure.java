/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;

/*
 * @canBeSeenBy %partners
 */
/**
 * a figure represents the Note view, this figure had a mode called
 * DiagramLinkMode, if this mode is turned ON then the text alignment
 * will be center instead of left and the border will be rectangle
 * instead of the note border shape, also it will not paint the border
 * @see #setDiagramLinkMode(boolean)
 * @author sshaw, mmostafa
 *
 */
public class NoteFigure extends DefaultSizeNodeFigure implements IPolygonAnchorableFigure {

	private boolean diagramLinkMode = false;


	private boolean withDanglingCorner = true;

	/**
	 * the clip height constant in device coordinates
	 */
	static public final int CLIP_HEIGHT_DP = 12;
	
	/**
	 * the margin constant in device coordinates
	 */
	static public final int MARGIN_DP = 5;
	
	/**
	 * the clip margin constant in device coordinates
	 */
	static public final int CLIP_MARGIN_DP = 14;
	
	/**
	 * Border for notes. Defines paint method and insets that depend on line width and
	 * given margin.
	 * @since 1.2
	 */
	public class NoteFigureBorder extends AbstractBorder {
		private Insets margin;
		NoteFigureBorder(Insets insets) {
			margin = insets;
		}	
		
		/**
		 * Returns margin for this border
		 * @return margin as Insets
		 */
		public Insets getMargin() {
			return margin;
		}

		/**
		 * Sets the margin for this border 
		 * @param margin as Insets
		 */
		public void setMargin(Insets margin) {
			this.margin = margin;
		}


		/*
		 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
		 */
		public Insets getInsets(IFigure figure) {
			NoteFigure noteFigure = (NoteFigure)figure;
			int width = noteFigure.getLineWidth();
			return new Insets(width + margin.top, width + margin.left, 
					width + margin.bottom, width + margin.right);
		}


		/* 
		 * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure, org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
		 */
		public void paint(IFigure figure, Graphics g, Insets insets) {
			NoteFigure noteFigure = (NoteFigure)figure;
			Rectangle r = noteFigure.getBounds().getCopy();
			r.shrink(noteFigure.getLineWidth() / 2, noteFigure.getLineWidth() / 2);
			
			PointList p = noteFigure.getPointList(r);
			p.addPoint(r.x, r.y - noteFigure.getLineWidth() / 2);
			g.setLineWidth(noteFigure.getLineWidth());  
			g.setLineStyle(noteFigure.getLineStyle());  
			g.drawPolyline(p);
	
			if (withDanglingCorner) {
				PointList corner = new PointList();
				corner.addPoint(r.x + r.width - getClipWidth(), r.y);
				corner.addPoint(r.x + r.width - getClipWidth(), r.y + getClipHeight());
				corner.addPoint(r.x + r.width, r.y + getClipHeight());
				g.drawPolyline(corner);			
			}			
		}
	}
		
	/**
	 * Constructor
	 * 
	 * @param width <code>int</code> value that is the default width in logical units
	 * @param height <code>int</code> value that is the default height in logical units
	 * @param insets <code>Insets</code> that is the empty margin inside the note figure in logical units
	 */
	public NoteFigure(int width, int height, Insets insets) {
		super(width, height);
		// NoteFigureBorder defines insets which ensure that content within the note will be indented 
		// appropriately as the line width changes
		setBorder(new NoteFigureBorder(insets)); 

		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout() {
			// Override to ensure that children's size is not taken into account
			// when calculating minimum size (otherwise, if WrappingLabel is a
			// child, the smallest rectangle we could get would be big enough to
			// fit the label of minimum size = three dots and icon if there is
			// one). 
			public Dimension calculateMinimumSize(IFigure container, int wHint,
					int hHint) {
				Insets insets = container.getInsets();
				Dimension minSize = new Dimension(0, 0);
				return transposer
					.t(minSize)
					.expand(insets.getWidth(), insets.getHeight())
					.union(getBorderPreferredSize(container));
			}			
		};
		layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT);
		layout.setSpacing(insets.top);
		setLayoutManager(layout);
	}
	
	private int getClipHeight() {
		return MapModeUtil.getMapMode(this).DPtoLP(12);
	}
	
	private int getClipWidth() {
		return getClipHeight() + MapModeUtil.getMapMode(this).DPtoLP(1);
	}
	
	/**
	 * Method getPointList.
	 * @param r
	 * @return PointList
	 */
	protected PointList getPointList(Rectangle r) {

		PointList p = new PointList();
		
		p.addPoint(r.x, r.y);
		if (!isDiagramLinkMode()){
			p.addPoint(r.x + r.width - getClipWidth(), r.y);
			p.addPoint(r.x + r.width - 1, r.y + getClipHeight());
		}else{
			p.addPoint(r.x + r.width - 1, r.y) ;
		}
		p.addPoint(r.x + r.width - 1, r.y + r.height - 1);
		p.addPoint(r.x, r.y + r.height - 1);
		p.addPoint(r.x, r.y);

		return p;
	}

	/**
	 * Paints border unless this Note is in DiagramLinkMode  
	 * 
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	protected void paintBorder(Graphics g) {
		if (!isDiagramLinkMode()) {
			getBorder().paint(this, g, NO_INSETS);
		}		 
	}


	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle r = getBounds();
		PointList p = getPointList(r);
		g.fillPolygon(p);			
	}

	
	/**
	 * sets or resets the diagram link mode, in diagram link mode the note
	 * will not paint a border or background for itself
	 * @param diagramLinkMode , the new diagram link mode state
	 * @return the old diagram Link mode state
	 */
	public boolean setDiagramLinkMode(boolean diagramLinkMode) {
		boolean bOldDiagramLinkMode = this.diagramLinkMode;
		ConstrainedToolbarLayout layout = (ConstrainedToolbarLayout)getLayoutManager();
		if (diagramLinkMode){
			layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_CENTER);
		}else {
			layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT);
		}
		this.diagramLinkMode = diagramLinkMode;
		return bOldDiagramLinkMode;
	}
	
	/**
	 * @return true is in diagram Link mode, otherwise false
	 */
	public boolean isDiagramLinkMode() {
		return diagramLinkMode;
	}

   /*
    * (non-Javadoc)
    * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure#getPolygonPoints()
    */ 
    public PointList getPolygonPoints() {
        return getPointList(getBounds());
    }

}
