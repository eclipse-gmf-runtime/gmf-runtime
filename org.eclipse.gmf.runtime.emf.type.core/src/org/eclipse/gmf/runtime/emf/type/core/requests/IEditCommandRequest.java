/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.requests;

import java.util.List;
import java.util.Map;


/**
 * Request for a command to edit a model element. These requests are passed to
 * <code>IEditHelpers</code> to obtain commands that will execute the edit
 * behaviour.
 * <P>
 * Clients should not implement this interface directly, but should subclass
 * {@link org.eclipse.gmf.runtime.emf.type.core.requests.AbstractEditCommandRequest}instead.
 * 
 * @author ldamus
 */
public interface IEditCommandRequest {

	/**
	 * Gets the request label.
	 * 
	 * @return the request label
	 */
	public abstract String getLabel();

	/**
	 * Gets the edit helper context for this request. The context can be an
	 * <code>IElementType</code> or an <code>EObject</code>. It determines
	 * which edit helper should be used to find a command to do the work in the
	 * request.
	 * 
	 * @return the edit helper context for this request
	 */
	public abstract Object getEditHelperContext();

	/**
	 * Gets the elements that will be changed when the work is done for this
	 * request.
	 * 
	 * @return the elements that will be edited
	 */
	public abstract List getElementsToEdit();
	
	/**
	 * Gets the request parameters keyed on parameter name. Each value is the
	 * parameter value.
	 * 
	 * @return the request parameters
	 */
	public abstract Map getParameters();
	
	/**
	 * Sets the request parameter named <code>name</code> to
	 * <code>value</code>.
	 * 
	 * @param name
	 *            the parameter name
	 * @param value
	 *            the parameter value
	 */
	public abstract void setParameter(String name, Object value);
	
	/**
	 * Gets the value for the request parameter named <code>name</code>.
	 * 
	 * @param name
	 *            the parameter name
	 * @return the parameter value
	 */
	public abstract Object getParameter(String name);
	
	/**
	 * Adds <code>newParameters</code> to this request.
	 * 
	 * @param newParameters
	 *            the parameters to be added
	 */
	public abstract void addParameters(Map newParameters);
	
}