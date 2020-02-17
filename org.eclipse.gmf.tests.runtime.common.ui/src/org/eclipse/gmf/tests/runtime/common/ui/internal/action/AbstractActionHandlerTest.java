/******************************************************************************
 * Copyright (c) 2006, 2014 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.internal.action;

import java.lang.reflect.InvocationTargetException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;


public class AbstractActionHandlerTest extends TestCase {

	public AbstractActionHandlerTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(AbstractActionHandlerTest.class);
	}

    /**
     * Tests that an error dialog can be successfully shown when
     * the action handler runs on a non-UI thread.
     */
    public void ignore_errorDialogOnNonUIThread_132143() {
        
        // This line must be enabled to really perform the test. Otherwise, the
        // error dialog is not displayed and the SWTException would NEVER occur.
        // However, for the purpose of automated testing, the automated mode
        // will be true.
        //
        // ErrorDialog.AUTOMATED_MODE = false;
        
         final IWorkbenchPart part = PlatformUI.getWorkbench()
         .getActiveWorkbenchWindow().getActivePage().getActivePart();

        AbstractActionHandler actionHandler = new AbstractActionHandler(part) {

            protected void doRun(IProgressMonitor progressMonitor) {

                final Exception e = new Exception("Forced Exception"); //$NON-NLS-1$
                
                IRunnableWithProgress runnable = new IRunnableWithProgress() {

                    public void run(IProgressMonitor monitor)
                        throws InvocationTargetException, InterruptedException {
                            setText("test_errorDialogOnNonUIThread_132143"); //$NON-NLS-1$
                            setWorkbenchPart(part);
                            handle(e);
                    }
                };

                try {
                    ModalContext.run(runnable, true, new NullProgressMonitor(),
                        Display.getCurrent());
                    
                } catch (InvocationTargetException ite) {
                    fail("Unexpected exception:" + ite); //$NON-NLS-1$
                    
                } catch (InterruptedException ie) {
                    fail("Unexpected exception:" + ie); //$NON-NLS-1$
                }
            }
            
            public void refresh() {
                // do nothing
            }
        };
        actionHandler.run(new NullProgressMonitor());
    }

	public void test_testNothing() {
		// There is an issue when running the test_errorDialogOnNonUIThread_132143 test, run no tests for now.
	}
}
