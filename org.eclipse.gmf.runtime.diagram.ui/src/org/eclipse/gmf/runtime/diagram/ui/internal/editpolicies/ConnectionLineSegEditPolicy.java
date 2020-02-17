/******************************************************************************
 * Copyright (c) 2002, 2003, 2005 IBM Corporation and others.
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
