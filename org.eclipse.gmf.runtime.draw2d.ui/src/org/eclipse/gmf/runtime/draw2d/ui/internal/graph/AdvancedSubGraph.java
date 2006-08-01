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
 * Advanced node introduce the AutoSize feature to the Draw2d SubGraph 
 *
 */
public class AdvancedSubGraph
    extends Subgraph {
    
    private boolean autoSize = false;
    private boolean hadBufferZone = false;
    
    public AdvancedSubGraph(Object data) {
        super(data, null);
    }

    public AdvancedSubGraph(Object data, Subgraph parent) {
        super(data, parent);
    }
    
    /**
     * set the auto size field
     * @param newValue
     */
    public void setAutoSize(boolean newValue){
        autoSize = newValue;
    }
    
    /**
     * access the auto size field
     * @return true is auto size is ON other wise false
     */
    public boolean isAutoSize(){
        return autoSize;
    }
    
    /**
     * set the AHs Buffered Zone field
     * @param newValue
     */
    public void setHasBufferedZone(boolean newValue){
        hadBufferZone = newValue;
    }
    
    /**
     * access the had buffered zone field
     * @return true is auto size is ON other wise false
     */
    public boolean isHasBufferedZone(){
        return hadBufferZone;
    }

}
