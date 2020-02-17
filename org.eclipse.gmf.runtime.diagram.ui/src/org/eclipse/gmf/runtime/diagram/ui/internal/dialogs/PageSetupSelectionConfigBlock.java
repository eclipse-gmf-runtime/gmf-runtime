/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs;

import java.util.ArrayList;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.ILabels;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageSetupWidgetFactory;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.preferences.PrintingPreferencePage;

/**
 * PSSelectionConfigurationBlock implements part of PSDialog allowing the user to toggle
 * between Diagram or Workspace settings.  It consists of the following controls:
 * 
 * 1. Use workspace settings radio button
 * 2. Use diagram settings radio button
 * 3. Configure workspace settings button
 * 
 * @author etworkow
 */
public class PageSetupSelectionConfigBlock implements ILabels {
	
	private Button 
		fButtonUseWorkspaceSettings,
		fButtonConfigure,
		fButtonUseDiagramSettings;
	
	private Shell fShell;

	/** Read preference values from preference store and uses them to initialize all controls. */
	private Initializer fInitialier;
	
	/** Saves preference values in preference store. */
	private Persistor fPersistor;

	/** List of all controls. */
	private ArrayList fControls = new ArrayList();
	
	/** Store holding all preferences */
	private IPreferenceStore fStore;

	/**
	 * Store holding the global preferences if the user chooses to configure the
	 * global page setting preferences
	 */
	private IPreferenceStore fGlobalStore;

	/** Parent dialog hosting thise selection configuration block. */
	private PageSetupDialog fParentDialog;
	
	/**
	 * Creates an instance of PageSetupSelectionConfigBlock.
	 * 
	 * @param store
	 *            Preference store used to initialize
	 *            PSSelectionConfigurationBlock
	 * @param globalStore
	 *            the global preference store used if the user chooses to
	 *            configure the global page setting preferences from this dialog
	 * @param dialog
	 */
	public PageSetupSelectionConfigBlock(IPreferenceStore store, IPreferenceStore globalStore, PageSetupDialog dialog) {
		fInitialier = new Initializer(this);
		fPersistor = new Persistor(this);
		fStore = store;
		fGlobalStore = globalStore;
		fParentDialog = dialog;
	}
	
	/**
	 * Create composite with three buttons allowing the user to toggle
	 * between diagram and workspace setting.
	 * 
	 * @param parent Composite holding the buttons
	 * @return Control composite with buttons
	 */
	protected Control createContents(Composite parent) {	
		Composite selectionComposite = new Composite(parent, SWT.NULL);
		selectionComposite.setLayout(new GridLayout(2, true));
				
		createSelectionGroup(selectionComposite);
		fShell = parent.getShell();
		
		fInitialier.initSelectionControls();
		
		return selectionComposite;
	}
	
	/**
	 * Saves preferences in a preference store.
	 */
	protected void save() {
		fPersistor.saveConfigBlockControlValues();
	}
	
	/** 
	 * Create part of PSDialog allowing the user to choose between diagram and workspace settings.
	 */
	private void createSelectionGroup(Composite composite) {
		
		// Add two radio buttons to the group
		fButtonUseWorkspaceSettings = PageSetupWidgetFactory.createRadioButton(composite, LABEL_BUTTON_USE_WORKSPACE_SETTINGS);
		fButtonConfigure = PageSetupWidgetFactory.createPushButton(composite, LABEL_BUTTON_CONFIGURE_WORKSPACE_SETTINGS);
		fButtonUseDiagramSettings = PageSetupWidgetFactory.createRadioButton(composite, LABEL_BUTTON_USE_DIAGRAM_SETTINGS);
		
		if (fGlobalStore == null
			|| !(fGlobalStore instanceof IPersistentPreferenceStore)) {
			fButtonUseWorkspaceSettings.setEnabled(false);
		}
		
		addUseWorkspaceSettingsListener();
		addUseDiagramSettingsListener();
		addConfigureWorkspaceSettingsListener();
		
		fControls.add(fButtonUseWorkspaceSettings);
		fControls.add(fButtonConfigure);
		fControls.add(fButtonUseDiagramSettings);
	}
	
