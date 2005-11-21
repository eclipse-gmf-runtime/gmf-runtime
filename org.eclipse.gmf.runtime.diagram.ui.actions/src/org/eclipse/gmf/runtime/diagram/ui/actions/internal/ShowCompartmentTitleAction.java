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

import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.BooleanPropertyAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;


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
