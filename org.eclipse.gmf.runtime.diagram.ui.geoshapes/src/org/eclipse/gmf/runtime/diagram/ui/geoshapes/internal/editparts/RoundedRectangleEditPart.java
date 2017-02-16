/******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts;

import java.util.Iterator;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeFigure;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeRoundedRectangleFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.RoundedRectangleBorder;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RoundedCornersStyle;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author jschofie, lgrahek
 *
 * Controls the interactions between the figure and
 * its underlying view 
 */
public class RoundedRectangleEditPart extends GeoShapeEditPart {

	/**
	 * Constructor - Create an EditPart for a given model object (View)
	 * 
	 * @param shapeView model object that represents the associated figure
	 */	
	public RoundedRectangleEditPart(View shapeView) {
				
		super(shapeView);
	}
				
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.GeoShapeEditPart#createNodeFigure()
	 */
	protected NodeFigure createNodeFigure() {
		NodeFigure nodeFigure = new GeoShapeRoundedRectangleFigure(getMapMode()
				.DPtoLP(100), getMapMode().DPtoLP(50), getMapMode().DPtoLP(5));
		RoundedRectangleBorder border = new RoundedRectangleBorder(getMapMode()
				.DPtoLP(8), getMapMode().DPtoLP(8));
		border.setWidth(getMapMode().DPtoLP(getLineWidth()));
		border.setStyle(getLineType());
		nodeFigure.setBorder(border);	
		return nodeFigure;
	}	
		
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.GeoShapeEditPart#handleNotificationEvent(org.eclipse.emf.common.notify.Notification)
	 * @since 1.4
	 */
	@Override
	protected void handleNotificationEvent(Notification notification) {
		Object feature = notification.getFeature();
		if (NotationPackage.Literals.ROUNDED_CORNERS_STYLE__ROUNDED_BENDPOINTS_RADIUS == feature) {
            refreshRoundedCorners();
		}
		super.handleNotificationEvent(notification);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.GeoShapeEditPart#refreshVisuals()
	 * @since 1.4
	 */
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshRoundedCorners();
	}

	/**
	 * Refreshes radius for rounded corners (radius smaller than 1 indicates
	 * that corners should not be rounded).
	 * 
	 * @since 1.4
	 */
    protected void refreshRoundedCorners() {
		RoundedCornersStyle style = (RoundedCornersStyle) ((View) getModel())
				.getStyle(NotationPackage.Literals.ROUNDED_CORNERS_STYLE);
		if (style != null && (getNodeFigure().getBorder() instanceof RoundedRectangleBorder)) {
			RoundedRectangleBorder border = (RoundedRectangleBorder)getNodeFigure().getBorder();
			int radius = getMapMode().DPtoLP(style.getRoundedBendpointsRadius());
			// user enters radius, while diameter is expected when drawing rounded rectangle, so multiply by 2
			border.setArcWidth(radius*2);
			border.setArcHeight(radius*2);
			getFigure().repaint();
		}
		refreshConnections();
    }	
    
	/**
	 * Refreshes all connectors. This is needed when the border changes (corners
	 * become more or less rounded)
	 * 
	 * @since 1.4
	 */
    protected void refreshConnections() {
    	Iterator<?> conns = getTargetConnections().iterator();
    	while (conns.hasNext()) {
    		((Connection)((ConnectionEditPart)conns.next()).getFigure()).revalidate();
    	}
    	conns = getSourceConnections().iterator();
    	while (conns.hasNext()) {
    		((Connection)((ConnectionEditPart)conns.next()).getFigure()).revalidate();
    	}    	
    }
    
	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#setLineWidth(int)
	 * @since 1.4
	 */
	protected void setLineWidth(int width) {
		if (width < 0) {
			width = 1;
		}
		super.setLineWidth(width);
		LineBorder lineBorder = (LineBorder)getNodeFigure().getBorder();
		lineBorder.setWidth(getMapMode().DPtoLP(width));
		getFigure().revalidate();
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#setLineType(int)
	 * @since 1.4
	 */
	protected void setLineType(int lineType) {
		super.setLineType(lineType);
		LineBorder lineBorder = (LineBorder)getNodeFigure().getBorder();
		lineBorder.setStyle(lineType);
	}    

	public IFigure getContentPane() {
		return ((GeoShapeFigure) getFigure()).getContentPane();
	}

	@Override
	public boolean supportsRoundedCorners() {
		return true;
	}
}
