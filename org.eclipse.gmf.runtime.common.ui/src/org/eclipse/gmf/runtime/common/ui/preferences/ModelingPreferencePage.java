/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.preferences;

import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.ResourceManager;

/**
 * This preference page provides general modeling preferences for the modeling
 * platform.
 * 
 * @author cmcgee, cmahoney
 */
public class ModelingPreferencePage
	extends AbstractPreferencePage {

	private String OPEN_UNRECOGNIZED_CONTENT_LABEL = ResourceManager
		.getI18NString("ModelingPreferencePage.OpenUnrecognizedContent.label"); //$NON-NLS-1$

	private String OPEN_UNRECOGNIZED_CONTENT_ALWAYS = ResourceManager
		.getI18NString("ModelingPreferencePage.OpenUnrecognizedContent.always"); //$NON-NLS-1$

	private String OPEN_UNRECOGNIZED_CONTENT_NEVER = ResourceManager
		.getI18NString("ModelingPreferencePage.OpenUnrecognizedContent.never"); //$NON-NLS-1$

	private String OPEN_UNRECOGNIZED_CONTENT_PROMPT = ResourceManager
		.getI18NString("ModelingPreferencePage.OpenUnrecognizedContent.prompt"); //$NON-NLS-1$

	private String SAVE_UNRECOGNIZED_CONTENT_LABEL = ResourceManager
		.getI18NString("ModelingPreferencePage.SaveUnrecognizedContent.label"); //$NON-NLS-1$

	private String SAVE_UNRECOGNIZED_CONTENT_ALWAYS = ResourceManager
		.getI18NString("ModelingPreferencePage.SaveUnrecognizedContent.always"); //$NON-NLS-1$

	private String SAVE_UNRECOGNIZED_CONTENT_NEVER = ResourceManager
		.getI18NString("ModelingPreferencePage.SaveUnrecognizedContent.never"); //$NON-NLS-1$

	private String SAVE_UNRECOGNIZED_CONTENT_PROMPT = ResourceManager
		.getI18NString("ModelingPreferencePage.SaveUnrecognizedContent.prompt"); //$NON-NLS-1$	

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
			OPEN_UNRECOGNIZED_CONTENT_LABEL,
			3, //$NON-NLS-1$
			new String[][] {
				{OPEN_UNRECOGNIZED_CONTENT_ALWAYS,
					MessageDialogWithToggle.ALWAYS}, //$NON-NLS-1$
				{OPEN_UNRECOGNIZED_CONTENT_NEVER, MessageDialogWithToggle.NEVER}, //$NON-NLS-1$
				{OPEN_UNRECOGNIZED_CONTENT_PROMPT,
					MessageDialogWithToggle.PROMPT}}, //$NON-NLS-1$
			getFieldEditorParent(), true));

		addField(saveLaterVersion = new RadioGroupFieldEditor(
			IPreferenceConstants.SAVE_UNRECOGNIZED_VERSIONS,
			SAVE_UNRECOGNIZED_CONTENT_LABEL,
			3, //$NON-NLS-1$
			new String[][] {
				{SAVE_UNRECOGNIZED_CONTENT_ALWAYS,
					MessageDialogWithToggle.ALWAYS}, //$NON-NLS-1$
				{SAVE_UNRECOGNIZED_CONTENT_NEVER, MessageDialogWithToggle.NEVER}, //$NON-NLS-1$
				{SAVE_UNRECOGNIZED_CONTENT_PROMPT,
					MessageDialogWithToggle.PROMPT}}, //$NON-NLS-1$
			getFieldEditorParent(), true));

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
