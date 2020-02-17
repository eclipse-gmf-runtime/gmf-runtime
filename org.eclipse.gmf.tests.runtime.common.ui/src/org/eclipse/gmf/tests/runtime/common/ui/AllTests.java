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

package org.eclipse.gmf.tests.runtime.common.ui;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.gmf.tests.runtime.common.ui.action.actions.global.GlobalRedoActionTest;
import org.eclipse.gmf.tests.runtime.common.ui.action.actions.global.GlobalUndoActionTest;
import org.eclipse.gmf.tests.runtime.common.ui.internal.action.AbstractActionDelegateTest;
import org.eclipse.gmf.tests.runtime.common.ui.internal.action.AbstractActionHandlerTest;
import org.eclipse.gmf.tests.runtime.common.ui.internal.action.ActionManagerTest;
import org.eclipse.gmf.tests.runtime.common.ui.internal.action.RemoveMnemonicTests;
import org.eclipse.gmf.tests.runtime.common.ui.internal.dialogs.SelectableElementTest;
import org.eclipse.gmf.tests.runtime.common.ui.internal.resources.FileChangeEventTest;
import org.eclipse.gmf.tests.runtime.common.ui.internal.resources.FileChangeEventTypeTest;
import org.eclipse.gmf.tests.runtime.common.ui.internal.resources.FileObserverFilterTest;
import org.eclipse.gmf.tests.runtime.common.ui.internal.resources.FileObserverFilterTypeTest;
import org.eclipse.gmf.tests.runtime.common.ui.util.StatusLineUtilTest;

public class AllTests extends TestCase implements IPlatformRunnable {

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
        suite.addTest(AbstractActionDelegateTest.suite());
        suite.addTest(AbstractActionHandlerTest.suite());
		suite.addTest(ActionManagerTest.suite());
		suite.addTest(RemoveMnemonicTests.suite());
//		suite.addTest(ExtendedPropertyDescriptorTest.suite());
//		suite.addTest(ExtendedTextPropertyDescriptorTest.suite());
//		suite.addTest(PropertiesServiceTest.suite());
//		suite.addTest(PropertySourceTest.suite());
		suite.addTest(FileChangeEventTypeTest.suite());
		suite.addTest(FileChangeEventTest.suite());
		suite.addTest(FileObserverFilterTypeTest.suite());
		suite.addTest(FileObserverFilterTest.suite());
        suite.addTest(GlobalRedoActionTest.suite());
        suite.addTest(GlobalUndoActionTest.suite());
        suite.addTest(SelectableElementTest.suite());
        suite.addTest(StatusLineUtilTest.suite());
        /**
         * waiting for resolution of Bugzilla 115843
         */
		//run ProviderPolicyTest before ProviderPolicyExceptionsTest
//		suite.addTest(ProviderPolicyTest.suite());
//		suite.addTest(ProviderPolicyExceptionsTest.suite());
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
