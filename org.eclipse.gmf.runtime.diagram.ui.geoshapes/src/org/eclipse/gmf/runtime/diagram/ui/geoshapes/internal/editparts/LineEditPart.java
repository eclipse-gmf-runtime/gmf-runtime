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

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NonSemanticEditPolicy;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author jschofie
 * 
 * Controls the interactions between the Line and its underlying view
 */
public class LineEditPart extends ConnectionNodeEditPart {

	public LineEditPart(View view) {
		super(view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#createConnectionFigure()
	 */
	protected Connection createConnectionFigure() {
		PolylineConnectionEx conn = new PolylineConnectionEx();
		return conn;
	}

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

		// This View doesn't have semantic elements so use a component edit
		// policy that only gets a command to delete the view
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new ViewComponentEditPolicy());
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart#handleNotificationEvent(org.eclipse.emf.common.notify.Notification)
	 */
	protected void handleNotificationEvent(Notification notification) {
		Object feature = notification.getFeature();
		if (NotationPackage.Literals.LINE_STYLE__LINE_WIDTH.equals(feature)) {
			refreshLineWidth();
			refreshArrowSource();
			refreshArrowTarget();
		} else if (NotationPackage.Literals.LINE_TYPE_STYLE__LINE_TYPE
				.equals(feature)) {
			refreshLineType();
		} else if (NotationPackage.Literals.ARROW_STYLE__ARROW_SOURCE
				.equals(feature)) {
			refreshArrowSource();
		} else if (NotationPackage.Literals.ARROW_STYLE__ARROW_TARGET
				.equals(feature)) {
			refreshArrowTarget();
		} else {
			super.handleNotificationEvent(notification);
		}
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshLineWidth();
		refreshLineType();
		refreshArrowSource();
		refreshArrowTarget();
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#setLineWidth(int)
	 */
	protected void setLineWidth(int width) {
		PolylineConnectionEx conn = (PolylineConnectionEx) getFigure();
		conn.setLineWidth(getMapMode().DPtoLP(width));
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#setLineType(int)
	 */
	protected void setLineType(int lineType) {
		PolylineConnectionEx conn = (PolylineConnectionEx) getFigure();
		conn.setLineStyle(lineType);
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#setArrowSource(org.eclipse.draw2d.RotatableDecoration)
	 */
	protected void setArrowSource(RotatableDecoration arrowDecoration) {
		PolylineConnectionEx conn = (PolylineConnectionEx) getFigure();
		conn.setSourceDecoration(arrowDecoration);
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#setArrowTarget(org.eclipse.draw2d.RotatableDecoration)
	 */
	protected void setArrowTarget(RotatableDecoration arrowDecoration) {
		PolylineConnectionEx conn = (PolylineConnectionEx) getFigure();
		conn.setTargetDecoration(arrowDecoration);
	}

}