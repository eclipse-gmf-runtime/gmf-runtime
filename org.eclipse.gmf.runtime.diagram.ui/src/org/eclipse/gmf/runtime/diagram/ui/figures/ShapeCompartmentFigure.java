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

import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimatableScrollPane;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.OverlayScrollPaneLayout;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * A specialized implementation of <code>ResizableCompartmentFigure</code>, this
 * class configures the scrollpane to use a {@link org.eclipse.draw2d.FreeformLayer}
 * with a {@link org.eclipse.draw2d.FreeformLayout} as its contents.  
 * 
 * @author mhanner
 */
public class ShapeCompartmentFigure extends ResizableCompartmentFigure {
	
	
	/**
	 * constant defines the font title 
	 */
	public static final Font FONT_TITLE = new Font(null, new FontData("Helvetica", 9, SWT.BOLD));//$NON-NLS-1$
	   
    /**
     * Create an instance.  Calls {@link #configureFigure()} to reconfigure
     * the scrollpane.
     * 
     * @param title figure's title.
     * @param mm the <code>IMapMode</code> that is used to initialize the
     * default values of of the scrollpane contained inside the figure.  This is
     * necessary since the figure is not attached at construction time and consequently
     * can't get access to the owned IMapMode in the parent containment hierarchy.
     */
    public ShapeCompartmentFigure(String title, IMapMode mm) {
        super(title, mm);
    }
    

    protected AnimatableScrollPane createScrollpane(IMapMode mm) {
        configureFigure(mm);
        return (AnimatableScrollPane)getScrollPane();
    }
	/**
	 * The scrollpane is configured to use a {@link FreeformLayer} with a
	 * {@link FreeformLayout} as its contents.
     * @deprecated use {@link ShapeCompartmentFigure#configureFigure(IMapMode)} instead
	 */
	protected void configureFigure() {
		configureFigure(MapModeUtil.getMapMode(this));        
	}
    
    /**
     * The scrollpane is configured to use a {@link FreeformLayer} with a
     * {@link FreeformLayout} as its contents.
     * 
     * @param mm the <code>IMapMode</code> that is used to initialize the
     * default values of of the scrollpane contained inside the figure.  This is
     * necessary since the figure is not attached at construction time and consequently
     * can't get access to the owned IMapMode in the parent containment hierarchy.
     */
    protected void configureFigure(IMapMode mm) {
        ScrollPane scrollpane = getScrollPane();
        if(scrollpane==null){
            scrollpane = scrollPane = new AnimatableScrollPane();
        }
        scrollpane.setViewport(new FreeformViewport());
        scrollPane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
        scrollPane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
        scrollpane.setLayoutManager(new OverlayScrollPaneLayout() );

        IFigure contents = new FreeformLayer();
        contents.setLayoutManager(new FreeformLayout());
        scrollpane.setContents(contents);
        
        int MB = mm.DPtoLP(5);
        scrollpane.setBorder(new MarginBorder(MB, MB, MB, MB));
        int SZ = mm.DPtoLP(10);
        scrollpane.setMinimumSize(new Dimension(SZ, SZ));
    
        this.setFont(FONT_TITLE);
    }    
   
  
	/**
	 * Convenience method to registers the supplied listener to the scrollpane's 
	 * vertical and horizonatl range models. 
	 * 
	 * @param listener The listener to add
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		getScrollPane().getViewport()
			.getVerticalRangeModel()
			.addPropertyChangeListener(
			listener);
		getScrollPane()
			.getViewport()
			.getHorizontalRangeModel()
			.addPropertyChangeListener(
			listener);
	}

	/**
	 * Convenience method to removes the supplied listener from the scrollpane's 
	 * RangeModel's list of PropertyChangeListeners.
	 * @param listener The listener to remove
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		getScrollPane()
			.getViewport()
			.getVerticalRangeModel()
			.removePropertyChangeListener(
			listener);
		getScrollPane()
			.getViewport()
			.getHorizontalRangeModel()
			.removePropertyChangeListener(listener);
	}
}