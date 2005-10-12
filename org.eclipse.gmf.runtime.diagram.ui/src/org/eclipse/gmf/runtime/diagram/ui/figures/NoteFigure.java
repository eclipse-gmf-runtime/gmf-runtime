/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
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
public class NoteFigure extends DefaultSizeNodeFigure {

	/**
	 * ther clip height constant
	 */
	static public final int CLIP_HEIGHT = MapMode.DPtoLP(12);
	
	/**
	 * the margin constant 
	 */
	static public final int MARGIN = MapMode.DPtoLP(5);
	
	/**
	 * the clip margin constant
	 */
	static public final int CLIP_MARGIN = MapMode.DPtoLP(14);
	static private final int clipHeight = CLIP_HEIGHT;
	static private final int clipWidth = CLIP_HEIGHT + MapMode.DPtoLP(1);
	private static final int DEFAULT_NOTE_WIDTH  = MapMode.DPtoLP(100);
	private static final int DEFAULT_NOTE_HEIGHT = MapMode.DPtoLP(56);
	
	private boolean diagrsamLinkMode = false;;


	private boolean withDanglingCorner = true;
	private int lineWidth = 1;  

	/**
	 * constructor
	 */
	public NoteFigure() {
		setDefaultSize(new Dimension(DEFAULT_NOTE_WIDTH, DEFAULT_NOTE_HEIGHT));
		setBorder(
			new MarginBorder(
				NoteFigure.MARGIN,
				NoteFigure.MARGIN,
				NoteFigure.MARGIN,
				NoteFigure.CLIP_MARGIN));

		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT);
		layout.setSpacing(MapMode.DPtoLP(5));
		setLayoutManager(layout);
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
			p.addPoint(r.x + r.width - clipWidth, r.y);
			p.addPoint(r.x + r.width - 1, r.y + clipHeight);
		}else{
			p.addPoint(r.x + r.width - 1, r.y) ;
		}
		p.addPoint(r.x + r.width - 1, r.y + r.height - 1);
		p.addPoint(r.x, r.y + r.height - 1);
		p.addPoint(r.x, r.y);

		return p;
	}

	protected void paintBorder(Graphics g) {
		if (!isDiagramLinkMode()) {
			Rectangle r = getBounds();
			
			PointList p = getPointList(r);
			g.setLineWidth(lineWidth);  
			g.drawPolyline(p);
	
			if (withDanglingCorner) {
				PointList corner = new PointList();
				corner.addPoint(r.x + r.width - clipWidth, r.y);
				corner.addPoint(r.x + r.width - clipWidth, r.y + clipHeight);
				corner.addPoint(r.x + r.width, r.y + clipHeight);
				g.drawPolyline(corner);			
			}
		}		 
	}


	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle r = getBounds();
		PointList p = getPointList(r);
		g.fillPolygon(p);			
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return super.getPreferredSize(wHint, hHint).getUnioned(new Dimension(2645, 1322));
	}
	
	/**
	 * sets or resets the diagram link mode, in diagram link mode the note
	 * will not paint a border or background for itself
	 * @param diagramLinkMode , the new diagram link mode state
	 * @return the old diagram Link mode state
	 */
	public boolean setDiagramLinkMode(boolean diagramLinkMode) {
		boolean bOldDiagramLinkMode = this.diagrsamLinkMode;
		ConstrainedToolbarLayout layout = (ConstrainedToolbarLayout)getLayoutManager();
		if (diagramLinkMode){
			layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_CENTER);
		}else {
			layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT);
		}
		this.diagrsamLinkMode = diagramLinkMode;
		return bOldDiagramLinkMode;
	}
	
	/**
	 * @return true is in diagram Link mode, otherwise false
	 */
	public boolean isDiagramLinkMode() {
		return diagrsamLinkMode;
	}

}
