/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter.SortFilterPage;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter.SortFilterDialogUtil;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.SortFilterCompartmentItemsRequest;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.SortFilterContentRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import com.ibm.xtools.notation.View;

/**
 * EditPolicy which provides sorting/filtering for ListCompartmentItems.
 * 
 * @author jcorchis
 */
public class SortFilterCompartmentItemsEditPolicy
	extends AbstractEditPolicy {

	/**
	 * Returns <code>true</code> if the request is a REQ_SORT_FILTER_COMPARTMENT type and the view is resolvable.
	 * @return <code>true</code> if the request is a REQ_SORT_FILTER_COMPARTMENT type and the view is resolvable.
	 * and <code>false</code> otherwise.
	 */
	public final boolean understandsRequest(Request request) {
		IGraphicalEditPart editPart = (IGraphicalEditPart) getHost();
		View view = editPart.getNotationView();
		if (RequestConstants
			.REQ_SORT_FILTER_COMPARTMENT
			.equals(request.getType())
			&& view !=null 
			&& ViewUtil.resolveSemanticElement(view)!= null) {
			return true;
		}
		return false;
	}

	/**
	 * Opens the sort/filter dialog if there is at least on contribution
	 * from a child.
	 * @return command the <code>SortFilterCommand</code>
	 */
	public Command getCommand(Request request) {
		if (understandsRequest(request)) {
				List childContributions = new ArrayList();

				GraphicalEditPart ep = (GraphicalEditPart) getHost();
				List children = ep.getChildren();
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i) instanceof ListCompartmentEditPart) {
						SortFilterContentRequest contentRequest =
							new SortFilterContentRequest(childContributions);
						ListCompartmentEditPart editPart =
							(ListCompartmentEditPart) children.get(i);
						editPart.getCommand(contentRequest);
					}
				}

				List selectedEditParts = ((SortFilterCompartmentItemsRequest) request)
					.getEditParts();
				if (selectedEditParts.size() == 1 && childContributions.size() > 0 ) {
					// Open the sort/filter dialog
					SortFilterDialogUtil.invokeDialog(
						(GraphicalEditPart) getHost(), getRootPage(),

						childContributions);
				} else if (selectedEditParts.size() > 1) {
					// Open the filter dialog if this host is the primary
					// selection and the selection size is greater than 1.
					
					// Set the filter map as the first non-empty map from the selection.
					if (Collections.EMPTY_MAP.equals(getFilterMap()) || getFilterMap() == null) {
						Map filterMap = null;
						Iterator i = selectedEditParts.iterator();
						while(i.hasNext()) {
							IGraphicalEditPart part = (IGraphicalEditPart) i.next();
							EditPolicy policy = part.getEditPolicy(EditPolicyRoles.SORT_FILTER_ROLE);
							if (policy instanceof SortFilterCompartmentItemsEditPolicy) {
								filterMap = ((SortFilterCompartmentItemsEditPolicy)policy).getFilterMap();
								if (!Collections.EMPTY_MAP.equals(filterMap) &&  filterMap != null)
									break;
							}
						}
						if (!Collections.EMPTY_MAP.equals(filterMap) && filterMap != null) 
							SortFilterDialogUtil.invokeFilterDialog(selectedEditParts,
								filterMap);
					} else  {
						SortFilterDialogUtil.invokeFilterDialog(selectedEditParts,
							getFilterMap());
					}
				}
			}
			return null;
	}
	
	public EditPart getTargetEditPart(Request request) {
		return understandsRequest(request) ? getHost() : null;
	}

	/**
	 * Returns the root <code>SortFilterPage</code> which filters using
	 * the <code>UMLModifiers</code> as the filtering criteria. Default implementation
	 * returns null.  Override to add the root page.
	 * @return the root <code>SortFilterPage</code>
	 */
	public SortFilterPage getRootPage() {
		return null;
	}
	
	/**
	 * Override to provide the filter Map. The default is any empty map
	 * @return Map
	 */
	public Map getFilterMap() {		
		return Collections.EMPTY_MAP;
	}	
}
