/******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
