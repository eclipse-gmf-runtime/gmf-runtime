/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.graph;

import org.eclipse.draw2d.graph.Subgraph;

/**
 * @author mmostafa
 *  for Internal use 
 *
 */

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
