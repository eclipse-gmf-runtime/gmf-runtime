/******************************************************************************
 * Copyright (c) 2002-2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.notifications;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

/**
 * EObject event listener.
 * 
 * @author rafikj
 */
public class MSLObjectListener {

	private MSLEditingDomain domain = null;

	/**
	 * Constructor.
	 */
	public MSLObjectListener(MSLEditingDomain domain) {
		super();
		this.domain = domain;
	}

	/**
	 * Handle object event.
	 */
	public void handleEvent(Notification notification) {
		EObject notifier = (EObject) notification.getNotifier();
		Resource resource = notifier.eResource();

		int eventType = notification.getEventType();

		if (resource != null) {

			if (!domain.getResouceListener().resourceFinishedLoading(resource))
				return;
		}

		Object newValue = notification.getNewValue();
		Object oldValue = notification.getOldValue();

		// convert the CREATE events to a more user friendly version.
		if (eventType == EventTypes.CREATE) {

			if (newValue instanceof EObject) {

				EObject newObject = (EObject) newValue;

				domain.getContentAdapter().listenToModifications(newObject);

				MSLUtil.sendCreateEvent(domain, newObject);
			}
			return;
		}

		// ignore events that do not modify the object.
		if (((eventType == Notification.SET) || (eventType == Notification.UNSET))) {

			if (newValue == oldValue)
				return;

			// ignore idempotent changes to data types that we know to be
			// immutable in Java
			if ((newValue instanceof Number) && newValue.equals(oldValue))
				return;
			if ((newValue instanceof String) && newValue.equals(oldValue))
				return;
			if ((newValue instanceof Boolean) && newValue.equals(oldValue))
				return;
			if ((newValue instanceof Character) && newValue.equals(oldValue))
				return;
		}
		// populate undo stack.
		domain.getCommandGenerator().generateCommand(notification);

		// forward event to broker.
		domain.getEventBroker().addEvent(notification);
	}
}