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
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ DiagramCommandStackTest.class, ConnectionHandleLocatorTest.class, ConnectionHandleTest.class,
		NoteTests.class, CommandTests.class,
		// URLImageEditPartTests.class,
		ConnectorTests.class, DiagramTests.class, ShapeTests.class, RubberBandSelectionToolTest.class,
		PaletteServiceTests.class, LogicCanonicalTests.class, LogicCreationTests.class,
		DiagramGraphicalViewerTests.class, LogicDiagramTests.class,
		// LogicShapeTests.class,
		DiagramAssistantTests.class, SemanticCreateCommandTest.class, PaletteTests.class,
		GraphicalNodeEditPolicyTests.class,
		// temporarily removed to check if it's causing problems on a build server
		// DiagramEventBrokerTests.class,
		LogicTransientViewsTests.class, RequestTests.class, ConnectionToolTests.class, CopyToImageUtilTests.class,
		DiagramEditingDomainTestCase.class, CommandUtilitiesTest.class, DiagramEventBrokerServiceTests.class,
		LabelTests.class, GroupTests.class, GroupsInCompartmentTests.class, LayoutTests.class, TextAlignmentTests.class,
		DiagramEditingDomainFactoryTests.class, })
public class AllTests {
}
