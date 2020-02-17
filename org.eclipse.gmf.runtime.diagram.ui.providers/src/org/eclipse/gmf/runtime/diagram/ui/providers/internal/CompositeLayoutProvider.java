/******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *    Mariot Chauvin <mariot.chauvin@obeo.fr> - bug 243888
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.graph.ConstantSizeNode;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graph.AdvancedSubGraph;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graph.CompositeDirectedGraphLayout;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graph.VirtualNode;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Size;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Provider that creates a command for the CompoundDirectedGraph layout in GEF.
 * 
 * @author mmostafa
 * 
 */

public abstract class CompositeLayoutProvider
    extends DefaultProvider {

    /**
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#build_nodes(java.util.List, java.util.Map, org.eclipse.draw2d.graph.Subgraph)
     * @deprecated
     */
    protected NodeList build_nodes(List selectedObjects,
            Map editPartToNodeDict, Subgraph rootGraph) {
    	return buildNodes(selectedObjects, editPartToNodeDict, rootGraph);
    }
	
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#buildNodes(java.util.List, java.util.Map, org.eclipse.draw2d.graph.Subgraph)
     */
    protected NodeList buildNodes(List selectedObjects,
            Map editPartToNodeDict, Subgraph rootGraph) {
        ListIterator li = selectedObjects.listIterator();
        NodeList nodes = new NodeList();
        while (li.hasNext()) {
            IGraphicalEditPart gep = (IGraphicalEditPart) li.next();
            boolean hasChildren = hasChildren(gep);
            if (!(gep instanceof IBorderItemEditPart)
                && (gep instanceof ShapeEditPart || gep instanceof ShapeCompartmentEditPart)) {
                GraphicalEditPart ep = (GraphicalEditPart) gep;
                Point position = ep.getFigure().getBounds().getLocation();
                if (minX == -1) {
                    minX = position.x;
                    minY = position.y;
                } else {
                    minX = Math.min(minX, position.x);
                    minY = Math.min(minY, position.y);
                }
                Node n = null;
                if (hasChildren && !(gep instanceof GroupEditPart)) {
                    AdvancedSubGraph subGraph = null;
                    if (rootGraph != null)
                        subGraph = new AdvancedSubGraph(ep, rootGraph);
                    else
                        subGraph = new AdvancedSubGraph(ep);
                    subGraph.setAutoSize(isAutoSizeOn(subGraph,ep));
                    if (gep instanceof CompartmentEditPart){
                        subGraph.setHasBufferedZone(true);
                    }
                    subGraph.setDirection(getLayoutDirection(ep));
                    n = subGraph;
                } else {
                    if (rootGraph != null)
                        n = new ConstantSizeNode(ep, rootGraph);
                    else
                        n = new ConstantSizeNode(ep);
                }
                adjustNodePadding(n, editPartToNodeDict);
                Dimension size = ep.getFigure().getBounds().getSize();
                setNodeMetrics(n, new Rectangle(position.x, position.y,
                    size.width, size.height));
                editPartToNodeDict.put(ep, n);
                nodes.add(n);
                if (hasChildren && !(gep instanceof GroupEditPart)) {
                    buildNodes(gep.getChildren(), editPartToNodeDict,
                        (Subgraph) n);
                }
                if (n instanceof ConstantSizeNode) {
                    buildBorderNodes(gep, (ConstantSizeNode)n, editPartToNodeDict);                	
                }
            }
        }
        return nodes;
    }
    
    /**
     * Gets the layout direction for an editpart. Every editpart mapped to 
     * <code>AdvancedSubGraph</code> will be asked for its desired layout direction
     * such that children of the subgraph are laid out accordingly to that direction.
     * 
     * @param ep the editpart
     */
    protected int getLayoutDirection(GraphicalEditPart ep) {
    	return PositionConstants.SOUTH;
    }

    private boolean isAutoSizeOn(AdvancedSubGraph subGraph, IGraphicalEditPart gEP) {
        if (gEP instanceof CompartmentEditPart && subGraph.getParent() instanceof AdvancedSubGraph){
            if (((AdvancedSubGraph)subGraph.getParent()).isAutoSize())
                return true;
        }else {
            View notationView = gEP.getNotationView();
            if (notationView !=null && notationView instanceof org.eclipse.gmf.runtime.notation.Node){
                org.eclipse.gmf.runtime.notation.Node node = (org.eclipse.gmf.runtime.notation.Node)notationView;
                LayoutConstraint contraint = node.getLayoutConstraint();
                if (contraint instanceof Size){
                    Size size = (Size)contraint;
                    if (size.getHeight() != -1 || size.getWidth()!=-1){
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#createGraphLayout()
     */
    protected DirectedGraphLayout createGraphLayout() {
        return new CompositeDirectedGraphLayout();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#createNodeChangeBoundCommands(org.eclipse.draw2d.graph.DirectedGraph, org.eclipse.draw2d.geometry.Point)
     */
    protected Command createNodeChangeBoundCommands(DirectedGraph g, Point diff) {
        CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
        NodeList list = new NodeList();
        NodeList subGraphs = ((CompoundDirectedGraph) g).nodes;
        list.addAll(subGraphs);
        for (Iterator iter = subGraphs.iterator(); iter.hasNext();) {
            Node element = (Node) iter.next();
            if (element instanceof Subgraph)
                list.addAll(getAllMembers((Subgraph) element));
        }
        createSubCommands(diff, list.listIterator(), cc);
        if (cc.isEmpty())
            return null;
        return cc;
    }

    private Collection getAllMembers(Subgraph element) {
        NodeList list = new NodeList();
        list.addAll(element.members);
        for (Iterator iter = element.members.iterator(); iter.hasNext();) {
            Node node = (Node) iter.next();
            if (node instanceof Subgraph)
                list.addAll(getAllMembers((Subgraph) node));
        }
        return list;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#getNodeMetrics(org.eclipse.draw2d.graph.Node)
     */
    protected Rectangle getNodeMetrics(Node n) {
        Rectangle rect = null;
        if (n.getParent() instanceof VirtualNode) {
            Node parent = n.getParent();
            rect = new Rectangle(n.x + parent.x, n.y + parent.y, n.width,
                n.height);
        } else
            rect = new Rectangle(n.x, n.y, n.width, n.height);
        return translateFromGraph(rect);
    }
    
    protected void postProcessGraph(DirectedGraph g, Hashtable editPartToNodeDict) {
        //default do nothing
    }
    
    /**
     * @param gep
     * @return
     */
    protected boolean hasChildren(IGraphicalEditPart gep) {
        List children = gep.getChildren();
        boolean hasChildren = false;
        if (!children.isEmpty()){
            for (Iterator iter = children.iterator(); iter.hasNext() && !hasChildren;) {
                Object element = iter.next();
                if (!(element instanceof IBorderItemEditPart) &&
                        ( element instanceof ShapeEditPart ||
                          element instanceof ShapeCompartmentEditPart)){
                    hasChildren = true;
                }else
                    hasChildren = hasChildren((IGraphicalEditPart)element);
            }
        }
        return hasChildren;
    }
    
    /**
     * this method will adjust the passed node Padding; the default implementatio 
     * will use a fixed Padding then it will consider adding extra Padding if the 
     * node parent is not a direct parent
     * clients can override this method to change the behaviour
     * @param node the node to adust the padding for
     */
    protected void adjustNodePadding(Node node,Map editPartToNodeDict) {
        Insets padding  = new Insets(getMapMode().DPtoLP(NODE_PADDING));
        GraphicalEditPart ep = (GraphicalEditPart)node.data;
        // check if the direct parent is added already to the graph
        GraphicalEditPart parent = (GraphicalEditPart)ep.getParent();
        if (parent != null &&
            node.getParent() != null &&
            editPartToNodeDict.get(parent)!=node.getParent()){
            // now the direct parent is not added to the graph so, we had 
            // to adjust the padding of the node to consider the parent
            IFigure thisFigure = parent.getFigure();
            IFigure parentFigure = ((GraphicalEditPart)node.getParent().data).getFigure();
            Point parentLocation = parentFigure.getBounds().getLocation();
            Point nodeLocation = thisFigure.getBounds().getLocation();
            thisFigure.translateToAbsolute(nodeLocation);
            parentFigure.translateToAbsolute(parentLocation);
            Dimension delta = nodeLocation.getDifference(parentLocation);
            Rectangle rect = translateToGraph(new Rectangle(delta.width , delta.height , 0 , 0));
            padding.top  += rect.y ;
            padding.left += rect.x;
        }
        node.setPadding(padding);
        if (node instanceof ConstantSizeNode) {
        	ConstantSizeNode cn = (ConstantSizeNode) node;
            cn.setMinIncomingPadding(getMapMode().DPtoLP(MIN_EDGE_END_POINTS_PADDING));
            cn.setMinOutgoingPadding(getMapMode().DPtoLP(MIN_EDGE_END_POINTS_PADDING));
        }
    }

    
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#createGraph()
     */
    protected DirectedGraph createGraph(){
        return new CompoundDirectedGraph();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#shouldHandleConnectableListItems()
     */
    protected boolean shouldHandleConnectableListItems() {
        return true;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#routeThrough(org.eclipse.draw2d.graph.Edge, org.eclipse.gef.ConnectionEditPart, org.eclipse.draw2d.graph.Node, org.eclipse.draw2d.graph.Node, org.eclipse.draw2d.geometry.PointList, org.eclipse.draw2d.geometry.Point)
	 */
	protected Command routeThrough(Edge edge, ConnectionEditPart connectEP,
			Node source, Node target, PointList points, Point diff) {
		Node parent = source.getParent();
		if (parent == null) {
			parent = target.getParent();
		}
		if (parent != null) {
			Point parentLocation = getNodeMetrics(parent).getLocation();
			points.translate(parentLocation.x, parentLocation.y);
		}
		return super
				.routeThrough(edge, connectEP, source, target, points, diff);
	}
	
}
