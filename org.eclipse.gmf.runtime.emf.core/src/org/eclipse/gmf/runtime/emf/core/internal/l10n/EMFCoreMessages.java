/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 * 
 * @author Christian Vogt (cvogt)
 */
public class EMFCoreMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.emf.core.internal.l10n.EMFCoreMessages"; //$NON-NLS-1$

	public static String validation_multi;
	public static String validation_none;
	public static String validation_nullStatus;
	public static String operation_canceled;
	public static String operation_ok;
	public static String operation_failed;
	public static String logError_badListener;
	public static String saveContextLabel;

	static {
		NLS.initializeMessages(BUNDLE_NAME, EMFCoreMessages.class);
	}
}
