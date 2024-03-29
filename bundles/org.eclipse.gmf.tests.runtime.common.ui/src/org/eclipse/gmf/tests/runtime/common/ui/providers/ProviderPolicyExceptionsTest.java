/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.providers;

import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.policies.TestExceptionThrowingIconPolicy;
import org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.providers.TestExceptionThrowingIconProvider;
import org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.providers.TestExceptionThrowingPolicyIconProvider;
import org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.testClasses.TestAdaptable;
import org.eclipse.swt.graphics.Image;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the Service infrastructure when used in conjunction with policies and
 * providers that throw exceptions.
 * 
 * @author wdiu, Wayne Diu
 */
public class ProviderPolicyExceptionsTest
	extends TestCase {

	public void testExceptionThrowingProvidersAndPolicies() {
		// the lowest priority provider should return a valid parser,
		// even when exceptions were thrown by other higher priority
		// providers
		Image icon = IconService.getInstance().getIcon(new TestAdaptable());
		assertNotNull(icon);
		icon.dispose();

		// the medium priority provider's policy was executed
		// but since it throws an exception, the corresponding provider should
		// not have been considered and so the provider's provides method should
		// never have been called
		assertTrue(TestExceptionThrowingIconPolicy.providesExecuted);
		assertFalse(TestExceptionThrowingPolicyIconProvider.providesExecuted);

		// the low priority provider was executed
		assertTrue(TestExceptionThrowingIconProvider.providesExecuted);
	}
	
    public static Test suite() {
        return new TestSuite(ProviderPolicyExceptionsTest.class);
    }
	
}
