/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;


import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Connection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * Implements a Connection Editpart to represnt a Wire like
 * connection.
 *
 */
/*
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class WireEditPart
	extends ConnectionNodeEditPart
	implements PropertyChangeListener
{

public WireEditPart(View view) {
	super(view);
}
	
public static final Color dead  = new Color(Display.getDefault(),0,0,0);

public void activateFigure(){
	super.activateFigure();
	/*Once the figure has been added to the ConnectionLayer, start listening for its
	 * router to change.
	 */
	getFigure().addPropertyChangeListener(Connection.PROPERTY_CONNECTION_ROUTER, this);
}

/**
 * Method createConnectionFigure.
 * @return Connection
 */
protected Connection createConnectionFigure(){
	if (getModel() == null)
		return null;
	
	Connection connx = new PolylineConnectionEx();
	return connx;
}

/**
 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
 */
protected void handleNotificationEvent(Notification event) {
	super.handleNotificationEvent(event);
	if (NotationPackage.eINSTANCE.getIdentityAnchor_Id().equals(event.getFeature())) {
    	anchorChange();
    }
}

/**
 * Refreshes the visual aspects of this, based upon the
 * model (Wire). It changes the wire color depending on
 * the state of Wire.
 * 
 */
protected void refreshVisuals() {
	super.refreshVisuals();

	getFigure().setForegroundColor(dead);

}

}
