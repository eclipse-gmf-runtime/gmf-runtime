/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *    Mariot Chauvin <mariot.chauvin@obeo.fr> - bug 243888
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.commands.SetConnectionAnchorsCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderedShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutNodesOperation;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.SetAllBendpointRequest;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType;
import org.eclipse.gmf.runtime.draw2d.ui.figures.BaseSlidableAnchor;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PrecisionPointList;
import org.eclipse.gmf.runtime.draw2d.ui.graph.BorderNode;
import org.eclipse.gmf.runtime.draw2d.ui.graph.ConstantSizeNode;
import org.eclipse.gmf.runtime.draw2d.ui.graph.ConstrainedEdge;
import org.eclipse.gmf.runtime.draw2d.ui.graph.GMFDirectedGraphLayout;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.OrthogonalRouter;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Provider that creates a command for the DirectedGraph layout in GEF.
 * 
 * @author sshaw
 * 
 */
public abstract class DefaultProvider
    extends AbstractLayoutEditPartProvider {

    // Minimum sep between icon and bottommost horizontal arc
    protected int minX = -1;
    protected int minY = -1;
    protected int layoutDefaultMargin = 0;
    protected IMapMode mm;
    
    protected static final int NODE_PADDING = 30;
    protected static final int MIN_EDGE_PADDING = 15;
    protected static final int MAX_EDGE_PADDING = NODE_PADDING * 3;
    protected static final int MIN_EDGE_END_POINTS_PADDING = 5;
    

    
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
        if (containerEditPart == null) {
            InvalidParameterException ipe = new InvalidParameterException();
            Trace.throwing(DiagramProvidersPlugin.getInstance(),
                DiagramProvidersDebugOptions.EXCEPTIONS_THROWING, getClass(),
                "layout()", //$NON-NLS-1$
                ipe);
            throw ipe;
        }
        mm = MapModeUtil.getMapMode(containerEditPart.getFigure());
        // setup graph
        DirectedGraph g = createGraph();
        buildGraph(g, containerEditPart.getChildren());
        createGraphLayout().visit(g);
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
        
        DirectedGraph g = createGraph();
        buildGraph(g, selectedObjects);
        createGraphLayout().visit(g);
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
     * @deprecated
     * @see DefaultProvider#buildNodes(List, Map, Subgraph)
     */
    protected NodeList build_nodes(List selectedObjects, Map editPartToNodeDict, Subgraph root) {
        return buildNodes(selectedObjects, editPartToNodeDict, root);
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
    protected NodeList buildNodes(List selectedObjects, Map editPartToNodeDict, Subgraph root) {
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
                

                ConstantSizeNode n = new ConstantSizeNode(shapeEP);
                n.setPadding(new Insets(getMapMode().DPtoLP(NODE_PADDING)));
                n.setMinIncomingPadding(getMapMode().DPtoLP(MIN_EDGE_END_POINTS_PADDING));
                n.setMinOutgoingPadding(getMapMode().DPtoLP(MIN_EDGE_END_POINTS_PADDING));
                Dimension size = shapeEP.getSize();

                setNodeMetrics(n, new Rectangle(position.x, position.y,
                    size.width, size.height));

                editPartToNodeDict.put(shapeEP, n);
                nodes.add(n);
                
                buildBorderNodes(shapeEP, n, editPartToNodeDict);
                
            }
        }

        return nodes;
    }
    
    /**
	 * Since an editpart may contain border items that may need be laid out,
	 * this is the place where border nodes can be created and added to the map
	 * of editparts to nodes. If border items locations don't have much
	 * semantical meaning and their locations are valubale notationally it's
	 * best that border nodes are created here in this method. The
	 * infrastructure for creating commands to move border items around is all
	 * in place already. Creates border nodes for an editpart.
	 * 
	 * @param parentEP
	 *            the editopart
	 * @param parentNode
	 *            the node for the editpart
	 * @param editPartToNodeDict
	 *            the map of editparts to nodes
	 * @since 2.1
	 * @deprecated
	 * @see DefaultProvider#buildBorderNodes(GraphicalEditPart, ConstantSizeNode, Map)
	 */
    protected void build_borderNodes(GraphicalEditPart parentEP, ConstantSizeNode parentNode, Map editPartToNodeDict) {
    	buildBorderNodes(parentEP, parentNode, editPartToNodeDict);
    }
    
    /**
	 * Since an editpart may contain border items that may need be laid out,
	 * this is the place where border nodes can be created and added to the map
	 * of editparts to nodes. If border items locations don't have much
	 * semantical meaning and their locations are valubale notationally it's
	 * best that border nodes are created here in this method. The
	 * infrastructure for creating commands to move border items around is all
	 * in place already. Creates border nodes for an editpart.
	 * 
	 * @param parentEP
	 *            the editopart
	 * @param parentNode
	 *            the node for the editpart
	 * @param editPartToNodeDict
	 *            the map of editparts to nodes
	 * @since 2.1
	 */
    protected void buildBorderNodes(GraphicalEditPart parentEP, ConstantSizeNode parentNode, Map editPartToNodeDict) {
    	if (!supportsBorderNodes()) {
    		return;
    	}
    	boolean borderNodesAdded = false;
    	Rectangle parentRect = new Rectangle(parentNode.x, parentNode.y, parentNode.width, parentNode.height);
    	Rectangle extendedRect = parentRect.getCopy();
    	for (Iterator itr = parentEP.getChildren().iterator(); itr.hasNext();) {
    		EditPart ep = (EditPart) itr.next();
    		if (ep instanceof IBorderItemEditPart && canCreateBorderNode((IBorderItemEditPart)ep)) {
    			IBorderItemEditPart bep = (IBorderItemEditPart) ep; 
    			BorderNode bn = new BorderNode(bep, parentNode);
    			setNodeMetrics(bn, bep.getFigure().getBounds());
    			/*
    			 * Border item bounding rectangle = b
    			 * Border item parent rectangle = p
    			 * outsideRatio = ( 1.0 - Area(Intersection(b, p))) / Area(p)
    			 */
    			bn.setOutsideRatio(1f - ((float) bep.getFigure().getBounds().getCopy().intersect(parentEP.getFigure().getBounds()).getSize().getArea()) / bep.getFigure().getSize().getArea());
    			editPartToNodeDict.put(bep, bn);
    			borderNodesAdded = true;
    			extendedRect.union(new Rectangle(bn.x, bn.y, bn.width, bn.height));
    			bn.setMinIncomingPadding(getMapMode().DPtoLP(MIN_EDGE_END_POINTS_PADDING));
    			bn.setMinOutgoingPadding(getMapMode().DPtoLP(MIN_EDGE_END_POINTS_PADDING));
    		}
    	}
    	if (borderNodesAdded) {
    		parentNode.getPadding().add(new Insets(Math.max(extendedRect.width - parentRect.width, extendedRect.height - parentRect.height)));
    	}
    }
    
    /**
     * Returns <code>true</code> if a border node for the given border item editpart needs to be created.
     * By default we just need to know if the border item is movable (can change its x,y coordinate), which
     * means that a non resizable edit policy have to be installed on the editpart. 
     * 
     * @param ep the border item editpart
     * @return <code>true</code> if border node needs to be created for the editpart
     */
    protected boolean canCreateBorderNode(IBorderItemEditPart ep) {
    	return ep.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE) instanceof NonResizableEditPolicy;
    }
    
    /**
     * Returns <code>true</code> if the layout provider supports creation of border nodes.
     * The default behavior for the layout provider is not to support arranging border items.
     * Clients must override if this support is needed.
     * 
     * @return <code>true</code> if border items layout is supported by the layout provider
     */
    protected boolean supportsBorderNodes() {
    	return false;
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
     protected Rectangle getNodeMetrics(Node n) {
        Rectangle rect = new Rectangle(n.x, n.y, n.width, n.height);
        PrecisionRectangle preciseRect = new PrecisionRectangle(rect);
        return translateFromGraph(preciseRect);
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
     * @deprecated
     * @see DefaultProvider#buildEdges(List, Map)
     */
    protected EdgeList build_edges(List selectedObjects, Map editPartToNodeDict) {
    	return buildEdges(selectedObjects, editPartToNodeDict);
    }
    
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
    protected EdgeList buildEdges(List selectedObjects, Map editPartToNodeDict) {

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
        ArrayList notTopDownEdges = new ArrayList();
        boolean shouldHandleListItems = shouldHandleConnectableListItems();
        while (li.hasNext()) {
            EditPart gep = (EditPart) li.next();
            if (gep instanceof ConnectionEditPart) {
                ConnectionEditPart poly = (ConnectionEditPart) gep;

                if (layoutTopDown(poly)) {
                    EditPart from = poly.getSource();
                    EditPart to = poly.getTarget();
                    if (from instanceof IBorderItemEditPart && !editPartToNodeDict.containsKey(from))
                        from = from.getParent();
                    else if (shouldHandleListItems && from instanceof ListItemEditPart)
                        from = getFirstAnscestorinNodesMap(from, editPartToNodeDict);
                    if (to instanceof IBorderItemEditPart && !editPartToNodeDict.containsKey(to))
                        to = to.getParent();
                    else if (shouldHandleListItems && to instanceof ListItemEditPart)
                        to = getFirstAnscestorinNodesMap(to, editPartToNodeDict);
        			Node fromNode = null;
        			if (from != null) {
        				fromNode = (Node) editPartToNodeDict.get(from);
        			}
        			Node toNode = null;
        			if (to != null) {
        				toNode = (Node) editPartToNodeDict.get(to);
        			}
                    
                    if (fromNode != null && toNode != null
                        && !checkSelfEdge(from, to, editPartToNodeDict)) {
                        addEdge(edges, poly, toNode, fromNode);
                    }
                }else{
                    notTopDownEdges.add(poly);
                }
            }
        }

        // third pass for all other connections
        li = notTopDownEdges.listIterator();
        while (li.hasNext()) {
            ConnectionEditPart poly = (ConnectionEditPart) li.next();
            EditPart from = poly.getSource();
            EditPart to = poly.getTarget();
            if (from instanceof IBorderItemEditPart && !editPartToNodeDict.containsKey(from))
                from = from.getParent();
            else if (shouldHandleListItems && from instanceof ListItemEditPart)
                from = getFirstAnscestorinNodesMap(from, editPartToNodeDict);
            if (to instanceof IBorderItemEditPart && !editPartToNodeDict.containsKey(to))
                to = to.getParent();
            else if (shouldHandleListItems && to instanceof ListItemEditPart)
                to = getFirstAnscestorinNodesMap(to, editPartToNodeDict);
            
			Node fromNode = null;
			if (from != null) {
				fromNode = (Node) editPartToNodeDict.get(from);
			}
			Node toNode = null;
			if (to != null) {
				toNode = (Node) editPartToNodeDict.get(to);
			}
            
            if (fromNode != null && toNode != null
                && !checkSelfEdge(from, to, editPartToNodeDict)) {
                addEdge(edges, poly, fromNode, toNode);
            }
        }
        return edges;
    }
    
    private boolean checkSelfEdge(EditPart from, EditPart to, Map dictionary) {
   		Node graphSource = from instanceof IBorderItemEditPart ? (Node) dictionary.get(from.getParent()) : (Node) dictionary.get(from); 
   		Node graphTarget = to instanceof IBorderItemEditPart ? (Node) dictionary.get(to.getParent()) : (Node) dictionary.get(to);
    	return graphSource != null && graphTarget != null && graphSource.equals(graphTarget);
    }
    
    /**
     * @param edges
     * @param gep
     * @param fromNode
     * @param toNode
     */
    private void addEdge(EdgeList edges, ConnectionEditPart connectionEP,
            Node fromNode, Node toNode) {
    	ConstrainedEdge edge = new ConstrainedEdge(connectionEP, fromNode, toNode);
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
        if (edge instanceof ConstrainedEdge && ((Connection)connectionEP.getFigure()).getConnectionRouter() instanceof OrthogonalRouter) {
        	((ConstrainedEdge)edge).setStyle(ConstrainedEdge.ORTHOGONAL_ROUTING_STYLE);
        }
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
        return Math.min(Math.max(Math.round(translateToGraph(Rectangle.SINGLETON).width * 1.5f), getMapMode().DPtoLP(MIN_EDGE_PADDING)), getMapMode().DPtoLP(MAX_EDGE_PADDING));
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
    protected List getRelevantConnections(Hashtable editPartToNodeDict) {
        Enumeration enumeration = editPartToNodeDict.keys();
        ArrayList connectionsToMove = new ArrayList();
        while (enumeration.hasMoreElements()) {
            Object e = enumeration.nextElement();
            GraphicalEditPart shapeEP = (GraphicalEditPart) e;
            Set sourceConnections = new HashSet(shapeEP.getSourceConnections());
            if (shapeEP instanceof IBorderedShapeEditPart){
                List borderItems = getBorderItemEditParts(shapeEP, editPartToNodeDict);
                for (Iterator iter = borderItems.iterator(); iter.hasNext();) {
                    GraphicalEditPart element = (GraphicalEditPart) iter.next();
                    sourceConnections.addAll(element.getSourceConnections());
                }
            }
            
            for (Iterator iter = sourceConnections.iterator();
                    iter.hasNext();) {
                ConnectionEditPart connectionEP = (ConnectionEditPart) iter.next();
                EditPart target = connectionEP.getTarget();
                // check to see if the toView is in the shapesDict, if yes,
                // the associated connectionView should be included on graph
                if (target instanceof IBorderItemEditPart)
                    target = target.getParent();
                if (target != null) {
                	Object o = editPartToNodeDict.get(target);
                    if (o != null) {
                        connectionsToMove.add(connectionEP);
                    }
                }
            }
            
            if (shouldHandleConnectableListItems()){
                handleConnectableListItems(shapeEP,editPartToNodeDict,connectionsToMove);
            }
        }

        return connectionsToMove;
    }
    
    private void handleConnectableListItems(GraphicalEditPart shapeEP, Map editPartToNodeDict, ArrayList connectionsToMove) {
        List children = shapeEP.getChildren();
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            EditPart ep = (EditPart) iter.next();
            if (ep instanceof ListCompartmentEditPart){
                List listItems = ep.getChildren();
                for (Iterator iterator = listItems.iterator(); iterator
                    .hasNext();) {
                    GraphicalEditPart listItem = (GraphicalEditPart) iterator.next();
                    List connections =listItem.getSourceConnections();
                    for (Iterator connectionIterator = connections.iterator(); connectionIterator
                        .hasNext();) {
                        ConnectionEditPart connectionEP = (ConnectionEditPart) connectionIterator.next();
                        EditPart ancestor = getFirstAnscestorinNodesMap(connectionEP.getTarget(),editPartToNodeDict);
                        if (ancestor!=null)
                            connectionsToMove.add(connectionEP);
                    }
                }
            }
            
        }
        
    }

    private EditPart getFirstAnscestorinNodesMap(EditPart editPart,Map editPartToNodeDict) {
        EditPart ancestor =  editPart;
        while (ancestor!=null){
            if (editPartToNodeDict.get(ancestor)!=null)
                return ancestor;
            ancestor = ancestor.getParent();
        }
        return null;
    }

    /**
     * This method searches an edit part for a child that is a border item edit part
     * @param parent part needed to search
     * @param set to be modified of border item edit parts that are direct children of the parent
     */
    private List getBorderItemEditParts(EditPart parent, Hashtable editPartToNodeDict ) {
        Iterator iter = parent.getChildren().iterator();
        List list = new ArrayList();
        while(iter.hasNext()) {
            EditPart child = (EditPart)iter.next();
            if (!editPartToNodeDict.containsKey(child) && child instanceof IBorderItemEditPart) {
                list.add(child);
            }
        }
        return list;
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
     * @deprecated
     * @see DefaultProvider#buildGraph(DirectedGraph, List)
     */
    protected void build_graph(DirectedGraph g, List selectedObjects) {
    	buildGraph(g, selectedObjects);
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
    protected void buildGraph(DirectedGraph g, List selectedObjects) {
        Hashtable editPartToNodeDict = new Hashtable(500);
        this.minX = -1;
        this.minY = -1;
        NodeList nodes = buildNodes(selectedObjects, editPartToNodeDict,null);

        // append edges that should be added to the graph
        ArrayList objects = new ArrayList();
        objects.addAll(selectedObjects);
        objects.addAll(getRelevantConnections(editPartToNodeDict));
        EdgeList edges = buildEdges(objects, editPartToNodeDict);
        g.nodes = nodes;
        g.edges = edges;
        postProcessGraph(g,editPartToNodeDict);
        //printGraph(g);
     }
    
    protected void postProcessGraph(DirectedGraph g, Hashtable editPartToNodeDict) {
        //default do nothing
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
    protected Command routeThrough(Edge edge, ConnectionEditPart connectEP, Node source, Node target, PointList points, Point diff) {

        if (connectEP == null)
            return null;

        PointList routePoints = points;
        if (source.data == connectEP.getTarget()) {
            routePoints = new PointList(points.size());
            reverse(points, routePoints);
            Node tmpNode = source;
            source = target;
            target = tmpNode;
        }
        
        double totalEdgeDiffX = diff.preciseX() ;
        double totalEdgeDiffY = diff.preciseY() ;
        
        PrecisionPointList allPoints = new PrecisionPointList(routePoints.size());
        for (int i = 0; i < routePoints.size(); i++) {
            allPoints.addPrecisionPoint(routePoints.getPoint(i).preciseX() + totalEdgeDiffX, routePoints
                .getPoint(i).preciseY()
                + totalEdgeDiffY);
        }

        CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
        
        LineSeg anchorReferencePoints = addAnchorsCommands(cc, allPoints.getFirstPoint(), allPoints.getLastPoint(), source, target, connectEP, diff);
        		
        SetAllBendpointRequest request = new SetAllBendpointRequest(
                RequestConstants.REQ_SET_ALL_BENDPOINT, allPoints,
                anchorReferencePoints.getOrigin(), anchorReferencePoints.getTerminus());

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
        
        if (cc.isEmpty())
            return null;
        return cc;
    }
    
    /**
	 * Creates source and target anchor commands and appends them to the
	 * compound command passed in. Returns a line segment ends of which are the
	 * new source and target anchor reference points for further use in the
	 * command setting the bend points.
	 * 
	 * @param cc
	 *            command to add anchors commands to
	 * @param sourceAnchorLocation
	 *            the source anchor location coordinates
	 * @param targetAnchorLocation
	 *            the target anchor location coordinates
	 * @param source
	 *            source node
	 * @param target
	 *            target node
	 * @param cep
	 *            connection editpart
	 * @param diffX
	 *            x axis offset
	 * @param diffY
	 *            y axis offset
	 * @return <code>LineSeg</code> origin is the new source anchor reference
	 *         point and origin is the new target anchor reference point
	 */
	protected LineSeg addAnchorsCommands(CompoundCommand cc,
			Point sourceAnchorLocation, Point targetAnchorLocation,
			Node source, Node target, ConnectionEditPart cep, Point diff) {
		Rectangle sourceExt = getNodeMetrics(source);
		Rectangle targetExt = getNodeMetrics(target);
		sourceExt.translate(diff);
		targetExt.translate(diff);
		
		/*
		 * If source or target anchor command won't be created or will be non-executable,
		 * source or target reference point is assumed to be the geometric centre of a shape.
		 */
		Point resultantSourceAnchorReference = sourceExt.getCenter();
		Point resultantTargetAnchorReference = targetExt.getCenter();

		PrecisionPoint sourceRatio = new PrecisionPoint((sourceAnchorLocation
				.preciseX() - sourceExt.preciseX())
				/ sourceExt.preciseWidth(),
				(sourceAnchorLocation.preciseY() - sourceExt.preciseY())
						/ sourceExt.preciseHeight());
		PrecisionPoint targetRatio = new PrecisionPoint((targetAnchorLocation
				.preciseX() - targetExt.preciseX())
				/ targetExt.preciseWidth(),
				(targetAnchorLocation.preciseY() - targetExt.preciseY())
						/ targetExt.preciseHeight());
		
		/*
		 * Need to fake reconnection of the ends of the connection. Currently
		 * existing figure coordinates (old coordinates) needs to be used for
		 * this, since the reconnection location is passed in absolute
		 * coordinates.
		 */
		if (cep.getSource().equals(source.data)) {
			ReconnectRequest reconnectRequest = new ReconnectRequest(
					org.eclipse.gef.RequestConstants.REQ_RECONNECT_SOURCE);
			reconnectRequest.setConnectionEditPart(cep);
			reconnectRequest.setTargetEditPart(cep.getSource());
			IFigure sourceFig = ((GraphicalEditPart)cep.getSource()).getFigure();
			Point sourceAnchorReference = new PrecisionPoint(
					sourceFig.getBounds().preciseX() + sourceRatio.preciseX()
							* sourceFig.getBounds().preciseWidth(), sourceFig
							.getBounds().preciseY()
							+ sourceRatio.preciseY()
							* sourceFig.getBounds().preciseHeight());
			sourceFig.translateToAbsolute(sourceAnchorReference);
			reconnectRequest.setLocation(sourceAnchorReference);
			Command sourceAnchorCommand = cep.getSource()
					.getCommand(reconnectRequest);
			if (sourceAnchorCommand != null && sourceAnchorCommand.canExecute()) {
				cc.add(sourceAnchorCommand);
				if (((Connection)cep.getFigure()).getSourceAnchor() instanceof BaseSlidableAnchor) {
					if (sourceAnchorCommand instanceof ICommandProxy) {
						updateNewSlidingAnchorReferenceRatio((ICommandProxy) sourceAnchorCommand, true, sourceRatio);
					}
					resultantSourceAnchorReference = new PrecisionPoint(sourceExt
							.preciseWidth()
							* sourceRatio.preciseX() + sourceExt.preciseX(), sourceExt
							.preciseHeight()
							* sourceRatio.preciseY() + sourceExt.preciseY());
				}
			}
		} else {
			resultantSourceAnchorReference = getNewAnchorReferencePoint(source, sourceExt, ((Connection)cep.getFigure()).getSourceAnchor().getReferencePoint());
		}

		if (cep.getTarget().equals(target.data)) {
			ReconnectRequest reconnectRequest = new ReconnectRequest(
					org.eclipse.gef.RequestConstants.REQ_RECONNECT_TARGET);
			reconnectRequest.setConnectionEditPart(cep);
			reconnectRequest.setTargetEditPart(cep.getTarget());
			IFigure targetFig = ((GraphicalEditPart) cep.getTarget()).getFigure();
			Point targetAnchorReference = new PrecisionPoint(
					targetFig.getBounds().preciseX() + targetRatio.preciseX()
							* targetFig.getBounds().preciseWidth(), targetFig
							.getBounds().preciseY()
							+ targetRatio.preciseY()
							* targetFig.getBounds().preciseHeight());
			targetFig.translateToAbsolute(targetAnchorReference);
			reconnectRequest.setLocation(targetAnchorReference);
			Command targetAnchorCommand = cep.getTarget()
					.getCommand(reconnectRequest);
			if (targetAnchorCommand != null && targetAnchorCommand.canExecute()) {
				cc.add(targetAnchorCommand);
				if (((Connection)cep.getFigure()).getTargetAnchor() instanceof BaseSlidableAnchor) {
					if (targetAnchorCommand instanceof ICommandProxy) {
						updateNewSlidingAnchorReferenceRatio((ICommandProxy) targetAnchorCommand, false, targetRatio);
					}
					resultantTargetAnchorReference = new PrecisionPoint(targetExt
							.preciseWidth()
							* targetRatio.preciseX() + targetExt.preciseX(), targetExt
							.preciseHeight()
							* targetRatio.preciseY() + targetExt.preciseY());
				}
			}
		} else {
			resultantTargetAnchorReference = getNewAnchorReferencePoint(target, targetExt, ((Connection)cep.getFigure()).getTargetAnchor().getReferencePoint());
		}
		return new LineSeg(resultantSourceAnchorReference,
				resultantTargetAnchorReference);
	}
	
	private void updateNewSlidingAnchorReferenceRatio(ICommandProxy setAnchorCommand, boolean source, PrecisionPoint ratio) {
		/*
		 * Find the SetConnectionAnchorsCommand
		 */
		SetConnectionAnchorsCommand cmd = findSetConnectionAnchorsCommand(setAnchorCommand.getICommand());
		if (cmd != null) {
			PrecisionPoint newRatio = null;
			if (source) {
				newRatio = cmd.getNewSourceTerminal() == null ? new PrecisionPoint(0.5, 0.5) : BaseSlidableAnchor.parseTerminalString(cmd.getNewSourceTerminal());
			} else {
				newRatio = cmd.getNewTargetTerminal() == null ? new PrecisionPoint(0.5, 0.5) : BaseSlidableAnchor.parseTerminalString(cmd.getNewTargetTerminal());
			}
			if (newRatio != null) {
				ratio.preciseX = newRatio.preciseX;
				ratio.preciseY = newRatio.preciseY;
				ratio.updateInts();
			}
		}
	}
	
	private SetConnectionAnchorsCommand findSetConnectionAnchorsCommand(ICommand cmd) {
		if (cmd instanceof SetConnectionAnchorsCommand) {
			return (SetConnectionAnchorsCommand) cmd;
		} else if (cmd instanceof CompositeCommand) {
			for (Iterator itr = ((CompositeCommand)cmd).listIterator(); itr.hasNext();) {
				ICommand childCmd = (ICommand) itr.next();
				SetConnectionAnchorsCommand setAnchorsCmd = findSetConnectionAnchorsCommand(childCmd);
				if (setAnchorsCmd != null) {
					return setAnchorsCmd;
				}
			}
		}
		return null;
	}
	
	private Point getNewAnchorReferencePoint(Node node, Rectangle nodeBoundsOnDiagram, Point oldAbsReference) {
		GraphicalEditPart gep = (GraphicalEditPart)node.data;
		PrecisionPoint parentLocation = new PrecisionPoint(gep.getFigure().getBounds().getLocation());
		gep.getFigure().translateToAbsolute(parentLocation);
		PrecisionDimension diff = new PrecisionDimension(oldAbsReference.preciseX() - parentLocation.preciseX(), oldAbsReference.preciseY() - parentLocation.preciseY());
		getMapMode().DPtoLP(diff);
		return nodeBoundsOnDiagram.getLocation().translate(diff);
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
    protected Command update_diagram(GraphicalEditPart diagramEP, DirectedGraph g,
            boolean isLayoutForSelected) {
        
        CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$

        Point diff = getLayoutPositionDelta(g, isLayoutForSelected);
        Command cmd = createNodeChangeBoundCommands(g, diff);
        if (cmd != null)
            cc.add(cmd);
        
        cmd = createEdgesChangeBoundsCommands(g, diff);
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
    protected Command createEdgesChangeBoundsCommands(DirectedGraph g, Point diff) {
        
        CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
        PointList points = new PrecisionPointList(10);
        
        ListIterator vi = g.edges.listIterator();
        while (vi.hasNext()) {
            Edge edge = (Edge) vi.next();
            
            if (edge.data == null || edge.getPoints()==null)
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
                Command cmd = routeThrough(edge, cep, source, target, points, diff);
                if (cmd != null)
                    cc.add(cmd);
            }
        }
        
        if (cc.isEmpty())
            return null;
        return cc;
    }
        
    private void collectPoints(PointList points, Edge edge) {
        PointList pointList = edge.getPoints();
        for (int i = 0; i < pointList.size(); i++) {
        	Rectangle pt = translateFromGraph(new Rectangle(pointList.getPoint(i), new Dimension()));
        	points.addPoint(pt.getLocation());
        }
    }

    protected Command createNodeChangeBoundCommands(DirectedGraph g, Point diff) {
        ListIterator vi = g.nodes.listIterator();
        CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
        createSubCommands(diff, vi, cc);
        if (cc.isEmpty())
            return null;
        return cc;
    }

    protected void createSubCommands(Point diff, ListIterator vi, CompoundCommand cc) {
        // Now set the position of the icons. This causes the
        // arc connection points to be recalculated
        while (vi.hasNext()) {
            Node node = (Node) vi.next();
            if (node.data instanceof ShapeEditPart) {
                IGraphicalEditPart gep = (IGraphicalEditPart)node.data;
                
                ChangeBoundsRequest request = new ChangeBoundsRequest(
                    RequestConstants.REQ_MOVE);
                Rectangle nodeExt = getNodeMetrics(node);
                Point ptLocation = new PrecisionPoint(nodeExt.preciseX() + diff.preciseX(), nodeExt.preciseY()
                    + diff.preciseY());

                PrecisionPoint ptOldLocation = new PrecisionPoint(gep.getFigure().getBounds().getLocation());
                gep.getFigure().translateToAbsolute(ptOldLocation);
                
                gep.getFigure().translateToAbsolute(ptLocation);
                PrecisionPoint delta = new PrecisionPoint(ptLocation.preciseX()
                    - ptOldLocation.preciseX(), ptLocation.preciseY()
                    - ptOldLocation.preciseY());

                request.setEditParts(gep);
                request.setMoveDelta(delta);
                request.setLocation(ptLocation);
                
                Command cmd = gep.getCommand(request);
                if (cmd != null && cmd.canExecute()) {
                    cc.add(cmd);
                }
            }
        	if (node instanceof ConstantSizeNode) {
        		ConstantSizeNode cn = (ConstantSizeNode) node;
        		for (Iterator<BorderNode> itr = cn.borderNodes.iterator(); itr.hasNext();) {
        			createBorderItemChangeBoundsCommand(itr.next(), cn, cc);
        		}
        	}
        }
    }
    
    private void createBorderItemChangeBoundsCommand(BorderNode bn, ConstantSizeNode parentNode, CompoundCommand cc) {
        ChangeBoundsRequest request = new ChangeBoundsRequest(
                RequestConstants.REQ_MOVE);
        Rectangle parentRect = getNodeMetrics(parentNode);
        Rectangle borderItemRect = getNodeMetrics(bn);
        Dimension offset = borderItemRect.getLocation().getDifference(parentRect.getLocation());
        
        IFigure parentFigure = ((GraphicalEditPart)parentNode.data).getFigure();
        IFigure borderItemFigure = ((GraphicalEditPart)bn.data).getFigure();
        
        PrecisionPoint oldParentLocation = new PrecisionPoint(parentFigure.getBounds().getLocation());
        PrecisionPoint oldBorderItemLocation = new PrecisionPoint(borderItemFigure.getBounds().getLocation());        
        PrecisionPoint newBorderItemLocation = new PrecisionPoint(oldParentLocation.preciseX() + offset.preciseWidth(), oldParentLocation.preciseY() + offset.preciseHeight());
        parentFigure.translateToAbsolute(oldParentLocation);
        parentFigure.translateToAbsolute(newBorderItemLocation);
        borderItemFigure.translateToAbsolute(oldBorderItemLocation);
        
        PrecisionPoint delta = new PrecisionPoint(newBorderItemLocation.preciseX() - oldBorderItemLocation.preciseX(), newBorderItemLocation.preciseY() - oldBorderItemLocation.preciseY());
        GraphicalEditPart gep = (GraphicalEditPart) bn.data;
        request.setEditParts(gep);
        request.setMoveDelta(delta);
        request.setLocation(newBorderItemLocation);
        
        Command cmd = gep.getCommand(request);
        if (cmd != null && cmd.canExecute()) {
            cc.add(cmd);
        }
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
    
    /**
     * Creates the graph that will be used by the layouy provider
     * Clients can override this method create different kind of graphs
     * This method is called by {@link DefaultProvider#layoutEditParts(GraphicalEditPart, IAdaptable) } 
     * and {@link DefaultProvider#layoutEditParts(List, IAdaptable)}  
     * @return the Graph that will be used by the layout algorithm
     */
    protected DirectedGraph createGraph(){
        return new DirectedGraph();
    }
    
    /**
     * Creates the graph layout algorithm that will be used to layout the diagram
     * This method is called by {@link DefaultProvider#layoutEditParts(GraphicalEditPart, IAdaptable) } 
     * and {@link DefaultProvider#layoutEditParts(List, IAdaptable)}  
     * @return the graph layout 
     */
    protected DirectedGraphLayout createGraphLayout() {
        return new GMFDirectedGraphLayout();
    }
    
    /** 
     * Allows "Arrange all" for 1 node, but doesn't allow "arrange selection" for 1 node
     */
    @Override
	public boolean canLayoutNodes(List layoutNodes,
			boolean shouldOffsetFromBoundingBox, IAdaptable layoutHint) {
    	if (super.canLayoutNodes(layoutNodes, shouldOffsetFromBoundingBox, layoutHint)) {
    		return !shouldOffsetFromBoundingBox || (shouldOffsetFromBoundingBox && layoutNodes.size() > 1);
    	}
    	return false;
	}

	/**
     * Indicates if the provider will consider the connections between ListItems 
     * while doing the arrange action
     * @return true or false
     */
    protected boolean shouldHandleConnectableListItems() {
        return false;
    }
    
   /* private void printGraph(DirectedGraph g){
        int depth = 0;
        if (g instanceof CompoundDirectedGraph){
            NodeList subGraphs = ((CompoundDirectedGraph)g).nodes;
            for (Iterator iter = subGraphs.iterator(); iter.hasNext();) {
                Node node = (Node)iter.next();
                if (node.getParent()!=null)
                    continue;
                if (node instanceof Subgraph){
                    printSubGraph((Subgraph)node,depth);
                }else {
                    printNode(node,depth);
                }
            }
        }
    }

    private void printNode(Node node, int depth) {
        StringBuffer buffer = new StringBuffer();
        for (int i =0 ; i<depth ; i++)
            buffer.append("\t");
        buffer.append("Node");
        System.out.println(buffer);
    }

    private void printSubGraph(Subgraph subgraph, int depth) {
        StringBuffer buffer = new StringBuffer();
        for (int i =0 ; i<depth ; i++)
            buffer.append("\t");
        buffer.append("SubGraph");
        if (!subgraph.members.isEmpty()){
            buffer.append(" : ");
            System.out.println(buffer);
            NodeList nodes = subgraph.members;
            depth++;
            for (Iterator iter = nodes.iterator(); iter.hasNext();) {
                Node element = (Node) iter.next();
                if (element instanceof Subgraph){
                    printSubGraph((Subgraph)element,depth);
                }else {
                    printNode(element,depth);
                }
            }
        }else {
            System.out.println(buffer);
        }
            
    }*/
}
