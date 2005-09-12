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

package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;


/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * Override for List compartments that removes deletion capability.
 */
public class ListComponentEditPolicy
	extends ComponentEditPolicy {

	/** 
	 * Return to make the <code>GraphicalEditPart</code>'s figure not visible.
	 * @param deleteRequest the original delete request.
	 */
	protected Command createDeleteViewCommand(GroupRequest deleteRequest) {
		return null;		
	}

	/** 
	 * Returns null.
	 * @see #shouldDeleteSemantic()
	 * @param deleteRequest the original delete request.
	 */
	protected Command createDeleteSemanticCommand(GroupRequest deleteRequest) {
		return null;
	}
}
