/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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
import org.eclipse.core.resources.IFolder;

import org.eclipse.gmf.runtime.common.ui.resources.IFileObserver;

/**
 * Public interface for the File Change Manager.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public interface IFileChangeManager {

	/**
	 * Add a file observer without a filter. The file observer will be notified
	 * of all file change events.
	 * 
	 * @param fileObserver
	 *            the file observer.
	 */
	public void addFileObserver(IFileObserver fileObserver);

	/**
	 * Add a file observer with a file filter. The file observer will be
	 * notified of file change events for the provided file filter.
	 * 
	 * @param fileObserver
	 *            the file observer.
	 * @param fileFilter
	 *            the file filter.
	 */
	public void addFileObserver(IFileObserver fileObserver, IFile fileFilter);

	/**
	 * Add a file observer with a folder filter. The file observer will be
	 * notified of file change events for any file under the provided folder
	 * filter.
	 * 
	 * @param fileObserver
	 *            the file observer.
	 * @param folderFilter
	 *            the folder filter.
	 */
	public void addFileObserver(IFileObserver fileObserver, IFolder folderFilter);

	/**
	 * Add a file observer with a file extension filter. The file observer will
	 * be notified of file change events for any file having the same file
	 * extension as the provided extension filter.
	 * 
	 * @param fileObserver
	 *            the file observer.
	 * @param extensionFilter
	 *            the file extension array filter.
	 */
	public void addFileObserver(IFileObserver fileObserver,
			String[] extensionFilter);

	/**
	 * Remove a file observer.
	 * 
	 * @param fileObserver
	 *            the file observer.
	 */
	public void removeFileObserver(IFileObserver fileObserver);

	/**
	 * Validates that the given files can be modified using the Team
	 * validateEdit support.
	 * 
	 * @param files
	 *            files that are to be modified; these files must all exist in
	 *            the workspace.
	 * @param modificationReason
	 *            a String describing the reason for modifying the file, usually
	 *            the command text.
	 * @return true if it is OK to edit the files.
	 * @see org.eclipse.core.resources.IFileModificationValidator#validateEdit
	 */
	public boolean okToEdit(IFile[] files, String modificationReason);

	/**
	 * Validates that the given file can be saved using the Team validateSave
	 * support.
	 * 
	 * @param file
	 *            the file that is to be saved; this file must exist in the
	 *            workspace.
	 * @return true if it is OK to save the file.
	 * @see org.eclipse.core.resources.IFileModificationValidator#validateSave
	 */
	public boolean okToSave(IFile file);

	/**
	 * Refresh changes made to the file directly on disk with the workspace.
	 * 
	 * @param file
	 *            the file to refresh.
	 */
	public void refreshLocal(IFile file);
}