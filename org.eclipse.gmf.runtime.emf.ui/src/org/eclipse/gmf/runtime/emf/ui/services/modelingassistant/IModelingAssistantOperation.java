/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.services.modelingassistant;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * Interface for all Modeling Assistant operations. Each operation must provide
 * an id string that is used in the plugin.xml for deferred loading. The ids are
 * defined here so it is easier to look them up. Each operation must all specify
 * a context object. The context object is an adaptable that adapts to the
 * editpart, view, or element.
 * 
 * @author cmahoney
 */
public interface IModelingAssistantOperation
	extends IOperation {

	/** Id for the GetTypes operation */
	final String GET_TYPES_ID = "GetTypes"; //$NON-NLS-1$

	/** Id for the GetRelTypesOnSource operation */
	final String GET_REL_TYPES_ON_SOURCE_ID = "GetRelTypesOnSource"; //$NON-NLS-1$

	/** Id for the GetRelTypesOnTarget operation */
	final String GET_REL_TYPES_ON_TARGET_ID = "GetRelTypesOnTarget"; //$NON-NLS-1$

	/** Id for the GetRelTypesOnSourceAndTarget operation */
	final String GET_REL_TYPES_ON_SOURCE_AND_TARGET_ID = "GetRelTypesOnSourceAndTarget"; //$NON-NLS-1$

	/** Id for the GetRelTypesForSREOnSource operation */
	final String GET_REL_TYPES_FOR_SRE_ON_SOURCE_ID = "GetRelTypesForSREOnSource"; //$NON-NLS-1$

	/** Id for the GetRelTypesForSREOnTarget operation */
	final String GET_REL_TYPES_FOR_SRE_ON_TARGET_ID = "GetRelTypesForSREOnTarget"; //$NON-NLS-1$

	/** Id for the GetTypesForSource operation */
	final String GET_TYPES_FOR_SOURCE_ID = "GetTypesForSource"; //$NON-NLS-1$

	/** Id for the GetTypesForTarget operation */
	final String GET_TYPES_FOR_TARGET_ID = "GetTypesForTarget"; //$NON-NLS-1$

	/** Id for the SelectExistingElementForSource operation */
	final String SELECT_EXISTING_ELEMENT_FOR_SOURCE_ID = "SelectExistingElementForSource"; //$NON-NLS-1$	

	/** Id for the SelectExistingElementForTarget operation */
	final String SELECT_EXISTING_ELEMENT_FOR_TARGET_ID = "SelectExistingElementForTarget"; //$NON-NLS-1$	

	/** Id for the GetTypesForPopupBar operation */
	final String GET_TYPES_FOR_POPUP_BAR_ID = "GetTypesForPopupBar"; //$NON-NLS-1$

	/**
	 * Returns the id string to match that specified in the plugin.xml for
	 * deferred loading.
	 * 
	 * @return the id string
	 */
	String getId();

	/**
	 * Returns the context object to match that specified in the plugin.xml for
	 * deferred loading.
	 * 
	 * @return the context object
	 */
	IAdaptable getContext();

}
