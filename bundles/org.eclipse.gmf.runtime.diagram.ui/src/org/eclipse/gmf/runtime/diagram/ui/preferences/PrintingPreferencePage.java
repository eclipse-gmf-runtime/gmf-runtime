/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.preferences;

import org.eclipse.gmf.runtime.common.ui.preferences.AbstractPreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.PageSetupConfigBlock;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.DefaultValues;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.ILabels;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * PrintingPreferencePage enables the user to specify printing settings such as
 * measurement units, page size, page orientation and margins. 
 * 
 * @author etworkow
 */
public class PrintingPreferencePage
	extends AbstractPreferencePage {

	/** Printing preferences page ID. */
	public static String ID_PAGE_SETUP_PREF_PAGE = "id.pageSetupPreferencePage"; //$NON-NLS-1$

	private PageSetupConfigBlock fPrinterConfigurationBlock;

	/**
	 * Creates an instance of PrintingPreferencePage.
	 */
	public PrintingPreferencePage() {
		super();
	}
	
	/**
	 * Creates an instance of PrintingPreferencePage.
	 */
	public PrintingPreferencePage(IPreferenceStore store) {
		super();
		setPreferenceStore(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.preferences.AbstractPreferencePage#addFields(org.eclipse.swt.widgets.Composite)
	 */
	protected void addFields(Composite parent) {
		//fPrinterConfigurationBlock.createContents(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.preferences.AbstractPreferencePage#initHelp()
	 */
	protected void initHelp() {
		// TODO Auto-generated method stub, modeler doesn't support it yet
	}

	/**
	 * Initializes printing preferences to their default values.
	 */
	public static void initDefaults(IPreferenceStore store) {
		store.setDefault(WorkspaceViewerProperties.PREF_USE_INCHES,
			DefaultValues.DEFAULT_INCHES);
		store.setDefault(WorkspaceViewerProperties.PREF_USE_MILLIM,
			DefaultValues.DEFAULT_MILLIM);
		store.setDefault(WorkspaceViewerProperties.PREF_USE_PORTRAIT,
			DefaultValues.DEFAULT_PORTRAIT);
		store.setDefault(WorkspaceViewerProperties.PREF_USE_LANDSCAPE,
			DefaultValues.DEFAULT_LANDSCAPE);
		store.setDefault(WorkspaceViewerProperties.PREF_PAGE_SIZE, DefaultValues
			.getLocaleSpecificPageType().getName());
		store.setDefault(WorkspaceViewerProperties.PREF_PAGE_WIDTH,
			DefaultValues.getLocaleSpecificPageType().getWidth());
		store.setDefault(WorkspaceViewerProperties.PREF_PAGE_HEIGHT,
			DefaultValues.getLocaleSpecificPageType().getHeight());
		store.setDefault(WorkspaceViewerProperties.PREF_MARGIN_TOP,
			DefaultValues.DEFAULT_MARGIN_TOP);
		store.setDefault(WorkspaceViewerProperties.PREF_MARGIN_BOTTOM,
			DefaultValues.DEFAULT_MARGIN_BOTTOM);
		store.setDefault(WorkspaceViewerProperties.PREF_MARGIN_LEFT,
			DefaultValues.DEFAULT_MARGIN_LEFT);
		store.setDefault(WorkspaceViewerProperties.PREF_MARGIN_RIGHT,
			DefaultValues.DEFAULT_MARGIN_RIGHT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		super.performDefaults();
		getFPrinterConfigurationBlock().performDefaults();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		boolean result = super.performOk();
		getFPrinterConfigurationBlock().save();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		//super.createContents(parent);
		Label instruction = new Label(parent, SWT.NONE);
		instruction.setText(ILabels.LABEL_PRINT_PREFERENCE_PAGE_INSTRUCTION);
		
		getFPrinterConfigurationBlock().createContents(parent);
		return parent;
	}
	
	private PageSetupConfigBlock getFPrinterConfigurationBlock() {
		if (fPrinterConfigurationBlock == null) {
			fPrinterConfigurationBlock = new PageSetupConfigBlock(getPreferenceStore(), this);
		}
		return fPrinterConfigurationBlock;
	}

}