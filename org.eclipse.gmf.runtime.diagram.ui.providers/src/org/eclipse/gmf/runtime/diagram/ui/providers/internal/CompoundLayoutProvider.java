package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.CompoundDirectedGraphLayout;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;

public abstract class CompoundLayoutProvider
    extends CompositeLayoutProvider {
    
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
     */
    /*public boolean provides(IOperation operation) {
      if (operation instanceof ILayoutNodeOperation) {
            Iterator nodes = ((ILayoutNodeOperation) operation)
                .getLayoutNodes().listIterator();
            if (nodes.hasNext()) {
                View node = ((ILayoutNode) nodes.next()).getNode();
                View container = (View) node.eContainer();
                if (!(container instanceof Diagram)
                    || !((Diagram) container).getType().equals("logic")) //$NON-NLS-1$
                    return false;
            }
        } else {
            return false;
        }
        IAdaptable layoutHint = ((ILayoutNodeOperation) operation)
            .getLayoutHint();
        String layoutType = (String) layoutHint.getAdapter(String.class);
        return LayoutType.DEFAULT.equals(layoutType);
    }*/

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#build_nodes(java.util.List, java.util.Map, org.eclipse.draw2d.graph.Subgraph)
     */
    protected NodeList build_nodes(List selectedObjects, Map editPartToNodeDict, Subgraph rootGraph) {
        ListIterator li = selectedObjects.listIterator();
        NodeList nodes = new NodeList();
        while (li.hasNext()) {
            IGraphicalEditPart gep = (IGraphicalEditPart) li.next();
            boolean hasChildren  = hasChildren(gep);
            if (!(gep instanceof IBorderItemEditPart) &&
                 ( gep instanceof ShapeEditPart ||
                   gep instanceof ShapeCompartmentEditPart)) {
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
                if (hasChildren){
                    if (rootGraph!=null)
                        n = new Subgraph(ep,rootGraph);
                    else
                        n = new Subgraph(ep);
                }
                else{
                    if (rootGraph!=null)
                        n = new Node(ep,rootGraph);
                    else
                        n = new Node(ep);
                }
                adjustNodePadding(n,editPartToNodeDict);
                Dimension size = ep.getFigure().getBounds().getSize();
                setNodeMetrics(n, new Rectangle(position.x, position.y,
                    size.width, size.height));
                editPartToNodeDict.put(ep, n);
                nodes.add(n);
                if (hasChildren){
                    nodes.addAll(build_nodes(gep.getChildren(),editPartToNodeDict,(Subgraph)n));
                }
            }
        }
        return nodes;
    }

    

       /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#createGraphLayout()
     */
    protected DirectedGraphLayout createGraphLayout() {
        return new CompoundDirectedGraphLayout();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#createChangeBoundsCommands(org.eclipse.draw2d.graph.DirectedGraph, org.eclipse.draw2d.geometry.Point)
     */
    protected Command createNodeChangeBoundCommands(DirectedGraph g, Point diff) {
        CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
        ListIterator vi = ((CompoundDirectedGraph)g).subgraphs.listIterator();
        createSubCommands(diff, vi, cc);
        vi = g.nodes.listIterator();
        createSubCommands(diff, vi, cc);
        if (cc.isEmpty())
            return null;
        return cc;
    }

    protected void postProcessGraph(DirectedGraph g, Hashtable editPartToNodeDict) {
        EdgeList edges = g.edges;
        NodeList nodes = g.nodes;
        virtualNodesToNodes virtualNodesNodes = new virtualNodesToNodes();
        for (Iterator edgeIter = edges.iterator(); edgeIter.hasNext();) {
            Edge element = (Edge) edgeIter.next();
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
                sg = new Subgraph(null,source.getParent());
                sg.setPadding(new Insets(0));
                nodes.add(sg);
            }
            if (!sourceHandled){
                addNode(sg, source);
                virtualNodesNodes.addNode(sg, source);
            }
            if (!targetHandled){
                addNode(sg, target);
                virtualNodesNodes.addNode(sg, target);
            }
        }
        
        for (Iterator iter = nodes.iterator(); iter.hasNext();) {
            Node element = (Node) iter.next();
            if (element.getParent() !=null &&
                element instanceof Subgraph &&
                element.data == null &&
                element.getParent().members.size()==1){
                Subgraph sg = (Subgraph)element;
                sg.getParent().members.remove(0);
                sg.getParent().members.addAll(sg.members);
                for (Iterator iterator = sg.getParent().members.iterator(); iterator
                    .hasNext();) {
                    Node node = (Node) iterator.next();
                    node.setParent(sg.getParent());
                }
                
                iter.remove();
            }
        }
    }
    
    private void addNode(Subgraph parent, Node node) {
        if (node.getParent()!=null){
            node.getParent().members.remove(node);
        }
        node.setParent(parent);
        parent.addMember(node);
    }
        
    private class virtualNodesToNodes extends HashMap{
        Set virtualNodes = new HashSet();
        public void addNode(Subgraph sg, Node node){
            virtualNodes.add(sg);
            put(node, sg);
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
