/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Connection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

/**
 * Implements a Connection Editpart to represnt a Wire like connection.
 *
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class WireEditPart extends ConnectionNodeEditPart implements PropertyChangeListener {

	public WireEditPart(View view) {
		super(view);
	}

	public static final Color dead = new Color(0, 0, 0);

	@Override
	public void activateFigure() {
		super.activateFigure();
		/*
		 * Once the figure has been added to the ConnectionLayer, start listening for
		 * its router to change.
		 */
		getFigure().addPropertyChangeListener(Connection.PROPERTY_CONNECTION_ROUTER, this);
	}

	/**
	 * Method createConnectionFigure.
	 * 
	 * @return Connection
	 */
	@Override
	protected Connection createConnectionFigure() {
		if (getModel() == null) {
			return null;
		}

		Connection connx = new PolylineConnectionEx();
		return connx;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	@Override
	protected void handleNotificationEvent(Notification event) {
		super.handleNotificationEvent(event);
		if (NotationPackage.eINSTANCE.getIdentityAnchor_Id().equals(event.getFeature())) {
			anchorChange();
		}
	}

	/**
	 * Refreshes the visual aspects of this, based upon the model (Wire). It changes
	 * the wire color depending on the state of Wire.
	 *
	 */
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();

		getFigure().setForegroundColor(dead);

	}

	@Override
	public Object getPreferredValue(EStructuralFeature feature) {
		if (feature == NotationPackage.eINSTANCE.getLineStyle_LineColor()) {
			return FigureUtilities.colorToInteger(dead);
		}
		return super.getPreferredValue(feature);
	}
}
