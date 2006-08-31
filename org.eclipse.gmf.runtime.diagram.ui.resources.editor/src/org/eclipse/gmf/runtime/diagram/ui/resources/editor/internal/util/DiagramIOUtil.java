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

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.FeatureNotFoundException;
import org.eclipse.emf.ecore.xmi.PackageNotFoundException;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorMessages;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.util.NotationExtendedMetaData;

public class DiagramIOUtil {
	
	// localized labels
	private static String UNABLE_TO_LOAD_DIAGRAM = EditorMessages.Diagram_UNABLE_TO_LOAD_RESOURCE;

	private static String NO_DIAGRAM_IN_RESOURCE = EditorMessages.Diagram_NO_DIAGRAM_IN_RESOURCE;

	private static interface ILoader {
		public Resource load(TransactionalEditingDomain domain, Map loadOptions, IProgressMonitor monitor) throws IOException, CoreException;
	}
	
	private static class FileLoader implements ILoader {
		private IFile fFile;
		public FileLoader(IFile file) {
			assert file != null;
			fFile = file;
		}
		
		public Resource load(TransactionalEditingDomain domain, Map loadOptions, IProgressMonitor monitor) throws IOException, CoreException {
			fFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
			URI uri = URI.createPlatformResourceURI(fFile.getFullPath()
                .toString(), true);
			
			Resource resource = domain.getResourceSet().getResource(uri, false);
			
			if (resource == null) {
				resource = domain.getResourceSet().createResource(uri);
			}
			
			if (!resource.isLoaded()) {
				Map loadingOptions = new HashMap(GMFResourceFactory.getDefaultLoadOptions());
				
                // propogate passed in options to the defaults
                Iterator iter = loadOptions.keySet().iterator();
                while (iter.hasNext()) {
                    Object key = iter.next();
                    loadingOptions.put(key, loadOptions.get(key));
                }
                
                try {
                	resource.load(loadingOptions);
                } catch (IOException e) {
                	resource.unload();
                	throw e;
                }
			}
			
			logResourceErrorsAndWarnings(resource);
						
			return resource;
		}
	}

	private static void logResourceErrorsAndWarnings(Resource resource) {
		for (Iterator iter = resource.getErrors().iterator(); iter.hasNext();) {
			Resource.Diagnostic diagnostic = (Resource.Diagnostic) iter.next();
			Log.error(EditorPlugin.getInstance(), EditorStatusCodes.ERROR, diagnostic.getMessage());				
		}

		for (Iterator iter = resource.getWarnings().iterator(); iter.hasNext();) {
			Resource.Diagnostic diagnostic = (Resource.Diagnostic) iter.next();
			Log.warning(EditorPlugin.getInstance(), EditorStatusCodes.WARNING, diagnostic.getMessage());				
		}
	}

	
	private static class StorageLoader implements ILoader {
		private IStorage fStorage;
		public StorageLoader(IStorage storage) {
			assert storage != null;
			fStorage = storage;
		}
		
		public Resource load(TransactionalEditingDomain editingDomain,
				Map loadOptions, IProgressMonitor monitor)
			throws IOException, CoreException {
            String storageName = fStorage.getName();
            URI uri = URI.createPlatformResourceURI(storageName);
            Resource resource = editingDomain.getResourceSet().getResource(uri,false);
            if (resource == null) {
                resource = editingDomain.getResourceSet().createResource(uri);
            }
            if (!resource.isLoaded()) {
                resource.load(fStorage.getContents(), loadOptions);
            }
			logResourceErrorsAndWarnings(resource);
			return resource;
		}
	}
	
	static public Diagram load(final TransactionalEditingDomain domain, final IFile file, boolean bTryCompatible, IProgressMonitor monitor) throws CoreException {
		FileLoader loader = new FileLoader(file);
		return load(domain, loader, bTryCompatible, monitor);
	}
	
	static public Diagram load(final TransactionalEditingDomain domain, final IStorage storage, boolean bTryCompatible, IProgressMonitor monitor) throws CoreException {
		ILoader loader = null;
		if(storage instanceof IFile) {
			loader = new FileLoader((IFile)storage);
		} else {
			loader = new StorageLoader(storage);
		}
		return load(domain, loader, bTryCompatible, monitor);
	}
	
