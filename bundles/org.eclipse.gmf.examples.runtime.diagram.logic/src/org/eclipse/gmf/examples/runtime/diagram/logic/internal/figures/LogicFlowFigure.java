/******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * code copied from real logic example in gef
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class LogicFlowFigure
	extends NodeFigure
	implements HandleBounds
{
	private Dimension prefSize;
	
	public LogicFlowFigure(Dimension size) {
		super();
		setOpaque(true);
		prefSize = size;
	}
 
	/**
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		return getBounds().getShrinked(new Insets(2,0,2,0));
	}

	public Dimension getPreferredSize(int w, int h) {
		Dimension newPrefSize = super.getPreferredSize(w, h);
		newPrefSize.union(prefSize);
		return newPrefSize;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		Rectangle rect = getBounds().getCopy();
		rect.shrink(new Insets(2,0,2,0));
	}

	public String toString() {
		return "LogicFlowBoardFigure"; //$NON-NLS-1$
	}

	public void validate() {
		if(isValid()) return;
		super.validate();
	}
}
