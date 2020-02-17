/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Wraps an edit command request in a GEF request.
 * 
 * @author ldamus
 */
public class EditCommandRequestWrapper
	extends Request {

	/**
	 * The edit command request that is wrapped.
	 */
	private IEditCommandRequest editCommandRequest;

	/**
	 * Constructs a new request wrapper.
	 * 
	 * @param editCommandRequest
	 *            the edit command request to be wrapped. Must not be
	 *            <code>null</code>.
	 */
	public EditCommandRequestWrapper(IEditCommandRequest editCommandRequest) {
		this(RequestConstants.REQ_SEMANTIC_WRAPPER, editCommandRequest, null);
	}

	/**
	 * Constructs a new request wrapper.
	 * 
	 * @param editCommandRequest
	 *            the edit command request to be wrapped. Must not be
	 *            <code>null</code>.
	 * @param requestParameters
	 *            custom request parameters
	 */
	public EditCommandRequestWrapper(IEditCommandRequest editCommandRequest, Map requestParameters) {

		this(RequestConstants.REQ_SEMANTIC_WRAPPER, editCommandRequest, requestParameters);
	}
	
	/**
	 * Constructs a new request wrapper.
	 * 
	 * @param requestType
	 *            the request type
	 * @param editCommandRequest
	 *            the edit command request to be wrapped. Must not be
	 *            <code>null</code>.
	 * @param requestParameters
	 *            custom request parameters
	 */
	public EditCommandRequestWrapper(Object requestType,
			IEditCommandRequest editCommandRequest) {

		this(requestType, editCommandRequest, null);
	}
	
	/**
	 * Constructs a new request wrapper.
	 * 
	 * @param requestType
	 *            the request type
	 * @param editCommandRequest
	 *            the edit command request to be wrapped. Must not be
	 *            <code>null</code>.
	 * @param requestParameters
	 *            custom request parameters
	 */
	public EditCommandRequestWrapper(Object requestType,
			IEditCommandRequest editCommandRequest, Map requestParameters) {

		setType(requestType);
		Assert.isNotNull(editCommandRequest);
		this.editCommandRequest = editCommandRequest;
		editCommandRequest.addParameters(requestParameters);
	}

	/**
	 * Gets the edit command request.
	 * 
	 * @return the edit command request
	 */
	public IEditCommandRequest getEditCommandRequest() {
		return editCommandRequest;
	}
	
	/**
	 * Sets the parameters on the wrapped request.
	 */
	public void setExtendedData(Map map) {
		IEditCommandRequest delegate = getEditCommandRequest();
		
		if (delegate != null) {
			delegate.getParameters().clear();
			delegate.addParameters(map);
		}
	}
	
	/**
	 * Gets the parameters from the wrapped request.
	 */
	public Map getExtendedData() {
		IEditCommandRequest delegate = getEditCommandRequest();
		
		if (delegate != null) {
			return delegate.getParameters();
		}
		
		return super.getExtendedData();
	}
}