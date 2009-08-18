/******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.CheckedPropertyAction;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.RoutingStyle;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Toggles routing style for selected connections
 * 
 * @canBeSeenBy %level1
 */
public class ToggleRouterAction extends CheckedPropertyAction {


	/**
	 * @param workbenchPage
	 * @param routerType
	 */
	public ToggleRouterAction(IWorkbenchPage workbenchPage) {
		super(
			workbenchPage,
			Properties.ID_ROUTING,
			DiagramUIMessages.ChangeRouterAction_ChangePropertyValueRequest_label,
			Routing.MANUAL_LITERAL);
		setId(ActionIds.ACTION_TOGGLE_ROUTER);
	}

	private boolean testTree(Object currentRoutingType) {
		if (currentRoutingType.equals(Routing.TREE_LITERAL)) {
			List selected = getSelectedObjects();
			if (selected.size() < 2) 
				return false;
			
			ListIterator li = selected.listIterator();
			while (li.hasNext()) {
				if (!(li.next() instanceof ITreeBranchEditPart))
					return false;
			}		
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.CheckedPropertyAction#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		
		// Method retrieves the current routing style and returns the next possible choice
		if (!getOperationSet().isEmpty()) {
			Object element = getOperationSet().get(0);
					
			if (element instanceof ConnectionEditPart) {
				ConnectionEditPart primaryConnection = (ConnectionEditPart) element;
				
				RoutingStyle style = (RoutingStyle) (primaryConnection.getNotationView())
				.getStyle(NotationPackage.eINSTANCE.getRoutingStyle());
				
				if (style != null) {
					Routing currentRouting = style.getRouting();
					if (currentRouting != null) {
						for (Iterator iter = Routing.VALUES.iterator(); iter.hasNext();) {
							if (iter.next().equals(currentRouting) && iter.hasNext()) {						
								Object currentRoutingType = iter.next();
								if (testTree(currentRoutingType)) {
									return currentRoutingType;
								}
							}
						}
					}
				}
			}
		}
		return Routing.VALUES.get(0);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createOperationSet()
	 */
	protected List createOperationSet() {
		List operationSet = super.createOperationSet();
		List connectionSet = new ArrayList();
		ListIterator iter = operationSet.listIterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof ConnectionEditPart) {
				connectionSet.add(obj);
			}
		}
		
		return connectionSet;
	}

}
