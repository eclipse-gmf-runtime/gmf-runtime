/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.BooleanPropertyAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;


/**
 * An action to show/hide compartment title
 * 
 * @author melaasar
 * @canBeSeenBy %level1
 */
public class ShowCompartmentTitleAction
	extends BooleanPropertyAction {

	/**
	 * @param workbenchPage
	 * @param propertyId
	 * @param propertyName
	 */
	public ShowCompartmentTitleAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage, Properties.ID_SHOWCOMPARTMENTTITLE, DiagramActionsResourceManager.getI18NString("PropertyDescriptorFactory.ShowCompartmentTitle")); //$NON-NLS-1$;
		setId(ActionIds.ACTION_SHOW_COMPARTMENT_TITLE);
		setText(DiagramActionsResourceManager.getI18NString("ShowCompartmentTitle.ActionLabelText")); //$NON-NLS-1$
		setToolTipText(DiagramActionsResourceManager.getI18NString("ShowCompartmentTitle.ActionToolTipText")); //$NON-NLS-1$
	}

}
