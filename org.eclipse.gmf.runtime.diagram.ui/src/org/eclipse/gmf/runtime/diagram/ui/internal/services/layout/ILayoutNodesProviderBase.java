/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The interface for providers of the layout provider extension point.
 * 
 * Implement this interface to contribute an extension to the
 * "layoutProviders" extension point.
 * The layoutNodes(List, boolean, IAdaptable) method call is 
 * invoked by internal presentation code for "arrange" actions.
 * 
 * Consideration of dependencies has to be done when choosing the priority
 * of the provider.  
 * The layout operation is executed on the first provider 
 * (in descending order of priority) that is found to provide the operation.
 * 
 * Implementors are not expected to start read/write operations.
 * 
 * @author schafe, sshaw 
 * @canBeSeenBy %level1
 */
public interface ILayoutNodesProviderBase extends IProvider {
	 
    /**
     * layoutNodes
     * Layout this map of nodes, using the specified layout hint.
     * 
     * @param layoutNodes List of ILayoutNode objects that are to participate in the 
     * layout.
     * @param offsetFromBoundingBox boolean indicating whether the Nodes should be laid out relative
     * to the bounding box of the Nodes in the nodesToSizes Map.
     * @param layoutHint IAdaptable hint to the provider to determine
	 * the layout kind.  IAdaptable will typically adapt to string that can be one of 
	 * ILayoutNodesProvider.DEFAULT_LAYOUT or ILayoutNodesProvider.RADIAL_LAYOUT.  
     * @return Runnable that contains the layout changes to be executed.
     */ 
    public Runnable layoutNodes(
        List layoutNodes, boolean offsetFromBoundingBox,
		IAdaptable layoutHint);
}
