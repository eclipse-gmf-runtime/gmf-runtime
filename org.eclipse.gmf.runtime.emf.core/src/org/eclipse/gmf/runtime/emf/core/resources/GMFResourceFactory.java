/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.resources;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import org.eclipse.gmf.runtime.emf.core.internal.util.EMFCoreConstants;

/**
 * A custom implementation of a resource factory. This factory when registered
 * against some file extensions or protocol schemas will create an GMFResource
 * and assigns it default save a load options.
 * 
 * @author rafikj
 */
public class GMFResourceFactory
	extends XMIResourceFactoryImpl {

	public GMFResourceFactory() {
		super();
	}

	// default load options.
	private static final Map loadOptions = new HashMap();

	// default save options.
	private static final Map saveOptions = new HashMap();

	static {

		XMIResource resource = new XMIResourceImpl();

		// default load options.
		loadOptions.putAll(resource.getDefaultLoadOptions());
		loadOptions.put(XMIResource.OPTION_LAX_FEATURE_PROCESSING, Boolean.TRUE);

		// default save options.
		saveOptions.putAll(resource.getDefaultSaveOptions());
		saveOptions.put(XMIResource.OPTION_DECLARE_XML, Boolean.TRUE);
		saveOptions.put(XMIResource.OPTION_PROCESS_DANGLING_HREF,
			XMIResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
		saveOptions.put(XMIResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
		saveOptions.put(XMIResource.OPTION_USE_XMI_TYPE, Boolean.TRUE);
		saveOptions.put(XMIResource.OPTION_SAVE_TYPE_INFORMATION, Boolean.TRUE);
		saveOptions.put(XMIResource.OPTION_SKIP_ESCAPE_URI, Boolean.FALSE);
		saveOptions.put(XMIResource.OPTION_ENCODING, EMFCoreConstants.XMI_ENCODING);
	}

	/**
	 * Get default load options.
	 */
	public static Map getDefaultLoadOptions() {
		return loadOptions;
	}

	/**
	 * Get default save options.
	 */
	public static Map getDefaultSaveOptions() {
		return saveOptions;
	}

	public Resource createResource(URI uri) {

		XMIResource resource = new GMFResource(uri);

		resource.getDefaultLoadOptions().putAll(loadOptions);
		resource.getDefaultSaveOptions().putAll(saveOptions);

		if (!resource.getEncoding().equals(EMFCoreConstants.XMI_ENCODING))
			resource.setEncoding(EMFCoreConstants.XMI_ENCODING);

		return resource;
	}
}