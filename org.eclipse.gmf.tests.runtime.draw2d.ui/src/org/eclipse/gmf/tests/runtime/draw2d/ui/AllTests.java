/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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

		suite.addTestSuite( LineSegTest.class );
		suite.addTestSuite( FigureUtilitiesTest.class );
		suite.addTestSuite( LeftRightForestRouterTest.class );
		suite.addTestSuite( TopDownForestRouterTest.class );
		suite.addTestSuite( TreeRouterTest.class );
		suite.addTestSuite( RectilinearRouterTest.class );
		
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
