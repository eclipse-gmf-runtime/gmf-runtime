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

package org.eclipse.gmf.runtime.emf.type.core.requests;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.transaction.TransactionalEditingDomain;


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
	 * Name of the request parameter used to indicate that the default edit
	 * command should not be considered. Edit advice will completely replace the
	 * default edit behaviour if the parameter value is <code>Boolean.TRUE</code>.
	 */
	public static String REPLACE_DEFAULT_COMMAND = "IEditCommandRequest.replaceDefaultCommand"; //$NON-NLS-1$

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
	
	/**
	 * Gets the editing domain in which I am requesting to make model changes.
	 * 
	 * @return the editing domain
	 */
	public abstract TransactionalEditingDomain getEditingDomain();
	
}