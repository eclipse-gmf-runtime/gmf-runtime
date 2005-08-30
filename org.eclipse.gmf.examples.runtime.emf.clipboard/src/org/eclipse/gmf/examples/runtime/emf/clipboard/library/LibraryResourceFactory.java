/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.emf.clipboard.library;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;


/**
 * Custom factory to create our custom resources.
 * 
 * @see LibraryResource
 */
public class LibraryResourceFactory
	extends XMIResourceFactoryImpl {

	/**
	 * Initializes me.
	 */
	public LibraryResourceFactory() {
		super();
	}

	/**
	 * Creates a {@link LibraryResource}.
	 * 
	 * @return a new {@link LibraryResource}
	 */
	public Resource createResource(URI uri) {
		return new LibraryResource(uri);
	}
}
