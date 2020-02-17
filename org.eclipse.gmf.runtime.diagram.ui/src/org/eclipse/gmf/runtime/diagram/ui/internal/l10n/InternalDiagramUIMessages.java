/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class InternalDiagramUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.diagram.ui.internal.l10n.InternalDiagramUIMessages";//$NON-NLS-1$

	private InternalDiagramUIMessages() {
		// Do not instantiate
	}

	public static String CreateOrSelectElementCommand_Label;
	public static String CreateOrSelectElementCommand_PopupMenu_UnspecifiedMenuItem_Text;
	public static String CreateOrSelectElementCommand_PopupMenu_CreateMenuItem_Text;
	public static String CreateOrSelectElementCommand_PopupMenu_SelectExistingElementMenuItem_Text;
	public static String CreateOrSelectElementCommand_PopupMenu_CreateWithoutBindingMenuItem_Text;
	
	public static String Text_InitialValue;

	static {
		NLS.initializeMessages(BUNDLE_NAME, InternalDiagramUIMessages.class);
	}
}
