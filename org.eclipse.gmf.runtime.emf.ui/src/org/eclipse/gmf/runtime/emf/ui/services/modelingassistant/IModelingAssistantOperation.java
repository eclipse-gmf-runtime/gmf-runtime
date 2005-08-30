/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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

	/** Id for the GetTypesForAttribute operation */
	public final String GET_TYPES_FOR_ATTRIBUTE_ID = "GetTypesForAttribute"; //$NON-NLS-1$

	/** Id for the GetRelTypesOnSource operation */
	public final String GET_REL_TYPES_ON_SOURCE_ID = "GetRelTypesOnSource"; //$NON-NLS-1$

	/** Id for the GetRelTypesOnTarget operation */
	public final String GET_REL_TYPES_ON_TARGET_ID = "GetRelTypesOnTarget"; //$NON-NLS-1$

	/** Id for the GetRelTypesOnSourceAndTarget operation */
	public final String GET_REL_TYPES_ON_SOURCE_AND_TARGET_ID = "GetRelTypesOnSourceAndTarget"; //$NON-NLS-1$

	/** Id for the GetRelTypesForSREOnSource operation */
	public final String GET_REL_TYPES_FOR_SRE_ON_SOURCE_ID = "GetRelTypesForSREOnSource"; //$NON-NLS-1$

	/** Id for the GetRelTypesForSREOnTarget operation */
	public final String GET_REL_TYPES_FOR_SRE_ON_TARGET_ID = "GetRelTypesForSREOnTarget"; //$NON-NLS-1$

	/** Id for the GetTypesForSource operation */
	public final String GET_TYPES_FOR_SOURCE_ID = "GetTypesForSource"; //$NON-NLS-1$

	/** Id for the GetTypesForTarget operation */
	public final String GET_TYPES_FOR_TARGET_ID = "GetTypesForTarget"; //$NON-NLS-1$

	/** Id for the SelectExistingElementForSource operation */
	public final String SELECT_EXISTING_ELEMENT_FOR_SOURCE_ID = "SelectExistingElementForSource"; //$NON-NLS-1$	

	/** Id for the SelectExistingElementForTarget operation */
	public final String SELECT_EXISTING_ELEMENT_FOR_TARGET_ID = "SelectExistingElementForTarget"; //$NON-NLS-1$	

	/** Id for the GetTypesForActionBar operation */
	public final String GET_TYPES_FOR_ACTION_BAR_ID = "GetTypesForActionBar"; //$NON-NLS-1$

	/**
	 * Returns the id string to match that specified in the plugin.xml for
	 * deferred loading.
	 * @return the id string
	 */
	String getId();

	/**
	 * Returns the context object to match that specified in the plugin.xml for
	 * deferred loading.
	 * @return the context object
	 */
	IAdaptable getContext();

}
