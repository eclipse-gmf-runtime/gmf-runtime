/******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.StringConstants;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal.ShapeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.PropertiesSetStyle;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The CircuitView Factory class
 * 
 * @author mmostafa
 */
public class CircuitViewFactory extends ShapeViewFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractNodeViewFactory#decorateView(org.eclipse.gmf.runtime.notation.View,
	 *      org.eclipse.gmf.runtime.notation.View,
	 *      org.eclipse.core.runtime.IAdaptable, java.lang.String, int, boolean)
	 */
	protected void decorateView(View containerView, View view,
			IAdaptable semanticAdapter, String semanticHint, int index,
			boolean persisted) {
		super.decorateView(containerView, view, semanticAdapter, semanticHint,
				index, persisted);
		ShapeStyle style = (ShapeStyle) view.getStyle(NotationPackage.eINSTANCE
				.getShapeStyle());
		style.setFillColor((FigureUtilities
				.colorToInteger(LogicColorConstants.logicGreen)).intValue());
		style
				.setLineColor((FigureUtilities
						.colorToInteger(LogicColorConstants.connectorGreen))
						.intValue());
		getViewService().createNode(semanticAdapter, view,
				LogicConstants.LOGIC_SHAPE_COMPARTMENT, ViewUtil.APPEND,
				persisted, getPreferencesHint());
	}

	protected List createStyles(View view) {
		List styles = super.createStyles(view);
		PropertiesSetStyle properties = NotationFactory.eINSTANCE
				.createPropertiesSetStyle();
		properties.setName(StringConstants.PORTS_PROPERTIES_STYLE_NAME);
		properties.createProperty(StringConstants.PORTS_COLOR_PROPERTY_NAME,
				EcorePackage.eINSTANCE.getEIntegerObject(), null);
		styles.add(properties);
		return styles;
	}
}