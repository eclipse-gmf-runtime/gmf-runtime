/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.gef.EditPolicy;

/**
 * Tree edit part customized for a diagram.
 *
 * @author schafe
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class TreeDiagramEditPart extends TreeContainerEditPart{
	
	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		removeEditPolicy(EditPolicy.COMPONENT_ROLE);
	}
	
	/**
	 * Constructor
	 * @param model
	 */
	public TreeDiagramEditPart(Object model) {
		super(model);
	}
	
}
