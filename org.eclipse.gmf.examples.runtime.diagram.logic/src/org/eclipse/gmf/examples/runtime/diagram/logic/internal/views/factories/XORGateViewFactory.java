/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The XORGateView Factory class
 * 
 * @author mmostafa
 */
public class XORGateViewFactory
	extends LogicGateViewFactory {
    
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted) {
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index,
            persisted);
        ShapeStyle style = (ShapeStyle)view.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
        style.setFillColor((FigureUtilities.colorToInteger(LogicColorConstants.xorGate)).intValue());

    }
}