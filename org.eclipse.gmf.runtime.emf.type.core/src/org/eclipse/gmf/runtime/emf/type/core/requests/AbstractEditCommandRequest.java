/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.requests;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;

/**
 * Abstract superclass for edit command requests.
 * 
 * @author ldamus
 */
public abstract class AbstractEditCommandRequest
	implements IEditCommandRequest {

	/**
	 * The edit command label. If <code>null</code>, the default command
	 * label will be used.
	 */
	private String label;
	
	/**
	 * My client context.
	 */
	private IClientContext clientContext;

	/**
	 * Arbitrary edit command parameters. Keyed on strings representing the name
	 * of the parameter. Value is any object representing the parameter value.
	 * <P>
	 * Used to pass additional information from the client to the edit helpers.
	 */
	private Map parameters = new HashMap();
	
	/**
	 * The editing domain in which I am requesting to make model changes.
	 */
	private TransactionalEditingDomain editingDomain;
	
	/**
	 * Initializes me with the editing domain in which I am requesting to make
	 * model changes.
	 * 
	 * @param editingDomain
	 *            the editing domain in which I am requesting to make model
	 *            changes.
	 */
	protected AbstractEditCommandRequest(TransactionalEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}
	
	// Documentation copied from the interface
	public TransactionalEditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * Gets the edit command label. If the label has not been specified, the
	 * default command label will be returned.
	 */
	public String getLabel() {
		if (label == null) {
			return getDefaultLabel();
		}
		return label;
	}

	/**
	 * Gets the default edit command label.
	 * 
	 * @return the default label
	 */
	protected String getDefaultLabel() {
		return StringStatics.BLANK;
	}

	/**
	 * Sets the edit command label. Once the edit command label has been set,
	 * the default command label will no longer be used.
	 * 
	 * @param label
	 *            the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditCommandRequest#getElementsToEdit()
	 */
	public List getElementsToEdit() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Gets the value of the parameter named <code>parameterName</code>.
	 * 
	 * @return the parameter value, or <code>null</code> if the parameter
	 *         value is not set.
	 */
	public Object getParameter(String parameterName) {
		return parameters.get(parameterName);
	}

	/**
	 * Sets the value of the parameter named <code>parameterName</code> to
	 * <code>value</code>.
	 * 
	 * @param parameterName
	 *            the parameter name
	 * @param value
	 *            the parameter value
	 */
	public void setParameter(String parameterName, Object value) {
		parameters.put(parameterName, value);
	}

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
	
	// documentation copied from the interface
	public void setClientContext(IClientContext clientContext) {
		this.clientContext = clientContext;
	}
	
	// documentation copied from the interface
	public IClientContext getClientContext() {
		return clientContext;
	}

}