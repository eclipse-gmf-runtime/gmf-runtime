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

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.MoveHandle;

import org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;

/**
 * A non-resizable editpolicy for text compartments. It handles the
 * selection and focus feedback of text compartments.
 * 
 * @author melaasar
 * @deprecated Renamed to {@link org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableTextEditPolicy}
 */
public class TextNonResizableEditPolicy
	extends NonResizableEditPolicyEx {

	private WrapLabel getLabel() {
		return ((TextCompartmentEditPart)getHost()).getLabel();

	}

	/**
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#hideFocus()
	 */
	protected void hideFocus() {
		getLabel().setFocus(false);
	}

	/**
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#hideSelection()
	 */
	protected void hideSelection() {
		getLabel().setSelected(false);
		getLabel().setFocus(false);
		super.hideSelection();
	}

	/**
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showFocus()
	 */
	protected void showFocus() {
		getLabel().setFocus(true);
	}

	/**
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#showSelection()
	 */
	protected void showPrimarySelection() {
		super.showPrimarySelection();
		getLabel().setFocus(true);
	}

	/**
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#showSelection()
	 */
	protected void showSelection() {
		super.showSelection();
		getLabel().setSelected(true);
		getLabel().setFocus(false);
	}
	/**
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		MoveHandle moveHandle = new MoveHandle((GraphicalEditPart)getHost());
		moveHandle.setBorder(null);
		moveHandle.setDragTracker(new DragEditPartsTrackerEx(getHost()));
		return Collections.singletonList(moveHandle);
	}

}
