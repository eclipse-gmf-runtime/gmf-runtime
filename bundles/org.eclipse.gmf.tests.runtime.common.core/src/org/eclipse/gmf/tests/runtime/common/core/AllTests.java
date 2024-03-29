/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.core;

import org.eclipse.gmf.tests.runtime.common.core.internal.command.AbstractCommandTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.command.CompositeCommandTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.command.FileModificationApproverTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.command.OneTimeCommandTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.service.AbstractProviderTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.service.ExecutionStrategyTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.service.ProviderPriorityTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.service.ServiceTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.util.HashUtilTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.util.ProxyTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.util.StringUtilTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class AllTests extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(AbstractCommandTest.suite());
        suite.addTest(AbstractProviderTest.suite());
        suite.addTest(CompositeCommandTest.suite());
        suite.addTest(ExecutionStrategyTest.suite());
        suite.addTest(FileModificationApproverTest.suite());
        suite.addTest(HashUtilTest.suite());
        suite.addTest(OneTimeCommandTest.suite());
        suite.addTest(ProviderPriorityTest.suite());
        suite.addTest(ProxyTest.suite());
        suite.addTest(StringUtilTest.suite());
        suite.addTest(ServiceTest.suite());
        return suite;
    }

    public AllTests() {
        super(""); //$NON-NLS-1$
    }
}
