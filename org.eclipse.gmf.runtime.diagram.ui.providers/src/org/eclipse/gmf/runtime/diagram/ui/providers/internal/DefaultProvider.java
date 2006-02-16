/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DiagramActionsDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutNodesOperation;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.SetAllBendpointRequest;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;

/**
 * Provider that creates a command for the DirectedGraph layout in GEF.
 * 
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 * 
 */
public abstract class DefaultProvider
	extends AbstractLayoutEditPartProvider {

	// Minimum sep between icon and bottommost horizontal arc
	protected int minX = -1;
	protected int minY = -1;
    private int layoutDefaultMargin = 0;
    private IMapMode mm;
	
	private static final int NODE_PADDING = 30;
	private static final int MIN_EDGE_PADDING = 5;
	private static final int MAX_EDGE_PADDING = NODE_PADDING * 3;

	
	/**
	 * @return the <code>IMapMode</code> that maps coordinates from
	 * device to logical and vice-versa.
	 */
	protected IMapMode getMapMode() {
		return mm;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		Assert.isNotNull(operation);

		View cview = getContainer(operation);
		if (cview == null)
			return false;

		IAdaptable layoutHint = ((LayoutNodesOperation) operation)
			.getLayoutHint();
		String layoutType = (String) layoutHint.getAdapter(String.class);
		return LayoutType.DEFAULT.equals(layoutType);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider#layoutEditParts(org.eclipse.gef.GraphicalEditPart, org.eclipse.core.runtime.IAdaptable)
	 */
	public Command layoutEditParts(GraphicalEditPart containerEditPart,
			IAdaptable layoutHint) {

		mm = MapModeUtil.getMapMode(containerEditPart.getFigure());
		if (containerEditPart == null) {
			InvalidParameterException ipe = new InvalidParameterException();
			Trace.throwing(DiagramProvidersPlugin.getInstance(),
				DiagramActionsDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"layout()", //$NON-NLS-1$
				ipe);
			throw ipe;
		}

		// setup graph
		DirectedGraph g = new DirectedGraph();
		build_graph(g, containerEditPart.getChildren());
		new DirectedGraphLayout().visit(g);
		// update the diagram based on the graph
		Command cmd = update_diagram(containerEditPart, g, false);
		
		// reset mm mapmode to avoid memory leak
		mm = null;
		return cmd;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider#layoutEditParts(java.util.List, org.eclipse.core.runtime.IAdaptable)
	 */
	public Command layoutEditParts(List selectedObjects, IAdaptable layoutHint) {

		if (selectedObjects.size() == 0) {
			return null;
		}

		// get the container edit part for the children
		GraphicalEditPart editPart = (GraphicalEditPart) selectedObjects.get(0);
		GraphicalEditPart containerEditPart = (GraphicalEditPart) editPart
			.getParent();

		mm = MapModeUtil.getMapMode(containerEditPart.getFigure());
		
		DirectedGraph g = new DirectedGraph();
		build_graph(g, selectedObjects);
		new DirectedGraphLayout().visit(g);
		// update the diagram based on the graph
		Command cmd = update_diagram(containerEditPart, g, true);
		
		// reset mm mapmode to avoid memory leak
		mm = null;
		return cmd;
	}

	/**
	 * layoutTopDown Utility function that is commonly subclasses by domain
	 * specific layouts to determine whether a specific connection type is layed
	 * out in a top down manner.
	 * 
	 * @param poly
	 *            <code>ConnectionEditPart</code> to determine whether it is to be layed
	 *            out in a top-down fashion.
	 * @return true if connection is to be layed out top-down, false otherwise.
	 */
	protected boolean layoutTopDown(ConnectionEditPart poly) {
		return false;
	}

	/**
	 * build_nodes Method to build up the nodes in the temporary Graph structure
	 * which the algorithm is executed on.
	 * 
	 * @param selectedObjects
	 *            List of selected objects to be layed out.
	 * @param editPartToNodeDict
	 *            Map of editparts from the GEF to the temporary Nodes used for
	 *            layout.
	 * @return NodeList list of Nodes that is passed to the graph structure.
	 */
	private NodeList build_nodes(List selectedObjects, Map editPartToNodeDict) {
		ListIterator li = selectedObjects.listIterator();

		NodeList nodes = new NodeList();

		while (li.hasNext()) {

			IGraphicalEditPart gep = (IGraphicalEditPart) li.next();
			if (gep instanceof ShapeEditPart) {

				ShapeEditPart shapeEP = (ShapeEditPart) gep;

				Point position = shapeEP.getLocation();

				// determine topleft most point, layout of items will be placed
				// starting at topleft point
				if (minX == -1) {
					minX = position.x;
					minY = position.y;
				} else {
					minX = Math.min(minX, position.x);
					minY = Math.min(minY, position.y);
				}

				Node n = new Node(shapeEP);
				n.setPadding(new Insets(NODE_PADDING));
				Dimension size = shapeEP.getSize();

				setNodeMetrics(n, new Rectangle(position.x, position.y,
					size.width, size.height));

				editPartToNodeDict.put(shapeEP, n);
				nodes.add(n);
			}
		}

		return nodes;
	}

	/**
	 * setNodeMetrics Sets the extend and position into the node object. Defined
	 * as abstract to allow subclasses to implement to perform a transformation
	 * on the values stored in the node. i.e. support for Left-Right layout as
	 * opposed to Top-Down.
	 * 
	 * @param n
	 *            Node that will receive the metrics values to be set.
	 * @param r
	 *            Rectangle that represents the location and extend of the Node.
	 */
	final protected void setNodeMetrics(Node n, Rectangle r) {
		Rectangle rectGraph = translateToGraph(r);
		n.x = rectGraph.x;
		n.y = rectGraph.y;
		n.width = rectGraph.width;
		n.height = rectGraph.height;
	}

	/**
	 * getNodeMetrics Retrieves the node extend and position from the node
	 * object. Defined as abstract to allow subclasses to implement to perform a
	 * transformation on the values stored in the node. i.e. support for
	 * Left-Right layout as opposed to Top-Down.
	 * 
	 * @param n
	 *            Node that has the metrics values to be retrieved.
	 * @return Rectangle that represents the location and extend of the Node.
	 */
	final protected Rectangle getNodeMetrics(Node n) {
		return translateFromGraph(new Rectangle(n.x, n.y, n.width, n.height));
	}

	/**
	 * Retrieves the extent and position from the given logical rectangle in 
	 * GEF graph coordinates. Defined as abstract to allow subclasses to implement 
	 * to perform a transformation on the values stored in the node. i.e. support for 
	 * Left-Right layout as opposed to Top-Down.
	 * 
	 * @param rect
	 *            <code>Rectangle</code> that has the values to be translated in 
	 *            logical (relative) coordinates.
	 *      
	 * @return <code>Rectangle</code> in graph coordinates.
	 */
	abstract protected Rectangle translateToGraph(Rectangle r);

	/**
	 * Retrieves the logical extent and position from the given rectangle.
	 * Defined as abstract to allow subclasses to implement to perform a
	 * transformation on the values stored in the node. i.e. support for
	 * Left-Right layout as opposed to Top-Down.
	 * 
	 * @param rect
	 *            <code>Rectangle</code> that has the values to be translated in
	 *            graph (pixel) coordinates.
	 * @return <code>Rectangle</code> in logical coordinates.
	 */
	abstract protected Rectangle translateFromGraph(Rectangle rect);
	
	/**
	 * build_edges Method to build up the edges in the temporary Graph structure
	 * which the algorithm is executed on.
	 * 
	 * selectedObjects List of selected objects to be layed out.
	 * 
	 * @param editPartToNodeDict
	 *            Map of editparts from the GEF to the temporary Nodes used for
	 *            layout.
	 * @return EdgeList list of Edges that is passed to the graph structure.
	 */
	private EdgeList build_edges(List selectedObjects, Map editPartToNodeDict) {

		EdgeList edges = new EdgeList();

		// Do "topdown" relationships first. Since they traditionally
		// go upward on a diagram, they are reversed when placed in the graph
		// for
		// layout. Also, layout traverses the arcs from each node in the order
		// of their insertion when finding a spanning tree for its constructed
		// hierarchy. Therefore, arcs added early are less likely to be
		// reversed.
		// In fact, since there are no cycles in these arcs, adding
		// them to the graph first should guarantee that they are never
		// reversed,
		// i.e., the inheritance hierarchy is preserved graphically.
		ArrayList objects = new ArrayList();
		objects.addAll(selectedObjects);
		ListIterator li = objects.listIterator();
		while (li.hasNext()) {
			EditPart gep = (EditPart) li.next();
			if (gep instanceof ConnectionEditPart) {
				ConnectionEditPart poly = (ConnectionEditPart) gep;

				if (layoutTopDown(poly)) {
					EditPart from = poly.getSource();
					EditPart to = poly.getTarget();

					Node fromNode = (Node) editPartToNodeDict.get(from);
					Node toNode = (Node) editPartToNodeDict.get(to);

					if (fromNode != null && toNode != null
						&& !fromNode.equals(toNode)) {
						addEdge(edges, poly, toNode, fromNode);
					}
				}
			}
		}

		// third pass for all other connections
		li = objects.listIterator();
		while (li.hasNext()) {
			EditPart gep = (EditPart) li.next();
			if (gep instanceof ConnectionEditPart) {
				ConnectionEditPart poly = (ConnectionEditPart) gep;

				if (!layoutTopDown(poly)) {
					EditPart from = poly.getSource();
					EditPart to = poly.getTarget();

					Node fromNode = (Node) editPartToNodeDict.get(from);
					Node toNode = (Node) editPartToNodeDict.get(to);

					if (fromNode != null && toNode != null
						&& !fromNode.equals(toNode)) {
						addEdge(edges, poly, fromNode, toNode);
					}
				}
			}
		}

		return edges;
	}

	/**
	 * @param edges
	 * @param gep
	 * @param fromNode
	 * @param toNode
	 */
	private void addEdge(EdgeList edges, ConnectionEditPart connectionEP,
			Node fromNode, Node toNode) {
		Edge edge = new Edge(connectionEP, fromNode, toNode);
		initializeEdge(connectionEP, edge);
		
		edges.add(edge);
	}

	/**
	 * initializeEdge Method used as a hook to initialize the Edge layout
	 * object. LayoutProvider subclasses may wish to initialize the edge
	 * different to customize the layout for their diagram domain.
	 * 
	 * @param connectionEP
	 *            EditPart used as a seed to initialize the edge properties
	 * @param edge
	 *            Edge to initialize with default values for the layout
	 */
	protected void initializeEdge(ConnectionEditPart connectionEP, Edge edge) {
		List affectingChildren = getAffectingChildren(connectionEP);
		
		// set the padding based on the extent of the children.
		edge.setPadding(Math.max(edge.getPadding(), calculateEdgePadding(connectionEP, affectingChildren)));
		edge.setDelta(Math.max(edge.getDelta(), affectingChildren.size() / 2));
	}
		
	/**
	 * Calculates the edge padding needed to initialize edge with.  Uses the number of children as a factor in
	 * determine the dynamic padding value.
	 */
	private int calculateEdgePadding(ConnectionEditPart connectionEP, List affectingChildren) {
		ListIterator li = affectingChildren.listIterator();
		
		int padding = 0;
		
		// union the children widths 
		while (li.hasNext()) {
			GraphicalEditPart gep = (GraphicalEditPart)li.next();
			
			padding = Math.max(padding, Math.max(gep.getFigure().getBounds().width, gep.getFigure().getBounds().height));
        }
        
		Rectangle.SINGLETON.x = 0;
		Rectangle.SINGLETON.y = 0;
		Rectangle.SINGLETON.width = padding;
		Rectangle.SINGLETON.height = padding;
		return Math.min(Math.max(Math.round(translateToGraph(Rectangle.SINGLETON).width * 1.5f), MIN_EDGE_PADDING), MAX_EDGE_PADDING);
	}
    
	/**
	 * Retrieve the associated children from the given connection editpart that will affect
	 * the layout.
	 * 
	 * @param conn the <code>ConnectionEditPart</code> to retrieve the children from
	 * @return a <code>List</code> that contains <code>GraphicalEditPart</code> objects
	 */
	private List getAffectingChildren(ConnectionEditPart conn) {
		List children = conn.getChildren();
		ListIterator lli = children.listIterator();
		List affectingChildrenList = new ArrayList();
		while (lli.hasNext()) {
			Object obj = lli.next();
			if (obj instanceof GraphicalEditPart) {
				GraphicalEditPart lep = (GraphicalEditPart)obj;
				Rectangle lepBox = lep.getFigure().getBounds().getCopy();
				
				if (!lep.getFigure().isVisible() || 
					lepBox.width == 0 || lepBox.height == 0)
					continue;
				
				affectingChildrenList.add(lep);
			}
		}
		return affectingChildrenList;
	}
	
	/**
	 * getRelevantConnections Given the editpart to Nodes Map this will
	 * calculate the connections in the diagram that are important to the
	 * layout.
	 * 
	 * @param editPartToNodeDict
	 *            Hashtable of editparts from the GEF to the temporary Nodes
	 *            used for layout.
	 * @return List of ConnectionEditPart that are to be part of the layout
	 *         routine.
	 */
	private List getRelevantConnections(Hashtable editPartToNodeDict) {
		Enumeration enumeration = editPartToNodeDict.keys();
		ArrayList connectionsToMove = new ArrayList();
		while (enumeration.hasMoreElements()) {
			Object e = enumeration.nextElement();
			ShapeEditPart shapeEP = (ShapeEditPart) e;
			List sourceConnections = shapeEP.getSourceConnections();
			for (int i = 0; i < sourceConnections.size(); i++) {
				ConnectionEditPart connectionEP = (ConnectionEditPart) sourceConnections
					.get(i);
				EditPart target = connectionEP.getTarget();

				// check to see if the toView is in the shapesDict, if yes,
				// the associated connectionView should be included on graph
				Object o = editPartToNodeDict.get(target);
				if (o != null) {
					connectionsToMove.add(connectionEP);
				}
			}
		}

		return connectionsToMove;
	}

	/**
	 * Method build_graph. This method will build the proxy graph that the
	 * layout is based on.
	 * 
	 * @param g
	 *            DirectedGraph structure that will be populated with Nodes and
	 *            Edges in this method.
	 * @param selectedObjects
	 *            List of editparts that the Nodes and Edges will be calculated
	 *            from.
	 */
	private void build_graph(DirectedGraph g, List selectedObjects) {
		Hashtable editPartToNodeDict = new Hashtable(500);

		this.minX = -1;
		this.minY = -1;

		NodeList nodes = build_nodes(selectedObjects, editPartToNodeDict);

		// append edges that should be added to the graph
		ArrayList objects = new ArrayList();
		objects.addAll(selectedObjects);
		objects.addAll(getRelevantConnections(editPartToNodeDict));

		EdgeList edges = build_edges(objects, editPartToNodeDict);

		g.nodes = nodes;
		g.edges = edges;

		//new BreakCycles().visit(g);
				
		// this has to be called after - BreakCycles to ensure we are fully
		// connected.
		//connectNonConnectedSubgraphs(nodes, edges);
	}

	/**
	 * reverse Utility function to reverse the order of points in a list.
	 * 
	 * @param c
	 *            PointList that is passed to the routine.
	 * @param rc
	 *            PointList that is reversed.
	 */
	private void reverse(PointList c, PointList rc) {
		rc.removeAllPoints();

		for (int i = c.size() - 1; i >= 0; i--) {
			rc.addPoint(c.getPoint(i));
		}
	}

	/**
	 * Computes the command that will route the given connection editpart with the given points.
	 */
	Command routeThrough(Edge edge, ConnectionEditPart connectEP, Node source, Node target, PointList points, int diffX, int diffY) {

		if (connectEP == null)
			return null;

		PointList routePoints = points;
		if (source.data != connectEP.getSource()) {
			routePoints = new PointList(points.size());
			reverse(points, routePoints);
			Node tmpNode = source;
			source = target;
			target = tmpNode;
		}
		
		PointList allPoints = new PointList(routePoints.size());
		for (int i = 0; i < routePoints.size(); i++) {
			allPoints.addPoint(routePoints.getPoint(i).x + diffX, routePoints
				.getPoint(i).y
				+ diffY);
		}

		Rectangle sourceExt = getNodeMetrics(source);
		Point ptSourceReference = sourceExt.getCenter().getTranslated(diffX,
			diffY);
		Rectangle targetExt = getNodeMetrics(target);
		Point ptTargetReference = targetExt.getCenter().getTranslated(diffX,
			diffY);
		
		SetAllBendpointRequest request = new SetAllBendpointRequest(
			RequestConstants.REQ_SET_ALL_BENDPOINT, allPoints,
			ptSourceReference, ptTargetReference);

		CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
		Command cmd = connectEP.getCommand(request);
		if (cmd != null)
			cc.add(cmd);
		
		// set the snapback position for all children owned by the connection
		List affectingChildren = getAffectingChildren(connectEP);
		Request snapBackRequest = new Request(RequestConstants.REQ_SNAP_BACK);
		ListIterator li = affectingChildren.listIterator();
		while (li.hasNext()) {
			EditPart ep = (EditPart)li.next();
			cmd = ep.getCommand(snapBackRequest);
			if (cmd != null)
				cc.add(cmd);
		}
		
		return cc;
	}
	
	/**
	 * Method update_diagram. Once the layout has been calculated with the GEF
	 * graph structure, the new layout values need to be propogated into the
	 * diagram. This is accomplished by creating a compound command that
	 * contains sub commands to change shapes positions and connection bendpoints
	 * positions. The command is subsequently executed by the calling action and
	 * then through the command infrastructure is undoable and redoable.
	 * 
	 * @param diagramEP
	 *            IGraphicalEditPart container that is target for the commands.
	 * @param g
	 *            DirectedGraph structure that contains the results of the
	 *            layout operation.
	 * @param isLayoutForSelected
	 *            boolean indicating that the layout is to be performed on
	 *            selected objects only. At this stage this is relevant only to
	 *            calculate the offset in the diagram where the layout will
	 *            occur.
	 * @return Command usually a command command that will set the locations of
	 *         nodes and bendpoints for connections.
	 */
	Command update_diagram(GraphicalEditPart diagramEP, DirectedGraph g,
			boolean isLayoutForSelected) {
		
		CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$

		Point diff = getLayoutPositionDelta(g, isLayoutForSelected);
		layoutDefaultMargin = MapModeUtil.getMapMode(diagramEP.getFigure()).DPtoLP(25);
		Command cmd = getShapesPositionCommand(g, diff);
		if (cmd != null)
			cc.add(cmd);
		
		cmd = getConnectionPositionCommand(g, diff);
		if (cmd != null)
			cc.add(cmd);
		
		return cc;
	}
	
	/*
	 * Find all of the arcs and set their intermediate points. This
	 * loop does not set the icon positions yet, because that causes
	 * recalculation of the arc connection points. The intermediate
	 * points of both outgoing and incomping arcs must be set before
	 * recalculating connection points.
	 */ 
	private Command getConnectionPositionCommand(DirectedGraph g, Point diff) {
		
		CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
		PointList points = new PointList(10);
		
		ListIterator vi = g.edges.listIterator();
		while (vi.hasNext()) {
			Edge edge = (Edge) vi.next();
			
			if (edge.data == null)
				continue;
			
			points.removeAllPoints();

			ConnectionEditPart cep = null;
			Node source = null, target = null;
			
			collectPoints(points, edge);
			cep = (ConnectionEditPart)edge.data;
			source = edge.source;
			target = edge.target;
			
			if (cep != null) {
				PointListUtilities.normalizeSegments(points, MapModeUtil.getMapMode(cep.getFigure()).DPtoLP(3));
					
				// Reset the points list
				Command cmd = routeThrough(edge, cep, source, target, points, diff.x, diff.y);
				if (cmd != null)
					cc.add(cmd);
			}
		}
		
		if (cc.isEmpty())
			return null;
		return cc;
	}

	private void collectPoints(PointList points, Edge edge) {
		Point startpt = edge.getPoints().getFirstPoint();
		Rectangle start = translateFromGraph(new Rectangle(startpt.x, startpt.y, 0, 0));
		points.addPoint(start.getTopLeft());

		NodeList vnodes = edge.vNodes;
		if (vnodes != null) {
			for (int i = 0; i < vnodes.size(); i++) {
				Node vn = vnodes.getNode(i);
				Rectangle nodeExt = getNodeMetrics(vn);
				int x = nodeExt.x;
				int y = nodeExt.y;

				points.addPoint(x + nodeExt.width / 2, y + nodeExt.height / 2);
			}
		}

		Point endpt = edge.getPoints().getLastPoint();
		Rectangle end = translateFromGraph(new Rectangle(endpt.x, endpt.y, 0, 0));
		points.addPoint(end.getTopLeft());
	}

	private Command getShapesPositionCommand(DirectedGraph g, Point diff) {
		ListIterator vi = g.nodes.listIterator();

		CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
		
		// Now set the position of the icons. This causes the
		// arc connection points to be recalculated
		while (vi.hasNext()) {
			Node node = (Node) vi.next();
			if (node.data instanceof ShapeEditPart) {
				IGraphicalEditPart gep = (IGraphicalEditPart)node.data;
				
				ChangeBoundsRequest request = new ChangeBoundsRequest(
					RequestConstants.REQ_MOVE);
				Rectangle nodeExt = getNodeMetrics(node);
				Point ptLocation = new Point(nodeExt.x + diff.x, nodeExt.y
					+ diff.y);

				Point ptOldLocation = gep.getFigure().getBounds().getLocation();
				gep.getFigure().translateToAbsolute(ptOldLocation);
				
				gep.getFigure().translateToAbsolute(ptLocation);
				Dimension delta = ptLocation.getDifference(ptOldLocation);

				request.setEditParts(gep);
				request.setMoveDelta(new Point(delta.width, delta.height));
				request.setLocation(ptLocation);

				Command cmd = gep.getCommand(request);
				if (cmd != null)
					cc.add(cmd);
			}
		}
		
		if (cc.isEmpty())
			return null;
		return cc;
	}

	private Point getLayoutPositionDelta(DirectedGraph g, boolean isLayoutForSelected) {
		// If laying out selected objects, use diff variables to
		// position objects at topleft corner of enclosing rectangle.
		if (isLayoutForSelected) {
			ListIterator vi;
			vi = g.nodes.listIterator();
			Point ptLayoutMin = new Point(-1, -1);
			while (vi.hasNext()) {
				Node node = (Node) vi.next();
				// ignore ghost node
				if (node.data != null) {
					Rectangle nodeExt = getNodeMetrics(node);
					if (ptLayoutMin.x == -1) {
						ptLayoutMin.x = nodeExt.x;
						ptLayoutMin.y = nodeExt.y;
					} else {
						ptLayoutMin.x = Math.min(ptLayoutMin.x, nodeExt.x);
						ptLayoutMin.y = Math.min(ptLayoutMin.y, nodeExt.y);
					}
				}
			}
	
			return new Point(this.minX - ptLayoutMin.x, this.minY - ptLayoutMin.y);
		}
		
		return new Point(layoutDefaultMargin, layoutDefaultMargin);
	}
}
