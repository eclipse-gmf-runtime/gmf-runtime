/******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.palette;

import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.tools.PanningSelectionTool;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.BorderItemContainerFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;

/**
 * GMF's customized version of GEF's <code>SelectionTool</code> overridden to:
 * <li>support traversing of shapes on a diagram using the TAB and SHIFT + TAB
 * keys</li>
 * <li>support moving a shape with the arrow keys without first accessing the
 * traverse handles</li>
 * 
 * @author crevells
 */
public class SelectionToolEx
    extends PanningSelectionTool {

    /**
     * Are the traverse handles being used? That is, has the '.' key been
     * pressed first before using the arrow keys?
     */
    private boolean isUsingTraverseHandles = false;

    protected void handleKeyTraversed(TraverseEvent e) {
        resetHover();

        // Tab and shift tab will be used for shape selection on the diagram, so
        // we must override the system tab traversal and ensure we get the key
        // pressed events.
        if (acceptTabKey(e)) {
            e.doit = false;
        } else {
            super.handleKeyTraversed(e);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.tools.SelectionTool#handleKeyDown(org.eclipse.swt.events.KeyEvent)
     */
    protected boolean handleKeyDown(KeyEvent e) {
		resetHover();

		if (acceptTabKey(e)) {
			if (getCurrentViewer().getKeyHandler() != null) {
				return getCurrentViewer().getKeyHandler().keyPressed(e);
			}
		}

		if (acceptArrowKeyOnly(e) && getState() == STATE_INITIAL
				&& !getCurrentViewer().getSelectedEditParts().isEmpty()) {

			EditPart selectedEP = (EditPart) getCurrentViewer()
					.getSelectedEditParts().get(0);
			
			if (selectedEP instanceof GraphicalEditPart) {
			
				GraphicalEditPart gep = (GraphicalEditPart) selectedEP; 
	
				/*
				 * The shape we'll be moved in the direction of the arrow key iff:
				 * 1) It has the appropriate edit policy that supports shape moving installed on the editpart
				 * 2) The editparts figure's parent layout manager is some sort of XYLayout
				 * In all other cases we just change the selection based on arrow key (implemented in GEF).  
				 */
				if (gep.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE) instanceof NonResizableEditPolicy
						&& gep.getFigure().getParent() != null
						&& (gep.getFigure().getParent().getLayoutManager() instanceof XYLayout
								|| gep.getFigure().getParent() instanceof BorderItemContainerFigure)) {
	
					resetHover();
		
					if (getDragTracker() != null)
						getDragTracker().deactivate();
		
					setState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS);
		
					setTargetEditPart(gep);
		
					updateTargetRequest();
					DragTracker dragTracker = gep
							.getDragTracker(getTargetRequest());
					if (dragTracker != null) {
						setDragTracker(dragTracker);
						dragTracker.keyDown(e, getCurrentViewer());
						lockTargetEditPart(gep);
						return true;
					}
					return false;
				}
			}
		}
		return super.handleKeyDown(e);
	}

    /* (non-Javadoc)
     * @see org.eclipse.gef.tools.SelectionTool#handleKeyUp(org.eclipse.swt.events.KeyEvent)
     */
    protected boolean handleKeyUp(KeyEvent e) {
        boolean returnVal = super.handleKeyUp(e);
        if (acceptArrowKeyOnly(e) && !isUsingTraverseHandles) {
            if (getDragTracker() != null) {
                getDragTracker().commitDrag();
            }
            setDragTracker(null);
            setState(STATE_INITIAL);
            unlockTargetEditPart();
        }
        return returnVal;
    }

    /**
     * Returns true if the event corresponds to a tab key and if the system is
     * in a state where the tab key should be accepted.
     * 
     * @param e
     *            the <code>KeyEvent</code>
     * @return true if the tab key should be accepted.
     */
    private boolean acceptTabKey(KeyEvent e) {
        return isInState(STATE_INITIAL | STATE_ACCESSIBLE_DRAG
            | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)
            && e.keyCode == SWT.TAB
            && (e.stateMask & (SWT.ALT | SWT.CONTROL)) == 0;
    }

    /**
     * Returns true if the event corresponds to an arrow key without any
     * modifiers and if the system is in a state where the arrow key should be
     * accepted.
     * 
     * @param e
     *            the <code>KeyEvent</code>
     * @return true if the tab key should be accepted.
     */
    private boolean acceptArrowKeyOnly(KeyEvent e) {
        int key = e.keyCode;
        if (!(isInState(STATE_INITIAL | STATE_ACCESSIBLE_DRAG
            | STATE_ACCESSIBLE_DRAG_IN_PROGRESS))) {
            return false;
        }
        return ((key == SWT.ARROW_UP) || (key == SWT.ARROW_RIGHT)
            || (key == SWT.ARROW_DOWN) || (key == SWT.ARROW_LEFT))
            && (e.stateMask & (SWT.ALT | SWT.CONTROL | SWT.SHIFT)) == 0;
    }

    protected void setState(int state) {
        if (state == STATE_TRAVERSE_HANDLE) {
            isUsingTraverseHandles = true;
        } else if (state == STATE_INITIAL) {
            isUsingTraverseHandles = false;
        }
        super.setState(state);
    }

}
