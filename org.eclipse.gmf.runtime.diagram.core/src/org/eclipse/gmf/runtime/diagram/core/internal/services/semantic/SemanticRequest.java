/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;



/**
 * The abstract semantic request
 * 
 * @author melaasar
 * 
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest}s
 *             to get commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 *  
 */
public abstract class SemanticRequest {

	/** the request type */
	private final Object requestType;
	
	/**
	 * Arbitrary edit command parameters. Keyed on strings representing the name
	 * of the parameter. Value is any object representing the parameter value.
	 * <P>
	 * Used to pass additional information from the client to the edit helpers.
	 */
	private Map parameters = new HashMap();
	
	/**
	 * Creates a new semantic request
	 * @param requestType
	 * @param modelOperation
	 */
	public SemanticRequest(
		Object requestType) {
		this.requestType = requestType;
	}

	/**
	 * Returns the requestType.
	 * @return Object
	 */
	public Object getRequestType() {
		return requestType;
	}

	/**
	 * Gets the context of the request
	 * 
	 * @return The request's context object
	 */
	public abstract Object getContext();
	


	/**
	 * Adds of the parameter values in <code>newParameters</code> to this
	 * request.
	 * 
	 * @param newParameters
	 *            the parameters to add
	 */
	public void addParameters(Map newParameters) {
		if (newParameters != null) {
			parameters.putAll(newParameters);
		}
	}

	/**
	 * Gets the parameters associated with this request.
	 * 
	 * @return the map of parameter values, keyed on parameter name
	 */
	public Map getParameters() {
		return parameters;
	}

}
