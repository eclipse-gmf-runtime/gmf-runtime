/******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.views.factories;

import java.util.List;

import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.type.GeoshapeType;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.TextShapeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

/**
 * The GeoShapeView Factory class
 * 
 * @author mmostafa
 */
public class GeoShapeViewFactory
	extends TextShapeViewFactory {

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.view.AbstractView#initializeFromPreferences(org.eclipse.jface.preference.IPreferenceStore)
	 */
	protected void initializeFromPreferences(View view) {
		ShapeStyle style = (ShapeStyle) view.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
		if (style != null) {
			style.setLineWidth(1);
			style.setTransparency(0);
			if (view.getType().equals(GeoshapeType.ROUNDRECTANGLE.getSemanticHint())) { 
				style.setRoundedBendpointsRadius(20);
			}
		}
		
		super.initializeFromPreferences(view);

		IPreferenceStore store = (IPreferenceStore) getPreferencesHint().getPreferenceStore();

		// fill color
		RGB fillRGB = PreferenceConverter.getColor(store,
			IPreferenceConstants.PREF_NOTE_FILL_COLOR);
		ViewUtil.setStructuralFeatureValue(view, NotationPackage.eINSTANCE.getFillStyle_FillColor(),
			FigureUtilities.RGBToInteger(fillRGB));

		//line color
		RGB lineRGB = PreferenceConverter.getColor(store,
			IPreferenceConstants.PREF_NOTE_LINE_COLOR);
		ViewUtil.setStructuralFeatureValue(view, NotationPackage.eINSTANCE.getLineStyle_LineColor(),
			FigureUtilities.RGBToInteger(lineRGB));
	}
	
	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory#createStyles(org.eclipse.gmf.runtime.notation.View)
	 */
	protected List createStyles(View view) {
		List styles = super.createStyles(view);
		
		styles.add(NotationFactory.eINSTANCE.createTextStyle());
		styles.add(NotationFactory.eINSTANCE.createLineTypeStyle());
		return styles;
	}
}