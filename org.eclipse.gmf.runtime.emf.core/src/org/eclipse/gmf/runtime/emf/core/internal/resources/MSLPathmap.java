/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.osgi.framework.Bundle;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;

/**
 * This class manages MSL pathmaps.
 * 
 * @author rafikj
 */
public class MSLPathmap {

	// path maps can be defined using an extension point: Pathmaps.

	// The variable name.
	private static final String NAME = "name"; //$NON-NLS-1$

	//The plugin containing the path.
	private static final String PLUGIN = "plugin"; //$NON-NLS-1$

	// The path.
	private static final String PATH = "path"; //$NON-NLS-1$

	// The path map as defined by the extensions
	private static final Map PATH_MAP = Collections.synchronizedMap(new HashMap());

	private MSLEditingDomain domain = null;

	/**
	 * Constructor.
	 */
	public MSLPathmap(MSLEditingDomain domain) {

		super();

		this.domain = domain;

		resyncEntries();
	}

	/**
	 * Set the value of a pathmap variable.
	 * 
	 * @param var the path map variable name
	 * @param val the path map variable value (a URI)
	 */
	public static void setPathVariable(String var, String val) {

		PATH_MAP.put(var, val);

		for (Iterator i = MEditingDomain.getResourceSets().iterator(); i
			.hasNext();) {

			ResourceSet resourceSet = (ResourceSet) i.next();

			MSLEditingDomain domain = (MSLEditingDomain) MEditingDomain
				.getEditingDomain(resourceSet);

			if (domain != null)
				domain.getPathmap().resyncEntries();
		}
	}

	/**
	 * Remove a pathmap variable.
	 */
	public static void removePathVariable(String var) {

		PATH_MAP.remove(var);

		for (Iterator i = MEditingDomain.getResourceSets().iterator(); i
			.hasNext();) {

			ResourceSet resourceSet = (ResourceSet) i.next();

			MSLEditingDomain domain = (MSLEditingDomain) MEditingDomain
				.getEditingDomain(resourceSet);

			if (domain != null)
				domain.getPathmap().resyncEntries();
		}
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

						if (val.charAt(len - 1) == MSLConstants.PATH_SEPARATOR)
							val = val.substring(0, len - 1);

						return val;
					}
				}
			}
		}

		return MSLConstants.EMPTY_STRING;
	}

	/**
	 * Configure the Pathmaps extension point.
	 */
	public static void configure(IConfigurationElement[] configs) {
		for (int i = 0; i < configs.length; ++i) {

			IConfigurationElement element = configs[i];

			String var = element.getAttribute(NAME);

			if ((var == null) || (var.length() == 0))
				continue;

			String path = element.getAttribute(PATH);

			if (path == null)
				path = MSLConstants.EMPTY_STRING;

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

				PATH_MAP.put(var, url.toString());

			} catch (IOException e) {

				Trace.catching(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_CATCHING, MSLPathmap.class,
					"configure", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Add all entries.
	 */
	private void resyncEntries() {

		// save URIs of all resources.
		Map savedURIs = new HashMap();

		ResourceSet resourceSet = domain.getResourceSet();

		for (Iterator i = resourceSet.getResources().iterator(); i.hasNext();) {

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
				&& (!MSLConstants.PATH_MAP_SCHEME.equals(key.scheme())))
				savedURIMap.put(key, uriMap.get(key));
		}

		// clear the map.
		getURIMap().clear();

		synchronized(PATH_MAP) {
			// rebuild the map.
			for (Iterator i = PATH_MAP.entrySet().iterator(); i.hasNext();) {
	
				Map.Entry entry = (Entry) i.next();
	
				addEntry((String) entry.getKey(), (String) entry.getValue());
			}
		}

		// restore the map.
		for (Iterator i = savedURIMap.keySet().iterator(); i.hasNext();) {

			URI key = (URI) i.next();

			if (key != null)
				uriMap.put(key, savedURIMap.get(key));
		}

		// denormalize all.
		denormalizeAll();

		// if some resources have changed their URI, ensure their exports are
		// dirtied.
		for (Iterator i = resourceSet.getResources().iterator(); i.hasNext();) {

			Resource resource = (Resource) i.next();

			URI uri = resource.getURI();

			URI savedURI = (URI) savedURIs.get(resource);

			if (uri != savedURI) {

				if ((uri != null) && (!uri.equals(savedURI))) {

					Collection exports = domain.getExports(resource);

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

			if (val.charAt(len - 1) != MSLConstants.PATH_SEPARATOR)
				uri.append(MSLConstants.PATH_SEPARATOR);

			URI valURI = URI.createURI(uri.toString());

			getURIMap().put(varURI, valURI);
		}
	}

	/**
	 * Normalize the URI of a set of resources.
	 */
	private void normalizeAll() {

		ResourceSet resourceSet = domain.getResourceSet();

		URIConverter converter = resourceSet.getURIConverter();

		if (converter != null) {

			for (Iterator i = resourceSet.getResources().iterator(); i
				.hasNext();) {

				Resource resource = (Resource) i.next();

				URI uri = resource.getURI();

				if ((MSLConstants.PATH_MAP_SCHEME.equals(uri.scheme()))
					&& (resource instanceof MSLResource)) {

					((MSLResource) resource)
						.setRawURI(converter.normalize(uri));
				}
			}
		}
	}

	/**
	 * Denormalize the URI of a set of resources.
	 */
	private void denormalizeAll() {

		ResourceSet resourceSet = domain.getResourceSet();

		URIConverter converter = resourceSet.getURIConverter();

		if (converter != null) {

			for (Iterator i = resourceSet.getResources().iterator(); i
				.hasNext();) {

				Resource resource = (Resource) i.next();

				URI uri = resource.getURI();

				if (resource instanceof MSLResource)
					((MSLResource) resource).setURI(converter.normalize(uri));
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

		uri.append(MSLConstants.PATH_MAP_SCHEME);
		uri.append(MSLConstants.SCHEME_SEPARATOR);
		uri.append(MSLConstants.PATH_SEPARATOR);
		uri.append(MSLConstants.PATH_SEPARATOR);
		uri.append(var);

		if (var.charAt(len - 1) != MSLConstants.PATH_SEPARATOR)
			uri.append(MSLConstants.PATH_SEPARATOR);

		return URI.createURI(uri.toString());
	}

	/**
	 * Get EMF's URI map.
	 */
	private Map getURIMap() {
		return domain.getResourceSet().getURIConverter().getURIMap();
	}
}