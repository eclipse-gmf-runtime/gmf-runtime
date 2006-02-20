/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.requests;


/**
 * 
 * Request that can optional have any UI suppressed.
 * @author choang
 * @canBeSeenBy %level1
 */
public interface SuppressibleUIRequest {

	public void setSuppressibleUI(boolean suppressUI);
	
	public boolean isUISupressed();
	
}
