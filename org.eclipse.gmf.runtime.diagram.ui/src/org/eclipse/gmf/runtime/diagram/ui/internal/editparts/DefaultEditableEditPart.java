/******************************************************************************
 * Copyright (c) 2003, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IEditableEditPart;

/**
 * Default implementation of the <code>IEditableEditPart</code> interface
 * 
 * @author Vishy Ramaswamy
 */
public class DefaultEditableEditPart
	implements IEditableEditPart {

	/**
	 * The edit part
	 */
	private final GraphicalEditPart editPart;

	/**
	 * Flag to indicate if the edit part is in edit mode
	 */
	private boolean isEditable = true;

	/**
	 * Constructor
	 * 
	 * @param editPart
	 *            the edit part
	 */
	public DefaultEditableEditPart(GraphicalEditPart editPart) {
		super();
		Assert.isNotNull(editPart);
		this.editPart = editPart;

	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IEditableEditPart#isEditModeEnabled()
	 */
	public boolean isEditModeEnabled() {
		return this.isEditable;
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IEditableEditPart#disableEditMode()
	 */
	public void disableEditMode() {
		if (!isEditModeEnabled()) {
			return;
		}

		List l = getGraphicalEditPart().getSourceConnections();
		for (int i = 0; i < l.size(); i++) {
			Object obj = l.get(i);
			if ( obj instanceof IEditableEditPart) {
				((IEditableEditPart)obj).disableEditMode();
			}
		}

		List c = getGraphicalEditPart().getChildren();
		for (int i = 0; i < c.size(); i++) {
			Object obj = c.get(i);
			if ( obj instanceof IEditableEditPart) {
				((IEditableEditPart)obj).disableEditMode();
			}
		}

		this.isEditable = false;
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IEditableEditPart#enableEditMode()
	 */
	public void enableEditMode() {
		if (isEditModeEnabled()) {
			return;
		}

		this.isEditable = true;

		List c = getGraphicalEditPart().getChildren();
		for (int i = 0; i < c.size(); i++) {
			Object obj = c.get(i);
			if ( obj instanceof IEditableEditPart) {
				((IEditableEditPart)obj).enableEditMode();
			}
		}

		List l = getGraphicalEditPart().getSourceConnections();
		for (int i = 0; i < l.size(); i++) {
			Object obj = l.get(i);
			if ( obj instanceof IEditableEditPart) {
				((IEditableEditPart)obj).enableEditMode();
			}
		}
	}

	/**
	 * Returns the editPart.
	 * 
	 * @return the editPart.
	 */
	private GraphicalEditPart getGraphicalEditPart() {
		return this.editPart;
	}
}
