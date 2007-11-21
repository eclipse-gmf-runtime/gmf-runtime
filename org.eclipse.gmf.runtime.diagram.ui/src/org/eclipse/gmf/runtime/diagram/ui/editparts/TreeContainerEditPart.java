/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Implementation for the regular tree edit part
 * 
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class TreeContainerEditPart extends TreeEditPart {

	/**
	 * Constructor
	 * @param model
	 */
	public TreeContainerEditPart(Object model) {
		super(model);
	}

	/**
	 * Returns the children of this from the model,
	 * as this is capable enough of holding EditParts.
	 *
	 * @return  List of children.
	 */
	protected List getModelChildren() {
		if (getModel() instanceof View)
			return ((View) getModel()).getChildren();
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.TreeEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handleNotificationEvent(Notification event) {
		Object feature = event.getFeature();
		if (NotationPackage.eINSTANCE.getView_PersistedChildren()==feature||
			NotationPackage.eINSTANCE.getView_TransientChildren()==feature)
			refreshChildren();
		else
			super.handleNotificationEvent(event);
	}

}
