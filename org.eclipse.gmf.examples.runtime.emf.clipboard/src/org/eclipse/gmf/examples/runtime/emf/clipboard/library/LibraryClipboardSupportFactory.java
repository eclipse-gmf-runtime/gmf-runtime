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

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.emf.clipboard.core.IClipboardSupport;
import org.eclipse.gmf.runtime.emf.clipboard.core.IClipboardSupportFactory;


/**
 * Factory to create {@link LibraryClipboardSupport}s.
 */
public class LibraryClipboardSupportFactory
	implements IClipboardSupportFactory {

	private final IClipboardSupport support = new LibraryClipboardSupport();
	
	/**
	 * Initializes me.
	 */
	public LibraryClipboardSupportFactory() {
		super();
	}

	public IClipboardSupport newClipboardSupport(EPackage ePackage) {
		// I only register support for the one Library EPackage
		return support;
	}

}
