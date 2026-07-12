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

package org.eclipse.gmf.tests.runtime.emf.ui;

import org.eclipse.gmf.tests.runtime.emf.ui.action.AbstractModelActionDelegateTest;
import org.eclipse.gmf.tests.runtime.emf.ui.action.AbstractModelActionHandlerTest;
import org.eclipse.gmf.tests.runtime.emf.ui.services.action.AbstractModelActionFilterProviderTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Anthony Hunter
 *         <a href="mailto:anthonyh@ca.ibm.com">mailto:anthonyh@ca.ibm.com</a>
 */
@Suite
@SelectClasses({ AbstractModelActionDelegateTest.class, AbstractModelActionHandlerTest.class,
		AbstractModelActionFilterProviderTest.class, ModelingAssistantServiceTests.class, })
public class AllTests {
}
