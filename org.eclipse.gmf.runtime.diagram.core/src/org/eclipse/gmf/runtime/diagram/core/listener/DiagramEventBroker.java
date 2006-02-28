/******************************************************************************
 * Copyright (c) 2002, 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.listener;

import java.lang.ref.WeakReference;
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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;


/**
 * A model server listener that broadcast EObject events to all registered
 * listeners.
 * 
 * @author melaasar, mmostafa, cmahoney
 */
public class DiagramEventBroker extends ResourceSetListenerImpl {

	private static String LISTEN_TO_ALL_FEATURES = "*"; //$NON-NLS-1$

	/** listener map */
	private final NotifierToKeyToListenersSetMap preListeners = new NotifierToKeyToListenersSetMap();
	private final NotifierToKeyToListenersSetMap postListeners = new NotifierToKeyToListenersSetMap();
	
	private static final Map instanceMap = new WeakHashMap();

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
	 * Creates a <code>DiagramEventBroker</code> that listens to all
	 * <code>EObject </code> notifications for the given editing domain.
	 */
	private DiagramEventBroker() {
		super(NotificationFilter.createNotifierTypeFilter(EObject.class));
	}
	
	/**
	 * Gets the diagmam event broker instance for the editing domain passed in.
	 * There is one diagram event broker per editing domain.
	 * 
	 * @param editingDomain
	 * @return Returns the diagram event broker.
	 */
	public static DiagramEventBroker getInstance(
			TransactionalEditingDomain editingDomain) {
		WeakReference reference = (WeakReference) instanceMap
			.get(editingDomain);
		if (reference != null) {
			return (DiagramEventBroker) reference.get();
		}
		return null;
	}

	/**
     * Creates a new diagram event broker instance for the editing domain passed
     * in only if the editing domain does not already have a diagram event
     * broker. There is one diagram event broker per editing domain. Adds the
     * diagram event broker instance as a listener to the editing domain.
     * 
     * @param editingDomain
     */
	public static void startListening(
			TransactionalEditingDomain editingDomain) {
		DiagramEventBroker diagramEventBroker = getInstance(editingDomain);
		if (diagramEventBroker == null) {
			diagramEventBroker = new DiagramEventBroker();
			editingDomain.addResourceSetListener(diagramEventBroker);
			instanceMap.put(editingDomain,
				new WeakReference(diagramEventBroker));
		}
	}

    /**
     * @param editingDomain
     */
    public static void stopListening(
            TransactionalEditingDomain editingDomain) {
        DiagramEventBroker diagramEventBroker = getInstance(editingDomain);
        if (diagramEventBroker != null) {
            editingDomain.removeResourceSetListener(diagramEventBroker);
            instanceMap.remove(editingDomain);
        }
    }
    
    public Command transactionAboutToCommit(ResourceSetChangeEvent event) {
        Set deletedObjects = getDeletedObjects(event);
 
        CompoundCommand cc = new CompoundCommand();
        for (Iterator i = event.getNotifications().iterator(); i.hasNext();) {
            final Notification notification = (Notification) i.next();
            Object eventFeature = notification.getFeature();

            // ignore touch event if it is not a resolve event,and ignore the mutable feature
            // events
            if ((notification.isTouch() && notification.getEventType() != Notification.RESOLVE)||
                 NotationPackage.eINSTANCE.getView_Mutable().equals(eventFeature)){
                 continue;
            }
            
            Object notifier = notification.getNotifier();
            if (notifier instanceof EObject) {
                if (deletedObjects.contains(notification.getNotifier()) && !isDestroyEvent(notification))
                    continue;
                Command cmd = handleTransactionAboutToCommitEvent(notification);
                if (cmd != null) {
                    cc.append(cmd);
                }
            }
        }
        return cc.isEmpty() ? null : cc;
    }

	public void resourceSetChanged(ResourceSetChangeEvent event) {
        Set deletedObjects = getDeletedObjects(event);

		for (Iterator i = event.getNotifications().iterator(); i.hasNext();) {
			final Notification notification = (Notification) i.next();
			Object eventFeature = notification.getFeature();

			// ignore touch event if it is not a resolve event,and ignore the mutable feature
			// events
			if ((notification.isTouch() && notification.getEventType() != Notification.RESOLVE)||
			     NotationPackage.eINSTANCE.getView_Mutable().equals(eventFeature)){
				 continue;
			}
			
			Object notifier = notification.getNotifier();
			if (notifier instanceof EObject) {
				if (deletedObjects.contains(notification.getNotifier())  && !isDestroyEvent(notification))
					continue;
				handleElementEvent(notification);
			}
		}
	}
    
    private Set getDeletedObjects(ResourceSetChangeEvent event) {
        HashSet deletedObjects = new HashSet();
        // first collect the "destroyed" objects
        for (Iterator i = event.getNotifications().iterator(); i.hasNext();) {
            Notification notification = (Notification) i.next();
            if (isDestroyEvent(notification))
                deletedObjects.add(notification.getNotifier());
        }
        return deletedObjects;
    }

