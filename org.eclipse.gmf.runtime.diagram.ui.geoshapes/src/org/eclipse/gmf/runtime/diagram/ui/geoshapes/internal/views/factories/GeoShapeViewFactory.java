/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.views.factories;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.TextShapeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.View;

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
		super.initializeFromPreferences(view);

		IPreferenceStore store = (IPreferenceStore) getPreferencesHint().getPreferenceStore();

		// fill color
		RGB fillRGB = PreferenceConverter.getColor(store,
			IPreferenceConstants.PREF_NOTE_FILL_COLOR);
		ViewUtil.setPropertyValue(view, Properties.ID_FILLCOLOR,
			FigureUtilities.RGBToInteger(fillRGB));

		//line color
		RGB lineRGB = PreferenceConverter.getColor(store,
			IPreferenceConstants.PREF_NOTE_LINE_COLOR);
		ViewUtil.setPropertyValue(view, Properties.ID_LINECOLOR,
			FigureUtilities.RGBToInteger(lineRGB));
	}
}