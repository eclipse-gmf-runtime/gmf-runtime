/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.ui.services.modelingassistant;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The operation used with the modeling assistant service that gets the type of
 * elements that can be created from an action bar.
 * 
 * @author cmahoney
 */
public class GetTypesForActionBarOperation
	implements IModelingAssistantOperation {

	/** the host: adapts to editpart, view, element, etc. */
	private final IAdaptable host;

	/**
	 * Creates a new <code>GetTypesForActionBarOperation</code>.
	 * 
	 * @param host
	 *            the host: adapts to editpart, view, element, etc.
	 */
	protected GetTypesForActionBarOperation(IAdaptable host) {
		this.host = host;
	}

	/**
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantOperation#getId()
	 */
	public String getId() {
		return GET_TYPES_FOR_ACTION_BAR_ID;
	}

	/**
	 * Returns the host. This is what will be used to assist in delayed loading
	 * of a modeling assistant provider.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantOperation#getContext()
	 */
	public IAdaptable getContext() {
		return getHost();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IModelingAssistantProvider) provider)
			.getTypesForActionBar(getHost());
	}

	/**
	 * @return Returns the host that is to have an action bar displayed on it.
	 */
	public final IAdaptable getHost() {
		return host;
	}

}