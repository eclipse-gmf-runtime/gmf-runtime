/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.domain;

import java.util.List;

import org.eclipse.gmf.runtime.emf.core.IOperationEvent;

/**
 * Implementation of the public API IOperationEvent interface.
 * 
 * @author rafikj
 */
public class MSLOperationEvent
	implements IOperationEvent {

	private List notifications = null;

	/**
	 * Constructor.
	 */
	public MSLOperationEvent(List notifications) {
		super();
		this.notifications = notifications;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.IOperationEvent#getNotifications()
	 */
	public List getNotifications() {
		return notifications;
	}
}