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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.gmf.runtime.draw2d.ui.graph.BorderNode;
import org.eclipse.gmf.runtime.draw2d.ui.graph.ConstantSizeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Implementation of a common part of the graph layout demo application. Mainly, it's
 * copied from the demo from GEF with some minor differences:
 * <li> Border nodes support
 * <li> Non-applicable things removed 
 * 
 * @author aboyko
 *
 */
public abstract class AbstractGMFGraphDemo {

	/** Contents of the demo */
	protected IFigure contents;
	/** Name of graph test method to run */
	protected static String graphMethod;
	/** Demo shell */
	protected Shell shell;

	private FigureCanvas fc;

	/**
	 * Builds a figure for the given edge and adds it to contents
	 * @param contents the parent figure to add the edge to
	 * @param edge the edge
	 */
	static void buildEdgeFigure(Figure contents, Edge edge) {
		PolylineConnection conn = connection(edge);
		conn.setForegroundColor(ColorConstants.gray);
		PolygonDecoration dec = new PolygonDecoration();
		conn.setTargetDecoration(dec);
		conn.setPoints(edge.getPoints());
		contents.add(conn);
	}

	/**
	 * Builds a Figure for the given node and adds it to contents
	 * @param contents the parent Figure to add the node to
	 * @param node the node to add
	 */
	static void buildNodeFigure(Figure contents, Node node) {
		Label label;
		label = new Label();
		label.setBackgroundColor(ColorConstants.lightGray);
		label.setOpaque(true);
		label.setBorder(new LineBorder());
		if (node.incoming.isEmpty())
			label.setBorder(new LineBorder(2));
		String text = node.data.toString();
		label.setText(text);
		node.data = label;
		contents.add(label, new Rectangle(node.x, node.y, node.width, node.height));
		
		if (node instanceof ConstantSizeNode) {
			ConstantSizeNode cn = (ConstantSizeNode) node;
			for (int i = 0; i < cn.borderNodes.size(); i++) {
				buildBorderNodeFigure(contents, cn.borderNodes.get(i));
			}
		}
	}

	static void buildBorderNodeFigure(Figure contents, BorderNode node) {
		Label label;
		label = new Label();
		label.setBackgroundColor(ColorConstants.lightGray);
		label.setOpaque(true);
		label.setBorder(new LineBorder());
		String text = node.data.toString();// + "(" + node.index +","+node.sortValue+ ")";
		label.setText(text);
		node.data = label;
		contents.add(label, new Rectangle(node.x, node.y, node.width, node.height));
	}
	
	/**
	 * Builds a connection for the given edge
	 * @param e the edge
	 * @return the connection
	 */
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

	/**
	 * @see org.eclipse.graph.AbstractExample#getContents()
	 */
	protected IFigure getContents() {
		return null;
	}

	/**
	 * Returns the FigureCanvas
	 * @return this demo's FigureCanvas
	 */
	protected FigureCanvas getFigureCanvas() {
		return fc;
	}

	/**
	 * Returns an array of strings that represent the names of the methods which build
	 * graphs for this graph demo
	 * @return array of graph building method names 
	 */
	protected abstract String[] getGraphMethods();


	/**
	 * @see org.eclipse.graph.AbstractExample#hookShell()
	 */
	protected void hookShell() {
		Composite composite = new Composite(shell, 0);
		composite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		
		composite.setLayout(new GridLayout());	
		final org.eclipse.swt.widgets.Label nodesLabel 
				= new org.eclipse.swt.widgets.Label(composite, SWT.NONE);
		nodesLabel.setText("Graph");
		final Combo graphList = new Combo(composite, SWT.DROP_DOWN);
		
		String[] graphMethods = getGraphMethods();
		for (int i = 0; i < graphMethods.length; i++) {
			if (graphMethods[i] != null)
				graphList.add(graphMethods[i]);
		}
		setGraphMethod(graphMethods[0]);
		graphList.setText(graphMethod);
		graphList.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				setGraphMethod(graphList.getItem(graphList.getSelectionIndex()));
				getFigureCanvas().setContents(getContents());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				graphList.setText(graphMethod);
			}
		});		
	}

	/**
	 * Runs the demo.
	 */
	protected void run() {
		Display d = Display.getDefault();
		shell = new Shell(d);
		String appName = getClass().getName();
		appName = appName.substring(appName.lastIndexOf('.') + 1);
		hookShell();
		shell.setText(appName);
		shell.setLayout(new GridLayout(2, false));
		setFigureCanvas(new FigureCanvas(shell));
		getFigureCanvas().setContents(contents = getContents());
		getFigureCanvas().getViewport().setContentsTracksHeight(true);
		getFigureCanvas().getViewport().setContentsTracksWidth(true);
		getFigureCanvas().setLayoutData(new GridData(GridData.FILL_BOTH));
		shell.setSize(1100, 700);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}

	/**
	 * Sets this demo's FigureCanvas
	 * @param canvas this demo's FigureCanvas
	 */
	protected void setFigureCanvas(FigureCanvas canvas) {
		this.fc = canvas;
	}

	/**
	 * Sets the name of the method to call to build the graph
	 * @param method name of the method used to build the graph
	 */
	public static void setGraphMethod(String method) {
		graphMethod = method;
	}

}
