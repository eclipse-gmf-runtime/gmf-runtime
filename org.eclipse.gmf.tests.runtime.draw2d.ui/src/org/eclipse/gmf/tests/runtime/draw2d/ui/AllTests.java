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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class AllTests extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("All Shapes GEF Tests"); //$NON-NLS-1$

        suite.addTestSuite(MapModeUtilTest.class);
        suite.addTestSuite(LineSegTest.class);
        suite.addTestSuite(FigureUtilitiesTest.class);
        suite.addTestSuite(LeftRightForestRouterTest.class);
        suite.addTestSuite(TopDownForestRouterTest.class);
        suite.addTestSuite(TreeRouterTest.class);
        suite.addTestSuite(RectilinearRouterTest.class);
        suite.addTestSuite(CompositeGraphLayoutTest.class);
        suite.addTestSuite(AvoidObstructionsRoutingTest.class);

        return suite;
    }

    public AllTests() {
        super(""); //$NON-NLS-1$
    }
}
