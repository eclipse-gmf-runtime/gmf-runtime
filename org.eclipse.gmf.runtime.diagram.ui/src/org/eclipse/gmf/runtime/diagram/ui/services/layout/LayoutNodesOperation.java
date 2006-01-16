/******************************************************************************
 * Copyright (c) 2002, 2004, 2005 IBM Corporation and others.
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

/**
 * @author sshaw
 * @deprecated use
 *             {@link org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodeOperation }
 *             Will be removed on December 16th / 2005
 */
public class LayoutNodesOperation
		extends
		org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutNodesOperation {

	public LayoutNodesOperation(List layoutNodes,
			boolean offsetFromBoundingBox, IAdaptable layoutHint) {
		super(layoutNodes, offsetFromBoundingBox, layoutHint);
	}

}
