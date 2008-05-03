/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/*
 * @canBeSeenBy %level1
 */
public class NoteAttachmentEditPart extends ConnectionNodeEditPart {

	public NoteAttachmentEditPart(View view) {
		super(view);
	}

	/**
	 * @see ConnectionNodeEditPart#createConnectionFigure()
	 */
	protected Connection createConnectionFigure() {
		PolylineConnectionEx conn = new PolylineConnectionEx();
		conn.setLineStyle(Graphics.LINE_DOT);
		return conn;
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
