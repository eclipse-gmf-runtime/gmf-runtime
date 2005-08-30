/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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