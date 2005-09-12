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

package org.eclipse.gmf.runtime.emf.core.internal.plugin;

import java.text.MessageFormat;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.edit.DemuxingMListener;
import org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener;
import org.eclipse.gmf.runtime.emf.core.edit.MFilter;
import org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;

/**
 * Listens for model events and displays trace output.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public class TraceEventListener
	implements IDemuxedMListener {

	/**
	 * model event listener
	 */
	private DemuxingMListener eventListener = new DemuxingMListener(this);

	private static String MODEL_OPENED = "[event] Resource Loaded {0}";//$NON-NLS-1$

	private static String MODEL_CLOSED = "[event] Resource Unloaded {0}";//$NON-NLS-1$

	private static String MODEL_DIRTIED = "[event] Resource Dirtied {0}";//$NON-NLS-1$

	private static String MODEL_SAVED = "[event] Resource Saved {0}";//$NON-NLS-1$

	private static String MODEL_IMPORT = "[event] Resource Imported {0}";//$NON-NLS-1$

	private static String MODEL_EXPORT = "[event] Resource Exported {0}";//$NON-NLS-1$

	private static String ELEMENT_CREATED = "[event] Element Created {0} in parent {1}";//$NON-NLS-1$

	private static String ELEMENT_DELETED = "[event] Element Deleted {0} from parent {1}";//$NON-NLS-1$

	private static String ELEMENT_MODIFIED = "[event] Element Modified {0}";//$NON-NLS-1$

	private static String UNDO_INTERVAL_CLOSED = "[event] Closed Undo Interval {0}";//$NON-NLS-1$

	private static String UNDO_INTERVALS_FLUSHED = "[event] Undo Interval Flushed";//$NON-NLS-1$

	/**
	 * Get the class name from the element.
	 * 
	 * @param element
	 *            the element.
	 * @return the class name.
	 */
	private String getClassName(EObject element) {
		String className = element.getClass().getName();
		return className.substring(className.lastIndexOf('.') + 1);
	}

	public MFilter getFilter() {
		return MFilter.WILDCARD_FILTER;
	}

	public void handleElementCreatedEvent(Notification notification,
			final EObject owner, final EObject newElement) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			MessageFormat.format(ELEMENT_CREATED, new String[] {
				getClassName(newElement), getClassName(owner)}));
	}

	public void handleElementDeletedEvent(Notification notification,
			final EObject owner, final EObject oldElement) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			MessageFormat.format(ELEMENT_DELETED, new String[] {
				getClassName(oldElement), getClassName(owner)}));
	}

	public void handleElementModifiedEvent(Notification notification,
			final EObject element) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			MessageFormat.format(ELEMENT_MODIFIED,
				new String[] {getClassName(element),}));
	}

	public void handleResourceUnloadedEvent(Notification notification,
			final Resource resource, EObject modelRoot) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			MessageFormat.format(MODEL_CLOSED, new String[] {ResourceUtil
				.getFilePath(resource)}));
	}

	public void handleResourceDirtiedEvent(Notification notification,
			final Resource resource) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			MessageFormat.format(MODEL_DIRTIED, new String[] {ResourceUtil
				.getFilePath(resource)}));
	}

	public void handleResourceLoadedEvent(Notification notification,
			final Resource resource) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			MessageFormat.format(MODEL_OPENED, new String[] {ResourceUtil
				.getFilePath(resource)}));
	}

	public void handleResourceSavedEvent(Notification notification,
			final Resource resource) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			MessageFormat.format(MODEL_SAVED, new String[] {ResourceUtil
				.getFilePath(resource)}));
	}

	public void handleResourceImportedEvent(Notification notification,
			final Resource resource) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			MessageFormat.format(MODEL_IMPORT, new String[] {ResourceUtil
				.getFilePath(resource)}));
	}

	public void handleResourceExportedEvent(Notification notification,
			final Resource resource) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			MessageFormat.format(MODEL_EXPORT, new String[] {ResourceUtil
				.getFilePath(resource)}));
	}

	/**
	 * Create a new TraceEventListener.
	 */
	public TraceEventListener() {
		eventListener.startListening();
	}

	public void handleUndoIntervalClosedEvent(Notification notification,
			final MUndoInterval undoInterval) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			MessageFormat.format(UNDO_INTERVAL_CLOSED,
				new String[] {undoInterval.getLabel()}));
	}

	public void handleUndoIntervalsFlushedEvent(Notification notification,
			MUndoInterval undoInterval) {
		Trace.trace(MSLPlugin.getDefault(), MSLDebugOptions.EVENTS,
			UNDO_INTERVALS_FLUSHED);
	}
}