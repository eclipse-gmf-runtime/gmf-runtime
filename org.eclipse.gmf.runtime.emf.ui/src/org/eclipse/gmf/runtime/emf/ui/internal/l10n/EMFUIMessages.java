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
package org.eclipse.gmf.runtime.emf.ui.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class EMFUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.emf.ui.internal.l10n.EMFUIMessages";//$NON-NLS-1$

	private EMFUIMessages() {
		// Do not instantiate
	}

	public static String CreateOrSelectElementCommand_Label;
	public static String CreateOrSelectElementCommand_PopupMenu_UnspecifiedMenuItem_Text;
	public static String CreateOrSelectElementCommand_PopupMenu_CreateMenuItem_Text;
	public static String CreateOrSelectElementCommand_PopupMenu_SelectExistingElementMenuItem_Text;
	public static String CreateOrSelectElementCommand_PopupMenu_CreateWithoutBindingMenuItem_Text;
	public static String SelectElementDialog_title;
	public static String SelectElementDialog_selection;
	public static String Validation_liveValidationGroupLabel;
	public static String Validation_liveValidationDestinationPrompt;
	public static String Validation_liveValidationDestination_dialogComboItem;
	public static String Validation_liveValidationDestination_consoleComboItem;
	public static String Validation_liveValidationShowConsolePrompt;
	public static String Validation_liveValidationWarnDialogPrompt;
	public static String Validation_error;
	public static String Validation_warn;
	public static String Validation_note;
	public static String Validation_problems;
	public static String Validation_rollback;
	public static String Validation_liveError;
	public static String Validation_liveWarning_part1;
	public static String Validation_liveWarning_part2;
	public static String Validation_liveDialogTitle;
	public static String Validation_dontShowCheck;
	public static String Validation_outputProviderCategory;
	public static String PathmapsPreferencePage_availablePathVariables;
	public static String PathmapsPreferencePage_pathVariablesUsedInModeling;
	public static String PathmapsPreferencePage_addChevron;
	public static String PathmapsPreferencePage_removeChevron;
	public static String PathmapsPreferencePage_incompatiblePathVariableErrorMessage;
	public static String PathmapsPreferencePage_registeredPathVariableErrorMessage;
	public static String PathmapsPreferencePage_mainDescription;
	public static String PathmapsPreferencePage_addAllChevron;
	public static String PathmapsPreferencePage_removeAllChevron;

	static {
		NLS.initializeMessages(BUNDLE_NAME, EMFUIMessages.class);
	}
}
