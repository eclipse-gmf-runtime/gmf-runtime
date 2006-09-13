/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.resources;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileChangeEvent;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileChangeEventType;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileObserverFilter;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileObserverFilterType;
import org.eclipse.gmf.runtime.common.ui.internal.resources.MarkerChangeEvent;
import org.eclipse.gmf.runtime.common.ui.internal.resources.MarkerChangeEventType;

/**
 * Manage the list of file observers for the file change manager.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public class FileObserverManager {

	/**
	 * list of file observers.
	 */
	private Hashtable fileObserverList = new Hashtable();

	/**
	 * Add a file observer with no filter.
	 * 
	 * @param fileObserver
	 *            the file observer class implementing the IFileObserver
	 *            interface.
	 */
	protected void add(IFileObserver fileObserver) {
		trace("...FileObserverManager: addFileObserver " //$NON-NLS-1$
			+ fileObserver.toString() + " filter ALL"); //$NON-NLS-1$
		FileObserverFilter filter = new FileObserverFilter(
			FileObserverFilterType.ALL);
		fileObserverList.put(fileObserver, filter);
	}

	/**
	 * Add a file observer with a file filter.
	 * 
	 * @param fileObserver
	 *            the file observer class implementing the IFileObserver
	 *            interface.
	 * @param fileFilter
	 *            the file filter.
	 */
	protected void add(IFileObserver fileObserver, IFile fileFilter) {
		trace("...FileObserverManager: addFileObserver " //$NON-NLS-1$
			+ fileObserver.toString()
			+ " filter " + fileFilter.getLocation().toOSString()); //$NON-NLS-1$
		FileObserverFilter filter = new FileObserverFilter(
			FileObserverFilterType.FILE, fileFilter);
		fileObserverList.put(fileObserver, filter);
	}

	/**
	 * Add a file observer with a content type filter.
	 * 
	 * @param fileObserver
	 *            the file observer class implementing the IFileObserver
	 *            interface.
	 * @param contentTypeFilter
	 *            the content type array filter.
	 */
	protected void add(IFileObserver fileObserver, IContentType[] contentTypeFilter) {
		trace("...FileObserverManager: addFileObserver " //$NON-NLS-1$
			+ fileObserver.toString()
			+ " filter " + contentTypeFilter.toString()); //$NON-NLS-1$
		FileObserverFilter filter = new FileObserverFilter(
			FileObserverFilterType.CONTENT_TYPE, contentTypeFilter);
		fileObserverList.put(fileObserver, filter);
	}

	/**
	 * Add a file observer with a folder filter.
	 * 
	 * @param fileObserver
	 *            the file observer class implementing the IFileObserver
	 *            interface.
	 * @param folderFilter
	 *            the folder filter.
	 */
	protected void add(IFileObserver fileObserver, IFolder folderFilter) {
		trace("...FileObserverManager: addFileObserver " //$NON-NLS-1$
			+ fileObserver.toString()
			+ " filter folder" + folderFilter.getLocation().toOSString()); //$NON-NLS-1$
		FileObserverFilter filter = new FileObserverFilter(
			FileObserverFilterType.FOLDER, folderFilter);
		fileObserverList.put(fileObserver, filter);
	}

	/**
	 * Add a file observer with a file extension filter.
	 * 
	 * @param fileObserver
	 *            the file observer class implementing the IFileObserver
	 *            interface.
	 * @param extensionFilter
	 *            the file extension array filter.
	 */
	protected void add(IFileObserver fileObserver, String[] extensionFilter) {
		trace("...FileObserverManager: addFileObserver " //$NON-NLS-1$
			+ fileObserver.toString() + " filter " + extensionFilter); //$NON-NLS-1$
		FileObserverFilter filter = new FileObserverFilter(
			FileObserverFilterType.EXTENSION, extensionFilter);
		fileObserverList.put(fileObserver, filter);
	}

	/**
	 * Remove a file observer.
	 * 
	 * @param fileObserver
	 *            the file observer class implementing the IFileObserver
	 *            interface.
	 */
	protected void remove(IFileObserver fileObserver) {
		if (fileObserverList.containsKey(fileObserver)) {
			trace("...FileObserverManager: removeFileObserver " + fileObserver.toString()); //$NON-NLS-1$
			fileObserverList.remove(fileObserver);
		}
	}

	/**
	 * Notify file observers of a file change event.
	 * 
	 * @param event
	 *            the file change event.
	 */
	protected void notify(FileChangeEvent event) {

		// Avoid concurrency problems when multiple threads are modifying
		// the fileObserverList
		Hashtable fileObserverListCopy = (Hashtable) fileObserverList.clone();

		for (Iterator i = fileObserverListCopy.entrySet().iterator(); i
			.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			IFileObserver fileObserver = (IFileObserver) entry.getKey();
			FileObserverFilter filter = (FileObserverFilter) entry.getValue();
			if (event.getEventType() == FileChangeEventType.MOVED) {
				if (filter.matches(event.getOldFile())) {
					trace("...FileObserverManager: Notify " //$NON-NLS-1$
						+ fileObserver.toString() + " handleFileMoved"); //$NON-NLS-1$
					fileObserver.handleFileMoved(event.getOldFile(), event
						.getFile());
				}
			} else if (event.getEventType() == FileChangeEventType.RENAMED) {
				if (filter.matches(event.getOldFile())) {
					trace("...FileObserverManager: Notify " //$NON-NLS-1$
						+ fileObserver.toString() + " handleFileRenamed"); //$NON-NLS-1$
					fileObserver.handleFileRenamed(event.getOldFile(), event
						.getFile());
				}
			} else if (event.getEventType() == FileChangeEventType.DELETED) {
				if (filter.matches(event.getFile())) {
					trace("...FileObserverManager: Notify " //$NON-NLS-1$
						+ fileObserver.toString() + " handleFileDeleted"); //$NON-NLS-1$
					fileObserver.handleFileDeleted(event.getFile());
				}
			} else if (event.getEventType() == FileChangeEventType.CHANGED) {
				if (filter.matches(event.getFile())) {
					trace("...FileObserverManager: Notify " //$NON-NLS-1$
						+ fileObserver.toString() + " handleFileChanged"); //$NON-NLS-1$
					fileObserver.handleFileChanged(event.getFile());
				}
			}
		}
	}

	/**
	 * Notify file observers of a marker change event.
	 * 
	 * @param event
	 *            the marker change event.
	 */
	protected void notify(MarkerChangeEvent event) {

		// Avoid concurrency problems when multiple threads are modifying
		// the fileObserverList
		Hashtable fileObserverListCopy = (Hashtable) fileObserverList.clone();

		for (Iterator i = fileObserverListCopy.entrySet().iterator(); i
			.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			IFileObserver fileObserver = (IFileObserver) entry.getKey();
			FileObserverFilter filter = (FileObserverFilter) entry.getValue();
			if (filter.matches(event.getMarker().getResource())) {
				if (event.getEventType() == MarkerChangeEventType.ADDED) {
					trace("...FileObserverManager: Notify " //$NON-NLS-1$
						+ fileObserver.toString() + " handleMarkerAdded"); //$NON-NLS-1$
					fileObserver.handleMarkerAdded(event.getMarker());
				} else if (event.getEventType() == MarkerChangeEventType.CHANGED) {
					trace("...FileObserverManager: Notify " //$NON-NLS-1$
						+ fileObserver.toString() + " handleMarkerChanged"); //$NON-NLS-1$
					fileObserver.handleMarkerChanged(event.getMarker());
				} else if (event.getEventType() == MarkerChangeEventType.REMOVED) {
					trace("...FileObserverManager: Notify " //$NON-NLS-1$
						+ fileObserver.toString() + " handleMarkerDeleted"); //$NON-NLS-1$
					fileObserver.handleMarkerDeleted(event.getMarker(), event
						.getAttributes());
				}
			}
		}
	}

	/**
	 * Print a trace message if tracing is on for file change management.
	 * 
	 * @param message
	 *            the trace message to print.
	 */
	private void trace(String message) {
		if (Trace.shouldTrace(CommonUIPlugin.getDefault(),
			CommonUIDebugOptions.RESOURCE)) {
			Trace.trace(CommonUIPlugin.getDefault(), message);
		}
	}
}