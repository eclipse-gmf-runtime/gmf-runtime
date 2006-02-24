package org.eclipse.gmf.runtime.draw2d.ui.internal.graph;

import org.eclipse.draw2d.graph.Subgraph;


public class VirtualNode
    extends Subgraph {

    /**
     * @param data
     */
    public VirtualNode(Object data) {
        super(data);
    }
    
    /**
     * @param data
     * @param parent
     */
    public VirtualNode(Object data, Subgraph parent) {
        super(data, parent);
    }

}
