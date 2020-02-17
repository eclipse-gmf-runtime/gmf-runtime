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


package org.eclipse.gmf.runtime.notation.providers.internal.copypaste;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.emf.clipboard.core.IClipboardSupport;
import org.eclipse.gmf.runtime.emf.clipboard.core.IClipboardSupportFactory;


/**
 * Implementation of the extension point for creating clipboard support
 * utilities.
 *
 * @author Christian W. Damus (cdamus)
 */
public class ClipboardSupportFactory
	implements IClipboardSupportFactory {

	private final IClipboardSupport clipHelper =
		new NotationClipboardOperationHelper();

	/**
	 * Initializes me.
	 */
	public ClipboardSupportFactory() {
		super();
	}

	/* (non-Javadoc)
	 * Implements the interface method.
	 */
	public IClipboardSupport newClipboardSupport(EPackage ePackage) {
		return clipHelper;
	}

}
