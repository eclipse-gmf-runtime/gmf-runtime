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

package org.eclipse.gmf.runtime.diagram.ui.view.factories;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
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

	/**
	 * @see com.ibm.xtools.presentation.internal.view.AbstractView#initializeFromPreferences(org.eclipse.jface.preference.IPreferenceStore)
	 */
	protected void initializeFromPreferences(View view) {
		super.initializeFromPreferences(view);
		// support the diagram link mode
		if (!(view.getElement() instanceof Diagram)){
			IPreferenceStore store = (IPreferenceStore) getPreferencesHint().getPreferenceStore();
			// fill color
			RGB fillRGB =
				PreferenceConverter.getColor(
					store,
					IPreferenceConstants.PREF_NOTE_FILL_COLOR);
			ViewUtil.setStructuralFeatureValue(view,NotationPackage.eINSTANCE.getFillStyle_FillColor(), FigureUtilities.RGBToInteger(fillRGB));

			//line color
			RGB lineRGB =
				PreferenceConverter.getColor(
					store,
					IPreferenceConstants.PREF_NOTE_LINE_COLOR);
			ViewUtil.setStructuralFeatureValue(view,NotationPackage.eINSTANCE.getLineStyle_LineColor(), FigureUtilities.RGBToInteger(lineRGB));
		}	}
}
