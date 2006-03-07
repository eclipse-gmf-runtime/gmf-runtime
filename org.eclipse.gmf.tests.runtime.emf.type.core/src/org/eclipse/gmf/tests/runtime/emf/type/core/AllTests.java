/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.type.core;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.gmf.tests.runtime.emf.type.core.commands.CreateElementCommandTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.commands.DestroyElementCommandTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.commands.SetValueCommandTest;

public class AllTests
	extends TestCase
	implements IPlatformRunnable {

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(AbstractEditHelperTest.suite());
		suite.addTest(ElementTypeRegistryTest.suite());
		suite.addTest(CreateElementCommandTest.suite());
		suite.addTest(CreateElementRequestTest.suite());
		suite.addTest(DestroyElementCommandTest.suite());
		suite.addTest(MetamodelTypeDescriptorTest.suite());
		suite.addTest(MoveRequestTest.suite());
        suite.addTest(SetValueCommandTest.suite());
		suite.addTest(SpecializationTypeDescriptorTest.suite());
        suite.addTest(SpecializationTypeTest.suite());
		return suite;
	}

	public AllTests() {
		super(""); //$NON-NLS-1$
	}

	public Object run(Object args)
		throws Exception {
		TestRunner.run(suite());
		return Arrays
			.asList(new String[] {"Please see raw test suite output for details."}); //$NON-NLS-1$
	}

}
