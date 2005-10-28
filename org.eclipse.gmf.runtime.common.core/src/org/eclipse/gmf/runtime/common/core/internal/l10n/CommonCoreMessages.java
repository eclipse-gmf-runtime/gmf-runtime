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
package org.eclipse.gmf.runtime.common.core.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class CommonCoreMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.common.core.internal.l10n.CommonCoreMessages";//$NON-NLS-1$

	private CommonCoreMessages() {
		// Do not instantiate
	}

	public static String XToolsPlugin__ERROR__startupErrorMessage;
	public static String XToolsPlugin__ERROR__shutdownErrorMessage;
	public static String ComboDirectoryFieldEditor_SpecifyLocation_label;
	public static String ComboDirectoryFieldEditor_BrowseButton_label;
	public static String AbstractCommand__INFO__cancelOperation;
	public static String list_separator;
	public static String list_separator_only;
	public static String list_separator_first;
	public static String list_separator_last;
	public static String list_prefix;
	public static String list_suffix;
	public static String NavigatorCategoryDescriptor__ERROR__invalidXML;
	public static String NavigatorContentTypeDescriptor__ERROR__invalidXML;
	public static String FileModificationValidator_EditProblemDialogTitle;
	public static String FileModificationValidator_EditProblemDialogMessage_part1;
	public static String FileModificationValidator_EditProblemDialogMessage_part2;
	public static String FileModificationValidator_EditProblemDialogMessage_part3;
	public static String FileModificationValidator_EditProblemDialogMessage_workspace;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CommonCoreMessages.class);
	}
}
