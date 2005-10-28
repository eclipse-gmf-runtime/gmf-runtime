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
package org.eclipse.gmf.runtime.common.ui.action.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class CommonUIActionMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionMessages";//$NON-NLS-1$

	private CommonUIActionMessages() {
		// Do not instantiate
	}

	public static String GlobalCloseAction_label;
	public static String CopyAction_label;
	public static String GlobalCutAction_label;
	public static String GlobalDeleteAction_label;
	public static String GlobalRedoAction_label;
	public static String GlobalUndoAction_label;
	public static String GlobalRedoAction_formattedLabel;
	public static String GlobalUndoAction_formattedLabel;
	public static String GlobalFindAction_label;
	public static String GlobalMoveAction_label;
	public static String GlobalOpenAction_label;
	public static String GlobalPasteAction_label;
	public static String GlobalPrintAction_label;
	public static String GlobalPropertiesAction_label;
	public static String GlobalRefreshAction_label;
	public static String GlobalRevertAction_label;
	public static String GlobalRenameAction_label;
	public static String GlobalSaveAction_label;
	public static String SelectAllAction_label;
	public static String RevertGlobalActionHandler_messageBox_title;
	public static String RevertGlobalActionHandler_messageBox_message;
	public static String RevertGlobalActionHandler_messageBox_prompt;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CommonUIActionMessages.class);
	}
}
