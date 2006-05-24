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

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IPathVariableChangeEvent;
import org.eclipse.core.resources.IPathVariableChangeListener;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCoreDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCorePlugin;
import org.eclipse.gmf.runtime.emf.core.internal.util.EMFCoreConstants;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResource;
import org.eclipse.gmf.runtime.emf.core.resources.IPathmapManager;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This class manages GMF path mappings for URI conversion.
 * 
 * @author rafikj
 */
public class PathmapManager extends AdapterImpl implements IPathmapManager {
	// path maps can be defined using an extension point: Pathmaps
	//  or by referencing an eclipse path variable
	//  or by adding a pathmap manually

	// The variable name.
	private static final String NAME = "name"; //$NON-NLS-1$

	//The plugin containing the path.
	private static final String PLUGIN = "plugin"; //$NON-NLS-1$

	// The path.
	private static final String PATH = "path"; //$NON-NLS-1$
	
	private static final String NODE_QUALIFIER = EMFCorePlugin.getDefault().getBundle().getSymbolicName();
	private static final String PREFERENCE_KEY = "referenced.path.variables"; //$NON-NLS-1$

	// The path map as defined by the extensions and the referenced path variables and the manually
	//  added pathmaps.
	private static final Map PATH_MAP = Collections.synchronizedMap(configure());
	
	private static final Map instances = Collections.synchronizedMap(new WeakHashMap());
	
	// The list of eclipse path variables that are being used in this path map manager
	private static Set referencedPathVariablesList;
	
	private static IEclipsePreferences preferenceStore = null;
	
	static {
		IPathVariableManager pathManager = ResourcesPlugin.getWorkspace().getPathVariableManager();
		
		// We will get the initial list of referenced path variables from our preference store
		IEclipsePreferences preferences = getPreferenceStore();
		String referencedPathVariables = preferences.get(PREFERENCE_KEY, ""); //$NON-NLS-1$
		StringTokenizer tokenizer = new StringTokenizer(referencedPathVariables, " "); //$NON-NLS-1$
		referencedPathVariablesList = new HashSet(tokenizer.countTokens());
		for (;tokenizer.hasMoreTokens();) {
			String pathVariable = tokenizer.nextToken();
			addPathVariableReference(pathVariable);
		}
		// Update the preference store in case some path variables have been deleted since the
		//  last time we saved the store.
		updatePreferenceStore();
		
		// Register this listener to keep up-to-date with the eclipse path variables and update our
		//  referenced path variables appropriately.
		pathManager.addChangeListener(new IPathVariableChangeListener() {
			public void pathVariableChanged(IPathVariableChangeEvent event) {
				switch (event.getType()) {
					case IPathVariableChangeEvent.VARIABLE_DELETED:
						removePathVariableReference(event.getVariableName());
						updatePreferenceStore();
						break;
					case IPathVariableChangeEvent.VARIABLE_CHANGED:
						// We only care about variables that we are referencing that
						//  have changed.
						if (referencedPathVariablesList.contains(event.getVariableName())) {
							// Check to see if it has become incompatible
							if (!isDirectory(event.getValue())) {
								removePathVariableReference(event.getVariableName());
							} else {
								setPathVariable(event.getVariableName(), URI.createFileURI(event.getValue().toString()).toString());
							}
							
							updatePreferenceStore();
						}
						break;
				}
			}
		});
	}

	private static IEclipsePreferences getPreferenceStore() {
		if (preferenceStore == null) {
			IScopeContext ctx = new InstanceScope();
			preferenceStore = ctx.getNode(NODE_QUALIFIER);
		}
		
		return preferenceStore;
	}
	
	/**
	 * Adds a new reference to a path variable defined in eclipse
	 *  to be used by this pathmap manager. It is assumed that this
	 *  path variable is declared in the eclipes path variable manager
	 *  and that it is a valid path variable for our purposes. 
	 *  See {@link #isCompatiblePathVariable(String)} for more details.
	 *  
	 * @param pathVariable A valid path variable that has been defined in the
	 *  eclipse {@link IPathVariableManager} and is compatible with our path maps.
	 */
	public static void addPathVariableReference(String pathVariable) {
		if (getAllPathVariables().contains(pathVariable)) {
			// We already reference this path variable so we can assume that it is added
			//  and is compatible.
			return;
		}
		
		if (!isCompatiblePathVariable(pathVariable)) {
			return;
		}
		
		IPathVariableManager pathManager = ResourcesPlugin.getWorkspace().getPathVariableManager();
		IPath value = pathManager.getValue(pathVariable);
		if (value != null) {
			referencedPathVariablesList.add(pathVariable);
			setPathVariable(pathVariable, URI.createFileURI(value.toString()).toString());
		}
	}
	
