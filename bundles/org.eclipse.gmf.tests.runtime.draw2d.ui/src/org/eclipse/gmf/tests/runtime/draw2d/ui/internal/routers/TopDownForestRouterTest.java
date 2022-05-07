/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.draw2d.ui.internal.routers;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ITreeConnection;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.OrthogonalRouterUtilities;
/**
 * @author sshaw
 * 
 * ForestRouter test class.
 */
public class TopDownForestRouterTest extends AbstractForestRouterTest {
	public TopDownForestRouterTest(String name) {
		super(name);
	}
	
	protected void setUp() {
		super.setUp();
		
		try {
			getConnection1().getSourceAnchor().getOwner().setLocation(new Point(100, 200));
			getConnection2().getSourceAnchor().getOwner().setLocation(new Point(200, 200));
			getConnection1().getTargetAnchor().getOwner().setLocation(new Point(150, 50));
			
			getConnection3().getSourceAnchor().getOwner().setLocation(new Point(100, 300));
			getConnection4().getSourceAnchor().getOwner().setLocation(new Point(200, 300));
			
			getConnection1().setOrientation(ITreeConnection.Orientation.VERTICAL);
			getConnection2().setOrientation(ITreeConnection.Orientation.VERTICAL);
			getConnection3().setOrientation(ITreeConnection.Orientation.VERTICAL);
			getConnection4().setOrientation(ITreeConnection.Orientation.VERTICAL);
			
		} catch (Exception e) {
			fail("The ForestRouterTest.setUp method caught an exception - " + e); //$NON-NLS-1$
		}
	}
	
	public void testRouteTopDown() {
		// test default routing - no constraint set
		routeConnections("TopDown:no constraint"); //$NON-NLS-1$
		
		// now set constraint and change it simulating user gesture
		List newConstraint = new ArrayList(getConnection1().getPoints().size());
		PointList pts = PointListUtilities
				.copyPoints(getConnection1().getPoints());
		OrthogonalRouterUtilities.resetEndPointsToCenter(getConnection1(), pts);
		// test 1 change trunk end
		pts.setPoint(pts.getPoint(2).getTranslated(10, 0), 2);
		pts.setPoint(pts.getPoint(3).getTranslated(10, 0), 3);
		for (int i = 0; i < pts.size(); i++) {
			Bendpoint abp = new AbsoluteBendpoint(pts.getPoint(i));
			newConstraint.add(abp);
		}
		getForestRouter().setConstraint(getConnection1(), newConstraint);
		routeConnections("TopDown:change trunk end"); //$NON-NLS-1$
		// test 2 change trunk shoulder
		pts.setPoint(pts.getPoint(1).getTranslated(0, 10), 1);
		pts.setPoint(pts.getPoint(2).getTranslated(0, 10), 2);
		newConstraint = new ArrayList(getConnection1().getPoints().size());
		for (int i = 0; i < pts.size(); i++) {
			Bendpoint abp = new AbsoluteBendpoint(pts.getPoint(i));
			newConstraint.add(abp);
		}
		getForestRouter().setConstraint(getConnection1(), newConstraint);
		routeConnections("TopDown:change trunk shoulder"); //$NON-NLS-1$
		// test 3 change branch
		pts.setPoint(pts.getPoint(0).getTranslated(10, 0), 0);
		pts.setPoint(pts.getPoint(1).getTranslated(10, 0), 1);
		newConstraint = new ArrayList(getConnection1().getPoints().size());
		for (int i = 0; i < pts.size(); i++) {
			Bendpoint abp = new AbsoluteBendpoint(pts.getPoint(i));
			newConstraint.add(abp);
		}
		getForestRouter().setConstraint(getConnection1(), newConstraint);
		routeConnections("TopDown:change branch"); //$NON-NLS-1$
	}
	
	public void testMultiTrees() {
		//	test default routing - no constraint set
		routeMultiTreeConnections("TopDown:no constraint"); //$NON-NLS-1$
	}
}
