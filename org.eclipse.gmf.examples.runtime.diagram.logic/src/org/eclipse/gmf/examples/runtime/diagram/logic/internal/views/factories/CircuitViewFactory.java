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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The CircuitView Factory class 
 * @author mmostafa
 */
public class CircuitViewFactory
	extends AbstractShapeViewFactory {

	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractNodeViewFactory#decorateView(org.eclipse.gmf.runtime.notation.View, org.eclipse.gmf.runtime.notation.View, org.eclipse.core.runtime.IAdaptable, java.lang.String, int, boolean)
	 */
	protected void decorateView(View containerView, View view,
			IAdaptable semanticAdapter, String semanticHint, int index,
			boolean persisted) {
		super.decorateView(containerView, view, semanticAdapter, semanticHint,
			index, persisted);
        ShapeStyle style = (ShapeStyle)view.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
        style.setFillColor((FigureUtilities.colorToInteger(LogicColorConstants.logicGreen)).intValue());
        style.setLineColor((FigureUtilities.colorToInteger(LogicColorConstants.connectorGreen)).intValue());
		getViewService().createNode(semanticAdapter, view,
			LogicConstants.LOGIC_SHAPE_COMPARTMENT, ViewUtil.APPEND, persisted, getPreferencesHint()); 
	}
}