	/**
	 * Updates the preference store with the current set of path variables that this manager
	 *  is currently referencing from the eclipse {@link IPathVariableManager}.
	 */
	public static void updatePreferenceStore() {
		StringBuffer referencedPathVariables = new StringBuffer();
		for (Iterator i = referencedPathVariablesList.iterator(); i.hasNext();) {
			referencedPathVariables.append((String)i.next());
			referencedPathVariables.append(' ');
		}
		
		getPreferenceStore().put(PREFERENCE_KEY, referencedPathVariables.toString());
		try {
			getPreferenceStore().flush();
		} catch (BackingStoreException e) {
			EMFCorePlugin.getDefault().getLog().log(new Status(IStatus.ERROR, EMFCorePlugin.getPluginId(), IStatus.ERROR, e.getMessage(), e));
		}
	}
	
	/**
	 * Removes a reference to a path variable defined in eclipse that was being
	 *  used by this pathmap manager.
	 *  
	 * @param pathVariable A path variable that was once referenced by this pathmap
	 *  manager pointing to a variable declared in the eclipse {@link IPathVariableManager}.
	 */
	public static void removePathVariableReference(String pathVariable) {
		if (referencedPathVariablesList.contains(pathVariable)) {
			referencedPathVariablesList.remove(pathVariable);
			unsetPathVariable(pathVariable);
		}
	}
	
	public static Set getPathVariableReferences() {
		return Collections.unmodifiableSet(referencedPathVariablesList);
	}
	
	/**
	 * Obtains a set of all path variable names, registered on the extension
	 * point and referenced from Eclipse Platform path variables.
	 * 
	 * @return the set of all mapped path variables
	 */
	public static Set getAllPathVariables() {
		return Collections.unmodifiableSet(PATH_MAP.keySet());
	}
	
	/**
	 * Queries whether the specified path variable name is registered on the
	 * extension point (versus selected by the user from the platform variables).
	 * 
	 * @param variable the variable name
	 * 
	 * @return <code>true</code> if this variable name is registered on the
	 *     path maps extension point; <code>false</code>, otherwise
	 */
	public static boolean isRegisteredPathVariable(String variable) {
		return PATH_MAP.containsKey(variable)
				&& !referencedPathVariablesList.contains(variable);
	}
	
	public static boolean isCompatiblePathVariable(String variable) {
		if (referencedPathVariablesList.contains(variable)) {
			// We assume that if this variable is already referenced then it is valid.
			return true;
		}
		
		IPathVariableManager pathManager = ResourcesPlugin.getWorkspace().getPathVariableManager();
		IPath value = pathManager.getValue(variable);
		
		if (value == null)
			return false;
		
		// Check to see if it is a directory first.
		// EMF will not correctly handle extension parsing
		//  of a pathmap URI if we point directly to a file. This
		//  means that the wrong resource factory could be called.
		// This could possibly change in the future.
		return isDirectory(value);
	}

	private static boolean isDirectory(IPath value) {
		File f = new File(value.toString());
		return (f.isDirectory());
	}

	/**
	 * Constructor.
	 */
	public PathmapManager() {

		super();

		instances.put(this, Boolean.TRUE);
	}

	/**
	 * Obtains all of the instances of this class.
	 * 
	 * @return my instances
	 */
	private static Set allInstances() {
		return instances.keySet();
	}
	
	/**
	 * Obtains the pathmap manager attached to the specified resource set, if any.
	 * 
	 * @param rset a resource set
	 * @return the attached pathmap manager, or <code>null</code> if none
	 */
	public static PathmapManager getExistingPathmapManager(ResourceSet rset) {
		PathmapManager result = null;
		List adapters = rset.eAdapters();
		
		for (int i = 0, size = adapters.size(); (result == null) && (i < size); i++) {
			Object next = adapters.get(i);
			
			if (next instanceof PathmapManager) {
				result = (PathmapManager) next;
			}
		}
		
		return result;
	}
	
