/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.diagram.ui;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.gmf.tests.runtime.diagram.ui.commands.CommandTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicCanonicalTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.ConnectorHandleLocatorTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.ConnectorHandleTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.DiagramCommandStackTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.services.PaletteServiceTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.tools.RubberBandSelectionToolTest;

public class AllTests extends TestCase implements IPlatformRunnable {

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(DiagramCommandStackTest.suite());
		suite.addTest(ConnectorHandleLocatorTest.suite());
		suite.addTest(ConnectorHandleTest.suite());
		suite.addTest(NoteTests.suite());
		suite.addTest(CommandTests.suite());
		//suite.addTest(URLImageEditPartTests.suite());
		suite.addTest(ConnectorTests.suite()); 
		suite.addTest(DiagramTests.suite());
		suite.addTest(ShapeTests.suite());
		suite.addTestSuite(RubberBandSelectionToolTest.class);
		suite.addTest(PaletteServiceTests.suite());
		suite.addTest(LogicCanonicalTests.suite());
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
