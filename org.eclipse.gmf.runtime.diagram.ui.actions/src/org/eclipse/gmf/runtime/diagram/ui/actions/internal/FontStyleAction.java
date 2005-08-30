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
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;

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
		FontStyleAction action = new FontStyleAction(workbenchPage, Properties.ID_FONTBOLD, Messages.getString("PropertyDescriptorFactory.FontStyle.Bold")); //$NON-NLS-1$
		action.setId(ActionIds.ACTION_FONT_BOLD);
		action.setText(DiagramActionsResourceManager.getInstance().getString("FontStyleAction.bold.text")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getInstance().getString("FontStyleAction.bold.tooltip")); //$NON-NLS-1$
		action.setImageDescriptor(Images.DESC_ACTION_BOLD); //$NON-NLS-1$
		action.setDisabledImageDescriptor(Images.DESC_ACTION_BOLD_DISABLED); //$NON-NLS-1$
		return action;
	}

	/**
	 * Creates a font style action that changes the font's ITALIC property
	 * 
	 * @param workbenchPage The workbench page
	 * @return An instance of font style action
	 */
	public static FontStyleAction createItalicFontStyleAction(IWorkbenchPage workbenchPage) {
		FontStyleAction action = new FontStyleAction(workbenchPage, Properties.ID_FONTITALIC, Messages.getString("PropertyDescriptorFactory.FontStyle.Italic")); //$NON-NLS-1$
		action.setId(ActionIds.ACTION_FONT_ITALIC);
		action.setText(DiagramActionsResourceManager.getInstance().getString("FontStyleAction.italic.text")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getInstance().getString("FontStyleAction.italic.tooltip")); //$NON-NLS-1$
		action.setImageDescriptor(Images.DESC_ACTION_ITALIC); //$NON-NLS-1$
		action.setDisabledImageDescriptor(Images.DESC_ACTION_ITALIC_DISABLED); //$NON-NLS-1$
		return action;
	}

}
