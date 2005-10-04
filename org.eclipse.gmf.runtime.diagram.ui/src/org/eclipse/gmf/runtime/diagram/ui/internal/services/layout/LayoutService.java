/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutNodesOperation;
import org.eclipse.jface.util.Assert;

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

	private LayoutService() {
        super(); //no caching for now
		configureProviders(DiagramUIPlugin.getPluginId(), "layoutProviders"); //$NON-NLS-1$
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
