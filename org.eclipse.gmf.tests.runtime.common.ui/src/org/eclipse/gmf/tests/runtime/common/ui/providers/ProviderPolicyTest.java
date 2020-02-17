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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.policies.TestParserPolicy;
import org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.testClasses.TestAdaptable;
import org.osgi.framework.Bundle;

/**
 * This test case tests provider polciies using the Parser Service from a UI
 * plugin as the example service.
 * 
 * @author wdiu, Wayne Diu
 */
public class ProviderPolicyTest
	extends TestCase {

	final private Bundle TEST_PROVIDER_PLUGIN_BUNDLE = Platform
		.getBundle("org.eclipse.gmf.tests.runtime.common.ui.services.provider"); //$NON-NLS-1$

	public void testPluginDoesNotLoad() {

		// this test should be run before loading the bundle, otherwise this
		// test will obviously fail
		// the ProviderPolicyExceptionsTest will load the bundle.

		IAdaptable adaptable = new IAdaptable() {

			public Object getAdapter(Class adapter) {
				return null;
			}

		};

		// make sure the org.eclipse.gmf.tests.runtime.common.core.provider
		// plug-in has not been loaded
		assertFalse(TEST_PROVIDER_PLUGIN_BUNDLE.getState() == Bundle.ACTIVE);

		// make sure the provides method has not been called yet
		assertFalse(TestParserPolicy.providesExecuted);

		// run getParser several times
		for (int i = 0; i < 4; i++) {
			ParserService.getInstance().getParser(adaptable);

			// make sure the org.eclipse.gmf.tests.runtime.common.core.provider
			// plug-in still did not load
			assertFalse(TEST_PROVIDER_PLUGIN_BUNDLE.getState() == Bundle.ACTIVE);

			assertTrue(TestParserPolicy.providesExecuted);
		}

	}

	public void testPluginLoad() {

		// make sure the org.eclipse.gmf.tests.runtime.common.core.provider
		// plug-in still did not load
		assertFalse(TEST_PROVIDER_PLUGIN_BUNDLE.getState() == Bundle.ACTIVE);

		ParserService.getInstance().getParser(new TestAdaptable());

		// make sure the org.eclipse.gmf.tests.runtime.common.core.provider
		// plug-in has now been loaded
		// because the provider provides for the TestAdaptable hint
		assertTrue(TEST_PROVIDER_PLUGIN_BUNDLE.getState() == Bundle.ACTIVE);

	}

	public static Test suite() {
		return new TestSuite(ProviderPolicyTest.class);
	}
}
