/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.jface.util.Assert;

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
}