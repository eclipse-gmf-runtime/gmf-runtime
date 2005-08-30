/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectorBendpointEditPolicy;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ForestRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.TreeRouter;
import org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.LineMode;
import org.eclipse.gmf.runtime.notation.Edge;


/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * Override to specify the default LineMode as constrained for Tree connections.
 */
public class TreeConnectorBendpointEditPolicy
	extends ConnectorBendpointEditPolicy {

	/**
	 * Default constructor
	 */
	public TreeConnectorBendpointEditPolicy() {
		super(LineMode.ORTHOGONAL_CONSTRAINED);
	}
	
	static private Map connectors = new HashMap();
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		connectors.put(getConnection(), getHost());
		super.activate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate() {
		connectors.remove(getConnection());
		super.deactivate();
	}
	
	/**
	 * Method getBendpointsChangedCommand.
	 * This method will return a SetBendpointsCommand with the points retrieved from
	 * the user feedback in the figure.
	 * @param request BendpointRequest from the user gesture for moving / creating a bendpoint
	 * @return Command SetBendpointsCommand that contains the point changes for the connector.
	 */
	protected Command getBendpointsChangedCommand(BendpointRequest request) {
		Command cmd = super.getBendpointsChangedCommand(request);
		if (cmd == null)
			return null;
		
		//synch constraints up with the rest of the tree
		if (getConnection().getConnectionRouter() instanceof ForestRouter) {
			ForestRouter forestRouter = (ForestRouter)getConnection().getConnectionRouter();
			TreeRouter treeRouter = forestRouter.getSubRouter(getConnection());
			if (treeRouter != null) {
				List connList = treeRouter.getConnectionList();
				ListIterator li = connList.listIterator();
				while (li.hasNext()) {
					Connection conn = (Connection)li.next();
					if (!conn.equals(getConnection())) {
						EditPart connectorEP = (EditPart)connectors.get(conn);
						if (connectorEP != null) {
							Edge connectorView = (Edge) connectorEP.getModel();
							Command cmd1 = getBendpointsChangedCommand(conn, connectorView);
							if (cmd1 != null)
								cmd = cmd.chain(cmd1);
						}
					}
				}
			}
		}
		
		return cmd;
	}
}
