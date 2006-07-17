/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * Interface describing the layout operation for a set of nodes with
 * corresponding sizes.
 * 
 * This interface can be used in the layout provider implementation class.
 * 
 * IMPORTANT: This interface is <EM>not</EM> intended to be implemented by clients.
 * New methods may be added in the future.
 * 
 * @author sshaw
 */
public interface ILayoutNodeOperation extends IOperation {

	/**
	 * Gets the list of nodes to layout.
	 * 
	 * @return <code>List</code> of <code>ILayoutNode</code> objects that
	 *         are to participate in the layout operation.
	 */
	public abstract List getLayoutNodes();

	/**
	 * @return <code>boolean</code> indicating whether the nodes should be
	 *         laid out relative to the bounding box of the original position of
	 *         the nodes that are participating in the layout operation.
	 */
	public abstract boolean shouldOffsetFromBoundingBox();

	/**
	 * Retrieves the value of the <code>layoutHint</code> instance variable.
	 * 
	 * @return <code>IAdaptable</code> hint to the provider to determine the
	 *         layout kind. <code>IAdaptable</code> will typically adapt to
	 *         string that can be one of <code>LayoutType.DEFAULT</code> or
	 *         <code>LayoutType.RADIAL</code>
	 */
	public IAdaptable getLayoutHint();
}
