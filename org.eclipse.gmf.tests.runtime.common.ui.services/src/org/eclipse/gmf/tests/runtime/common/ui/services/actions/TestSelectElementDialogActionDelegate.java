/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.services.actions;

import java.util.List;

import org.eclipse.gmf.tests.runtime.common.ui.services.dialogs.TestSelectElementDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Action to launch the TestSelectElementDialog.
 * 
 * @author Anthony Hunter
 */
public class TestSelectElementDialogActionDelegate
    extends AbstractTestElementSelectionServiceActionDelegate {

    /**
     * {@inheritDoc}
     */
    public void run(IAction action) {
        TestSelectElementDialog dialog = new TestSelectElementDialog(window
            .getShell());
        dialog.open();
        if (dialog.getReturnCode() != Dialog.CANCEL) {
            List selectedElements = dialog.getSelectedElements();
            MessageDialog.openInformation(window.getShell(),
                "Result", "Selected "//$NON-NLS-2$//$NON-NLS-1$
                    + ((String) selectedElements.get(0)));
        }
    }
}