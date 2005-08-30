/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ScrollPaneLayout;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/*
 * @canBeSeenBy %partners
 * 
 * <p>
 * Code taken from Eclipse reference bugzilla #98820
 * 
 */
public class OverlayScrollPaneLayout extends ScrollPaneLayout {

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure, int, int)
	 */
	protected Dimension calculatePreferredSize(
		IFigure container,
		int wHint,
		int hHint) {
		ScrollPane scrollpane = (ScrollPane) container;
		Insets insets = scrollpane.getInsets();

		int excludedWidth = insets.getWidth();
		int excludedHeight = insets.getHeight();

		return scrollpane
			.getViewport()
			.getPreferredSize(wHint - excludedWidth, hHint - excludedHeight)
			.getExpanded(excludedWidth, excludedHeight);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout(IFigure parent) {
		ScrollPane scrollpane = (ScrollPane) parent;
		Rectangle clientArea = parent.getClientArea();

		ScrollBar hBar = scrollpane.getHorizontalScrollBar(),
			vBar = scrollpane.getVerticalScrollBar();
		Viewport viewport = scrollpane.getViewport();

		Insets insets = new Insets();
		insets.bottom =
			hBar.getPreferredSize(clientArea.width, clientArea.height).height;
		insets.right =
			vBar.getPreferredSize(clientArea.width, clientArea.height).width;

		int hVis = scrollpane.getHorizontalScrollBarVisibility(),
			vVis = scrollpane.getVerticalScrollBarVisibility();

		Dimension available = clientArea.getSize(),
			preferred =
				viewport
					.getPreferredSize(available.width, available.height)
					.getCopy();

		boolean none = available.contains(preferred),
			both =
				!none
					&& vVis != NEVER
					&& hVis != NEVER
					&& preferred.contains(available),
			showV = both || preferred.height > available.height,
			showH = both || preferred.width > available.width;

		//Adjust for visibility override flags
		showV = !(vVis == NEVER) && (showV || vVis == ALWAYS);
		showH = !(hVis == NEVER) && (showH || hVis == ALWAYS);

		if (!showV)
			insets.right = 0;
		if (!showH)
			insets.bottom = 0;
		Rectangle bounds, viewportArea = clientArea;

		int vPad = showV ? vBar.getSize().width : 0;
		int hPad = showH ?  hBar.getSize().height : 0;
		if (showV) {
			bounds =
				new Rectangle(
					viewportArea.right() - insets.right,
					viewportArea.y,
					insets.right,
					viewportArea.height - hPad);
			vBar.setBounds(bounds);
			//vBar.setMaximum(preferred.height);
		}
		if (showH) {
			bounds =
				new Rectangle(
					viewportArea.x,
					viewportArea.bottom() - insets.bottom,
					viewportArea.width - vPad,
					insets.bottom);
			hBar.setBounds(bounds);
			//hBar.setMaximum(preferred.width);
		}
		vBar.setVisible(showV);
		hBar.setVisible(showH);
		viewport.setBounds(viewportArea);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ScrollPaneLayout#calculateMinimumSize(org.eclipse.draw2d.IFigure)
	 */
	public Dimension calculateMinimumSize(IFigure container) {
		ScrollPane scrollpane = (ScrollPane) container;
		return scrollpane.getViewport().getMinimumSize();
	}

}
