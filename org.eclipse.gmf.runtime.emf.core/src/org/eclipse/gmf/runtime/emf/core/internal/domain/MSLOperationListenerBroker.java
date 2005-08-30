/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.IOperationEvent;
import org.eclipse.gmf.runtime.emf.core.OperationListener;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MFilter;
import org.eclipse.gmf.runtime.emf.core.edit.MListener;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;

/**
 * @author rafikj
 */
public class MSLOperationListenerBroker
	extends MListener {

	private Set listeners = new HashSet();

	/**
	 * Constructor.
	 */
	public MSLOperationListenerBroker(MEditingDomain domain) {
		super(domain, MFilter.WILDCARD_FILTER);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.EditingDomain#addOperationListener(org.eclipse.gmf.runtime.emf.core.OperationListener)
	 */
	public void addOperationListener(OperationListener listener) {

		if (listener == null)
			return;

		startListening();

		listeners.add(listener);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.EditingDomain#removeOperationListener(org.eclipse.gmf.runtime.emf.core.OperationListener)
	 */
	public void removeOperationListener(OperationListener listener) {

		if (listener == null)
			return;

		listeners.remove(listener);

		stopListening();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MListener#onEvent(java.util.List)
	 */
	public void onEvent(List events) {

		if (events.isEmpty())
			return;

		Notification firstEvent = (Notification) events.get(0);

		boolean done = true;
		boolean undone = false;
		boolean redone = false;

		if (domain.isUndoNotification(firstEvent)) {

			done = false;
			undone = true;

		} else if (domain.isRedoNotification(firstEvent)) {

			done = false;
			redone = true;
		}

		final IOperationEvent event = new MSLOperationEvent(events);

		Object[] allListeners = listeners.toArray();

		for (int i = 0; i < allListeners.length; i++) {

			final OperationListener listener = (OperationListener) allListeners[i];

			try {
				if (done)
					listener.done(event);

				else if (undone)
					listener.undone(event);

				else if (redone)
					listener.redone(event);
			} catch (Exception e) {

				// this is a bad listener so remove it so the next
				// listeners can get their events.
				removeOperationListener(listener);

				Log.error(MSLPlugin.getDefault(), 1,
					"removed failed event handler", e); //$NON-NLS-1$

				Trace.catching(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_CATCHING, getClass(),
					"onEvent", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MListener#startListening()
	 */
	public void startListening() {
		if (listeners.isEmpty())
			super.startListening();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MListener#stopListening()
	 */
	public void stopListening() {
		if (listeners.isEmpty())
			super.stopListening();
	}
}