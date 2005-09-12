/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
/*
 * Created on Mar 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.gmf.tests.runtime.diagram.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ReconnectRequest;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers.GeoshapeType;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.PresentationNotationType;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture;
import org.eclipse.gmf.runtime.diagram.ui.tools.ConnectorEndpointTracker;


/**
 * @author sshaw
 *
 * ConnectorsTests
 */
public class ConnectorTests extends AbstractConnectorTests {

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
				.createShapeUsingTool(PresentationNotationType.NOTE,
					new Point(10, 10));
			
			ConnectionNodeEditPart line = (ConnectionNodeEditPart)getFixture()
				.createConnectorUsingTool(note1EP, note1EP, GeoshapeType.LINE);

			flushEventQueue();
			
			class MyConnectorEndpointTracker
				extends ConnectorEndpointTracker {

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

			PointList pointList_2 = line.getConnectionFigure()
				.getPoints();
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
}
