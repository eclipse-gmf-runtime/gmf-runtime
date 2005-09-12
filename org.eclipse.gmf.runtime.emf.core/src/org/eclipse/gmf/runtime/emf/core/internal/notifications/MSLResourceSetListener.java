/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;

/**
 * Resource set event listener.
 * 
 * @author rafikj
 */
public class MSLResourceSetListener {

	private MSLEditingDomain domain = null;

	/**
	 * Constructor.
	 */
	public MSLResourceSetListener(MSLEditingDomain domain) {
		super();
		this.domain = domain;
	}

	/**
	 * Handle resource set event.
	 */
	public void handleEvent(Notification notification) {

		int eventType = notification.getEventType();

		if (eventType == EventTypes.CREATE)
			return;

		Object newValue = notification.getNewValue();
		Object oldValue = notification.getOldValue();

		int featureID = notification.getFeatureID(ResourceSet.class);

		if ((eventType == Notification.ADD)
			|| (eventType == Notification.REMOVE)) {

			if (featureID == ResourceSet.RESOURCE_SET__RESOURCES) {

				if (newValue instanceof Resource)
					domain.getResouceListener().markResourceFinishedLoading(
						(Resource) newValue);

				else if (oldValue instanceof Resource)
					domain.getResouceListener().markResourceFinishedLoading(
						(Resource) oldValue);
			}
		}

		// forward event to broker.
		domain.getEventBroker().addEvent(notification);
	}
}