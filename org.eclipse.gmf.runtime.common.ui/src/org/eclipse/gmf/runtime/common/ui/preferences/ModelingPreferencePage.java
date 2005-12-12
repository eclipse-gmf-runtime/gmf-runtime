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

package org.eclipse.gmf.runtime.common.ui.preferences;

import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * This preference page provides general modeling preferences for the modeling
 * platform.
 * 
 * @author cmcgee, cmahoney
 */
public class ModelingPreferencePage
	extends AbstractPreferencePage {

	private RadioGroupFieldEditor openLaterVersion = null;

	private RadioGroupFieldEditor saveLaterVersion = null;

	/**
	 * Creates a new instance and initializes the preference store.
	 */
	public ModelingPreferencePage() {
		super();
		setPreferenceStore(CommonUIPlugin.getDefault().getPreferenceStore());
	}

	protected void addFields(Composite parent) {
		GridData gridData = null;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		composite.setLayoutData(gridData);

		addField(openLaterVersion = new RadioGroupFieldEditor(
			IPreferenceConstants.OPEN_UNRECOGNIZED_VERSIONS,
			CommonUIMessages.ModelingPreferencePage_OpenUnrecognizedContent_label,
			3,
			new String[][] {
				{
					CommonUIMessages.ModelingPreferencePage_OpenUnrecognizedContent_always,
					MessageDialogWithToggle.ALWAYS},
				{
					CommonUIMessages.ModelingPreferencePage_OpenUnrecognizedContent_never,
					MessageDialogWithToggle.NEVER},
				{
					CommonUIMessages.ModelingPreferencePage_OpenUnrecognizedContent_prompt,
					MessageDialogWithToggle.PROMPT}}, getFieldEditorParent(),
			true));

		addField(saveLaterVersion = new RadioGroupFieldEditor(
			IPreferenceConstants.SAVE_UNRECOGNIZED_VERSIONS,
			CommonUIMessages.ModelingPreferencePage_SaveUnrecognizedContent_label,
			3,
			new String[][] {
				{
					CommonUIMessages.ModelingPreferencePage_SaveUnrecognizedContent_always,
					MessageDialogWithToggle.ALWAYS},
				{
					CommonUIMessages.ModelingPreferencePage_SaveUnrecognizedContent_never,
					MessageDialogWithToggle.NEVER},
				{
					CommonUIMessages.ModelingPreferencePage_SaveUnrecognizedContent_prompt,
					MessageDialogWithToggle.PROMPT}}, getFieldEditorParent(),
			true));

		openLaterVersion.setPreferenceStore(getPreferenceStore());
		saveLaterVersion.setPreferenceStore(getPreferenceStore());
	}

	protected void initHelp() {
		// No context-help for now.
	}

	/**
	 * Initializes the default preference values for this preference store.
	 * 
	 * @param IPreferenceStore
	 *            preferenceStore
	 */
	public static void initDefaults(IPreferenceStore preferenceStore) {

		preferenceStore.setDefault(
			IPreferenceConstants.OPEN_UNRECOGNIZED_VERSIONS,
			MessageDialogWithToggle.PROMPT);

		preferenceStore.setDefault(
			IPreferenceConstants.SAVE_UNRECOGNIZED_VERSIONS,
			MessageDialogWithToggle.PROMPT);
	}
}
