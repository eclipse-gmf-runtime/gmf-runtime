/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.listener;

import java.beans.PropertyChangeListener;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * An object that allows listeners to listen and stop listening to events related
 * to a given model element 
 * 
 * @author melaasar
 */
public final class PropertyChangeNotifier {

	private EObject element;
	private EStructuralFeature feature = null;

	/**
	 * Create an instance.
	 * @param element element to listen to
	 */
	public PropertyChangeNotifier(EObject element) {
		this.element = element;
	}
	
	/**
	 * Create an instance.
	 * @param element element to listen to
	 * @param feature the feature interested in; if this parameter is null, it means it is 
	 * 				  interested in all features
	 */
	public PropertyChangeNotifier(EObject element ,EStructuralFeature feature){
		this.element = element;
		this.feature = feature;
	}
		
	/**
	 * Registers the supplied listener with the model server listener to
	 * receives events fires againts the cached element.
	 * @param listener the Listener to add 
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (feature==null)
			PresentationListener.getInstance().addPropertyChangeListener( element, listener);
		else
			PresentationListener.getInstance().addPropertyChangeListener(element,feature,listener);
	}

	/**
	 * Unregisters the supplied listener from the model server.
	 * @param listener the Listener to remove 
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (feature==null)
			PresentationListener.getInstance().removePropertyChangeListener(element, listener);
		else
			PresentationListener.getInstance().removePropertyChangeListener(element, feature, listener);
			
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof PropertyChangeNotifier))
			return false;
		return ((PropertyChangeNotifier) obj).element == element;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return element.hashCode();
	}
}

