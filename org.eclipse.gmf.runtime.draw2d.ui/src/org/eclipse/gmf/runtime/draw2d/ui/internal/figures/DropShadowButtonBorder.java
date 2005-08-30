/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Color;

/*
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 */
public class DropShadowButtonBorder 
	extends AbstractBorder
{

protected Insets insets = new Insets(26,26,80,80);

private static final Color
	highlight   = ColorConstants.menuBackgroundSelected,
	dropshadow2 = new Color(null, ViewForm.borderMiddleRGB),
    dropshadow3 = new Color(null, ViewForm.borderOutsideRGB);

/**
 * Returns the space used by the border for the 
 * figure provided as input. In this border all 
 * sides always have equal width.
 *
 * @param figure  Figure for which this is the border.
 * @return  Insets for this border.
 */
public Insets getInsets(IFigure figure){
	return insets;
}

public boolean isOpaque(){
	return true;
}

public void paint(IFigure figure, Graphics g, Insets theInsets){
	ButtonModel model = ((Clickable)figure).getModel();
	Rectangle r = getPaintRectangle(figure, theInsets);
	g.setLineWidth(1);
	r.width  -= 80;
	r.height -= 80;

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
		r.translate(26,26);

		g.setForegroundColor(highlight);
		g.drawRectangle(r);

		r.translate(26,26);
		g.setForegroundColor(dropshadow2);
		g.drawLine(r.x      , r.bottom(), r.right(), r.bottom());
		g.drawLine(r.right(), r.y       , r.right(), r.bottom());
	}

	else {
		r.translate(26,26);

		g.setForegroundColor(dropshadow3);
		g.drawRectangle(r);

		r.translate(26,26);
		g.drawLine(r.x      , r.bottom(), r.right(), r.bottom());
		g.drawLine(r.right(), r.y       , r.right(), r.bottom());
	}
}

}
