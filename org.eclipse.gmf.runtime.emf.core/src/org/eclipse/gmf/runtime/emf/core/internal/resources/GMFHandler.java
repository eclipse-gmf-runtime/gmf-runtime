/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.util.Map;

import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * The SAX handler for MSL resources. Updates demand-created packages with their
 * namespace prefixes and schema locations.
 * 
 * @author khussey
 * 
 * @deprecated Use the {@link org.eclipse.gmf.runtime.emf.core.resources.GMFHandler}
 *     class, instead
 */
public class GMFHandler
	extends org.eclipse.gmf.runtime.emf.core.resources.GMFHandler {

	/**
	 * Constructs a new MSL handler for the specified resource with the
	 * specified helper and options.
	 * 
	 * @param xmiResource
	 *            The resource for the new handler.
	 * @param helper
	 *            The helper for the new handler.
	 * @param options
	 *            The load options for the new handler.
	 */
	public GMFHandler(XMLResource xmiResource, XMLHelper helper, Map options) {
		super(xmiResource, helper, options);
	}
}