/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodeOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodeProvider;


/**
 * @author sshaw
 *
 * The LayoutNodesOperation is an operation used to layout all of the members of a map
 * of Nodes. 
 */
public class LayoutNodesOperation implements ILayoutNodeOperation {
	
	private List layoutNodes = null;
	private boolean offsetFromBoundingBox;
	private final IAdaptable layoutHint;
    
    /**
     *  Constructs a new layout operation with the specified parameters
     * @param layoutNodes   nodes to layout
     * @param offsetFromBoundingBox  the offset
     * @param layoutHint the layout hint
     */
    public LayoutNodesOperation(
        List layoutNodes, boolean offsetFromBoundingBox,
		IAdaptable layoutHint) {            
            super();
            Assert.isNotNull(layoutNodes);	            
            this.layoutNodes = layoutNodes; 
            this.offsetFromBoundingBox = offsetFromBoundingBox;
            this.layoutHint = layoutHint;
    }
    
	public List getLayoutNodes() {
		return layoutNodes;
	}
	
	public boolean shouldOffsetFromBoundingBox() {
		return offsetFromBoundingBox;
	}
	
    /**
     * Retrieves the value of the <code>layoutHint</code> instance variable.
     * 
     * @return IAdaptable layoutHint
     */
    public final IAdaptable getLayoutHint() {
        return this.layoutHint;
    } 
	
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
     */
    public Object execute(IProvider provider) {
        Assert.isNotNull(provider);

        return ((ILayoutNodeProvider) provider).layoutLayoutNodes(
        	getLayoutNodes(), shouldOffsetFromBoundingBox(), getLayoutHint());
    }
}
