/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public static String MultiplicityValue_none_text;
	public static String MultiplicityValue_all_text;
	public static String MultiplicityValue_zeroOrOne_text;
	public static String MultiplicityValue_one_text;
	public static String MultiplicityValue_oneOrMore_text;
	public static String logError_badListener;

	static {
		NLS.initializeMessages(BUNDLE_NAME, EMFCoreMessages.class);
	}
}
