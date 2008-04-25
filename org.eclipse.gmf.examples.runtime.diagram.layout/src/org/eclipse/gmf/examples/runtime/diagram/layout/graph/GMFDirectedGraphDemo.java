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
package org.eclipse.gmf.examples.runtime.diagram.layout.graph;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;

public class GMFDirectedGraphDemo extends AbstractGMFGraphDemo {

	protected String[] getGraphMethods() {
		Method[] methods = GMFGraphTests.class.getMethods();
		String[] methodNames = new String[methods.length];
		
		int nameIndex = 0;
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getReturnType().equals(DirectedGraph.class)) {
				methodNames[nameIndex] = methods[i].getName();
				nameIndex++;
			}
		}
		return methodNames;
	}

	protected IFigure getContents() {
		DirectedGraph graph = null;
		try {
			graph =
				(DirectedGraph) (GMFGraphTests
					.class
					.getMethod(graphMethod, null)
					.invoke(null, null));
		} catch (Exception e) {
			System.out.println("Could not build graph"); //$NON-NLS-1$
			e.printStackTrace();
		}
		Figure contents = buildGraph(graph);
		return contents;
	}

	public static void main(String[] args) {
		new GMFDirectedGraphDemo().run();
	}
	
	/**
	 * Builds the graph, creates Draw2d figures for all graph components.
	 * @param graph the graph to build
	 * @return the Figure representing the graph
	 */
	public static Figure buildGraph(DirectedGraph graph) {
		Figure contents = new Panel();
		contents.setBackgroundColor(ColorConstants.white);
		contents.setLayoutManager(new XYLayout());
		
		for (int i = 0; i < graph.nodes.size(); i++) {
			Node node = graph.nodes.getNode(i);
			buildNodeFigure(contents, node);
		}
		
		for (int i = 0; i < graph.edges.size(); i++) {
			Edge edge = graph.edges.getEdge(i);
			buildEdgeFigure(contents, edge);
		}
		return contents;
	}
		
	static void buildEdgeFigure(Figure contents, Edge edge) {
		PolylineConnection conn = connection(edge);
		conn.setForegroundColor(ColorConstants.gray);
		PolygonDecoration dec = new PolygonDecoration();
		conn.setTargetDecoration(dec);
		conn.setPoints(edge.getPoints());
		contents.add(conn);
	}
	
	static PolylineConnection connection(Edge e) {
		PolylineConnection conn = new PolylineConnection();
		conn.setConnectionRouter(new BendpointConnectionRouter());
		List bends = new ArrayList();
		NodeList nodes = e.vNodes;
		if (nodes != null) {
			for (int i = 0; i < nodes.size(); i++) {
				Node n = nodes.getNode(i);
				int x = n.x;
				int y = n.y;
				bends.add(new AbsoluteBendpoint(x, y));
				bends.add(new AbsoluteBendpoint(x, y + n.height));
			}
		}
		conn.setRoutingConstraint(bends);
		return conn;
	}
}
