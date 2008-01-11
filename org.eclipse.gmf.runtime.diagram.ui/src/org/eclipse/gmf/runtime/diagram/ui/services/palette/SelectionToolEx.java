/******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.palette;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.SelectionTool;
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
    extends SelectionTool {

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

    protected boolean handleKeyDown(KeyEvent e) {
        resetHover();

        if (acceptTabKey(e)) {
            if (getCurrentViewer().getKeyHandler() != null) {
                return getCurrentViewer().getKeyHandler().keyPressed(e);
            }
        }

        if (acceptArrowKeyOnly(e) && getState() == STATE_INITIAL) {
            if (!stateTransition(STATE_INITIAL,
                STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
                resetHover();
                return true;
            }
            resetHover();

            if (getDragTracker() != null)
                getDragTracker().deactivate();

            if (!getCurrentViewer().getSelectedEditParts().isEmpty()) {
                EditPart selectedEP = (EditPart) getCurrentViewer()
                    .getSelectedEditParts().get(0);
                setTargetEditPart(selectedEP);

                if (selectedEP != null) {
                    updateTargetRequest();
                    DragTracker dragTracker = selectedEP
                        .getDragTracker(getTargetRequest());
                    if (dragTracker != null) {
                        setDragTracker(dragTracker);
                        dragTracker.keyDown(e, getCurrentViewer());
                        lockTargetEditPart(selectedEP);
                        return true;
                    }
                }
                return false;
            }
        }
        return super.handleKeyDown(e);
    }

    @Override
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
