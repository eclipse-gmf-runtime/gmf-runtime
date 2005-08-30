/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.notifications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.edit.MFilter;
import org.eclipse.gmf.runtime.emf.core.edit.MListener;
import org.eclipse.gmf.runtime.emf.core.edit.MRunOption;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.edit.MSemProcProvider;
import org.eclipse.gmf.runtime.emf.core.internal.commands.MSLUndoStack;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.IMetamodelSupport;

/**
 * This the MSL event broker class. The MSL uses a single content adapter to
 * listen to EMF notifications. The MSL event broker then forwards these events
 * to the proper listeners.
 * 
 * @author rafikj
 */
public class MSLEventBroker {

	private final MSLEditingDomain domain;

	private final Map listeners = new WeakHashMap();

	private final Map semProcProviders = new WeakHashMap();

	private static final Map universalListeners = new WeakHashMap();

	private PendingEvents pendingEvents = null;

	private final Map undoRedoEvents = new WeakHashMap();

	private boolean sendEventsToListeners = true;

	private boolean sendEventsToMetaModel = true;

	private boolean validateEvents = true;

	private final List eventQueue = new ArrayList();

	private boolean sendingEvents = false;

	/**
	 * Constructor.
	 */
	public MSLEventBroker(MSLEditingDomain domain) {
		super();
		this.domain = domain;
	}

	/**
	 * Adds a listener.
	 */
	public void addListener(MListener listener) {
		listeners.put(listener, new ListenerData());
	}

