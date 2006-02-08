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

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.osgi.framework.Bundle;

/**
 * This class manages GMF path mappings for URI conversion.
 * 
 * @author rafikj
 */
public class PathmapManager extends AdapterImpl {

	// path maps can be defined using an extension point: Pathmaps.

	// The variable name.
	private static final String NAME = "name"; //$NON-NLS-1$

	//The plugin containing the path.
	private static final String PLUGIN = "plugin"; //$NON-NLS-1$

	// The path.
	private static final String PATH = "path"; //$NON-NLS-1$

	// The path map as defined by the extensions
	private static final Map PATH_MAP = Collections.synchronizedMap(configure());

	private static final Map instances = Collections.synchronizedMap(new WeakHashMap());
	
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
	 * @param val the path map variable value (a URI)
	 */
	public static void setPathVariable(String var, String val) {
		PATH_MAP.put(var, val);

		for (Iterator i = allInstances().iterator(); i.hasNext();) {
			((PathmapManager) i.next()).resyncEntries(true);
		}
	}

	/**
	 * Remove a pathmap variable.
	 */
	public static void removePathVariable(String var) {
		PATH_MAP.remove(var);

		for (Iterator i = allInstances().iterator(); i.hasNext();) {
			((PathmapManager) i.next()).resyncEntries(true);
		}
	}
	
	/**
	 * Obtains the resource set for which I manage the path mappings.
	 * 
	 * @return my resource set
	 */
	public ResourceSet getResourceSet() {
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
				plugin = element.getDeclaringExtension().getNamespace();

			Bundle bundle = Platform.getBundle(plugin);

			if (bundle == null)
				continue;

			URL url = bundle.getEntry(path);

			if (url == null)
				continue;

			try {

				url = Platform.resolve(url);

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

				URI uri = resource.getURI();

				if ((EMFCoreConstants.PATH_MAP_SCHEME.equals(uri.scheme()))
					&& (resource instanceof GMFResource)) {

					((GMFResource) resource)
						.setRawURI(converter.normalize(uri));
				}
			}
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

				URI uri = resource.getURI();

				if (resource instanceof GMFResource)
					((GMFResource) resource).setURI(converter.normalize(uri));
			}
		}
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
}