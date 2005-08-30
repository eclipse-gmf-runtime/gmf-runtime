/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.requests;

import java.util.List;

import org.eclipse.gef.Request;

import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * Request for Sort/Filter dialog contents.  EditPolicy ask for SortFilterPage s
 * from <code>ListCompartmentEditPart</code> which support the REQ_SORT_FILTER_CONTENT
 * role.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class SortFilterContentRequest extends Request {

	private List pageList;

	/**
	 * Constructor
	 * @param the pageList
	 */
	public SortFilterContentRequest(List pageList) {
		super(RequestConstants.REQ_SORT_FILTER_CONTENT);
		this.pageList = pageList;
	}

	/**
	 * Gets the SortFilterPage <code>List</code> so contributions may
	 * be added to the <code>SortFilterDialog</code> 
	 * @return the <code>List</code> of page contributions
	 */
	public List getSortFilterPageList() {
		return this.pageList;
	}
}
