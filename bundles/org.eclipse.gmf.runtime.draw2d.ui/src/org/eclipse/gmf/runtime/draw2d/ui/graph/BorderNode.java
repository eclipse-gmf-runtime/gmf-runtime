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
import org.eclipse.draw2d.geometry.Point;

/**
 * Implementation of node that will be laid out by <code>GMFDirectedGraphLayout</code> as a node
 * attached to the border of it's parent node.
 * 
 * @author aboyko
 * @since 2.1
 */
public class BorderNode extends ConstantSizeNode {
	
	/**
	 * Describes how the border node is attached to the parent node. The outside
	 * ratio is a value between 0 and 1 representing the percentage of the
	 * border item that appears outside of the parent node.
	 */
	private float outsideRatio = 0.5f;
	
	JointIncomingEdges incomingJointEdges = new JointIncomingEdges(this);
	JointOutgoingEdges outgoingJointEdges = new JointOutgoingEdges(this);
	
	ConstantSizeNode borderNodeParent;
	
	int position = PositionConstants.NONE;
	
	/**
	 * Creates an instance of a border node given its data and parent
	 * @param data data
	 * @param parentNode the parent node of the border node
	 */
	public BorderNode(Object data, ConstantSizeNode parentNode) {
		super(data);
		this.borderNodeParent = parentNode;
		borderNodeParent.borderNodes.add(this);
		incomingJointEdges.target = borderNodeParent;
		outgoingJointEdges.source = borderNodeParent;
		width = 10;
		height = 10;
	}
	
	void setPoint(Point p) {
		if (p.x == borderNodeParent.x) {
			position = PositionConstants.WEST;
			x =  p.x - (int) (width * outsideRatio);
			y = p.y - height / 2;
		} else if (p.y == borderNodeParent.y) {
			position = PositionConstants.NORTH;
			x = p.x - width  / 2;
			y = p.y - (int) (height * outsideRatio);
		} else if (p.x == borderNodeParent.x + borderNodeParent.width) {
			position = PositionConstants.EAST;
			x =  p.x - (int) (width * (1 - outsideRatio));
			y = p.y - height / 2;
		} else {
			position = PositionConstants.SOUTH;
			x = p.x - width  / 2;
			y = p.y - (int) (height *  (1 - outsideRatio));
		}
	}

	void addIncomingEdge(ConstrainedEdge e) {
		incomingJointEdges.edges.add(e);
		e.target = borderNodeParent; 
	}
	
	void addOutgoingEdge(ConstrainedEdge e) {
		outgoingJointEdges.edges.add(e);
		e.source = borderNodeParent;
	}
	
	Point getEdgesDefaultEndPoint() {
		if (position == PositionConstants.WEST) {
			return new Point(x, y + height / 2);
		} else if (position == PositionConstants.NORTH) {
			return new Point(x + width / 2, y);
		} else if (position == PositionConstants.EAST) {
			return new Point(x + width, y + height / 2);
		} else {
			return new Point(x + width / 2, y + height);
		}
	}

	/**
	 * Gets the outside of parent ratio value. The outside ratio is a value
	 * between 0 and 1 representing the percentage of the border item that
	 * appears outside of the parent node.
	 * 
	 * @return the value
	 * @since 2.1
	 */
	public float getOutsideRatio() {
		return outsideRatio;
	}

	/**
	 * Sets the outside parent ratio value. The outside ratio is a value between
	 * 0 and 1 representing the percentage of the border item that appears
	 * outside of the parent node.
	 * 
	 * @param outsideRatio
	 * @since 2.1
	 */
	public void setOutsideRatio(float outsideRatio) {
		if (outsideRatio < 0f || outsideRatio > 1f) {
			throw new IllegalArgumentException("Ratio must be between 0 and 1 inclusively"); //$NON-NLS-1$
		}
		this.outsideRatio = outsideRatio;
	}
}
