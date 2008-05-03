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
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.PropertyChangeAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.notation.LineType;
import org.eclipse.gmf.runtime.notation.LineTypeStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IWorkbenchPage;

/**
 * The action for line type.
 * 
 * @author Anthony Hunter
 */
public class LineTypeAction extends PropertyChangeAction {

	/**
	 * The line type property value.
	 */
	private LineType lineType;

	/**
	 * Constructor for a LineTypeAction.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @param textAlignment
	 *            the text alignment type.
	 */
	protected LineTypeAction(IWorkbenchPage workbenchPage, LineType lineType) {
		super(
				workbenchPage,
				Properties.ID_LINE_TYPE,
				DiagramUIActionsMessages.LineTypeAction_ChangePropertyValueRequest_label);
		this.lineType = lineType;
	}

	/**
	 * Creates the line type solid action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the line type solid action.
	 */
	public static LineTypeAction createLineTypeSolidAction(
			IWorkbenchPage workbenchPage) {
		LineTypeAction action = new LineTypeAction(workbenchPage,
				LineType.SOLID_LITERAL);
		action.setId(ActionIds.ACTION_LINE_TYPE_SOLID);
		action.setText(DiagramUIActionsMessages.LineTypeAction_solid);
		action
				.setToolTipText(DiagramUIActionsMessages.LineTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_SOLID);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_SOLID_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_SOLID);
		return action;
	}

	/**
	 * Creates the line type dash action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the line type dash action.
	 */
	public static LineTypeAction createLineTypeDashAction(
			IWorkbenchPage workbenchPage) {
		LineTypeAction action = new LineTypeAction(workbenchPage,
				LineType.DASH_LITERAL);
		action.setId(ActionIds.ACTION_LINE_TYPE_DASH);
		action.setText(DiagramUIActionsMessages.LineTypeAction_dash);
		action
				.setToolTipText(DiagramUIActionsMessages.LineTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DASH);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DASH_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DASH);
		return action;
	}

	/**
	 * Creates the line type dot action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the line type dot action.
	 */
	public static LineTypeAction createLineTypeDotAction(
			IWorkbenchPage workbenchPage) {
		LineTypeAction action = new LineTypeAction(workbenchPage,
				LineType.DOT_LITERAL);
		action.setId(ActionIds.ACTION_LINE_TYPE_DOT);
		action.setText(DiagramUIActionsMessages.LineTypeAction_dot);
		action
				.setToolTipText(DiagramUIActionsMessages.LineTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DOT);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DOT_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DOT);
		return action;
	}

	/**
	 * Creates the line type dash dot action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the line type dash dot action.
	 */
	public static LineTypeAction createLineTypeDashDotAction(
			IWorkbenchPage workbenchPage) {
		LineTypeAction action = new LineTypeAction(workbenchPage,
				LineType.DASH_DOT_LITERAL);
		action.setId(ActionIds.ACTION_LINE_TYPE_DASH_DOT);
		action.setText(DiagramUIActionsMessages.LineTypeAction_dashdot);
		action
				.setToolTipText(DiagramUIActionsMessages.LineTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DASH_DOT);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DASH_DOT_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DASH_DOT);
		return action;
	}

	/**
	 * Creates the line type dash dot dot action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the line type dash dot dot action.
	 */
	public static LineTypeAction createLineTypeDashDotDotAction(
			IWorkbenchPage workbenchPage) {
		LineTypeAction action = new LineTypeAction(workbenchPage,
				LineType.DASH_DOT_DOT_LITERAL);
		action.setId(ActionIds.ACTION_LINE_TYPE_DASH_DOT_DOT);
		action.setText(DiagramUIActionsMessages.LineTypeAction_dashdotdot);
		action
				.setToolTipText(DiagramUIActionsMessages.LineTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DASH_DOT_DOT);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DASH_DOT_DOT_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_TYPE_DASH_DOT_DOT);
		return action;
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (getId() == ActionIds.ACTION_LINE_TYPE_SOLID
				|| getId() == ActionIds.ACTION_LINE_TYPE_DASH
				|| getId() == ActionIds.ACTION_LINE_TYPE_DASH_DOT
				|| getId() == ActionIds.ACTION_LINE_TYPE_DASH_DOT_DOT
				|| getId() == ActionIds.ACTION_LINE_TYPE_DOT) {
			for (ListIterator<?> li = getSelectedObjects().listIterator(); li
					.hasNext();) {
				Object object = li.next();
				if (!(object instanceof ConnectionEditPart)
						&& !(object instanceof ShapeNodeEditPart)) {
					return false;
				}
				View view = ((IGraphicalEditPart) object).getPrimaryView();
				if (view != null) {
					LineTypeStyle style = (LineTypeStyle) view
							.getStyle(NotationPackage.eINSTANCE
									.getLineTypeStyle());
					if (style == null) {
						return false;
					}
				}
			}
		}

		return super.calculateEnabled();
	}

	/* 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeAction#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		return lineType;
	}

}
