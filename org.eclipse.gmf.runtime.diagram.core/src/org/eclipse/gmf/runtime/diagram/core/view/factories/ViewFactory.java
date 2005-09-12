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

package org.eclipse.gmf.runtime.diagram.core.view.factories;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.notation.View;


/**
 * Interface defining the basic View Factory APIs; a view factory is responsible for creating a view.
 * The <code>createView</code> will be called by the <code>ViewService<code> (using reflection) during
 * a view creation process. 
 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService
 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateViewOperation
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
