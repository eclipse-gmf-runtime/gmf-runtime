/******************************************************************************
 * Copyright (c) 2005, 2021 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.services.action;

import org.eclipse.gmf.tests.runtime.common.ui.services.action.contributionitem.ContributionItemServiceTests;
import org.eclipse.gmf.tests.runtime.common.ui.services.action.internal.filter.ActionFilterServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ ActionFilterServiceTest.class, ContributionItemServiceTests.class, })
public class AllTests {
}
