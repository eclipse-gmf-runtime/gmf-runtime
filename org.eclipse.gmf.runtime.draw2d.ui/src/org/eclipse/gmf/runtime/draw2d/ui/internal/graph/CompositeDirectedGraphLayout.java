/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.gmf.runtime.draw2d.ui.graph.GMFDirectedGraphLayout;



/**
 * @author mmostafa
 * 
 * Composite layout that layout the passed graph in a recursive fashion 
 *
 */
public class CompositeDirectedGraphLayout
    extends DirectedGraphLayout {
	
	private int graphDirection = PositionConstants.SOUTH;

    /* (non-Javadoc)
     * @see org.eclipse.draw2d.graph.DirectedGraphLayout#visit(org.eclipse.draw2d.graph.DirectedGraph)
     */
    public void visit(DirectedGraph graph) {
		graphDirection = graph.getDirection();
		layoutNodes(graph.nodes, false);
	}
    
    private void layoutNodes(NodeList nodes, boolean virtualPass) {
        EdgeList edges = new EdgeList();
        for (Iterator iter = nodes.iterator(); iter.hasNext();) {
            Node element = (Node) iter.next();
            if (element instanceof Subgraph && !(element instanceof VirtualNode)){
                layoutNodes(((Subgraph)element).members,virtualPass);
            }
            for (Iterator edgesIter = element.outgoing.iterator(); edgesIter.hasNext();) {
                Edge edge = (Edge)edgesIter.next();
                if (nodes.contains(edge.target)){
                    edges.add(edge);
                }
            }
        }
        if (!virtualPass){
            virtualNodesToNodes virtualNodesNodes = new virtualNodesToNodes();
            createVirtualNodes(nodes, edges, virtualNodesNodes);
            NodeList vituralNodes = virtualNodesNodes.getVirtualNodes();
            int size = vituralNodes.size();
            if (size > 0){
                edges = virtualNodesNodes.getEdges();
                for (Iterator iter = vituralNodes.iterator(); iter.hasNext();) {
                    Subgraph virtualNode = (Subgraph) iter.next();
                    layoutNodes(virtualNode.members, true);
                }
                adjustVirtualNodesWidthAndHeight(vituralNodes);
            }
        }
        Map nodeToOutGoing = new HashMap();
        Map nodeToIncomingGoing = new HashMap();
        removeDisconnectedEdges(nodes, nodeToOutGoing, nodeToIncomingGoing);
        if (nodes.size() > 0){
            Node parent  = getParent(nodes.getNode(0));
            DirectedGraph g = new DirectedGraph();
            g.nodes = nodes;
            g.edges = edges;
            AdvancedSubGraph advancedSubgraphParent = parent instanceof AdvancedSubGraph ? (AdvancedSubGraph)parent : null;
            if (advancedSubgraphParent != null) {
           		g.setDirection(advancedSubgraphParent.getDirection());
            } else {
            	g.setDirection(graphDirection);            	
            }
            DirectedGraphLayout layout = new GMFDirectedGraphLayout();
            layout.visit(g);
            if (advancedSubgraphParent != null && advancedSubgraphParent.isAutoSize()) {
                advancedSubgraphParent.width = g.getLayoutSize().width;
                advancedSubgraphParent.height = g.getLayoutSize().height;
            }
        }
        
        restoreDisconnectedEdges(nodeToOutGoing, nodeToIncomingGoing);
    }

    private void restoreDisconnectedEdges(Map nodeToOutGoing, Map nodeToIncomingGoing) {
        restoreEdges(nodeToOutGoing.entrySet(),true);
        restoreEdges(nodeToIncomingGoing.entrySet(),false);
    }

    private void removeDisconnectedEdges(NodeList nodes, Map nodeToOutGoing, Map nodeToIncomingGoing) {
        for (Iterator iter = nodes.iterator(); iter.hasNext();) {
            Node element = (Node) iter.next();
            pushExtraEdges(nodes, nodeToOutGoing, element, element.outgoing,false);
            pushExtraEdges(nodes, nodeToIncomingGoing, element, element.incoming,true);
        }
    }

    private void createVirtualNodes(NodeList nodes, EdgeList edges, virtualNodesToNodes virtualNodesNodes) {
        Set handledEdges = new HashSet();
        recursiveHandleVirtualNode(nodes, edges, virtualNodesNodes, handledEdges, new HashSet(nodes));
    }

    private void recursiveHandleVirtualNode(NodeList nodes, EdgeList edges, virtualNodesToNodes virtualNodesNodes, Set handledEdges, Set nodesSnapeShot) {
        for (Iterator edgeIter = edges.iterator(); edgeIter.hasNext();) {
            Edge element = (Edge) edgeIter.next();
            if (handledEdges.contains(element))
                continue;
            handledEdges.add(element);
            if (!nodesSnapeShot.contains(element.source) || !nodesSnapeShot.contains(element.target))
                continue;
            Node source = element.source;
            Node target = element.target;
            boolean sourceHandled = true;
            boolean targetHandled = true;
            Subgraph sg = virtualNodesNodes.getVirtualContainer(source);
            Subgraph sg1 = virtualNodesNodes.getVirtualContainer(target);
            if (sg==null){
                sourceHandled = false;
                sg = sg1;
            }
            if (sg1==null)
                targetHandled = false;
            if (sourceHandled == false && targetHandled==false){
                sg = new VirtualNode(null,source.getParent());
                sg.setPadding(new Insets(30));
                if (source.getParent()==null)
                    nodes.add(sg);
            }
            if (!sourceHandled){
                addNode(sg, source, nodes);
                virtualNodesNodes.addNode(sg, source);
            }
            if (!targetHandled){
                addNode(sg, target, nodes);
                virtualNodesNodes.addNode(sg, target);
            }
            // order is important; so we should start handling the outgoing and the incoming
            // edges only after the source and target had been handled
            if (!sourceHandled){
                recursiveHandleVirtualNode(nodes,source.outgoing,virtualNodesNodes,handledEdges,nodesSnapeShot);
                recursiveHandleVirtualNode(nodes,source.incoming,virtualNodesNodes,handledEdges,nodesSnapeShot);
            }
            if (!targetHandled){
                recursiveHandleVirtualNode(nodes,target.outgoing,virtualNodesNodes,handledEdges,nodesSnapeShot);
                recursiveHandleVirtualNode(nodes,target.incoming,virtualNodesNodes,handledEdges,nodesSnapeShot);
            }
        }
    }

    private void pushExtraEdges(NodeList nodes, Map nodeToIncomingGoing, Node element, List list,boolean sourceCheck) {
        List edges = new ArrayList();
        for (Iterator iterator = list.iterator() ; iterator.hasNext();) {
            Edge edge = (Edge) iterator.next();
            Node nodeToCheck = sourceCheck ? edge.source : edge.target;
            if (!nodes.contains(nodeToCheck)){
                edges.add(edge);
                iterator.remove();
                Node sourceNode = null;
                Node targetNode = null;
                sourceNode = getParent(edge.source);
                targetNode = getParent(edge.target);
                sourceNode = (!sourceCheck || sourceNode!=null )? sourceNode : edge.source;
                targetNode = ( sourceCheck || targetNode!=null)? targetNode : edge.target;
                if (!sourceCheck &&
                    sourceNode!= null && targetNode!=null && sourceNode!=targetNode &&
                    (edge.source!=sourceNode || edge.target!=targetNode)){
                    Edge virtualEdge = new Edge(sourceNode,
                                                targetNode,
                                                edge.getDelta(),
                                                edge.weight);
                    virtualEdge.setPadding(edge.getPadding());
                }
            }
        }
        if (!edges.isEmpty())
            nodeToIncomingGoing.put(element,edges);
    }

    private Node getParent(Node node) {
        Node parent  = node.getParent();
        if (parent != null && parent instanceof VirtualNode)
            parent = parent.getParent();
        return parent;
    }

    private void restoreEdges(Set entries, boolean outgoing) {
        for (Iterator iter = entries.iterator(); iter.hasNext();) {
            Map.Entry entry =   (Map.Entry) iter.next();
            Node node = (Node)entry.getKey();
            List edgesList = (List)entry.getValue();
            for (Iterator iterator = edgesList.iterator(); iterator.hasNext();) {
                Edge edgeToRestore = (Edge) iterator.next();
                if (outgoing)
                    node.outgoing.add(edgeToRestore);
                else
                    node.incoming.add(edgeToRestore);
            }
            
        }
    }

    private void adjustVirtualNodesWidthAndHeight(NodeList vituralNodes) {
        for (Iterator iter = vituralNodes.iterator(); iter.hasNext();) {
            Subgraph subGraph = (Subgraph) iter.next();
            adjustVirtualNodeWidthAndHeight(subGraph);
        }
        
    }

    private void adjustVirtualNodeWidthAndHeight(Subgraph subGraph) {
       NodeList nodes = subGraph.members;
       if (nodes.isEmpty())
           return;
       int size = nodes.size();
       Node node = nodes.getNode(0);
       int top=node.y,left=node.x,bottom = top + node.height ,right = left+node.width;
       for (int index = 1 ; index<size; index++) {
           node = (Node)nodes.get(index);
           if (top>node.y)
               top = node.y;
           if (bottom < (node.y+node.height))
               bottom = node.y+node.height;
           if (left>node.x)
               left = node.x;
           if (right<(node.x+node.width))
               right = node.x+node.width;
       }
       subGraph.width = right - left;
       subGraph.height = bottom - top;
     }
    
    /**
     * If the node passed in is in autosize mode, then this method will set the
     * width and height of this node based on how its children/members were
     * arranged.
     * 
     * @param subGraph
     *            the node whose size will be adjusted
     */
//    private void adjustAutoSizeNodeWidthAndHeight(AdvancedSubGraph subGraph) {
//    	if (!subGraph.isAutoSize()) {
//    	    return;
//    	}
//        NodeList nodes = subGraph.members;
//        if (nodes.isEmpty())
//            return;
//        int size = nodes.size();
//        Node node = nodes.getNode(0);
//        int top=node.y,left=node.x,bottom = top + node.height ,right = left+node.width;
//        Node topNode, leftNode;
//        topNode = leftNode = node;
//        for (int index = 1 ; index<size; index++) {
//            node = (Node)nodes.get(index);
//            if (top>node.y){
//                top = node.y;
//                topNode = node;
//            }
//            if (bottom < (node.y+node.height))
//                bottom = node.y+node.height;
//            if (left>node.x){
//                left = node.x;
//                leftNode = node;
//            }
//            if (right<(node.x+node.width))
//                right = node.x+node.width;
//        }
//        int xDiff = 0 ;
//        int yDiff = 0 ;
//        if (subGraph.isHasBufferedZone()){
//             xDiff = leftNode.x;
//             yDiff = topNode.y ;
//        }
//        subGraph.width = right - left + xDiff;
//        subGraph.height = bottom - top + yDiff;
//        
//    }

    private void addNode(Subgraph parent, Node node, NodeList nodes) {
        if (node.getParent()!=null)
            node.getParent().members.remove(node);
        node.setParent(parent);
        parent.addMember(node);
        nodes.remove(node);
    }
    
    private class virtualNodesToNodes extends HashMap{
        Set virtualNodes = new HashSet();
        public void addNode(Subgraph sg, Node node){
            virtualNodes.add(sg);
            put(node, sg);
        }
        
        public EdgeList getEdges() {
            EdgeList edges = new EdgeList();
            for (Iterator iter = virtualNodes.iterator(); iter.hasNext();) {
                Node element = (Node) iter.next();
                for (Iterator iterator = element.outgoing.iterator(); iterator
                    .hasNext();) {
                    Edge edge = (Edge) iterator.next();
                    if (virtualNodes.contains(edge.target))
                        edges.add(edge);
                    
                }
            }
            return edges;
        }

        public Subgraph getVirtualContainer(Node node){
            return (Subgraph)get(node);
        }
        
        public NodeList getVirtualNodes(){
            NodeList nodeList = new NodeList();
            nodeList.addAll(virtualNodes);
            return nodeList;
        }
    }
}
