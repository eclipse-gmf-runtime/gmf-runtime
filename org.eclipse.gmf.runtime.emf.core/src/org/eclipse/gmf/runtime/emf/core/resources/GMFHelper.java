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

package org.eclipse.gmf.runtime.emf.core.resources;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;
import org.eclipse.gmf.runtime.emf.core.internal.util.EMFCoreConstants;

/**
 * This class changes the behavior of the default XMIHelper so that references
 * between projects are not deresolved.
 * 
 * @author rafikj
 */
public class GMFHelper
	extends XMIHelperImpl {

	/**
	 * Constructor.
	 */
	public GMFHelper(XMLResource resource) {
		super(resource);
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.XMLHelper#deresolve(org.eclipse.emf.common.util.URI)
	 */
	public URI deresolve(URI uri) {

		// if this both target and container are within a platform resource and
		// projects
		// or plugins are different then do not deresolve.
		if (((EMFCoreConstants.PLATFORM_SCHEME.equals(uri.scheme())) && (EMFCoreConstants.PLATFORM_SCHEME
			.equals(resourceURI.scheme())))
			&& ((uri.segmentCount() > 2) && (resourceURI.segmentCount() > 2))
			&& ((!uri.segments()[0].equals(resourceURI.segments()[0])) || (!uri
				.segments()[1].equals(resourceURI.segments()[1]))))
			return uri;

		return super.deresolve(uri);
	}
}
