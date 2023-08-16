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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Node;

/**
 * Performs the pre-routing work such as to route edges from border nodes located on left or right sides of a parent node.
 * Those edges are assigned <code>startingRoutedPoints</code> and/or <endingRoutedPoints</code> to route edges to the top or
 * bottom of the parent node rank.
 * This class can be used to do some pre-routing work for orthogonal edges in the future.
 * 
 * @author aboyko
 * @since 2.1
 */
class PreRouteEdges {
	
	private DirectedGraph g;
	
	public PreRouteEdges(DirectedGraph g) {
		this.g = g;
	}
	
	void preRouteEdges() {
		for (int i = 0; i < g.nodes.size(); i++) {
			Node n = g.nodes.getNode(i);
			if (n instanceof ConstantSizeNode) {
				preRouteEdgesFromNode((ConstantSizeNode)n);
			}
		}
	}
	
	private void preRouteEdgesFromNode(ConstantSizeNode n) {
		List<BorderNode> leftBorderNodes = new ArrayList<BorderNode>();
		List<BorderNode> rightBorderNodes = new ArrayList<BorderNode>();
		for (Iterator<BorderNode> itr = n.borderNodes.iterator(); itr.hasNext();) {
			BorderNode bn = itr.next();
			if (bn.position == PositionConstants.EAST) {
				rightBorderNodes.add(bn);
			} else if (bn.position == PositionConstants.WEST) {
				leftBorderNodes.add(bn);
			}
		}
		createRoutingPointsForSideBorderNodes(leftBorderNodes, n, PositionConstants.WEST);
		createRoutingPointsForSideBorderNodes(rightBorderNodes, n, PositionConstants.EAST);
	}
	
	private void createRoutingPointsForSideBorderNodes(List<BorderNode> nodes, ConstantSizeNode parentNode, int position) {
		List<ConstrainedEdge> incomingEdges = new ArrayList<ConstrainedEdge>();
		List<ConstrainedEdge> outgoingEdges = new ArrayList<ConstrainedEdge>();
		int maxBorderItemOutsideWidth = initBorderNodeEdgesLists(nodes, incomingEdges, outgoingEdges, position);
		
		int nodePadding = position == PositionConstants.WEST ? g.getPadding(parentNode).left : g.getPadding(parentNode).right;
		
		if (nodePadding <= maxBorderItemOutsideWidth) {
			throw new RuntimeException("Node padding must be greater than the the width of the widest border node"); //$NON-NLS-1$
		}
		
		if (incomingEdges.isEmpty() || outgoingEdges.isEmpty()) {
			/*
			 *  Border nodes sitting on the side should either have both the incoming
			 *  and outgoing edges or no edges at all
			 */
			return;
		}
		
		int incomingPadding = (nodePadding - maxBorderItemOutsideWidth) / (incomingEdges.size() + 1);
		int outgoingPadding = (nodePadding - maxBorderItemOutsideWidth) / (outgoingEdges.size() + 1);
//		if (incomingEdges.size() >= outgoingEdges.size()) {
//			incomingPadding = (nodePadding - maxBorderItemOutsideWidth) / (incomingEdges.size() + 1);
//			outgoingPadding = incomingEdges.size() % outgoingEdges.size() == 0 ? (nodePadding - maxBorderItemOutsideWidth) / (outgoingEdges.size() + 2)
//					: (nodePadding - maxBorderItemOutsideWidth) / (outgoingEdges.size() + 1);
//		} else {
//			outgoingPadding = (nodePadding - maxBorderItemOutsideWidth) / (outgoingEdges.size() + 1);
//			incomingPadding = outgoingEdges.size() % incomingEdges.size() == 0 ? (nodePadding - maxBorderItemOutsideWidth) / (incomingEdges.size() + 2)
//					: (nodePadding - maxBorderItemOutsideWidth) / (incomingEdges.size() + 1);
//		}

		Collections.sort(incomingEdges, new Comparator<ConstrainedEdge>() {
			public int compare(ConstrainedEdge e1, ConstrainedEdge e2) {
				int diff = GraphUtilities.getIncomingEdgeBendpointX(e2, g) - GraphUtilities.getIncomingEdgeBendpointX(e1, g);
				if (e1.targetConstraint.position == PositionConstants.WEST) {
					return diff;
				} else {
					return -diff; 
				}
			}
		});
		
		Collections.sort(outgoingEdges, new Comparator<ConstrainedEdge>() {
			public int compare(ConstrainedEdge e1, ConstrainedEdge e2) {
				int diff = GraphUtilities.getOutogingEdgeBendpointX(e2, g) - GraphUtilities.getOutogingEdgeBendpointX(e1, g);
				if (e1.sourceConstraint.position == PositionConstants.WEST) {
					return diff;
				} else {
					return -diff; 
				}
			}
		});
		
		int rankHeight = GraphUtilities.getRankHeightFromNode(parentNode, g);
		Point incomingStartPt = position == PositionConstants.WEST ? new Point(parentNode.x - maxBorderItemOutsideWidth, parentNode.y)
			: new Point(parentNode.x + parentNode.width + maxBorderItemOutsideWidth, parentNode.y);
		Point outgoingStartPt = position == PositionConstants.WEST ? new Point(parentNode.x - maxBorderItemOutsideWidth, parentNode.y + rankHeight)
			: new Point(parentNode.x + parentNode.width + maxBorderItemOutsideWidth, parentNode.y + parentNode.height);
		Dimension incomingOffset = position == PositionConstants.WEST ? new Dimension(-incomingPadding, 0)
			: new Dimension(incomingPadding, 0);
		Dimension outgoingOffset = position == PositionConstants.WEST ? new Dimension(-outgoingPadding, 0)
			: new Dimension(outgoingPadding, 0);
		
		padBendpointsForEdges(incomingEdges, incomingStartPt, incomingOffset, true);
		padBendpointsForEdges(outgoingEdges, outgoingStartPt, outgoingOffset, false);
		
	}
	
