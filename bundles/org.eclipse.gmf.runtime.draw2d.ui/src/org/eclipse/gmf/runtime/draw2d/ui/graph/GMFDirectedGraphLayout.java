/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.draw2d.ui.graph;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;

/**
 * Implementation of the lLayout algorithm that:
 * <li> Preserves the node sizes
 * <li> Routes edges around the nodes
 * <li> Accounts for the edge routing style
 * <li> Lays out border nodes
 * <li> Pads edges end points
 * 
 * @author aboyko
 *
 */
public class GMFDirectedGraphLayout extends DirectedGraphLayout {

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.graph.DirectedGraphLayout#visit(org.eclipse.draw2d.graph.DirectedGraph)
	 */
	public void visit(DirectedGraph graph) {
		GraphUtilities.storeNodesSizes(graph);
		super.visit(graph);
		GraphUtilities.recallNodesSizes(graph);
		if (graph.getDirection() != PositionConstants.SOUTH) {
			GraphUtilities.transpose(graph);
		}
		postProcessGraph(graph);
		if (graph.getDirection() != PositionConstants.SOUTH) {
			GraphUtilities.transpose(graph);
		}
	}
	
	/**
	 * Performs layout work  after Draw2D DGL completes
	 * @param graph the directed graph
	 */
	public void postProcessGraph(DirectedGraph graph) {
		GraphUtilities.invertEdges(graph);
		new EdgeEndPointsAssignment(graph).assignEdgesEndPoints();
		new PreRouteEdges(graph).preRouteEdges();
		new EdgesRouter(graph).routeEdges();
		GraphUtilities.invertEdges(graph);
		
		new CleanupBorderNodeEdges(graph).cleanup();
	}

}