	/**
	 * Set the value of a pathmap variable.
	 * 
	 * @param var the path map variable name
	 * @param val the path map variable value (a file URI)
	 */
	public static void setPathVariable(String var, String val) {
		// We must try to determine if this pathmap resides in the workspace as some container
		//  so that we store into the pathmap a substitution that is a platform:/resource 
		//  type of substitution. This is required because otherwise, pathmap URIs normalize
		//  to file URIs while platform URIs do not normalize, they remain as platform URIs.
		//  This will break some comparisons that might occur when trying to load a resource
		//  that is already loaded because the normalized version of the platform URI to be loaded
		//  will not match the normalized version of the pathmap URI causing two instances of
		//  the same resource to be loaded.
		java.net.URI valURI = java.net.URI.create(val);
		IContainer[] containers = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocationURI(valURI);
		if (containers.length == 1) {
			val = URI.createPlatformResourceURI(containers[0].getFullPath().toString(),true).toString();
		}
		IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(valURI);
		if (files.length == 1) {
			val = URI.createPlatformResourceURI(files[0].getFullPath().toString(),true).toString();
		}
		
		PATH_MAP.put(var, val);

		for (Iterator i = allInstances().iterator(); i.hasNext();) {
			((PathmapManager) i.next()).resyncEntries(true);
		}
	}

	public IStatus addPathVariable(String name, String value) {
		setPathVariable(name, value);
		
		return Status.OK_STATUS; // TODO: report accurate status
	}
	
	/**
	 * Remove a pathmap variable.
	 */
	public static void unsetPathVariable(String var) {
		PATH_MAP.remove(var);

		for (Iterator i = allInstances().iterator(); i.hasNext();) {
			((PathmapManager) i.next()).resyncEntries(true);
		}
	}
	
	public IStatus removePathVariable(String name) {
		unsetPathVariable(name);
		
		return Status.OK_STATUS; // TODO: report accurate status
	}
	
	/**
	 * Obtains the resource set for which I manage the path mappings.
	 * 
	 * @return my resource set
	 */
	private ResourceSet getResourceSet() {
		return (ResourceSet) getTarget();
	}

	/**
	 * Get the value of a pathmap variable.
	 * 
	 * @param var the path map variable name
	 * @return the path map variable value (a URI) or an empty string if
	 *    the specified variable is undefined
	 */
	public String getPathVariable(String var) {

		URI varURI = makeURI(var);

		if (varURI != null) {

			URI valURI = (URI) getURIMap().get(varURI);

			if (valURI != null) {

				String val = valURI.toString();

				if (val != null) {

					int len = val.length();

					if (len != 0) {

						if (val.charAt(len - 1) == EMFCoreConstants.PATH_SEPARATOR)
							val = val.substring(0, len - 1);

						return val;
					}
				}
			}
		}

		return EMFCoreConstants.EMPTY_STRING;
	}

	/**
	 * Configure the Pathmaps extension point.
	 */
	private static Map configure() {
		Map paths = new HashMap();
		
		IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getExtensionPoint(EMFCorePlugin.getPluginId(), "Pathmaps") //$NON-NLS-1$
				.getConfigurationElements();

		for (int i = 0; i < configs.length; ++i) {

			IConfigurationElement element = configs[i];

			String var = element.getAttribute(NAME);

			if ((var == null) || (var.length() == 0))
				continue;

			String path = element.getAttribute(PATH);

			if (path == null)
				path = EMFCoreConstants.EMPTY_STRING;

			String plugin = element.getAttribute(PLUGIN);

			if ((plugin == null) || (plugin.length() == 0))
				plugin = element.getDeclaringExtension().getNamespaceIdentifier();

			Bundle bundle = Platform.getBundle(plugin);

			if (bundle == null)
				continue;

			URL url = bundle.getEntry(path);

			if (url == null)
				continue;

			try {

				url = FileLocator.resolve(url);

				if (url == null)
					continue;

				paths.put(var, url.toString());

			} catch (IOException e) {

				Trace.catching(EMFCorePlugin.getDefault(),
					EMFCoreDebugOptions.EXCEPTIONS_CATCHING, PathmapManager.class,
					"configure", e); //$NON-NLS-1$
			}
		}

		return paths;
	}
	
