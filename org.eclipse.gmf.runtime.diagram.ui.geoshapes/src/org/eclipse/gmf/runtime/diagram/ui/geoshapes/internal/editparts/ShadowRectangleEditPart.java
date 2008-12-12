/******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeFigure;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeRectangleFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.RectangularDropShadowLineBorder;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Controls the interactions between the figure and
 * its underlying view. 
 *
 * @author jschofie
 */
public class ShadowRectangleEditPart extends GeoShapeEditPart {

	/**
	 * Constructor - Create an EditPart for a given model object (View)
	 * 
	 * @param shapeView model object that represents the associated figure
	 */	
	public ShadowRectangleEditPart(View shapeView) {
				
		super(shapeView);
	}
				
	protected NodeFigure createNodeFigure() {
		NodeFigure f = new GeoShapeRectangleFigure( getMapMode().DPtoLP(100), getMapMode().DPtoLP(50), getMapMode().DPtoLP(5) );
		f.setBorder( new RectangularDropShadowLineBorder() );		
		return f;
	}

	public IFigure getContentPane() {
		return ((GeoShapeFigure) getFigure()).getContentPane();
	}
	
	/**
	 * Sets the line width to the border as well.
	 * @see org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.GeoShapeEditPart#setLineWidth(int)
	 */
	protected void setLineWidth(int width) {
		super.setLineWidth(width);
		((RectangularDropShadowLineBorder)getFigure().getBorder()).setWidth(getMapMode().DPtoLP(width));
		getFigure().revalidate();
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.GeoShapeEditPart#setLineType(int)
	 */
	protected void setLineType(int lineType) {
		super.setLineType(lineType);
		((RectangularDropShadowLineBorder)getFigure().getBorder()).setStyle(lineType);
	}	

}
