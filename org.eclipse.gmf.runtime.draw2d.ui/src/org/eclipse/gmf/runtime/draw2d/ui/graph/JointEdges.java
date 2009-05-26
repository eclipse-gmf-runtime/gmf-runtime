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
