/******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.accessibility.AccessibleEvent;

import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenDiagramEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers.GeoshapeConstants;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.DiagramLinkDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NonSemanticEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;

/**
 * This is a base class for all Geometric Shapes
 * 
 * @author jschofie
 */
public abstract class GeoShapeEditPart extends ShapeNodeEditPart {

	/**
	 * @param view
	 */
	public GeoShapeEditPart(View view) {
		super(view);
	}

	/**
	 * Implement to return the NodeFigre.
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#createNodeFigure()
	 */
	protected abstract NodeFigure createNodeFigure();

	/**
	 * Adds support for diagram links.
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#createDefaultEditPolicies()
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();

		// Remove semantic edit policy and install a non-semantic edit policy
		removeEditPolicy(EditPolicyRoles.SEMANTIC_ROLE);
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new NonSemanticEditPolicy());

		// Add diagram link support to all Geo shapes
		// The following two edit policies support the links.
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new DiagramLinkDragDropEditPolicy());

		installEditPolicy(EditPolicyRoles.OPEN_ROLE,
				new OpenDiagramEditPolicy());

		// This View doesn't have semantic elements so use a component edit
		// policy that only gets a command to delete the view
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new ViewComponentEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#getAccessibleEditPart()
	 */
	protected AccessibleEditPart getAccessibleEditPart() {

		if (accessibleEP == null)
			accessibleEP = new AccessibleGraphicalEditPart() {

				public void getName(AccessibleEvent e) {
					View view = (View) getModel();
					e.result = GeoshapeConstants.getShapeLocalizedType(view
							.getType());
				}
			};

		return accessibleEP;
	}

	/**
	 * this method will return the primary child EditPart inside this edit part
	 * 
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(CommonParserHint.DESCRIPTION);
	}

	public Object getPreferredValue(EStructuralFeature feature) {
		Object preferenceStore = getDiagramPreferencesHint()
				.getPreferenceStore();
		if (preferenceStore instanceof IPreferenceStore) {
			if (feature == NotationPackage.eINSTANCE.getLineStyle_LineColor()) {

				return FigureUtilities.RGBToInteger(PreferenceConverter
						.getColor((IPreferenceStore) preferenceStore,
								IPreferenceConstants.PREF_NOTE_LINE_COLOR));

			} else if (feature == NotationPackage.eINSTANCE
					.getFillStyle_FillColor()) {

				return FigureUtilities.RGBToInteger(PreferenceConverter
						.getColor((IPreferenceStore) preferenceStore,
								IPreferenceConstants.PREF_NOTE_FILL_COLOR));

			}
		}

		return super.getPreferredValue(feature);
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart#setLineWidth(int)
	 */
	protected void setLineWidth(int width) {
		((NodeFigure) getFigure()).setLineWidth(getMapMode().DPtoLP(width));
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#handleNotificationEvent(org.eclipse.emf.common.notify.Notification)
	 */
	protected void handleNotificationEvent(Notification notification) {
		Object feature = notification.getFeature();
		if (NotationPackage.eINSTANCE.getLineStyle_LineWidth().equals(feature)) {
			refreshLineWidth();
		} else if (NotationPackage.eINSTANCE.getLineTypeStyle_LineType().equals(
				feature)) {
			refreshLineType();
		} else {
			super.handleNotificationEvent(notification);
		}
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshLineWidth();
		refreshLineType();
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#setLineType(int)
	 */
	protected void setLineType(int lineType) {
		((NodeFigure) getFigure()).setLineStyle(lineType);
	}

}