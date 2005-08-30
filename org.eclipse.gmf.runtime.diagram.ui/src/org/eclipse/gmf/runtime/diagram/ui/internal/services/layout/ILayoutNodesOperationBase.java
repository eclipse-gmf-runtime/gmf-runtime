/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * Interface describing the layout operation for a set of nodes with corresponding sizes.
 * 
 * This interface can be used in the layout provider implementation class.
 * 
 * @author sshaw
 * @canBeSeenBy %level1
 */
public interface ILayoutNodesOperationBase extends IOperation {

	/**
	 * getLayoutNodes
	 * Gets the list of nodes to layout.
	 * 
	 * @return List of ILayoutNode objects that are to participate in the layout 
	 * operation.
	 */
	public abstract List getLayoutNodes(); 
	
	/**
	 * shouldOffsetFromBoundingBox
	 * 
	 * @return boolean indicating whether the Nodes should be laid out relative
     * to the bounding box of the Nodes in the nodesToSizes Map.
	 */
	public abstract boolean shouldOffsetFromBoundingBox();
	
	/**
	 * getLayoutHint
     * Retrieves the value of the <code>layoutHint</code> instance variable.
     * 
     * @return IAdaptable hint to the provider to determine the layout kind.  IAdaptable will 
     * typically adapt to string that can be one of ILayoutNodesProvider.DEFAULT_LAYOUT or 
     * ILayoutNodesProvider.RADIAL_LAYOUT.  
     */
    public IAdaptable getLayoutHint();
}
