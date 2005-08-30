/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.SetConnectorBendpointsCommand;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.SetAllBendpointRequest;
import org.eclipse.gmf.runtime.diagram.ui.util.SelectInDiagramHelper;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.LineMode;
import org.eclipse.gmf.runtime.notation.Edge;

/**
 * This EditPolicy defines the behavior of Bendpoints on a Connection.
 */
/*
 * @canBeSeenBy %partners
 */
public class ConnectorBendpointEditPolicy
	extends org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.ConnectorBendpointEditPolicy { 
	
	/**
	 * @param lineSegMode
	 */
	protected ConnectorBendpointEditPolicy(LineMode lineSegMode) {
		super(lineSegMode);
	}

	/**
	 * 
	 */
	public ConnectorBendpointEditPolicy() {
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
	 * @return Command SetBendpointsCommand that contains the point changes for the connector.
	 */
	protected Command getBendpointsChangedCommand(BendpointRequest request) {
		if ((getHost().getViewer() instanceof ScrollingGraphicalViewer)&&
				(getHost().getViewer().getControl() instanceof FigureCanvas)){
			SelectInDiagramHelper.exposeLocation((FigureCanvas)getHost().getViewer().getControl(),request.getLocation().getCopy());
		}
		Connection connection = getConnection();
		Edge connectorView = (Edge) request.getSource().getModel();
		
		return getBendpointsChangedCommand(connection, connectorView);
	}

	/**
	 * Method getBendpointsChangedCommand
	 * Different signature method that allows a command to constructed for changing the bendpoints
	 * without requiring the original Request.
	 * 
	 * @param connection Connection to generate the bendpoints changed command from
	 * @param connectorView IConnectorView notation element that the command will operate on.
	 * @return Command SetBendpointsCommand that contains the point changes for the connector.
	 */
	protected Command getBendpointsChangedCommand(Connection connection, Edge connectorView) {
		Point ptRef1 = connection.getSourceAnchor().getReferencePoint();
		getConnection().translateToRelative(ptRef1);

		Point ptRef2 = connection.getTargetAnchor().getReferencePoint();
		getConnection().translateToRelative(ptRef2);

		SetConnectorBendpointsCommand sbbCommand = new SetConnectorBendpointsCommand();
		sbbCommand.setConnectorAdapter(new EObjectAdapter(connectorView));
		sbbCommand.setNewPointList(connection.getPoints(), ptRef1, ptRef2);

		return new EtoolsProxyCommand(sbbCommand);
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

		SetConnectorBendpointsCommand sbbCommand = new SetConnectorBendpointsCommand();
		sbbCommand.setConnectorAdapter(new EObjectAdapter((Edge)getHost().getModel()));
		
		if (request.getSourceReference() != null && request.getTargetReference() != null) {
			sbbCommand.setNewPointList(
				newPoints, request.getSourceReference(), request.getTargetReference());
		}
		else {
			sbbCommand.setNewPointList(
				newPoints, connection.getSourceAnchor(), connection.getTargetAnchor());
		}
		
		return new EtoolsProxyCommand(sbbCommand);
	}
}
