/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import com.ibm.xtools.notation.View;

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
