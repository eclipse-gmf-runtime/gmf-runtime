/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.gef.AccessibleHandleProvider;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

/**
 * This edit policy does not allow elements to be moved
 * 
 * @author Wayne Diu, wdiu
 */
public class ConnectorUnmoveableEditPolicy extends ConnectorEndpointEditPolicy {
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