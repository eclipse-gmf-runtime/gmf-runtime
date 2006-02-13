/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.core;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.gmf.tests.runtime.common.core.internal.command.AbstractCommandTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.command.CompositeCommandTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.command.FileModificationApproverTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.command.compatibility.CommandManagerTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.service.AbstractProviderTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.service.ExecutionStrategyTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.service.ProviderPriorityTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.service.ServiceTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.util.HashUtilTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.util.ProxyTest;
import org.eclipse.gmf.tests.runtime.common.core.internal.util.StringUtilTest;

public class AllTests extends TestCase implements IPlatformRunnable {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(AbstractCommandTest.suite());
        suite.addTest(org.eclipse.gmf.tests.runtime.common.core.internal.command.compatibility.AbstractCommandTest.suite());
        suite.addTest(AbstractProviderTest.suite());
        suite.addTest(CommandManagerTest.suite());
        suite.addTest(CompositeCommandTest.suite());
        suite.addTest(org.eclipse.gmf.tests.runtime.common.core.internal.command.compatibility.CompositeCommandTest.suite());
		suite.addTest(ExecutionStrategyTest.suite());
        suite.addTest(FileModificationApproverTest.suite());
        suite.addTest(HashUtilTest.suite());
        suite.addTest(ProviderPriorityTest.suite());
        suite.addTest(ProxyTest.suite());
        suite.addTest(StringUtilTest.suite());
        suite.addTest(ServiceTest.suite());
        return suite;
    }

    public AllTests() {
        super(""); //$NON-NLS-1$
    }

    public Object run(Object args) throws Exception {
        TestRunner.run(suite());
        return Arrays.asList(new String[] { "Please see raw test suite output for details." }); //$NON-NLS-1$
    }

}
