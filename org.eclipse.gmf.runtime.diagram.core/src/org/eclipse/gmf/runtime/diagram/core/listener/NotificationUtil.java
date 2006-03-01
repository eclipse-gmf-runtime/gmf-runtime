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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;

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
	 * one of {@link Notification#SET} or {@link Notification#UNSET};
	 * otherwise <tt>false</tt>.
	 */
	public static boolean isSlotModified(Notification notification) {
		return notification.getEventType() == Notification.SET || 
			   notification.getEventType() == Notification.UNSET;
	}
	
	/**
	 * checks if the event resulted in adding an element
	 * @return <tt>true</tt> if the event type is
	 * one of {@link Notification#ADD} or {@link Notification#ADD_MANY};
	 * otherwise <tt>false</tt>.
	 */
	public static boolean isElementAddedToSlot(Notification notification) {
		return notification.getEventType() == Notification.ADD ||
			   notification.getEventType() == Notification.ADD_MANY;
	}
	

	/**
	 * checks if the event resulted in removing an element
	 * @return <tt>true</tt> if the event type is
	 * one of {@link Notification#REMOVE} or {@link Notification#REMOVE_MANY};
	 * otherwise <tt>false</tt>.
	 */
	public static boolean isElementRemovedFromSlot(Notification notification) {
		return notification.getEventType() == Notification.REMOVE ||
			   notification.getEventType() == Notification.REMOVE_MANY;
	}
	
	/**
	 * checks if the event is an {@link Notification.MOVE} event.
	 * @return <tt>true</tt> if the event type equals
	 * {@link Notification.MOVE}; otherwise <tt>false</tt>.
	 */
	public static boolean isMove(Notification notification) {
		return notification.getEventType() == Notification.MOVE;
	}
    
    /**
     * Collect the deleted objects from all the notifications in the event.
     * 
     * @param event
     * @return
     */
    public static Set getDeletedObjects(ResourceSetChangeEvent event) {
        HashSet deletedObjects = new HashSet();
        for (Iterator i = event.getNotifications().iterator(); i.hasNext();) {
            Notification notification = (Notification) i.next();
            if (notification.getEventType() == Notification.REMOVE
                && ((EObject) notification.getOldValue()).eResource() == null) {
                deletedObjects.add(notification.getOldValue());
            }
        }
        return deletedObjects;
    }
}
