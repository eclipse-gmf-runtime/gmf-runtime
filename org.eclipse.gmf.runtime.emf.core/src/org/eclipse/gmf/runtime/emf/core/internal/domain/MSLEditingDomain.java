/******************************************************************************
 * Copyright (c) 2004-2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.WeakHashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.OverrideableCommand;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.OperationListener;
import org.eclipse.gmf.runtime.emf.core.ResourceSetModifyOperation;
import org.eclipse.gmf.runtime.emf.core.ResourceSetOperation;
import org.eclipse.gmf.runtime.emf.core.ResourceSetReadOperation;
import org.eclipse.gmf.runtime.emf.core.clipboard.CopyingResource;
import org.eclipse.gmf.runtime.emf.core.clipboard.CopyingResourceSet;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MResourceOption;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLRuntimeException;
import org.eclipse.gmf.runtime.emf.core.internal.commands.MSLCommandGenerator;
import org.eclipse.gmf.runtime.emf.core.internal.commands.MSLUndoStack;
import org.eclipse.gmf.runtime.emf.core.internal.index.MSLObjectIndexer;
import org.eclipse.gmf.runtime.emf.core.internal.index.MSLResourceIndexer;
import org.eclipse.gmf.runtime.emf.core.internal.l10n.EMFCoreMessages;
import org.eclipse.gmf.runtime.emf.core.internal.notifications.MSLContentAdapter;
import org.eclipse.gmf.runtime.emf.core.internal.notifications.MSLEventBroker;
import org.eclipse.gmf.runtime.emf.core.internal.notifications.MSLObjectListener;
import org.eclipse.gmf.runtime.emf.core.internal.notifications.MSLResourceListener;
import org.eclipse.gmf.runtime.emf.core.internal.notifications.MSLResourceSetListener;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLStatusCodes;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MResource;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MSLExtendedMetaData;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MSLPathmap;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MSLResource;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLAdapterFactoryManager;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLComposedAdapterFactory;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

import com.ibm.icu.util.StringTokenizer;

/**
 * This is the implementation of the MSL editing domain interface. The
 * implementation wraps and delegates to an EMF AdapterFactoryEditingDomain. The
 * MSL editing domain does not support clients calling the following methods
 * from EditingDomain: <code>
 * createCommand
 * createOverrideCommand
 * getCommandStack
 * getClipboard
 * setClipboard
 * </code>
 * 
 * @author rafikj
 */

