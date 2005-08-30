/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
