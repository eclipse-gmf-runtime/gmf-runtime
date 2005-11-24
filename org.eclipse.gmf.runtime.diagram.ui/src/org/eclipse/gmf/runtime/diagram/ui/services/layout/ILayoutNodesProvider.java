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
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The interface for providers of the layout provider extension point.
 * 
 * Implement this interface to contribute an extension to the "layoutProviders"
 * extension point. The layoutNodes(List, boolean, IAdaptable) method call is
 * invoked by internal diagram code for "arrange" actions.
 * 
 * Consideration of dependencies has to be done when choosing the priority of
 * the provider. The layout operation is executed on the first provider (in
 * descending order of priority) that is found to provide the operation.
 * 
 * Implementors are not expected to start read/write operations.
 * 
 * @author schafe, sshaw
 * @deprecated use {@link ILayoutNodeProvider} Will be removed on December 16th /
 *             2005
 */
public interface ILayoutNodesProvider extends IProvider {

	/**
	 * @deprecated use {#link
	 *             {@link org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType#DEFAULT}
	 */
	public static String DEFAULT_LAYOUT = LayoutType.DEFAULT;

	/**
	 * @deprecated use {#link
	 *             {@link org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType#RADIAL}
	 */
	public static String RADIAL_LAYOUT = LayoutType.RADIAL;

	/**
	 * @deprecated use
	 *             {@link ILayoutNodeProvider#layoutLayoutNodes(List, boolean, IAdaptable)}
	 */
	public Runnable layoutNodes(List layoutNodes,
			boolean offsetFromBoundingBox, IAdaptable layoutHint);
}
