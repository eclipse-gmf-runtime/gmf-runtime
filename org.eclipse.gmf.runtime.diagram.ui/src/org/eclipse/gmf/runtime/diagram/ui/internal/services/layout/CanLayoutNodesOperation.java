/******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
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

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodeProvider;

/**
 * The operation that determines whether the nodes can be laid out
 * 
 * @author aboyko
 *
 * @since 1.3.1
 */
public class CanLayoutNodesOperation extends LayoutNodesOperation {

	/**
	 * Constructs an instance
	 * 
	 * @param layoutNodes
	 *            nodes to layout
	 * @param offsetFromBoundingBox
	 *            <code>true</code> if part of the graph is laid out,
	 *            <code>false<code> for the whole graph layout
	 * @param layoutHint
	 *            the layout hint parameter
	 */
	public CanLayoutNodesOperation(List layoutNodes,
			boolean offsetFromBoundingBox, IAdaptable layoutHint) {
		super(layoutNodes, offsetFromBoundingBox, layoutHint);
	}

	@Override
	public Object execute(IProvider provider) {
        Assert.isNotNull(provider);

        return ((ILayoutNodeProvider) provider).canLayoutNodes(
        	getLayoutNodes(), shouldOffsetFromBoundingBox(), getLayoutHint());
	}

}
