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

package org.eclipse.gmf.tests.runtime.diagram.ui;

import org.eclipse.gmf.tests.runtime.diagram.core.DiagramEditingDomainFactoryTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.commands.CommandTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.commands.CommandUtilitiesTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.commands.SemanticCreateCommandTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.label.LabelTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.DiagramAssistantTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.DiagramGraphicalViewerTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.GroupTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.GroupsInCompartmentTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LayoutTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicCanonicalTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicCreationTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicDiagramTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.LogicTransientViewsTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.PaletteTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.logic.TextAlignmentTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.ConnectionHandleLocatorTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.ConnectionHandleTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.DiagramCommandStackTest;
import org.eclipse.gmf.tests.runtime.diagram.ui.parts.GraphicalNodeEditPolicyTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.render.util.CopyToImageUtilTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.requests.RequestTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.services.DiagramEventBrokerServiceTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.services.PaletteServiceTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.tools.ConnectionToolTests;
import org.eclipse.gmf.tests.runtime.diagram.ui.tools.RubberBandSelectionToolTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class AllTests extends TestCase {

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
        // suite.addTest(URLImageEditPartTests.suite());
        suite.addTest(ConnectorTests.suite());
        suite.addTest(DiagramTests.suite());
        suite.addTest(ShapeTests.suite());
        suite.addTestSuite(RubberBandSelectionToolTest.class);
        suite.addTest(PaletteServiceTests.suite());
        suite.addTest(LogicCanonicalTests.suite());
        suite.addTest(LogicCreationTests.suite());
        suite.addTest(DiagramGraphicalViewerTests.suite());
        suite.addTest(LogicDiagramTests.suite());
        // suite.addTest(LogicShapeTests.suite());
        suite.addTest(DiagramAssistantTests.suite());
        suite.addTest(SemanticCreateCommandTest.suite());
        suite.addTest(PaletteTests.suite());
        suite.addTest(GraphicalNodeEditPolicyTests.suite());
        // temporarily removed to check if it's causing problems on a build server
        // suite.addTest(DiagramEventBrokerTests.suite());
        suite.addTest(LogicTransientViewsTests.suite());
        suite.addTest(RequestTests.suite());
        suite.addTest(ConnectionToolTests.suite());
        suite.addTest(CopyToImageUtilTests.suite());
        suite.addTest(DiagramEditingDomainTestCase.suite());
        suite.addTest(CommandUtilitiesTest.suite());
        suite.addTest(DiagramEventBrokerServiceTests.suite());
        suite.addTest(LabelTests.suite());
        suite.addTest(GroupTests.suite());
        suite.addTest(GroupsInCompartmentTests.suite());
        suite.addTest(LayoutTests.suite());
        suite.addTest(TextAlignmentTests.suite());
        suite.addTest(DiagramEditingDomainFactoryTests.suite());

        return suite;
    }

    public AllTests() {
        super(""); //$NON-NLS-1$
    }

}
