/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.emf.core.edit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;


/**
 * An optional extension to the {@link IDemuxedListener} interface for listeners
 * interested in separation and absorption events in logical resources.
 *
 * @author Christian W. Damus (cdamus)
 * @author Vishy Ramaswamy
 */
public interface IDemuxedMListener2
	extends IDemuxedMListener {
	
	/**
	 * Notifies me of an event indicating that an element in a logical resource
	 * has been separated into a subordinate (physical) resource.
	 * 
	 * @param notification the raw notification
	 * @param resource the logical resource that was separated
	 * @param eObject an object in the <code>resource</code>, which is now
	 *     separated into a subordinate resource with the specified <code>uri</code>
	 * @param newResource the new subordinate resource (within the logical
	 *     <code>resource</code>) that stores the <code>eObject</code>
	 * 
	 * @deprecated Use the cross-resource containment support provided by EMF,
	 *     instead, by defining containment features that are capable of storing
	 *     proxies.
	 */
	public void handleElementSeparatedEvent(Notification notification,
			org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource resource,
			EObject eObject, Resource newResource);
	
	/**
	 * Notifies me of an event indicating that an element in a logical resource
	 * has been absorbed from a subordinate (physical) resource.
	 * 
	 * @param notification the raw notification
	 * @param resource the logical resource into which the object was absorbed
	 * @param eObject an object in the <code>resource</code>, which was
	 *     absorbed from a subordinate resource with the specified <code>uri</code>
	 * @param oldResource the subordinate resource (within the logical
	 *     <code>resource</code>) that formerly stored the <code>eObject</code>
	 * 
	 * @deprecated Use the cross-resource containment support provided by EMF,
	 *     instead, by defining containment features that are capable of storing
	 *     proxies.
	 */
	public void handleElementAbsorbedEvent(Notification notification,
			org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource resource,
			EObject eObject, Resource oldResource);
	
	/**
	 * Notifies me of an event indicating that an element in a logical resource
	 * has been loaded from a subordinate (physical) resource.
	 * 
	 * @param notification the raw notification
	 * @param resource the logical resource into which the object was loaded
	 * @param eObject an object in the <code>resource</code>, which is
	 *     newly loaded from a previously unloaded physical resource
	 * 
	 * @deprecated Use the cross-resource containment support provided by EMF,
	 *     instead, by defining containment features that are capable of storing
	 *     proxies.
	 */
	public void handleElementLoadedEvent(Notification notification,
			org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource resource,
			EObject eObject);
	
	/**
	 * Notifies me of an event indicating that a root has been added to the resource
	 * 
	 * @param notification the raw notification
	 * @param resource the resource into which the root object was added
	 * @param eObject the root object that was added
	 */
	public void handleRootAddedEvent(Notification notification, Resource resource, EObject eObject);

	/**
	 * Notifies me of an event indicating that a root has been removed from the resource
	 * 
	 * @param notification the raw notification
	 * @param resource the resource from which the root object was removed
	 * @param eObject the root object that was removed
	 */
	public void handleRootRemovedEvent(Notification notification, Resource resource, EObject eObject);
}
