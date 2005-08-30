/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Connection;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.FigureFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

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
	Connection connx = FigureFactory.createNewBendableWire();
	return connx;
}

/**
 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
 */
protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
	super.handlePropertyChangeEvent(event);
	
    Object element = MetaModelUtil.getElement(event.getPropertyName());
    if (element != null && element.equals(NotationPackage.eINSTANCE.getIdentityAnchor_Id())) {
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
