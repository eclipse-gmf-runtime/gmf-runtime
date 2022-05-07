/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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

import java.util.ListIterator;

import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.PropertyChangeAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.notation.LineStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IWorkbenchPage;

/**
 * The action for line width.
 * 
 * @author Anthony Hunter
 */
public class LineWidthAction extends PropertyChangeAction {

	/**
	 * The line width property value.
	 */
	private int lineWidth;

	/**
	 * Constructor for a LineWidthAction.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @param lineWidth
	 *            the line width.
	 */
	protected LineWidthAction(IWorkbenchPage workbenchPage, int lineWidth) {
		super(
				workbenchPage,
				Properties.ID_LINE_WIDTH,
				DiagramUIActionsMessages.LineWidthAction_ChangePropertyValueRequest_label);
		this.lineWidth = lineWidth;
	}

	/**
	 * Creates the line width of one point action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the line width of one point action.
	 */
	public static LineWidthAction createLineWidthOneAction(
			IWorkbenchPage workbenchPage) {
		LineWidthAction action = new LineWidthAction(workbenchPage, 1);
		action.setId(ActionIds.ACTION_LINE_WIDTH_ONE);
		action.setText(DiagramUIActionsMessages.LineWidthAction_one);
		action
				.setToolTipText(DiagramUIActionsMessages.LineWidthAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_ONE);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_ONE_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_ONE);
		return action;
	}

	/**
	 * Creates the line width of two points action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the line width of two points action.
	 */
	public static LineWidthAction createLineWidthTwoAction(
			IWorkbenchPage workbenchPage) {
		LineWidthAction action = new LineWidthAction(workbenchPage, 2);
		action.setId(ActionIds.ACTION_LINE_WIDTH_TWO);
		action.setText(DiagramUIActionsMessages.LineWidthAction_two);
		action
				.setToolTipText(DiagramUIActionsMessages.LineWidthAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_TWO);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_TWO_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_TWO);
		return action;
	}

	/**
	 * Creates the line width of three points action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the line width of three points action.
	 */
	public static LineWidthAction createLineWidthThreeAction(
			IWorkbenchPage workbenchPage) {
		LineWidthAction action = new LineWidthAction(workbenchPage, 3);
		action.setId(ActionIds.ACTION_LINE_WIDTH_THREE);
		action.setText(DiagramUIActionsMessages.LineWidthAction_three);
		action
				.setToolTipText(DiagramUIActionsMessages.LineWidthAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_THREE);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_THREE_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_THREE);
		return action;
	}

	/**
	 * Creates the line width of four points action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the line width of four points action.
	 */
	public static LineWidthAction createLineWidthFourAction(
			IWorkbenchPage workbenchPage) {
		LineWidthAction action = new LineWidthAction(workbenchPage, 4);
		action.setId(ActionIds.ACTION_LINE_WIDTH_FOUR);
		action.setText(DiagramUIActionsMessages.LineWidthAction_four);
		action
				.setToolTipText(DiagramUIActionsMessages.LineWidthAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_FOUR);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_FOUR_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_FOUR);
		return action;
	}

	/**
	 * Creates the line width of five points action.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the line width of five points action.
	 */
	public static LineWidthAction createLineWidthFiveAction(
			IWorkbenchPage workbenchPage) {
		LineWidthAction action = new LineWidthAction(workbenchPage, 5);
		action.setId(ActionIds.ACTION_LINE_WIDTH_FIVE);
		action.setText(DiagramUIActionsMessages.LineWidthAction_five);
		action
				.setToolTipText(DiagramUIActionsMessages.LineWidthAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_FIVE);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_FIVE_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_LINE_WIDTH_FIVE);
		return action;
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (getId() == ActionIds.ACTION_LINE_WIDTH_ONE
				|| getId() == ActionIds.ACTION_LINE_WIDTH_TWO
				|| getId() == ActionIds.ACTION_LINE_WIDTH_THREE
				|| getId() == ActionIds.ACTION_LINE_WIDTH_FOUR
				|| getId() == ActionIds.ACTION_LINE_WIDTH_FIVE) {
			for (ListIterator<?> li = getSelectedObjects().listIterator(); li
					.hasNext();) {
				Object object = li.next();
				if (!(object instanceof ConnectionEditPart)
						&& !(object instanceof ShapeNodeEditPart)) {
					return false;
				}
				View view = ((IGraphicalEditPart) object).getPrimaryView();
				if (view != null) {
					LineStyle style = (LineStyle) view
							.getStyle(NotationPackage.eINSTANCE.getLineStyle());
					if (style == null || style.getLineWidth() == -1) {
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
		return lineWidth;
	}

}
