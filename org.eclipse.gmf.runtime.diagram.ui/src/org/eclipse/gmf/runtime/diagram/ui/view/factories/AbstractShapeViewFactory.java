/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.view.factories; 

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import com.ibm.xtools.notation.LayoutConstraint;
import com.ibm.xtools.notation.NotationFactory;
import com.ibm.xtools.notation.View;
/**
 * The base abstract node view factory 
 * @see  org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractNodeViewFactory
 * @author mmostafa
 */
public class AbstractShapeViewFactory extends AbstractNodeViewFactory {

	/**
	 * Method used to create the layout constraint that will get set on the 
	 * created view. You can override this method in your own factory to change
	 * the default constraint. This method is called by @link #createView(IAdaptable, View, String, int, boolean) 
	 * @return a new layout constraint for the view
	 */
	protected LayoutConstraint createLayoutConstraint() {
		return NotationFactory.eINSTANCE.createBounds();
	}
	
	/**
	 * Initialize the newly created view from the preference store, this
	 * method get called by @link #decorateView(View, IAdaptable, String)
	 * @param view the view to initialize
	 */
	protected void initializeFromPreferences(View view) {
		super.initializeFromPreferences(view);

		IPreferenceStore store = (IPreferenceStore) getPreferencesHint().getPreferenceStore();

		// fill color
		RGB fillRGB = PreferenceConverter.getColor(store,
			IPreferenceConstants.PREF_FILL_COLOR);
		setPreferncePropertyValue(view, Properties.ID_FILLCOLOR,
			FigureUtilities.RGBToInteger(fillRGB));
	}
	
	/**
	 * this method is called by @link #createView(IAdaptable, View, String, int, boolean) to 
	 * create the styles for the view that will be created, you can override this 
	 * method in you factory sub class to provide additional styles
	 * @return a list of style for the newly created view or an empty list if none (do not return null)
	 */
	protected List createStyles() {
		List styles = new ArrayList();
		styles.add(NotationFactory.eINSTANCE.createShapeStyle());
		return styles;
	}
}