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

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter.SortFilterLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter.SortFilterPage;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.SortFilterContentRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * EditPolicy which contributes <code>SortFilterPage</code> content 
 * to the <code>SortFilterDialog</code>.
 * 
 * @author jcorchis
 */
public abstract class SortFilterContentEditPolicy extends AbstractEditPolicy {

	/**
	 * Returns <code>true</code> if the request is a REQ_SORT_FILTER_CONTENT type.
	 * @return <code>true</code> if the request is a REQ_SORT_FILTER_CONTENT type
	 * and <code>false</code> otherwise.
	 */
	public boolean understandsRequest(Request request) {
		return RequestConstants.REQ_SORT_FILTER_CONTENT.equals(request
			.getType());
	}

	/**
	 * Adds the <code>SortFilterPage</code> to the <code>SortFilterDialog</code> given
	 * if the provided implementation has content.
	 * @param request the <code>SortFilterContentRequest</code>
	 * @return null Does not return a <code>Command</code>
	 */
	public Command getCommand(Request request) {
		if (understandsRequest(request)) {

			// Do not contribute to the Sort/Filter dialog
			// if there is nothing to sort or filter.	
			if (getContents() == null || getContents().size() == 0)
				return null;

			SortFilterContentRequest contentRequest =
				(SortFilterContentRequest) request;
			SortFilterPage newSortFilterPage =
				new SortFilterPage(
					SortFilterPage.CHILD_PAGE,
					(GraphicalEditPart) getHost(),
					getCollectionColumns(),
					getLabelProvider());

			newSortFilterPage.setContents(getContents(), getHiddenContents());
			newSortFilterPage.setTitle(getCompartmentLabel());

			if (getFilter() != null && getFilterColumn() != null)
				newSortFilterPage.setFilter(getFilter(), getFilterColumn());

			// Get the List from the request and Add the new page
			contentRequest.getSortFilterPageList().add(newSortFilterPage);
		}
		return null;
	}

	/**
	 * Override to return the label for the compartment other than the one provided
	 * by <code>ResizableCompartmentEditPart.getTitleName()</code>.
	 * @return the compartment label
	 */
	public String getCompartmentLabel() {
		if (getHost() instanceof ListCompartmentEditPart) {
			return ((ListCompartmentEditPart) getHost()).getCompartmentName();
		}
		return null;
	}

	/**
	 * Implement to return a <code>List</code> of <code>SortFilterCollectionColumn</code>s
	 * which are used to define the <code>SortFilterDialog</code> table.
	 * @return the collection columns.
	 */
	public abstract List getCollectionColumns();

	/**
	 * Implement to return a <code>SortFilterLabelProvider</code> for
	 * for the compartment.  The <code>SortFilterLabelProvider</code> 
	 * provides the content for the SortFilterDialog table.
	 * @return <code>SortFilterLabelProvider</code>
	 */
	public abstract SortFilterLabelProvider getLabelProvider();

	/**
	 * Implement to return a <code>List</code> of <code>SortFilterElement</code>s
	 * which are used to define the <code>SortFilterDialog</code> tables content.
	 * @return the collection of <code>SortFilterElement</code>s
	 */
	public abstract List getContents();
	
	/**
	 * Override to return a list of elements that are hidden by other means. The 
	 * items in this list will not be displayed in the sort/filter dialog, and will
	 * remain in the ID_FILTERED_OBJECTS list when changes to the sort/filter
	 * are applied.
	 * 
	 * @return a list if items not to be displayed in the sort/filter dialog.
	 */
	public List getHiddenContents() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Override to return a filter this SortFilterPage.
	 * @return the filter map
	 */
	public Map getFilter() {
		return null;
	}

	/**
	 * Override to return the column to which the filter applies.
	 * @return the filter column
	 */
	public String getFilterColumn() {
		return null;
	}

}
