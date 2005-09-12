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

package org.eclipse.gmf.runtime.common.ui.resources;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;

/**
 * A file observer is notified of changes to files in the workspace.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public interface IFileObserver {

	/**
	 * handle the file renamed event.
	 * 
	 * @param oldFile
	 *            the original file.
	 * @param file
	 *            the renamed file.
	 */
	public void handleFileRenamed(IFile oldFile, IFile file);

	/**
	 * handle the file moved event.
	 * 
	 * @param oldFile
	 *            the original file.
	 * @param file
	 *            the moved file.
	 */
	public void handleFileMoved(IFile oldFile, IFile file);

	/**
	 * handle the file deleted event.
	 * 
	 * @param file
	 *            the deleted file.
	 */
	public void handleFileDeleted(IFile file);

	/**
	 * handle the file content changed event.
	 * 
	 * @param file
	 *            the changed file.
	 */
	public void handleFileChanged(IFile file);

	/**
	 * handle the marker added event
	 * 
	 * @param marker
	 *            the added marker
	 */
	public void handleMarkerAdded(IMarker marker);

	/**
	 * Handle the marker deleted event.
	 * 
	 * Note that if the marker was deleted, it cannot be used to access
	 * attributes. Use the attribute map parameter to access the old marker's
	 * attributes.
	 * 
	 * @param marker
	 *            the deleted marker
	 * @param attributes
	 *            the old marker's attributes.
	 */
	public void handleMarkerDeleted(IMarker marker, Map attributes);

	/**
	 * handle the marker changed event
	 * 
	 * @param marker
	 *            the changed marker
	 */
	public void handleMarkerChanged(IMarker marker);
}