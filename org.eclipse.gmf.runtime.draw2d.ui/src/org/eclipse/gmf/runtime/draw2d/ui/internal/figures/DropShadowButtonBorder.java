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

package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/*
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 */
public class DropShadowButtonBorder 
	extends AbstractBorder
{

private static final Color
	highlight   = ColorConstants.menuBackgroundSelected,
	dropshadow2 = new Color(null, new RGB (143, 141, 138)),
	dropshadow3 = ColorConstants.buttonDarker;

/**
 * Returns the space used by the border for the 
 * figure provided as input. In this border all 
 * sides always have equal width.
 *
 * @param figure  Figure for which this is the border.
 * @return  Insets for this border.
 */
public Insets getInsets(IFigure figure) {
    IMapMode mm = MapModeUtil.getMapMode(figure);

    int DPtoLP_1 = mm.DPtoLP(1);
    int DPtoLP_3 = mm.DPtoLP(3);
    return new Insets(DPtoLP_1, DPtoLP_1, DPtoLP_3, DPtoLP_3);
}

public boolean isOpaque(){
	return true;
}

public void paint(IFigure figure, Graphics g, Insets theInsets){
    
	ButtonModel model = ((Clickable)figure).getModel();
	Rectangle r = getPaintRectangle(figure, theInsets);
	g.setLineWidth(1);
    
    IMapMode mm = MapModeUtil.getMapMode(figure);

    int DPtoLP_3 = mm.DPtoLP(3);
	r.width  -= DPtoLP_3;
	r.height -= DPtoLP_3;

    int DPtoLP_1 = mm.DPtoLP(1);

	if (model.isMouseOver() && !model.isArmed()){

		g.setForegroundColor(highlight);
		g.drawRectangle(r);

		r.translate(1,1);
		g.setForegroundColor(dropshadow2);
		g.drawLine(r.x      , r.bottom(), r.right(), r.bottom());
		g.drawLine(r.right(), r.y       , r.right(), r.bottom());

		r.translate(1,1);
		g.setForegroundColor(dropshadow3);
		g.drawLine(r.x+1    , r.bottom(), r.right()-1, r.bottom());
		g.drawLine(r.right(), r.y+1     , r.right()  , r.bottom()-1);
	}

	else if (model.isPressed()){
		r.translate(DPtoLP_1, DPtoLP_1);

		g.setForegroundColor(highlight);
		g.drawRectangle(r);

		r.translate(DPtoLP_1, DPtoLP_1);
		g.setForegroundColor(dropshadow2);
		g.drawLine(r.x      , r.bottom(), r.right(), r.bottom());
		g.drawLine(r.right(), r.y       , r.right(), r.bottom());
	}

	else {
		r.translate(DPtoLP_1, DPtoLP_1);

		g.setForegroundColor(dropshadow3);
		g.drawRectangle(r);

		r.translate(DPtoLP_1, DPtoLP_1);
		g.drawLine(r.x      , r.bottom(), r.right(), r.bottom());
		g.drawLine(r.right(), r.y       , r.right(), r.bottom());
	}
}

}
