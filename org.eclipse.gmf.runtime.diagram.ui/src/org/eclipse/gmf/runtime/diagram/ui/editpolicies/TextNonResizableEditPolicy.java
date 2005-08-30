/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
