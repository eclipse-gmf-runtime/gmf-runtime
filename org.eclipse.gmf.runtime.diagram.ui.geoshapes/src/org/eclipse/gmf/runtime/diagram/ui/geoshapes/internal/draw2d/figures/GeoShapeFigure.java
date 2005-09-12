/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
/*
 * Created on Jun 22, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;


/**
 * @author melaasar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GeoShapeFigure extends DefaultSizeNodeFigure {
	
	/**
	 * Creates a new GeoShapeFigure
	 */
	public GeoShapeFigure(int defWidth, int defHeight) {
		super(defWidth, defHeight);
		setOpaque(true);
		setLayoutManager(new StackLayout() {
			public void layout(IFigure figure) {
				Rectangle r = figure.getClientArea();
				List children = figure.getChildren();
				IFigure child;
				Dimension d;
				for (int i = 0; i < children.size(); i++) {
					child = (IFigure)children.get(i);
					d = child.getPreferredSize(r.width, r.height);
					d.width = Math.min(d.width, r.width);
					d.height = Math.min(d.height, r.height);
					Rectangle childRect = new Rectangle(
						r.x + (r.width - d.width)/2,
						r.y + (r.height - d.height)/2,
						d.width,
						d.height);
					child.setBounds(childRect);
				}
			}
		});
		
		IFigure f = new Figure();
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		layout.setSpacing(MapMode.DPtoLP(5));
		f.setLayoutManager(layout);
		add(f);
	}
	
	public IFigure getContentPane() {
		return (IFigure) getChildren().get(0);
	}
}