	/**
	 * Attach selection handling logic to 'Use workspace settings' button. 
	 */
	private void addUseWorkspaceSettingsListener() {
		fButtonUseWorkspaceSettings.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				fParentDialog.getConfigurationBlock().disableAllControls();
				fButtonConfigure.setEnabled(true);
				fParentDialog.getOkButton().setEnabled(true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	
	/** 
	 * Attach selection handling logic to 'Use diagram settings' button. 
	 */
	private void addUseDiagramSettingsListener() {
		fButtonUseDiagramSettings.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				fParentDialog.getConfigurationBlock().enableAllControls();
				fButtonConfigure.setEnabled(false);
				fParentDialog.getConfigurationBlock().setOkButtonEnableStatus();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}			
		});
	}
	
	/** 
	 * Attach selection handling logic to 'Configure workspace settings' button. 
	 */
	private void addConfigureWorkspaceSettingsListener() {
		fButtonConfigure.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				// Display Printing preference page allowing the user to configure global printing settings
				IPreferencePage page = new PrintingPreferencePage(fGlobalStore);
				page.setTitle(ILabels.LABEL_PREFERENCE_PAGE_PRINTING);
				
				IPreferenceNode targetNode = new PreferenceNode(ID_PAGE_SETUP_PREF_PAGE, page);
				PreferenceManager manager = new PreferenceManager();
				manager.addToRoot(targetNode);
				
				PreferenceDialog dialog = new PreferenceDialog(fShell, manager);
				
				dialog.create();
				dialog.setMessage(TITLE_PAGE_SETUP_TAB_ITEM);
				dialog.open();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
			
		});
	}

	/** 
	 * Return specified control. 
	 */
	public Control getControl(PageSetupControlType controlType) {
		if (controlType.equals(PageSetupControlType.BUTTON_USE_WORKSPACE_SETTINGS))
			return fButtonUseWorkspaceSettings;
		else if (controlType.equals(PageSetupControlType.BUTTON_USE_DIAGRAM_SETTINGS))
			return fButtonUseDiagramSettings;
		else if (controlType.equals(PageSetupControlType.BUTTON_CONFIGURE_WORKSPACE_SETTINGS))
			return fButtonConfigure;
		else
			return null;
	}
	
	/** 
	 * Calling this method will disable the 'Configure workspace settings' button. 
	 */
	public void disableButtonConfigure() {
		fButtonConfigure.setEnabled(false);
	}
	
	/** 
	 * Calling this method will enable the 'Configure workspace settings' button. 
	 */
	public void enableButtonConfigure() {
		fButtonConfigure.setEnabled(true);
	}
	
	// INITIALIZER
	private class Initializer {

		private PageSetupSelectionConfigBlock fBlockPrint;

		public Initializer(PageSetupSelectionConfigBlock configBlock) {
			fBlockPrint = configBlock;
		}
		
		public void initSelectionControls() {
			initRadioButtons();
		}
		
		private void initRadioButtons() {
			initRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_WORKSPACE_SETTINGS, WorkspaceViewerProperties.PREF_USE_WORKSPACE_SETTINGS);
			initRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_DIAGRAM_SETTINGS, WorkspaceViewerProperties.PREF_USE_DIAGRAM_SETTINGS);
		}
		
		private void initRadioButton(PageSetupSelectionConfigBlock block, PageSetupControlType controlType, String key) {
			Button button = (Button) block.getControl(controlType);
			button.setSelection(fStore.getBoolean(key));
		}
	}

	// PERSISTOR
	private class Persistor {

		private PageSetupSelectionConfigBlock fBlockPrint;

		public Persistor(PageSetupSelectionConfigBlock configBlock) {
			fBlockPrint = configBlock;
		}
		
		public void saveConfigBlockControlValues() {	
			saveConfigRadioButtonValues();
			//fBlockPrint.emptyControlList();
		}
		
		private void saveConfigRadioButtonValues() {
			saveRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_WORKSPACE_SETTINGS, WorkspaceViewerProperties.PREF_USE_WORKSPACE_SETTINGS);
			saveRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_DIAGRAM_SETTINGS, WorkspaceViewerProperties.PREF_USE_DIAGRAM_SETTINGS);
		}
		
		private void saveRadioButton(PageSetupSelectionConfigBlock block, PageSetupControlType controlType, String key) {
			Button button = (Button) block.getControl(controlType);
			fStore.setValue(key, button.getSelection());
		}
	}
}

