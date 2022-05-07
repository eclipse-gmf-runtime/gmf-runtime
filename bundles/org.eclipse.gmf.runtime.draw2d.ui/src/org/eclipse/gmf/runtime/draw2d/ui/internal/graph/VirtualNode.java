/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
    extends AdvancedSubGraph {

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
