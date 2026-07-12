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
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ AbstractCommandTest.class, AbstractProviderTest.class, CompositeCommandTest.class,
		ExecutionStrategyTest.class, FileModificationApproverTest.class, HashUtilTest.class, OneTimeCommandTest.class,
		ProviderPriorityTest.class, ProxyTest.class, StringUtilTest.class, ServiceTest.class, })
public class AllTests {
}
