/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gef.tools.ConnectionEndpointTracker;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers.GeoshapeType;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.SetConnectionBendpointsCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.DiagramNotationType;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
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
			
			getCommandStack().execute(new ICommandProxy(bendpointsChanged));
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
    
    /**
     * A callback mechanism to test the line.
     */
    private static interface LineTester {
        void testLine(ConnectionEditPart lineEP); 
    }
    
    /**
     * Performs some tests with rectilinear connections to ensure that the line is
     * always connected to the source and target and that the line is rectilinear.See bugzilla#112996 for a
     * description of the original issue.If this test fails, it can be
     * reproduced manually on a logic diagram with notes and note attachments.
     * See the console output for the location of the notes.
     * 
     * @author Cherie Revells
     * @throws Exception
     */
    public void testRectilinearRoutingToConnection()
        throws Exception {
        try {
            getFixture().openDiagram();

            // Add three notes.
            final ShapeEditPart note1EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(100, 100));
            final ShapeEditPart note2EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(200, 300));
            final ShapeEditPart note3EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(300, 100));

            flushEventQueue();

            ConnectionNodeEditPart targetLineEP = (ConnectionNodeEditPart) getFixture()
                .createConnectorUsingTool(note1EP, note2EP, GeoshapeType.LINE);
            ConnectionNodeEditPart lineToTestEP = (ConnectionNodeEditPart) getFixture()
                .createConnectorUsingTool(note3EP, targetLineEP,
                    GeoshapeType.LINE);

            Request request = new ChangePropertyValueRequest(
                StringStatics.BLANK, Properties.ID_ROUTING,
                Routing.RECTILINEAR_LITERAL);

            Command cmd = lineToTestEP.getCommand(request);
            getCommandStack().execute(cmd);
            flushEventQueue();

            moveShapeAndTestLine(note3EP, lineToTestEP, new LineTester() {

                public void testLine(ConnectionEditPart lineEP) {
                    if (!areEndsConnected(lineEP)) {
                        failWithMessage("ends not connected", lineEP, note1EP, //$NON-NLS-1$
                            note2EP, note3EP);
                    } else if (!isRectilinear(lineEP)) {
                        failWithMessage("not rectilinear", lineEP, note1EP, //$NON-NLS-1$
                            note2EP, note3EP);
                    }
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            getFixture().closeDiagram();
        }
    }

    /**
     * Performs some tests with rectilinear connections to ensure that the line is
     * always connected to the source and target and that the line is rectilinear.See bugzilla#112996 for a
     * description of the original issue.If this test fails, it can be
     * reproduced manually on a logic diagram with notes and note attachments.
     * See the console output for the location of the notes.
     * 
     * @author Cherie Revells
     * @throws Exception
     */
    public void testRectilinearRoutingFromConnection()
        throws Exception {
        try {
            getFixture().openDiagram();

            // Add three notes.
            final ShapeEditPart note1EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(100, 100));
            final ShapeEditPart note2EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(200, 300));
            final ShapeEditPart note3EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(300, 100));

            flushEventQueue();

            ConnectionNodeEditPart sourceLineEP = (ConnectionNodeEditPart) getFixture()
                .createConnectorUsingTool(note1EP, note2EP, GeoshapeType.LINE);

            // Throw in some bendpoints

            ConnectionNodeEditPart lineToTestEP = (ConnectionNodeEditPart) getFixture()
                .createConnectorUsingTool(sourceLineEP, note3EP,
                    GeoshapeType.LINE);

            Request request = new ChangePropertyValueRequest(
                StringStatics.BLANK, Properties.ID_ROUTING,
                Routing.RECTILINEAR_LITERAL);

            Command cmd = lineToTestEP.getCommand(request);
            getCommandStack().execute(cmd);
            flushEventQueue();

            moveShapeAndTestLine(note3EP, lineToTestEP, new LineTester() {

                public void testLine(ConnectionEditPart lineEP) {
                    if (!areEndsConnected(lineEP)) {
                        failWithMessage("ends not connected", lineEP, note1EP, //$NON-NLS-1$
                            note2EP, note3EP);
                    } else if (!isRectilinear(lineEP)) {
                        failWithMessage("not rectilinear", lineEP, note1EP, //$NON-NLS-1$
                            note2EP, note3EP);
                    }
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            getFixture().closeDiagram();
        }
    }

    /**
     * Performs some tests with oblique connections to ensure that the line is
     * always connected to the source and target.If this test fails, it can be
     * reproduced manually on a logic diagram with notes and note attachments.
     * See the console output for the location of the notes.
     * 
     * @author Cherie Revells
     * @throws Exception
     */
    public void testObliqueRoutingToConnection()
        throws Exception {
        try {
            getFixture().openDiagram();

            // Add three notes.
            final ShapeEditPart note1EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(100, 100));
            final ShapeEditPart note2EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(200, 300));
            final ShapeEditPart note3EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(300, 100));
            flushEventQueue();

            ConnectionNodeEditPart targetLineEP = (ConnectionNodeEditPart) getFixture()
                .createConnectorUsingTool(note1EP, note2EP, GeoshapeType.LINE);
            ConnectionNodeEditPart lineToTestEP = (ConnectionNodeEditPart) getFixture()
                .createConnectorUsingTool(note3EP, targetLineEP,
                    GeoshapeType.LINE);
            flushEventQueue();

            moveShapeAndTestLine(note3EP, lineToTestEP, new LineTester() {

                public void testLine(ConnectionEditPart lineEP) {
                    if (!areEndsConnected(lineEP)) {
                        failWithMessage("ends not connected", lineEP, note1EP, //$NON-NLS-1$
                            note2EP, note3EP);
                    }
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            getFixture().closeDiagram();
        }
    }

    /**
     * Performs some tests with oblique connections to ensure that the line is
     * always connected to the source and target.If this test fails, it can be
     * reproduced manually on a logic diagram with notes and note attachments.
     * See the console output for the location of the notes.
     * 
     * @author Cherie Revells
     * @throws Exception
     */
    public void testObliqueRoutingFromConnection()
        throws Exception {
        try {
            getFixture().openDiagram();

            // Add three notes.
            final ShapeEditPart note1EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(100, 100));
            final ShapeEditPart note2EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(200, 300));
            final ShapeEditPart note3EP = getFixture().createShapeUsingTool(
                DiagramNotationType.NOTE, new Point(300, 100));
            flushEventQueue();

            ConnectionNodeEditPart sourceLineEP = (ConnectionNodeEditPart) getFixture()
                .createConnectorUsingTool(note1EP, note2EP, GeoshapeType.LINE);
            ConnectionNodeEditPart lineToTestEP = (ConnectionNodeEditPart) getFixture()
                .createConnectorUsingTool(sourceLineEP, note3EP,
                    GeoshapeType.LINE);
            flushEventQueue();

            moveShapeAndTestLine(note3EP, lineToTestEP, new LineTester() {

                public void testLine(ConnectionEditPart lineEP) {
                    if (!areEndsConnected(lineEP)) {
                        failWithMessage("ends not connected", lineEP, note1EP, //$NON-NLS-1$
                            note2EP, note3EP);
                    }
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            getFixture().closeDiagram();
        }
    }

    /**
     * Verifies conditions on the line as note3 is moved around.
     * @param note1EP
     * @param note2EP
     * @param note3EP
     * @param lineToTestEP
     * @param isRectilinear
     */
    private void moveShapeAndTestLine(ShapeEditPart shapeToMoveEP,
            ConnectionNodeEditPart lineToTestEP, LineTester lineTester) {

        lineTester.testLine(lineToTestEP);

        // Move the end note around a bit so that the rectilinear connection
        // will move.
        ChangeBoundsRequest moveRequest = new ChangeBoundsRequest(
            RequestConstants.REQ_MOVE);
        moveRequest.setEditParts(shapeToMoveEP);

        for (int i = 0; i <= 50; i++) {
            moveRequest.setMoveDelta(new Point(0, 5));
            getCommandStack().execute(shapeToMoveEP.getCommand(moveRequest));
            flushEventQueue();
            lineTester.testLine(lineToTestEP);
        }
        for (int i = 0; i <= 50; i++) {
            moveRequest.setMoveDelta(new Point(-5, 0));
            getCommandStack().execute(shapeToMoveEP.getCommand(moveRequest));
            flushEventQueue();
            lineTester.testLine(lineToTestEP);
        }
        for (int i = 0; i <= 50; i++) {
            moveRequest.setMoveDelta(new Point(0, -5));
            getCommandStack().execute(shapeToMoveEP.getCommand(moveRequest));
            flushEventQueue();
            lineTester.testLine(lineToTestEP);
        }
    }

    /**
     * Verifies that the connection editpart passed in is connected to the
     * source and target ends.
     * 
     * @param lineEP
     * @return
     */
    private boolean areEndsConnected(ConnectionEditPart lineEP) {

        Point firstPoint = ((Connection) lineEP.getFigure()).getPoints()
            .getFirstPoint();
        Point lastPoint = ((Connection) lineEP.getFigure()).getPoints()
            .getLastPoint();

        // Leave a little space to account for rounding errors in himetric mode.  It is hardly noticeable.
        Dimension buffer = new Dimension(3, 3);

        lineEP.getFigure().translateToRelative(buffer);

        IFigure sourceFigure = ((IGraphicalEditPart) lineEP.getSource())
            .getFigure();
        if (sourceFigure instanceof Connection) {
            PointList points = ((Connection) sourceFigure).getPoints();
            int index = PointListUtilities.findNearestLineSegIndexOfPoint(
                points, firstPoint);
            LineSeg lineSeg = (LineSeg) PointListUtilities.getLineSegments(
                points).get(index - 1);
            if (!lineSeg.containsPoint(firstPoint, buffer.height * 2)) {
                return false;
            }
        } else {
            Rectangle bounds = sourceFigure.getBounds().getCopy();
            bounds.expand(buffer.width, buffer.height);
            if (!bounds.contains(firstPoint)) {
                return false;
            }
        }

        IFigure targetFigure = ((IGraphicalEditPart) lineEP.getTarget())
            .getFigure();
        if (targetFigure instanceof Connection) {
            PointList points = ((Connection) targetFigure).getPoints();
            int index = PointListUtilities.findNearestLineSegIndexOfPoint(
                points, lastPoint);
            LineSeg lineSeg = (LineSeg) PointListUtilities.getLineSegments(
                points).get(index - 1);
            if (!lineSeg.containsPoint(lastPoint, buffer.height * 2)) {
                return false;
            }
        } else {
            Rectangle bounds = targetFigure.getBounds().getCopy();
            bounds.expand(buffer.width, buffer.height);
            if (!bounds.contains(lastPoint)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verifies that the connection editpart passed in is actually rectilinear.
     * 
     * @param lineEP
     * @return
     */
    private boolean isRectilinear(ConnectionEditPart lineEP) {

        PointList points = ((Connection) lineEP.getFigure()).getPoints();

        // Verify that the line is in fact rectilinear.
        for (int i = 0; i < points.size() - 1; i++) {
            Point ptCurrent = points.getPoint(i);
            Point ptNext = points.getPoint(i + 1);
            if (!(ptCurrent.x == ptNext.x || ptCurrent.y == ptNext.y)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prints debug info to the console and fails the test.
     */
    private void failWithMessage(String message, ConnectionEditPart lineEP,
            ShapeEditPart note1EP, ShapeEditPart note2EP, ShapeEditPart note3EP) {
        System.out.println("------ " + message); //$NON-NLS-1$
        System.out
            .println("Issue can be reproduced with notes in the following locations:"); //$NON-NLS-1$
        System.out.println("note1: " + note1EP.getFigure().getBounds()); //$NON-NLS-1$
        System.out.println("note2: " + note2EP.getFigure().getBounds()); //$NON-NLS-1$
        System.out.println("note3: " + note3EP.getFigure().getBounds()); //$NON-NLS-1$
        System.out.println("connection start: " //$NON-NLS-1$
            + ((Connection) lineEP.getFigure()).getPoints().getFirstPoint());
        System.out.println("connection end: " //$NON-NLS-1$
            + ((Connection) lineEP.getFigure()).getPoints().getLastPoint());

        fail("See console for details."); //$NON-NLS-1$
    }

}
