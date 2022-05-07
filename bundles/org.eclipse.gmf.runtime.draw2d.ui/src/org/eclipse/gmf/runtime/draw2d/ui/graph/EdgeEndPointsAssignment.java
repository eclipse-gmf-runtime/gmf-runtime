/******************************************************************************
 * Copyright (c) 2008, 2022 IBM Corporation and others.
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
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;

/**
 * Assigns the locations to edges end points as well as lays out border nodes
 * 
 * @author aboyko
 * @since 2.1
 */
class EdgeEndPointsAssignment {
	
	private DirectedGraph graph;
	
	public EdgeEndPointsAssignment(DirectedGraph g) {
		this.graph = g;
	}	
	
	void assignEdgesEndPoints() {
		for (int i = 0; i < graph.edges.size(); i++) {
			Edge e = graph.edges.getEdge(i);
			e.start = null;
			e.end = null;
		}
		
		Collections.sort(graph.nodes, new Comparator<Node>() {
			public int compare(Node arg0, Node arg1) {
				return arg0.width - arg1.width;
			}
		});
		
		for (int i = 0; i < graph.nodes.size(); i++) {
			Node node = graph.nodes.getNode(i);
			assignEndPointsForEdgesFromNode(node);
		}
	}
	
	private void assignEndPointsForEdgesFromNode(Node node) {
		EdgeList incoming = new EdgeList(), outgoing = new EdgeList();
		List<BorderNode> specialBorderNodes = new ArrayList<>();
		if (node instanceof ConstantSizeNode) {
			initEdgesSets((ConstantSizeNode)node, incoming, outgoing, specialBorderNodes);
		} else {
			incoming = node.incoming;
			outgoing = node.outgoing;
		}
		
		if (node instanceof ConstantSizeNode && ((ConstantSizeNode)node).minIncomingPadding > 0) {
			assignEdgesEndPoints(incoming, (ConstantSizeNode) node, true);
		} else {
			for (int i = 0; i < incoming.size(); i++) {
				setEndPoint(incoming.getEdge(i), new Point(node.x + node.getOffsetIncoming(), node.y));
			}
		}
		
		if (node instanceof ConstantSizeNode && ((ConstantSizeNode)node).minOutgoingPadding > 0) {
			assignEdgesEndPoints(outgoing, (ConstantSizeNode) node, false);
		} else {
			for (int i = 0; i < outgoing.size(); i++) {
				setStartPoint(outgoing.getEdge(i), new Point(node.x + node.getOffsetOutgoing(), node.y + node.height));
			}
		}
		
		if (node instanceof ConstantSizeNode) {
			assignEndPointsForJointEdgeWithIncomingAndOutgoingEdges((ConstantSizeNode)node, specialBorderNodes);
		}
	}
	
