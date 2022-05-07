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
package org.eclipse.gmf.examples.runtime.diagram.layout.graph;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.gmf.runtime.draw2d.ui.graph.BorderNode;
import org.eclipse.gmf.runtime.draw2d.ui.graph.ConstantSizeNode;
import org.eclipse.gmf.runtime.draw2d.ui.graph.ConstrainedEdge;
import org.eclipse.gmf.runtime.draw2d.ui.graph.GMFDirectedGraphLayout;

public class GMFGraphTests {
	
	public static DirectedGraph test1() {
		ConstantSizeNode hub, n1, n2, n3, n4, n5, n6, n7, n8, n9, n10;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		Node extra = new ConstantSizeNode("Extra"); //$NON-NLS-1$
		nodes.add(extra);
		
		nodes.add(hub = new ConstantSizeNode("Hub")); //$NON-NLS-1$
		nodes.add(n1 = new ConstantSizeNode("n1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("n2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("n3")); //$NON-NLS-1$
		nodes.add(n4 = new ConstantSizeNode("n4")); //$NON-NLS-1$
		nodes.add(n5 = new ConstantSizeNode("n5")); //$NON-NLS-1$
		nodes.add(n6 = new ConstantSizeNode("n6")); //$NON-NLS-1$
		nodes.add(n7 = new ConstantSizeNode("n7")); //$NON-NLS-1$
		nodes.add(n8 = new ConstantSizeNode("n8")); //$NON-NLS-1$
		nodes.add(n9 = new ConstantSizeNode("n9")); //$NON-NLS-1$
		nodes.add(n10 = new ConstantSizeNode("n10")); //$NON-NLS-1$
		
		hub.width = 400;
		hub.setMinIncomingPadding(20);
		hub.setMinOutgoingPadding(20);
		n1.setMinIncomingPadding(10);
		n2.setMinIncomingPadding(10);
		n3.setMinIncomingPadding(10);
		n4.setMinIncomingPadding(10);
		n1.width = n6.width = n10.width = n4.width = 100;
		n5.width = 180;
		
		n10.setMinOutgoingPadding(10);
		n5.setMinIncomingPadding(10);
		
		edges.add(new Edge(extra, n1));
		edges.add(new Edge(extra, n2));
		edges.add(new Edge(extra, n2));
		
//		edges.add(new Edge(n10, n5));
		
		edges.add(new ConstrainedEdge(hub, n1));
		edges.add(new ConstrainedEdge(hub, n2));
		edges.add(new ConstrainedEdge(hub, n3));
		edges.add(new ConstrainedEdge(hub, n4));
		edges.add(new ConstrainedEdge(hub, n5));
		edges.add(new ConstrainedEdge(n6, hub));
		edges.add(new ConstrainedEdge(n7, hub));
		edges.add(new ConstrainedEdge(n8, hub));
		edges.add(new ConstrainedEdge(n9, hub));
		edges.add(new ConstrainedEdge(n10, hub));
				
		DirectedGraph g = new DirectedGraph();
		g.nodes = nodes;
		g.edges = edges;
		
		new GMFDirectedGraphLayout().visit(g);
		
		return g;
		
	}
	
	public static DirectedGraph test2() {
		ConstantSizeNode head, n1, n2, n3, n4, n5, d1, d2, n6, n7, tail;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		nodes.add(head = new ConstantSizeNode("head")); //$NON-NLS-1$
		nodes.add(n1 = new ConstantSizeNode("n1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("n2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("n3")); //$NON-NLS-1$
		nodes.add(n4 = new ConstantSizeNode("n4")); //$NON-NLS-1$
		nodes.add(n5 = new ConstantSizeNode("n5")); //$NON-NLS-1$
		nodes.add(d1 = new ConstantSizeNode("d1")); //$NON-NLS-1$
		nodes.add(d2 = new ConstantSizeNode("d2")); //$NON-NLS-1$
		nodes.add(n6 = new ConstantSizeNode("n6")); //$NON-NLS-1$
		nodes.add(n7 = new ConstantSizeNode("n7")); //$NON-NLS-1$
		nodes.add(tail = new ConstantSizeNode("tail")); //$NON-NLS-1$
		
		head.width = tail.width = 20;
		head.height = tail.height = 250;
		
		edges.add(new Edge(head, n1));
		edges.add(new Edge(head, n2));
		edges.add(new Edge(head, n3));
		edges.add(new Edge(head, n4));
		edges.add(new Edge(head, n5));
		
		edges.add(new Edge(n1, d1));
		edges.add(new Edge(n2, tail));
		edges.add(new Edge(n3, d2));
		edges.add(new Edge(n4, tail));
		edges.add(new Edge(n5, tail));
		
		edges.add(new Edge(d1, n6));
		edges.add(new Edge(n6, tail));
		edges.add(new Edge(d1, tail));
		
		edges.add(new Edge(d2, n7));
		edges.add(new Edge(n7, tail));
		edges.add(new Edge(d2, tail));
		
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.getNode(i);
			if (n instanceof ConstantSizeNode) {
				ConstantSizeNode cn = (ConstantSizeNode) n;
				cn.setMinIncomingPadding(5);
				cn.setMinOutgoingPadding(5);
			}
		}
		
		DirectedGraph graph = new DirectedGraph();
		graph.nodes = nodes;
		graph.edges = edges;
		graph.setDirection(PositionConstants.WEST);
		
		new GMFDirectedGraphLayout().visit(graph);
		return graph;
	}
	
	public static DirectedGraph test3() {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		ConstantSizeNode hub, n1, n2, n3, n4, n5, n6, n7, n8, n9, n10;
		
		nodes.add(n1 = new ConstantSizeNode("n1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("n2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("n3")); //$NON-NLS-1$
		nodes.add(n4 = new ConstantSizeNode("n4")); //$NON-NLS-1$
		nodes.add(n5 = new ConstantSizeNode("n5")); //$NON-NLS-1$
		nodes.add(n6 = new ConstantSizeNode("n6")); //$NON-NLS-1$
		nodes.add(n7 = new ConstantSizeNode("n7")); //$NON-NLS-1$
		nodes.add(n8 = new ConstantSizeNode("n8")); //$NON-NLS-1$
		nodes.add(n9 = new ConstantSizeNode("n9")); //$NON-NLS-1$
		nodes.add(n10 = new ConstantSizeNode("n10")); //$NON-NLS-1$
		nodes.add(hub = new ConstantSizeNode("hub")); //$NON-NLS-1$
		
		hub.setMinIncomingPadding(10);
		hub.setMinOutgoingPadding(10);
		
		hub.width = 1500;
		hub.height = 500;
		
		//n1.width = n2.width = n3.width = n4.width = n5.width = n6.width = n7.width = n8.width = n9.width = n10.width = 300;
		
		
		BorderNode bn1 = new BorderNode("BN1", hub); //$NON-NLS-1$
		BorderNode bn2 = new BorderNode("BN2", hub); //$NON-NLS-1$
		BorderNode bn3 = new BorderNode("BN3", hub); //$NON-NLS-1$
		BorderNode bn4 = new BorderNode("BN4", hub); //$NON-NLS-1$
		BorderNode bn5 = new BorderNode("BN5", hub); //$NON-NLS-1$
		BorderNode bn6 = new BorderNode("BN6", hub); //$NON-NLS-1$
		BorderNode bn7 = new BorderNode("BN7", hub); //$NON-NLS-1$
		bn1.width = bn1.height = bn2.width = bn2.height = bn3.width = bn3.height = bn4.width = bn4.height = bn5.width = bn5.height = bn6.width = bn6.height = bn7.width = bn7.height = 50;
		
		bn7.setMinOutgoingPadding(5);
		bn6.setMinIncomingPadding(5);
		
		edges.add(new ConstrainedEdge(hub, n1));
		edges.add(new ConstrainedEdge(bn7, n2));
		edges.add(new ConstrainedEdge(hub, n3));
		edges.add(new ConstrainedEdge(hub, n4));
		edges.add(new ConstrainedEdge(bn7, n5));
		edges.add(new ConstrainedEdge(n6, bn6));
		edges.add(new ConstrainedEdge(n7, bn6));
		edges.add(new ConstrainedEdge(n8, hub));
		edges.add(new ConstrainedEdge(n9, hub));
		edges.add(new ConstrainedEdge(n10, bn6));
		
		DirectedGraph graph = new DirectedGraph();
		graph.nodes = nodes;
		graph.edges = edges;
		graph.setDefaultPadding(new Insets(50));
		
		new GMFDirectedGraphLayout().visit(graph);
		return graph;
	}

	public static DirectedGraph test2Orthogonal() {
		ConstantSizeNode head, n1, n2, n3, n4, n5, d1, d2, n6, n7, tail;
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		nodes.add(head = new ConstantSizeNode("head")); //$NON-NLS-1$
		nodes.add(n1 = new ConstantSizeNode("n1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("n2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("n3")); //$NON-NLS-1$
		nodes.add(n4 = new ConstantSizeNode("n4")); //$NON-NLS-1$
		nodes.add(n5 = new ConstantSizeNode("n5")); //$NON-NLS-1$
		nodes.add(d1 = new ConstantSizeNode("d1")); //$NON-NLS-1$
		nodes.add(d2 = new ConstantSizeNode("d2")); //$NON-NLS-1$
		nodes.add(n6 = new ConstantSizeNode("n6")); //$NON-NLS-1$
		nodes.add(n7 = new ConstantSizeNode("n7")); //$NON-NLS-1$
		nodes.add(tail = new ConstantSizeNode("tail")); //$NON-NLS-1$
		
		head.width = tail.width = 20;
		head.height = tail.height = 250;
		
		edges.add(new ConstrainedEdge(head, n1));
		edges.add(new ConstrainedEdge(head, n2));
		edges.add(new ConstrainedEdge(head, n3));
		edges.add(new ConstrainedEdge(head, n4));
		edges.add(new ConstrainedEdge(head, n5));
		
		edges.add(new ConstrainedEdge(n1, d1));
		edges.add(new ConstrainedEdge(n2, tail));
		edges.add(new ConstrainedEdge(n3, d2));
		edges.add(new ConstrainedEdge(n4, tail));
		edges.add(new ConstrainedEdge(n5, tail));
		
		edges.add(new ConstrainedEdge(d1, n6));
		edges.add(new ConstrainedEdge(n6, tail));
		edges.add(new ConstrainedEdge(d1, tail));
		
		edges.add(new ConstrainedEdge(d2, n7));
		edges.add(new ConstrainedEdge(n7, tail));
		edges.add(new ConstrainedEdge(d2, tail));
		
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.getNode(i);
			if (n instanceof ConstantSizeNode) {
				ConstantSizeNode cn = (ConstantSizeNode) n;
				cn.setMinIncomingPadding(5);
				cn.setMinOutgoingPadding(5);
			}
		}
		
		for (int i = 0; i < edges.size(); i++) {
			ConstrainedEdge ce = (ConstrainedEdge) edges.getEdge(i);
			ce.setStyle(ConstrainedEdge.ORTHOGONAL_ROUTING_STYLE);
		}
		
		DirectedGraph graph = new DirectedGraph();
		graph.nodes = nodes;
		graph.edges = edges;
		graph.setDirection(PositionConstants.WEST);
		
		new GMFDirectedGraphLayout().visit(graph);
		return graph;
	}
	
	public static DirectedGraph test4() {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		ConstantSizeNode n1, n2, n3, n4, hub;
		BorderNode bn1, bn2;
		
		nodes.add(n1 = new ConstantSizeNode("n1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("n2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("n3")); //$NON-NLS-1$
		nodes.add(n4 = new ConstantSizeNode("n4")); //$NON-NLS-1$
		nodes.add(hub = new ConstantSizeNode("hub")); //$NON-NLS-1$
		
		n1.width = n1.height = n2.width = n2.height = n3.width = n3.height = n4.width = n4.height = 100;
		hub.width = 150;
		hub.height = 150;
		
		hub.setPadding(new Insets(20, 50, 20, 50)); 
		
		bn1 = new BorderNode("bn1", hub); //$NON-NLS-1$
		bn1.width = bn1.height = 20;
		bn2 = new BorderNode("bn2", hub); //$NON-NLS-1$
		bn2.width = bn2.height = 35;
		bn2.setOutsideRatio(0.2f);
		
		bn2.setMinIncomingPadding(5);
		bn1.setMinOutgoingPadding(5);
		
		edges.add(new ConstrainedEdge(n1, bn1));
		edges.add(new ConstrainedEdge(n2, bn2));
		edges.add(new ConstrainedEdge(bn1, n3));
		edges.add(new ConstrainedEdge(bn2, n4));
		
		DirectedGraph g = new DirectedGraph();
		g.nodes = nodes;
		g.edges = edges;
		
		new GMFDirectedGraphLayout().visit(g);
		return g;
	}

	public static DirectedGraph test5() {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		ConstantSizeNode n1, n2;
		BorderNode bn1, bn2;
		
		nodes.add(n1 = new ConstantSizeNode("n1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("n2")); //$NON-NLS-1$
		
		n1.width = n2.width = 100;
		n1.height = n2.height = 30; 
		
		bn1 = new BorderNode("bn1", n1); //$NON-NLS-1$
		bn2 = new BorderNode("bn2", n2); //$NON-NLS-1$
		bn1.width = bn2.width = bn1.height = bn2.height = 10;
		bn2.setOutsideRatio(1f);
		bn1.setOutsideRatio(1f);
		
		bn2.setMinIncomingPadding(5);
		bn1.setMinOutgoingPadding(5);
		
		edges.add(new ConstrainedEdge(bn1, bn2));
		
		DirectedGraph g = new DirectedGraph();
		g.nodes = nodes;
		g.edges = edges;
		
		new GMFDirectedGraphLayout().visit(g);
		return g;
	}
	
	public static DirectedGraph variousHeights_Test1() {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		ConstantSizeNode n1, n2, n3, n4, n5, n6;
		
		nodes.add(n1 = new ConstantSizeNode("n1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("n2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("n3")); //$NON-NLS-1$
		nodes.add(n4 = new ConstantSizeNode("n4")); //$NON-NLS-1$
		nodes.add(n5 = new ConstantSizeNode("n5")); //$NON-NLS-1$
		nodes.add(n6 = new ConstantSizeNode("n6")); //$NON-NLS-1$
		
		n2.height = 200;
		n1.height = n3.height = 30;
		
		edges.add(new Edge(n1, n4));
		edges.add(new Edge(n1, n5));
		edges.add(new Edge(n1, n6));
		edges.add(new Edge(n2, n4));
		edges.add(new Edge(n2, n5));
		edges.add(new Edge(n2, n6));
		edges.add(new Edge(n3, n4));
		edges.add(new Edge(n3, n5));
		edges.add(new Edge(n3, n6));
		
		DirectedGraph g = new DirectedGraph();
		g.nodes = nodes;
		g.edges = edges;
		
		new GMFDirectedGraphLayout().visit(g);
		return g;
	}

	public static DirectedGraph variousHeights_Test2() {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		ConstantSizeNode n1, n2, n3, n4, n5, n6;
		
		nodes.add(n1 = new ConstantSizeNode("n1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("n2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("n3")); //$NON-NLS-1$
		nodes.add(n4 = new ConstantSizeNode("n4")); //$NON-NLS-1$
		nodes.add(n5 = new ConstantSizeNode("n5")); //$NON-NLS-1$
		nodes.add(n6 = new ConstantSizeNode("n6")); //$NON-NLS-1$
		
		n1.setMinOutgoingPadding(5);
		n2.setMinOutgoingPadding(5);
		n3.setMinOutgoingPadding(5);
		
		n4.setMinIncomingPadding(5);
		n5.setMinIncomingPadding(5);
		n6.setMinIncomingPadding(5);
		
		n2.height = 150;
		n1.height = n3.height = 30;
		
		edges.add(new Edge(n1, n4));
		edges.add(new Edge(n1, n5));
		edges.add(new Edge(n1, n6));
		edges.add(new Edge(n2, n4));
		edges.add(new Edge(n2, n5));
		edges.add(new Edge(n2, n6));
		edges.add(new Edge(n3, n4));
		edges.add(new Edge(n3, n5));
		edges.add(new Edge(n3, n6));
		
		DirectedGraph g = new DirectedGraph();
		g.nodes = nodes;
		g.edges = edges;
		
		new GMFDirectedGraphLayout().visit(g);
		return g;
	}
	
	public static DirectedGraph composite_test1() {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		ConstantSizeNode n1, n2, n3, n4;
		BorderNode bn1, bn2, bn3, bn4;
		Edge e1, e2, e3, e4;
		
		nodes.add(n1 = new ConstantSizeNode("Node1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("Node2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("Node3")); //$NON-NLS-1$
		nodes.add(n4 = new ConstantSizeNode("Node4")); //$NON-NLS-1$
		
		bn1 = new BorderNode("bn1", n1); //$NON-NLS-1$
		bn2 = new BorderNode("bn2", n2); //$NON-NLS-1$
		bn3 = new BorderNode("bn3", n3); //$NON-NLS-1$
		bn4 = new BorderNode("bn4", n4); //$NON-NLS-1$
		
		bn1.width = bn2.width = bn3.width = bn4.width = bn1.height = bn2.height = bn3.height = bn4.height = 10;
		
		bn1.setMinIncomingPadding(5);
		bn2.setMinIncomingPadding(5);
		bn3.setMinIncomingPadding(5);
		bn4.setMinIncomingPadding(5);
		n1.setMinIncomingPadding(5);
		n2.setMinIncomingPadding(5);
		n3.setMinIncomingPadding(5);
		n4.setMinIncomingPadding(5);
		
		bn1.setMinOutgoingPadding(5);
		bn2.setMinOutgoingPadding(5);
		bn3.setMinOutgoingPadding(5);
		bn4.setMinOutgoingPadding(5);
		n1.setMinOutgoingPadding(5);
		n2.setMinOutgoingPadding(5);
		n3.setMinOutgoingPadding(5);
		n4.setMinOutgoingPadding(5);

		edges.add(e1 = new ConstrainedEdge(bn1, bn2));
		edges.add(e2 = new ConstrainedEdge(bn1, n3));
		edges.add(e3 = new ConstrainedEdge(bn2, bn3));
		edges.add(e4 = new ConstrainedEdge(bn4, bn3));
		
		e1.setPadding(20);
		e2.setPadding(20);
		e3.setPadding(20);
		e4.setPadding(20);
		
		n1.width = n3.width = 50;
		n3.height = n3.height = 20;
		
		n2.height = 200;
		n2.width = 150;
		
		n4.width = 120;
		n4.height = 120;
		
		DirectedGraph g = new DirectedGraph();
		g.nodes = nodes;
		g.edges = edges;
		
		g.setDefaultPadding(new Insets(20));
		
		new GMFDirectedGraphLayout().visit(g);
		
		return g;
	}
	
	public static DirectedGraph cycle_test() {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		ConstantSizeNode n1, n2, n3;
		Edge e1, e2, e3;
		
		nodes.add(n1 = new ConstantSizeNode("Node1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("Node2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("Node3")); //$NON-NLS-1$
		
		n1.setMinIncomingPadding(5);
		n2.setMinIncomingPadding(5);
		n3.setMinIncomingPadding(5);
		
		n1.setMinOutgoingPadding(5);
		n2.setMinOutgoingPadding(5);
		n3.setMinOutgoingPadding(5);

		edges.add(e1 = new ConstrainedEdge(n1, n2));
		edges.add(e2 = new ConstrainedEdge(n2, n3));
		edges.add(e3 = new ConstrainedEdge(n3, n1));		
		e1.setPadding(20);
		e2.setPadding(20);
		e3.setPadding(20);
		
		DirectedGraph g = new DirectedGraph();
		g.nodes = nodes;
		g.edges = edges;
		
		g.setDefaultPadding(new Insets(20));
		
		new GMFDirectedGraphLayout().visit(g);
		
		return g;
	}
	
	public static DirectedGraph cycle_BorderNodes_test() {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		ConstantSizeNode n1, n2, n3;
		BorderNode bn1, bn2, bn3, bn4, bn5, bn6;
		Edge e1, e2, e3;
		
		nodes.add(n1 = new ConstantSizeNode("Node1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("Node2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("Node3")); //$NON-NLS-1$
		bn1 = new BorderNode("bn1", n1); //$NON-NLS-1$
		bn2 = new BorderNode("bn2", n1); //$NON-NLS-1$
		bn3 = new BorderNode("bn3", n2); //$NON-NLS-1$
		bn4 = new BorderNode("bn4", n2); //$NON-NLS-1$
		bn5 = new BorderNode("bn5", n3); //$NON-NLS-1$
		bn6 = new BorderNode("bn6", n3); //$NON-NLS-1$
		
		n1.setMinIncomingPadding(5);
		n2.setMinIncomingPadding(5);
		n3.setMinIncomingPadding(5);
		n1.setMinOutgoingPadding(5);
		n2.setMinOutgoingPadding(5);
		n3.setMinOutgoingPadding(5);

		edges.add(e1 = new ConstrainedEdge(bn1, bn3));
		edges.add(e2 = new ConstrainedEdge(bn4, bn5));
		edges.add(e3 = new ConstrainedEdge(bn6, bn2));		
		e1.setPadding(20);
		e2.setPadding(20);
		e3.setPadding(20);
		
		DirectedGraph g = new DirectedGraph();
		g.nodes = nodes;
		g.edges = edges;
		
		g.setDefaultPadding(new Insets(20));
		
		new GMFDirectedGraphLayout().visit(g);
		
		return g;
	}

	public static DirectedGraph cycle_BorderNodes_test2() {
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		ConstantSizeNode n1, n2, n3;
		BorderNode bn1, bn2, bn3;
		Edge e1, e2, e3;
		
		nodes.add(n1 = new ConstantSizeNode("Node1")); //$NON-NLS-1$
		nodes.add(n2 = new ConstantSizeNode("Node2")); //$NON-NLS-1$
		nodes.add(n3 = new ConstantSizeNode("Node3")); //$NON-NLS-1$
		bn1 = new BorderNode("bn1", n1); //$NON-NLS-1$
		bn2 = new BorderNode("bn2", n2); //$NON-NLS-1$
		bn3 = new BorderNode("bn3", n3); //$NON-NLS-1$
		
		n1.setMinIncomingPadding(5);
		n2.setMinIncomingPadding(5);
		n3.setMinIncomingPadding(5);
		n1.setMinOutgoingPadding(5);
		n2.setMinOutgoingPadding(5);
		n3.setMinOutgoingPadding(5);

		edges.add(e1 = new ConstrainedEdge(bn1, bn2));
		edges.add(e2 = new ConstrainedEdge(bn2, bn3));
		edges.add(e3 = new ConstrainedEdge(bn3, bn1));		
		e1.setPadding(20);
		e2.setPadding(20);
		e3.setPadding(20);
		
		DirectedGraph g = new DirectedGraph();
		g.nodes = nodes;
		g.edges = edges;
		
		g.setDefaultPadding(new Insets(20));
		
		new GMFDirectedGraphLayout().visit(g);
		
		return g;
	}
}
