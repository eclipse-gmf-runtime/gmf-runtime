/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TopGraphicEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import com.ibm.xtools.notation.View;

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
		super(workbenchPage, Properties.ID_ISVISIBLE, Messages.getString("ConstrainedFlowLayoutEditPolicy.changeVisibilityCommand.label")); //$NON-NLS-1$);
		this.visibility = visibility ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeAction#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		return visibility;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#getTargetEdiParts(org.eclipse.gef.EditPart)
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
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#getCommandLabel()
	 */
	protected String getCommandLabel() {
		if (((Boolean) getNewPropertyValue()).booleanValue())
			return Messages.getString("ShowAllResizableCompartmentsAction.ShowAllText"); //$NON-NLS-1$
		else
			return Messages.getString("ShowAllResizableCompartmentsAction.HideAllText"); //$NON-NLS-1$ 
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
		action.setText(Messages.getString("ShowAllResizableCompartmentsAction.ShowAllText")); //$NON-NLS-1$
		action.setToolTipText(Messages.getString("ShowAllResizableCompartmentsAction.ShowAllTooltip")); //$NON-NLS-1$
		action.setImageDescriptor(
			Images.DESC_ACTION_SHOW_ALL_RESIZABLE_COMPARTMENTS);
		action.setHoverImageDescriptor(
			Images.DESC_ACTION_SHOW_ALL_RESIZABLE_COMPARTMENTS);
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
		action.setText(Messages.getString("ShowAllResizableCompartmentsAction.HideAllText")); //$NON-NLS-1$
		action.setToolTipText(Messages.getString("ShowAllResizableCompartmentsAction.HideAllTooltip")); //$NON-NLS-1$
		action.setImageDescriptor(
			Images.DESC_ACTION_HIDE_ALL_RESIZABLE_COMPARTMENTS);
		action.setHoverImageDescriptor(
			Images.DESC_ACTION_HIDE_ALL_RESIZABLE_COMPARTMENTS);
		return action;
	}

}