	/**
	 * load an existing diagram file.
	 * 
	 * @param file
	 * @return
	 * @throws CoreException
	 */
	static private Diagram load(final TransactionalEditingDomain domain, final ILoader loader, boolean bTryCompatible, IProgressMonitor monitor) throws CoreException  {
		Resource notationModel = null;
		try {
			try {	
				// File exists with contents..
				notationModel = loader.load(domain, new HashMap(), monitor);
			} catch (Resource.IOWrappedException e) {
				if (bTryCompatible) {
					Throwable causeError = e.getCause();
					
					if (causeError == null) {
						causeError = e;
					}
					
					String errMsg = causeError.getLocalizedMessage();
					if (causeError instanceof Resource.IOWrappedException) {
						Exception exc = (Exception)((Resource.IOWrappedException) causeError)
							.getCause();
						if (exc != null) {
							causeError = exc;
						}
					}
					
					if ((causeError instanceof PackageNotFoundException 
							|| causeError instanceof ClassNotFoundException
							|| causeError instanceof FeatureNotFoundException)) {
						if (shouldLoadInCompatibilityMode(errMsg)) {
                            Map loadOptions = new HashMap();
            				
                            // We will place a special extended metadata in here to ensure that we can load diagrams
            				//  from older versions of our metamodel.
            				loadOptions.put(XMLResource.OPTION_EXTENDED_META_DATA, new NotationExtendedMetaData());
            				
                            loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
							notationModel = loader.load(domain, loadOptions, monitor);
						} else {
							// user does not want to load in compatibility mode.
							return null; 
						}
					} else {
                        throw e;
					}
				} else {
					throw e;
				}
			}
			if(notationModel == null)
				throw new RuntimeException(UNABLE_TO_LOAD_DIAGRAM);

			Iterator rootContents = notationModel.getContents().iterator();
			while(rootContents.hasNext()) {
				EObject rootElement = (EObject)rootContents.next();
				if(rootElement instanceof Diagram)
					return (Diagram)rootElement;
			}
			
			throw new RuntimeException(NO_DIAGRAM_IN_RESOURCE);
		} catch(Exception e) {
			Trace.catching(EditorPlugin.getInstance(), EditorDebugOptions.EXCEPTIONS_CATCHING, DiagramIOUtil.class, "load(IFile, boolean)", e); //$NON-NLS-1$
			CoreException thrownExcp = null;
			if(e instanceof CoreException) {
				thrownExcp = (CoreException)e;
            } else {
                String exceptionMessage = e.getLocalizedMessage();
                thrownExcp = new CoreException(new Status(IStatus.ERROR,
                    EditorPlugin.getPluginId(), EditorStatusCodes.ERROR,
                    exceptionMessage != null ? exceptionMessage
                        : "load(IFile, boolean)", e)); //$NON-NLS-1$
            }
			Trace.throwing(EditorPlugin.getInstance(), EditorDebugOptions.EXCEPTIONS_THROWING, DiagramIOUtil.class, "load(IFile, boolean)", thrownExcp); //$NON-NLS-1$
			throw thrownExcp;
		}
	}

	static public void save(TransactionalEditingDomain domain, IFile file, Diagram diagram, boolean bKeepUnrecognizedData, IProgressMonitor progressMonitor) throws CoreException {
        Map options = new HashMap();
		if(bKeepUnrecognizedData)
            options.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
        save(domain, file, diagram, progressMonitor, options);
	}
    
    static public void save(TransactionalEditingDomain domain, IFile file, Diagram diagram, IProgressMonitor progressMonitor) throws CoreException {
        Map options = new HashMap();
        save(domain, file, diagram, progressMonitor, options);
    }
	
	static public void save(TransactionalEditingDomain domain, IFile file, Diagram diagram, IProgressMonitor progressMonitor, Map options) throws CoreException {
		Resource notationModel = ((EObject) diagram).eResource();
		String fileName = file.getFullPath().toOSString();
		notationModel.setURI(URI.createPlatformResourceURI(fileName, true));
		try {
			notationModel.save(options);
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, EditorPlugin
				.getPluginId(), EditorStatusCodes.RESOURCE_FAILURE, e
				.getLocalizedMessage(), null));
		}

		if (progressMonitor != null)
			progressMonitor.done();
		
		logResourceErrorsAndWarnings(notationModel);
	}
	
		/**
	 * @param errMsg
	 * @return
	 */
	private static boolean shouldLoadInCompatibilityMode(String errMsg) {
		// no compatibility support at present
		return false;
	}
	
	public static void unload(TransactionalEditingDomain domain, Diagram diagram) {
		diagram.eResource().unload();
	}

	public static boolean hasUnrecognizedData(Resource resource) {
		// no compatibility support at present
		return false;
	}
}
