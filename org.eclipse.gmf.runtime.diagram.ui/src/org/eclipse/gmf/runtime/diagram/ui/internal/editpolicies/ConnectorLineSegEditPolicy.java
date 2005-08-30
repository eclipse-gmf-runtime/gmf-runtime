/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectorBendpointEditPolicy;
import org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.LineMode;


/*
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class ConnectorLineSegEditPolicy
	extends ConnectorBendpointEditPolicy {

	/**
	 * @param lineSegMode
	 */
	public ConnectorLineSegEditPolicy() {
		super(LineMode.ORTHOGONAL_FREE);
	}
}
