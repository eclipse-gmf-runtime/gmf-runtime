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

package org.eclipse.gmf.runtime.common.ui.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;

/**
 * This class provides general methods for operating on files.
 * @author wdiu
 */
public class FileUtil {
	/**
	 * Creates a file resource given the file handle and contents.
	 *
	 * @param fileHandle the file handle to create a file resource with
	 * @param contents the initial contents of the new file resource, or
	 *   <code>null</code> if none (equivalent to an empty stream)
	 * @param monitor the progress monitor to show visual progress with
	 * @exception CoreException if the operation fails
	 */
	static public void createFile(
		IFile fileHandle,
		InputStream contents,
		IProgressMonitor monitor)
		throws CoreException {
		if (contents == null)
			contents = new ByteArrayInputStream(new byte[0]);

		try {
			// Create a new file resource in the workspace
			fileHandle.create(contents, false, monitor);
		} catch (CoreException e) {
			// If the file already existed locally, just refresh to get contents
			if (e.getStatus().getCode() == IResourceStatus.PATH_OCCUPIED)
				fileHandle.refreshLocal(IResource.DEPTH_ZERO, null);
			else {
				Trace.catching(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_CATCHING, CommonUIPlugin.getDefault().getClass(), "createFile", e); //$NON-NLS-1$
				Log.error(CommonUIPlugin.getDefault(), CommonUIStatusCodes.SERVICE_FAILURE, "createFile", e); //$NON-NLS-1$
				Trace.throwing(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_THROWING, CommonUIPlugin.getDefault().getClass(), "createFile", e); //$NON-NLS-1$
				throw e;
			}
		}

		if (monitor.isCanceled())
			throw new OperationCanceledException();
	}

	/**
	 * Deletes a file resource given the file handle.
	 *
	 * @param fileHandle the file handle to delete.
	 * @param monitor the progress monitor to show visual progress with
	 * @exception CoreException if the operation fails
	 */
	static public void deleteFile(IFile fileHandle, IProgressMonitor monitor)
		throws CoreException {
		try {
			// Delete a file resource in the workspace
			fileHandle.delete(true, monitor);
		} catch (CoreException e) {
			Trace.catching(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_CATCHING, CommonUIPlugin.getDefault().getClass(), "createFile", e); //$NON-NLS-1$
			Log.error(CommonUIPlugin.getDefault(), CommonUIStatusCodes.SERVICE_FAILURE, "createFile", e); //$NON-NLS-1$
			Trace.throwing(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_THROWING, CommonUIPlugin.getDefault().getClass(), "createFile", e); //$NON-NLS-1$
			throw e;
		}

		if (monitor.isCanceled())
			throw new OperationCanceledException();
	}
	
	private static final String RELATIVE_STR = ".."; //$NON-NLS-1$

	/**
	 * Build the relative path according to the url path and model path
	 * 
	 * @param urlPathstr
	 *            the url path, make sure the url is a file path, otherwise do
	 *            not change.
	 * @param modelPathstr
	 *            the model's path
	 * @return the relative path
	 */
	public static String getRelativePath(String urlPathstr, String modelPathstr) {
		StringBuffer res = new StringBuffer();
		IPath urlPath = new Path(urlPathstr);
		IPath modelPath = new Path(modelPathstr);
		if (urlPathstr.indexOf("://") > 0) { //$NON-NLS-1$
			return urlPathstr;
		}
		int matchingSegments = urlPath.matchingFirstSegments(modelPath);
		int backSegments = modelPath.segmentCount() - matchingSegments - 1;
		while (backSegments > 0) {
			res.append(RELATIVE_STR); //$NON-NLS-1$
			res.append(File.separatorChar);
			backSegments--;
		}
		int segCount = urlPath.segmentCount();
		for (int i = matchingSegments; i < segCount; i++) {
			if (i > matchingSegments) {
				res.append(File.separatorChar);
			}
			res.append(urlPath.segment(i));
		}
		return res.toString();
	}
	
	
	/**
	 * make the absolute path from relative url path and base model path
	 * 
	 * @param strurlPath
	 *            the URL Path String
	 * @param strmodelPath
	 *            the absolute Model Path
	 * @return the relative path String
	 */
	public static String getAbsolutePath(String strurlPath, String strmodelPath) {
		IPath urlPath = new Path(strurlPath);
		
		// RATLC00535293 - if the file path is already absolute, just return it
		if (urlPath.isAbsolute())
			return strurlPath;
		 
		IPath modelPath = new Path(strmodelPath);
		int rel_level = 0;
		for (int i = 0; i < urlPath.segmentCount(); i++) {
			if (urlPath.segment(i).equals(RELATIVE_STR)) {
				rel_level++;
			}
		}
		urlPath = urlPath.removeFirstSegments(rel_level);
		modelPath = modelPath.removeLastSegments(rel_level + 1);
		urlPath = modelPath.append(urlPath);
		return urlPath.toOSString();
	}
}