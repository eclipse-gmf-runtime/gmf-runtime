/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.SetConnectionBendpointsCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.JumpLinkStatus;
import org.eclipse.gmf.runtime.notation.JumpLinkType;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.Smoothness;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.ITestCommandCallback;

/**
 * @author choang
 *
 * AbstractConnectTests that contain the test logic that is common across diagram
 * Should be extended by diagram specic test to test connections functionality.
 */
public abstract class AbstractConnectionTests extends AbstractTestBase {

	/**
	 * Constructor for AbstractConnectionTests.
	 * @param arg0
	 */
	public AbstractConnectionTests(String arg0) {
		super(arg0);
	}

	static public final Point ptStart = new Point(100, 100);
	static public final Point ptEnd = new Point(300, 300);
	static public final Point ptMiddle = new Point(200, 200);

	public void testDeleteConnection() throws Exception {

		final Object[] diagramValues = new Object[2];
		getTestFixture().getEditingDomain().runExclusive(
			new Runnable() {
			public void run() {

				Diagram dgrm2 =
					getTestFixture().getConnectorView().getDiagram();

				diagramValues[0] = dgrm2;
				diagramValues[1] = new Integer(dgrm2.getEdges().size());
			}
		});

		final Diagram dgrm = (Diagram) diagramValues[0];

		final int dgrmSize = ((Integer) diagramValues[1]).intValue();

		DeleteCommand delete =
			new DeleteCommand(getTestFixture()
            .getEditingDomain(), getTestFixture().getConnectorView());

		testCommand(delete, new ITestCommandCallback() {
			public void onCommandExecution() {
				assertTrue(dgrm.getEdges().size() == (dgrmSize - 1));
			}
		});

	}

	public void testAddBendpoints() throws Exception {

				// moved to setup of super class 
				final Edge connectorView =
					getTestFixture().getConnectorView();

				RelativeBendpoints pbs = (RelativeBendpoints) connectorView.getBendpoints(); 
				assertTrue(pbs.getPoints().size() == 2);

				PointList newpts = new PointList(3);
				newpts.addPoint(new Point(ptStart));
				newpts.addPoint(new Point(ptMiddle));
				newpts.addPoint(new Point(ptEnd));

				Point r1 = new Point(ptStart);
				Point r2 = new Point(ptEnd);

				SetConnectionBendpointsCommand bendpointsChanged =
					new SetConnectionBendpointsCommand(getTestFixture().getEditingDomain());
				bendpointsChanged.setEdgeAdapter(new EObjectAdapter(connectorView));
				bendpointsChanged.setNewPointList(newpts, r1, r2);
				testCommand(bendpointsChanged, new ITestCommandCallback() {
					public void onCommandExecution() {
						RelativeBendpoints bendpoints = (RelativeBendpoints) connectorView.getBendpoints(); 
						assertTrue(bendpoints.getPoints().size() == 3);
					}
				});
				flushEventQueue();

				// now test smooth connector
				final Smoothness s1 = Smoothness.NORMAL_LITERAL;
				SetPropertyCommand c =
					new SetPropertyCommand(getTestFixture().getEditingDomain(), new EObjectAdapter(connectorView),
						Properties.ID_SMOOTHNESS,
						"", //$NON-NLS-1$
						s1);
				testCommand(c, new ITestCommandCallback() {
					public void onCommandExecution() {
						Object s2 = ViewUtil.getStructuralFeatureValue(connectorView,
							NotationPackage.eINSTANCE.getRoutingStyle_Smoothness());
						assertTrue(s1.equals(s2));
					}
				});

				newpts.removeAllPoints();

				newpts.addPoint(new Point(ptStart));
				newpts.addPoint(new Point(ptMiddle));
				newpts.addPoint(
					new Point(
						getDiagramEditPart()
							.getFigure()
							.getBounds()
							.getBottomRight()));
				newpts.addPoint(new Point(ptEnd));

				bendpointsChanged = new SetConnectionBendpointsCommand(getTestFixture().getEditingDomain());
				bendpointsChanged.setEdgeAdapter(new EObjectAdapter(connectorView));
				bendpointsChanged.setNewPointList(newpts, r1, r2);
				testCommand(bendpointsChanged, new ITestCommandCallback() {
					public void onCommandExecution() {
						RelativeBendpoints bendpoints = (RelativeBendpoints) connectorView.getBendpoints(); 
						assertTrue(bendpoints.getPoints().size() == 4);
					}
				});
				flushEventQueue();
			}

	/**
	 * Method testConnectionProperties.
	 * @throws Exception
	 */
	public void testConnectionProperties() throws Exception {
				final Edge connectorView =
					getTestFixture().getConnectorView();

				testProperty(
					connectorView,
					Properties.ID_JUMPLINKS_STATUS,
					JumpLinkStatus.ABOVE_LITERAL);
				flushEventQueue();

				testProperty(
					connectorView,
					Properties.ID_JUMPLINKS_TYPE,
					JumpLinkType.SEMICIRCLE_LITERAL);
				flushEventQueue();

				testProperty(
					connectorView,
					Properties.ID_JUMPLINKS_REVERSE,
					Boolean.TRUE);
				flushEventQueue();

//				testProperty(
//					connectorView,
//					Properties.ID_LINECOLOR,
//					org.eclipse.draw2d.ColorConstants.red);
//				flushEventQueue();

				testProperty(
					connectorView,
					Properties.ID_CLOSESTDISTANCE,
					Boolean.TRUE);
				flushEventQueue();

				testProperty(
					connectorView,
					Properties.ID_AVOIDOBSTRUCTIONS,
					Boolean.TRUE);
				flushEventQueue();

				testProperty(
					connectorView,
					Properties.ID_SMOOTHNESS,
					Smoothness.NORMAL_LITERAL);
				flushEventQueue();

				testProperty(
					connectorView,
					Properties.ID_ROUTING,
					Routing.RECTILINEAR_LITERAL);
				flushEventQueue();

				testProperty(
					connectorView,
					Properties.ID_ROUTING,
					Routing.MANUAL_LITERAL);
				flushEventQueue();
				
				testProperty(
					connectorView,
					Properties.ID_ROUNDED_BENDPOINTS_RADIUS,
					10);
				flushEventQueue();				
	}

}
