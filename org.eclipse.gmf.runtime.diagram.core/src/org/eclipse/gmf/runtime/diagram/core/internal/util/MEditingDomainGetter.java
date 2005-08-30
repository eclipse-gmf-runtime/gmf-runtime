/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.util;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;


/**
 * @author Yasser Lulu
 *
 */
public class MEditingDomainGetter {

	//emf editing domain related funcs	
	//TODO revisit and verfiy that it is OK to return the deafult 
	//MEditingDomain.INSTANCE as opposed to returning null instead
	public static MEditingDomain getMEditingDomain(EObject eObject) {
		return getMEditingDomain(eObject, true);
	}

	public static MEditingDomain getMEditingDomain(EObject eObject,
			boolean returnDefaultOnFailure) {
		if (eObject != null) {
			return getMEditingDomain(eObject.eResource(),
				returnDefaultOnFailure);
		}
		return (returnDefaultOnFailure) ? MEditingDomain.INSTANCE
			: null;
	}

	public static MEditingDomain getMEditingDomain(Resource eResource) {
		return getMEditingDomain(eResource, true);
	}

	public static MEditingDomain getMEditingDomain(Resource eResource,
			boolean returnDefaultOnFailure) {
		MEditingDomain editingDomain = null;
		if (eResource != null) {
			editingDomain = MEditingDomain.getEditingDomain(eResource);
		}
		return ((editingDomain == null) && returnDefaultOnFailure) ? MEditingDomain.INSTANCE
			: editingDomain;
	}

	public static MEditingDomain getMEditingDomain(ResourceSet resourceSet) {
		return getMEditingDomain(resourceSet, true);
	}

	public static MEditingDomain getMEditingDomain(ResourceSet resourceSet,
			boolean returnDefaultOnFailure) {
		MEditingDomain editingDomain = null;
		if (resourceSet != null) {
			editingDomain = MEditingDomain.getEditingDomain(resourceSet);
		}
		return ((editingDomain == null) && returnDefaultOnFailure) ? MEditingDomain.INSTANCE
			: editingDomain;
	}

	public static MEditingDomain getMEditingDomain(IAdaptable eObjectAdaptable) {		
		return getMEditingDomain(eObjectAdaptable, true);
	}

	public static MEditingDomain getMEditingDomain(IAdaptable eObjectAdaptable,
			boolean returnDefaultOnFailure) {
		if (eObjectAdaptable != null) {
			return getMEditingDomain((EObject) eObjectAdaptable
				.getAdapter(EObject.class), returnDefaultOnFailure);
		}
		return (returnDefaultOnFailure) ? MEditingDomain.INSTANCE
			: null;
	}

	public static MEditingDomain getMEditingDomain(
			NotificationEvent notificationEvent) {
		return getMEditingDomain(notificationEvent.getElement(), true);
	}

	public static MEditingDomain getMEditingDomain(
			NotificationEvent notificationEvent, boolean returnDefaultOnFailure) {
		return getMEditingDomain(notificationEvent.getElement(),
			returnDefaultOnFailure);
	}

	public static MEditingDomain getMEditingDomain(Notification notification) {
		return getMEditingDomain(notification, true);
	}

	public static MEditingDomain getMEditingDomain(Notification notification,
			boolean returnDefaultOnFailure) {
		return getMEditingDomain(notification.getNotifier(),
			returnDefaultOnFailure);
	}

	public static MEditingDomain getMEditingDomain(Collection collection) {
		return getMEditingDomain(collection, true);
	}

	public static MEditingDomain getMEditingDomain(Collection elements,
			boolean returnDefaultOnFailure) {
		MEditingDomain editingDomain = null;
		Iterator it = elements.iterator();
		while (it.hasNext() && (editingDomain == null)) {
			editingDomain = getMEditingDomain(it.next(), false);//we'll get the best match if any
		}
		return ((editingDomain == null) && returnDefaultOnFailure) ? MEditingDomain.INSTANCE
			: editingDomain;
	}

	//a desperate attempt!
	private static MEditingDomain getMEditingDomain(Object obj,
			boolean returnDefaultOnFailure) {
		if (obj != null) {
			if (obj instanceof EObject) {
				return getMEditingDomain((EObject) obj, returnDefaultOnFailure);
			} else if (obj instanceof IAdaptable) {
				return getMEditingDomain((IAdaptable) obj,
					returnDefaultOnFailure);
			} else if (obj instanceof Resource) {
				return getMEditingDomain((Resource) obj, returnDefaultOnFailure);
			} else if (obj instanceof ResourceSet) {
				return getMEditingDomain((ResourceSet) obj,
					returnDefaultOnFailure);
			} 
		}
		return (returnDefaultOnFailure) ? MEditingDomain.INSTANCE
			: null;
	}

}
