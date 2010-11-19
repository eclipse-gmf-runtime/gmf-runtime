/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
