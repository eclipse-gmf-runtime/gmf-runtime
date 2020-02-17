/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.gef.AccessibleHandleProvider;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

/**
 * This edit policy does not allow elements to be moved
 * 
 * @author Wayne Diu, wdiu
 */
public class UnmovableConnectionEditPolicy extends org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy {
	/**
	 * Just override getCommand to always return null.
	 * 
	 * Then, it won't support move or align, and orphan because aligning
	 * means moving the element and orphan implies that the element has
	 * moved somewhere.
	 * 
	 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request request) {
		return null;
	}
	
	/**
	 * Since unmoveable, override to never return an AccessibleHandleProvider
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == AccessibleHandleProvider.class) {
			return null;
		}
		return super.getAdapter(key);
	}
}