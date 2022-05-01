/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeDiamondFigure;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author jschofie
 *
 * Controls the interactions between the figure and
 * its underlying view 
 */
public class DiamondEditPart extends GeoShapeEditPart {

	/**
	 * Constructor - Create an EditPart for a given model object (View)
	 * 
	 * @param shapeView model object that represents the associated figure
	 */
	public DiamondEditPart(View shapeView) {
		
		super(shapeView);
	}
		
	protected NodeFigure createNodeFigure() {
		return new GeoShapeDiamondFigure( getMapMode().DPtoLP(50), getMapMode().DPtoLP(50), getMapMode().DPtoLP(5) );
	}

	public IFigure getContentPane() {
		return ((GeoShapeFigure) getFigure()).getContentPane();
	}

}
