/******************************************************************************
 * Copyright (c) 2003, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeFigure;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeRectangleFigure;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeLineStyleBorder;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
	
/**
 * Controls the interactions between the figure and
 * its underlying view 
 *
 * @author jschofie
 */
public class RectangleEditPart extends GeoShapeEditPart {

	/**
	 * Constructor - Create an EditPart for a given model object (View)
	 * 
	 * @param shapeView model object that represents the associated figure
	 */	
	public RectangleEditPart(View shapeView) {
			
		super(shapeView);
	}
			
	protected NodeFigure createNodeFigure() {
		NodeFigure nodeFigure = new GeoShapeRectangleFigure( getMapMode().DPtoLP(50), getMapMode().DPtoLP(50), getMapMode().DPtoLP(5) );
		GeoShapeLineStyleBorder lineBorder = new GeoShapeLineStyleBorder();
		lineBorder.setWidth(getMapMode().DPtoLP(getLineWidth()));
		lineBorder.setStyle(getLineType());
		nodeFigure.setBorder(lineBorder);
        return nodeFigure;
    }

	public IFigure getContentPane() {
		return ((GeoShapeFigure) getFigure()).getContentPane();
	}
	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#setLineWidth(int)
	 */
	protected void setLineWidth(int width) {
		if (width < 0) {
			width = 1;
		}
		super.setLineWidth(width);
		GeoShapeLineStyleBorder lineBorder = (GeoShapeLineStyleBorder)((NodeFigure)getFigure()).getBorder();
		lineBorder.setWidth(getMapMode().DPtoLP(width));
		getFigure().revalidate();
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#setLineType(int)
	 */
	protected void setLineType(int lineType) {
		super.setLineType(lineType);
		GeoShapeLineStyleBorder lineBorder = (GeoShapeLineStyleBorder)((NodeFigure)getFigure()).getBorder();
		lineBorder.setStyle(lineType);
	}

}
