/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.view.factories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService;
import org.eclipse.gmf.runtime.diagram.core.internal.view.factories.ViewFactory;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author mmostafa
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
abstract public class AbstractViewFactory implements ViewFactory {

	/**
	 * The hint used to find the appropriate preference store from which general
	 * diagramming preference values for properties of shapes, connectors, and
	 * diagrams can be retrieved. This hint is mapped to a preference store in
	 * the {@link DiagramPreferencesRegistry}.
	 */
	private PreferencesHint preferencesHint;

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.factories.ViewFactory#createView(org.eclipse.core.runtime.IAdaptable, org.eclipse.gmf.runtime.notation.View, java.lang.String, int, boolean, java.lang.String)
	 */
	abstract public View createView(final IAdaptable semanticAdapter,
			final View containerView, final String semanticHint,
			final int index, final boolean persisted, final PreferencesHint thePreferencesHint);

	/**
	 * @return a list of style for the newly created view or an empty list if none (do not return null)
	 */
	protected List createStyles() {
		return new ArrayList();
	}
	
	/**
	 * indicates if the view requires an element inside it or it can use
	 * its container's element
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @return
	 */
	protected boolean requiresElement(IAdaptable semanticAdapter, View containerView) {
		EObject semanticElement = null;
		if (semanticAdapter!=null){
			semanticElement = (EObject)semanticAdapter.getAdapter(EObject.class);
			return requiresElement(semanticElement,containerView);
		}
		return true;
	}
	
	/**
	 * indicates if the view requires an element inside it or it can use
	 * its container's element
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @return
	 */
	protected boolean requiresElement(EObject semanticElement, View containerView) {
		EObject containerSemanticElement = containerView.getElement();
		if (containerSemanticElement==semanticElement)
			return false;
		return true;
	}
	
	/**
	 * Initialize the newly created view from the preference store
	 * @param view the view to initialize
	 */
	protected void initializeFromPreferences(View view) {
		
		IPreferenceStore store = (IPreferenceStore) getPreferencesHint().getPreferenceStore();
		if (store == null) {
			return;
		}

		// line color
		RGB lineRGB =
			PreferenceConverter.getColor(
				store,
				IPreferenceConstants.PREF_LINE_COLOR);
		setPreferncePropertyValue(view,Properties.ID_LINECOLOR, FigureUtilities.RGBToInteger(lineRGB));

		//default font
		FontData fontData =
			PreferenceConverter.getFontData(
				store,
				IPreferenceConstants.PREF_DEFAULT_FONT);
		setPreferncePropertyValue(view,Properties.ID_FONTNAME, fontData.getName());
		setPreferncePropertyValue(view,Properties.ID_FONTSIZE, new Integer(fontData.getHeight()));
		setPreferncePropertyValue(view,Properties.ID_FONTBOLD, Boolean.valueOf((fontData.getStyle() & SWT.BOLD) != 0));
		setPreferncePropertyValue(view,Properties.ID_FONTITALIC, Boolean.valueOf((fontData.getStyle() & SWT.ITALIC) != 0));

		//font color
		RGB fontRGB =
			PreferenceConverter.getColor(
				store,
				IPreferenceConstants.PREF_FONT_COLOR);
		setPreferncePropertyValue(view,Properties.ID_FONTCOLOR, FigureUtilities.RGBToInteger(fontRGB));
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.util.ViewUtil#isPropertySupported(View, Object)
	 */
	public final boolean isPropertySupported(View view,Object id) {
		return ViewUtil.isPropertySupported(view, id);
	}
	
	/**
	 * A utility method to set a property value from the preference
	 * "only" if it was found to be supported by the view. This is needed
	 * since some derived views could decide to shadow certain properties
	 * @param id the preference property id
	 * @param value the preference property value
	 */
	protected final void setPreferncePropertyValue(View view,
												   Object id,
												   Object value) {
		if (isPropertySupported(view,id))
			setPropertyValue(view,id, value);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.util.ViewUtil#isPropertySupported(Object)
	 */
	protected boolean isPropertySupported(View view,
										  EStructuralFeature feature,
										  EClass featureClass) {
		return ViewUtil.isPropertySupported(view, feature, featureClass);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.util.ViewUtil#setPropertyValue(View, Object, Object)
	 */
	public final void setPropertyValue(View view,Object id, Object value) {
		ViewUtil.setPropertyValue(view, id, value);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.util.ViewUtil#setPropertyValue(View, EStructuralFeature, EClass, Object)
	 */
	protected void setPropertyValue(View view,EStructuralFeature feature, EClass featureClass, Object value) {
		ViewUtil.setPropertyValue(view,feature, featureClass, value);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.util.ViewUtil#getPropertyValue(View, Object)
	 */
	public final Object getPropertyValue(View view,Object id) {
		return ViewUtil.getPropertyValue(view, id);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.util.ViewUtil#getPropertyValue(View, EStructuralFeature, EClass)
	 */
	protected Object getPropertyValue(View view,EStructuralFeature feature, EClass featureClass) {
		return ViewUtil.getPropertyValue(view, feature, featureClass);
	}
	
	/**
	 * Method getViewService.
	 * a utility function to return the view service instance
	 * @return ViewService
	 */
	protected static ViewService getViewService() {
		return ViewService.getInstance();
	}
	
	/**
	 * Gets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @return the preferences hint
	 */
	protected PreferencesHint getPreferencesHint() {
		return preferencesHint;
	}
	
	/**
	 * Sets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @param preferencesHint the preferences hint
	 */
	protected void setPreferencesHint(PreferencesHint preferencesHint) {
		this.preferencesHint = preferencesHint;
	}
}
