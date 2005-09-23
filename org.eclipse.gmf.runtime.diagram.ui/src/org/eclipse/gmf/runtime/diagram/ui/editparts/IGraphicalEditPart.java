/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author melaasar
 *
 * The interface of all editparts with IView as a model
 */
public interface IGraphicalEditPart
	extends org.eclipse.gef.GraphicalEditPart, IEditableEditPart {

	/**
	 * Return the editpart's associated Notation View.
	 * @return <code>View</code>, the associated view or null if there is no associated Notation View
	 */
	public View getNotationView();

	/**
	 * Gets the associated DiagramEditDomain to the edit part
	 * @return IDiagramEditDomain
	 */
	public IDiagramEditDomain getDiagramEditDomain();

	/**
	 * Returns the first child inside this edit part that owns a view
	 * with the same type as the passed hint
	 * @param semanticHint
	 * @return IGraphicalEditPart
	 */
	public IGraphicalEditPart getChildBySemanticHint(String semanticHint);

	/**
	 * Method getTopGraphicEditPart.
	 * @return TopGraphicEditPart
	 */
	public TopGraphicEditPart getTopGraphicEditPart();

	/**
	 * Method fillAppearancePropertiesMap.
	 * @param properties
	 */
	public void fillAppearancePropertiesMap(Map properties);

	/**
	 * Convenience method to retreive the value for the supplied poperty
	 * from the editpart's associated view element.
	 * @param id the property id
	 * @return Object the value
	 * @deprecated use {@link #getStructuralFeatureValue(EStructuralFeature)} instead
	 */
	public Object getPropertyValue(Object id);
	
	/**
	 * Convenience method to retreive the value for the supplied feature
	 * from the editpart's associated view element.
	 * @param feature the feature to use
	 * @return Object the value
	 */
	public Object getStructuralFeatureValue(EStructuralFeature id);

	/**
	 * Convenience method to set a property value.
	 * @param id
	 * @param value
	 * @deprecated use {@link #setStructuralFeatureValue(Object, Object)} instead
	 */
	public void setPropertyValue(Object id, Object value);
	
	/**
	 * Convenience method to set a feature value.
	 * @param feature
	 * @param value
	 */
	public void setStructuralFeatureValue(EStructuralFeature feature, Object value);

	/**
	 * Method getAppearancePropertiesMap.
	 * @return Map
	 */
	public Map getAppearancePropertiesMap();
	
	/**
	 * finds an editpart given a starting editpart and an EObject
	 * @param epBegin starting edit part
	 * @param theElement	eObject to use
	 * @return the found edit part if there is any
	 */
	public EditPart findEditPart(EditPart epBegin, EObject theElement);
	
	/**
	 * Convenience method returns the editpart's primary view.
	 * @return the primary view 
	 */
	public View getPrimaryView();
	
	/**
	 * this method will return the primary child view inside this edit part
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart();
	
	/**
	 * Gets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @return the preferences hint
	 */
	public PreferencesHint getDiagramPreferencesHint();
	
	/**
	 * Gets the semantic element associated to this editpart's view.
	 * @return the semantic element or <code>null</code> if the semantic element was
	 * <code>null</code> or unresolvable 
	 */
	public EObject resolveSemanticElement();
}
