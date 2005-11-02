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

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TopGraphicEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AllCompartmentsAction extends PropertyChangeAction {
	/**
	 * The requested visibility of all compartments
	 */
	private Boolean visibility;

	/**
	 * @param workbenchPage
	 * @param visibility
	 */
	protected AllCompartmentsAction(
		IWorkbenchPage workbenchPage,
		boolean visibility) {
		super(workbenchPage, Properties.ID_ISVISIBLE, DiagramActionsResourceManager.getI18NString("ConstrainedFlowLayoutEditPolicy.changeVisibilityCommand.label")); //$NON-NLS-1$);
		this.visibility = visibility ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeAction#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		return visibility;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getTargetEdiParts(org.eclipse.gef.EditPart)
	 */
	protected List getTargetEdiParts(EditPart editpart) {
		List targetEPs = null;
		if (editpart instanceof TopGraphicEditPart) {
			final TopGraphicEditPart topEP = (TopGraphicEditPart) editpart;
			targetEPs = (List) MEditingDomainGetter.getMEditingDomain((View)editpart.getModel()).runAsRead(new MRunnable() {
				public Object run() {
					return topEP.getResizableCompartments();
				}
			});
		}
		return (targetEPs == null || targetEPs.isEmpty())
			? Collections.EMPTY_LIST
			: targetEPs;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getCommandLabel()
	 */
	protected String getCommandLabel() {
		if (((Boolean) getNewPropertyValue()).booleanValue())
			return DiagramActionsResourceManager.getI18NString("ShowAllResizableCompartmentsAction.ShowAllText"); //$NON-NLS-1$
		else
			return DiagramActionsResourceManager.getI18NString("ShowAllResizableCompartmentsAction.HideAllText"); //$NON-NLS-1$ 
	}

	/**
	 * Creates the show all compartments action
	 * @param workbenchPage
	 * @return
	 */
	public static AllCompartmentsAction createShowAllCompartmentsAction(IWorkbenchPage workbenchPage) {
		AllCompartmentsAction action =
			new AllCompartmentsAction(workbenchPage, true);
		action.setId(ActionIds.ACTION_COMPARTMENT_ALL);
		action.setText(DiagramActionsResourceManager.getI18NString("ShowAllResizableCompartmentsAction.ShowAllText")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("ShowAllResizableCompartmentsAction.ShowAllTooltip")); //$NON-NLS-1$

		ImageDescriptor imageDesc = DiagramActionsResourceManager
			.getInstance()
			.getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_SHOW_ALL_RESIZABLE_COMPARTMENTS);
		action.setImageDescriptor(imageDesc);
		action.setHoverImageDescriptor(imageDesc);
		return action;
	}

	/**
	 * Creates the hide all compartments action
	 * @param workbenchPage
	 * @return
	 */
	public static AllCompartmentsAction createHideAllCompartmentsAction(IWorkbenchPage workbenchPage) {
		AllCompartmentsAction action =
			new AllCompartmentsAction(workbenchPage, false);
		action.setId(ActionIds.ACTION_COMPARTMENT_NONE);
		action.setText(DiagramActionsResourceManager.getI18NString("ShowAllResizableCompartmentsAction.HideAllText")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("ShowAllResizableCompartmentsAction.HideAllTooltip")); //$NON-NLS-1$

		ImageDescriptor imageDesc = DiagramActionsResourceManager
			.getInstance()
			.getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_HIDE_ALL_RESIZABLE_COMPARTMENTS);
		action.setImageDescriptor(imageDesc);
		action.setHoverImageDescriptor(imageDesc);
		return action;
	}

}