	public void notifyChanged(Notification msg) {
		if (msg.getFeatureID(ResourceSet.class) == ResourceSet.RESOURCE_SET__RESOURCES) {
			switch (msg.getEventType()) {
			case Notification.ADD:
				denormalize((Resource) msg.getNewValue(), getResourceSet().getURIConverter());
				break;
			case Notification.ADD_MANY:
				List resources = (List)msg.getNewValue();
				if (resources == null)
					break;
				
				for (Iterator i = resources.iterator(); i.hasNext();) {
					denormalize((Resource)msg.getNewValue(), getResourceSet().getURIConverter());
				}
				break;
			case Notification.REMOVE:
				normalize((Resource)msg.getOldValue(), getResourceSet().getURIConverter());
				break;
			case Notification.REMOVE_MANY:
				resources = (List)msg.getNewValue();
				if (resources == null)
					break;
				
				for (Iterator i = resources.iterator(); i.hasNext();) {
					normalize((Resource)msg.getNewValue(), getResourceSet().getURIConverter());
				}
				break;
			}
		}
	}

	public void setTarget(Notifier newTarget) {
		// get the old resource set
		ResourceSet rset = getResourceSet();
		
		if (rset != null) {
			// remove all path mappings from existing resources
			resyncEntries(false);
		}
		
		super.setTarget(newTarget);
		
		// get the new resource set
		rset = getResourceSet();
		
		if (rset != null) {
			// denormalize all resources using the path mappings
			resyncEntries(true);
		}
	}
	
	/**
	 * Add all entries.
	 */
	private void resyncEntries(boolean resync) {

		// save URIs of all resources.
		Map savedURIs = new HashMap();

		ResourceSet rset = getResourceSet();
		
		if (rset == null)
			return;

		for (Iterator i = rset.getResources().iterator(); i.hasNext();) {

			Resource resource = (Resource) i.next();

			URI uri = resource.getURI();

			savedURIs.put(resource, uri);
		}

		// normalize all resource URIs before clearing the map.
		normalizeAll();

		// get the URI Map.
		Map uriMap = getURIMap();

		// save the URI Map.
		Map savedURIMap = new HashMap();

		for (Iterator i = uriMap.keySet().iterator(); i.hasNext();) {

			URI key = (URI) i.next();

			if ((key != null)
				&& (!EMFCoreConstants.PATH_MAP_SCHEME.equals(key.scheme())))
				savedURIMap.put(key, uriMap.get(key));
		}

		// clear the map.
		getURIMap().clear();

		if (resync) {
			synchronized(PATH_MAP) {
				// rebuild the map.
				for (Iterator i = PATH_MAP.entrySet().iterator(); i.hasNext();) {
		
					Map.Entry entry = (Entry) i.next();
		
					addEntry((String) entry.getKey(), (String) entry.getValue());
				}
			}
		}
		
		// restore the map.
		for (Iterator i = savedURIMap.keySet().iterator(); i.hasNext();) {

			URI key = (URI) i.next();

			if (key != null)
				uriMap.put(key, savedURIMap.get(key));
		}

		if (resync) {
			// denormalize all.
			denormalizeAll();
		}
		
		// if some resources have changed their URI, ensure their exports are
		// dirtied.
		for (Iterator i = rset.getResources().iterator(); i.hasNext();) {

			Resource resource = (Resource) i.next();

			URI uri = resource.getURI();

			URI savedURI = (URI) savedURIs.get(resource);

			if (uri != savedURI) {

				if ((uri != null) && (!uri.equals(savedURI))) {

					Collection exports = EMFCoreUtil.getExports(resource);

					for (Iterator j = exports.iterator(); j.hasNext();) {

						Resource export = (Resource) j.next();

						if (!export.isModified())
							export.setModified(true);
					}
				}
			}
		}
	}

