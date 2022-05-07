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

import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;

/**
 * A set of outgoing edges that is treated as one outgoing edge.
 * Used internally for laying out connected border nodes.
 * 
 * @author aboyko
 * @since 2.1
 */
public class JointOutgoingEdges extends Edge implements JointEdges {
	
	private BorderNode joint;

	EdgeList edges = new EdgeList();
	
	private Edge leadingEdge = null;
	private int leadingCost = Integer.MAX_VALUE;;
	
	public JointOutgoingEdges(BorderNode joint) {
		super(new Node(), new Node());
		this.joint = joint;
		source = target = joint.borderNodeParent;
	}
	
	public Edge getLeadingEdge() {
		if (leadingEdge == null) {
			for (int i = 0; i < edges.size(); i++) {
				Edge e = edges.getEdge(i);
				int cost = getBendpointX(e) - e.source.x - e.source.getOffsetOutgoing(); 
				if (Math.abs(cost) < Math.abs(leadingCost)) {
					leadingCost = cost;
					leadingEdge = e;
				}
			}
		}
		return leadingEdge;
	}
		
	public BorderNode getJoint() {
		return joint;
	}
	
	private int getBendpointX(Edge e) {
		Node node = e.vNodes == null ? e.target : e.vNodes.getNode(0);
		return node.x + node.getOffsetIncoming();
	}
	
}
