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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationWrapper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

/**
 * Resource event listener.
 * 
 * @author rafikj
 */
public class MSLResourceListener {
	/**
	 * Special notification wrapper 
	 * @author vramaswa
	 *
	 */
	public class UnloadNotification
		extends NotificationWrapper {
		private EObject resourceRoot = null;

		/**
		 * Constructor
		 * @param notifier
		 * @param notification
		 */
		public UnloadNotification(EObject resourceRoot, Notification notification) {
			super(notification);
			
			this.resourceRoot = resourceRoot;

		}
		
		/**
		 * @return Returns the resourceRoot.
		 */
		public final EObject getResourceRoot() {
			return resourceRoot;
		}

	}
	
	private MSLEditingDomain domain = null;

	private Map loadedResources = new WeakHashMap();

	private Map unloadedResourcesRoot = new HashMap();
	
	// TODO Remove this tracking of resources with errors in the next iteration.
	/*
	 * This map is here to keep track of which resources were
	 *  loaded with errors. For now we are not going to be propagating
	 *  any events associated with loading/unloaded these resources.
	 *  In the next iteration we will propagate all of these events
	 *  and listeners will be required to verify whether the resource
	 *  loaded with errors or not.
	 *  
	 *  Look at MEditingDomain.loadResource, MEditingDomain.unloadResource
	 *   and IDemuxedMListener.handleResourceLoadedEvent for more details.
	 *   
	 *  cmcgee
	 */
	private Map resourcesWithErrors = new WeakHashMap();
	
	/**
	 * Constructor.
	 */
	public MSLResourceListener(MSLEditingDomain domain) {
		super();
		this.domain = domain;
	}

	/**
	 * Handle resource event.
	 */
	public void handleEvent(Notification notification) {

		Resource notifier = (Resource) notification.getNotifier();

		int eventType = notification.getEventType();

		if (eventType == EventTypes.CREATE)
			return;

		boolean resourceAlreadyLoaded = resourceFinishedLoading(notifier);
		
		Object newValue = notification.getNewValue();
		Object oldValue = notification.getOldValue();

		int featureID = notification.getFeatureID(Resource.class);

		if ((eventType == Notification.ADD)
				|| (eventType == Notification.ADD_MANY)) {
			if (featureID == Resource.RESOURCE__CONTENTS) {
				/* Only propagate resource content changes that are not part of the
				 * loading the resource */
				if ( !resourceFinishedLoading(notifier) ) {
					return;
				}
			}			
		} else if ((eventType == Notification.REMOVE)
				|| (eventType == Notification.REMOVE_MANY)) {
			if (featureID == Resource.RESOURCE__CONTENTS) {
				/* Only propagate resource content changes that are not part of the
				 * unloading the resource */
				if ( isResourceUnLoading(notifier) ) {
					if ( !unloadedResourcesRoot.containsKey(notifier)) {
						if (oldValue instanceof Collection) {
							Iterator j = ((Collection) oldValue).iterator();
							while (j.hasNext()) {
								Object value = j.next();
								if (value instanceof EObject) {
									unloadedResourcesRoot.put(notifier, value);
									break;
								}
							}
						} else if (oldValue instanceof EObject) {
							unloadedResourcesRoot.put(notifier, oldValue);
						}
					}

					return;
				}
			}
		} else if (eventType == Notification.SET) {

			if (featureID == Resource.RESOURCE__IS_LOADED) {

				if ((newValue instanceof Boolean)
					&& (oldValue instanceof Boolean)) {

					boolean newBooleanValue = notification.getNewBooleanValue();
					boolean oldBooleanValue = notification.getOldBooleanValue();

					if (newBooleanValue && !oldBooleanValue) {

						loadedResources.put(notifier, Boolean.TRUE);

						MSLUtil.postProcessResource(notifier);
						
						// TODO Remove this check for errors in the next iteration.
						// If the resource loaded with errors, place it into a special
						//  map indicating that it was loaded with errors so that we
						//  do not propagate any automated unload events. This is going
						//  to change in the next iteration where we will propagate all
						//  resource-level events.
						//
						// cmcgee
						//
						if (notifier.getErrors().size() > 0) {
							resourcesWithErrors.put(notifier, Boolean.TRUE);
						} else {
							resourcesWithErrors.remove(notifier);

							// forward event to broker.
							domain.getEventBroker().addEvent(notification);
						}
						return;
					} else if (!newBooleanValue && oldBooleanValue) {

						loadedResources.remove(notifier);

						domain.getResourceIndexer().deregisterReferences(
							notifier);

						domain.getUndoStack().flushAll();

						EObject root = null;
						if ( unloadedResourcesRoot.containsKey(notifier)) {
							root = (EObject)unloadedResourcesRoot.get(notifier);
						}
						
						UnloadNotification unloadNotification = new UnloadNotification(root, notification);
						unloadedResourcesRoot.remove(notifier);
						
						// TODO Remove this check for resources with errors in the next iteration.
						// We will be checking whether this resource was one that
						//  loaded with errors in it. If this is the case then we
						//  do not propagate the event. This is going to change in the
						//  next iteration.
						//
						// cmcgee
						//
						if (!resourcesWithErrors.containsKey(notifier)) {
							domain.getEventBroker().addEvent(unloadNotification);
						} else {
							resourcesWithErrors.remove(notifier);
						}
						
						return;
					}
				}
			}
		}
		
		if (resourceAlreadyLoaded) {
			// populate undo stack if the resource was already loaded before
			//    we received this event
			domain.getCommandGenerator().generateCommand(notification);
		}

		// forward event to broker.
		domain.getEventBroker().addEvent(notification);
	}

	/**
	 * Has resource finished loading?
	 */
	public boolean resourceFinishedLoading(Resource resource) {
		return loadedResources.containsKey(resource);
	}

	/**
	 * Is resource unloading?
	 */
	public boolean isResourceUnLoading(Resource resource) {
		return !resource.isLoaded() && loadedResources.containsKey(resource);
	}
	
	/**
	 * Mark resource as finished loading.
	 */
	public void markResourceFinishedLoading(Resource resource) {

		if (resource.isLoaded())
			loadedResources.put(resource, Boolean.TRUE);
		else
			loadedResources.remove(resource);
	}
}