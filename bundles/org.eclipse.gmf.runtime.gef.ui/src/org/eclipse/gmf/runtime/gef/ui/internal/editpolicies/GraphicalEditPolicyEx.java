/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.editpolicies;

import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

/**
 * Extends GEF's <code>GraphicalEditPolicy</code> to add the ability to be 
 * refreshed from its editpart.
 * 
 * @author chmahone
 */
public abstract class GraphicalEditPolicyEx extends GraphicalEditPolicy {

	/**
	 * This method is called when the editpart is refreshed.
	 */
	public void refresh() {
	  // Default behaviour is to do nothing
	}
}
