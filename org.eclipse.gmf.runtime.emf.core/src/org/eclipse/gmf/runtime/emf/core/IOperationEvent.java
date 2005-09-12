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

package org.eclipse.gmf.runtime.emf.core;

import java.util.List;


/**
 * An event that describes the notifications that are batched when an API executes.
 * <p>
 * API clients should <b>not </b> implement this interface.
 * </p>
 * {@link ResourceSetOperation}or during its undo execution.
 * 
 * @see OperationListener
 */
public interface IOperationEvent {

	/**
	 * The list of notifications that were sent while performing or undoing the
	 * operation. Each item in the list is an instance of
	 * {@link org.eclipse.emf.common.notify.Notification Notification} or one of
	 * its derived classes.
	 * {@link org.eclipse.emf.common.notify.Notification#getEventType Notification.getEventType}
	 * should be used to determine the exact nature of the change this
	 * notification represents. Possible event types are exposed in
	 * {@link org.eclipse.gmf.runtime.emf.core.EventTypes EventTypes}.
	 * @return The list of notifications
	 */
	public List getNotifications();
}