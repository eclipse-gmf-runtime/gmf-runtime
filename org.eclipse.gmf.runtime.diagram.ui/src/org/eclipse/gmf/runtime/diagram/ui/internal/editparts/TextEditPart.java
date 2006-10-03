/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

/*
 * @canBeSeenBy %level1
 */
public class TextEditPart extends ShapeEditPart {

	private static final int TEXT_DEFAULT_WIDTH_DP  = 100;
	private static final int TEXT_DEFAULT_HEIGHT_DP =  13;

	public TextEditPart(View view) {
		super(view);
	}

	protected IFigure createFigure() {
		Dimension defaultSize = new Dimension(getMapMode().DPtoLP(TEXT_DEFAULT_WIDTH_DP), getMapMode().DPtoLP(TEXT_DEFAULT_HEIGHT_DP));
		DefaultSizeNodeFigure nodeFigure = new DefaultSizeNodeFigure(defaultSize.width, defaultSize.height);
		nodeFigure.setBorder(null);
		nodeFigure.setOpaque(false);
		nodeFigure.setLayoutManager(new StackLayout());
		nodeFigure.setDefaultSize(defaultSize);
		return nodeFigure;
	}

	protected void setBackgroundColor(Color color) {
		getFigure().setBackgroundColor(null);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 * The Text view does not have foreground and background colors
	 */
	protected void refreshVisuals() {
		refreshVisibility();
		refreshBounds();
		refreshFont();
	}

	/**
	 * this method will return the primary child EditPart  inside this edit part
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart(){
		return getChildBySemanticHint(CommonParserHint.DESCRIPTION);
	}

}
