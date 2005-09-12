/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.edit;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.internal.notifications.MSLResourceListener;
import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;

/**
 * A demultiplexing event listener, which relays batched events from the
 * {@link MListener} protocol one at a time, dispatching them to the
 * appropriate methods of an {@link IDemuxedMListener} according to
 * the nature of each event.
 * <p>
 * <b>Note</b> that this class is not intended to be extended
 * by clients.  Clients may instantiate it.
 * </p>
 * 
 * @author Anthony Hunter
 * 
 * @see IDemuxedMListener
 */
public class DemuxingMListener
	extends MListener {

	/**
	 * the event listener client.
	 */
	private IDemuxedMListener eventListener;
	
	/** The extended listener client, if any. */
	private IDemuxedMListener2 eventListener2;

	/**
	 * Demultiplexes the <code>events</code>, dispatching them individually
	 * to the call-back methods of my {@link IDemuxedMListener}.
	 */
	public void onEvent(List events) {
		for (Iterator i = events.iterator(); i.hasNext();) {
			Notification notification = (Notification) i.next();
			
			Object notifier = notification.getNotifier();

			if (notifier instanceof Resource) {
				if ((notification.getEventType() == Notification.ADD)
					|| (notification.getEventType() == Notification.REMOVE)
					|| (notification.getEventType() == Notification.ADD_MANY)
					|| (notification.getEventType() == Notification.REMOVE_MANY)) {

					if (notification.getFeatureID(Resource.class) == Resource.RESOURCE__CONTENTS) {
						Object oldValue = notification.getOldValue();
						Object newValue = notification.getNewValue();

						if (oldValue instanceof Collection) {
							Iterator j = ((Collection) oldValue).iterator();
							while (j.hasNext()) {
								Object value = j.next();
								if (value instanceof EObject) {
									if (eventListener2 != null) {
										eventListener2.handleRootRemovedEvent(notification, (Resource) notifier,(EObject) value);
									}
								}
							}
						} else if (oldValue instanceof EObject) {
							if (eventListener2 != null) {
								eventListener2.handleRootRemovedEvent(notification, (Resource) notifier,(EObject) oldValue);
							}
						}

						if (newValue instanceof Collection) {
							Iterator j = ((Collection) newValue).iterator();
							while (j.hasNext()) {
								Object value = j.next();
								if (value instanceof EObject) {
									if (eventListener2 != null) {
										eventListener2.handleRootAddedEvent(notification, (Resource) notifier,(EObject) value);
									}
								}
							}
						} else if (newValue instanceof EObject) {
							if (eventListener2 != null) {
								eventListener2.handleRootAddedEvent(notification, (Resource) notifier,(EObject) newValue);
							}
						}
					}
				} else if (notification.getEventType() == Notification.SET) {
					Resource resource = (Resource) notifier;
					EObject root = ResourceUtil.getFirstRoot(resource);
					int featureID = notification.getFeatureID(Resource.class);
					if (featureID == Resource.RESOURCE__IS_MODIFIED
						&& notification.getNewBooleanValue() != notification
							.getOldBooleanValue()) {
						if (resource != null && root != null
							&& root.eResource() != null
							&& root.eResource().equals(resource)
							&& resource.isLoaded()) {

							if (notification.getNewBooleanValue()) {
								eventListener.handleResourceDirtiedEvent(
									notification, resource);
							} else {
								eventListener.handleResourceSavedEvent(
									notification, resource);
							}
						}
					} else if (featureID == Resource.RESOURCE__URI) {
						eventListener.handleResourceSavedEvent(notification,
							resource);
					} else if (featureID == Resource.RESOURCE__IS_LOADED) {
						if ( resource != null) {
							boolean isLoaded = notification.getNewBooleanValue();
							if (isLoaded) {
								eventListener.handleResourceLoadedEvent(notification, resource);
							} else {
								if ( notification instanceof MSLResourceListener.UnloadNotification ) {
									eventListener.handleResourceUnloadedEvent(notification, resource, ((MSLResourceListener.UnloadNotification)notification).getResourceRoot());
								}
							}
						}
					}
				} else if (notification.getEventType() == EventTypes.IMPORT) {
					Resource resource = (Resource) notifier;
					eventListener
						.handleResourceImportedEvent(notification, resource);
				} else if (notification.getEventType() == EventTypes.EXPORT) {
					Resource resource = (Resource) notifier;
					eventListener
						.handleResourceExportedEvent(notification, resource);
				} else if (notification.getEventType() == EventTypes.SEPARATE) {
					int featureID = notification.getFeatureID(Resource.class);
					if ((featureID == Resource.RESOURCE__CONTENTS) && (eventListener2 != null)) {
						ILogicalResource res = (ILogicalResource) notifier;
						Resource newRes = (Resource) notification.getNewValue();
						int position = notification.getPosition();
						
						EObject element = (EObject) res.getContents().get(position);
						
						eventListener2.handleElementSeparatedEvent(
							notification, res, element, newRes);
					}
				} else if (notification.getEventType() == EventTypes.ABSORB) {
					int featureID = notification.getFeatureID(Resource.class);
					if ((featureID == Resource.RESOURCE__CONTENTS) && (eventListener2 != null)) {
						ILogicalResource res = (ILogicalResource) notifier;
						Resource oldRes = (Resource) notification.getOldValue();
						int position = notification.getPosition();
						
						EObject element = (EObject) res.getContents().get(position);
						
						eventListener2.handleElementAbsorbedEvent(
							notification, res, element, oldRes);
					}
				} else if (notification.getEventType() == EventTypes.LOAD) {
					int featureID = notification.getFeatureID(Resource.class);
					if ((featureID == Resource.RESOURCE__CONTENTS) && (eventListener2 != null)) {
						EObject element = (EObject) notification.getNewValue();
						ILogicalResource res = (ILogicalResource) notifier;
						
						eventListener2.handleElementLoadedEvent(
							notification, res, element);
					}
				}
			} else if (notifier instanceof EObject) {
				if (notification.getEventType() == EventTypes.ADD) {
					EObject owner = (EObject) notifier;
					if (notification.getNewValue() instanceof EObject) {
						EObject newElement = (EObject) notification
							.getNewValue();
						eventListener.handleElementCreatedEvent(notification,
							owner, newElement);
					} else {
						eventListener.handleElementModifiedEvent(notification,
							owner);
					}
				} else if (notification.getEventType() == EventTypes.ADD_MANY) {
					EObject owner = (EObject) notifier;
					if (notification.getNewValue() instanceof List) {
						for (Iterator j = ((List) notification.getNewValue())
							.iterator(); j.hasNext();) {
							Object newValue = j.next();
							if (newValue instanceof EObject) {
								eventListener.handleElementCreatedEvent(
									notification, owner, (EObject) newValue);
							} else {
								eventListener.handleElementModifiedEvent(
									notification, owner);
							}
						}
					}
				} else if (notification.getEventType() == EventTypes.REMOVE) {
					EObject owner = (EObject) notifier;
					if (notification.getOldValue() instanceof EObject) {
						EObject oldElement = (EObject) notification
							.getOldValue();
						eventListener.handleElementDeletedEvent(notification,
							owner, oldElement);
					} else {
						eventListener.handleElementModifiedEvent(notification,
							owner);
					}
				} else if (notification.getEventType() == EventTypes.REMOVE_MANY) {
					EObject owner = (EObject) notifier;
					if (notification.getOldValue() instanceof List) {
						for (Iterator j = ((List) notification.getOldValue())
							.iterator(); j.hasNext();) {
							Object oldValue = j.next();
							if (oldValue instanceof EObject) {
								eventListener.handleElementDeletedEvent(
									notification, owner, (EObject) oldValue);
							} else {
								eventListener.handleElementModifiedEvent(
									notification, owner);
							}
						}
					}
				} else if (notification.getEventType() == EventTypes.SET
						|| notification.getEventType() == EventTypes.UNSET) {
					Object feature = notification.getFeature();
					if (feature instanceof EReference
						&& ((EReference) feature).isContainment()
						&& !((EReference) feature).isMany()) {
						// handle element insertion/deletion in non-collections
						// (e.g. MultiplicityElement.lowerValue)
						EObject owner = (EObject) notifier;

						EObject newElement = (EObject) notification
							.getNewValue();
						if (newElement != null) {
							eventListener.handleElementCreatedEvent(
								notification, owner, newElement);
						}

						EObject oldElement = (EObject) notification
							.getOldValue();
						if (oldElement != null) {
							eventListener.handleElementDeletedEvent(
								notification, owner, oldElement);
						}
					} else {
						EObject element = (EObject) notifier;
						eventListener.handleElementModifiedEvent(notification,
							element);
					}
				} else if (notification.getEventType() == EventTypes.MOVE) {
					EObject element = (EObject) notifier;
					eventListener.handleElementModifiedEvent(notification,
						element);
				} else if (notification.getEventType() == EventTypes.SEPARATE) {
					if (eventListener2 != null) {
						EObject element = (EObject) notifier;
						ILogicalResource res = (ILogicalResource) element.eResource();
						EReference feature = (EReference) notification.getFeature();
						Resource newRes = (Resource) notification.getNewValue();
						int position = notification.getPosition();
						
						if (position < 0) {
							// scalar reference
							element = (EObject) element.eGet(feature);
						} else {
							element = (EObject) ((EList) element.eGet(feature)).get(position);
						}
						
						eventListener2.handleElementSeparatedEvent(
							notification, res, element, newRes);
					}
				} else if (notification.getEventType() == EventTypes.ABSORB) {
					if (eventListener2 != null) {
						EObject element = (EObject) notifier;
						ILogicalResource res = (ILogicalResource) element.eResource();
						EReference feature = (EReference) notification.getFeature();
						Resource oldRes = (Resource) notification.getOldValue();
						int position = notification.getPosition();
						
						if (position < 0) {
							// scalar reference
							element = (EObject) element.eGet(feature);
						} else {
							element = (EObject) ((EList) element.eGet(feature)).get(position);
						}
						
						eventListener2.handleElementAbsorbedEvent(
							notification, res, element, oldRes);
					}
				} else if (notification.getEventType() == EventTypes.LOAD) {
					if (eventListener2 != null) {
						EObject element = (EObject) notification.getNewValue();
						ILogicalResource res = (ILogicalResource) element.eResource();
						
						eventListener2.handleElementLoadedEvent(
							notification, res, element);
					}
				}
			} else if (notifier instanceof MUndoInterval) {
				MUndoInterval undoInterval = (MUndoInterval) notifier;
				if (notification.getEventType() == EventTypes.CREATE) {
					eventListener.handleUndoIntervalClosedEvent(notification,
						undoInterval);
				} else if (notification.getEventType() == EventTypes.DESTROY) {
					eventListener.handleUndoIntervalsFlushedEvent(notification,
						undoInterval);
				}
			}
		}
	}

	/**
	 * start listening.
	 */
	public void startListening() {

		/* add the listener */
		super.startListening();

		/* add the filter */
		MFilter filter = eventListener.getFilter();
		
		if (filter == null) {
			filter = MFilter.WILDCARD_FILTER;
		}
		
		setFilter(filter);
	}

	/**
	 * stop listening.
	 */
	public void stopListening() {
		super.stopListening();
	}

	/**
	 * This constructor creates default listener for the specified editing
	 * domain. The default listener filters out events that are not generated
	 * for default file extensions.
	 * 
	 * @param eventListener
	 *            the event listener
	 * @param editingDomain
	 *            the editing domain
	 */
	public DemuxingMListener(IDemuxedMListener eventListener,
			MEditingDomain editingDomain) {
		super(editingDomain);
		this.eventListener = eventListener;
		
		if (eventListener instanceof IDemuxedMListener2) {
			eventListener2 = (IDemuxedMListener2) eventListener;
		}
	}

	/**
	 * This constructor creates default listener. The default listener filters
	 * out events that are not generated for default file extensions.
	 */
	public DemuxingMListener(IDemuxedMListener eventListener) {
		super();
		this.eventListener = eventListener;
		
		if (eventListener instanceof IDemuxedMListener2) {
			eventListener2 = (IDemuxedMListener2) eventListener;
		}
	}
}