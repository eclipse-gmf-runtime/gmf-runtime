/******************************************************************************
 * Copyright (c) 2002, 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
