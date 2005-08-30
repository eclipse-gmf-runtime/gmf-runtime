/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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

	public boolean isExecutable() {
		Object source = ((ConfigureRequest) getRequest())
			.getParameter(CreateRelationshipRequest.SOURCE);

		Object target = ((ConfigureRequest) getRequest())
			.getParameter(CreateRelationshipRequest.TARGET);
		return source != null && target != null && super.isExecutable();
	}

}