	private void padBendpointsForEdges(List<ConstrainedEdge> edges, Point startPoint, Dimension offset, boolean incoming) {
		Point current = startPoint.getCopy().translate(offset);
		for (Iterator<ConstrainedEdge> itr = edges.iterator(); itr.hasNext(); current.translate(offset)) {
			ConstrainedEdge e = itr.next();
			if (incoming) {
				e.endingRoutedPoints.addPoint(current);
				if (e.targetConstraint.minIncomingPadding > 0 || e.targetConstraint.minOutgoingPadding > 0) {
					e.endingRoutedPoints.addPoint(current.x, e.end.y);
				}
				e.endingRoutedPoints.addPoint(e.end);
			} else {
				e.startingRoutedPoints.addPoint(e.start);
				if (e.sourceConstraint.minIncomingPadding > 0 || e.sourceConstraint.minOutgoingPadding > 0) {
					e.startingRoutedPoints.addPoint(current.x, e.start.y);
				}
				e.startingRoutedPoints.addPoint(current);
			}
		}
	}
	
	private int initBorderNodeEdgesLists(List<BorderNode> nodes, List<ConstrainedEdge> incomingEdges, List<ConstrainedEdge> outgoingEdges, int position) {
		int maxBorderNodeOutsideWidth = 0;
		for (Iterator<BorderNode> itr = nodes.iterator(); itr.hasNext();) {
			BorderNode bn = itr.next();
			if (bn.position == position) {
				bn.incomingJointEdges.edges.stream().filter(ConstrainedEdge.class::isInstance).map(ConstrainedEdge.class::cast).forEach(incomingEdges::add);
				bn.outgoingJointEdges.edges.stream().filter(ConstrainedEdge.class::isInstance).map(ConstrainedEdge.class::cast).forEach(outgoingEdges::add);
				maxBorderNodeOutsideWidth = Math.max(maxBorderNodeOutsideWidth, (int)(bn.width * bn.getOutsideRatio()));
			}
		}
		return maxBorderNodeOutsideWidth;
	}
	
	

}