	/**
	 * Add entry to map.
	 */
	private void addEntry(String var, String val) {

		URI varURI = makeURI(var);

		if (varURI != null) {

			int len = val.length();

			if (len == 0)
				return;

			StringBuffer uri = new StringBuffer();

			uri.append(val);

			if (val.charAt(len - 1) != EMFCoreConstants.PATH_SEPARATOR)
				uri.append(EMFCoreConstants.PATH_SEPARATOR);

			URI valURI = URI.createURI(uri.toString());

			getURIMap().put(varURI, valURI);
		}
	}

	/**
	 * Normalize the URI of a set of resources.
	 */
	private void normalizeAll() {

		ResourceSet rset = getResourceSet();

		URIConverter converter = rset.getURIConverter();

		if (converter != null) {

			for (Iterator i = rset.getResources().iterator(); i
				.hasNext();) {

				Resource resource = (Resource) i.next();
				normalize(resource, converter);
			}
		}
	}
	
	private void normalize(Resource resource, URIConverter converter) {
		URI uri = resource.getURI();
		
		if (uri == null)
			return;
		
		if ((EMFCoreConstants.PATH_MAP_SCHEME.equals(uri.scheme()))
				&& (resource instanceof GMFResource)) {

				((GMFResource) resource)
					.setRawURI(converter.normalize(uri));
			}
	}

	/**
	 * Denormalize the URI of a set of resources.
	 */
	private void denormalizeAll() {

		ResourceSet rset = getResourceSet();

		URIConverter converter = rset.getURIConverter();

		if (converter != null) {

			for (Iterator i = rset.getResources().iterator(); i
				.hasNext();) {

				Resource resource = (Resource) i.next();
				denormalize(resource, converter);
			}
		}
	}
	
	private void denormalize(Resource resource, URIConverter converter) {
		URI uri = resource.getURI();
		
		if (uri == null)
			return;

		if (resource instanceof GMFResource)
			((GMFResource) resource).setURI(converter.normalize(uri));
	}

	/**
	 * Make a pathmap uri from a pathmap variable name.
	 */
	private static URI makeURI(String var) {

		int len = var.length();

		if (len == 0)
			return null;

		StringBuffer uri = new StringBuffer();

		uri.append(EMFCoreConstants.PATH_MAP_SCHEME);
		uri.append(EMFCoreConstants.SCHEME_SEPARATOR);
		uri.append(EMFCoreConstants.PATH_SEPARATOR);
		uri.append(EMFCoreConstants.PATH_SEPARATOR);
		uri.append(var);

		if (var.charAt(len - 1) != EMFCoreConstants.PATH_SEPARATOR)
			uri.append(EMFCoreConstants.PATH_SEPARATOR);

		return URI.createURI(uri.toString());
	}

	/**
	 * Get EMF's URI map.
	 */
	private Map getURIMap() {
		return getResourceSet().getURIConverter().getURIMap();
	}

	/**
	 * Denormalizes a given resource's URI to a pathmap URI if it is possible.
	 * 
	 * @param uri A file or platform URI that has been denormalized as much
	 *  possible.
	 *  
	 * @return The original URI if it could not be denormalized any further
	 *  or a new pathmap URI otherwise.
	 */
	public static URI denormalizeURI(URI uri) {
		String uriAsString = uri.toString();
		
		String maxValueString = null;
		String maxKey = null;
		
		synchronized(PATH_MAP) {
			for (Iterator i = PATH_MAP.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry)i.next();
				String valueString = (String)entry.getValue();
				
				// Wipe out the trailing separator from the value if necessary
				if (valueString.endsWith("/")) { //$NON-NLS-1$
					valueString = valueString.substring(0,valueString.length()-1);
				}
				
				if (uriAsString.startsWith(valueString)
					&& (maxValueString == null || 
							maxValueString.length() < valueString.length())) {
					maxValueString = valueString;
					maxKey = (String)entry.getKey();
				}
			}
		}
		
		if (maxKey != null) {
			URI valueURI = URI.createURI(maxValueString);
			URI pathmapURI = makeURI(maxKey);
			
			int segmentStart = valueURI.segmentCount();
			int segmentCount = uri.segmentCount();
			
			for (int j=segmentStart; j < segmentCount; j++) {
				pathmapURI = pathmapURI.appendSegment(uri.segment(j));
			}
			
			return pathmapURI;
		}
		
		return uri;
	}
}