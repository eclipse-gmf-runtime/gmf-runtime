/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;

import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.OnConnectionLocator;
import com.ibm.xtools.notation.View;

/**
 * An editpart to show a default connector view.
 * 
 * @author cmahoney
 */
public class DefaultConnectorEditPart
	extends ConnectionNodeEditPart {

	/**
	 * Constructs a new instance.
	 * 
	 * @param view
	 */
	public DefaultConnectorEditPart(View view) {
		super(view);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#createConnectionFigure()
	 */
	protected Connection createConnectionFigure() {

		PolylineConnectionEx conn = new PolylineConnectionEx() {

			public void paint(Graphics graphics) {
				setForegroundColor(ColorConstants.red);
				super.paint(graphics);
			}

		};

		conn.add(new WrapLabel(PresentationResourceManager
			.getI18NString("InvalidView")), new OnConnectionLocator(conn, 50)); //$NON-NLS-1$
		return conn;
	}
}