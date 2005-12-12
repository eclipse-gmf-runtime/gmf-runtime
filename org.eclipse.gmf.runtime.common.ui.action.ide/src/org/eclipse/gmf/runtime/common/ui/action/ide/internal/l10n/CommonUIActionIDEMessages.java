/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.common.ui.action.ide.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class CommonUIActionIDEMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.common.ui.action.ide.internal.l10n.CommonUIActionIDEMessages";//$NON-NLS-1$

	private CommonUIActionIDEMessages() {
		// Do not instantiate
	}

	public static String GlobalBookmarkAction_label;
	public static String GlobalCloseProjectAction_label;
	public static String GlobalOpenProjectAction_label;
	public static String CopyToClipboardProblemDialog_title;
	public static String CopyToClipboardProblemDialog_message;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, CommonUIActionIDEMessages.class);
	}
}
