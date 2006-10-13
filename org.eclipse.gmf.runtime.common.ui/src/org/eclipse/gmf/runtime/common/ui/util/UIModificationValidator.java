/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.core.command.IModificationValidator;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;
import org.eclipse.gmf.runtime.common.ui.resources.FileModificationValidator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * A validator responsible for doing the actual validation on files. The
 * validation determines whether files may be modified. It delegates to
 * IWorkspace's validateEdit and supplies a UI context from the active shell if
 * available.
 * 
 * @author wdiu, Wayne Diu
 */
public class UIModificationValidator
	implements IModificationValidator {

    /**
     * Window listener
     */
    private WindowListener listener;
    
    /**
     * Window listener to obtain the active shell
     * 
     * @author wdiu, Wayne Diu
     */
    private class WindowListener implements IWindowListener {
        /**
         * A shell that can be specified as a parameter in the constructor
         */
        protected Shell shell;
        
        /* (non-Javadoc)
         * @see org.eclipse.ui.IWindowListener#windowActivated(org.eclipse.ui.IWorkbenchWindow)
         */
        public void windowActivated(IWorkbenchWindow window) {
            shell = window.getShell();
        }

        /* (non-Javadoc)
         * @see org.eclipse.ui.IWindowListener#windowClosed(org.eclipse.ui.IWorkbenchWindow)
         */
        public void windowClosed(IWorkbenchWindow window) {
            //do nothing
        }

        /* (non-Javadoc)
         * @see org.eclipse.ui.IWindowListener#windowDeactivated(org.eclipse.ui.IWorkbenchWindow)
         */
        public void windowDeactivated(IWorkbenchWindow window) {
            //do nothing
        }

        /* (non-Javadoc)
         * @see org.eclipse.ui.IWindowListener#windowOpened(org.eclipse.ui.IWorkbenchWindow)
         */
        public void windowOpened(IWorkbenchWindow window) {
            //do nothing
        }
        
        /**
         * Constructor that takes a shell
         * @param theShell initial active shell
         */
        public WindowListener(Shell theShell) {
            this.shell = theShell;
        }
        
        /**
         * Returns the active shell
         * @return active Shell
         */
        public Shell getShell() {
            return shell;
        }
    }
    
	/**
	 * Error status code. The OK status code is defined by Eclipse's Status
	 * class.
	 */
	private static final Status ERROR_STATUS = new Status(Status.ERROR,
		Platform.PI_RUNTIME, 1, StringStatics.BLANK, null);

    /**
     * Constructs a UI modification validator and initializes the UI context
     */
    public UIModificationValidator() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                Shell shell = window == null ? null : window.getShell();
                listener = new WindowListener(shell);
                PlatformUI.getWorkbench().addWindowListener(listener);
            }
        });
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.IModificationValidator#validateEdit(org.eclipse.core.resources.IFile[])
	 */
	public IStatus validateEdit(IFile[] files) {
        Shell shell = listener == null ? null : listener.getShell(); 
		boolean ok = FileModificationValidator.getInstance().okToEdit(files,
			CommonUIMessages.UIModificationValidator_ModificationMessage, shell);

		return ok ? Status.OK_STATUS
			: ERROR_STATUS;
	}
    
    /**
     * Disposes this UI modification validator.
     */
    public void dispose() {
        if (listener != null) {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    PlatformUI.getWorkbench().removeWindowListener(listener);
                }
            });
        }
    }

}