/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.view;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.diagram.core.listener.PropertyChangeNotifier;
import org.eclipse.gmf.runtime.notation.Style;

/**
 * A facade inteface for the View interface
 * @deprecated View Facades are deprectaed and will be removed soon; use the 
 * notation view instead; you can reach it by calling getModel on a view EditPart
 * or by using the view service to create a new view
 * @author melaasar
 */
public interface IView extends IAdaptable {
	
	/** the append index */
	public static final int APPEND = -1;

	/**
	 * Method destroy. detaches and destroys the view
	 */
	public void destroy();
	
	/**
	 * Method getContainerView. retrieves the direct container
	 * 
	 * @return IContainerView the container view or null for a diagram view
	 */
	public IContainerView getContainerView();
	
	/**
	 * Method getDiagramView. retrieves the container diagram view (the root
	 * view)
	 * 
	 * @return DiagramView
	 */
	public IDiagramView getDiagramView();
	
	/**
	 * Method getIdStr. returns the unique GUID of the view
	 * 
	 * @return String the GUID of a view (constant)
	 */
	public String getIdStr();

	/**
	 * Retrieves the view's property change notifier object
	 * 
	 * @return IPropertyChangeNotifier
	 */
	public PropertyChangeNotifier getPropertyChangeNotifier();
	
	/**
	 * Returns the value of the property with the given id
	 * 
	 * @param id
	 *            the id of the property being set
	 * @return the value of the property, or <code>null</code>
	 */
	public Object getPropertyValue(Object id);
	
	/**
	 * Method getViewModel. Returns the <code>Resource</code> associated
	 * with this view used to copy the children views on to the system
	 * clipboard
	 * 
	 * @return Resource
	 */
	public Resource getResource();
	
	/**
	 * Return the view's semantic element which might be a proxy element
	 * @return the semantic element
	 * @see #resolveSemanticElement() if you want a resolved element or null returned
	 */
	public EObject getSemanticElement();

	/**
	 * Returns the class id of the referenced semantic element
	 * @return The class id of the referenced semantic element
	 */
	public String getSemanticElementClassId();

	/**
	 * Retrieves the view's semantic type
	 * 
	 * @return The semantic type string
	 */
	public String getSemanticType();

	/**
	 * Gets the connectable view's source connectios  
	 * gets all the connectors whose source is this view
	 * @return List
	 */
	public List getSourceConnections();
	
	public Style getStyle(EClass eClass);
	
	/**
	 * Gets the connectable view's target connectios  
	 * gets all the connectors whose target is this view
	 * @return List
	 */
	public List getTargetConnections();

	/**
	 * Method isDetached. checks if the view is in the detached state
	 * 
	 * @return boolean
	 */
	public boolean isDetached();
	
	/**
	 * Method isPropertySupported. checks if the passed property is supported
	 * by this view
	 * 
	 * @param id
	 * @return boolean
	 */
	public boolean isPropertySupported(Object id);
	
	/**
	 * Method isVisible. gets the view's visibility state
	 * @return boolean
	 */
	public boolean isVisible();


	/** Return the view's underlying semantic element; resolving it if it is a proxy element. */
	public EObject resolveSemanticElement();
	
	/**
	 * Sets the property with the given id if possible.
	 * 
	 * @param id
	 *            the id of the property being set
	 * @param value
	 *            the value of the property being set
	 */
	public void setPropertyValue(Object id, Object value);
	
	/**
	 * Methos setSemanticElement. Sets the view's semantic element
	 * @param element The view's semantic element
	 */
	public void setSemanticElement(EObject element);

	/**
	 * Sets the view's semantic type
	 * 
	 * @param type The semantic type
	 */
	public void setSemanticType(String type);

	/**
	 * Sets the view's visibility state
	 * @param visible The view's new visibility state
	 */
	public void setVisible(boolean visible);

}
