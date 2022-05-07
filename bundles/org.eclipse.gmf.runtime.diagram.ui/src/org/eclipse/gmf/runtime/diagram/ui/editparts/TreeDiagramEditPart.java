/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * Tree edit part customized for a diagram.
 *
 * @author schafe
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

	protected void handleNotificationEvent(Notification event) {
		if (NotationPackage.Literals.DIAGRAM__NAME.equals(event.getFeature())) {
			refreshVisuals();
		} else {
			super.handleNotificationEvent(event);
		}
	}
	
}
