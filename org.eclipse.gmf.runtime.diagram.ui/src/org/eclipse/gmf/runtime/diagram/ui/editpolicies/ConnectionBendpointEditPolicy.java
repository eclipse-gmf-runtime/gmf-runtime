/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.SetConnectionBendpointsCommand;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.SetAllBendpointRequest;
import org.eclipse.gmf.runtime.diagram.ui.util.SelectInDiagramHelper;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.LineMode;
import org.eclipse.gmf.runtime.notation.Edge;

/**
 * This EditPolicy defines the behavior of Bendpoints on a Connection.
 */
public class ConnectionBendpointEditPolicy
	extends org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.ConnectionBendpointEditPolicy { 
	
	/**
	 * @param lineSegMode
	 */
	protected ConnectionBendpointEditPolicy(LineMode lineSegMode) {
		super(lineSegMode);
	}

	/**
	 * 
	 */
	public ConnectionBendpointEditPolicy() {
		super(LineMode.OBLIQUE);
	}

	/**
	 * Returns the appropriate Command for the request type given.  Handles
	 * creating, moving and deleting bendpoints.  The actual creation of the
	 * command is taken care of by subclasses implementing the appropriate
	 * methods.
	 *
	 * @see #getCreateBendpointCommand(BendpointRequest)
	 * @see #getMoveBendpointCommand(BendpointRequest)
	 * @see #getDeleteBendpointCommand(BendpointRequest)
	 */
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_SET_ALL_BENDPOINT.equals(request.getType()))
			return getSetBendpointCommand((SetAllBendpointRequest)request);
			
		return super.getCommand(request);
	}

	/**
	 * Method getBendpointsChangedCommand.
	 * This method will return a SetBendpointsCommand with the points retrieved from
	 * the user feedback in the figure.
	 * @param request BendpointRequest from the user gesture for moving / creating a bendpoint
	 * @return Command SetBendpointsCommand that contains the point changes for the connection.
	 */
	protected Command getBendpointsChangedCommand(BendpointRequest request) {
		if ((getHost().getViewer() instanceof ScrollingGraphicalViewer)&&
				(getHost().getViewer().getControl() instanceof FigureCanvas)){
			SelectInDiagramHelper.exposeLocation((FigureCanvas)getHost().getViewer().getControl(),request.getLocation().getCopy());
		}
		Connection connection = getConnection();
		Edge connectionView = (Edge) request.getSource().getModel();
		
		return getBendpointsChangedCommand(connection, connectionView);
	}

	/**
	 * Method getBendpointsChangedCommand
	 * Different signature method that allows a command to constructed for changing the bendpoints
	 * without requiring the original Request.
	 * 
	 * @param connection Connection to generate the bendpoints changed command from
	 * @param edge notation element that the command will operate on.
	 * @return Command SetBendpointsCommand that contains the point changes for the connection.
	 */
	protected Command getBendpointsChangedCommand(Connection connection, Edge edge) {
		Point ptRef1 = connection.getSourceAnchor().getReferencePoint();
		getConnection().translateToRelative(ptRef1);

		Point ptRef2 = connection.getTargetAnchor().getReferencePoint();
		getConnection().translateToRelative(ptRef2);

        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();
        
        SetConnectionBendpointsCommand sbbCommand = new SetConnectionBendpointsCommand(
            editingDomain);
        sbbCommand.setEdgeAdapter(new EObjectAdapter(edge));
		sbbCommand.setNewPointList(connection.getPoints(), ptRef1, ptRef2);

		return new ICommandProxy(sbbCommand);
	}

	/**
	 * Method getSetBendpointCommand.
	 * This method returns a command that executes the REQ_SET_ALL_BENDPOINT request
	 * @param request SetAllBendpointRequest that stores the points to be set by the command.
	 * @return Command to be executed.
	 */
	protected Command getSetBendpointCommand(SetAllBendpointRequest request) {
		Connection connection = getConnection();
		PointList newPoints = request.getPoints();

        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();
        SetConnectionBendpointsCommand sbbCommand = new SetConnectionBendpointsCommand(editingDomain);
		sbbCommand.setEdgeAdapter(new EObjectAdapter((Edge)getHost().getModel()));
		
		if (request.getSourceReference() != null && request.getTargetReference() != null) {
			sbbCommand.setNewPointList(
				newPoints, request.getSourceReference(), request.getTargetReference());
		}
		else {
			sbbCommand.setNewPointList(
				newPoints, connection.getSourceAnchor(), connection.getTargetAnchor());
		}
		
		return new ICommandProxy(sbbCommand);
	}
}
