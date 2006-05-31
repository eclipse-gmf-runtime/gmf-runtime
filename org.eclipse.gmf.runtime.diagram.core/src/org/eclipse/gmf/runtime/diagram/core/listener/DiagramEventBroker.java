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
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.EMFOperationCommand;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.PersistElementCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A model server listener that broadcast EObject events to all registered
 * listeners.
 * 
 * @author melaasar, mmostafa, cmahoney
 */
public class DiagramEventBroker
    extends ResourceSetListenerImpl {

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
    protected DiagramEventBroker() {
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
    	
    	return initializeDiagramEventBroker(editingDomain);
    }

    /**
     * Creates a new diagram event broker instance for the editing domain passed
     * in only if the editing domain does not already have a diagram event
     * broker. There is one diagram event broker per editing domain. Adds the
     * diagram event broker instance as a listener to the editing domain.
     * 
     * @param editingDomain
     */
    public static void startListening(TransactionalEditingDomain editingDomain) {
    	initializeDiagramEventBroker(editingDomain);
    }

	private static DiagramEventBroker initializeDiagramEventBroker(TransactionalEditingDomain editingDomain) {
		WeakReference reference = (WeakReference) instanceMap.get(editingDomain);
		if (reference == null) {
            DiagramEventBroker diagramEventBroker = debFactory.createDiagramEventBroker(editingDomain);
            editingDomain.addResourceSetListener(diagramEventBroker);
            reference = new WeakReference(diagramEventBroker);
            instanceMap.put(editingDomain, reference);
        }
		
		return (DiagramEventBroker) reference.get();
	}
    
    /**
     * Factory interface that can be used to create overrides of the DiagramEventBroker class
     * @author sshaw
     */
    public static interface DiagramEventBrokerFactory {
    	/**
    	 * @param editingDomain the <code>TransactionalEditingDomain</code> that is associated
    	 * with the <code>DiagramEventBroker</code> instance.
    	 * @return the <code>DiagramEventBroker</code> instance.
    	 */
    	public DiagramEventBroker createDiagramEventBroker(TransactionalEditingDomain editingDomain); 
    }
    
    private static class DiagramEventBrokerFactoryImpl implements DiagramEventBrokerFactory {
    	public DiagramEventBroker createDiagramEventBroker(TransactionalEditingDomain editingDomain) {
    		return new DiagramEventBroker();
    	}
    }
    
    private static DiagramEventBrokerFactory debFactory = new DiagramEventBrokerFactoryImpl();
    
    /**
     * @param newDebFactory
     */
    public static void registerDiagramEventBrokerFactory(DiagramEventBrokerFactory newDebFactory) {
    	debFactory = newDebFactory;
    }

    /**
     * @param editingDomain
     */
    public static void stopListening(TransactionalEditingDomain editingDomain) {
        DiagramEventBroker diagramEventBroker = getInstance(editingDomain);
        if (diagramEventBroker != null) {
            editingDomain.removeResourceSetListener(diagramEventBroker);
            instanceMap.remove(editingDomain);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#transactionAboutToCommit(org.eclipse.emf.transaction.ResourceSetChangeEvent)
     */
    public Command transactionAboutToCommit(ResourceSetChangeEvent event) {
        Set deletedObjects = NotificationUtil.getDeletedObjects(event);
        Set elementsInPersistQueue = new HashSet();
        CompoundCommand cc = new CompoundCommand();
        for (Iterator i = event.getNotifications().iterator(); i.hasNext();) {
            final Notification notification = (Notification) i.next();
            if (shouldIgnoreNotification(notification))
                continue;
            Object notifier = notification.getNotifier();
            if (notifier instanceof EObject) {
                if (deletedObjects.contains(notification.getNotifier())) {
                    continue;
                }
                Command cmd = handleTransactionAboutToCommitEvent(notification,elementsInPersistQueue);
                if (cmd != null) {
                    cc.append(cmd);
                }
            }
        }
        elementsInPersistQueue.clear();
        return cc.isEmpty() ? null
            : cc;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#resourceSetChanged(org.eclipse.emf.transaction.ResourceSetChangeEvent)
     */
    public void resourceSetChanged(ResourceSetChangeEvent event) {
        Set deletedObjects = NotificationUtil.getDeletedObjects(event);
        for (Iterator i = event.getNotifications().iterator(); i.hasNext();) {
            final Notification notification = (Notification) i.next();
            if (shouldIgnoreNotification(notification))
                continue;
            Object notifier = notification.getNotifier();
            if (notifier instanceof EObject) {
                if (deletedObjects.contains(notification.getNotifier())) {
                    continue;
                }

                handleElementEvent(notification);
            }
        }
    }
    
    /**
     * determine if the passed notification can be ignored or not the default
     * implementation will ignore touch event if it is not a resolve event, also
     * it will ignore the mutable feature events
     * 
     * @param notification
     *            the notification to check
     * @return true if the notification should be ignored, otherwise false
     */
    protected boolean shouldIgnoreNotification(Notification notification) {
        if ((notification.isTouch() && notification.getEventType() != Notification.RESOLVE)
            || NotationPackage.eINSTANCE.getView_Mutable().equals(
                notification.getFeature())) {
            return true;
        }
        return false;
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
        Collection listenerList = getInterestedNotificationListeners(event,
            false);
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

    private Command fireTransactionAboutToCommit(Notification event,Set elementsInPersistQueue) {
        Collection listenerList = getInterestedNotificationListeners(event,
            true);
        CompoundCommand cc = new CompoundCommand();
        preparePersistCommand(event,cc,elementsInPersistQueue);
        if (!listenerList.isEmpty()) {
            List listenersSnapShot = new ArrayList(listenerList);
            if (!listenerList.isEmpty()) {
                
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
        
        if (cc.isEmpty())
            return null;
        
        return cc;
    }

    private void preparePersistCommand(Notification event, CompoundCommand cc, Set elementsInPersistQueue) {
        PersistElementCommand persistCmd = null;
        if (!event.isTouch()) {
            EObject elementToPersist = (EObject) event.getNotifier();
            while (elementToPersist != null && !(elementToPersist instanceof View)) {
                elementToPersist = elementToPersist.eContainer();
            }
            if (elementToPersist != null && !elementsInPersistQueue.contains(elementToPersist)
                && ViewUtil.isTransient(elementToPersist)) {
                if (!NotificationFilter.READ.matches(event)) {
                    elementsInPersistQueue.add(elementToPersist);
                    persistCmd = getPersistViewCommand((View)elementToPersist);
                }
            }
        }
        if (persistCmd!=null)
            cc.append(new EMFOperationCommand(persistCmd.getEditingDomain(),persistCmd));
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
            preListeners.removeListener(target, LISTEN_TO_ALL_FEATURES,
                listener);
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
            postListeners.removeListener(target, LISTEN_TO_ALL_FEATURES,
                listener);
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
    private Set getNotificationListeners(Object notifier, Object key,
            boolean preCommit) {
        NotifierToKeyToListenersSetMap listeners = preCommit ? preListeners
            : postListeners;
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
    final protected Set getInterestedNotificationListeners(Notification event,
            boolean preCommit) {
        HashSet listenerSet = new HashSet();

        Collection c = getNotificationListeners(event.getNotifier(), event
            .getFeature(), preCommit);
        if (c != null) {
            listenerSet.addAll(c);
        }

        EObject notifier = (EObject) event.getNotifier();
        // the Visibility Event get fired to all interested listeners in the
        // container
        if (NotationPackage.eINSTANCE.getView_Visible().equals(
            event.getFeature())
            && notifier.eContainer() != null) {
            listenerSet.addAll(getNotificationListeners(notifier.eContainer(),
                preCommit));
        } else if (notifier instanceof EAnnotation) {
            addListenersOfNotifier(listenerSet, notifier.eContainer(), event,
                preCommit);
        } else if (!(notifier instanceof View)) {
            while (notifier != null && !(notifier instanceof View)) {
                notifier = notifier.eContainer();
            }
            addListenersOfNotifier(listenerSet, notifier, event, preCommit);
        }
        return listenerSet;
    }
    
    public boolean isAggregatePrecommitListener() {
    	return true;
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
     * @p
     */
    private Command handleTransactionAboutToCommitEvent(Notification event, Set elementsInPersistQueue) {
        EObject element = (EObject) event.getNotifier();
        if (element != null) {
            return fireTransactionAboutToCommit(event,elementsInPersistQueue);
        }
        return null;
    }
    
    
    /**
     * Forwards the event to all interested listeners.
     * 
     * @param event
     *            the event to handle
     * 
     */
    private void handleElementEvent(Notification event) {
        EObject element = (EObject) event.getNotifier();
        if (element != null) {
            fireNotification(event);
        }
    }

    /**
     * Returns a persisted view command. This command if executed will persisted
     * the passed view and all its required parents.
     * @param view the view to persist
     * @return the command to persist the view; the return value can be null
     */
    private PersistElementCommand getPersistViewCommand(View view) {
        PersistElementCommand pvc = null;
        // only immutable views can be persisted
        if (!view.isMutable()) {
            TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(view);
            // get Top view needs to get persisted
            View viewToPersist = ViewUtil.getTopViewToPersist(view);
            if (viewToPersist!=null){
                // now the command that will persist the view
                //Map options = Collections.singletonMap( Transaction.OPTION_UNPROTECTED, Boolean.TRUE);
                pvc = new PersistElementCommand(editingDomain, viewToPersist /*, options*/);
            }
        }
        return pvc;
        
    }
}