	/**
	 * Removes a listener.
	 */
	public void removeListener(MListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Adds a SemProc Provider.
	 */
	public void addSemProcProvider(MSemProcProvider listener) {
		semProcProviders.put(listener, new ListenerData());
	}

	/**
	 * Removes a SemProc Provider.
	 */
	public void removeSemProcProvider(MSemProcProvider listener) {
		semProcProviders.remove(listener);
	}

	/**
	 * Adds a listener.
	 */
	public static void addUniversalListener(MListener listener) {
		universalListeners.put(listener, new ListenerData());
	}

	/**
	 * Removes a listener.
	 */
	public static void removeUniversalListener(MListener listener) {
		universalListeners.remove(listener);
	}

	/**
	 * Runs the runnable instance without sending events.
	 */
	public Object runSilent(MRunnable runnable) {

		boolean savedSendEvents = sendEventsToListeners;

		sendEventsToListeners = false;

		Object result = null;

		try {

			result = runnable.run();

		} finally {

			sendEventsToListeners = savedSendEvents;
		}

		return result;
	}

	/**
	 * Runs the runnable instance without semantic procedures.
	 */
	public Object runWithNoSemProcs(MRunnable runnable) {

		boolean savedSendEvents = sendEventsToMetaModel;

		sendEventsToMetaModel = false;

		Object result = null;

		try {

			result = runnable.run();

		} finally {

			sendEventsToMetaModel = savedSendEvents;
		}

		return result;
	}

	/**
	 * Runs the runnable instance without validation.
	 */
	public Object runUnvalidated(MRunnable runnable) {

		boolean savedValidateEvents = validateEvents;

		validateEvents = false;

		Object result = null;

		try {

			result = runnable.run();

		} finally {

			validateEvents = savedValidateEvents;
		}

		return result;
	}

	/**
	 * Runs the runnable instance with options. This method could be used to
	 * combine the effects of runSilent, runUnchecked, runUnvalidated and
	 * runWithNoSemProcs.
	 * 
	 * @see MRunOption
	 */
	public Object runWithOptions(MRunnable runnable, int options) {

		boolean savedSendEventsToListeners = sendEventsToListeners;
		boolean savedSendEventsToMetaModel = sendEventsToMetaModel;
		boolean savedValidateEvents = validateEvents;

		Object result = null;

		try {

			if ((options & MRunOption.SILENT) != 0)
				sendEventsToListeners = false;

			if ((options & MRunOption.NO_SEM_PROCS) != 0)
				sendEventsToMetaModel = false;

			if ((options & MRunOption.UNVALIDATED) != 0)
				validateEvents = false;

			if ((options & MRunOption.UNCHECKED) != 0)
				domain.startUnchecked();

			result = runnable.run();

		} finally {

			if ((options & MRunOption.UNCHECKED) != 0)
				domain.complete();

			sendEventsToListeners = savedSendEventsToListeners;
			sendEventsToMetaModel = savedSendEventsToMetaModel;
			validateEvents = savedValidateEvents;
		}

		return result;
	}

	/**
	 * Obtains the events that are currently awaiting firing to listeners.
	 */
	public List getEventsToValidate() {

		return ((pendingEvents == null) || (!validateEvents)) ? Collections.EMPTY_LIST
			: pendingEvents.detachEventsToValidate();
	}

	/**
	 * Fires pending events.
	 */
	public void fireEvents() {

		List eventsToSend = null;

		if (pendingEvents == null)
			eventsToSend = Collections.EMPTY_LIST;

		else {

			eventsToSend = pendingEvents.detachEventsToSend();
			pendingEvents = null;
		}

		fireEvents(eventsToSend);
	}

	/**
	 * Clears pending events.
	 */
	public void clearEvents() {
		pendingEvents = null;
	}

	/**
	 * Clears events where the notifier is an EObject.
	 */
	public void clearObjectEvents() {

		if (pendingEvents != null)
			pendingEvents.clearEObjectEvents();
	}

	/**
	 * Adds a new event to the pending list of events.
	 */
	public void addEvent(final Object object, int eventType) {

		if (object instanceof InternalEObject)
			addEvent(new ENotificationImpl((InternalEObject) object, eventType,
				null, (Object) null, (Object) null, -1));

		else
			addEvent(new NotificationImpl(eventType, 
				(Object) null, (Object) null, -1) {

				public Object getNotifier() {
					return object;
				}
			});
	}

	/**
	 * Fires an event.
	 */
	public void fireEvent(final Object object, int eventType) {

		fireEvent(new NotificationImpl(eventType, (Object) null,
			(Object) null, -1) {

			public Object getNotifier() {
				return object;
			}
		});
	}

	/**
	 * Adds a new event to the pending list of events.
	 */
	public void addEvent(Notification event) {

		notifyMetaModel(event);

		boolean fireEvent = false;

		if (domain.getUndoStack().isUndoInProgress())
			undoRedoEvents.put(event, Boolean.TRUE);

		else if (domain.getUndoStack().isRedoInProgress())
			undoRedoEvents.put(event, Boolean.FALSE);

		if (pendingEvents != null)
			pendingEvents.add(event);

		else if (domain.getUndoStack().isModifyInProgress()) {

			pendingEvents = new PendingEvents();
			pendingEvents.add(event);

		} else
			fireEvent = true;

		if (fireEvent)
			fireEvents(Collections.singletonList(event));
	}

	/**
	 * Fires an event.
	 */
	private void fireEvent(Notification event) {

		notifyMetaModel(event);

		if (domain.getUndoStack().isUndoInProgress())
			undoRedoEvents.put(event, Boolean.TRUE);

		else if (domain.getUndoStack().isUndoInProgress())
			undoRedoEvents.put(event, Boolean.FALSE);

		fireEvents(Collections.singletonList(event));
	}

	/**
	 * Checks if event is undo event.
	 */
	public boolean isUndoEvent(Notification event) {

		Boolean isUndo = (Boolean) undoRedoEvents.get(event);

		return (isUndo != null) && (isUndo.booleanValue());
	}

	/**
	 * Checks if event is redo event.
	 */
	public boolean isRedoEvent(Notification event) {

		Boolean isUndo = (Boolean) undoRedoEvents.get(event);

		return (isUndo != null) && (!isUndo.booleanValue());
	}

	/**
	 * Notify the meta-model about the modification.
	 */
	public void notifyMetaModel(Notification event) {

		if (sendEventsToMetaModel) {

			Object notifier = event.getNotifier();

			MSLUndoStack undoStack = domain.getUndoStack();

			if (((undoStack.isWriteActionInProgress()) || (undoStack
				.isUncheckedActionInProgress()))
				&& (!undoStack.isUndoInProgress())
				&& (!undoStack.isRedoInProgress())
				&& (!undoStack.isAbandonInProgress())
				&& (notifier instanceof EObject)) {

				IMetamodelSupport metaModel = MSLUtil.getMetaModel((EObject) notifier);

				if (metaModel != null)
					metaModel.handleEvent(event);
			}
		}
	}

	/**
	 * Fires events.
	 */
	private void fireEvents(List events) {

		if ((!sendEventsToListeners) || (events.isEmpty()))
			return;

		List allListeners = getAllListeners();

		int allListenersSize = allListeners.size();

		if (allListenersSize == 0)
			return;

		// enqueue the event to ensure correct event ordering.
		eventQueue.add(events);

		// already sending events.
		if (sendingEvents)
			return;

		sendingEvents = true;

		try {

			for (int minIndex = Integer.MIN_VALUE; minIndex < eventQueue.size();) {

				minIndex = Integer.MAX_VALUE;

				for (int i = 0; i < allListenersSize; i++) {

					MListener listener = (MListener) allListeners.get(i);

					int index = fireEvents(listener);

					if (index < minIndex)
						minIndex = index;
				}
			}

		} finally {

			Iterator i = allListeners.iterator();

			while (i.hasNext()) {

				ListenerData data = getListenerData((MListener) i.next());

				if (data != null)
					data.index = 0;
			}

			eventQueue.clear();

			sendingEvents = false;
		}
	}

	/**
	 * Gets all listeners.
	 */
	private List getAllListeners() {

		List allListeners = new ArrayList(listeners.size()
			+ universalListeners.size() + semProcProviders.size());

		allListeners.addAll(semProcProviders.keySet());
		allListeners.addAll(listeners.keySet());
		allListeners.addAll(universalListeners.keySet());

		return allListeners;
	}

	/**
	 * Gets listener data.
	 */
	private ListenerData getListenerData(MListener listener) {

		ListenerData data = null;
		
		if(listener instanceof MSemProcProvider)
			data = (ListenerData) semProcProviders.get(listener);
		else 
			data = (ListenerData) listeners.get(listener);

		if (data == null)
			data = (ListenerData) universalListeners.get(listener);

		return data;
	}

	/**
	 * Sends events to a listener.
	 */
	private int fireEvents(MListener listener) {

		if ((!listeners.containsKey(listener))
			&& (!universalListeners.containsKey(listener))
			&& (!semProcProviders.containsKey(listener)))
			return Integer.MAX_VALUE;

		MFilter filter = listener.getFilter();

		if (filter == null)
			return Integer.MAX_VALUE;

		ListenerData data = getListenerData(listener);

		if (data == null)
			return Integer.MAX_VALUE;

		while (data.index < eventQueue.size()) {

			List events = (List) eventQueue.get(data.index);

			// process next event list.
			data.index++;

			List eventsToSend = null;

			if (filter == MFilter.WILDCARD_FILTER)
				eventsToSend = events;

			else {

				eventsToSend = new ArrayList();

				Iterator j = events.iterator();

				while (j.hasNext()) {

					Notification event = (Notification) j.next();

					if (filter.matches(event))
						eventsToSend.add(event);
				}
			}

			if (eventsToSend.isEmpty())
				continue;

			try {

				listener.onEvent(eventsToSend);

			} catch (Exception e) {

				// this is a bad listener so remove it so the next
				// listeners can get their events.
				removeListener(listener);

				Log.error(MSLPlugin.getDefault(), 1,
					"removed failed event handler", e); //$NON-NLS-1$

				Trace.catching(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_CATCHING, getClass(),
					"fireEvents", e); //$NON-NLS-1$
			}
		}

		return data.index;
	}

	/**
	 * Class that maintains list of pending events to be sent or to use for
	 * validation purposes.
	 */
	private class PendingEvents {

		private List eventsToSend = new ArrayList();

		private List eventsToValidate = new ArrayList();

		/**
		 * Gets the events to send.
		 */
		public List detachEventsToSend() {

			List events = eventsToSend;
			eventsToSend = null;

			return events;
		}

		/**
		 * Gets the events to use for validation.
		 */
		public List detachEventsToValidate() {

			List events = eventsToValidate;
			eventsToValidate = null;

			return events;
		}

		/**
		 * Adds a pending event.
		 */
		public void add(Notification event) {

			if ((sendEventsToListeners) && (eventsToSend != null))
				eventsToSend.add(event);

			Object notifier = event.getNotifier();

			if ((validateEvents) && (eventsToValidate != null)
				&& !event.isTouch() && (notifier instanceof EObject))
				eventsToValidate.add(event);
		}

		/**
		 * Clears EObject events from pending events.
		 */
		public void clearEObjectEvents() {

			if (eventsToSend != null) {

				List newEventsToSend = new ArrayList();

				Iterator i = eventsToSend.iterator();

				while (i.hasNext()) {

					Notification event = (Notification) i.next();

					Object notifier = event.getNotifier();

					if (!(notifier instanceof EObject))
						newEventsToSend.add(event);
				}

				eventsToSend = newEventsToSend;
			}

			if (eventsToValidate != null)
				eventsToValidate.clear();
		}
	}

	/**
	 * Class that maintains data associated with listeners.
	 */
	private static class ListenerData {

		public int index = 0;
	}
}