	/**
	 * Returns true if this notification is the equivalent of what used to be a
	 * destroy event. Assumes the notifier is an <code>EObject</code>.
	 * 
	 * @param notification
	 * @return
	 */
	private boolean isDestroyEvent(Notification notification) {		
        return false;
        // TODO:  FIX THIS!
//		return (notification.getEventType() == Notification.REMOVE || notification
//			.getEventType() == Notification.REMOVE_MANY)
//			&& ((EObject) notification.getNotifier()).eContainer() == null;
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
	private void fireNotification(Notification event) {
		Collection listenerList = getInterestedNotificationListeners(event, false);
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

	private Command fireTransactionAboutToCommit(Notification event) {
		Collection listenerList = getInterestedNotificationListeners(event,
			true);
		if (!listenerList.isEmpty()) {
			List listenersSnapShot = new ArrayList(listenerList);
			if (!listenerList.isEmpty()) {
				CompoundCommand cc = new CompoundCommand();
				for (Iterator listenerIT = listenersSnapShot.iterator(); listenerIT
					.hasNext();) {
					NotificationPreCommitListener listener = (NotificationPreCommitListener) listenerIT
						.next();
					Command cmd = listener.transactionAboutToCommit(event);
					if (cmd != null) {
						cc.append(cmd);
					}
				}
				return cc.isEmpty() ? null
					: cc;
			}
		}
		return null;
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
			NotificationPreCommitListener listener) {
		if (target != null) {
			preListeners.addListener(target, LISTEN_TO_ALL_FEATURES, listener);
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
			postListeners.addListener(target, LISTEN_TO_ALL_FEATURES, listener);
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
			EStructuralFeature key, NotificationPreCommitListener listener) {
		if (target != null) {
			preListeners.addListener(target, key, listener);
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
			postListeners.addListener(target, key, listener);
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
			NotificationPreCommitListener listener) {
		if (target != null) {
			preListeners.removeListener(target, LISTEN_TO_ALL_FEATURES, listener);
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
			postListeners.removeListener(target, LISTEN_TO_ALL_FEATURES, listener);
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
			NotificationPreCommitListener listener) {
		if (target != null) {
			preListeners.removeListener(target, key, listener);
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
			postListeners.removeListener(target, key, listener);
		}
	}
	public final void finalize() {
		try {
			for (Iterator iter = instanceMap.keySet().iterator(); iter
				.hasNext();) {
				TransactionalEditingDomain editingDomain = (TransactionalEditingDomain) iter
					.next();
				editingDomain
					.removeResourceSetListener((DiagramEventBroker) ((WeakReference) instanceMap
						.get(editingDomain)).get());
			}
		} catch (Throwable ignored) {
			// intentionally ignored
		}
	}
	
	private Set getNotificationListeners(Object notifier, boolean preCommit) {
		NotifierToKeyToListenersSetMap listeners = preCommit ? preListeners
			: postListeners;
		return listeners.getListeners(notifier, LISTEN_TO_ALL_FEATURES);
	}


	/**
	 * @param notifier
	 * @param key
	 * @param preCommit
	 * @return
	 */
	private Set getNotificationListeners(Object notifier, Object key, boolean preCommit) {
		NotifierToKeyToListenersSetMap listeners = preCommit ? preListeners : postListeners;
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
	
	/**
	 * gets a subset of all the registered listeners who are interested in
	 * receiving the supplied event.
	 * 
	 * @param event
	 *            the event to use
	 * @return the interested listeners in the event
	 */
	private Set getInterestedNotificationListeners(Notification event, boolean preCommit) {
		HashSet listenerSet = new HashSet();

		Collection c = getNotificationListeners(event.getNotifier(), event
			.getFeature(), preCommit);
		if (c != null) {
			listenerSet.addAll(c);
		}
		
		EObject notifier = (EObject) event.getNotifier();
		//the Visibility Event get fired to all interested listeners in the container
		if (NotationPackage.eINSTANCE.getView_Visible().equals(event.getFeature()) &&
			notifier.eContainer()!=null){
				listenerSet.addAll(getNotificationListeners(notifier.eContainer(), preCommit));
		}
		else if (notifier instanceof EAnnotation) {
			addListenersOfNotifier(listenerSet, notifier.eContainer(), event, preCommit);
		} else if (!(notifier instanceof View)) {
			while (notifier != null && !(notifier instanceof View)) {
				notifier = notifier.eContainer();
			}
			addListenersOfNotifier(listenerSet, notifier, event, preCommit);
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
			Notification event, boolean preCommit) {
		if (notifier != null) {
			Collection c = getNotificationListeners(notifier, event
				.getFeature(), preCommit);
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
	private Command handleTransactionAboutToCommitEvent(Notification event) {
		EObject element = (EObject) event.getNotifier();
		if (element != null) {
			return fireTransactionAboutToCommit(event);
		}

		return null;
	}

	/**
	 * Forwards the event to all interested listeners.
	 * 
	 * @param event
	 *            the event to handle
	 */
	private void handleElementEvent(Notification event) {
		
		if (!event.isTouch()) {
			EObject element = (EObject) event.getNotifier();
			while (element != null && !(element instanceof View)) {
				element = element.eContainer();
			}
			if (element != null) {
                if (!NotificationFilter.READ.matches(event)) {
                    ViewUtil.persistElement((View) element);
                }
			}
		}

		EObject element = (EObject) event.getNotifier();
		if (element != null) {
			fireNotification(event);
		}
	}

}
