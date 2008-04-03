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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Subgraph;

/**
 * The node the size of which will not be modified by the layout algorithm. The node also has the support for:
 * <li> Border nodes
 * <li> Padding of incomimg and outgoing edges end points
 * 
 * @author aboyko
 * @since 2.1
 */
public class ConstantSizeNode extends Node {
	
	int minIncomingPadding = 0;
	int minOutgoingPadding = 0;
	
	int constantWidth;
	int constantHeight;
	
	/**
	 * List of border nodes owned by this node. The list should be modified manually.
	 * Use the border node constructor instead - it will updates parent's list of border nodes automatically.
	 * @since 2.1
	 */
	public List<BorderNode> borderNodes = new ArrayList<BorderNode>();

	/**
	 * Constructs the instance of the class
	 * @since 2.1
	 */
	public ConstantSizeNode() {
		super();
	}

	/**
	 * Constructs a node with the given data object
	 * @param data an arbitrary data object
	 * @since 2.1
	 */
	public ConstantSizeNode(Object data) {
		super(data);
	}
	
	/**
	 * Constructs a node attached to the parent subgraph  
	 * @param data data
	 * @param parent subgraph parent
	 * @since 2.1
	 */
	public ConstantSizeNode(Object data, Subgraph parent) {
		super(data, parent);
	}

	/**
	 * Gets the minimal padding between the end points of incoming edges
	 * @return the padding value
	 * @since 2.1
	 */
	public int getMinIncomingPadding() {
		return minIncomingPadding;
	}

	/**
	 * Sets the minimal padding between the end points of incoming edges.
	 * @param minIncomingPadding
	 * @since 2.1
	 */
	public void setMinIncomingPadding(int minIncomingPadding) {
		this.minIncomingPadding = minIncomingPadding;
	}

	/**
	 * Gets the minimal padding between the start points of outgoing edges
	 * @return the padding
	 * @since 2.1
	 */
	public int getMinOutgoingPadding() {
		return minOutgoingPadding;
	}

	/**
	 * Sets the minimal padding between the start points of outgoing edges
	 * @param minOutgoingPadding
	 * @since 2.1
	 */
	public void setMinOutgoingPadding(int minOutgoingPadding) {
		this.minOutgoingPadding = minOutgoingPadding;
	}
	
}
