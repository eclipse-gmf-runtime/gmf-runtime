/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.BooleanPropertyAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.ui.IWorkbenchPage;


/**
 * An action to show/hide compartment title
 * 
 * @author melaasar
 */
public class ShowCompartmentTitleAction
	extends BooleanPropertyAction {

	/**
	 * @param workbenchPage
	 * @param propertyId
	 * @param propertyName
	 */
	public ShowCompartmentTitleAction(IWorkbenchPage workbenchPage) {
		super(
			workbenchPage,
			Properties.ID_SHOWCOMPARTMENTTITLE,
			DiagramUIActionsMessages.PropertyDescriptorFactory_ShowCompartmentTitle);
		setId(ActionIds.ACTION_SHOW_COMPARTMENT_TITLE);
		setText(DiagramUIActionsMessages.ShowCompartmentTitle_ActionLabelText);
		setToolTipText(DiagramUIActionsMessages.ShowCompartmentTitle_ActionToolTipText);
	}

}
