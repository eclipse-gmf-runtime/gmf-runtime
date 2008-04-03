/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider;

/**
 * @author sshaw
 *
 * Custom provider that lays out the directed graph in a top to down fashion.
 */
public class TopDownProvider extends DefaultProvider {
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#translateToGraph(org.eclipse.draw2d.geometry.Rectangle)
	 */
	protected Rectangle translateToGraph(Rectangle r) {
		Rectangle rDP = r.getCopy();
		return rDP;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#translateFromGraph(org.eclipse.draw2d.geometry.Rectangle)
	 */
	protected Rectangle translateFromGraph(Rectangle rect) {
		Rectangle rLP = rect.getCopy();
		return rLP;
	}
	
	public Command layoutEditParts(List selectedObjects, IAdaptable layoutHint) {
		return super.layoutEditParts(selectedObjects, layoutHint);
	}
	
	protected Command createEdgesChangeBoundsCommands(DirectedGraph g, Point diff) {
		return super.createEdgesChangeBoundsCommands(g, diff);
	}
	
	protected Command createNodeChangeBoundCommands(DirectedGraph g, Point diff) {
		return super.createNodeChangeBoundCommands(g, diff);
	}
	
	protected Rectangle getNodeMetrics(Node n) {
		return super.getNodeMetrics(n);
	}
	
	protected void setLayoutDefaultMargin(int newMargin) {
		layoutDefaultMargin = newMargin;
	}
}
