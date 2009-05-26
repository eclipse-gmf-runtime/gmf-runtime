/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.ListIterator;

import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.PropertyChangeAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.TextAlignment;
import org.eclipse.gmf.runtime.notation.TextStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IWorkbenchPage;

/**
 * The action for text alignment.
 * 
 * @author Anthony Hunter
 */
public class TextAlignmentAction extends PropertyChangeAction {

	/**
	 * The text alignment property value.
	 */
	private TextAlignment textAlignment;

	/**
	 * Constructor for a TextAlignmentAction.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @param textAlignment
	 *            the text alignment type.
	 */
	protected TextAlignmentAction(IWorkbenchPage workbenchPage,
			TextAlignment textAlignment) {
		super(
				workbenchPage,
				Properties.ID_TEXT_ALIGNMENT,
				DiagramUIActionsMessages.TextAlignmentAction_ChangePropertyValueRequest_label);
		this.textAlignment = textAlignment;
	}

	/**
	 * Creates the left text alignment action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the left text alignment action.
	 */
	public static TextAlignmentAction createTextAlignmentLeftAction(
			IWorkbenchPage workbenchPage) {
		TextAlignmentAction action = new TextAlignmentAction(workbenchPage,
				TextAlignment.LEFT_LITERAL);
		action.setId(ActionIds.ACTION_TEXT_ALIGNMENT_LEFT);
		action.setText(DiagramUIActionsMessages.TextAlignmentAction_left);
		action
				.setToolTipText(DiagramUIActionsMessages.TextAlignmentAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_TEXT_ALIGNMENT_LEFT);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_TEXT_ALIGNMENT_LEFT_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_TEXT_ALIGNMENT_LEFT);
		return action;
	}

	/**
	 * Creates the center text alignment action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the center text alignment action.
	 */
	public static TextAlignmentAction createTextAlignmentCenterAction(
			IWorkbenchPage workbenchPage) {
		TextAlignmentAction action = new TextAlignmentAction(workbenchPage,
				TextAlignment.CENTER_LITERAL);
		action.setId(ActionIds.ACTION_TEXT_ALIGNMENT_CENTER);
		action.setText(DiagramUIActionsMessages.TextAlignmentAction_center);
		action
				.setToolTipText(DiagramUIActionsMessages.TextAlignmentAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_TEXT_ALIGNMENT_CENTER);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_TEXT_ALIGNMENT_CENTER_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_TEXT_ALIGNMENT_CENTER);
		return action;
	}

	/**
	 * Creates the right text alignment action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the right text alignment action.
	 */
	public static TextAlignmentAction createTextAlignmentRightAction(
			IWorkbenchPage workbenchPage) {
		TextAlignmentAction action = new TextAlignmentAction(workbenchPage,
				TextAlignment.RIGHT_LITERAL);
		action.setId(ActionIds.ACTION_TEXT_ALIGNMENT_RIGHT);
		action.setText(DiagramUIActionsMessages.TextAlignmentAction_right);
		action
				.setToolTipText(DiagramUIActionsMessages.TextAlignmentAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_TEXT_ALIGNMENT_RIGHT);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_TEXT_ALIGNMENT_RIGHT_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_TEXT_ALIGNMENT_RIGHT);
		return action;
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (getId() == ActionIds.ACTION_TEXT_ALIGNMENT_LEFT
				|| getId() == ActionIds.ACTION_TEXT_ALIGNMENT_CENTER
				|| getId() == ActionIds.ACTION_TEXT_ALIGNMENT_RIGHT) {
			for (ListIterator<?> li = getSelectedObjects().listIterator(); li
					.hasNext();) {
				Object object = li.next();
				if (!(object instanceof IGraphicalEditPart)) {
					return false;
				}
				IGraphicalEditPart node = (IGraphicalEditPart) object;
				if (node.getModel() != null) {
					View view = (View) node.getModel();
					TextStyle style = (TextStyle) view
							.getStyle(NotationPackage.Literals.TEXT_STYLE);
					if (style == null) {
						return false;
					}
				}
			}
		}

		return super.calculateEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeAction#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		return textAlignment;
	}

}
