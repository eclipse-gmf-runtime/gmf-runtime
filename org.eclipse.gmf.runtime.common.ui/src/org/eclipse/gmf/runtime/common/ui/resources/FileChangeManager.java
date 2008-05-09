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

package org.eclipse.gmf.runtime.common.ui.resources;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileChangeEvent;
import org.eclipse.gmf.runtime.common.ui.internal.resources.FileChangeEventType;
import org.eclipse.gmf.runtime.common.ui.internal.resources.IFileChangeManager;
import org.eclipse.gmf.runtime.common.ui.internal.resources.MarkerChangeEvent;
import org.eclipse.gmf.runtime.common.ui.internal.resources.MarkerChangeEventType;

/**
 * The file change manager handles changes made to file resources within the
 * Eclipse workspace. Files in the workspace are affected by change events on
 * the files themselves as well as change events on the project and folder that
 * contains these files.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public class FileChangeManager
	implements IResourceChangeListener, IResourceDeltaVisitor,
	IFileChangeManager {

	/**
	 * singleton instance of this class
	 */
	private static FileChangeManager INSTANCE = new FileChangeManager();

	/**
	 * get the singleton instance of this class
	 * 
	 * @return singleton instance of the FileChangeManager class
	 */
	public static FileChangeManager getInstance() {
		return INSTANCE;
	}

	/**
	 * list of resource observers
	 */
	private FileObserverManager fileObserverManager = new FileObserverManager();

	/**
	 * Simple constructor.
	 */
	private FileChangeManager() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
			IResourceChangeEvent.POST_CHANGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public final void resourceChanged(IResourceChangeEvent event) {
		switch (event.getType()) {
			case IResourceChangeEvent.POST_CHANGE:
				try {
					event.getDelta().accept(this);
				} catch (CoreException e) {
					Trace.catching(CommonUIPlugin.getDefault(),
						CommonUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
						"resourceChanged", e); //$NON-NLS-1$
					Log.warning(CommonUIPlugin.getDefault(),
						CommonUIStatusCodes.IGNORED_EXCEPTION_WARNING, e
							.getMessage(), e);
				}
				break;
			default:
				break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	 */
	public final boolean visit(IResourceDelta delta) {
		switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
					if (delta.getMovedFromPath().removeLastSegments(1)
						.equals(
							delta.getResource().getFullPath()
								.removeLastSegments(1))) {
						trace("...FileChangeManager: Resource " //$NON-NLS-1$
							+ getAbsolutePath(delta.getResource())
							+ " was renamed from " //$NON-NLS-1$
							+ delta.getMovedFromPath().toString());
						if (delta.getResource() instanceof IFile) {
							FileChangeEvent event = new FileChangeEvent(
								FileChangeEventType.RENAMED,
								(IFile) getMovedFromResource(delta),
								(IFile) delta.getResource());
							fileObserverManager.notify(event);
						}
					} else {
						trace("...FileChangeManager: Resource " //$NON-NLS-1$
							+ getAbsolutePath(delta.getResource())
							+ " was moved from " //$NON-NLS-1$
							+ delta.getMovedFromPath().toString());
						if (delta.getResource() instanceof IFile) {
							FileChangeEvent event = new FileChangeEvent(
								FileChangeEventType.MOVED,
								(IFile) getMovedFromResource(delta),
								(IFile) delta.getResource());
							fileObserverManager.notify(event);
						}
					}
				} else {
					trace("...FileChangeManager: Resource " //$NON-NLS-1$ 
						+ getAbsolutePath(delta.getResource()) + " was added"); //$NON-NLS-1$ 
				}
				break;
			case IResourceDelta.REMOVED:
				if ((delta.getFlags() & IResourceDelta.MOVED_TO) == 0) {
					trace("...FileChangeManager: Resource " //$NON-NLS-1$
						+ getAbsolutePath(delta.getResource()) + " was deleted"); //$NON-NLS-1$
					if (delta.getResource() instanceof IFile) {
						FileChangeEvent event = new FileChangeEvent(
							FileChangeEventType.DELETED, (IFile) delta
								.getResource());
						fileObserverManager.notify(event);
					}
				}
				break;
			case IResourceDelta.CHANGED:
				trace("...FileChangeManager: Resource " //$NON-NLS-1$
					+ getAbsolutePath(delta.getResource()) + " was changed"); //$NON-NLS-1$
				if ((delta.getFlags() & IResourceDelta.MARKERS) != 0) {
					// fire notifications if markers have been
					// added/removed/changed
					List markers = Arrays.asList(delta.getMarkerDeltas());
					for (Iterator i = markers.iterator(); i.hasNext();) {
						trace("...FileChangeManager: Resource marker of " //$NON-NLS-1$
							+ getAbsolutePath(delta.getResource())
							+ " was changed"); //$NON-NLS-1$

						MarkerChangeEvent event = null;
						IMarkerDelta markerDelta = (IMarkerDelta) i.next();
						switch (markerDelta.getKind()) {
							case IResourceDelta.ADDED:
								event = new MarkerChangeEvent(
									MarkerChangeEventType.ADDED, markerDelta
										.getMarker());
								fileObserverManager.notify(event);
								break;
							case IResourceDelta.REMOVED:
								event = new MarkerChangeEvent(
									MarkerChangeEventType.REMOVED, markerDelta
										.getMarker(), markerDelta
										.getAttributes());
								fileObserverManager.notify(event);
								break;
							case IResourceDelta.CHANGED:
								event = new MarkerChangeEvent(
									MarkerChangeEventType.CHANGED, markerDelta
										.getMarker());
								fileObserverManager.notify(event);
								break;
							default:
								break;
						}
					}
				} else if (delta.getResource() instanceof IFile) {
					FileChangeEvent event = new FileChangeEvent(
						FileChangeEventType.CHANGED, (IFile) delta
							.getResource());
					fileObserverManager.notify(event);
				}
				break;
			default:
				break;
		}
		return true;
	}

	/**
	 * Print a trace message if tracing is on for resource management.
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileChangeManager#okToEdit(org.eclipse.core.resources.IFile[],
	 *      java.lang.String)
	 */
	public boolean okToEdit(IFile[] files, String modificationReason) {
		return FileModificationValidator.getInstance().okToEdit(files,
			modificationReason);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileChangeManager#okToSave(org.eclipse.core.resources.IFile)
	 */
	public boolean okToSave(IFile file) {
		return FileModificationValidator.getInstance().okToSave(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileChangeManager#removeFileObserver(org.eclipse.gmf.runtime.common.ui.resources.IFileObserver)
	 */
	public void removeFileObserver(IFileObserver fileObserver) {
		fileObserverManager.remove(fileObserver);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileChangeManager#addFileObserver(org.eclipse.gmf.runtime.common.ui.resources.IFileObserver,
	 *      org.eclipse.core.resources.IFile)
	 */
	public void addFileObserver(IFileObserver fileObserver, IFile fileFilter) {
		fileObserverManager.add(fileObserver, fileFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileChangeManager#addFileObserver(org.eclipse.gmf.runtime.common.ui.resources.IFileObserver,
	 *      org.eclipse.core.runtime.content.IContentType[])
	 */
	public void addFileObserver(IFileObserver fileObserver, IContentType[] contentTypeFilter) {
		fileObserverManager.add(fileObserver, contentTypeFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileChangeManager#addFileObserver(org.eclipse.gmf.runtime.common.ui.resources.IFileObserver,
	 *      org.eclipse.core.resources.IFolder)
	 */
	public void addFileObserver(IFileObserver fileObserver, IFolder folderFilter) {
		fileObserverManager.add(fileObserver, folderFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileChangeManager#addFileObserver(org.eclipse.gmf.runtime.common.ui.resources.IFileObserver,
	 *      java.lang.String[])
	 */
	public void addFileObserver(IFileObserver fileObserver,
			String[] extensionFilter) {
		fileObserverManager.add(fileObserver, extensionFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileChangeManager#addFileObserver(org.eclipse.gmf.runtime.common.ui.resources.IFileObserver)
	 */
	public void addFileObserver(IFileObserver fileObserver) {
		fileObserverManager.add(fileObserver);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileChangeManager#refreshLocal(org.eclipse.core.resources.IFile)
	 */
	public void refreshLocal(final IFile file) {
		try {
			file.getWorkspace().run(new IWorkspaceRunnable() {

				public void run(IProgressMonitor monitor)
					throws CoreException {

					trace("...FileChangeManager: Resource " + getAbsolutePath(file) + " was refreshed"); //$NON-NLS-2$//$NON-NLS-1$
					file.refreshLocal(IResource.DEPTH_ZERO, null);

				}
			}, new NullProgressMonitor());
		} catch (CoreException e) {
			Trace.catching(CommonUIPlugin.getDefault(),
				CommonUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"refreshResource", e); //$NON-NLS-1$
			Log.error(CommonUIPlugin.getDefault(),
				CommonUIStatusCodes.SERVICE_FAILURE, e.getMessage(), e); 
		}

	}

	/**
	 * Retrieve the moved from resource from the resource delta. The moved from
	 * resource is the original resource after a rename or move.
	 * 
	 * @param delta
	 *            the resource change containing the moved from path.
	 * @return the moved from resource.
	 */
	public static IResource getMovedFromResource(IResourceDelta delta) {
		IPath movedFromPath = delta.getMovedFromPath();
		IResource resource = delta.getResource();
		IResource movedResource = null;
		switch (resource.getType()) {
			case IResource.PROJECT:
				movedResource = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(movedFromPath.lastSegment());
				break;
			case IResource.FOLDER:
				movedResource = ResourcesPlugin.getWorkspace().getRoot()
					.getFolder(movedFromPath);
				break;
			case IResource.FILE:
				movedResource = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(movedFromPath);
				break;
			default:
				break;
		}
		return movedResource;
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