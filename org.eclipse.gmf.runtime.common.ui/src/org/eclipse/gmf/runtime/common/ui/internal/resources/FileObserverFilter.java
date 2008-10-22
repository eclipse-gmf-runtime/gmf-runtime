/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.content.IContentType;


/**
 * Utility class that describes a file observer filter.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public class FileObserverFilter {

	/**
	 * the filter type.
	 */
	private FileObserverFilterType filterType;

	/**
	 * the filter.
	 */
	private Object filter;

	/**
	 * Create a file observer filter that will not filter out any events.
	 * 
	 * @param aFilterType
	 *            The filter type, all.
	 */
	public FileObserverFilter(FileObserverFilterType aFilterType) {
		assert (aFilterType == FileObserverFilterType.ALL);
		setFilterType(aFilterType);
		setFilter(null);
	}

	/**
	 * Create a file observer filter that will filter out any events not for the
	 * provided file extension.
	 * 
	 * @param aFilterType
	 *            The filter type, extension.
	 * @param extension
	 *            The file extension array filter.
	 */
	public FileObserverFilter(FileObserverFilterType aFilterType,
			String[] extension) {
		assert (aFilterType == FileObserverFilterType.EXTENSION);
		setFilterType(aFilterType);
		setFilter(extension);
	}

	/**
	 * Create a file observer filter that will filter out any events not for the
	 * provided content types.
	 * 
	 * @param aFilterType
	 *            The filter type, content type.
	 * @param extension
	 *            The file content type array filter.
	 */
	public FileObserverFilter(FileObserverFilterType aFilterType,
			IContentType[] contentType) {
		assert (aFilterType == FileObserverFilterType.CONTENT_TYPE);
		setFilterType(aFilterType);
		setFilter(contentType);
	}

	/**
	 * Create a file observer filter that will filter out any events not for
	 * children files under the provided folder.
	 * 
	 * @param aFilterType
	 *            The filter type, folder.
	 * @param folder
	 *            The folder filter.
	 */
	public FileObserverFilter(FileObserverFilterType aFilterType, IFolder folder) {
		assert (aFilterType == FileObserverFilterType.FOLDER);
		setFilterType(aFilterType);
		setFilter(folder);
	}

	/**
	 * Create a file observer filter that will filter out any events not for the
	 * provided file.
	 * 
	 * @param aFilterType
	 *            The filter type, all.
	 * @param file
	 *            The file filter.
	 */
	public FileObserverFilter(FileObserverFilterType aFilterType, IFile file) {
		assert (aFilterType == FileObserverFilterType.FILE);
		setFilterType(aFilterType);
		setFilter(file);
	}

	/**
	 * Determines if the filter matches the provided resource.
	 * 
	 * @param resource
	 *            the resource.
	 * @return true if the filter matches the provided resource and the event
	 *         should be given to the file change observer.
	 */
	public boolean matches(IResource resource) {
		if (getFilterType() == FileObserverFilterType.ALL) {
			return true;
		}
		if (getFilterType() == FileObserverFilterType.FILE
			&& resource instanceof IFile
			&& getAbsolutePath(getFileFilter()).equals(
				getAbsolutePath(resource))) {
			return true;

		}
		if (getFilterType() == FileObserverFilterType.FOLDER
				&& resource instanceof IFile
				&& getAbsolutePath(resource).startsWith(
					getAbsolutePath(getFolderFilter()))) {
				return true;
			}
		if (getFilterType() == FileObserverFilterType.CONTENT_TYPE
				&& resource instanceof IFile
				&& matchesContentType(((IFile)resource).getName())) {
				return true;
			}
		if (getFilterType() == FileObserverFilterType.EXTENSION
			&& resource instanceof IFile) {
			String fileExtension = resource.getFullPath().getFileExtension();
			if (matchesExtension(fileExtension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if the filter matches the provided path.
	 * 
	 * @param path
	 *            the path.
	 * @return true if the filter matches the provided path and the event should
	 *         be given to the file change observer.
	 */
	public boolean matches(IPath path) {
		if (getFilterType() == FileObserverFilterType.ALL) {
			return true;
		}
		if (getFilterType() == FileObserverFilterType.FILE
			&& getFileFilter().getFullPath().equals(path)) {
			return true;
		}
		if (getFilterType() == FileObserverFilterType.FOLDER
				&& path.isPrefixOf(getFolderFilter().getFullPath())) {
				return true;
			}
		if (getFilterType() == FileObserverFilterType.CONTENT_TYPE
				&& matchesContentType(path.segment(path.segmentCount()-1))) {
				return true;
			}
		if (getFilterType() == FileObserverFilterType.EXTENSION
			&& matchesExtension(path.getFileExtension())) {
			return true;
		}
		return false;
	}

	/**
	 * Determines if the file name matches observed content types
	 * 
	 * @param fileName
	 *            file name to be matched
	 * @return true if the file name matches observed content types
	 */
	private boolean matchesContentType(String fileName) {
		IContentType[] contentTypes = getContentTypeFilter();
		for (int i = 0; i < contentTypes.length; i++) {
			if (contentTypes[i].isAssociatedWith(fileName)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Determines if the extension matches observed extensions
	 * 
	 * @param extension
	 *            Extension to be matched
	 * @return true if the extension matches observed extensions
	 */
	private boolean matchesExtension(String extension) {
		String[] extensions = getExtensionFilter();
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i].equals(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the file filter.
	 * 
	 * @return the file filter.
	 */
	private IFile getFileFilter() {
		assert (filterType == FileObserverFilterType.FILE);
		return (IFile) filter;
	}

	/**
	 * Get the folder filter.
	 * 
	 * @return the folder filter.
	 */
	private IFolder getFolderFilter() {
		assert (filterType == FileObserverFilterType.FOLDER);
		return (IFolder) filter;
	}

	/**
	 * Get the file extension filter.
	 * 
	 * @return the file extension array filter.
	 */
	private String[] getExtensionFilter() {
		assert (filterType == FileObserverFilterType.EXTENSION);
		return (String[]) filter;
	}

	/**
	 * Get the content type filter.
	 * 
	 * @return the content type array filter.
	 */
	private IContentType[] getContentTypeFilter() {
		assert (filterType == FileObserverFilterType.CONTENT_TYPE);
		return (IContentType[]) filter;
	}

	/**
	 * Get the file observer filter type.
	 * 
	 * @return the file observer filter type.
	 */
	private FileObserverFilterType getFilterType() {
		return filterType;
	}

	/**
	 * Set the filter.
	 * 
	 * @param object
	 *            the filter.
	 */
	private void setFilter(Object object) {
		filter = object;
	}

	/**
	 * Set the file observer filter type.
	 * 
	 * @param type
	 *            the file observer filter type.
	 */
	private void setFilterType(FileObserverFilterType type) {
		filterType = type;
	}

	/**
	 * Get the path for a resource. In the case of a moved or deleted resource,
	 * resource.getLocation() returns null since it does not exist in the
	 * workspace. The workaround is below.
	 * 
	 * @param resource
	 *            the resource.
	 * @return the path for a resource.
	 */
	private String getAbsolutePath(IResource resource) {
		if (resource.getLocationURI() == null) {
			return resource.getFullPath().toString();
		} else {
			return resource.getLocationURI().toString();
		}
	}
}