/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.util;

import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;

/**
 * A proxy object that handles exceptions.
 * 
 * @author khussey
 */
public abstract class Proxy {

	/**
	 * The real object for this proxy.
	 */
	private Object realObject;

	/**
	 * Constructs a new proxy for the specified object.
	 * 
	 * @param realObject
	 *            The obect for which to create a proxy.
	 */
	protected Proxy(Object realObject) {

		super();

		assert null != realObject : "null argument passed to proxy"; //$NON-NLS-1$

		this.realObject = realObject;
	}

	/**
	 * Retrieves the value of the <code>realObject</code> instance variable.
	 * 
	 * @return The value of the <code>realObject</code> instance variable.
	 */
	public Object getRealObject() {
		return realObject;
	}

	/**
	 * Sets the value of the <code>realObject</code> instance variable.
	 * 
	 * @param realObject
	 *            The obect for which to create a proxy.
	 */
	public void setRealObject(Object realObject) {

		assert null != realObject : "null argument passed setRealObject"; //$NON-NLS-1$

		this.realObject = realObject;
	}

	/**
	 * Handles the specified exception.
	 * 
	 * @param exception
	 *            The exception to be handled
	 */
	protected void handle(Exception exception) {
		Trace.catching(CommonCorePlugin.getDefault(),
			CommonCoreDebugOptions.EXCEPTIONS_CATCHING, getClass(),
			"handle", exception); //$NON-NLS-1$
		RuntimeException cre = new RuntimeException(exception);
		Trace.throwing(CommonCorePlugin.getDefault(),
			CommonCoreDebugOptions.EXCEPTIONS_THROWING, getClass(),
			"handle", cre); //$NON-NLS-1$
		throw cre;
	}

	/**
	 * Retrieves a hash code value for this proxy. This method is supported for
	 * the benefit of hashtables such as those provided by
	 * <code>java.util.Hashtable</code>.
	 * 
	 * @return A hash code value for this proxy.
	 * 
	 * @see Object#hashCode()
	 */
	public final int hashCode() {
		return getRealObject().hashCode();
	}

	/**
	 * Indicates whether some other proxy is "equal to" this proxy.
	 * 
	 * @return <code>true</code> if this proxy is the same as the proxy
	 *         argument; <code>false</code> otherwise.
	 * @param proxy
	 *            The reference proxy with which to compare.
	 */
	private boolean equals(Proxy proxy) {
		return null == getRealObject() ? null == proxy.getRealObject()
			: getRealObject().equals(proxy.getRealObject());
	}

	/**
	 * Indicates whether some other object is "equal to" this proxy.
	 * 
	 * @return <code>true</code> if this proxy is the same as the object
	 *         argument; <code>false</code> otherwise.
	 * @param object
	 *            The reference object with which to compare.
	 * 
	 * @see Object#equals(Object)
	 */
	public final boolean equals(Object object) {
		return object instanceof Proxy && equals((Proxy) object);
	}
}
