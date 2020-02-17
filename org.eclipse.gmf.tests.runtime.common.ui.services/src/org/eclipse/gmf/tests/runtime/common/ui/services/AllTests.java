/******************************************************************************
 * Copyright (c) 2006, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.services;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.gmf.tests.runtime.common.ui.services.elementselection.ElementSelectionFilterTest;
import org.eclipse.gmf.tests.runtime.common.ui.services.elementselection.ElementSelectionScopeTest;
import org.eclipse.gmf.tests.runtime.common.ui.services.elementselection.ElementSelectionUserInputTest;

/**
 * All tests for the plug-in.
 * 
 * @author Anthony Hunter
 */
public class AllTests
    extends TestCase
    implements IPlatformRunnable {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(ElementSelectionScopeTest.suite());
        suite.addTest(ElementSelectionFilterTest.suite());
        suite.addTest(ElementSelectionUserInputTest.suite());
        return suite;
    }

    public AllTests() {
        super(""); //$NON-NLS-1$
    }

    public Object run(Object args)
        throws Exception {
        TestRunner.run(suite());
        return Arrays
            .asList(new String[] {"Please see raw test suite output for details."}); //$NON-NLS-1$
    }

}
