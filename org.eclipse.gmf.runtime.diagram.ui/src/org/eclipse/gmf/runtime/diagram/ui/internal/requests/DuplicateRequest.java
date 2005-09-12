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

package org.eclipse.gmf.runtime.diagram.ui.internal.requests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * The request used to duplicate a list of editparts. A list that will hold the
 * new duplicated views after the command is executed can be retrieved via
 * <code>getDuplicatedViews()</code>.
 * 
 * @author cmahoney
 * @canBeSeenBy %level1
 */
public class DuplicateRequest
	extends GroupRequest {

	/**
	 * This will be populated with the views that are duplicated after the
	 * command executes.
	 */
	List duplicatedViews = new ArrayList();

	/**
	 * Creates a new <code>DuplicateElementsRequest</code>.
	 */
	public DuplicateRequest() {
		super(RequestConstants.REQ_DUPLICATE);
	}

	/**
	 * Gets the list that will hold the new duplicated views after the command
	 * is executed.
	 * 
	 * @return Returns the duplicatedViews.
	 */
	public List getDuplicatedViews() {
		return duplicatedViews;
	}

}