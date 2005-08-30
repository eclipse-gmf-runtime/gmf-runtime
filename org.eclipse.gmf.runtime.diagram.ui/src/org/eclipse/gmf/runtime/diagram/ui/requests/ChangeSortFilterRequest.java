/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.Request;

import com.ibm.xtools.notation.Filtering;
import com.ibm.xtools.notation.Sorting;


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
