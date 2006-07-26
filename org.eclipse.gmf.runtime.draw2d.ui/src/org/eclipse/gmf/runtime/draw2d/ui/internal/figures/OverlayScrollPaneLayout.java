/******************************************************************************
 * Copyright (c) 2000, 2003  IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
        int hVis;
        int vVis;
        ScrollPane scrollpane;
        ScrollBar vBar;
        ScrollBar hBar;      
        if (parent instanceof AnimatableScrollPane) {
            AnimatableScrollPane p = (AnimatableScrollPane) parent;
            hBar = p.basicGetHorizontalScrollBar();
            vBar = p.basicGetVerticalScrollBar();
            hVis = p.getHorizontalScrollBarVisibility();
            vVis = p.getVerticalScrollBarVisibility();
            scrollpane = p;
        } else {
            scrollpane = (ScrollPane) parent;
            hVis = scrollpane.getHorizontalScrollBarVisibility();
            vVis = scrollpane.getVerticalScrollBarVisibility();           
                hBar = (hVis != NEVER)?scrollpane.getHorizontalScrollBar():null;            
                vBar = (vVis != NEVER)?scrollpane.getVerticalScrollBar():null;
        }
		Rectangle clientArea = parent.getClientArea();
		int bottom = 0;
		int right  = 0 ;		
		Viewport viewport = scrollpane.getViewport();
		Dimension available = clientArea.getSize();
		Dimension preferred = viewport.getPreferredSize(available.width, available.height).getCopy();
		boolean none = available.contains(preferred);
		boolean both =
			!none
				&& vVis != NEVER
				&& hVis != NEVER
				&& preferred.contains(available);
		boolean showV = both || (preferred.height > available.height && (available.height > 0));
		boolean showH = both || (preferred.width > available.width && (available.width > 0));
		//Adjust for visibility override flags
		showV = !(vVis == NEVER) && (showV || vVis == ALWAYS);
		showH = !(hVis == NEVER) && (showH || hVis == ALWAYS);
		Rectangle bounds, viewportArea = clientArea;
		int hPad = 0;
		int vPad = 0;       
	
		if (showH){
            hBar = scrollpane.getHorizontalScrollBar();
			bottom = hBar.getPreferredSize(clientArea.width, clientArea.height).height;
			hPad = hBar.getSize().height;
		}
		
		if (showV){
            vBar = scrollpane.getVerticalScrollBar();
			right =	vBar.getPreferredSize(clientArea.width, clientArea.height).width;
			vPad = vBar.getSize().width;
		}
		
		if (showV) {
			bounds =
				new Rectangle(
					viewportArea.right() - right,
					viewportArea.y,
					right,
					viewportArea.height - hPad);
			vBar.setBounds(bounds);
		}
        
		if (showH) {
			bounds =
				new Rectangle(
					viewportArea.x,
					viewportArea.bottom() - bottom,
					viewportArea.width - vPad,
					bottom);
			hBar.setBounds(bounds);
		}
        
		if (vBar!=null){
			vBar.setVisible(showV);            
        }
		if (hBar!=null){
			hBar.setVisible(showH);            
        }
        
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
