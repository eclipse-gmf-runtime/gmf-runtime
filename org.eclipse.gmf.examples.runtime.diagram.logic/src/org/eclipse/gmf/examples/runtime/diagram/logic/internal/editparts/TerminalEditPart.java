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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.diagram.ui.figures.IBorderItemLocator;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A Connection Point can sit on a border of a logic shape or on the
 * interior.
 * 
 * @author qili
 */
public class TerminalEditPart extends AbstractBorderItemEditPart {

	private BorderItemLocator locator;

	/**
	 * @param view
	 */
	public TerminalEditPart(View view) {
		super(view);
	}

	protected NodeFigure createNodeFigure() {
		EditPart host = getParent();
		if (host instanceof ITerminalOwnerEditPart) {
			return ((ITerminalOwnerEditPart) host)
				.createOwnedTerminalFigure(this);
		}
		return null;
	}
	
	/* 
	 * Don't allow terminal editparts to be selectable
	 */
	public boolean isSelectable() {
		return false;
	}
	
	public void activate() {
		super.activate();
		Insets parentInset = new Insets(0);
		IFigure fig = ((BorderItemLocator)getLocator()).getParentFigure();
		if (fig != null) {
			parentInset = fig.getInsets();
		}
		Rectangle rBounds = ((NodeFigure) getFigure()).getHandleBounds();
		((BorderItemLocator)getLocator()).setBorderItemOffset(new Dimension(
			rBounds.width / 2 + parentInset.getWidth() / 2, rBounds.height / 2
				+ parentInset.getHeight() / 2));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart#getLocator()
	 */
	public IBorderItemLocator getLocator() {
		return locator;
	}

	
	/**
	 * Sets the locator.
	 * @param locator The locator to set.
	 */
	public void setLocator(BorderItemLocator locator) {
		this.locator = locator;
	}
    
    public Object getPreferredValue(EStructuralFeature feature) {
        if (feature == NotationPackage.eINSTANCE.getFillStyle_FillColor()) {
            return FigureUtilities
                .colorToInteger(LogicColorConstants.connectorGreen);
        } else if (feature == NotationPackage.eINSTANCE
            .getLineStyle_LineColor()) {
            return FigureUtilities
                .colorToInteger(LogicColorConstants.logicBlack);
        }
        return super.getPreferredValue(feature);
    }
}