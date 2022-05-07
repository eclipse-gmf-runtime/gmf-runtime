/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import java.util.List;


/**
 * Interface for those edit parts that support surface operations.
 * 
 * @author schafe, cmahoney
 */
public interface ISurfaceEditPart {
	
	/**
	 * Returns true if the surface edit part is to support
	 * the view actions.  False otherwise.
	 * @return boolean isSupportingViewActions
	 */
	public boolean isSupportingViewActions();
	
	/**
	 * Setter for isSupportingViewActions
	 * @param boolean supportsViewActions
	 */
	public void setIsSupportingViewActions(boolean supportsViewActions);
	
	/**
	 * Gets the primary editparts on this surface, that is, the top-level shapes
	 * and connectors.
	 * 
	 * @return List of primary edit parts. If there are none then it returns a
	 *         Collections.EMPTY_LIST, which is immutable
	 */
	public List getPrimaryEditParts();
	
}
