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

import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.BooleanPropertyAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramUIPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 * 
 * An action to change one of the font style properties: BOLD or ITALIC
 */
public class FontStyleAction extends BooleanPropertyAction {

	/**
	 * Creates a new FontStyleAction instance
	 * 
	 * @param workbenchPage The part Service
	 * @param propertyId The property Id
	 * @param propertyName The property Name
	 */
	private FontStyleAction(
		IWorkbenchPage workbenchPage,
		String propertyId,
		String propertyName) {
		super(workbenchPage, propertyId, propertyName);
	}

	/**
	 * Creates a font style action that changes the font's BOLD property
	 * 
	 * @param workbenchPage The workbench page
	 * @return An instance of font style action
	 */
	public static FontStyleAction createBoldFontStyleAction(IWorkbenchPage workbenchPage) {
		FontStyleAction action = new FontStyleAction(workbenchPage, Properties.ID_FONTBOLD, DiagramUIActionsMessages.PropertyDescriptorFactory_FontStyle_Bold);
		action.setId(ActionIds.ACTION_FONT_BOLD);
		action.setText(DiagramUIActionsMessages.FontStyleAction_bold_text);
		action
			.setToolTipText(DiagramUIActionsMessages.FontStyleAction_bold_tooltip);
		action.setImageDescriptor(DiagramUIActionsPluginImages.DESC_BOLD);
		action.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_BOLD_DISABLED);
		return action;
	}

	/**
	 * Creates a font style action that changes the font's ITALIC property
	 * 
	 * @param workbenchPage The workbench page
	 * @return An instance of font style action
	 */
	public static FontStyleAction createItalicFontStyleAction(IWorkbenchPage workbenchPage) {
		FontStyleAction action = new FontStyleAction(workbenchPage,
			Properties.ID_FONTITALIC,
			DiagramUIActionsMessages.PropertyDescriptorFactory_FontStyle_Italic);
		action.setId(ActionIds.ACTION_FONT_ITALIC);
		action.setText(DiagramUIActionsMessages.FontStyleAction_italic_text);
		action
			.setToolTipText(DiagramUIActionsMessages.FontStyleAction_italic_tooltip);
		action.setImageDescriptor(DiagramUIPluginImages.DESC_ITALIC);
		action.setDisabledImageDescriptor(DiagramUIPluginImages.DESC_ITALIC_DISABLED);
		return action;
	}

}
