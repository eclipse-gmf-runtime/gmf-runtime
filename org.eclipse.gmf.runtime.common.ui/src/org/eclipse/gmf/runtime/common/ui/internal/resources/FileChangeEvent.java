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

package org.eclipse.gmf.runtime.common.ui.internal.resources;

import org.eclipse.core.resources.IFile;


/**
 * Utility class that describes a file change event.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public class FileChangeEvent {

	/**
	 * the event type.
	 */
	private FileChangeEventType eventType;

	/**
	 * the original file of a rename or move event.
	 */
	private IFile oldFile;

	/**
	 * the event file.
	 */
	private IFile file;

	/**
	 * Constructor for a file change event.
	 * 
	 * @param anEventType
	 *            the event type, either changed or deleted.
	 * @param aFile
	 *            the changed or deleted file.
	 */
	public FileChangeEvent(FileChangeEventType anEventType, IFile aFile) {
		assert (anEventType == FileChangeEventType.CHANGED
			|| anEventType == FileChangeEventType.DELETED);
		setEventType(anEventType);
		setFile(aFile);
		setOldFile(null);
	}

	/**
	 * Constructor for a file change event.
	 * 
	 * @param anEventType
	 *            the event type, either moved or renamed.
	 * @param anOldFile
	 *            the original file before the event.
	 * @param newFile
	 *            the new file after the event.
	 */
	public FileChangeEvent(FileChangeEventType anEventType, IFile anOldFile,
			IFile newFile) {
		assert (anEventType == FileChangeEventType.RENAMED
			|| anEventType == FileChangeEventType.MOVED);
		setEventType(anEventType);
		setFile(newFile);
		setOldFile(anOldFile);
	}

	/**
	 * Get the event type.
	 * 
	 * @return the event type.
	 */
	public FileChangeEventType getEventType() {
		return eventType;
	}

	/**
	 * Get the file for the event.
	 * 
	 * @return the file for the event.
	 */
	public IFile getFile() {
		return file;
	}

	/**
	 * Get the original file before a rename or move the event.
	 * 
	 * @return the original file before a rename or move the event.
	 */
	public IFile getOldFile() {
		return oldFile;
	}

	/**
	 * Set the event type.
	 * 
	 * @param type
	 *            the event type.
	 */
	private void setEventType(FileChangeEventType type) {
		this.eventType = type;
	}

	/**
	 * Set the file for the event.
	 * 
	 * @param aFile
	 *            the file for the event.
	 */
	private void setFile(IFile aFile) {
		this.file = aFile;
	}

	/**
	 * Set the original file before a rename or move the event.
	 * 
	 * @param aFile
	 *            the original file before a rename or move the event.
	 */
	private void setOldFile(IFile aFile) {
		this.oldFile = aFile;
	}

}