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
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;


/**
 * Custom library resource that uses UUIDs for URI fragments.
 */
public class LibraryResource
	extends XMIResourceImpl {

	/**
	 * Initializes me.
	 */
	public LibraryResource() {
		super();
	}

	/**
	 * Initializes me with my URI.
	 * 
	 * @param uri my URI
	 */
	public LibraryResource(URI uri) {
		super(uri);
	}

	/**
	 * We want to use UUIDs.
	 * 
	 * @return <code>true</code>, always
	 */
	protected boolean useUUIDs() {
		return true;
	}
}
