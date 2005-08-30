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
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutNodesOperation;


/** 
 * A service that provides for diagram layout.
 * 
 * @author schafe
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
final public class LayoutService
    extends Service implements ILayoutNodesProvider {

    private final static LayoutService instance =
        new LayoutService();

    protected LayoutService() {
        super(); //no caching for now
    }

    public static LayoutService getInstance() {
        return instance;
    }

    /**
      * Executes the specified layout operation using the FIRST
      * execution strategy.
      * 
      * @param operation
      * @return Object
      */
    private Object execute(LayoutNodesOperation operation) { 
        List results = execute(ExecutionStrategy.FIRST, operation);
        return results.isEmpty() ? null : results.get(0);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutProvider#layoutNodes(java.util.Map, int, int, org.eclipse.core.runtime.IAdaptable)
     */
    public Runnable layoutNodes(
            List layoutNodes, boolean offsetFromBoundingBox,
    		IAdaptable layoutHint) {
    	Assert.isNotNull(layoutNodes);
    	Assert.isNotNull(layoutHint);
    	return (Runnable)execute(new LayoutNodesOperation(layoutNodes, offsetFromBoundingBox, layoutHint));
    }
}
