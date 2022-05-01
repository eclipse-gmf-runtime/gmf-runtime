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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class AbstractActionDelegateTest extends TestCase {

	public AbstractActionDelegateTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(AbstractActionDelegateTest.class);
	}

    /**
     * Tests that an error dialog can be successfully shown when
     * the action delegate runs on a non-UI thread.
     */
    public void ignore_errorDialogOnNonUIThread_125482() {
        
        // This line must be enabled to really perform the test. Otherwise, the
        // error dialog is not displayed and the SWTException would NEVER occur.
        // However, for the purpose of automated testing, the automated mode
        // will be true.
        //
        // ErrorDialog.AUTOMATED_MODE = false;
        

        AbstractActionDelegate actionDelegate = new AbstractActionDelegate() {

            protected void doRun(IProgressMonitor progressMonitor) {

                final Exception e = new Exception("Forced Exception"); //$NON-NLS-1$
                final IWorkbenchPart part = PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow().getActivePage().getActivePart();

                IRunnableWithProgress runnable = new IRunnableWithProgress() {

                    public void run(IProgressMonitor monitor)
                        throws InvocationTargetException, InterruptedException {
                        try {
                            setAction(new Action("AbstractActionDelegateTest") { //$NON-NLS-1$
                                // nothing
                            });
                            setWorkbenchPart(part);

                            handle(e);
                        } catch (SWTException swte) {
                            fail("Do not expect SWT Exception: " + swte.getLocalizedMessage()); //$NON-NLS-1$
                        }
                    }
                };

                try {
                    ModalContext.run(runnable, true, new NullProgressMonitor(),
                        Display.getCurrent());
                } catch (InterruptedException ie) {
                    // do nothing
                } catch (InvocationTargetException ite) {
                    // do nothing
                }
            }
        };
        actionDelegate.run(new NullProgressMonitor());
    }

	public void test_testNothing() {
		// There is an issue when running the test_errorDialogOnNonUIThread_125482() test, run no tests for now.
	}
}
