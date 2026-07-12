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

package org.eclipse.gmf.tests.runtime.draw2d.ui;

import org.eclipse.gmf.tests.runtime.draw2d.ui.graph.CompositeGraphLayoutTest;
import org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers.AvoidObstructionsRoutingTest;
import org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers.LeftRightForestRouterTest;
import org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers.RectilinearRouterTest;
import org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers.TopDownForestRouterTest;
import org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers.TreeRouterTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ MapModeUtilTest.class, LineSegTest.class, FigureUtilitiesTest.class, LeftRightForestRouterTest.class,
		TopDownForestRouterTest.class, TreeRouterTest.class, RectilinearRouterTest.class,
		CompositeGraphLayoutTest.class, AvoidObstructionsRoutingTest.class, })
public class AllTests {
}
