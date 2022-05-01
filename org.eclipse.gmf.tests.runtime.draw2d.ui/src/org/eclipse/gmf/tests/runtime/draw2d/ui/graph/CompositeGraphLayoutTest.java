/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.draw2d.ui.graph;

import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graph.CompositeDirectedGraphLayout;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * @author mmostafa
 *
 */
public class CompositeGraphLayoutTest
    extends TestCase {
    
    public static Test suite() {
        return new TestSuite(CompositeGraphLayoutTest.class);
    }
    
    public void testVirtualNodeCreation(){
        // in this case the order of creating the nodes is very important
        // so do not change the order in wich we add nodes/edges to the 
        // edge/node list
        Node n1,n2,n3,n4;
        NodeList nodes = new NodeList();
        EdgeList edges = new EdgeList();
        
        nodes.add(n2 = new Node("N2")); //$NON-NLS-1$
        nodes.add(n1 = new Node("N1")); //$NON-NLS-1$
        nodes.add(n3 = new Node("N3")); //$NON-NLS-1$
        nodes.add(n4 = new Node("N4")); //$NON-NLS-1$
        
        edges.add(new Edge(n1,n4));
        edges.add(new Edge(n2,n3));
        edges.add(new Edge(n1,n2));
        edges.add(new Edge(n3,n1));
        
        int X = n1.x;
        
        CompoundDirectedGraph g = new CompoundDirectedGraph();
        g.nodes = nodes;
        g.edges = edges;
        
        CompositeDirectedGraphLayout layout = new CompositeDirectedGraphLayout();
        layout.visit(g);
        assertTrue(n1.x!=X);
    }
    
    public void testVirtualEdgeCreation(){
        Node n1,n2;
        Subgraph sg1;
        NodeList nodes = new NodeList();
        EdgeList edges = new EdgeList();
        
        nodes.add(sg1 = new Subgraph("SubGraph1")); //$NON-NLS-1$
        nodes.add(n1 = new Node("N1",sg1)); //$NON-NLS-1$
        nodes.add(n2 = new Node("N3")); //$NON-NLS-1$
        edges.add(new Edge(n1,n2));
        CompoundDirectedGraph g = new CompoundDirectedGraph();
        int X= n1.x;
        g.nodes = nodes;
        g.edges = edges;
        CompositeDirectedGraphLayout layout = new CompositeDirectedGraphLayout();
        layout.visit(g);
        assertTrue(n1.x!=X);
    }
    
    
}
