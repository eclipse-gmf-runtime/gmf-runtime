/******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.requests;

import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * Request to sort/filter list compartment items.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class SortFilterCompartmentItemsRequest
	extends GroupRequest {

	/**
	 * Default constructor. Creates a Request of type <code>RequestConstants.REQ_SORT_FILTER_COMPARTMENT</code>
	 */
	public SortFilterCompartmentItemsRequest() {
		super(RequestConstants.REQ_SORT_FILTER_COMPARTMENT);
	}
}
