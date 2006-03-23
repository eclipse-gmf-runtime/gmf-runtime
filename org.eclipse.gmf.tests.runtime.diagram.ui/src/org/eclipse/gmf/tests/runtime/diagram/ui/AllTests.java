/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.gmf.tests.runtime.diagram.ui.commands.CommandTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.commands.SemanticCreateCommandTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.DiagramAssistantTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.DiagramEventBrokerTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.DiagramGraphicalViewerTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicCanonicalTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicCreationTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicDiagramTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicShapeTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicTransientViewsTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.PaletteTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.ConnectionHandleLocatorTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.ConnectionHandleTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.DiagramCommandStackTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.GraphicalNodeEditPolicyTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.render.util.CopyToImageUtilTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.requests.RequestTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.services.PaletteServiceTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.tools.ConnectionToolTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.tools.RubberBandSelectionToolTest;

public class AllTests extends TestCase implements IPlatformRunnable {

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(DiagramCommandStackTest.suite());
		suite.addTest(ConnectionHandleLocatorTest.suite());
		suite.addTest(ConnectionHandleTest.suite());
		suite.addTest(NoteTests.suite());
		suite.addTest(CommandTests.suite());
		//suite.addTest(URLImageEditPartTests.suite());
		suite.addTest(ConnectorTests.suite()); 
		suite.addTest(DiagramTests.suite());
		suite.addTest(ShapeTests.suite());
		suite.addTestSuite(RubberBandSelectionToolTest.class);
		suite.addTest(PaletteServiceTests.suite());
		suite.addTest(LogicCanonicalTests.suite());
		suite.addTest(LogicCreationTests.suite());
		suite.addTest(DiagramGraphicalViewerTests.suite());
		suite.addTest(LogicDiagramTests.suite());
		suite.addTest(LogicShapeTests.suite());
		suite.addTest(DiagramAssistantTests.suite());
		suite.addTest(SemanticCreateCommandTest.suite());
		suite.addTest(PaletteTests.suite());
		suite.addTest(GraphicalNodeEditPolicyTests.suite());
		suite.addTest(DiagramEventBrokerTests.suite());
		suite.addTest(LogicTransientViewsTests.suite());
		suite.addTest(RequestTests.suite());
		suite.addTest(ConnectionToolTests.suite());
        suite.addTest(CopyToImageUtilTests.suite());
        suite.addTest(DiagramEditingDomainTestCase.suite());
		
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
