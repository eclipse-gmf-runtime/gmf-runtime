/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.commands;

import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * Edit command to configure a new relationship element with the characteristics
 * of its element type.
 * 
 * @author ldamus
 */
public abstract class ConfigureRelationshipCommand
	extends ConfigureElementCommand {

	/**
	 * Constructs a new element configuration command for the
	 * <code>request</code>.
	 * 
	 * @param request
	 *            the element configuration request
	 */
	public ConfigureRelationshipCommand(ConfigureRequest request) {

		super(request);
	}

	public boolean canExecute() {
		Object source = ((ConfigureRequest) getRequest())
			.getParameter(CreateRelationshipRequest.SOURCE);

		Object target = ((ConfigureRequest) getRequest())
			.getParameter(CreateRelationshipRequest.TARGET);
		return source != null && target != null && super.canExecute();
	}

}