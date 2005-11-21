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

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.CheckedPropertyAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Connection router action
 * 
 * @author melaasar
 * @canBeSeenBy %level1
 */
public class RouterAction extends CheckedPropertyAction {

	/**
	 * @param workbenchPage
	 * @param routerType
	 */
	protected RouterAction(IWorkbenchPage workbenchPage, Routing routerType) {
		super(workbenchPage, Properties.ID_ROUTING, DiagramActionsResourceManager.getI18NString("ChangeRouterAction.ChangePropertyValueRequest.label"), routerType); //$NON-NLS-1$
	}

	/**
	 * Creates the rectilinear router action
	 * 
	 * @param workbenchPage
	 * @return
	 */
	public static RouterAction createRectilinearRouterAction(IWorkbenchPage workbenchPage) {
		RouterAction action =
			new RouterAction(workbenchPage, Routing.RECTILINEAR_LITERAL);
		action.setId(ActionIds.ACTION_ROUTER_RECTILINEAR);
		action.setText(DiagramActionsResourceManager.getI18NString("ChangeRouterAction.Rectilinear.ActionLabelText")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("ChangeRouterAction.Rectilinear.ActionToolTipText")); //$NON-NLS-1$
		
		ImageDescriptor enabledImage = DiagramActionsResourceManager
			.getInstance()
			.getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_CHANGEROUTERACTION_RECTILINEAR);
		action.setImageDescriptor(enabledImage);
		action
			.setDisabledImageDescriptor(DiagramActionsResourceManager
				.getInstance()
				.getImageDescriptor(
					DiagramActionsResourceManager.IMAGE_CHANGEROUTERACTION_RECTILINEAR_DISABLED));
		action.setHoverImageDescriptor(enabledImage);
		return action;
	}

	/**
	 * Creates the rectilinear router action
	 * 
	 * @param workbenchPage
	 * @return
	 */
	public static RouterAction createObliqueRouterAction(IWorkbenchPage workbenchPage) {
		RouterAction action =
			new RouterAction(workbenchPage, Routing.MANUAL_LITERAL);
		action.setId(ActionIds.ACTION_ROUTER_OBLIQUE);
		action.setText(DiagramActionsResourceManager.getI18NString("ChangeRouterAction.Oblique.ActionLabelText")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("ChangeRouterAction.Oblique.ActionToolTipText")); //$NON-NLS-1$

		ImageDescriptor enabledImage = DiagramResourceManager.getInstance()
			.getImageDescriptor(
				DiagramResourceManager.IMAGE_CHANGEROUTERACTION_OBLIQUE);
		action.setImageDescriptor(enabledImage);
		action
			.setDisabledImageDescriptor(DiagramResourceManager
				.getInstance()
				.getImageDescriptor(
					DiagramResourceManager.IMAGE_CHANGEROUTERACTION_OBLIQUE_DISABLED));
		action.setHoverImageDescriptor(enabledImage);
		return action;
	}
	
	/**
	 * Creates the tree router action
	 * 
	 * @param workbenchPage
	 * @return
	 */
	public static RouterAction createTreeRouterAction(IWorkbenchPage workbenchPage) {
		RouterAction action =
			new RouterAction(workbenchPage, Routing.TREE_LITERAL);
		action.setId(ActionIds.ACTION_ROUTER_TREE);
		action.setText(DiagramActionsResourceManager.getI18NString("ChangeRouterAction.Tree.ActionLabelText")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("ChangeRouterAction.Tree.ActionToolTipText")); //$NON-NLS-1$
		
		ImageDescriptor enabledImage = DiagramResourceManager.getInstance()
			.getImageDescriptor(
				DiagramResourceManager.IMAGE_CHANGEROUTERACTION_TREE);
		action.setImageDescriptor(enabledImage);
		action.setDisabledImageDescriptor(DiagramResourceManager.getInstance()
			.getImageDescriptor(
				DiagramResourceManager.IMAGE_CHANGEROUTERACTION_TREE_DISABLED));
		action.setHoverImageDescriptor(enabledImage);
		return action;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (getId() == ActionIds.ACTION_ROUTER_TREE) {
			List selected = getSelectedObjects();
			if (selected.size() < 2) 
				return false;
			
			ListIterator li = selected.listIterator();
			while (li.hasNext()) {
				if (!(li.next() instanceof ITreeBranchEditPart))
					return false;
			}
		}
		
		return super.calculateEnabled();
	}
}
