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
package org.eclipse.gmf.runtime.diagram.ui.providers.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 *
 * @author cmahoney
 */
public final class DiagramUIProvidersMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.diagram.ui.providers.internal.l10n.DiagramUIProvidersMessages";//$NON-NLS-1$

	private DiagramUIProvidersMessages() {
		// Do not instantiate
	}

	public static String DiagramMainMenu_DiagramMainMenuText;
	public static String DiagramPropertiesProvider_viewCategory;
	public static String RadialProvider_changeFontRequest_label;
	public static String RadialProvider_changeVisibilityRequest_label;
	public static String NavigateMenuManager_Navigate_ActionLabelText;
	public static String FormatMenuManager_Format_ActionLabelText;
	public static String Note_menuItem;
	public static String Text_menuItem;
	public static String Add_menuItem;

	static {
		NLS.initializeMessages(BUNDLE_NAME, DiagramUIProvidersMessages.class);
	}
}