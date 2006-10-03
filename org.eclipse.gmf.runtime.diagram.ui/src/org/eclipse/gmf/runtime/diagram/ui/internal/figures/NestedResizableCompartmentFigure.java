/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;

/**
 * A figure to represent the nested resizable compartment figure. Extends 
 * ResizeableCompartmentFigure with the following differences
 * 1.  If the contents of the scroll pane is empty then the figure will not be visible
 * 2.  Provides a constructor that has the text pane text align to the left
 * 3.  Provides a constructor that doesn't have a title by default.
 * 
 * Used by List compartment figures that are nested within other resizable compartment 
 * @author choang
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class NestedResizableCompartmentFigure extends ResizableCompartmentFigure {
    
    /**
     * Constructors a ResizeableComparmtmentFigure that has the text align to the left
     * and the scrollpane 
     * @param mm the <code>IMapMode</code> that is used to initialize the
     * default values of of the scrollpane contained inside the figure.  This is
     * necessary since the figure is not attached at construction time and consequently
     * can't get access to the owned IMapMode in the parent containment hierarchy.
     */
    public NestedResizableCompartmentFigure(IMapMode mm) {
        super(null, mm);
        setBorder(null);
        ConstrainedToolbarLayout layout = (ConstrainedToolbarLayout)getLayoutManager();
        layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT); //diff cause we want to align our title to the left top
        getScrollPane().getContents().setBorder(null);
    }
	
	/*
	 * Zero dimension
	 */
	private static Dimension ZERO_DIM = new Dimension(0,0);
	
	/* 
	 * Override as the min dimension is 0,0 since this compartment doesn't want to 
	 * leave any spaces if there are not contains
	 * @see org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure#getMinClientDimension()
	 */
	public Dimension getMinClientDimension() {		
		return ZERO_DIM;
	}

}
