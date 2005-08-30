/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

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
