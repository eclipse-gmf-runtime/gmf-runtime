/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


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
