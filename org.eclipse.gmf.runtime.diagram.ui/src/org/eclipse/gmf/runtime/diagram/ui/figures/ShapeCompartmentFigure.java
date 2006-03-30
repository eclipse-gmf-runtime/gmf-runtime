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
import org.eclipse.draw2d.Orientable;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ListScrollBar;
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
	 * @deprecated use @link(ResizableCompartment(String compartmentTitle, int minClientSize)} instead.
	 * Clients must specify the minClientSize in their own logical coordinate system instead of the
	 * figure assuming a default.  @link{ ResizableCompartmentFigure.MIN_CLIENT_DP } is provided as a default
	 * value for convenience in device coordinates.
	 */
	public ShapeCompartmentFigure() {
		this(null, MapModeUtil.getMapMode().DPtoLP(ResizableCompartmentFigure.MIN_CLIENT_DP));
	}
	
	/**
	 * Create an instance.  Calls {@link #configureFigure()} to reconfigure
	 * the scrollpane.
	 * 
	 * @param title figure's title.
	 * @deprecated use @link(ResizableCompartment(String compartmentTitle, IMapMode mm)} instead.
	 */
	public ShapeCompartmentFigure(String title) {
		this(title, MapModeUtil.getMapMode().DPtoLP(ResizableCompartmentFigure.MIN_CLIENT_DP));
	}
	
	/**
	 * Create an instance.  Calls {@link #configureFigure()} to reconfigure
	 * the scrollpane.
	 * 
	 * @param title figure's title.
	 * @param minClientSize <code>int</code> that is the minimum size the client area can occupy in 
	 * logical coordinates.
     * @deprecated use @link(ResizableCompartment(String compartmentTitle, IMapMode mm)} instead.
	 */
	public ShapeCompartmentFigure(String title, int minClientSize) {
		super(title, minClientSize);
		configureFigure();
	}
    
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
        configureFigure(mm);
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
        scrollpane.setViewport(new FreeformViewport());
        
        Insets insets = new Insets(mm.DPtoLP(1), mm.DPtoLP(2),
            mm.DPtoLP(1), mm.DPtoLP(0));
        Dimension size = new Dimension(mm.DPtoLP(15), mm.DPtoLP(15));
        
        scrollpane.setHorizontalScrollBar(
            new ListScrollBar(Orientable.HORIZONTAL, insets, size, 
            mm.DPtoLP(10), mm.DPtoLP(50)));
        scrollpane.setVerticalScrollBar(new ListScrollBar(Orientable.VERTICAL, insets, size, 
            mm.DPtoLP(10), mm.DPtoLP(50)));

        scrollpane.setScrollBarVisibility(ScrollPane.AUTOMATIC);
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