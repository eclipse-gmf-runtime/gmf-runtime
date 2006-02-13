/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.commands.core;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.gmf.tests.runtime.emf.commands.core.command.AbstractModelCommandTest;
import org.eclipse.gmf.tests.runtime.emf.commands.core.command.AbstractTransactionalCommandTest;
import org.eclipse.gmf.tests.runtime.emf.commands.core.command.CompositeModelCommandTest;
import org.eclipse.gmf.tests.runtime.emf.commands.core.command.CompositeTransactionalCommandTest;

/**
 * @author gvaradar
 *
 */
public class AllTests extends TestCase implements IPlatformRunnable {

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		
		suite.addTest(AbstractModelCommandTest.suite());
        suite.addTest(AbstractTransactionalCommandTest.suite());
		suite.addTest(CompositeModelCommandTest.suite());
        suite.addTest(CompositeTransactionalCommandTest.suite());

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