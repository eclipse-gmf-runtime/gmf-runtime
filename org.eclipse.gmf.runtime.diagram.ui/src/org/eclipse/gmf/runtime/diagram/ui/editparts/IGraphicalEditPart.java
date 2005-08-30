/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;

import org.eclipse.gmf.runtime.diagram.core.internal.view.IView;
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
	 * Method getView.
	 * @return IView
	 * @deprecated use getNotationView instead
	 */
	public IView getView();
	
	/**
	 * Return the editpart's associated Notation View.
	 * @return <code>View</code>, the associated view or null if there is no associated Notation View
	 */
	public View getNotationView();

	/**
	 * Method getDiagramEditDomain.
	 * @return IDiagramEditDomain
	 */
	public IDiagramEditDomain getDiagramEditDomain();

	/**
	 * Method getChildBySemanticHint.
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
	 * Convenience method to retreive the value for the supplied value
	 * from the editpart's associated view element.
	  * @param id the property id
	 * @return Object the value
	 */
	public Object getPropertyValue(Object id);

	/**
	 * Method setPropertyValue.
	 * @param id
	 * @param value
	 */
	public void setPropertyValue(Object id, Object value);

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
	 * Convenience method returns the editpart's parimary view.
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
}
