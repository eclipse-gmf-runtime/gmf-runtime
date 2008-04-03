/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.draw2d.ui.graph;

import java.util.Iterator;

import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Rank;

/**
 * Class containing graph utility methods used internally
 * 
 * @author aboyko
 * @since 2.1
 */
class GraphUtilities {
	
	static void transpose(DirectedGraph g) {
		for (int i = 0; i < g.nodes.size(); i++) {
			transpose(g.nodes.getNode(i));
		}
		for (int i = 0; i < g.edges.size(); i++) {
			transpose(g.edges.getEdge(i));
		}
		g.getLayoutSize().transpose();
		g.getDefaultPadding().transpose();
		if (g instanceof CompoundDirectedGraph) {
			CompoundDirectedGraph cg = (CompoundDirectedGraph) g;
			for (int i = 0; i < cg.subgraphs.size(); i++) {
				transpose(cg.subgraphs.getNode(i));
			}
		}
	}
	
	private static void transpose(Node n) {
		int temp = n.x;
		n.x = n.y;
		n.y = temp;
		temp = n.width;
		n.width = n.height;
		n.height = temp;
		if (n.getPadding() != null) {
			n.getPadding().transpose();
		}
		if (n instanceof ConstantSizeNode) {
			for (Iterator<BorderNode> itr = ((ConstantSizeNode)n).borderNodes.iterator(); itr.hasNext();) {
				transpose(itr.next());
			}
		}
	}
	
	private static void transpose(Edge e) {
		e.start.transpose();
		e.end.transpose();
		e.getPoints().transpose();
		if (e.vNodes != null) {
			for (int i = 0; i < e.vNodes.size(); i++) {
				transpose(e.vNodes.getNode(i));
			}
		}
	}
	
	static void storeNodesSizes(DirectedGraph g) {
		for (int i = 0; i < g.nodes.size(); i++) {
			Node n = g.nodes.getNode(i);
			if (n instanceof ConstantSizeNode) {
				ConstantSizeNode cn = (ConstantSizeNode) n;
				cn.constantWidth = cn.width;
				cn.constantHeight = cn.height;
			}
		}
	}
	
	static void recallNodesSizes(DirectedGraph g) {
		for (int i = 0; i < g.nodes.size(); i++) {
			Node n = g.nodes.getNode(i);
			if (n instanceof ConstantSizeNode) {
				ConstantSizeNode cn = (ConstantSizeNode) n;
				cn.width = cn.constantWidth;
				cn.height = cn.constantHeight;
			}
		}
	}
	
	static int getRankHeightFromNode(Node n, DirectedGraph g) {
		int rankHeight = -1;
		Rank rank = getNodeRank(n, g);
		if (rank != null) {
			for (int i = 0; i < rank.size(); i++) {
				rankHeight = Math.max(rankHeight, rank.getNode(i).height);
			}
		}
		return rankHeight;
	}
	
	static Rank getNodeRank(Node n, DirectedGraph g) {
		for (int i = 0; i < g.ranks.size(); i++) {
			Rank rank = g.ranks.getRank(i);
			if (!rank.isEmpty()) {
				if (n.y == rank.getNode(0).y) {
					return rank;
				}
			}
		}
		return null;
	}

	static int getOutogingEdgeBendpointX(Edge e, DirectedGraph g) {
		if ( e instanceof JointEdges) {
			return getOutogingEdgeBendpointX(((JointEdges)e).getLeadingEdge(), g);
		} else {
			if (e.vNodes == null) {
				if (e.end != null) {
					return e.end.x;
				} else {
					int sourceX = e.source.x + e.source.getOffsetOutgoing();
					if (e.target instanceof ConstantSizeNode && ((ConstantSizeNode)e.target).getMinIncomingPadding() > 0 && e.target.x < sourceX && sourceX < e.target.x + e.target.width) {
						return sourceX;
					} else {
						return e.target.x + e.target.getOffsetIncoming(); 
					}
				}
			} else {
				Node vn = e.vNodes.getNode(0);
				if (e instanceof ConstrainedEdge && ((ConstrainedEdge)e).getStyle().equals(ConstrainedEdge.ORTHOGONAL_ROUTING_STYLE)) {
					return vn.x + vn.getOffsetIncoming();
				}
				int leftX = Integer.MIN_VALUE;
				int rightX = Integer.MAX_VALUE;
				for (int i = e.vNodes.size() - 1; i >= 0; i--) {
					vn = e.vNodes.getNode(i);
					int currentLeftX = getLeftX(vn, e, g);
					int currentRightX = getRightX(vn, e, g);
					if (currentLeftX > rightX) {
						return rightX;
					} else if (currentRightX < leftX) {
						return leftX;
					} else {
						if (currentLeftX > leftX) {
							leftX = currentLeftX;
						}
						if (currentRightX < rightX) {
							rightX = currentRightX;
						}
					}
				}
				int targetX = e.end != null ? e.end.x : e.target.x + e.target.getOffsetIncoming();
				if (targetX > rightX) {
					return rightX;
				} else if (targetX < leftX) {
					return leftX;
				}
				return targetX;
			}
		}
	}
	
