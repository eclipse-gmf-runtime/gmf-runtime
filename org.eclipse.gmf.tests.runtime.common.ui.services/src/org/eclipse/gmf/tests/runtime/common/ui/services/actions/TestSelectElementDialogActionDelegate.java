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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Action to launch the TestSelectElementDialog.
 * 
 * @author Anthony Hunter
 */
public class TestSelectElementDialogActionDelegate
    implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    /**
     * The action has been activated. The argument of the method represents the
     * 'real' action sitting in the workbench UI.
     * 
     * @see IWorkbenchWindowActionDelegate#run
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

    /**
     * Selection in the workbench has been changed. We can change the state of
     * the 'real' action here if we want, but this can only happen after the
     * delegate has been created.
     * 
     * @see IWorkbenchWindowActionDelegate#selectionChanged
     */
    public void selectionChanged(IAction action, ISelection selection) {
        // empty
    }

    /**
     * We can use this method to dispose of any system resources we previously
     * allocated.
     * 
     * @see IWorkbenchWindowActionDelegate#dispose
     */
    public void dispose() {
        // empty
    }

    /**
     * We will cache window object in order to be able to provide parent shell
     * for the message dialog.
     * 
     * @see IWorkbenchWindowActionDelegate#init
     */
    public void init(IWorkbenchWindow aWindow) {
        this.window = aWindow;
    }
}