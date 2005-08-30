/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2000, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.Iterator;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.SelectionRequest;

import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IContainedEditPart;
import org.eclipse.gmf.runtime.gef.ui.tools.DelegatingDragEditPartsTracker;
import com.ibm.xtools.notation.View;

/**
 * @author melaasar
 * 
 * An editpart that controls a compartment view
 */
public abstract class CompartmentEditPart extends GraphicalEditPart implements IContainedEditPart {

	/**
	 * Constructs a new compartment edit part
	 * @param view
	 */
	public CompartmentEditPart(View view) {
		super(view);
	}

	/**
	 * By default, all compartment edit parts get selectable only if 
	 * the compartment's top level container matches the top level
	 * container of the editpart that currently has the keyboard focus
	 * i.e: you need to select the shape first before selecting a compartment
	 * but once a compartment is selected, a peer compartment can also be selected
	 * @see org.eclipse.gef.EditPart#isSelectable()
	 */
	public boolean isSelectable() {
		if (super.isSelectable()) {
			EditPart focusPart = getViewer().getFocusEditPart();
			if (focusPart instanceof IGraphicalEditPart) {
				TopGraphicEditPart focusTopEP =
					((IGraphicalEditPart) focusPart).getTopGraphicEditPart();
				TopGraphicEditPart myTopEP = getTopGraphicEditPart();
				if (myTopEP == focusTopEP) {
					// check if the selection contains only editparts belonging to 
					// the same top level editpart
					Iterator selection =
						getViewer().getSelectedEditParts().iterator();
					while (selection.hasNext()) {
						Object editPart = selection.next();
						if (editPart instanceof IGraphicalEditPart
							&& (((IGraphicalEditPart) editPart)
								.getTopGraphicEditPart()
								!= myTopEP))
							return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		if (request instanceof SelectionRequest
			&& ((SelectionRequest) request).getLastButtonPressed() == 3)
			return null;
		return new DelegatingDragEditPartsTracker(
			this,
			getTopGraphicEditPart());
	}

}
