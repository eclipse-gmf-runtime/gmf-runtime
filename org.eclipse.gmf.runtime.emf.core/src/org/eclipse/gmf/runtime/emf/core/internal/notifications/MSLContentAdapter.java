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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MResource;

/**
 * This class overrides EMF content adapter class by disabling the automatic
 * removal of the content adapter from the object's adapter list when the object
 * it self is removed from its container. This is required since the MSL
 * requires events on detached objects.
 * 
 * @author rafikj
 */
public class MSLContentAdapter
	extends EContentAdapter {

	private MSLEditingDomain domain = null;

	/**
	 * Constructor.
	 */
	public MSLContentAdapter(MSLEditingDomain domain) {
		super();
		this.domain = domain;
	}

	/**
	 * Attaches adapter to notifier.
	 */
	public void listenToModifications(Notifier notifier) {

		if (notifier.eAdapters().contains(this))
			return;

		addAdapter(notifier);
	}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {

		super.notifyChanged(notification);

		Object notifier = notification.getNotifier();

		if (notifier instanceof EObject)
			domain.getObjectListener().handleEvent(notification);

		else if (notifier instanceof Resource)
			domain.getResouceListener().handleEvent(notification);

		else if (notifier instanceof ResourceSet)
			domain.getResouceSetListener().handleEvent(notification);
	}

	/**
	 * @see org.eclipse.emf.ecore.util.EContentAdapter#selfAdapt(org.eclipse.emf.common.notify.Notification)
	 */
	protected void selfAdapt(Notification notification) {

		Object notifier = notification.getNotifier();

		if (notifier instanceof ResourceSet) {

			if (notification.getFeatureID(ResourceSet.class) == ResourceSet.RESOURCE_SET__RESOURCES)
				handleContainment(notification);

		} else if (notifier instanceof Resource) {

			if (notification.getFeatureID(Resource.class) == Resource.RESOURCE__CONTENTS)
				handleContainment(notification);

		} else if (notifier instanceof EObject) {

			Object feature = notification.getFeature();

			if (feature instanceof EReference
				&& ((EReference) feature).isContainment())
				handleContainment(notification);
		}
	}

	/**
	 * @see org.eclipse.emf.ecore.util.EContentAdapter#handleContainment(org.eclipse.emf.common.notify.Notification)
	 */
	protected void handleContainment(Notification notification) {

		switch (notification.getEventType()) {

			case Notification.SET:
			case Notification.UNSET: {
				Notifier newValue = (Notifier) notification.getNewValue();

				if (newValue != null)
					addAdapter(newValue);

				break;
			}
			case Notification.ADD: {
				Notifier newValue = (Notifier) notification.getNewValue();

				if (newValue != null)
					addAdapter(newValue);

				break;
			}
			case Notification.ADD_MANY: {
				Collection newValues = (Collection) notification.getNewValue();

				for (Iterator i = newValues.iterator(); i.hasNext();) {

					Notifier newValue = (Notifier) i.next();

					addAdapter(newValue);
				}

				break;
			}
		}
	}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier object) {

		// do not set "this.target" to "object" as it will cause a memory leak.

		Collection contents = getContents(object);

		if (contents != null)
			for (Iterator i = contents.iterator(); i.hasNext();)
				addAdapter((Notifier) i.next());
	}

	/**
	 * @see org.eclipse.emf.ecore.util.EContentAdapter#unsetTarget(java.lang.Object)
	 */
	protected void unsetTarget(Object object) {

		this.target = null;

		Collection contents = getContents(object);

		if (contents != null)
			for (Iterator i = contents.iterator(); i.hasNext();)
				removeAdapter((Notifier) i.next());
	}

	/**
	 * Attaches adapter to notifier.
	 */
	protected void addAdapter(Notifier notifier) {

		MSLContentAdapter foundAdapter = null;

		for (Iterator i = notifier.eAdapters().iterator(); (i.hasNext())
			&& (foundAdapter == null);) {

			Object adapter = i.next();

			if (adapter instanceof MSLContentAdapter)
				foundAdapter = (MSLContentAdapter) adapter;
		}

		if (foundAdapter != null)
			notifier.eAdapters().remove(foundAdapter);

		notifier.eAdapters().add(this);
	}

	/**
	 * detaches adapter from notifier.
	 */
	protected void removeAdapter(Notifier notifier) {
		notifier.eAdapters().remove(this);
	}

	/**
	 * Gets contents of object.
	 */
	private static Collection getContents(Object object) {

		Collection contents = null;

		if (object instanceof EObject) {

			EObject eObject = (EObject) object;

			Resource resource = eObject.eResource();

			if ((resource != null) && (resource instanceof MResource))
				contents = ((MResource) resource).getHelper().getContents(
					eObject);
			else
				contents = eObject.eContents();

		} else if (object instanceof Resource)
			contents = ((Resource) object).getContents();

		else if (object instanceof ResourceSet)
			contents = ((ResourceSet) object).getResources();

		return contents;
	}
}