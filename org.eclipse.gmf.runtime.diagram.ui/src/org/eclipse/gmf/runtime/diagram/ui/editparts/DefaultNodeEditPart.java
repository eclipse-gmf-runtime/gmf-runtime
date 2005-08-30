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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.diagram.ui.figures.DiagramColorConstants;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * An editpart to show a default node view.
 * 
 * @author cmahoney
 */
public class DefaultNodeEditPart
	extends ShapeNodeEditPart {

	/**
	 * Rectangle figure with error text.
	 */
	static class DefaultNodeFigure
		extends DefaultSizeNodeFigure {

		protected void paintFigure(Graphics g) {
			Rectangle r = Rectangle.SINGLETON;
			r.setBounds(getBounds());

			g.setBackgroundColor(DiagramColorConstants.diagramLightRed);
			g.fillRectangle(r);

			g.setForegroundColor(DiagramColorConstants.black);
			r.width--;
			r.height--;
			g.drawRectangle(r);

			String txt = PresentationResourceManager
				.getI18NString("InvalidView"); //$NON-NLS-1$

			if (txt != null && txt.length() > 0) {
				Dimension td = FigureUtilities.getTextExtents(txt, g.getFont());
				MapMode.translateToLP(td);
				Point p = FigureUtilities.getLocation(
					PositionConstants.NORTH_SOUTH, td, r);
				g.drawString(txt, p);
				setPreferredSize(td.expand(MapMode.DPtoLP(10), MapMode
					.DPtoLP(10)));
			}
		}
	}

	/**
	 * Constructs a new instance.
	 * 
	 * @param view
	 */
	public DefaultNodeEditPart(View view) {
		super(view);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#createNodeFigure()
	 */
	protected NodeFigure createNodeFigure() {
		return new DefaultNodeFigure();
	}

}
