/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import org.eclipse.gmf.runtime.draw2d.ui.figures.ListScrollBar;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.OverlayScrollPaneLayout;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;

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
	 */
	public ShapeCompartmentFigure(String title) {
		super(title);
		configureFigure();

	}

	/** Creates an instance.  */
	public ShapeCompartmentFigure() {
		this(null);
	}

	/**
	 * The scrollpane is configured to use a {@link FreeformLayer} with a
	 * {@link FreeformLayout} as its contents.
	 */
	protected void configureFigure() {
		ScrollPane scrollpane = getScrollPane();
		scrollpane.setViewport(new FreeformViewport());
		
		scrollpane.setHorizontalScrollBar(
			new ListScrollBar(Orientable.HORIZONTAL));
		scrollpane.setVerticalScrollBar(new ListScrollBar(Orientable.VERTICAL));

		scrollpane.setScrollBarVisibility(ScrollPane.AUTOMATIC);
		scrollpane.setLayoutManager(new OverlayScrollPaneLayout() );

		IFigure contents = new FreeformLayer();
		contents.setLayoutManager(new FreeformLayout());
		scrollpane.setContents(contents);
		int MB = MapMode.DPtoLP(5);
		scrollpane.setBorder(new MarginBorder(MB, MB, MB, MB));
		int SZ = MapMode.DPtoLP(10);
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