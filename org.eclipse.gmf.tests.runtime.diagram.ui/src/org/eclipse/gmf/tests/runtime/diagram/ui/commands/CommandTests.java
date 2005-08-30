/***************************************************************************
 * Licensed Materials - Property of IBM
 * (C) Copyright IBM Corp. 2004.  All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 **************************************************************************/

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
