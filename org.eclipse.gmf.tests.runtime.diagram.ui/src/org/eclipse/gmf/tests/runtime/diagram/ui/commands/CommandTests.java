/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.tests.runtime.diagram.ui.commands;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author jschofie
 */
public class CommandTests {
	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.gmf.tests.runtime.diagram.ui.commands"); //$NON-NLS-1$
		//$JUnit-BEGIN$
		suite.addTestSuite(SetBoundsCommandTest.class);
		suite.addTestSuite(SendToBackCommandTest.class);
		suite.addTestSuite(BringToFrontCommandTest.class);
		suite.addTestSuite(SendBackwardCommandTest.class);
		suite.addTestSuite(BringForwardCommandTest.class);
		//$JUnit-END$
		return suite;
	}
}
