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

package org.eclipse.gmf.examples.runtime.emf.clipboard.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 * 
 * @author Christian Vogt (cvogt)
 */
public class EMFClipboardMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.examples.runtime.emf.clipboard.internal.l10n.EMFClipboardMessages"; //$NON-NLS-1$

	public static String pasteProblems_title;
	public static String pasteProblems_msg;
	public static String paste_label;

	static {
		NLS.initializeMessages(BUNDLE_NAME, EMFClipboardMessages.class);
	}
}
