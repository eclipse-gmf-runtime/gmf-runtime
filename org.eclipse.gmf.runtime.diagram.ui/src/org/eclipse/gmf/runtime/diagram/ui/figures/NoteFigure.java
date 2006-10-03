/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
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
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
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
public class NoteFigure extends DefaultSizeNodeFigure {

	private boolean diagrsamLinkMode = false;;


	private boolean withDanglingCorner = true;
	private int lineWidth = 1;  

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
	 * Constructor
	 * 
	 * @param width <code>int</code> value that is the default width in logical units
	 * @param height <code>int</code> value that is the default height in logical units
	 * @param insets <code>Insets</code> that is the empty margin inside the note figure in logical units
	 */
	public NoteFigure(int width, int height, Insets insets) {
		super(width, height);
		setBorder(
			new MarginBorder(insets.top, insets.left, insets.bottom, insets.right));

		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
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

	protected void paintBorder(Graphics g) {
		if (!isDiagramLinkMode()) {
			Rectangle r = getBounds();
			
			PointList p = getPointList(r);
			g.setLineWidth(lineWidth);  
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
		return super.getPreferredSize(wHint, hHint).getUnioned(new Dimension(
								MapModeUtil.getMapMode(this).DPtoLP(100), 
								MapModeUtil.getMapMode(this).DPtoLP(50)));
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
