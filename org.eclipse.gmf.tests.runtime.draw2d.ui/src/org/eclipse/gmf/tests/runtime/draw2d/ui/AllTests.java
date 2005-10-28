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

package org.eclipse.gmf.tests.runtime.draw2d.ui; 

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers.LeftRightForestRouterTest;
import org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers.RectilinearRouterTest;
import org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers.TopDownForestRouterTest;
import org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers.TreeRouterTest;

public class AllTests extends TestCase implements IPlatformRunnable {

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite( "All Shapes GEF Tests" ); //$NON-NLS-1$

		suite.addTestSuite( MapModeUtilTest.class );
		suite.addTestSuite( LineSegTest.class );
		suite.addTestSuite( FigureUtilitiesTest.class );
		suite.addTestSuite( LeftRightForestRouterTest.class );
		suite.addTestSuite( TopDownForestRouterTest.class );
		suite.addTestSuite( TreeRouterTest.class );
		suite.addTestSuite( RectilinearRouterTest.class );
		suite.addTestSuite( PolylineAnchorTest.class );
		
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
