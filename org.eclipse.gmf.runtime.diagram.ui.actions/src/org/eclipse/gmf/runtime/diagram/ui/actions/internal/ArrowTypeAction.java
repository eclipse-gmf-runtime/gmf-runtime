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
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.PropertyChangeAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.notation.ArrowStyle;
import org.eclipse.gmf.runtime.notation.ArrowType;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IWorkbenchPage;

/**
 * The action for arrow type.
 * 
 * @author Anthony Hunter
 */
public class ArrowTypeAction extends PropertyChangeAction {

	/**
	 * The arrow type property value.
	 */
	private ArrowType arrowType;

	/**
	 * Constructor for an ArrowTypeAction.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @param arrowType
	 *            the arrow type.
	 */
	protected ArrowTypeAction(IWorkbenchPage workbenchPage,
			ArrowType arrowType, String property) {
		super(
				workbenchPage,
				property,
				DiagramUIActionsMessages.ArrowTypeAction_ChangePropertyValueRequest_label);
		this.arrowType = arrowType;
	}

	/**
	 * Creates the arrow type none action on the source end.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the arrow type none action.
	 */
	public static ArrowTypeAction createArrowTypeSourceNoneAction(
			IWorkbenchPage workbenchPage) {
		ArrowTypeAction action = new ArrowTypeAction(workbenchPage,
				ArrowType.NONE_LITERAL, Properties.ID_ARROW_SOURCE);
		action.setId(ActionIds.ACTION_ARROW_TYPE_SOURCE_NONE);
		action.setText(DiagramUIActionsMessages.ArrowTypeAction_none);
		action
				.setToolTipText(DiagramUIActionsMessages.ArrowTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_NONE);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_NONE_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_NONE);
		return action;
	}

	/**
	 * Creates the arrow type none action on the target end.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the arrow type none action.
	 */
	public static ArrowTypeAction createArrowTypeTargetNoneAction(
			IWorkbenchPage workbenchPage) {
		ArrowTypeAction action = new ArrowTypeAction(workbenchPage,
				ArrowType.NONE_LITERAL, Properties.ID_ARROW_TARGET);
		action.setId(ActionIds.ACTION_ARROW_TYPE_TARGET_NONE);
		action.setText(DiagramUIActionsMessages.ArrowTypeAction_none);
		action
				.setToolTipText(DiagramUIActionsMessages.ArrowTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_NONE);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_NONE_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_NONE);
		return action;
	}

	/**
	 * Creates the solid arrow type action on the source end.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the solid arrow type action.
	 */
	public static ArrowTypeAction createArrowTypeSourceSolidAction(
			IWorkbenchPage workbenchPage) {
		ArrowTypeAction action = new ArrowTypeAction(workbenchPage,
				ArrowType.SOLID_ARROW_LITERAL, Properties.ID_ARROW_SOURCE);
		action.setId(ActionIds.ACTION_ARROW_TYPE_SOURCE_SOLID);
		action.setText(DiagramUIActionsMessages.ArrowTypeAction_solid);
		action
				.setToolTipText(DiagramUIActionsMessages.ArrowTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_SOLID);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_SOLID_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_SOLID);
		return action;
	}

	/**
	 * Creates the solid arrow type action on the target end.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the solid arrow type action.
	 */
	public static ArrowTypeAction createArrowTypeTargetSolidAction(
			IWorkbenchPage workbenchPage) {
		ArrowTypeAction action = new ArrowTypeAction(workbenchPage,
				ArrowType.SOLID_ARROW_LITERAL, Properties.ID_ARROW_TARGET);
		action.setId(ActionIds.ACTION_ARROW_TYPE_TARGET_SOLID);
		action.setText(DiagramUIActionsMessages.ArrowTypeAction_solid);
		action
				.setToolTipText(DiagramUIActionsMessages.ArrowTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_SOLID);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_SOLID_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_SOLID);
		return action;
	}

	/**
	 * Creates the open arrow type action on the source end.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the solid arrow type action.
	 */
	public static ArrowTypeAction createArrowTypeSourceOpenAction(
			IWorkbenchPage workbenchPage) {
		ArrowTypeAction action = new ArrowTypeAction(workbenchPage,
				ArrowType.OPEN_ARROW_LITERAL, Properties.ID_ARROW_SOURCE);
		action.setId(ActionIds.ACTION_ARROW_TYPE_SOURCE_OPEN);
		action.setText(DiagramUIActionsMessages.ArrowTypeAction_open);
		action
				.setToolTipText(DiagramUIActionsMessages.ArrowTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_OPEN);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_OPEN_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_OPEN);
		return action;
	}

	/**
	 * Creates the open arrow type action on the target end.
	 * 
	 * @param workbenchPage
	 *            the workbench page.
	 * @return the solid arrow type action.
	 */
	public static ArrowTypeAction createArrowTypeTargetOpenAction(
			IWorkbenchPage workbenchPage) {
		ArrowTypeAction action = new ArrowTypeAction(workbenchPage,
				ArrowType.OPEN_ARROW_LITERAL, Properties.ID_ARROW_TARGET);
		action.setId(ActionIds.ACTION_ARROW_TYPE_TARGET_OPEN);
		action.setText(DiagramUIActionsMessages.ArrowTypeAction_open);
		action
				.setToolTipText(DiagramUIActionsMessages.ArrowTypeAction_ChangePropertyValueRequest_label);
		action
				.setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_OPEN);
		action
				.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_OPEN_DISABLED);
		action
				.setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARROW_TYPE_OPEN);
		return action;
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (getId() == ActionIds.ACTION_ARROW_TYPE_SOURCE_NONE
				|| getId() == ActionIds.ACTION_ARROW_TYPE_SOURCE_OPEN
				|| getId() == ActionIds.ACTION_ARROW_TYPE_SOURCE_SOLID
				|| getId() == ActionIds.ACTION_ARROW_TYPE_TARGET_NONE
				|| getId() == ActionIds.ACTION_ARROW_TYPE_TARGET_OPEN
				|| getId() == ActionIds.ACTION_ARROW_TYPE_TARGET_SOLID) {
			for (ListIterator<?> li = getSelectedObjects().listIterator(); li
					.hasNext();) {
				Object object = li.next();
				if (!(object instanceof ConnectionEditPart)) {
					return false;
				}
				View view = ((IGraphicalEditPart) object).getPrimaryView();
				if (view != null) {
					ArrowStyle style = (ArrowStyle) view
							.getStyle(NotationPackage.eINSTANCE.getArrowStyle());
					if (style == null) {
						return false;
					}
				}
			}
		}

		return super.calculateEnabled();
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.actions.PropertyChangeAction#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		return arrowType;
	}

}
