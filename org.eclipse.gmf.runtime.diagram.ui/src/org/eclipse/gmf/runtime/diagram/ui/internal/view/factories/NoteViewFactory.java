/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.view.factories;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.View;

/**
 * The NoteView Factory class
 * 
 * @author mmostafa
 * @canBeSeenBy %level1
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
			   view.setType(Properties.NOTE);
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
			setPreferncePropertyValue(view,Properties.ID_FILLCOLOR, FigureUtilities.RGBToInteger(fillRGB));

			//line color
			RGB lineRGB =
				PreferenceConverter.getColor(
					store,
					IPreferenceConstants.PREF_NOTE_LINE_COLOR);
			setPreferncePropertyValue(view,Properties.ID_LINECOLOR, FigureUtilities.RGBToInteger(lineRGB));
		}	}
}
