/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 */

package org.eclipse.gmf.examples.runtime.diagram.logic.semantic.presentation;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.DemultiplexingListener;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.presentation.LogicsemanticEditorPlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Listens for the loading of resources, and creates editors on them when they
 * load.
 */
public class ResourceLoadedListener extends DemultiplexingListener {
	private static ResourceLoadedListener instance;
	
	private final Set<Resource> ignoredResources = new java.util.HashSet<Resource>();
	
	/**
	 * Initializes me with my filter.
	 */
	public ResourceLoadedListener() {
		super(NotificationFilter.createFeatureFilter(
				EcorePackage.eINSTANCE.getEResource(), Resource.RESOURCE__IS_LOADED));
		
		instance = this;
	}
	
	/**
	 * Returns the default listener instance.
	 * 
	 * @return the instance associated with the editing domain that manages the
	 *     specified resource set, or <code>null</code> if none is found
	 */
	public static ResourceLoadedListener getDefault() {
		return instance;
	}
	
	/**
	 * Ignores any future load/unload notifications from the specified resource,
	 * until the next call to {@link #watch(Resource) watch(res)}.
	 * 
	 * @param res the resource to ignore
	 */
	public void ignore(Resource res) {
		ignoredResources.add(res);
	}
	
	/**
	 * Ceases to {@link #ignore(Resource)} a previously ignored resource.
	 * 
	 * @param res the resource
	 */
	public void watch(Resource res) {
		ignoredResources.remove(res);
	}

	protected void handleNotification(TransactionalEditingDomain domain, Notification notification) {
		if (ignoredResources.contains(notification.getNotifier())) {
			// skip any resource that we are supposed to ignore
			return;
		}
		
		if (notification.getNewBooleanValue() && !notification.getOldBooleanValue()) {
			// a resource has been loaded that was not loaded before.  Open an editor
			final IFile file = getFile((Resource)notification.getNotifier(), domain);
			
			if (file != null && PlatformUI.isWorkbenchRunning()) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					public void run() {
						try {
							IWorkbenchPage page = getActivePage();
							
							if (page != null) {
								IEditorPart activeEditor = page.getActiveEditor();
								
								if (file.getFileExtension().equals("logic2semantic")) { //$NON-NLS-1$
									page.openEditor(
											new FileEditorInput(file),
											"org.eclipse.gmf.examples.runtime.diagram.logic.semantic.presentation.SemanticEditorID", //$NON-NLS-1$
											false);
								}
								
								// restore the previously active editor to active
								//    state
								if (activeEditor != null) {
									page.activate(activeEditor);
								}
							}
						} catch (PartInitException e) {
							LogicsemanticEditorPlugin.getPlugin().log(e.getStatus());
						}
					}});
			}
		} else if (!notification.getNewBooleanValue() && notification.getOldBooleanValue()) {
			// a resource has been unloaded that was  loaded before.  Close
			//    the editor, if any
			final IFile file = WorkspaceSynchronizer.getFile(
					(Resource) notification.getNotifier());
			
			if (file != null && PlatformUI.isWorkbenchRunning()) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					public void run() {
						IWorkbenchPage page = getActivePage();
						
						if (page != null) {
							IEditorReference[] editors = page.findEditors(
									new FileEditorInput(file),
									"org.eclipse.gmf.examples.runtime.diagram.logic.semantic.presentation.SemanticEditorID", //$NON-NLS-1$
									IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
							
							page.closeEditors(editors, false);
						}
					}});
			}
		}
	}
	
	private IFile getFile(Resource resource, TransactionalEditingDomain domain) {
		IFile result = null;
		
		URI normalizedURI = domain.getResourceSet().getURIConverter().normalize(resource.getURI());
		
		if ("file".equals(normalizedURI.scheme())) { //$NON-NLS-1$
			IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(new Path(URI.decode(normalizedURI.devicePath())));
			if (files.length > 0) {
				result = files[0];
			}
		} else  {
			if ("platform".equals(normalizedURI.scheme()) && (normalizedURI.segmentCount() > 2)) { //$NON-NLS-1$
				if ("resource".equals(normalizedURI.segment(0))) { //$NON-NLS-1$
					IPath path = new Path(URI.decode(normalizedURI.path())).removeFirstSegments(1);
					
					result = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				}
			}
		}
		return result;
	}

	/**
	 * Obtains the currently active workbench page.
	 * 
	 * @return the active page, or <code>null</code> if none is active
	 */
	private IWorkbenchPage getActivePage() {
		IWorkbenchPage result = null;
		
		IWorkbench bench = PlatformUI.getWorkbench();
		if (bench != null) {
			IWorkbenchWindow window = bench.getActiveWorkbenchWindow();
			
			if (window != null) {
				result = window.getActivePage();
			}
		}
		
		return result;
	}
}
