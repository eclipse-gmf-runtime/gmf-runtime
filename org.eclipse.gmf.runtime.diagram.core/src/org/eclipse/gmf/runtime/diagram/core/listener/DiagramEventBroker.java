/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramDebugOptions;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MFilter;
import org.eclipse.gmf.runtime.emf.core.edit.MUniversalListener;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A model server listener that broadcast EObject events to all registered
 * listeners.
 * 
 * @author melaasar, mmostafa
 */
public class DiagramEventBroker
	extends MUniversalListener {

	private static String LISTEN_TO_ALL_FEATURES = "*"; //$NON-NLS-1$

	/** listener map */
	private final NotifierToKeyToListenersSetMap listeners = new NotifierToKeyToListenersSetMap();

	/**
	 * Utility class representing a Map of Notifier to a Map of Keys to a Set of
	 * listener
	 * 
	 * @author mmostafa
	 */
	private final class NotifierToKeyToListenersSetMap {

		/**
		 * internal map to hold the listeners
		 */
		private final Map listenersMap = new WeakHashMap();

		/**
		 * Adds a listener to the map
		 * 
		 * @param notifier
		 *            the notifier the listener will listen to
		 * @param key
		 *            a key for the listener, this help in categorizing the
		 *            listeners based on their interest
		 * @param listener
		 *            the listener
		 */
		public void addListener(EObject notifier, Object key, Object listener) {
			Map keys = (Map) listenersMap.get(notifier);
			if (keys == null) {
				keys = new HashMap();
				listenersMap.put(notifier, keys);
			}
			Set listenersSet = (Set) keys.get(key);
			if (listenersSet == null) {
				listenersSet = new HashSet();
				keys.put(key, listenersSet);
			}
			listenersSet.add(listener);
		}

		/**
		 * Adds a listener to the notifier; this listener is added againest a
		 * generic key, <code>LISTEN_TO_ALL_FEATURES<code>
		 * so it can listen to all events on the notifier 
		 * @param notifier the notifier the listener will listen to
		 * @param listener the listener
		 */
		public void addListener(EObject notifier, Object listener) {
			addListener(notifier, LISTEN_TO_ALL_FEATURES, listener);
		}

		/**
		 * removes a listener from the map
		 * 
		 * @param notifier
		 * @param key
		 * @param listener
		 */
		public void removeListener(EObject notifier, Object key, Object listener) {
			Map keys = (Map) listenersMap.get(notifier);
			if (keys != null) {
				Set listenersSet = (Set) keys.get(key);
				if (listenersSet != null) {
					listenersSet.remove(listener);
					if (listenersSet.isEmpty()) {
						keys.remove(key);
					}
				}
				if (keys.isEmpty())
					listenersMap.remove(notifier);
			}
		}

		/**
		 * get listeners interested in the passed notifier and key
		 * 
		 * @param notifier
		 * @param key
		 * @return <code>Set</code> of listeners
		 */
		public Set getListeners(Object notifier, Object key) {
			Map keys = (Map) listenersMap.get(notifier);
			if (keys != null) {
				Set listenersSet = (Set) keys.get(key);
				if (listenersSet != null) {
					return listenersSet;
				}
			}
			return Collections.EMPTY_SET;
		}

		/**
		 * return all listeners interested in the passed notifier
		 * 
		 * @param notifier
		 * @return
		 */
		public Set getAllListeners(Object notifier) {
			Map keys = (Map) listenersMap.get(notifier);
			if (keys == null || keys.isEmpty()) {
				return Collections.EMPTY_SET;
			}
			Set listenersCollection = new HashSet();
			Set enteries = keys.entrySet();
			for (Iterator iter = enteries.iterator(); iter.hasNext();) {
				Map.Entry entry = (Map.Entry) iter.next();
				Set listenersSet = (Set) entry.getValue();
				if (listenersSet != null && !listenersSet.isEmpty())
					listenersCollection.addAll(listenersSet);
			}
			return listenersCollection;
		}
	}

	/**
	 * Start listening to the model server. {@link MFilter.WildCard} is the
	 * default filter by this listener.
	 */
	public void startListening() {
		Trace.trace(DiagramPlugin.getInstance(), DiagramDebugOptions.EVENTS,
			this + "#startListening()"); //$NON-NLS-1$
		setFilter(new MFilter.NotifierType(EObject.class, false));
		super.startListening();
	}

	/** Stop listening to Model Server */
	public void stopListening() {
		Trace.trace(DiagramPlugin.getInstance(), DiagramDebugOptions.EVENTS,
			this + "#stopListening()"); //$NON-NLS-1$
		super.stopListening();
	}

	/**
	 * Model Server event callback method. This method will redirect the events
	 * to their respective event type handler
	 * 
	 * @see #handleElementEvent(Notification)
	 * @see #handleResourceEvent(Notification)
	 */
	public final void onEvent(List events) {
		List eventArray = new ArrayList(events);

		// if the events contain "uncreated" objects, remove all events related
		// to those
		// objects from the event list (except the "uncreate" events themselves)
		HashSet deletedObjects = new HashSet();
		// first collect the "destroyed" objects
		for (Iterator i = eventArray.iterator(); i.hasNext();) {
			Notification event = (Notification) i.next();
			if (event.getEventType() == EventTypes.UNCREATE
				|| event.getEventType() == EventTypes.DESTROY)
				deletedObjects.add(event.getNotifier());
		}

		for (Iterator i = eventArray.iterator(); i.hasNext();) {
			Notification event = (Notification) i.next();
			Object eventFeature = event.getFeature();

			// ignore touch event if it is not a resolve event,and ignore the mutable feature
			// events
			if ((event.isTouch() && event.getEventType() != Notification.RESOLVE)||
			     NotationPackage.eINSTANCE.getView_Mutable().equals(eventFeature)){
				 continue;
			}
			
			Object notifier = event.getNotifier();
			if (notifier instanceof EObject) {
				if (deletedObjects.contains(event.getNotifier())
					&& event.getEventType() != EventTypes.UNCREATE
					&& event.getEventType() != EventTypes.DESTROY
					&& event.getEventType() != EventTypes.UNRESOLVE)
					continue;
				handleElementEvent(event);
			}
		}
	}

	/**
	 * Forward the supplied event to all listeners listening on the supplied
	 * target element.
	 * <P>
	 * <B> Note, for the MSL migration effort, each listener will be forwarded 2
	 * events. First, a MSL complient Notification event followed by an
	 * ElementEvent (for backwards compatibility). The ElementEvent will be
	 * removed one the MSL migration is complete.
	 */
	protected void fireNotification(Notification event) {
		Collection listenerList = getInterestedNotificationListeners(event);
		if (!listenerList.isEmpty()) {
			List listenersSnapShot = new ArrayList(listenerList);
			if (!listenerList.isEmpty()) {
				for (Iterator listenerIT = listenersSnapShot.iterator(); listenerIT
					.hasNext();) {
					NotificationListener listener = (NotificationListener) listenerIT
						.next();
					listener.notifyChanged(event);
				}
			}
		}
	}

	/**
	 * Add the supplied <tt>listener</tt> to the listener list.
	 * 
	 * @param target
	 *            the traget to listen to
	 * @param listener
	 *            the listener
	 */
	public final void addNotificationListener(EObject target,
			NotificationListener listener) {
		if (target != null) {
			listeners.addListener(target, LISTEN_TO_ALL_FEATURES, listener);
		}
	}

	/**
	 * Add the supplied <tt>listener</tt> to the listener list.
	 * 
	 * @param target
	 *            the traget to listen to
	 * @param key
	 *            the key for the listener
	 * @param listener
	 *            the listener
	 */
	public final void addNotificationListener(EObject target,
			EStructuralFeature key, NotificationListener listener) {
		if (target != null) {
			listeners.addListener(target, key, listener);
		}
	}

	/**
	 * remove the supplied <tt>listener</tt> from the listener list.
	 * 
	 * @param target
	 *            the traget to listen to
	 * @param listener
	 *            the listener
	 */
	public final void removeNotificationListener(EObject target,
			NotificationListener listener) {
		if (target != null) {
			listeners.removeListener(target, LISTEN_TO_ALL_FEATURES, listener);
		}
	}

	/**
	 * remove the supplied <tt>listener</tt> from the listener list.
	 * 
	 * @param target
	 *            the traget to listen to
	 * @param key
	 *            the key for the listener
	 * @param listener
	 *            the listener
	 */
	public final void removeNotificationListener(EObject target, Object key,
			NotificationListener listener) {
		if (target != null) {
			listeners.removeListener(target, key, listener);
		}
	}

	public final void finalize() {
		try {
			stopListening();
		} catch (Throwable ignored) {
			// intentionally ignored
		}
	}

	protected Set getNotificationListeners(Object notifier) {
		return listeners.getListeners(notifier, LISTEN_TO_ALL_FEATURES);
	}

	protected Set getNotificationListeners(Object notifier, Object key) {
		if (key != null) {
			if (!key.equals(LISTEN_TO_ALL_FEATURES)) {
				Set listenersSet = new HashSet();
				Collection c = listeners.getListeners(notifier, key);
				if (c != null && !c.isEmpty())
					listenersSet.addAll(c);
				c = listeners.getListeners(notifier, LISTEN_TO_ALL_FEATURES);
				if (c != null && !c.isEmpty())
					listenersSet.addAll(c);
				return listenersSet;
			} else if (key.equals(LISTEN_TO_ALL_FEATURES)) {
				return listeners.getAllListeners(notifier);
			}
		}
		return listeners.getAllListeners(notifier);
	}

	/** SLOT_MODIFIED filter. */
	public final static MFilter SLOT_MODIFIED = new MFilter.And(
		new MFilter.NotifierType(EObject.class, false), new MFilter.And(
			new MFilter.EventType(EventTypes.SET), new MFilter.EventType(
				EventTypes.UNSET)));

	/** ELEMENT_INSERTED_INTO_SLOT filter. */
	public final static MFilter ELEMENT_INSERTED_INTO_SLOT = new MFilter.And(
		new MFilter.NotifierType(EObject.class, false), new MFilter.Or(
			new MFilter.EventType(EventTypes.ADD), new MFilter.EventType(
				EventTypes.ADD_MANY)));

	/** ELEMENT_REMOVED_FROM_SLOT filter. */
	public final static MFilter ELEMENT_REMOVED_FROM_SLOT = new MFilter.And(
		new MFilter.NotifierType(EObject.class, false), new MFilter.Or(
			new MFilter.EventType(EventTypes.REMOVE), new MFilter.EventType(
				EventTypes.REMOVE_MANY)));

	/** ELEMENT_CREATED filter. */
	public final static MFilter ELEMENT_CREATED = new MFilter.And(
		new MFilter.NotifierType(EObject.class, false), new MFilter.EventType(
			EventTypes.CREATE));

	/** ELEMENT_UNCREATED filter */
	public final static MFilter ELEMENT_UNCREATED = new MFilter.And(
		new MFilter.NotifierType(EObject.class, false), new MFilter.EventType(
			EventTypes.UNCREATE));

	/** ELEMENT_DELETED filter. */
	public final static MFilter ELEMENT_DELETED = new MFilter.And(
		new MFilter.NotifierType(EObject.class, false), new MFilter.EventType(
			EventTypes.DESTROY));

	/** ELEMENT_UNDELETED filter. */
	public final static MFilter ELEMENT_UNDELETED = new MFilter.And(
		new MFilter.NotifierType(EObject.class, false), new MFilter.EventType(
			EventTypes.UNDESTROY));

	/** The DiagramEventBroker singleton */
	private static DiagramEventBroker instance;

	/**
	 * gives access to the <code>DiagramEventBroker</code> singleton
	 * 
	 * @return the <code>DiagramEventBroker</code> singleton
	 */
	public static DiagramEventBroker getInstance() {
		if (instance == null) {
			instance = new DiagramEventBroker();
		}
		return instance;
	}

	/**
	 * gets a subset of all the registered listeners who are interested in
	 * receiving the supplied event.
	 * 
	 * @param event
	 *            the event to use
	 * @return the interested listeners in the event
	 */
	protected Set getInterestedNotificationListeners(Notification event) {
		HashSet listenerSet = new HashSet();

		Collection c = getNotificationListeners(event.getNotifier(), event
			.getFeature());
		if (c != null) {
			listenerSet.addAll(c);
		}
		
		EObject notifier = (EObject) event.getNotifier();
		//the Visibility Event get fired to all interested listeners in the container
		if (NotationPackage.eINSTANCE.getView_Visible().equals(event.getFeature()) &&
			notifier.eContainer()!=null){
				listenerSet.addAll(getNotificationListeners(notifier.eContainer()));
		}
		else if (notifier instanceof EAnnotation) {
			addListenersOfNotifier(listenerSet, notifier.eContainer(), event);
		} else if (!(notifier instanceof View)) {
			while (notifier != null && !(notifier instanceof View)) {
				notifier = notifier.eContainer();
			}
			addListenersOfNotifier(listenerSet, notifier, event);
		}
		return listenerSet;
	}

	/**
	 * Helper method to add all the listners of the given <code>notifier</code>
	 * to the list of listeners
	 * 
	 * @param listenerSet
	 * @param notifier
	 */
	private void addListenersOfNotifier(Set listenerSet, EObject notifier,
			Notification event) {
		if (notifier != null) {
			Collection c = getNotificationListeners(notifier, event
				.getFeature());
			if (c != null) {
				if (listenerSet.isEmpty())
					listenerSet.addAll(c);
				else {
					Iterator i = c.iterator();
					while (i.hasNext()) {
						Object o = i.next();
						listenerSet.add(o);
					}
				}
			}
		}
	}

	/**
	 * Forwards the event to all interested listeners.
	 * 
	 * @param event
	 *            the event to handle
	 */
	protected void handleElementEvent(Notification event) {
		MEditingDomain doamin = null;
		if (!event.isTouch()
			&& !(doamin = MEditingDomainGetter.getMEditingDomain(event))
				.isUndoNotification(event) && !doamin.isRedoNotification(event)) {
			EObject element = (EObject) event.getNotifier();
			while (element != null && !(element instanceof View)) {
				element = element.eContainer();
			}
			if (element != null) {
				ViewUtil.persistElement((View) element);
			}
		}
		EObject element = (EObject) event.getNotifier();
		if (element != null) {
			fireNotification(event);
		}
	}
}