public class MSLEditingDomain
	extends MEditingDomain {

	private MSLAdapterFactoryEditingDomain editingDomain = null;

	private MSLContentAdapter contentAdapter = null;

	private MSLUndoStack undoStack = null;

	private MSLEventBroker eventBroker = null;

	private MSLCommandGenerator commandGenerator = null;

	private MSLObjectListener objectListener = null;

	private MSLResourceListener resourceListener = null;

	private MSLResourceSetListener resourceSetListener = null;

	private MSLOperationListenerBroker operationListenerBroker = null;

	private MSLPathmap pathmap = null;

	private MSLObjectIndexer objectIndexer = null;

	private MSLResourceIndexer resourceIndexer = null;

	private static Map resourceSets = new WeakHashMap();

	/**
	 * The extended metadata for MSL resources.
	 */
	private ExtendedMetaData extendedMetaData = null;

	/**
	 * Stack of {@link org.eclipse.gmf.runtime.emf.core.internal.MRunnable}s that
	 * are running cancellable write operations.
	 */
	private List writers = new ArrayList();

	/**
	 * Constructor.
	 */
	public MSLEditingDomain() {

		this((ResourceSet) null);
	}

	/**
	 * Constructor.
	 */
	public MSLEditingDomain(ResourceSet rset) {

		super();

		MSLComposedAdapterFactory composedFactory = MSLAdapterFactoryManager
			.getAdapterFactory();

		if (rset != null) {
			editingDomain = new MSLAdapterFactoryEditingDomain(composedFactory, rset);
		} else {
			editingDomain = new MSLAdapterFactoryEditingDomain(composedFactory);
		}

		contentAdapter = new MSLContentAdapter(this);

		undoStack = new MSLUndoStack(this);

		eventBroker = new MSLEventBroker(this);

		commandGenerator = new MSLCommandGenerator(this);

		objectListener = new MSLObjectListener(this);

		resourceListener = new MSLResourceListener(this);

		resourceSetListener = new MSLResourceSetListener(this);

		pathmap = new MSLPathmap(this);

		objectIndexer = new MSLObjectIndexer(this);

		resourceIndexer = new MSLResourceIndexer(this);

		ResourceSet resourceSet = getResourceSet();

		extendedMetaData = new MSLExtendedMetaData();

		contentAdapter.listenToModifications(resourceSet);

		resourceSets.put(resourceSet, new WeakReference(this));

		for (Iterator i = composedFactory.getFactories().iterator(); i
			.hasNext();) {

			AdapterFactory factory = (AdapterFactory) i.next();

			Collection factories = resourceSet.getAdapterFactories();

			factories.remove(factory);
			factories.add(factory);
		}
	}

	/**
	 * Returns the EMF content adapter.
	 */
	public MSLContentAdapter getContentAdapter() {
		return contentAdapter;
	}

	/**
	 * Returns the command stack.
	 */
	public MSLUndoStack getUndoStack() {
		return undoStack;
	}

	/**
	 * Returns the event broker.
	 */
	public MSLEventBroker getEventBroker() {
		return eventBroker;
	}

	/**
	 * Returns the command generator.
	 */
	public MSLCommandGenerator getCommandGenerator() {
		return commandGenerator;
	}

	/**
	 * Returns the EObject listener.
	 */
	public MSLObjectListener getObjectListener() {
		return objectListener;
	}

	/**
	 * Returns the Resource listener.
	 */
	public MSLResourceListener getResouceListener() {
		return resourceListener;
	}

	/**
	 * Returns the ResourceSet listener.
	 */
	public MSLResourceSetListener getResouceSetListener() {
		return resourceSetListener;
	}

	/**
	 * Returns the path map.
	 */
	public MSLPathmap getPathmap() {
		return pathmap;
	}

	/**
	 * Returns the object indexer.
	 */
	public MSLObjectIndexer getObjectIndexer() {
		return objectIndexer;
	}

	/**
	 * Returns the resource indexer.
	 */
	public MSLResourceIndexer getResourceIndexer() {
		return resourceIndexer;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#create(org.eclipse.emf.ecore.EClass)
	 */
	public EObject create(EClass eClass) {
		return MSLUtil.create(this, eClass, true);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getResourceFileName(org.eclipse.emf.ecore.resource.Resource)
	 */
	public String getResourceFileName(Resource resource) {
		return MSLUtil.getFilePath(resource);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#setResourceFileName(org.eclipse.emf.ecore.resource.Resource,
	 *      java.lang.String)
	 */
	public void setResourceFileName(Resource resource, String fileNameURI) {
		setResourceFileName(resource, fileNameURI, 0);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#setResourceFileName(org.eclipse.emf.ecore.resource.Resource,
	 *      java.lang.String, int)
	 */
	public void setResourceFileName(Resource resource, String fileNameURI,
			int options) {

		URI uri = null;

		if ((options & MResourceOption.URI) != 0)
			uri = URI.createURI(fileNameURI);

		else
			uri = URI.createFileURI(new File(fileNameURI).getAbsolutePath());

		resource.setURI(uri);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#findResource(java.lang.String)
	 */
	public Resource findResource(String fileNameURI) {
		return findResource(fileNameURI, 0);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#findResource(java.lang.String,
	 *      int)
	 */
	public Resource findResource(String fileNameURI, int options) {

		URI uri = null;

		if ((options & MResourceOption.URI) != 0)
			uri = URI.createURI(fileNameURI);

		else
			uri = URI.createFileURI(new File(fileNameURI).getAbsolutePath());

		ResourceSet resourceSet = getResourceSet();

		Resource resource = resourceSet.getResource(uri, false);

		if (resource == null) {
			URI convertedURI = convertURI(uri);

			if (!convertedURI.equals(uri))
				resource = resourceSet.getResource(convertedURI, false);
		}

		return resource;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#convertURI(org.eclipse.emf.common.util.URI)
	 */
	public URI convertURI(URI uri) {

		URI resolvedURI = uri;

		if (MSLConstants.PLATFORM_SCHEME.equals(resolvedURI.scheme())) {

			String filePath = MSLUtil
				.getFilePath(getResourceSet(), resolvedURI);

			if ((filePath != null) && (filePath.length() > 0))
				resolvedURI = URI.createFileURI(filePath);
		}

		if ((resolvedURI != null) && (resolvedURI.isFile())) {

			ResourceSet resourceSet = getResourceSet();

			String fileName = resolvedURI.lastSegment();

			// attempt to convert the URI to a path map URI.
			if (fileName != null) {

				URI prefix = resolvedURI.trimSegments(1);

				// find a matching pathmap.
				URI foundKeyURI = null;
				URI foundValURI = null;
				int minDiff = Integer.MAX_VALUE;

				Iterator i = resourceSet.getURIConverter().getURIMap()
					.entrySet().iterator();

				while (i.hasNext()) {

					Map.Entry entry = (Map.Entry) i.next();

					if (entry != null) {

						URI keyURI = (URI) entry.getKey();
						URI valURI = (URI) entry.getValue();

						if ((keyURI.isHierarchical())
							&& (MSLConstants.PATH_MAP_SCHEME.equals(keyURI
								.scheme())) && (valURI.isFile())) {

							int diff = computeDiff(valURI, prefix);

							if ((diff >= 0) && (diff < minDiff)) {

								minDiff = diff;

								foundKeyURI = keyURI;
								foundValURI = valURI;

								if (minDiff == 0)
									break;
							}
						}
					}
				}

				if ((foundKeyURI != null) && (foundValURI != null))
					return resolvedURI.replacePrefix(foundValURI, foundKeyURI);
			}

			// attempt to convert URI to a platform URI.
			URI platformURI = getPlatformURI(uri);

			if (platformURI != null)
				return platformURI;
		}

		return uri;
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#createResource(java.lang.String)
	 */
	public Resource createResource(String fileNameURI) {
		return createResource(fileNameURI, null, 0);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#createResource(java.lang.String,
	 *      int)
	 */
	public Resource createResource(String fileNameURI, int options) {
		return createResource(fileNameURI, null, options);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#createResource(java.lang.String,
	 *      org.eclipse.emf.ecore.EClass)
	 */
	public Resource createResource(String fileNameURI, EClass rootEClass) {
		return createResource(fileNameURI, rootEClass, 0);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#createResource(java.lang.String,
	 *      org.eclipse.emf.ecore.EClass, int)
	 */
	public Resource createResource(String fileNameURI, EClass rootEClass,
			int options) {

		URI uri = null;
		
		boolean wasCreated = false;

		if ((fileNameURI == null)
			|| (fileNameURI.equals(MSLConstants.EMPTY_STRING)))
			uri = URI.createFileURI(new File(EcoreUtil.generateUUID()
				+ MSLConstants.INVALID_PATH).getPath());

		else if (fileNameURI.equals(MSLConstants.EPHEMERAL_INVALID_PATH))
			uri = URI.createFileURI(new File(EcoreUtil.generateUUID()
				+ MSLConstants.EPHEMERAL_INVALID_PATH).getPath());

		else if ((options & MResourceOption.URI) != 0)
			uri = URI.createURI(fileNameURI);

		else
			uri = URI.createFileURI(new File(fileNameURI).getAbsolutePath());

		if (uri != null) {

			ResourceSet resourceSet = getResourceSet();

			URI convertedURI = convertURI(uri);

			Resource resource = resourceSet.getResource(uri, false);

			if ((resource == null) && (!convertedURI.equals(uri)))
				resource = resourceSet.getResource(convertedURI, false);

			if (resource == null) {
				resource = resourceSet.createResource(convertedURI);
				wasCreated = true;
			}

			else if (resource.isLoaded()) {

				RuntimeException e = new IllegalStateException(
					"resource already created and loaded"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"createResource", e); //$NON-NLS-1$

				throw e;
			}

			if (resource != null) {

				if (rootEClass != null) {

					EObject root = null;

					if (resource instanceof MResource)
						((MResource) resource).getHelper().create(this,
							rootEClass);
					else
						root = MSLUtil.create(this, rootEClass, false);

					if (root != null)
						resource.getContents().add(root);

					MSLUtil.postProcessResource(resource);
				}

				if (wasCreated)
					eventBroker.addEvent(resource, EventTypes.CREATE);

				return resource;
			}
		}

		return null;
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#loadResource(java.lang.String)
	 */
	public Resource loadResource(String fileNameURI) {
		return loadResource(fileNameURI, 0, null);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#loadResource(java.lang.String,
	 *      int)
	 */
	public Resource loadResource(String fileNameURI, int options) {
		return loadResource(fileNameURI, options, null);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#loadResource(java.lang.String,
	 *      int, java.io.InputStream)
	 */
	public Resource loadResource(String fileNameURI, int options,
			InputStream inputStream) {

		URI uri = null;

		if (fileNameURI != null) {

			if ((options & MResourceOption.URI) != 0)
				uri = URI.createURI(fileNameURI);

			else
				uri = URI
					.createFileURI(new File(fileNameURI).getAbsolutePath());
		} else
			uri = null;

		if (uri != null) {

			ResourceSet resourceSet = getResourceSet();

			URI convertedURI = convertURI(uri);

			Resource resource = resourceSet.getResource(uri, false);

			if ((resource == null) && (!convertedURI.equals(uri)))
				resource = resourceSet.getResource(convertedURI, false);

			if (resource == null)
				resource = resourceSet.createResource(convertedURI);

			if (resource != null) {
				
				try {
					loadResource(resource, options, inputStream);
				} catch (RuntimeException e) {
					// If an exception is thrown then we will try to automatically unload the resource.
					if (resource.isLoaded()) {
						resource.unload();
					}
					throw e;
				}
				
				return resource;
			}
		}

		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#loadResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	public void loadResource(Resource resource) {
		loadResource(resource, 0, null);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#loadResource(org.eclipse.emf.ecore.resource.Resource,
	 *      int)
	 */
	public void loadResource(Resource resource, int options) {
		loadResource(resource, options, null);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#loadResource(org.eclipse.emf.ecore.resource.Resource,
	 *      int, java.io.InputStream)
	 */
	public void loadResource(Resource resource, int options,
			InputStream inputStream) {
		try {
			Map loadOptions = null;

			if (resource instanceof XMLResource) {

				Map defaultLoadOptions = ((XMLResource) resource)
					.getDefaultLoadOptions();

				if (defaultLoadOptions != null)
					loadOptions = new HashMap(defaultLoadOptions);
			}

			if (loadOptions == null)
				loadOptions = new HashMap();

			if ((options & MResourceOption.COMPATIBILITY_MODE) != 0) {

				loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE,
					Boolean.TRUE);

				loadOptions.put(XMLResource.OPTION_EXTENDED_META_DATA,
					extendedMetaData);
			} else {
				loadOptions.put(MSLResource.OPTION_ABORT_ON_ERROR,
					Boolean.TRUE);
			}
			
			if (null == inputStream)
				resource.load(loadOptions);
			else
				resource.load(inputStream, loadOptions);

		} catch (Exception e) {
			// TODO In the next iteration, we will no longer be automatically unloading a resource if there are errors.
			resource.unload();
			
			eventBroker.clearEvents();
			
			RuntimeException newE = null;

			if (e instanceof MSLRuntimeException)
				newE = (MSLRuntimeException) e;
			else
				newE = new MSLRuntimeException(e);

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"loadResource", newE); //$NON-NLS-1$

			throw newE;
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#unloadResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	public void unloadResource(Resource resource) {
		unloadResource(resource, 0);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#unloadResource(org.eclipse.emf.ecore.resource.Resource,
	 *      int)
	 */
	public void unloadResource(Resource resource, int options) {

		try {

			resource.unload();

		} catch (Exception e) {
			eventBroker.clearEvents();
			
			RuntimeException newE = null;

			if (e instanceof MSLRuntimeException)
				newE = (MSLRuntimeException) e;
			else
				newE = new MSLRuntimeException(e);

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"unloadResource", newE); //$NON-NLS-1$

			throw newE;
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#saveResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	public void saveResource(Resource resource) {
		saveResource(resource, 0);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#saveResource(org.eclipse.emf.ecore.resource.Resource,
	 *      int)
	 */
	public void saveResource(Resource resource, int options) {

		if (resource.isLoaded()) {

			try {

				Map saveOptions = null;

				if (resource instanceof XMLResource) {

					Map defaultSaveOptions = ((XMLResource) resource)
						.getDefaultSaveOptions();

					if (defaultSaveOptions != null)
						saveOptions = new HashMap(defaultSaveOptions);
				}

				if (saveOptions == null)
					saveOptions = new HashMap();

				if ((options & MResourceOption.COMPATIBILITY_MODE) != 0)
					saveOptions.put(XMLResource.OPTION_EXTENDED_META_DATA,
						extendedMetaData);

				else if (resource instanceof XMLResource)
					((XMLResource) resource).getEObjectToExtensionMap().clear();

				if ((options & MResourceOption.USE_FILE_BUFFER) != 0) {

					saveOptions.put(XMLResource.OPTION_USE_FILE_BUFFER,
						Boolean.TRUE);
					saveOptions.put(XMLResource.OPTION_FLUSH_THRESHOLD,
						MSLConstants.OUTPUT_BUFFER_SIZE);
				}
				
				resource.save(saveOptions);

			} catch (Exception e) {

				eventBroker.clearEvents();

				RuntimeException newE = null;

				if (e instanceof MSLRuntimeException)
					newE = (MSLRuntimeException) e;
				else
					newE = new MSLRuntimeException(e);

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"saveResource", newE); //$NON-NLS-1$

				throw newE;
			}
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#saveResourceAs(org.eclipse.emf.ecore.resource.Resource,
	 *      java.lang.String)
	 */
	public void saveResourceAs(Resource resource, String fileNameURI) {
		saveResourceAs(resource, fileNameURI, 0);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#saveResourceAs(org.eclipse.emf.ecore.resource.Resource,
	 *      java.lang.String, int)
	 */
	public void saveResourceAs(Resource resource, String fileNameURI,
			int options) {

		setResourceFileName(resource, fileNameURI, options);
		saveResource(resource, options);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#openUndoInterval()
	 * @deprecated overrides a deprecated method from MEditingDomain
	 */
	public void openUndoInterval() {
		undoStack.openUndoInterval(MSLConstants.EMPTY_STRING,
			MSLConstants.EMPTY_STRING);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#openUndoInterval(java.lang.String)
	 * @deprecated overrides a deprecated method from MEditingDomain
	 */
	public void openUndoInterval(String label) {
		undoStack.openUndoInterval(label, MSLConstants.EMPTY_STRING);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#openUndoInterval(java.lang.String,
	 *      java.lang.String)
	 * @deprecated overrides a deprecated method from MEditingDomain
	 */
	public void openUndoInterval(String label, String description) {
		undoStack.openUndoInterval(label, description);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#closeUndoInterval()
	 * @deprecated overrides a deprecated method from MEditingDomain
	 */
	public MUndoInterval closeUndoInterval() {
		return undoStack.closeUndoInterval();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#canUndoCurrentInterval()
	 */
	public boolean canUndoCurrentInterval() {
		return undoStack.canUndoCurrentInterval();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#canRedoCurrentInterval()
	 */
	public boolean canRedoCurrentInterval() {
		return undoStack.canRedoCurrentInterval();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#setCanUndoCurrentInterval(boolean)
	 */
	public void setCanUndoCurrentInterval(boolean canUndo) {
		undoStack.setCanUndoCurrentInterval(canUndo);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#setCanRedoCurrentInterval(boolean)
	 */
	public void setCanRedoCurrentInterval(boolean canRedo) {
		undoStack.setCanRedoCurrentInterval(canRedo);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#runInUndoInterval(java.lang.Runnable)
	 */
	public MUndoInterval runInUndoInterval(Runnable runnable) {
		return runInUndoInterval(MSLConstants.EMPTY_STRING, runnable);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#runInUndoInterval(java.lang.String,
	 *      java.lang.Runnable)
	 */
	public MUndoInterval runInUndoInterval(String label, Runnable runnable) {
		return runInUndoInterval(label, MSLConstants.EMPTY_STRING, runnable);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#runInUndoInterval(java.lang.String,
	 *      java.lang.String, java.lang.Runnable)
	 */
	public MUndoInterval runInUndoInterval(String label, String description,
			Runnable runnable) {

		// unwind the read action stack, if any
		final int readCount = unwindReadActions();

		undoStack.acquire(MSLUndoStack.ActionLockMode.WRITE);
		try {
			boolean open = isUndoIntervalOpen();
	
			if (!open)
				openUndoInterval(label, description);
	
			MUndoInterval undoInterval = null;
	
			try {
	
				runnable.run();
	
			} finally {
				if (!open)
					undoInterval = closeUndoInterval();
			}
	
			return undoInterval;
		} finally {
			undoStack.release();
			// restore the previous read action stack, if any
			rewindReadActions(readCount);
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#isUndoIntervalOpen()
	 */
	public boolean isUndoIntervalOpen() {
		return undoStack.isUndoIntervalOpen();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#startRead()
	 * @deprecated overrides a deprecated method from MEditingDomain
	 */
	public void startRead() {
		undoStack.startAction(MSLUndoStack.ActionLockMode.READ);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#startWrite()
	 * @deprecated overrides a deprecated method from MEditingDomain
	 */
	public void startWrite() {
		undoStack.startAction(MSLUndoStack.ActionLockMode.WRITE);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#startUnchecked()
	 * @deprecated overrides a deprecated method from MEditingDomain
	 */
	public void startUnchecked() {
		undoStack.startAction(MSLUndoStack.ActionLockMode.UNCHECKED);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#complete()
	 * @deprecated overrides a deprecated method from MEditingDomain
	 */
	public void complete() {
		undoStack.completeAction();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#completeAndValidate()
	 * @deprecated overrides a deprecated method from MEditingDomain
	 */
	public IStatus completeAndValidate()
		throws MSLActionAbandonedException {
		return undoStack.completeAndValidateAction();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#abandon()
	 * @deprecated overrides a deprecated method from MEditingDomain
	 */
	public void abandon() {
		undoStack.abandonAction();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#runAsRead(org.eclipse.gmf.runtime.emf.core.internal.MRunnable)
	 */
	public Object runAsRead(MRunnable runnable) {
		boolean read = true;
		undoStack.acquire(MSLUndoStack.ActionLockMode.READ);
		try {
			read = canCurrentThreadRead();
			if (!read)
				startRead();

		} finally {
			undoStack.release();
		}

		try {
			Object result = null;
			result = runnable.run();
			return result;
		} finally {
			if (!read)
				complete();
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#runAsWrite(org.eclipse.gmf.runtime.emf.core.internal.MRunnable)
	 */
	public Object runAsWrite(MRunnable runnable)
		throws MSLActionAbandonedException {

		// unwind the read action stack, if any
		final int readCount = unwindReadActions();

		undoStack.acquire(MSLUndoStack.ActionLockMode.WRITE);

		try {
			boolean write = canWrite();

			if (!write)
				startWrite();

			MRunnable top = peekWriter();
			pushWriter(runnable); // push the runnable onto the stack

			Object result = null;

			try {
				if ((top != null) && top.isAbandoned()) {
					// the previous runnable already canceled, so propagate
					//   up the stack
					runnable.abandon();
				}

				result = runnable.run();

			} finally {
				popWriter(); // pop the runnable

				if (runnable.isAbandoned() && (top != null)) {
					// propagate cancellation down the runnable stack
					top.abandon();
				}

				if (!write) {

					if (runnable.isAbandoned()) {
						abandon();
						runnable.setStatus(new Status(IStatus.CANCEL,
							MSLPlugin.getPluginId(),
							MSLStatusCodes.OPERATION_CANCELED_BY_USER,
							EMFCoreMessages.operation_canceled,
							null));
					} else {
						try {

							// complete and validate may throw, so that we
							// will
							// set the status in the catch block
							runnable.setStatus(completeAndValidate());

						} catch (MSLActionAbandonedException e) {

							runnable.setStatus(e.getStatus());

							throw e; // propagate the exception to the
									 // caller
						}
					}
				} else {

					runnable.setStatus(Status.OK_STATUS);
				}
			}
			return result;
		} finally {
			undoStack.release();
			// restore the previous read action stack, if any
			rewindReadActions(readCount);
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#runAsUnchecked(org.eclipse.gmf.runtime.emf.core.internal.MRunnable)
	 */
	public Object runAsUnchecked(MRunnable runnable) {
		undoStack.acquire(MSLUndoStack.ActionLockMode.UNCHECKED);

		try {
			startUnchecked();
	
			Object result = null;
	
			try {
	
				result = runnable.run();
	
			} finally {
	


				complete();
			}
			return result;
		} finally {
			undoStack.release();
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#runSilent(org.eclipse.gmf.runtime.emf.core.internal.MRunnable)
	 */
	public Object runSilent(MRunnable runnable) {
		return eventBroker.runSilent(runnable);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#runWithNoSemProcs(org.eclipse.gmf.runtime.emf.core.internal.MRunnable)
	 */
	public Object runWithNoSemProcs(MRunnable runnable) {
		return eventBroker.runWithNoSemProcs(runnable);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#runUnvalidated(org.eclipse.gmf.runtime.emf.core.internal.MRunnable)
	 */
	public Object runUnvalidated(MRunnable runnable) {
		return eventBroker.runUnvalidated(runnable);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#runWithOptions(org.eclipse.gmf.runtime.emf.core.internal.MRunnable,
	 *      int)
	 */
	public Object runWithOptions(MRunnable runnable, int options) {
		return eventBroker.runWithOptions(runnable, options);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#canRead()
	 */
	public boolean canRead() {
		return undoStack.getActionLockMode() != MSLUndoStack.ActionLockMode.NONE;
	}
	
	protected boolean canCurrentThreadRead() {
		return undoStack.getCurrentThreadActionLockMode() != MSLUndoStack.ActionLockMode.NONE;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#canWrite()
	 */
	public boolean canWrite() {
		return ((undoStack.isWriteActionInProgress()) || (undoStack
			.isUncheckedActionInProgress()));
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#isWriteInProgress()
	 */
	public boolean isWriteInProgress() {
		return undoStack.isWriteActionInProgress();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#isUncheckedInProgress()
	 */
	public boolean isUncheckedInProgress() {
		return undoStack.isUncheckedActionInProgress();
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#isUndoNotification(org.eclipse.emf.common.notify.Notification)
	 */
	public boolean isUndoNotification(Notification notification) {
		return eventBroker.isUndoEvent(notification);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#isRedoNotification(org.eclipse.emf.common.notify.Notification)
	 */
	public boolean isRedoNotification(Notification notification) {
		return eventBroker.isRedoEvent(notification);
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#getResourceSet()
	 */
	public ResourceSet getResourceSet() {
		return editingDomain.getResourceSet();
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#createCommand(java.lang.Class,
	 *      org.eclipse.emf.edit.command.CommandParameter)
	 */
	public Command createCommand(Class commandClass,
			CommandParameter commandParameter) {

		return editingDomain.createCommand(commandClass, commandParameter);
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#createOverrideCommand(org.eclipse.emf.edit.command.OverrideableCommand)
	 */
	public Command createOverrideCommand(OverrideableCommand command) {

		return editingDomain.createOverrideCommand(command);
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#getCommandStack()
	 */
	public CommandStack getCommandStack() {
		return editingDomain.getCommandStack();
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#getChildren(java.lang.Object)
	 */
	public Collection getChildren(Object object) {
		return editingDomain.getChildren(object);
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#getParent(java.lang.Object)
	 */
	public Object getParent(Object object) {
		return editingDomain.getParent(object);
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#getRoot(java.lang.Object)
	 */
	public Object getRoot(Object object) {
		return editingDomain.getRoot(object);
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#getNewChildDescriptors(java.lang.Object,
	 *      java.lang.Object)
	 */
	public Collection getNewChildDescriptors(Object object, Object sibling) {
		return editingDomain.getNewChildDescriptors(object, sibling);
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#treeIterator(java.lang.Object)
	 */
	public TreeIterator treeIterator(Object object) {
		return editingDomain.treeIterator(object);
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#getTreePath(java.lang.Object)
	 */
	public List getTreePath(Object object) {
		return editingDomain.getTreePath(object);
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#getClipboard()
	 */
	public Collection getClipboard() {

		RuntimeException e = new UnsupportedOperationException();

		Trace.throwing(MSLPlugin.getDefault(),
			MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "getClipboard", e); //$NON-NLS-1$

		throw e;
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#setClipboard(java.util.Collection)
	 */
	public void setClipboard(Collection clipboard) {

		RuntimeException e = new UnsupportedOperationException();

		Trace.throwing(MSLPlugin.getDefault(),
			MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "setClipboard", e); //$NON-NLS-1$

		throw e;
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#getOptimizeCopy()
	 */
	public boolean getOptimizeCopy() {

		RuntimeException e = new UnsupportedOperationException();

		Trace.throwing(MSLPlugin.getDefault(),
			MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
			"getOptimizeCopy", e); //$NON-NLS-1$

		throw e;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#sendNotification(org.eclipse.emf.common.notify.Notification)
	 */
	public void sendNotification(Notification notification) {
		eventBroker.addEvent(notification);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#sendNotification(java.lang.Object,
	 *      int)
	 */
	public void sendNotification(Object notifier, int eventType) {
		eventBroker.addEvent(notifier, eventType);
	}

	/**
	 * @see org.eclipse.emf.edit.domain.EditingDomain#isReadOnly(org.eclipse.emf.ecore.resource.Resource)
	 */
	public boolean isReadOnly(Resource resource) {
		return editingDomain.isReadOnly(resource);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.EditingDomain#run(org.eclipse.gmf.runtime.emf.core.ResourceSetOperation,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(final ResourceSetOperation operation,
			final IProgressMonitor monitor)
		throws InvocationTargetException, InterruptedException {

		final Exception[] pendingException = new Exception[1];

		if (operation instanceof ResourceSetModifyOperation) {
			try {
				run((ResourceSetModifyOperation) operation, monitor);
			} catch (Exception e) {
				pendingException[0] = e;
			}
		} else if (operation instanceof ResourceSetReadOperation) {
			try {
				run((ResourceSetReadOperation) operation, monitor);
			} catch (Exception e) {
				pendingException[0] = e;
			}
		} else {

			RuntimeException e = new IllegalArgumentException(
				"ResourceSetModifyOperation and ResourceSetReadOperation are the only supported kind of operations"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "run", e); //$NON-NLS-1$

			throw e;
		}

		// check whether the operation failed with some kind of exception and,
		//    if so, re-throw it according to the exceptions declared by
		//    this method
		Throwable exception = pendingException[0];

		if (exception instanceof InterruptedException) {

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"run", exception); //$NON-NLS-1$

			throw (InterruptedException) exception;

		} else if (exception instanceof InvocationTargetException) {

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"run", exception); //$NON-NLS-1$

			throw (InvocationTargetException) exception;

		} else if (exception instanceof Exception) {

			InvocationTargetException e = new InvocationTargetException(
				exception);

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "run", e); //$NON-NLS-1$

			throw e;
		}
	}

	/**
	 * Runs a resource set modification operation.
	 * 
	 * @param modifyOp
	 *            the modify operation to run
	 * @param monitor
	 *            the progress monitor to pass to the operation
	 * 
	 * @throws Exception
	 *             an exception to proagate to the client
	 */
	private void run(final ResourceSetModifyOperation modifyOp,
			final IProgressMonitor monitor)
		throws Exception {

		final Exception[] pendingException = new Exception[1];

		Runnable runnable = new Runnable() {

			public void run() {
				MRunnable mrun = new MRunnable() {

					public Object run() {
						try {
							modifyOp.run(monitor);
						} catch (Exception e) {
							modifyOp.setResult(new Status(IStatus.ERROR,
								MSLPlugin.getPluginId(),
								MSLStatusCodes.OPERATION_FAILED,
								EMFCoreMessages.operation_failed,
								e));
							pendingException[0] = e;
						}

						return null;
					}
				};

				try {
					runAsWrite(mrun);
				} catch (MSLActionAbandonedException e) {
					pendingException[0] = e;
				} finally {
					if (modifyOp.getResult() == null) {
						// if the exception handler didn't set a status,
						//    inherit the MRunnable's status
						modifyOp.setResult(mrun.getStatus());
					}
				}
			}
		};

		runInUndoInterval(modifyOp.getLabel(), runnable);

		if (pendingException[0] != null) {
			// don't log at this point because we will do that in the
			//   calling method
			throw pendingException[0];
		}
	}

	/**
	 * Runs a resource set read operation.
	 * 
	 * @param readOp
	 *            the read operation to run
	 * @param monitor
	 *            the progress monitor to pass to the operation
	 * 
	 * @throws Exception
	 *             an exception to proagate to the client
	 */
	private void run(final ResourceSetReadOperation readOp,
			final IProgressMonitor monitor)
		throws Exception {

		final Exception[] pendingException = new Exception[1];

		runAsRead(new MRunnable() {

			public Object run() {
				try {

					readOp.run(monitor);

					readOp.setResult(new Status(IStatus.OK, MSLPlugin
						.getPluginId(), MSLStatusCodes.OK,
						EMFCoreMessages.operation_ok,
						null));
				} catch (Exception e) {

					readOp.setResult(new Status(IStatus.ERROR, MSLPlugin
						.getPluginId(), MSLStatusCodes.OPERATION_FAILED,
						EMFCoreMessages.operation_failed,
						e));

					pendingException[0] = e;
				}

				return null;
			}
		});

		if (pendingException[0] != null) {
			// don't log at this point because we will do that in the
			//   calling method
			throw pendingException[0];
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.EditingDomain#addOperationListener(org.eclipse.gmf.runtime.emf.core.OperationListener)
	 */
	public void addOperationListener(OperationListener listener) {

		if (operationListenerBroker == null)
			operationListenerBroker = new MSLOperationListenerBroker(this);

		operationListenerBroker.addOperationListener(listener);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.EditingDomain#removeOperationListener(org.eclipse.gmf.runtime.emf.core.OperationListener)
	 */
	public void removeOperationListener(OperationListener listener) {

		if (operationListenerBroker != null)
			operationListenerBroker.removeOperationListener(listener);
	}

	/**
	 * Computes segement count difference between two URIs if one is a subset of
	 * the other.
	 */
	private static int computeDiff(URI subURI, URI containerURI) {

		int subSegmentCount = subURI.segmentCount();
		int containerSegmentCount = containerURI.segmentCount();

		if ((subSegmentCount > 0)
			&& (subURI.segment(subSegmentCount - 1)
				.equals(MSLConstants.EMPTY_STRING))) {

			subURI = subURI.trimSegments(1);
			subSegmentCount--;
		}

		if ((containerSegmentCount > 0)
			&& (containerURI.segment(containerSegmentCount - 1)
				.equals(MSLConstants.EMPTY_STRING))) {

			containerURI = containerURI.trimSegments(1);
			containerSegmentCount--;
		}

		int diff = containerSegmentCount - subSegmentCount;

		if (diff < 0)
			return -1;

		else if (diff > 0)
			containerURI = containerURI.trimSegments(diff);

		if (!subURI.equals(containerURI))
			return -1;

		return diff;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getSchemaToLocationMap(org.eclipse.emf.common.util.URI)
	 */
	public Map getSchemaToLocationMap(URI uri)
		throws IOException {

		InputStream inputStream = null;
		Map requiredLocations = null;

		// create an input stream using the passed uri.
		try {

			inputStream = getResourceSet().getURIConverter().createInputStream(
				uri);

			requiredLocations = getSchemaToLocationMap(inputStream);

		} finally {

			if (inputStream != null)
				inputStream.close();
		}

		return requiredLocations;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getSchemaToLocationMap(org.eclipse.emf.common.util.URI)
	 */
	public Map getSchemaToLocationMap(InputStream inputStream)
		throws IOException {

		Map requiredLocations = new HashMap();

		// create a buffer reader using the stream we just created
		BufferedReader bufferedReader = null;

		try {

			bufferedReader = new BufferedReader(new InputStreamReader(
				inputStream));

			String line;
			int indexOfSchemasLocations = -1;

			// read the file line by line
			while ((line = bufferedReader.readLine()) != null) {

				// try to find the schema's location line
				indexOfSchemasLocations = line.indexOf(XMLResource.XSI_NS
					+ ":" + XMLResource.SCHEMA_LOCATION); //$NON-NLS-1$

				if (indexOfSchemasLocations != -1) {

					int schemaStart = line.indexOf(
						"\"", indexOfSchemasLocations + 1); //$NON-NLS-1$

					int schemaEnd = line.indexOf("\"", schemaStart + 1); //$NON-NLS-1$

					String schemas = line.substring(schemaStart + 1, schemaEnd);

					// trimming the white spaces
					schemas = schemas.trim();
					StringTokenizer st = new StringTokenizer(schemas);

					while (st.hasMoreTokens()) {

						String schema = st.nextToken();
						String requiredLocation = ""; //$NON-NLS-1$

						if (st.hasMoreTokens())
							requiredLocation = st.nextToken();

						int hashLocation = requiredLocation.indexOf("#"); //$NON-NLS-1$

						if (hashLocation != -1)
							requiredLocation = requiredLocation.substring(0,
								hashLocation);

						if (requiredLocation.length() > 0)
							requiredLocations.put(schema, URI
								.decode(requiredLocation));
					}

					break;
				}
			}

		} finally {

			if (bufferedReader != null)
				bufferedReader.close();
		}

		return requiredLocations;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#setPathVariable(java.lang.String,
	 *      java.lang.String)
	 */
	public void setPathVariable(String var, String val) {
		MSLPathmap.setPathVariable(var, val);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#removePathVariable(java.lang.String)
	 */
	public void removePathVariable(String var) {
		MSLPathmap.removePathVariable(var);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getPathVariable(java.lang.String)
	 */
	public String getPathVariable(String var) {
		return pathmap.getPathVariable(var);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getImports(org.eclipse.emf.ecore.resource.Resource)
	 */
	public Collection getImports(Resource resource) {
		return resourceIndexer.getImports(resource);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getExports(org.eclipse.emf.ecore.resource.Resource)
	 */
	public Collection getExports(Resource resource) {
		return resourceIndexer.getExports(resource);
	}
	
	/**
	 * <P>
	 * Provides the extended metadata object that would be used
	 *  to load resources in compatibility mode.
	 * </P>
	 * <P>
	 * NOTE: This is not intended to be used externally or even
	 *  internally if it can be avoided.
	 * </P> 
	 *  
	 * @see MResourceOption#COMPATIBILITY_MODE
	 * 
	 * @return The extended metadata object used in this resource set.
	 */
	public ExtendedMetaData getExtendedMetaData() {
		return extendedMetaData;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getAllImports(org.eclipse.emf.ecore.resource.Resource)
	 */
	public Collection getAllImports(Resource resource) {

		Collection imports = new HashSet();
		Collection unload = new HashSet();

		getAllImports(resource, imports, unload);

		for (Iterator i = unload.iterator(); i.hasNext();)
			unloadResource((Resource) i.next());

		return imports;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getAllExports(org.eclipse.emf.ecore.resource.Resource)
	 */
	public Collection getAllExports(Resource resource) {

		Collection exports = new HashSet();
		Collection unload = new HashSet();

		getAllExports(resource, exports, unload);

		for (Iterator i = unload.iterator(); i.hasNext();)
			unloadResource((Resource) i.next());

		return exports;
	}

	/**
	 * Get all imports of a resource.
	 */
	private void getAllImports(Resource resource, Collection imports,
			Collection unload) {

		if (!resource.isLoaded()) {

			try {
				loadResource(resource);
			} catch (Exception e) {
				// ignore resources that fail to load.
			}

			unload.add(resource);
		}

		Collection directImports = getImports(resource);

		for (Iterator i = directImports.iterator(); i.hasNext();) {

			Resource directImport = (Resource) i.next();

			if (!imports.contains(directImport)) {

				imports.add(directImport);

				getAllImports(directImport, imports, unload);
			}
		}
	}

	/**
	 * Get all exports of a resource.
	 */
	private void getAllExports(Resource resource, Collection exports,
			Collection unload) {

		if (!resource.isLoaded()) {

			try {
				loadResource(resource);
			} catch (Exception e) {
				// ignore resources that fail to load.
			}

			unload.add(resource);
		}

		Collection directExports = getExports(resource);

		for (Iterator i = directExports.iterator(); i.hasNext();) {

			Resource directExport = (Resource) i.next();

			if (!exports.contains(directExport)) {

				directExports.add(directExport);

				getAllExports(directExport, exports, unload);
			}
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getResourceSets()
	 */
	public static Set getResourceSets() {
		return Collections.unmodifiableSet(resourceSets.keySet());
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getEditingDomain(org.eclipse.emf.ecore.resource.ResourceSet)
	 */
	public static MEditingDomain getEditingDomain(ResourceSet resourceSet) {

		WeakReference reference = (WeakReference) resourceSets.get(resourceSet);

		if (reference != null)
			return (MSLEditingDomain) reference.get();

		return null;
	}
	
	/**
	 * Associates the specified resource set with an editing domain.
	 * 
	 * @param rset the resource set to associate with the <code>domain</code>
	 * @param domain the editing domain to associate with the resource set
	 */
	public static void setEditingDomain(ResourceSet rset, MEditingDomain domain) {
		
		MEditingDomain currentDomain = getEditingDomain(rset);
		
		if (currentDomain != domain) {
			if (currentDomain instanceof MSLEditingDomain) {
				// remove current content adapter
				rset.eAdapters().remove(((MSLEditingDomain) currentDomain).getContentAdapter());
			}
			
			if (domain instanceof MSLEditingDomain) {
				MSLEditingDomain newDomain = (MSLEditingDomain) domain;
				
				// attach new content adapter
				rset.eAdapters().add(newDomain.getContentAdapter());
				
				// for each resource that is loaded, ensure that the new domain
				//   know that it has finished loading
				for (Iterator iter = rset.getResources().iterator(); iter.hasNext();) {
					Resource next = (Resource) iter.next();
					
					if (next.isLoaded()) {
						newDomain.getResouceListener().markResourceFinishedLoading(next);
					}
				}
			}
			
			resourceSets.put(rset, new WeakReference(domain));
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#getEditingDomain(org.eclipse.emf.ecore.resource.Resource)
	 */
	public static MEditingDomain getEditingDomain(Resource resource) {

		ResourceSet resourceSet = resource.getResourceSet();

		if (resourceSet != null)
			return getEditingDomain(resourceSet);

		return null;
	}

	/**
	 * Converts a file URI to a platform URI.
	 */
	private static URI getPlatformURI(URI uri) {

		if (MSLConstants.PLATFORM_SCHEME.equals(uri.scheme()))
			return URI.createURI(uri.toString(), true);

		IFile file = findFileInWorkspace(uri);

		if (file != null) {

			IProject project = file.getProject();

			if (project != null) {

				StringBuffer pathName = new StringBuffer(project.getName());

				pathName.append(MSLConstants.PATH_SEPARATOR);
				pathName.append(file.getProjectRelativePath().toString());

				return URI.createURI(URI.createPlatformResourceURI(
					pathName.toString(),true).toString(), true);
			}
		}

		return null;
	}

	/**
	 * Finds a file in the workspace given its file URI.
	 */
	private static IFile findFileInWorkspace(URI uri) {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		if (workspace != null) {

			IWorkspaceRoot root = workspace.getRoot();

			if (root != null) {

				IFile[] files = root.findFilesForLocation(new Path(uri
					.toFileString()));

				if (files != null) {

					for (int i = 0; i < files.length; i++) {

						IFile file = files[i];

						IProject project = file.getProject();

						if (project != null)
							return file;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Creates re-gui-ided copies of the resources provided in the map. The
	 * resources must be loaded resources
	 * 
	 * @param resource2URI
	 *            a map with the resources to copy as keys and their new URIs as
	 *            values
	 * @throws IOException
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain#copyResources(java.util.Map)
	 */
	public void copyResources(Map resource2URI)
		throws IOException {

		CopyingResourceSet mslCopyingResourceSet = new CopyingResourceSet(
			getResourceSet());

		Iterator it = resource2URI.entrySet().iterator();

		while (it.hasNext()) {

			Map.Entry entry = (Map.Entry) it.next();
			new CopyingResource((XMLResource) entry.getKey(), (URI) entry
				.getValue(), mslCopyingResourceSet);
		}

		it = mslCopyingResourceSet.getResources().iterator();

		while (it.hasNext()) {

			CopyingResource copyingRes = (CopyingResource) it.next();
			copyingRes.save(Collections.EMPTY_MAP);
		}
	}

	/** @deprecated */
	public boolean isLogicalResource(Resource resource) {
		return resource instanceof org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;
	}
	
	/** @deprecated */
	public org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource asLogicalResource(Resource resource) {
		org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource result = null;
		
		if (resource instanceof org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource) {
			result = (org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource) resource;
		} else {
			result = org.eclipse.gmf.runtime.emf.core.internal.resources.LogicalResourceWrapper.get(resource);
		}
		
		return result;
	}

	/**
	 * Pushes a writer onto the stack of currently executing runnables.
	 * 
	 * @param writer
	 * 
	 * @see #runAsWrite(MRunnable)
	 * @see #popWriter()
	 */
	protected synchronized final void pushWriter(MRunnable writer) {
		writers.add(writers.size(), writer);
	}

	/**
	 * Pops the topmost writer from the stack of currently executing runnables.
	 * 
	 * @return the topmost writer from the stack
	 */
	protected synchronized final MRunnable popWriter() {
		return (MRunnable) writers.remove(writers.size() - 1);
	}

	/**
	 * Peeks at the top of the stack of currently executing runnables.
	 * 
	 * @return the top writer, or <code>null</code> if there are no writers
	 */
	protected synchronized final MRunnable peekWriter() {
		return writers.isEmpty() ? null
			: (MRunnable) writers.get(writers.size() - 1);
	}

	public void yieldForReads() {
		undoStack.yieldForReads();

	}
	
	/**
	 * Unwinds the read actions at the top of the {@link MSLUndoStack}'s action
	 * stack, if any.
	 * 
	 * @return the number (zero or more) of read actions that were removed.
	 *         These should be restored later by a call to
	 *         {@link #rewindReadActions(int)}
	 * 
	 * @see #rewindReadActions(int)
	 */
	private int unwindReadActions() {
		int result = 0;

		while (canCurrentThreadRead() && !canWrite()) {
			result++;
			complete();
		}
		return result;
	}

	/**
	 * Re-opens read actions previously closed by a call to
	 * {@link #unwindReadActions()}
	 * 
	 * @param count
	 *            the number (zero or more) of read actions to restore
	 * 
	 * @see #unwindReadActions()
	 */
	private void rewindReadActions(int count) {
		for (int i = 0; i < count; i++) {
			startRead();
		}
	}

	public boolean isControllable(Object object) {
		// TODO Auto-generated method stub
		return false;
	}
}