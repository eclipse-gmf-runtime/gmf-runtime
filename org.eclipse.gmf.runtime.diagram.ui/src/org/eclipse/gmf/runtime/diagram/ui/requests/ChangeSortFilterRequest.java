/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.notation.Filtering;
import org.eclipse.gmf.runtime.notation.Sorting;


/**
 * Request to set the sort/filter view properties.
 * 
 * @author jcorchis
 */
public class ChangeSortFilterRequest
	extends Request {
	
	// Sort items
	private Filtering filtering;
	private List filteredObjects;
	private List filterKeys;
	
	// Filter items
	private Sorting sorting;
	private List sortedObjects;
	private Map sortKeys;
	
	/**
	 * 
	 */
	public ChangeSortFilterRequest() {
		super(RequestConstants.REQ_CHANGE_SORT_FILTER);
		
	}
	
	/**
	 * 
	 * @param filtering
	 * @param filteredObjects
	 * @param filterKeys
	 * @param sorting
	 * @param sortedObjects
	 * @param sortKeys
	 */
	public ChangeSortFilterRequest(Filtering filtering, List filteredObjects, List filterKeys,
			Sorting sorting, List sortedObjects, Map sortKeys) {
		
		super(RequestConstants.REQ_CHANGE_SORT_FILTER);
		
		this.filtering = filtering;
		this.filteredObjects = filteredObjects;
		this.filterKeys = filterKeys;
		
		this.sorting = sorting;
		this.sortedObjects = sortedObjects;
		this.sortKeys = sortKeys;
		
	}

	/**
	 * @return Returns the filteredObjects.
	 */
	public List getFilteredObjects() {
		return filteredObjects;
	}
	/**
	 * @param filteredObjects The filteredObjects to set.
	 */
	public void setFilteredObjects(List filteredObjects) {
		this.filteredObjects = filteredObjects;
	}
	/**
	 * @return Returns the filtering.
	 */
	public Filtering getFiltering() {
		return filtering;
	}
	/**
	 * @param filtering The filtering to set.
	 */
	public void setFiltering(Filtering filtering) {
		this.filtering = filtering;
	}
	/**
	 * @return Returns the filterKeys.
	 */
	public List getFilterKeys() {
		return filterKeys;
	}
	/**
	 * @param filterKeys The filterKeys to set.
	 */
	public void setFilterKeys(List filterKeys) {
		this.filterKeys = filterKeys;
	}
	/**
	 * @return Returns the sortedObjects.
	 */
	public List getSortedObjects() {
		return sortedObjects;
	}
	/**
	 * @param sortedObjects The sortedObjects to set.
	 */
	public void setSortedObjects(List sortedObjects) {
		this.sortedObjects = sortedObjects;
	}
	/**
	 * @return Returns the sorting.
	 */
	public Sorting getSorting() {
		return sorting;
	}
	/**
	 * @param sorting The sorting to set.
	 */
	public void setSorting(Sorting sorting) {
		this.sorting = sorting;
	}
	/**
	 * @return Returns the sortKeys.
	 */
	public Map getSortKeys() {
		return sortKeys;
	}
	/**
	 * @param sortKeys The sortKeys to set.
	 */
	public void setSortKeys(Map sortKeys) {
		this.sortKeys = sortKeys;
	}
}
