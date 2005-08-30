/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter;

import java.text.Collator;
import java.util.Locale;

import org.eclipse.jface.viewers.ViewerSorter;

import com.ibm.xtools.notation.SortingDirection;

/**
 * Provides a <code>ViewerSorter</code> which tracks the type 
 * of sorting to ascending or descending orders.
 * 
 * @author jcorchis
 */
public class SortFilterViewerSorter extends ViewerSorter {
	
	/** The sorting direction for comparator */
	private SortingDirection sortingDirection = SortingDirection.ASCENDING_LITERAL;
	/** The hint for the sorting.  Defines the item to sort on  */
	protected int criteria;
	/** The locale specific collator */
	protected Collator collator = Collator.getInstance(Locale.getDefault());	
	

	/**
	 * Toggles the sorting direction from its current state.
	 */
	public void toggleSortingDirection() {
		sortingDirection = (SortingDirection.ASCENDING_LITERAL.equals(sortingDirection)) 
			? SortingDirection.DESCENDING_LITERAL
			: SortingDirection.ASCENDING_LITERAL;
	}

	/**
	 * @return Returns the sortingDirection.
	 */
	public SortingDirection getSortingDirection() {
		return sortingDirection;
	}
	/**
	 * @param sortingDirection The sortingDirection to set.
	 */
	public void setSortingDirection(SortingDirection sortingDirection) {
		this.sortingDirection = sortingDirection;
	}
	
	/**
	 * @return Returns the criteria.
	 */
	public int getCriteria() {
		return criteria;
	}
	
	/**
	 * @param criteria The criteria to set.
	 */
	public void setCriteria(int criteria) {
		this.criteria = criteria;
	}
	
	/**
	 * compares the passed elements based on the current <code>SortingDirection
	 * </code>
	 * @param item1	first item
	 * @param item2 second item
	 * @return a negative number if the first item is before the 
     *  second element; the value <code>0</code> if the first element is
     *  the same order as the second element; and a positive number if the first
     *  element is after the second element
     */
	protected int compareString(String item1, String item2) {

		int result = 0;
		if (SortingDirection.ASCENDING_LITERAL.equals(getSortingDirection())) {
			result = collator.compare(item1, item2);
		} else {
			result = collator.compare(item2, item1);
		}

		return result;
	}

	/**
	 * compares the passed elements based on the current <code>SortingDirection
	 * </code>
	 * @param item1	first item
	 * @param item2 second item
	 * @return a negative number if the first item is before the 
     *  second element; the value <code>0</code> if the first element is
     *  the same order as the second element; and a positive number if the first
     *  element is after the second element
     */	
	protected int compareVisible(String item1, String item2) {
		int result = 0;
		if (SortingDirection.ASCENDING_LITERAL.equals(getSortingDirection())) {
			result = collator.compare(item1, item2);
		} else {
			result = collator.compare(item2, item1);
		}

		return result;
	}	
}
