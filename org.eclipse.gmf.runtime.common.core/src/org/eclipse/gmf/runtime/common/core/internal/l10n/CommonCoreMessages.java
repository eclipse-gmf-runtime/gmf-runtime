/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.common.core.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class CommonCoreMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.common.core.internal.l10n.CommonCoreMessages";//$NON-NLS-1$

	private CommonCoreMessages() {
		// Do not instantiate
	}

	public static String XToolsPlugin__ERROR__startupErrorMessage;
	public static String XToolsPlugin__ERROR__shutdownErrorMessage;
	public static String AbstractCommand__INFO__cancelOperation;

    public static String workspaceCommandInterrupted;
    public static String workspaceCommandFailed;
	public static String executeRecoveryFailed;
	public static String cannotUndoExecuted;
	public static String undoRecoveryFailed;
	public static String cannotRedo;
	public static String redoRecoveryFailed;
	public static String cannotUndo;
	public static String serviceProviderNotActivated;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CommonCoreMessages.class);
	}
}
