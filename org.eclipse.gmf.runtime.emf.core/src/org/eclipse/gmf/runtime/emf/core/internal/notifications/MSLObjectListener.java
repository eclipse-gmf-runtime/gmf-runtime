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

package org.eclipse.gmf.runtime.emf.core.internal.notifications;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MResource;
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

		Object feature = notification.getFeature();

		if (resource != null) {

			if (!domain.getResouceListener().resourceFinishedLoading(resource))
				return;
			// set resource dirty flag.
			switch (eventType) {
				case Notification.SET:
				case Notification.UNSET:
				case Notification.MOVE: {
					if (!notification.isTouch() && !resource.isModified()) {
						if (!isTransient(notifier, feature))
							resource.setModified(true);
					}
					break;
				}
	
				case Notification.ADD:
				case Notification.REMOVE:
				case Notification.ADD_MANY:
				case Notification.REMOVE_MANY: {
					if (!resource.isModified() && !isTransient(notifier, feature))
						resource.setModified(true);
					break;
				}
			}

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

		// process references.
		processReferences(notifier, eventType, newValue, oldValue, feature,
				resource);

		// forward event to broker.
		domain.getEventBroker().addEvent(notification);
	}

	/**
	 * check if the feature or one of its containers is transient
	 * 
	 * @param notifier
	 * @param feature
	 * @param transientChange
	 * @return
	 */
	private boolean isTransient(EObject notifier, Object feature) {
		if (feature instanceof EStructuralFeature) {
			if (((EStructuralFeature) feature).isTransient())
				return true;
			else
				// calling isTransient could be a lengthy operation
				return isTransient(notifier);
		}
		return false;
	}

	/**
	 * Is object transient?
	 */
	private boolean isTransient(EObject eObject) {
		EStructuralFeature containmentFeature = eObject.eContainmentFeature();
		while (containmentFeature != null) {
			if (containmentFeature.isTransient())
				return true;
			eObject = eObject.eContainer();
			if (eObject != null)
				containmentFeature = eObject.eContainmentFeature();
			else
				break;
		}
		return false;
	}

	/**
	 * Process references and update reference maps.
	 */
	private void processReferences(EObject notifier, int eventType,
			Object newValue, Object oldValue, Object feature, Resource resource) {

		if (feature instanceof EReference) {

			// maintain the reverese reference map.
			EReference reference = (EReference) feature;

			List newObjects = new ArrayList();
			List oldObjects = new ArrayList();

			if ((eventType == Notification.SET)
					|| (eventType == Notification.UNSET)
					|| (eventType == Notification.ADD)
					|| (eventType == Notification.REMOVE)
					|| (eventType == Notification.ADD_MANY)
					|| (eventType == Notification.REMOVE_MANY)
					|| (eventType == Notification.RESOLVE)) {

				if (newValue instanceof EObject)
					newObjects.add(newValue);

				else if (newValue instanceof Collection) {

					Iterator i = ((Collection) newValue).iterator();

					while (i.hasNext()) {

						Object newObject = i.next();

						if (newObject instanceof EObject)
							newObjects.add(newObject);
					}
				}

				if (oldValue instanceof EObject)
					oldObjects.add(oldValue);

				else if (oldValue instanceof Collection) {

					Iterator i = ((Collection) oldValue).iterator();

					while (i.hasNext()) {

						Object oldObject = i.next();

						if (oldObject instanceof EObject)
							oldObjects.add(oldObject);
					}
				}
			}

			if (resource instanceof MResource)
				((MResource) resource).getHelper().registerReferences(domain,
						notifier, reference, newObjects, oldObjects);
			else
				MSLUtil.registerReferences(domain, notifier, reference,
						newObjects, oldObjects);
		}
	}
}