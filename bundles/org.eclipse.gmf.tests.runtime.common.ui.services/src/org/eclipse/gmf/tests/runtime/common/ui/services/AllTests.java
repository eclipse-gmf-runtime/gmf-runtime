/******************************************************************************
 * Copyright (c) 2006, 2021 IBM Corporation and others.
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

import org.eclipse.gmf.tests.runtime.common.ui.services.elementselection.ElementSelectionFilterTest;
import org.eclipse.gmf.tests.runtime.common.ui.services.elementselection.ElementSelectionScopeTest;
import org.eclipse.gmf.tests.runtime.common.ui.services.elementselection.ElementSelectionUserInputTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * All tests for the plug-in.
 *
 * @author Anthony Hunter
 */
@Suite
@SelectClasses({ ElementSelectionScopeTest.class, ElementSelectionFilterTest.class,
		ElementSelectionUserInputTest.class, })
public class AllTests {
}
