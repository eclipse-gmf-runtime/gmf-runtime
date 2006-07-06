/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.ListComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.ModifySortFilterEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.NestedResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.Filtering;
import org.eclipse.gmf.runtime.notation.FilteringStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Sorting;
import org.eclipse.gmf.runtime.notation.SortingDirection;
import org.eclipse.gmf.runtime.notation.SortingStyle;
import org.eclipse.gmf.runtime.notation.View;

/**
 * An editpart implementation of the ResizableCompartment as a list compartment
 * 
 * @author melaasar
 */
public abstract class ListCompartmentEditPart
	extends ResizableCompartmentEditPart {
	
	static long count = 0;
	
	/** list of model children that this edit part is listening */
	protected List modelChildrenListeners;
	/** State of listening */
	protected boolean listening;

	/**
	 * @param view The IResizableCompartmentView compartment view
	 */
	public ListCompartmentEditPart(EObject model) {
		super(model);
	}
	
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.MODIFY_SORT_FILTER_ROLE, new ModifySortFilterEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ListComponentEditPolicy());
	}
		

	/** 
	 * Adds a constrained flow layout algorithm to the content pane of compartment figure
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	public IFigure createFigure() {
		ResizableCompartmentFigure rcf;
		if (getParent() == getTopGraphicEditPart()){
			rcf = (ResizableCompartmentFigure) super.createFigure();
		} else {
			rcf = new NestedResizableCompartmentFigure(getMapMode());
		}
		
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
			layout.setStretchMajorAxis(false);
			layout.setStretchMinorAxis(false);
			layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT);
			rcf.getContentPane().setLayoutManager(layout);
		
		
		return rcf;
	}

	/**
	 * Returns a list of sorted / filtered model children.  To get the model children
	 * in their natural ordering and size use getBaseModelChildren();
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected final List getModelChildren() {
		List sortedFilteredChildren = new ArrayList(getSortedChildren());
		sortedFilteredChildren.removeAll(getFilteredChildren());
		return sortedFilteredChildren;
	}
	
	/**
	 * Returns a list of model children in their natural model state.
	 * @return list of model children of this edit part
	 */
	protected List getBaseModelChildren() {
		return super.getChildren();
	}

	/**
	 * Determines if the given event affects the semantic model children
	 * 
	 * @param evt The event in question
	 * @return <code>true</code> if the events affects model children, <code>false</code> otherwise
	 */
	abstract protected boolean hasModelChildrenChanged(Notification evt);

	protected void handleNotificationEvent(Notification event ) {
		// If a child has been added or removed while sorting
		// or filtering is automatic, re-register the listeners.		
		if (hasModelChildrenChanged(event) && modeAutomatic()) {
			removeSemanticChildrenListeners();
			addSemanticChildrenListeners();
			refresh();
		} 
		
		Object feature = event.getFeature();
		if (NotationPackage.eINSTANCE.getFilteringStyle_Filtering().equals(feature)
			|| NotationPackage.eINSTANCE.getFilteringStyle_FilteringKeys().equals(feature)
			|| NotationPackage.eINSTANCE.getFilteringStyle_FilteredObjects().equals(feature)
			|| NotationPackage.eINSTANCE.getSortingStyle_Sorting().equals(feature)
			|| NotationPackage.eINSTANCE.getSortingStyle_SortingKeys().equals(feature)
			|| NotationPackage.eINSTANCE.getSortingStyle_SortedObjects().equals(feature)) {
			refresh();			
			
			if (modeAutomatic() && !listening) {  // start listening...
				removeSemanticChildrenListeners();
				addSemanticChildrenListeners();
			} 
			if (!modeAutomatic() && listening) { // stop listening...
				removeSemanticChildrenListeners();
			}
		} else {
			super.handleNotificationEvent(event);
		}
		
		// refresh() if one of the children have changed a feature
		// affecting sorting / filtering.
		if (isAffectingSortingFiltering(feature) && modeAutomatic())
			refresh();
	}
	

	/**
	 * Returns a <code>List</code> of model children corresponding appearance order of the list 
	 * compartment items.
	 * @return a <code>List</code> of Ids
	 */
	protected List getSortedChildren() {
		SortingStyle style = (SortingStyle)  ((View)getModel()).getStyle(NotationPackage.eINSTANCE.getSortingStyle());

		if (style != null) {
			Sorting sorting = style.getSorting();
			if (Sorting.NONE_LITERAL == sorting) {
				
				return super.getModelChildren();
				
			} else if (Sorting.MANUAL_LITERAL == sorting) {
	
				List allChildren = super.getModelChildren();
				// Return the empty list if the model children have not yet been
				// created.
				if (allChildren.isEmpty())
					return allChildren;
				
				List sortedChildren = style.eIsSet(NotationPackage.eINSTANCE.getSortingStyle_SortedObjects())
					? style.getSortedObjects() 
					: Collections.EMPTY_LIST;
	
				List sorted = new ArrayList();
				
				// Get the corresponding views...
				Iterator i = sortedChildren.iterator();
				while(i.hasNext()) {
					EObject eObject = (EObject) i.next();
					View view = getModelChildByID(eObject);
					if (view != null)
						sorted.add(view);
				}	
				
				// Add any remaining model children to the end
				for (int j = 0; j < allChildren.size(); j++) {
					View view = (View) allChildren.get(j);
					if (!sorted.contains(view))
						sorted.add(view);
				}
				
				return sorted;
				
			} else if (Sorting.AUTOMATIC_LITERAL == sorting) {
				Map sortingKeys = style.eIsSet(NotationPackage.eINSTANCE.getSortingStyle_SortingKeys())
					? style.getSortingKeys() 
					: Collections.EMPTY_MAP;
				return getChildrenSortedBy(sortingKeys);
			}
		}
		
		return super.getModelChildren();		
	}
	
	/**
	 * Returns the model children sorted by the order specified by Properties.ID_SORTING_KEYS.
	 * This is used to support dynamic list compartment sorting.
	 * @param sortingKeys
	 * @return the model children sorted
	 */
	protected List getChildrenSortedBy(Map sortingKeys) {
		
		List allChildren = new ArrayList(super.getModelChildren());
		
		// Currently only one sorting key is supported.
		if (sortingKeys != null && !sortingKeys.isEmpty()) {
			Collection keySet = sortingKeys.keySet();
			Iterator i = keySet.iterator();
			String name = (String) i.next();
			SortingDirection direction = (SortingDirection) sortingKeys.get(name);
			
			Comparator comparator = getComparator(name, direction);
			
			if (comparator != null)
				Collections.sort(allChildren, getComparator(name, direction));									
		} 
		return allChildren;
	}
	
	/**
	 * Returns a <code>Comparator</code> that is used to sort the list compartment
	 * children.  Override to provide a <code>Comparator</code> that provides
	 * for a particular child type.
	 * @param name
	 * @param direction
	 * @return comparator
	 */
	protected Comparator getComparator(String name, SortingDirection direction) {
		return null;
	}	
	
	/**
	 * Returns a <code>List</code> of <code>View</code> s corresponding
	 * to the unique id of the semantic elements which are filtered.
	 * 
	 * @return list of filtered model children
	 */
	protected List getFilteredChildren() {
		Object model = getModel();
		if (model instanceof View){
			View view = (View)model;
			FilteringStyle style = (FilteringStyle)  view.getStyle(NotationPackage.eINSTANCE.getFilteringStyle());
			if (style != null) {
				Filtering filtering = style.getFiltering();
				if (Filtering.NONE_LITERAL == filtering) {
					
					return Collections.EMPTY_LIST;
					
				} else if (Filtering.MANUAL_LITERAL == filtering) {
					
					List filteredChildren = style.eIsSet(NotationPackage.eINSTANCE.getFilteringStyle_FilteredObjects())
						? style.getFilteredObjects() 
						: Collections.EMPTY_LIST;
		
					List filteredViews = new ArrayList();
					// Get the corresponding views...
					Iterator i = filteredChildren.iterator();
					while(i.hasNext()) {
						EObject eObject = (EObject) i.next();
                        View modelChild = getModelChildByID(eObject);
                        if (modelChild != null) {
                            filteredViews.add(modelChild);
                        }
					}
					return filteredViews;	
				
				} else if (Filtering.AUTOMATIC_LITERAL == filtering) {
					List filteringKeys = style.eIsSet(NotationPackage.eINSTANCE.getFilteringStyle_FilteringKeys())
						? style.getFilteringKeys() 
						: Collections.EMPTY_LIST;
					return getChildrenFilteredBy(filteringKeys);
				}
			}
		}
		
		return Collections.EMPTY_LIST;
	}
	
	/**
	 * Returns a list of model children that are filtered according to 
	 * Properties.ID_FILTERING_KEYS.  This is used to support dynamic sorting.
	 * @param filterKeys
	 * @return list of filtered children
	 */
	protected List getChildrenFilteredBy(List filterKeys) {
		return Collections.EMPTY_LIST;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ResizableCompartmentEditPart#getCompartmentName()
	 */
	public String getCompartmentName() {
		return null;
	}
	
	/**
	 * Returns the child view given the semantic ID.
	 * @param eObject the semantic element
	 * @return the view or null if not found
	 */
	protected View getModelChildByID(EObject eObject) {
        List modelChildren = super.getModelChildren();
        for (int i = 0; i < modelChildren.size(); i++) {
            View view = (View) modelChildren.get(i);
            EObject e = ViewUtil.resolveSemanticElement(view);
            if (eObject.equals(e))
                return view;
        }
        return null;
	}
	
	/**
	 * Returns <code>true</code> if sorting or filtering is in automatic mode
	 * and <code>false</code> otherwise.
	 * @return <code>true</code> if yes, oherwise <code>false</code> 
	 */
	protected boolean modeAutomatic() {
		View view = getNotationView();
		if (view!=null){
			SortingStyle sortingStyle = (SortingStyle) view.getStyle(NotationPackage.eINSTANCE.getSortingStyle());
			FilteringStyle filteringStyle = (FilteringStyle) view.getStyle(NotationPackage.eINSTANCE.getFilteringStyle());
			if (sortingStyle != null && Sorting.AUTOMATIC_LITERAL == sortingStyle.getSorting()) 
				return true;
			if (filteringStyle != null && Filtering.AUTOMATIC_LITERAL == filteringStyle.getFiltering())
				return true;
		}
		return false;
	}
	
	/**
	 * checks whether the feature affects the sorting / filtering.
	 * @param feature the feature to check
	 * @return <tt>true</tt> if it affects it otherwise <tt>false</tt>
	 */
	protected boolean isAffectingSortingFiltering(Object feature) {
		return false;
	}

	/**
	 * Add listeners to the children of this list compartment so changes
	 * to the children can be pickup and the sorting / filtering may be
	 * updated.
	 */
	protected void addSemanticChildrenListeners() {
		modelChildrenListeners = super.getModelChildren();
		for (int i = 0; i < modelChildrenListeners.size(); i++) {
			EObject eObject = ViewUtil.resolveSemanticElement((View)modelChildrenListeners.get(i));
			if (eObject != null)
				addListenerFilter(
					"SemanticModel" + i, this, eObject); //$NON-NLS-1$
		}	
		listening = true;
	}
	
	/**
	 * Remove the listeners on the children of this list compartment.
	 */
	protected void removeSemanticChildrenListeners() {
		if (modelChildrenListeners != null) {
			for (int i = 0; i < modelChildrenListeners.size(); i++)
				removeListenerFilter("SemanticModel" + i); //$NON-NLS-1$
			modelChildrenListeners.clear();
		}
		listening = false;
	}
	
	/**
	 * Add semanticChildrenListeners when we activiate, if needed.
	 */
	protected void addSemanticListeners() {
		if (modeAutomatic() && !listening) {
			addSemanticChildrenListeners();
		}
		super.addSemanticListeners();
	}
	
	/**
	 * Remove semanticChildrenListeners when we stop listening.
	 */
	protected void removeSemanticListeners() {
		removeSemanticChildrenListeners();
		modelChildrenListeners = null;
		listening = false;
		super.removeSemanticListeners();
	}
}
