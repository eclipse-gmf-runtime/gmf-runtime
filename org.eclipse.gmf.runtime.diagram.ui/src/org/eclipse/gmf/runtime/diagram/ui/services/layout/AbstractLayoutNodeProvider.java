/******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.layout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author sshaw
 * 
 * Abstract provider for internal layout providers. Provides a redirection from
 * the Node based api to the internal EditPart based api.
 */
abstract public class AbstractLayoutNodeProvider extends AbstractProvider
		implements ILayoutNodeProvider {

	/**
	 * Retrieves the common container that will be the target for the layout
	 * operation.
	 * 
	 * @param operation
	 *            the <code>IOperation</code> that gets thee contributing
	 *            nodes to the layout to calculate the container from.
	 * @return the <code>View</code> that will be the target for the layout
	 *         operation
	 */
	protected View getContainer(IOperation operation) {
		View container = null;

		if (operation instanceof ILayoutNodeOperation) {
			Iterator nodes = ((ILayoutNodeOperation) operation)
					.getLayoutNodes().listIterator();
			if (nodes.hasNext()) {
				Node node = ((ILayoutNode) nodes.next()).getNode();
				container = ViewUtil.getContainerView(node);
			}
		} else {
			return null;
		}

		return container;
	}

	/**
	 * Gets a <code>Map</code> where the keys are the notation
	 * <code>Node</code> and the associated value is a
	 * <code>org.eclipse.draw2d.geometry.Dimension</code> object.
	 * 
	 * @param operation
	 *            <code>ILayoutNodeOperation</code> to retrieve the nodes
	 *            sizes
	 * @return <code>Map</code>
	 */
	protected Map getNodeToSizeMap(ILayoutNodeOperation operation) {
		List layoutNodes = operation.getLayoutNodes();
		Map viewsToSizesMap = new HashMap(layoutNodes.size());
		Iterator nodes = layoutNodes.listIterator();
		while (nodes.hasNext()) {
			ILayoutNode layoutNode = ((ILayoutNode) nodes.next());
			Node node = layoutNode.getNode();

			viewsToSizesMap.put(node,
					new org.eclipse.draw2d.geometry.Dimension(layoutNode
							.getWidth(), layoutNode.getHeight()));
		}

		return viewsToSizesMap;
	}

	/**
	 * @since 1.3 (1.3.1)
	 */
	public boolean canLayoutNodes(List layoutNodes,
			boolean shouldOffsetFromBoundingBox, IAdaptable layoutHint) {
		return layoutNodes != null && !layoutNodes.isEmpty();
	}

}
