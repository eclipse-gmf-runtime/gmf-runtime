/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core;

import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * The abstract parent of all drag and drop listener operations. This class
 * maintains a reference to the <code>IListenerContext</code>.
 * 
 * @author Vishy Ramaswamy
 */
public abstract class ListenerOperation
	implements IOperation {

	/**
	 * Attribute for the <code>IListenerContext</code>.
	 */
	private final IListenerContext context;

	/**
	 * Create a ListenerOperation.
	 * 
	 * @param aContext
	 *            the IListenerContext to keep a reference to
	 */
	public ListenerOperation(IListenerContext aContext) {
		assert null != aContext : "aContext cannot be null"; //$NON-NLS-1$
		this.context = aContext;
	}

	/**
	 * Returns the context.
	 * 
	 * @return The <code>context</code> instance variable
	 */
	public final IListenerContext getContext() {
		return context;
	}
}