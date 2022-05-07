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

package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.label.ILabelDelegate;

/**
 * A non-resizable editpolicy for text compartments. It handles the selection
 * and focus feedback of text compartments.
 * 
 * @author melaasar
 */
public class TextSelectionEditPolicy
    extends SelectionEditPolicy {

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
        if (getLabel() != null) {
            getLabel().setFocus(false);
        }
    }

    /**
     * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#hideSelection()
     */
    protected void hideSelection() {
        if (getLabel() != null) {
            getLabel().setSelected(false);
            getLabel().setFocus(false);
        }

    }

    /**
     * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showFocus()
     */
    protected void showFocus() {
        if (getLabel() != null) {
            getLabel().setFocus(true);
        }
    }

    /**
     * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#showSelection()
     */
    protected void showPrimarySelection() {
        if (getLabel() != null) {
            getLabel().setSelected(true);
            getLabel().setFocus(true);
        }
    }

    /**
     * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#showSelection()
     */
    protected void showSelection() {
        if (getLabel() != null) {
            getLabel().setSelected(true);
            getLabel().setFocus(false);
        }
    }
}
