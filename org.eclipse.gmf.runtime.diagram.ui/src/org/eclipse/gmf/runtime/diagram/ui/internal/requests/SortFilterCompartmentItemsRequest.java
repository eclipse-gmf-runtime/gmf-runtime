/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
