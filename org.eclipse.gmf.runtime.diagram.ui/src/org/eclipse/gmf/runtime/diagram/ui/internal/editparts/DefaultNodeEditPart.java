/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.DiagramColorConstants;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
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

			String txt = DiagramResourceManager
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
