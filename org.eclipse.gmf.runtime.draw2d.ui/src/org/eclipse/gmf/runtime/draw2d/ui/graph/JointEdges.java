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

/**
 * Interface for joint edges. Joint edges  is a set of edges that acts like one edge
 * 
 * @author aboyko
 * @since 2.1
 */
interface JointEdges {
	
	public Edge getLeadingEdge();
	
	public BorderNode getJoint();
	
}
