/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.core;

import java.util.Arrays;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.gmf.tests.runtime.emf.core.resources.GMFResourceModificationManagerTests;
import org.eclipse.gmf.tests.runtime.emf.core.util.EMFCoreUtilTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class AllTests extends TestCase implements IApplication {

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(GMFEditingDomainFactoryTests.suite());
		suite.addTest(GMFResourceModificationManagerTests.suite());
		suite.addTest(EMFCoreUtilTests.suite());
		return suite;
	}

	public AllTests() {
		super(""); //$NON-NLS-1$
	}

	public Object start(IApplicationContext context) throws Exception {
		TestRunner.run(suite());
		return Arrays
				.asList(new String[] { "Please see raw test suite output for details." }); //$NON-NLS-1$
	}

	public void stop() {
		// do nothing
	}
}
