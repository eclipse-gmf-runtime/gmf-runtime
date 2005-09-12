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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;


/**
 * Interface for a class interested in individual resource and object
 * lifecycle events.
 * <p>
 * This interface is intended to be implemented by clients.  However,
 * it is often more convenient to extend the {@link DemuxedMListener} class
 * and redefine only the interesting callback methods.
 * </p><p>
 * The {@link IDemuxedMListener2} interface defines additional callbacks for
 * events pertaining to the management of separate elements in a logical
 * resource.
 * </p>
 * 
 * @author Anthony Hunter
 * 
 * @see DemuxingMListener
 * @see DemuxedMListener
 */
public interface IDemuxedMListener {

	/**
	 * This method is invoked by the {@link DemuxingMListener} to get a
	 * filter describing the events of interest to the implementing class.
	 * If multiple filters are required, they should be composed using
	 * an {@linkplain MFilter.Or or} filter.
	 * 
	 * @return the filter required, or <code>null</code> if no filtering
	 *     is needed (equivalent to {@link MFilter#WILDCARD_FILTER})
	 */
	public MFilter getFilter();

	/**
	 * Notifies me of an event indicating that a resource has been unloaded.
	 * 
	 * @param notification the raw notification
	 * @param resource the resource that was unloaded
	 * @param root the root object of the <code>resource</code>, which is now
	 *     unloaded and therefore not actually accessible from the
	 *     <code>resource</code>
	 */
	public void handleResourceUnloadedEvent(Notification notification,
		Resource resource, EObject root);

	/**
	 * Notifies me of an event indicating that a resource has been loaded.
	 * 
	 * @param notification the raw notification
	 * @param resource the resource that was loaded
	 */
	public void handleResourceLoadedEvent(Notification notification,
		Resource resource);

	/**
	 * Notifies me of an event indicating that a resource has been dirtied (is
	 * now different from the saved state).
	 * 
	 * @param notification the raw notification
	 * @param resource the resource that was dirtied
	 */
	public void handleResourceDirtiedEvent(Notification notification,
		Resource resource);

	/**
	 * Notifies me of an event indicating that a resource has been saved.
	 * 
	 * @param notification the raw notification
	 * @param resource the resource that was saved
	 */
	public void handleResourceSavedEvent(Notification notification,
		Resource resource);

	/**
	 * Notifies me of an event indicating that a resource has added a reference
	 * dependency (import) on another resource.
	 * 
	 * @param notification the raw notification
	 * @param resource the resource that has a new import
	 */
	public void handleResourceImportedEvent(Notification notification,
		Resource resource);
	
	/**
	 * Notifies me of an event indicating that a resource has been referenced
	 * (imported) by another resource.
	 * 
	 * @param notification the raw notification
	 * @param resource the resource that has a new export
	 */
	public void handleResourceExportedEvent(Notification notification,
		Resource resource);
	
	/**
	 * Notifies me of an event indicating that an element has been created in
	 * a resource.
	 * 
	 * @param notification the raw notification
	 * @param container the container of the new element
	 * @param newElement the new element
	 */
	public void handleElementCreatedEvent(Notification notification,
		EObject container, EObject newElement);

	/**
	 * Notifies me of an event indicating that an element has been removed from
	 * a resource.
	 * 
	 * @param notification the raw notification
	 * @param container the former container of the element
	 * @param oldElement the element that was removed
	 */
	public void handleElementDeletedEvent(Notification notification,
		EObject container, EObject oldElement);

	/**
	 * Notifies me of an event indicating that an element has been modified.
	 * 
	 * @param notification the raw notification
	 * @param element the changed element
	 */
	public void handleElementModifiedEvent(Notification notification,
		EObject element);

	/**
	 * Notifies me of an event indicating that an undo interval has been closed.
	 * 
	 * @param notification the raw notification
	 * @param undoInterval the closed undo interval
	 */
	public void handleUndoIntervalClosedEvent(Notification notification,
		MUndoInterval undoInterval);

	/**
	 * Notifies me of an event indicating that all of the undo intervals up to
	 * and including the specified interval have been flush from the stack.
	 * 
	 * @param notification the raw notification
	 * @param undoInterval the flushed undo interval
	 */
	public void handleUndoIntervalsFlushedEvent(Notification notification,
		MUndoInterval undoInterval);
}
