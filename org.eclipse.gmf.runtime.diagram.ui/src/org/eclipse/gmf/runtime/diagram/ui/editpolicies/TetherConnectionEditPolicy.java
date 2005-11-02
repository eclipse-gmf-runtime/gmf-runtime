/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.GraphicalEditPolicyEx;

/**
 * An EditPolicy that tethers a shape to the connection to which it 
 * applies. The default style for the tether is <code>Graphics.LINE_DASH</code>
 * 
 * @author jcorchis
 */
public class TetherConnectionEditPolicy
	extends GraphicalEditPolicyEx {

	private Polyline tether;

	private OwnerMovedListener ownerMovedListener = new OwnerMovedListener();

	/**
	 * Returns the tether figure which is a <code>Polyline</code> with the
	 * given style.
	 * 
	 * @return the <code>Polyline</code>
	 */
	private Polyline getConnection() {
		if (tether == null) {
			tether = new PolylineConnectionEx();
			tether.setLineStyle(Graphics.LINE_DASH);			
		}
		return tether;
	}

	/**
	 * Listens to the owner figure being moved so the tether position can be
	 * updated when this occurs.
	 */
	private class OwnerMovedListener
		implements FigureListener {

		/**
		 * @see org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
		 */
		public void figureMoved(IFigure source) {
			refresh();
		}
	}

	/**
	 * Adds the tether upon activation
	 */
	public void activate() {
		super.activate();
		addConnection();
		((IGraphicalEditPart) getHost()).getFigure().addFigureListener(
			ownerMovedListener);
	}

	/**
	 * Removes the tether upon deactivation.
	 */
	public void deactivate() {
		((IGraphicalEditPart) getHost()).getFigure().removeFigureListener(
			ownerMovedListener);
		removeConnection();
		super.deactivate();
	}

	/**
	 * Removes the tether from the label.
	 */
	private void removeConnection() {
		if (getParentFigure().getChildren().contains(getConnection()))
			getParentFigure().remove(getConnection());
	}
	
	/**
	 * Adds the tether
	 *
	 */
	private void addConnection() {
		if (!getParentFigure().getChildren().contains(getConnection()))
			getParentFigure().add(getConnection());
	}

	/**
	 * Updates the end-points of the tether based on the figure's position.
	 */
	public void refresh() {
		IFigure figure = ((GraphicalEditPart) getHost()).getFigure();

		Rectangle r = figure.getBounds().getCopy();
		
		PointList pl = new PointList();
		Point refPoint = new Point();
		if (getHost().getParent() instanceof AbstractConnectionEditPart) {
			AbstractConnectionEditPart connectionClassEP = (AbstractConnectionEditPart) getHost()
			.getParent();
			pl = connectionClassEP.getConnectionFigure().getPoints();
			refPoint = PointListUtilities.calculatePointRelativeToLine(pl, 0, 50, true);
		} else {
			refPoint = ((AbstractGraphicalEditPart)getHost().getParent()).getFigure().getBounds().getLocation();
		}

		Point midTop = new Point(r.x + r.width / 2, r.y);
		Point midBottom = new Point(r.x + r.width / 2, r.y + r.height);
		Point midLeft = new Point(r.x, r.y + r.height / 2);
		Point midRight = new Point(r.x + r.width, r.y + r.height / 2);

		Point startPoint = midTop;

		int x = r.x + r.width / 2 - refPoint.x;
		int y = r.y + r.height / 2 - refPoint.y;

		if (y > 0 && y > x && y > -x)
			startPoint = midTop;
		else if (y < 0 && y < x && y < -x)
			startPoint = midBottom;
		else if (x < 0 && y > x && y < -x)
			startPoint = midRight;
		else
			startPoint = midLeft;

		getConnection().setStart(startPoint);
		getConnection().setEnd(refPoint);
		getConnection().setForegroundColor(
			((AbstractGraphicalEditPart) getHost().getParent()).getFigure()
				.getForegroundColor());
	}

	/**
	 * Returns the layer that the tether will be added.
	 * 
	 * @return the layer that the tether will be added
	 */
	private IFigure getParentFigure() {
		return getHostFigure().getParent();		
	}
}

