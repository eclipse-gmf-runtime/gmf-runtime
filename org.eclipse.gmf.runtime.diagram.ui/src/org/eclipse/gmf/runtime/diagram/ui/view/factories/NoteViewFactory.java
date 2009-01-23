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

package org.eclipse.gmf.runtime.diagram.ui.view.factories;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.FillStyle;
import org.eclipse.gmf.runtime.notation.LineStyle;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

/**
 * The Factory class responsible for creating Note Views
 * @author mmostafa
 */
public class NoteViewFactory
	extends TextShapeViewFactory {

	/**
	 * Method NoteView. creation constructor
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param persisted
	 */
	public View createView(IAdaptable semanticAdapter, View containerView,
			String semanticHint, int index, boolean persisted, final PreferencesHint preferencesHint) {
		View view =  super.createView(semanticAdapter, containerView, semanticHint,
			index, persisted, preferencesHint);
		// if a note view get created with a diagram semantic element
		// linked to it then we mark the note view as a diagram link
		// this will trigger the noteEdit part to switch the figure
		// to the DiagramLink mode (no border, no fill color and center
		// alignment)
		if (view.getElement() instanceof Diagram){
			if(semanticHint==null || semanticHint.length()==0)
			   view.setType(ViewType.NOTE);
			EAnnotation annotation  = EcoreFactory.eINSTANCE.createEAnnotation();
			annotation.setSource(Properties.DIAGRAMLINK_ANNOTATION);
			view.getEAnnotations().add(annotation);
		}
		return view;
	}

	protected void initializeFromPreferences(View view) {
		super.initializeFromPreferences(view);
		// support the diagram link mode
		if (!(view.getElement() instanceof Diagram)) {
			IPreferenceStore store = (IPreferenceStore) getPreferencesHint()
				.getPreferenceStore();
			FillStyle fillStyle = (FillStyle) view
				.getStyle(NotationPackage.Literals.FILL_STYLE);
			if (fillStyle != null) {
				// fill color
				RGB fillRGB = PreferenceConverter.getColor(store,
					IPreferenceConstants.PREF_NOTE_FILL_COLOR);

				fillStyle.setFillColor(FigureUtilities.RGBToInteger(fillRGB)
					.intValue());
			}

			LineStyle lineStyle = (LineStyle) view
				.getStyle(NotationPackage.Literals.LINE_STYLE);
			if (lineStyle != null) {
				// line color
				RGB lineRGB = PreferenceConverter.getColor(store,
					IPreferenceConstants.PREF_NOTE_LINE_COLOR);

				lineStyle.setLineColor(FigureUtilities.RGBToInteger(lineRGB)
					.intValue());
			}
		}
	}

	@Override
	protected void decorateView(View containerView, View view,
			IAdaptable semanticAdapter, String semanticHint, int index,
			boolean persisted) {
		ShapeStyle style = (ShapeStyle) view.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
		if (style != null) {
			style.setLineWidth(1);
		}
		super.decorateView(containerView, view, semanticAdapter, semanticHint, index,
				persisted);
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
