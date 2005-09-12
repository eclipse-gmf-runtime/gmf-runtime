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

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;


/**
 * Interface for those edit parts that support surface operations.
 * 
 * @author schafe
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
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
	
}