	static int getIncomingEdgeBendpointX(Edge e, DirectedGraph g) {
		if (e instanceof JointEdges) {
			return getIncomingEdgeBendpointX(((JointEdges)e).getLeadingEdge(), g);
		} else {
			if (e.vNodes == null) {
				if (e.start != null) {
					return e.start.x;
				} else {
					int targetX = e.target.x + e.target.getOffsetIncoming();
					if (e.source instanceof ConstantSizeNode && ((ConstantSizeNode)e.source).getMinOutgoingPadding() > 0 && e.source.x < targetX && targetX < e.source.x + e.source.width) {
						return targetX;
					} else {
						return e.source.x + e.source.getOffsetOutgoing();
					}
				}
			} else {
				Node vn = e.vNodes.getNode(e.vNodes.size() - 1);
				if (e instanceof ConstrainedEdge && ((ConstrainedEdge)e).getStyle().equals(ConstrainedEdge.ORTHOGONAL_ROUTING_STYLE)) {
					return vn.x + vn.getOffsetOutgoing();
				}
				int leftX = Integer.MIN_VALUE;
				int rightX = Integer.MAX_VALUE;
				for (int i = e.vNodes.size() - 1; i >= 0; i--) {
					vn = e.vNodes.getNode(i);
					int currentLeftX = getLeftX(vn, e, g);
					int currentRightX = getRightX(vn, e, g);
					if (currentLeftX > rightX) {
						return rightX;
					} else if (currentRightX < leftX) {
						return leftX;
					} else {
						if (currentLeftX > leftX) {
							leftX = currentLeftX;
						}
						if (currentRightX < rightX) {
							rightX = currentRightX;
						}
					}
				}
				int sourceX = e.start != null ? e.start.x : e.source.x + e.source.getOffsetOutgoing();
				if (sourceX > rightX) {
					return rightX;
				} else if (sourceX < leftX) {
					return leftX;
				}
				return sourceX;
			}
		}
	}
	
	private static int getLeftX(Node vn, Edge e, DirectedGraph g) {
		return vn.getLeft() != null ? vn.getLeft().x + vn.getLeft().width + g.getPadding(vn.getLeft()).right + e.getPadding() - 1 : 0;
	}
	
	private static int getRightX(Node vn, Edge e, DirectedGraph g) {
		return vn.getRight() != null ? vn.getRight().x - g.getPadding(vn.getRight()).left - e.getPadding() + 1 : g.getLayoutSize().width;
	}
	
	static void translateGraph(DirectedGraph g, int x, int y) {
		for (int i = 0; i < g.nodes.size(); i++) {
			translateNode(g.nodes.getNode(i), x, y);
		}
		for (int i = 0; i < g.edges.size(); i++) {
			translateEdge(g.edges.getEdge(i), x, y);
		}
	}
	
	static void translateNode(Node n, int x, int y) {
		n.x += x;
		n.y += y;
		if (n instanceof ConstantSizeNode) {
			ConstantSizeNode cn = (ConstantSizeNode) n;
			for (Iterator<BorderNode> itr = cn.borderNodes.iterator(); itr.hasNext();) {
				translateNode(itr.next(), x, y);
			}
		}
	}
	
	static void translateEdge(Edge e, int x, int y) {
		e.start.translate(x, y);
		e.end.translate(x, y);
		e.getPoints().translate(x, y);
	}
	
	static void invertEdges(DirectedGraph g) {
		for (int i = 0; i < g.edges.size(); i++) {
			Edge e = g.edges.getEdge(i);
			if (e.isFeedback()) {
				e.invert();
			}
		}
	}
}
