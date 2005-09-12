/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.IEditPartLayoutProvider;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.IInternalLayoutRunnable;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author sshaw
 *
 * AbstractLayoutProvider for internal layout providers.  Provides a redirection from the
 * Node based api to the internal EditPart based api.
 */
abstract public class AbstractLayoutProvider extends AbstractProvider 
	implements IEditPartLayoutProvider {
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider#layoutNodes(java.util.List, boolean, org.eclipse.core.runtime.IAdaptable)
	 */
	public Runnable layoutNodes(List layoutNodes,
			boolean offsetFromBoundingBox, IAdaptable layoutHint) {
		IGraphicalEditPart gep = (IGraphicalEditPart)layoutHint.getAdapter(IGraphicalEditPart.class);
		List editparts = new ArrayList(layoutNodes.size());
		Assert.isNotNull(gep, "The Graphical EditPart is null"); //$NON-NLS-1$

		Map registry = gep.getViewer().getEditPartRegistry();
		Iterator nodes = layoutNodes.listIterator();
		while (nodes.hasNext()) {
			Node view = ((ILayoutNode)nodes.next()).getNode();
			editparts.add(registry.get(view));
		}
		
		if (offsetFromBoundingBox) {
			final Command cmdSelect = layoutEditParts(editparts, layoutHint);
			return new IInternalLayoutRunnable() {
				public void run() {
					cmdSelect.execute(); 
				}
				
				public Command getCommand() {
					return cmdSelect;
				}
			};
		}
		else {
			final Command cmdDiag = layoutEditParts(gep, layoutHint);
		
			return new IInternalLayoutRunnable() {
				public void run() {
					cmdDiag.execute();
				}
				
				public Command getCommand() {
					return cmdDiag;
				}
			};
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.IEditPartLayoutProvider#layoutEditParts(org.eclipse.gef.GraphicalEditPart, org.eclipse.core.runtime.IAdaptable)
	 */
	abstract public Command layoutEditParts(
        GraphicalEditPart containerEditPart,
		IAdaptable layoutHint);

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.IEditPartLayoutProvider#layoutEditParts(java.util.List, org.eclipse.core.runtime.IAdaptable)
     */
    abstract public Command layoutEditParts(
        List selectedObjects,
		IAdaptable layoutHint);
	
	/**
	 * getContainer
	 * @param operation
	 * @return the <code>View</code> that contains the layout operation
	 */
	protected View getContainer(IOperation operation) {
		View container = null;
		
		if (operation instanceof ILayoutNodesOperation) {
			Iterator nodes = ((ILayoutNodesOperation)operation).getLayoutNodes().listIterator();
			if (nodes.hasNext()) { 
				Node node = ((ILayoutNode)nodes.next()).getNode();
				container = ViewUtil.getContainerView(node);
			}
		}
		else {
			return null;
		}
		
		return container;
	}
	
	/**
	 * getViewsToSizesMap
	 * @param operation
	 * @return <code>Map</code>
	 */
	protected Map getViewsToSizesMap(IOperation operation) {
		if (operation instanceof ILayoutNodesOperation) { 
			List layoutNodes = ((ILayoutNodesOperation)operation).getLayoutNodes();  
			Map viewsToSizesMap = new HashMap(layoutNodes.size());
			Iterator nodes = layoutNodes.listIterator();
			while (nodes.hasNext()) {
				ILayoutNode layoutNode = ((ILayoutNode)nodes.next());
				Node node = layoutNode.getNode();
				
				viewsToSizesMap.put(node, new org.eclipse.draw2d.geometry.Dimension(layoutNode.getWidth(), layoutNode.getHeight()));
			}
			
			return viewsToSizesMap;
		}
		
		return null;
	}
}
