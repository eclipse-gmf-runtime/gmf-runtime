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

package org.eclipse.gmf.runtime.diagram.core.listener;

import org.eclipse.emf.common.notify.Notification;

/**
 * Defines an EMF {@link Notification} listener; any class interested in listenning 
 * to events from the {@link DiagramEventBroker} it implement this interface.
 * the notifyChanged will get called by the {@link DiagramEventBroker#handleElementEvent(Notification)}  
 * 
 * @author mmostafa
 * 
 */

public interface NotificationListener {
	
	/**
	 * Will be called when a Notification event get sent from the DiagramEventBroker
	 * @param notification	the notification object
	 */
	public void notifyChanged(Notification notification);
}
