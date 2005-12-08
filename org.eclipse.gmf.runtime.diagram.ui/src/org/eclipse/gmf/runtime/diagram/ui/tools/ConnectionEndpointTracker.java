/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
/*
 * Created on Apr 28, 2003
 *
 */
package org.eclipse.gmf.runtime.diagram.ui.tools;

import org.eclipse.gef.ConnectionEditPart;

/**
 * @author mmuszyns
 * 
 * This class overrides its superclass in order to fix a problem with proper
 * tracking feedback for zooming and scrolling.
 * 
 * @deprecated use {@link org.eclipse.gef.tools.ConnectionEndpointTracker}. Will be removed on January 17th / 2006.
 */
public class ConnectionEndpointTracker
	extends org.eclipse.gef.tools.ConnectionEndpointTracker {

	public ConnectionEndpointTracker(ConnectionEditPart cep) {
		super(cep);
		// TODO Auto-generated constructor stub
	}
}
