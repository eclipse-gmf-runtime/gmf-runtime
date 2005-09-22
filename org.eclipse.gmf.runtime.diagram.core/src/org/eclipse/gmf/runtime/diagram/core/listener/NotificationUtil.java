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


package org.eclipse.gmf.runtime.diagram.core.listener;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gmf.runtime.emf.core.EventTypes;

/**
 * Utility class that implements few helper methods around the EMF
 * <code>Notification</code> object
 * 
 * @author mmostafa
 */
public class NotificationUtil {
	
	/**
	 * checks if the event resulted in a slot modification (set/unset event)
	 * @return <tt>true</tt> if the event type is
	 * one of {@link EventTypes#SET} or {@link EventTypes#UNSET};
	 * otherwise <tt>false</tt>.
	 */
	public static boolean isSlotModified(Notification notification) {
		return notification.getEventType() == EventTypes.SET || 
			   notification.getEventType() == EventTypes.UNSET;
	}
	
	/**
	 * checks if the event resulted in adding an element
	 * @return <tt>true</tt> if the event type is
	 * one of {@link EventTypes#ADD} or {@link EventTypes#ADD_MANY};
	 * otherwise <tt>false</tt>.
	 */
	public static boolean isElementAddedToSlot(Notification notification) {
		return notification.getEventType() == EventTypes.ADD ||
			   notification.getEventType() == EventTypes.ADD_MANY;
	}
	

	/**
	 * checks if the event resulted in removing an element
	 * @return <tt>true</tt> if the event type is
	 * one of {@link EventTypes#REMOVE} or {@link EventTypes#REMOVE_MANY};
	 * otherwise <tt>false</tt>.
	 */
	public static boolean isElementRemovedFromSlot(Notification notification) {
		return notification.getEventType() == EventTypes.REMOVE ||
			   notification.getEventType() == EventTypes.REMOVE_MANY;
	}

	/**
	 * checks if the event resulted in destroying an element
	 * @return <tt>true</tt> if the event type equals
	 * {@link EventTypes#DESTROY}; otherwise <tt>false</tt>.
	 */
	public static boolean isDestroy(Notification notification) {
		return notification.getEventType() == EventTypes.DESTROY;
	}
	
	/**
	 * checks if the event is an {@link EventTypes.MOVE} event.
	 * @return <tt>true</tt> if the event type equals
	 * {@link EventTypes.MOVE}; otherwise <tt>false</tt>.
	 */
	public static boolean isMove(Notification notification) {
		return notification.getEventType() == EventTypes.MOVE;
	}
}
