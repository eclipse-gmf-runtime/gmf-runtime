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

package org.eclipse.gmf.runtime.emf.clipboard.core.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 * 
 * @author Christian Vogt (cvogt)
 */
public class EMFClipboardCoreMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.emf.clipboard.core.internal.l10n.EMFClipboardCoreMessages"; //$NON-NLS-1$

	public static String copypaste_duplicateId;
	public static String pasteChildOperation_copyPrefix;
	public static String missing_nsUri_ERROR_;
	public static String unresolved_nsUri_ERROR_;
	public static String missing_class_ERROR_;
	public static String factory_failed_ERROR_;

	static {
		NLS.initializeMessages(BUNDLE_NAME, EMFClipboardCoreMessages.class);
	}
}
