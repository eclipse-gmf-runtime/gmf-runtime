/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
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

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gmf.runtime.diagram.ui.label.ILabelDelegate;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;

/**
 * A non-resizable editpolicy for text compartments. It handles the selection
 * and focus feedback of text compartments.
 * 
 * @author melaasar
 */
public class NonResizableTextEditPolicy
	extends NonResizableEditPolicyEx {

    /**
     * Gets the label delegate that is used to interact with the label figure
     * inside the host editpart.
     * 
     * @return the label delegate
     */
    private ILabelDelegate getLabel() {
        return (ILabelDelegate) getHost().getAdapter(ILabelDelegate.class);
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
		MoveHandle moveHandle = new MoveHandle((GraphicalEditPart) getHost());
		moveHandle.setBorder(null);
		moveHandle.setDragTracker(new DragEditPartsTrackerEx(getHost()));
		return Collections.singletonList(moveHandle);
	}

}
