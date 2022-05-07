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

package org.eclipse.gmf.runtime.gef.ui.figures;

import java.util.Iterator;
import java.util.ListIterator;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Wrapper <code>NodeFigure</code> figure to contain other figures, which may have different borders.
 * 
 * @author sshaw
 */
public class WrapperNodeFigure extends NodeFigure {

	private IFigure subFigure;

	/**
	 * Composite pattern for wrapping a template list compartment around any
	 * potential figure.
	 * 
	 * @param subFigure the <code>IFigure</code> that is being wrapped.
	 */
	public WrapperNodeFigure(IFigure subFigure) {
		setLayoutManager(new StackLayout() {
			public boolean isObservingVisibility() {
				return true;
			}
		});
		add(subFigure);
		this.subFigure = subFigure;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		
		Iterator iter = this.getChildren().iterator();
		while (iter.hasNext()){
			Figure figure = (Figure)iter.next();
			if (figure.containsPoint(x,y)){
				return true;
			}
		}
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		ListIterator li = getChildren().listIterator();
		Rectangle unionBounds = null;
		while (li.hasNext()) {
			IFigure fig = (IFigure) li.next();
			if (fig.isVisible()) {
				Rectangle figBounds = fig.getBounds();
				if (fig instanceof NodeFigure) {
					figBounds = ((NodeFigure) fig).getHandleBounds();
				}

				if (unionBounds == null)
					unionBounds = new Rectangle(figBounds);
				else
					unionBounds.union(figBounds);
			}
		}

		if (unionBounds != null)
			return unionBounds;

		return super.getHandleBounds();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		// Do nothing
	}

	/**
	 * This will return null since we have override the setBorder(Border) api.
	 */
	public Border getBorder() {
		return super.getBorder();
	}

	/**
	 * Sets the subfigure border. We are not permitting them to update the
	 * wrapper border. We intepret it as setting the border of the subFigure
	 * instead.
	 */
	public void setBorder(Border border) {
		subFigure.setBorder(border);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getSourceConnectionAnchorAt(org.eclipse.draw2d.geometry.Point)
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		if( subFigure instanceof NodeFigure )
			return ((NodeFigure)subFigure).getSourceConnectionAnchorAt(p);
		return super.getSourceConnectionAnchorAt(p);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getTargetConnectionAnchorAt(org.eclipse.draw2d.geometry.Point)
	 */
	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		if( subFigure instanceof NodeFigure )
			return ((NodeFigure)subFigure).getTargetConnectionAnchorAt(p);
		return super.getTargetConnectionAnchorAt(p);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getConnectionAnchor(java.lang.String)
	 */
	public ConnectionAnchor getConnectionAnchor(String terminal) {
		if( subFigure instanceof NodeFigure )
			return ((NodeFigure)subFigure).getConnectionAnchor(terminal);
		return super.getConnectionAnchor(terminal);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getConnectionAnchorTerminal(org.eclipse.draw2d.ConnectionAnchor)
	 */
	public String getConnectionAnchorTerminal(ConnectionAnchor c) {
		if( subFigure instanceof NodeFigure )
			return ((NodeFigure)subFigure).getConnectionAnchorTerminal(c);
		return super.getConnectionAnchorTerminal(c);		
	}


}
