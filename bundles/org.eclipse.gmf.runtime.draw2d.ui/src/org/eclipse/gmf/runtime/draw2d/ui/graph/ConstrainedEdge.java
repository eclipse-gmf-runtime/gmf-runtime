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

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;

/**
 * Implementation of Edge that:
 * <li> Can connect border node(s)
 * <li> Can be routed obliquely or orthogonally
 * 
 * @author aboyko
 * @since 2.1
 */
public class ConstrainedEdge extends Edge {
	
	public static String DEFAULT_ROUTING_STYLE = "Default"; //$NON-NLS-1$
	public static String ORTHOGONAL_ROUTING_STYLE = "Orthogonal"; //$NON-NLS-1$
	
	private String style = DEFAULT_ROUTING_STYLE;
	
	BorderNode sourceConstraint = null;
	BorderNode targetConstraint = null;
	
	PointList startingRoutedPoints = new PointList();
	PointList endingRoutedPoints = new PointList();

	/**
	 * Constructs a new edge with the given source and target nodes.  All other fields will
	 * have their default values.
	 * @param source the source Node
	 * @param target the target Node
	 * @since 2.1
	 */
	public ConstrainedEdge(Node source, Node target) {
		this(null, source, target);
	}
	
	/**
	 * Constructs a new edge with the given source, target, delta, and weight.
	 * @param source the source Node
	 * @param target the target Node
	 * @param delta the minimum edge span
	 * @param weight the weight hint
	 * @since 2.1
	 */
	public ConstrainedEdge(Node source, Node target, int delta, int weight) {
		this(source, target);
		this.delta = delta;
		this.weight = weight;
	}

	/**
	 * Constructs a new edge with the given data object, source, and target node.
	 * @param data an arbitrary data object
	 * @param source the source node
	 * @param target the target node
	 * @since 2.1
	 */
	public ConstrainedEdge(Object data, Node source, Node target) {
		super(data, source, target);
		if (source instanceof BorderNode) {
			sourceConstraint = (BorderNode)source;
			this.source = sourceConstraint.borderNodeParent;
			this.source.outgoing.add(this);
			sourceConstraint.addOutgoingEdge(this);
		}
		if (target instanceof BorderNode) {
			targetConstraint = (BorderNode)target;
			this.target = targetConstraint.borderNodeParent;
			this.target.incoming.add(this);
			targetConstraint.addIncomingEdge(this);
		}
		if (this.source.equals(this.target)) {
			throw new RuntimeException("Edges between border nodes on the same parent or border node and its parent are disallowed."); //$NON-NLS-1$
		}
	}

	/**
	 * Gets the routing style for the edge 
	 * @return the style constant
	 * @since 2.1
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Sets the routing style for the edge (orthogonal or default)
	 * @param style the style constant
	 * @since 2.1
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.graph.Edge#invert()
	 */
	public void invert() {
		super.invert();
		
		if (sourceConstraint != null) {
			sourceConstraint.outgoingJointEdges.edges.remove(this);
		}
		if (targetConstraint != null) {
			targetConstraint.incomingJointEdges.edges.remove(this);
		}
		
		BorderNode temp = sourceConstraint;
		sourceConstraint = targetConstraint;
		targetConstraint = temp;
		
		if (sourceConstraint != null) {
			sourceConstraint.outgoingJointEdges.edges.add(this);
		}
		if (targetConstraint != null) {
			targetConstraint.incomingJointEdges.edges.add(this);
		}
	}

}
