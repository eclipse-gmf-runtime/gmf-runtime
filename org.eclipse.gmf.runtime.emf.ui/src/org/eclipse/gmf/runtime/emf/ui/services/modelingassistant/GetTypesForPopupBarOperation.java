/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The operation used with the modeling assistant service that gets the type of
 * elements that can be created from an action bar.
 * 
 * @author cmahoney
 */
public class GetTypesForPopupBarOperation
	implements IModelingAssistantOperation {

	/** the host: adapts to editpart, view, element, etc. */
	private final IAdaptable host;

	/**
	 * Creates a new <code>GetTypesForPopupBarOperation</code>.
	 * 
	 * @param host
	 *            the host: adapts to editpart, view, element, etc.
	 */
	protected GetTypesForPopupBarOperation(IAdaptable host) {
		this.host = host;
	}

	/* (non-Javadoc)
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantOperation#getId()
	 */
	public String getId() {
		return GET_TYPES_FOR_POPUP_BAR_ID;
	}

	/**
	 * Returns the host. This is what will be used to assist in delayed loading
	 * of a modeling assistant provider.
	 */
	public IAdaptable getContext() {
		return getHost();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IModelingAssistantProvider) provider)
			.getTypesForPopupBar(getHost());
	}

	/**
	 * @return Returns the host that is to have an action bar displayed on it.
	 */
	public final IAdaptable getHost() {
		return host;
	}

}