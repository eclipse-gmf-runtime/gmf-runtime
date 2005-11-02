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

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionBendpointEditPolicy;
import org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.LineMode;

public class ConnectionLineSegEditPolicy
	extends ConnectionBendpointEditPolicy {

	/**
	 * @param lineSegMode
	 */
	public ConnectionLineSegEditPolicy() {
		super(LineMode.ORTHOGONAL_FREE);
	}
}
