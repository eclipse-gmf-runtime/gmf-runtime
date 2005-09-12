/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TreeContainerEditPart extends TreeEditPart {

	/**
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
		if (getModel() instanceof Diagram)
			return ((Diagram) getModel()).getChildren();
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.TreeEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(Properties.ID_PERSISTED_CHILDREN)||
			event.getPropertyName().equals(Properties.ID_TRANSIENT_CHILDREN))
			refreshChildren();
		else
			super.handlePropertyChangeEvent(event);
	}

}
