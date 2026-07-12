/******************************************************************************
 * Copyright (c) 2002, 2021 IBM Corporation and others.
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
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ AbstractActionDelegateTest.class, AbstractActionHandlerTest.class, ActionManagerTest.class,
		RemoveMnemonicTests.class,
		// ExtendedPropertyDescriptorTest.class,
		// ExtendedTextPropertyDescriptorTest.class,
		// PropertiesServiceTest.class,
		// PropertySourceTest.class,
		FileChangeEventTypeTest.class, FileChangeEventTest.class, FileObserverFilterTypeTest.class,
		FileObserverFilterTest.class, GlobalRedoActionTest.class, GlobalUndoActionTest.class,
		SelectableElementTest.class, StatusLineUtilTest.class,
/**
 * waiting for resolution of Bugzilla 115843
 */
// run ProviderPolicyTest before ProviderPolicyExceptionsTest
// ProviderPolicyTest.class,
// ProviderPolicyExceptionsTest.class,
})
public class AllTests {
}