	private void initEdgesSets(ConstantSizeNode n, EdgeList incoming, EdgeList outgoing, List<BorderNode> specialBorderNodes) {
		for (int i = 0; i < n.outgoing.size(); i++) {
			Edge e = n.outgoing.getEdge(i);
			if (e instanceof ConstrainedEdge) {
				ConstrainedEdge ce = (ConstrainedEdge) e;
				if (ce.sourceConstraint != null) {
					continue;
				}
			}
			outgoing.add(e);
		}
		
		for (int i = 0;  i < n.incoming.size(); i++) {
			Edge e = n.incoming.getEdge(i);
			if (e instanceof ConstrainedEdge) {
				ConstrainedEdge ce = (ConstrainedEdge) e;
				if (ce.targetConstraint != null) {
					continue;
				}
			}
			incoming.add(e);
		}
		
		for (Iterator<BorderNode> itr = n.borderNodes.iterator(); itr.hasNext();) {
			BorderNode borderNode = itr.next();
			if (!(borderNode.incomingJointEdges.edges.isEmpty() ^ borderNode.outgoingJointEdges.edges.isEmpty())) {
				specialBorderNodes.add(borderNode);
			} else if (borderNode.incomingJointEdges.edges.isEmpty()) {
				outgoing.add(borderNode.outgoingJointEdges);
			} else {
				incoming.add(borderNode.incomingJointEdges);
			}
		}
		
		Collections.sort(incoming, new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				return GraphUtilities.getIncomingEdgeBendpointX(e1, graph) - GraphUtilities.getIncomingEdgeBendpointX(e2, graph);
			}
		});
		
		Collections.sort(outgoing, new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				return GraphUtilities.getOutogingEdgeBendpointX(e1, graph) - GraphUtilities.getOutogingEdgeBendpointX(e2, graph);
			}
		});
	}

	private void assignEdgesEndPoints(EdgeList edges, ConstantSizeNode n, boolean end) {
		int leftIndex;
		int rightIndex;
		int padding = end ? n.minIncomingPadding : n.minOutgoingPadding;
		int leftX = n.x;
		int rightX = n.x + n.width;
		int leftBendpointX = 0;
		int rightBendpointX = 0;
		for (leftIndex = 0; leftIndex < edges.size(); leftIndex++) {
			Edge e = edges.getEdge(leftIndex);
			leftBendpointX = end ? GraphUtilities.getIncomingEdgeBendpointX(e, graph) : GraphUtilities.getOutogingEdgeBendpointX(e, graph);			
			if (leftX < leftBendpointX && leftBendpointX < rightX && (float)(leftBendpointX - leftX)/(leftIndex + 1) >= padding) {
				break;
			}
		}
		for (rightIndex = edges.size() - 1; rightIndex >= leftIndex; rightIndex--) {
			Edge e = edges.getEdge(rightIndex);
			rightBendpointX = end ? GraphUtilities.getIncomingEdgeBendpointX(e, graph) : GraphUtilities.getOutogingEdgeBendpointX(e, graph);			
			if (leftX < rightBendpointX && rightBendpointX < rightX && (float)(rightX - rightBendpointX)/(edges.size() - rightIndex) >= padding) {
				break;
			}
		}
		
		int y = end ? n.y : n.y + n.height;
		if (rightIndex >= leftIndex) {
			uniformlyPadEdges(edges, 0, leftIndex, new Point(leftX, y), new Point(leftBendpointX, y), end);
			uniformlyPadEdges(edges, rightIndex + 1, edges.size(), new Point(rightBendpointX, y), new Point(rightX, y), end);
			makeStraight(edges, leftIndex, rightIndex + 1, end);
		} else {
			uniformlyPadEdges(edges, 0, edges.size(), new Point(leftX, y), new Point(rightX, y), end);
		}
	}
	
	private void uniformlyPadEdges(List edges, int startIndex, int endIndex, Point startPoint, Point endPoint, boolean end) {
		Dimension diff = endPoint.getDifference(startPoint);
		int numPieces = endIndex - startIndex + 1;
		for (int i = startIndex; i < endIndex; i++) {
			Edge e = (Edge) edges.get(i);
			float coefficient = (float) (i - startIndex + 1) / numPieces;
			Point p = startPoint.getCopy().translate(diff.getCopy().scale(coefficient));
			if (end) {
				setEndPoint(e, p);
			} else {
				setStartPoint(e, p);
			}
		}
	}
	
	private void makeStraight(EdgeList edges, int startIndex, int endIndex, boolean end) {
		for (int i = startIndex; i < endIndex; i++) {
			Edge e = edges.getEdge(i);
			int y = end ? e.target.y : e.source.y + e.source.height;
			if (e instanceof ConstrainedEdge) {
				ConstrainedEdge ce = (ConstrainedEdge) e;
				if (end && ce.targetConstraint != null) {
					y = ce.targetConstraint.y;
				} else if (!end && ce.sourceConstraint != null) {
					y = ce.sourceConstraint.y + ce.sourceConstraint.height;
				}
			}
			if (end) {
				setEndPoint(e, new Point(GraphUtilities.getIncomingEdgeBendpointX(e, graph), y));
			} else {
				setStartPoint(e, new Point(GraphUtilities.getOutogingEdgeBendpointX(e, graph), y));
			}
		}
	}
	
	private void setStartPoint(Edge e, Point p) {
		if (e instanceof JointEdges) {
			JointEdges je = (JointEdges) e;
			je.getJoint().setPoint(p);
			assignEndPointsForEdgesFromBorderNode(je.getJoint());
		} else {
			if (p == null) {
				if (e instanceof ConstrainedEdge && ((ConstrainedEdge) e).sourceConstraint != null) {
					p = ((ConstrainedEdge) e).sourceConstraint.getEdgesDefaultEndPoint();
				} else {
					p = new Point(e.source.x + e.source.getOffsetOutgoing(), e.source.y + e.source.height);
				}
			}
			e.start = p;
		}
	}
	
	private void setEndPoint(Edge e, Point p) {
		if (e instanceof JointEdges) {
			JointEdges je = (JointEdges) e;
			je.getJoint().setPoint(p);
			assignEndPointsForEdgesFromBorderNode(je.getJoint());
		} else {
			if (p == null) {
				if (e instanceof ConstrainedEdge && ((ConstrainedEdge) e).targetConstraint != null) {
					p = ((ConstrainedEdge) e).targetConstraint.getEdgesDefaultEndPoint();
				} else {
					p = new Point(e.target.x + e.target.getOffsetIncoming(), e.target.y);
				}
			}
			e.end = p;
		}
	}
	
	private void assignEndPointsForJointEdgeWithIncomingAndOutgoingEdges(ConstantSizeNode node, List<BorderNode> specialBorderNodes) {
		Collections.sort(specialBorderNodes, new Comparator<>() {
			public int compare(BorderNode bn1, BorderNode bn2) {
				return bn1.incomingJointEdges.edges.size() + bn1.outgoingJointEdges.edges.size() - bn2.incomingJointEdges.edges.size() - bn2.outgoingJointEdges.edges.size(); 
			}
		});
		
		List<BorderNode> leftSideBorderNodes = new ArrayList<>(specialBorderNodes.size() / 2 + 1);
		List<BorderNode> rightSideBorderNodes = new ArrayList<>(specialBorderNodes.size() / 2 + 1);
		
		for (Iterator<BorderNode> itr = specialBorderNodes.iterator(); itr.hasNext();) {
			leftSideBorderNodes.add(itr.next());
			itr.remove();
			if (itr.hasNext()) {
				rightSideBorderNodes.add(itr.next());
				itr.remove();
			}
		}
		
		Collections.sort(leftSideBorderNodes, new Comparator<>() {
			public int compare(BorderNode bn1, BorderNode bn2) {
				return bn1.outgoingJointEdges.edges.size() - bn1.incomingJointEdges.edges.size() - (bn2.outgoingJointEdges.edges.size() - bn2.incomingJointEdges.edges.size());
			}		
		});
		Collections.sort(rightSideBorderNodes, new Comparator<>() {
			public int compare(BorderNode bn1, BorderNode bn2) {
				return bn1.outgoingJointEdges.edges.size() - bn1.incomingJointEdges.edges.size() - (bn2.outgoingJointEdges.edges.size() - bn2.incomingJointEdges.edges.size());
			}		
		});
		
		uniformlyPadBorderNodes(leftSideBorderNodes, 0, leftSideBorderNodes.size(), new Point(node.x, node.y), new Point(node.x, node.y + node.height));
		uniformlyPadBorderNodes(rightSideBorderNodes, 0, rightSideBorderNodes.size(), new Point(node.x + node.width, node.y), new Point(node.x + node.width, node.y + node.height));
		
		for (Iterator<BorderNode> itr = leftSideBorderNodes.iterator(); itr.hasNext();) {
			BorderNode bn = itr.next();
			if (!bn.incomingJointEdges.edges.isEmpty() || !bn.outgoingJointEdges.edges.isEmpty()) {
				assignEndPointsForEdgesFromBorderNode(bn);
			}
		}
		for (Iterator<BorderNode> itr = rightSideBorderNodes.iterator(); itr.hasNext();) {
			BorderNode bn = itr.next();
			if (!bn.incomingJointEdges.edges.isEmpty() || !bn.outgoingJointEdges.edges.isEmpty()) {
				assignEndPointsForEdgesFromBorderNode(bn);
			}
		}		
	}
	
	private void uniformlyPadBorderNodes(List<BorderNode> borderNodes, int startIndex, int endIndex, Point startPoint, Point endPoint) {
		Dimension diff = endPoint.getDifference(startPoint);
		int numPieces = endIndex - startIndex + 1;
		for (int i = startIndex; i < endIndex; i++) {
			float coefficient = (float) (i - startIndex + 1) / numPieces;
			Point p = startPoint.getCopy().translate(diff.getCopy().scale(coefficient));
			borderNodes.get(i).setPoint(p);
		}
		
	}
	
	private void assignEndPointsForEdgesFromBorderNode(BorderNode node) {
		Collections.sort(node.incomingJointEdges.edges, new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				return GraphUtilities.getIncomingEdgeBendpointX(e1, graph) - GraphUtilities.getIncomingEdgeBendpointX(e2, graph);				}
		});
		Collections.sort(node.outgoingJointEdges.edges, new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				return GraphUtilities.getOutogingEdgeBendpointX(e1, graph) - GraphUtilities.getOutogingEdgeBendpointX(e2, graph);				}
		});
		if (node.minIncomingPadding > 0 || node.minOutgoingPadding > 0) {
			if (node.position == PositionConstants.NORTH) {
				assignEdgesEndPoints(node.incomingJointEdges.edges, node, true);
			} else if (node.position == PositionConstants.SOUTH) {
				assignEdgesEndPoints(node.outgoingJointEdges.edges, node, false);
			} else {
				Point incomingStartPt, incomingEndPt, outgoingStartPt, outgoingEndPt;
				if (node.position == PositionConstants.WEST) {
					incomingStartPt = new Point(node.x, node.y + (node.incomingJointEdges.edges.size() + 1) * node.height / (node.incomingJointEdges.edges.size() + node.outgoingJointEdges.edges.size() + 1));
					incomingEndPt = new Point(node.x, node.y);
					outgoingStartPt = new Point(node.x, node.y + node.incomingJointEdges.edges.size() * node.height / (node.incomingJointEdges.edges.size() + node.outgoingJointEdges.edges.size() + 1));
					outgoingEndPt = new Point(node.x, node.y + node.height);
				} else {
					incomingStartPt = new Point(node.x + node.width, node.y);
					incomingEndPt = new Point(node.x + node.width, node.y + (node.incomingJointEdges.edges.size() + 1) * node.height / (node.incomingJointEdges.edges.size() + node.outgoingJointEdges.edges.size() + 1));
					outgoingStartPt = new Point(node.x + node.width, node.y + node.height);
					outgoingEndPt = new Point(node.x + node.width, node.y + node.incomingJointEdges.edges.size() * node.height / (node.incomingJointEdges.edges.size() + node.outgoingJointEdges.edges.size() + 1));
				}
				uniformlyPadEdges(node.incomingJointEdges.edges, 0, node.incomingJointEdges.edges.size(), incomingStartPt, incomingEndPt, true);
				uniformlyPadEdges(node.outgoingJointEdges.edges, 0, node.outgoingJointEdges.edges.size(), outgoingStartPt, outgoingEndPt, false);	
			}
		} else {
			Point defaultPt = node.getEdgesDefaultEndPoint();
			for (int i = 0; i < node.incomingJointEdges.edges.size(); i++) {
				setEndPoint(node.incomingJointEdges.edges.getEdge(i), defaultPt);
			}
			for (int i = 0; i < node.outgoingJointEdges.edges.size(); i++) {
				setStartPoint(node.outgoingJointEdges.edges.getEdge(i), defaultPt);
			}
		}
	}
	
}
