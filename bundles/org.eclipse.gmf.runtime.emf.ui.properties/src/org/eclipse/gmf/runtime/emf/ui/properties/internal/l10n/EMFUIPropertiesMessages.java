/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.emf.ui.properties.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class EMFUIPropertiesMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.emf.ui.properties.internal.l10n.EMFUIPropertiesMessages";//$NON-NLS-1$

	private EMFUIPropertiesMessages() {
		// Do not instantiate
	}

	public static String EMFCompositeSourcePropertyDescriptor_undoProperty_commandName;
	public static String PropertyPageSelection_PropertyDialog_selectedElementsTitle;
	public static String PropertyPageViewAction_label;
	public static String PropertyPageViewAction_tooltip;
	public static String PropertyPageViewAction_NoPropertiesMessageBox_Title;
	public static String PropertyPageViewAction_NoPropertiesMessageBox_Message;
	public static String UndoablePropertySheetEntry_commandName;

	static {
		NLS.initializeMessages(BUNDLE_NAME, EMFUIPropertiesMessages.class);
	}
}
