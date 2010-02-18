/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.layout;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The interface for providers of the layout provider extension point.
 * 
 * The {@link ILayoutNodeProvider#layoutLayoutNodes(List, boolean, IAdaptable)}
 * method call is invoked by internal diagram code for "arrange" actions.
 * 
 * Consideration of dependencies has to be done when choosing the priority of
 * the provider. The layout operation is executed on the first provider (in
 * descending order of priority) that is found to provide the operation.
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * IMPORTANT: This interface is <EM>not</EM> intended to be implemented by
 * clients. Clients should inherit from
 * {@link org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutNodeProvider}.
 * New methods may be added in the future.
 * 
 * @author sshaw
 */
public interface ILayoutNodeProvider extends IProvider {

	/**
	 * Layout this map of nodes, using the specified layout hint.
	 * 
	 * @param layoutNodes
	 *            List of <code>ILayoutNode</code> objects that are to
	 *            participate in the layout.
	 * @param offsetFromBoundingBox
	 *            <code>boolean</code> indicating whether the Nodes should be
	 *            laid out relative to the bounding box of the Nodes in the
	 *            nodesToSizes Map.
	 * @param layoutHint
	 *            <code>IAdaptable</code> hint to the provider to determine
	 *            the layout kind. <code>IAdaptable</code> will typically
	 *            adapt to string that can be one of
	 *            <code>LayoutType.DEFAULT</code> or
	 *            <code>LayoutType.RADIAL</code>.
	 * @return <code>Runnable</code> that contains the layout changes to be
	 *         executed.
	 */
	public Runnable layoutLayoutNodes(List layoutNodes,
			boolean offsetFromBoundingBox, IAdaptable layoutHint);

	/**
	 * Returns <code>true</code> if the nodes can be laid out by the provider,
	 * e.g whether the layout is needed.
	 * 
	 * @param layoutNodes
	 *            List of <code>ILayoutNode</code> objects that are to
	 *            participate in the layout.
	 * @param offsetFromBoundingBox
	 *            <code>boolean</code> indicating whether the Nodes should be
	 *            laid out relative to the bounding box of the Nodes in the
	 *            nodesToSizes Map.
	 * @param layoutHint
	 *            <code>IAdaptable</code> hint to the provider to determine the
	 *            layout kind. <code>IAdaptable</code> will typically adapt to
	 *            string that can be one of <code>LayoutType.DEFAULT</code> or
	 *            <code>LayoutType.RADIAL</code>.
	 * @return <code>true</code> if nodes can be laid out
	 * 
	 * @since 1.3 (1.3.1)
	 */
	public boolean canLayoutNodes(List layoutNodes,
			boolean shouldOffsetFromBoundingBox, IAdaptable layoutHint);
}
