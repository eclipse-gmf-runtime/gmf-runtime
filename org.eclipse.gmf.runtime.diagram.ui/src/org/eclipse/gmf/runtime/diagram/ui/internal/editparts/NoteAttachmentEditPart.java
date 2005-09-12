/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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

import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
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
}
