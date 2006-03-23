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

package org.eclipse.gmf.tests.runtime.common.ui.util;

import java.lang.reflect.InvocationTargetException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gmf.runtime.common.ui.util.StatusLineUtil;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class StatusLineUtilTest
    extends TestCase {

    public StatusLineUtilTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(StatusLineUtilTest.class,
            "StatusLineUtil Test Suite"); //$NON-NLS-1$
    }

    /**
     * Tests that the status line can be successfully updated from a non-UI
     * thread.
     */
    public void test_statusLineUpdateOnNonUIThread_128868()
        throws Exception {

        final IWorkbenchPart part = PlatformUI.getWorkbench()
            .getActiveWorkbenchWindow().getActivePage().getActivePart();

        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor)
                throws InvocationTargetException, InterruptedException {
                StatusLineUtil.outputErrorMessage(part,
                    "test_statusLineUpdateOnNonUIThread_128868"); //$NON-NLS-1$

            }
        };

        try {
            ModalContext.run(runnable, true, new NullProgressMonitor(), Display
                .getCurrent());

        } catch (InvocationTargetException ite) {
            fail("Unexpected exception:" + ite); //$NON-NLS-1$

        } catch (InterruptedException ie) {
            fail("Unexpected exception:" + ie); //$NON-NLS-1$
        }
    }
}
