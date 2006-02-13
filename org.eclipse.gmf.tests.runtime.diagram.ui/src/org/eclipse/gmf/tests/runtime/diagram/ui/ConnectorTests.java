/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gef.tools.ConnectionEndpointTracker;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers.GeoshapeType;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.SetConnectionBendpointsCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.DiagramNotationType;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture;

/**
 * @author sshaw
 * 
 * ConnectorsTests
 */
public class ConnectorTests
	extends AbstractConnectionTests {

	public static Test suite() {
		return new TestSuite(ConnectorTests.class);
	}

	/**
	 * @param arg0
	 */
	public ConnectorTests(String arg0) {
		super(arg0);
	}

	protected void setTestFixture() {
		testFixture = new DiagramTestFixture();
	}

	protected AbstractPresentationTestFixture getFixture() {
		return (AbstractPresentationTestFixture) testFixture;
	}

	public void testSelfConnector_RATLC00533255()
		throws Exception {
		try {
			getFixture().openDiagram();
			// Add a notes.
			NoteEditPart note1EP = (NoteEditPart) getFixture()
				.createShapeUsingTool(DiagramNotationType.NOTE,
					new Point(10, 10));

			ConnectionNodeEditPart line = (ConnectionNodeEditPart) getFixture()
				.createConnectorUsingTool(note1EP, note1EP, GeoshapeType.LINE);

			flushEventQueue();

			class MyConnectorEndpointTracker
				extends ConnectionEndpointTracker {

				private Point location;

				public MyConnectorEndpointTracker(ConnectionEditPart cep,
						Point location) {
					super(cep);
					this.location = location;
				}

				public void updateTargetRequest() {
					super.updateTargetRequest();
				}

				public Request getTargetRequest() {
					return super.getTargetRequest();
				}

				public Point getLocation() {
					return location;
				}

				public boolean updateTargetUnderMouse() {
					return false;
				}
			}

			PointList pointList = line.getConnectionFigure().getPoints();
			assertTrue(pointList.size() > 1);
			assertFalse(pointList.getFirstPoint().equals(
				pointList.getLastPoint()));
			Point copySrcPoint = pointList.getFirstPoint().getCopy();
			copySrcPoint.translate(0, 20);
			MyConnectorEndpointTracker tracker = new MyConnectorEndpointTracker(
				line, copySrcPoint);
			tracker.setCommandName(RequestConstants.REQ_RECONNECT_SOURCE);
			tracker.setConnectionEditPart(line);
			tracker.updateTargetRequest();
			ReconnectRequest reconnectRequest = (ReconnectRequest) tracker
				.getTargetRequest();
			reconnectRequest.setTargetEditPart(note1EP);

			Command command = note1EP.getCommand(reconnectRequest);
			getCommandStack().execute(command);
			flushEventQueue();

			PointList pointList_2 = line.getConnectionFigure().getPoints();
			assertTrue(pointList.size() > 1);
			Point srcPoint_2 = pointList_2.getFirstPoint();
			Point targetPoint_2 = pointList_2.getLastPoint();
			assertFalse(srcPoint_2.equals(targetPoint_2));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getFixture().closeDiagram();
		}
	}

	public void testReorientRectilinear_bugzilla113003()
		throws Exception {
		try {
			getFixture().openDiagram();
			// Add a notes.
			NoteEditPart note1EP = (NoteEditPart) getFixture()
				.createShapeUsingTool(DiagramNotationType.NOTE,
					new Point(10, 10));

			NoteEditPart note2EP = (NoteEditPart) getFixture()
			.createShapeUsingTool(DiagramNotationType.NOTE,
				new Point(300, 10));
			
			ConnectionNodeEditPart line = (ConnectionNodeEditPart) getFixture()
			.createConnectorUsingTool(note1EP, note2EP, GeoshapeType.LINE);
			
			flushEventQueue();

			Request request = new ChangePropertyValueRequest(
				StringStatics.BLANK,
				Properties.ID_ROUTING,
				Routing.RECTILINEAR_LITERAL );
		
			Command cmd = line.getCommand( request );
			getCommandStack().execute(cmd);

			// Now move the line in order to create 2 bendpoints
			PointList pointList = line.getConnectionFigure().getPoints();
			
			PointList newpts = new PointList(3);
			newpts.addPoint(new Point(pointList.getFirstPoint()));
			newpts.addPoint(new Point(new Point(150, 100)));
			newpts.addPoint(new Point(pointList.getLastPoint()));

			Point r1 = new Point(pointList.getFirstPoint());
			Point r2 = new Point(pointList.getLastPoint());

			SetConnectionBendpointsCommand bendpointsChanged =
				new SetConnectionBendpointsCommand(getTestFixture().getEditingDomain());
			bendpointsChanged.setEdgeAdapter(new EObjectAdapter(line.getNotationView()));
			bendpointsChanged.setNewPointList(newpts, r1, r2);
			
			getCommandStack().execute(new EtoolsProxyCommand(bendpointsChanged));
			flushEventQueue();

			assertTrue(line.getConnectionFigure().getPoints().size() == 4);
			
			class MyConnectorEndpointTracker
				extends ConnectionEndpointTracker {

				private Point location;

				public MyConnectorEndpointTracker(ConnectionEditPart cep,
						Point location) {
					super(cep);
					this.location = location;
				}

				public boolean handleDragInProgress() {
					return super.handleDragInProgress();
				}

				public Request getTargetRequest() {
					return super.getTargetRequest();
				}

				public Point getLocation() {
					return location;
				}

				public boolean updateTargetUnderMouse() {
					return false;
				}
			}

			assertTrue(pointList.size() > 1);
			assertFalse(pointList.getFirstPoint().equals(
				pointList.getLastPoint()));
			
			// track it into space...
			Point newSrcPoint = new Point(500, 500);
			MyConnectorEndpointTracker tracker = new MyConnectorEndpointTracker(
				line, newSrcPoint);
			tracker.setCommandName(RequestConstants.REQ_RECONNECT_SOURCE);
			tracker.setConnectionEditPart(line);
			tracker.handleDragInProgress();
			line.getConnectionFigure().revalidate();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getFixture().closeDiagram();
		}
	}
}
