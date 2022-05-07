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

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;

/**
 * Cleans up data associated with border nodes. Edges, at least one of the ends of which is a border node
 * have border nodes replaced by their parents during the layout. This graph modification procedure assigns
 * border node(s) back to edges source and/or targets where appropriate.
 * 
 * @author aboyko
 * @since 2.1
 */
class CleanupBorderNodeEdges {
	
	private DirectedGraph g;
	
	public CleanupBorderNodeEdges(DirectedGraph g) {
		this.g = g;
	}
	
	void cleanup() {
		for (int i = 0; i < g.edges.size(); i++) {
			Edge e = g.edges.getEdge(i);
			if (e instanceof ConstrainedEdge) {
				ConstrainedEdge ce = (ConstrainedEdge) e;
				if (ce.sourceConstraint != null) {
					ce.source = ce.sourceConstraint;
				}
				if (ce.targetConstraint != null) {
					ce.target = ce.targetConstraint;
				}
			}
		}
	}

}
