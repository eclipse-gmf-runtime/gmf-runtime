/******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.StringConstants;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal.ShapeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.PropertiesSetStyle;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The LEDView Factory class 
 * @author mmostafa
 */
public class LEDViewFactory extends ShapeViewFactory {

	protected void decorateView(View containerView, View view,
			IAdaptable semanticAdapter, String semanticHint, int index,
			boolean persisted) {
		super.decorateView(containerView, view, semanticAdapter, semanticHint,
				index, persisted);
		ShapeStyle style = (ShapeStyle) view.getStyle(NotationPackage.eINSTANCE
				.getShapeStyle());
		style.setFontColor((FigureUtilities
				.colorToInteger(LogicColorConstants.displayText)).intValue());
		style.setFontHeight(19);
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