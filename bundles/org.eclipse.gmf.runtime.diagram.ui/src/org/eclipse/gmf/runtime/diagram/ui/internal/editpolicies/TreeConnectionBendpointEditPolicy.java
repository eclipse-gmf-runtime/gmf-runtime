/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionBendpointEditPolicy;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ForestRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.TreeRouter;
import org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.LineMode;
import org.eclipse.gmf.runtime.notation.Edge;


/**
 * @author sshaw
 *
 * Override to specify the default LineMode as constrained for Tree connections.
 */
public class TreeConnectionBendpointEditPolicy
	extends ConnectionBendpointEditPolicy {

	/**
	 * Default constructor
	 */
	public TreeConnectionBendpointEditPolicy() {
		super(LineMode.ORTHOGONAL_CONSTRAINED);
	}
	
	static private Map connections = new HashMap();
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		connections.put(getConnection(), getHost());
		super.activate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate() {
		connections.remove(getConnection());
		super.deactivate();
	}
	
	/**
	 * Method getBendpointsChangedCommand.
	 * This method will return a SetBendpointsCommand with the points retrieved from
	 * the user feedback in the figure.
	 * @param request BendpointRequest from the user gesture for moving / creating a bendpoint
	 * @return Command SetBendpointsCommand that contains the point changes for the connection.
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
						EditPart connectionEP = (EditPart)connections.get(conn);
						if (connectionEP != null) {
							Edge connectionView = (Edge) connectionEP.getModel();
							Command cmd1 = getBendpointsChangedCommand(conn, connectionView);
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
