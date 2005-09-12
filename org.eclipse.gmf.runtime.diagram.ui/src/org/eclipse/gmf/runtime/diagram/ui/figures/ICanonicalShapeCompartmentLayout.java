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

import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Layout figures who's position is undefined.  This interface guarantees
 * implementations access to the [figure,editpart] registry map.
 * 
 * @see org.eclipse.gmf.runtime.diagram.ui.figures.CanonicalShapeCompartmentLayout
 * @author mhanner
 */
public interface ICanonicalShapeCompartmentLayout extends LayoutManager
{
	/** Constant that represetns an <i>undefined</i> element. */
	public static final Rectangle UNDEFINED = LayoutHelper.UNDEFINED;
	
	/**
	 * Set the [figure, editpart] map.
	 * @param map to set
	 * @see org.eclipse.gef.EditPartViewer#getVisualPartMap() 
	 */
	void setVisualPartMap( Map map );
	
	/** 
	 * Return the [figure, editpart] map.
	 * @see org.eclipse.gef.EditPartViewer#getVisualPartMap()
	 * @return <code>Map</code> 
	 */
	Map getVisualPartMap();

	/**
	 * Layout the supplied parent's children whose position is equal to {@link #UNDEFINED}.
	 * @param parent the containing figure.
	 */
	void layoutUndefinedChildren( IFigure parent );
	
}
