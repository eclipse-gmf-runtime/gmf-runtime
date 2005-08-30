/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.view.factories;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author mmostafa
 */
public interface ViewFactory {
	
	/**
	 * factory method, that will be called by the view service to creat the view
	 * 
	 * @param semanticAdapter
	 *            semanitc element of the view, it can be null
	 * @param containerView
	 *            the view to contain the connector
	 * @param semanticHint
	 *            a semantic hint to reflect the view type, it can be empty
	 * @param index
	 *            position with parent's child collection
	 * @param persisted
	 *            persisted flag, this will indicate if the created view will be
	 *            a presisted or transient view, transient views never get
	 *            serialized
	 * @param preferenceStoreID
	 *            the ID mapped to the preference store to be used when
	 *            initializing the view's properties
	 * @return the new view
	 */
	public View createView(final IAdaptable semanticAdapter,
						   final View containerView,
						   final String semanticHint,final int index,
						   final boolean persisted, final PreferencesHint preferencesHint);
	
}
