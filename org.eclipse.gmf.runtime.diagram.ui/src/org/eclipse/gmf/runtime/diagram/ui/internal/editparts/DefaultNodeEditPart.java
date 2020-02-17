/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
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

		public DefaultNodeFigure(int width, int height) {
			super(width, height);
		}

		protected void paintFigure(Graphics g) {
			Rectangle r = Rectangle.SINGLETON;
			r.setBounds(getBounds());

			g.setBackgroundColor(DiagramColorConstants.diagramLightRed);
			g.fillRectangle(r);

			g.setForegroundColor(DiagramColorConstants.black);
			r.width--;
			r.height--;
			g.drawRectangle(r);

			String txt = DiagramUIMessages.InvalidView;

			IMapMode mm = MapModeUtil.getMapMode(this);
			if (txt != null && txt.length() > 0) {
				Dimension td = FigureUtilities.getTextExtents(txt, g.getFont());
				mm.DPtoLP(td);
				Point p = FigureUtilities.getLocation(
					PositionConstants.NORTH_SOUTH, td, r);
				g.drawString(txt, p);
				setPreferredSize(td.expand(mm.DPtoLP(10), mm.DPtoLP(10)));
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
		return new DefaultNodeFigure(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40));
	}

}
