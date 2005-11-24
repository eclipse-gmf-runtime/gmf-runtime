/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.DefaultValues;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.ILabels;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageSetupPageType;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageSetupWidgetFactory;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.preferences.PrintingPreferencePage;

/**
 * PSConfigurationBlock implements part of PSDialog allowing the user to configure
 * the following printing preferences:
 * 
 * 1. Measurement units
 * 2. Page orientation
 * 3. Page size
 * 4. Page margins
 * 
 * PSConfiguration Block is also used by PSPreferencePage.
 * 
 * @author etworkow
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class PageSetupConfigBlock implements ILabels {
	
	private Button 
		fButtonPortrait, 
		fButtonLandscape, 
		fButtonInches, 
		fButtonMillimetres;
	
	private Label
		fLabelSize,
		fLabelWidth,
		fLabelUnitWidth,
		fLabelHeight,
		fLabelUnitHeight,
		fLabelMarginTop,
		fLabelUnitMarginTop,
		fLabelMarginBottom,
		fLabelUnitMarginBottom,
		fLabelMarginLeft,
		fLabelUnitMarginLeft,
		fLabelMarginRight,
		fLabelUnitMarginRight;
	
	private Combo 
		fComboSize;
	
	private Text
		fTextWidth,
		fTextHeight,
		fTextMarginTop,
		fTextMarginBottom,
		fTextMarginLeft,
		fTextMarginRight;
	
	private Initializer fInitialier;
	private Persistor fPersistor;
	private Convertor fConvertor;
	
	private PageSetupDialog fParentDialog;
	private PrintingPreferencePage fPreferencePage;
	
	private ArrayList fControls = new ArrayList();
	private IPreferenceStore fStore;
	private String fCurrentUnit;
	private NumberFormat fNumberFormat;
	
	/**
	 * Creates an instance of PageSetupConfigBlock.
	 * 
	 * @param store Preference store for storing and reading preferences.
	 */
	public PageSetupConfigBlock(IPreferenceStore store) {
		fInitialier = new Initializer(this);
		fPersistor = new Persistor(this);
		fConvertor = new Convertor(this);
		fStore = store;
		fNumberFormat = NumberFormat.getNumberInstance();
	}
	
	/**
	 * Creates an instance of PageSetupConfigBlock.
	 * 
	 * @param store Preference store for storing and reading preferences.
	 * @param parentDialog Dialog containing this Page Setup Configuration Block.
	 */
	public PageSetupConfigBlock(IPreferenceStore store, PageSetupDialog parentDialog) {
		this(store);
		fParentDialog = parentDialog;
	}
	
	/**
	 * Creates an instance of PageSetupConfigBlock.
	 * 
	 * @param store Preference store for storing and reading preferences.
	 */
	public PageSetupConfigBlock(IPreferenceStore store, PrintingPreferencePage preferencePage) {
		this(store);
		fPreferencePage = preferencePage;
	}
	
	/**
	 * Add page setup tab to Page Setup Dialog composite.
	 * 
	 * @param parent Composite which will hold Page Setup Tab.
	 * @return Control Page Setup Folder
	 */
	public Control createContents(Composite parent) {
		TabFolder folder = new TabFolder(parent, SWT.NONE);
		//folder.setLayout(new TabFolderLayout());	
		folder.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite pageSetupComposite = createPageSetupTabContent(folder);
	
		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(LABEL_TITLE_PAGE_SETUP_TAB_ITEM);
		item.setControl(pageSetupComposite);
			
		fInitialier.initPrintControls();
		
		return folder;
	}
	
	/**
	 * Saves Page Setup preferences.  This method is called after user presses 'Ok' button in
	 * Page Setup Dialog or 'Apply' button in Printing preference page.
	 */
	public void save() {
		fPersistor.saveConfigBlockControlValues();
	}
			
	/** 
	 * Restores Page Setup Defaults.  This method is called after user presses 'Restore Defaults'
	 * button in printing preference page.
	 */
	public void performDefaults() {

		fButtonInches.setSelection(DefaultValues.DEFAULT_INCHES);
		fButtonInches.setEnabled(true);
		fButtonMillimetres.setSelection(DefaultValues.DEFAULT_MILLIM);
		fButtonMillimetres.setEnabled(true);
		
		fButtonPortrait.setSelection(DefaultValues.DEFAULT_PORTRAIT);
		fButtonLandscape.setSelection(DefaultValues.DEFAULT_LANDSCAPE);
		
		fComboSize.select(DefaultValues.getLocaleSpecificPageType().getIndex());
		fTextWidth.setText(fNumberFormat.format(DefaultValues.getLocaleSpecificPageType().getWidth()));
		fTextHeight.setText(fNumberFormat.format(DefaultValues.getLocaleSpecificPageType().getHeight()));
		
		fTextMarginTop.setText(fNumberFormat.format(DefaultValues.DEFAULT_MARGIN_TOP));
		fTextMarginBottom.setText(fNumberFormat.format(DefaultValues.DEFAULT_MARGIN_BOTTOM));
		fTextMarginLeft.setText(fNumberFormat.format(DefaultValues.DEFAULT_MARGIN_LEFT));
		fTextMarginRight.setText(fNumberFormat.format(DefaultValues.DEFAULT_MARGIN_RIGHT));
		
		fLabelUnitHeight.setText(LABEL_INCHES);
		fLabelUnitWidth.setText(LABEL_INCHES);
		fLabelUnitMarginTop.setText(LABEL_INCHES);
		fLabelUnitMarginBottom.setText(LABEL_INCHES);
		fLabelUnitMarginLeft.setText(LABEL_INCHES);
		fLabelUnitMarginRight.setText(LABEL_INCHES);	
		
		fPreferencePage.setErrorMessage(null);
	}
	
	/** 
	 * Create Page Setup tab allowing the user to choose print page preferences.
	 */
	private Composite createPageSetupTabContent(Composite folder) {
		
		Composite composite = new Composite(folder, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		
		createGroupPageOrientation(composite);
		createGroupUnits(composite);
		createGroupPaperSize(composite);
		createGroupMargin(composite);
		
		return composite;
	}
	
	/**
	 * Create part of PSDialog allowing the user to choose page orientation. 
	 */
	private void createGroupPageOrientation(Composite composite) {
		Group group = PageSetupWidgetFactory.createGroup(composite, LABEL_TITLE_GROUP_ORIENTATION);
		
		// Add two radio buttons to the group
		fButtonPortrait = PageSetupWidgetFactory.createRadioButton(group, LABEL_BUTTON_PORTRAIT);
		fButtonLandscape  = PageSetupWidgetFactory.createRadioButton(group, LABEL_BUTTON_LANDSCAPE);
	
		fControls.add(fButtonPortrait);
		fControls.add(fButtonLandscape);
	}
	
	/** 
	 * Create part of PSDialog allowing the user to choose measurement units. 
	 */
	private void createGroupUnits(Composite composite) {
		Group group = PageSetupWidgetFactory.createGroup(composite, LABEL_TITLE_GROUP_UNITS);
		
		fButtonInches = PageSetupWidgetFactory.createRadioButtonInches(group, LABEL_BUTTON_INCHES);
		fButtonMillimetres  = PageSetupWidgetFactory.createRadioButtonMillim(group, LABEL_BUTTON_MILLIMETRES);
	
		fButtonInches.addSelectionListener(new InchesSelectionListener(LABEL_INCHES));
		fButtonMillimetres.addSelectionListener(new MillimetresSelectionListener(LABEL_MILLIMETRES));
		
		fControls.add(fButtonInches);
		fControls.add(fButtonMillimetres);
	}
	
	/** 
	 * Create part of PSDialog allowing the user to specify page size, width and height. 
	 */
	private void createGroupPaperSize(Composite composite) {
		Group group = PageSetupWidgetFactory.createGroupPaperSize(composite, LABEL_TITLE_GROUP_PAPER_SIZE);
			
		fLabelSize = PageSetupWidgetFactory.createLabel(group, LABEL_PAGE_SIZE);
		fComboSize = PageSetupWidgetFactory.createComboSize(group);
		
		fLabelWidth = PageSetupWidgetFactory.createLabel(group, LABEL_PAGE_WIDTH);
		fTextWidth = PageSetupWidgetFactory.createTextWidth(group);
		fLabelUnitWidth = PageSetupWidgetFactory.createLabelUnit(group);
		
		fLabelHeight = PageSetupWidgetFactory.createLabel(group, LABEL_PAGE_HEIGHT);
		fTextHeight = PageSetupWidgetFactory.createTextHeight(group);
		fLabelUnitHeight = PageSetupWidgetFactory.createLabelUnit(group);
		
		fComboSize.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				
				int index = fComboSize.getSelectionIndex();
				
				if (isConversionNeeded()) {
					fTextHeight.setText(fNumberFormat.format(fConvertor.convertInchesToMilim(PageSetupPageType.pages[index].getHeight())));
					fTextHeight.setText(fNumberFormat.format(fConvertor.convertInchesToMilim(PageSetupPageType.pages[index].getWidth())));
				} else {
					fTextHeight.setText(fNumberFormat.format(PageSetupPageType.pages[index].getHeight()));
					fTextWidth.setText(fNumberFormat.format(PageSetupPageType.pages[index].getWidth()));
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		fTextWidth.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (fTextWidth.isFocusControl()) {
					fComboSize.select(PageSetupPageType.USER_DEFINED.getIndex());
					setOkButtonEnableStatus();
				}
			}
		});
		
		fTextHeight.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (fTextHeight.isFocusControl()) {
					fComboSize.select(PageSetupPageType.USER_DEFINED.getIndex());
					setOkButtonEnableStatus();
				}
			}
		});
		
		fControls.add(fLabelSize);
		fControls.add(fComboSize);
		fControls.add(fLabelWidth);
		fControls.add(fTextWidth);
		fControls.add(fLabelUnitWidth);
		fControls.add(fLabelHeight);
		fControls.add(fTextHeight);
		fControls.add(fLabelUnitHeight);
	}
	
	/**
	 * Sets the status of the OK button.  If the user input is invalid, OK button's
	 * status will be set to disabled.  Otherwise, it will be set to 'enabled'.
	 */
	public void setOkButtonEnableStatus() {
		// Verify all margin and page size text fields contain valid values
		boolean allValid = 
			StringValidator.isValid(fTextWidth.getText()) &&
			StringValidator.isValid(fTextHeight.getText()) &&
		
			StringValidator.isValid(fTextMarginTop.getText()) &&
			StringValidator.isValid(fTextMarginBottom.getText()) &&
			StringValidator.isValid(fTextMarginLeft.getText()) &&
			StringValidator.isValid(fTextMarginRight.getText());
		
		// Set the status of the OK button
		if (null == fPreferencePage) {
			if (allValid) {
				fParentDialog.getOkButton().setEnabled(true);
				fButtonInches.setEnabled(true);
				fButtonMillimetres.setEnabled(true);
			}
			else {
				fParentDialog.getOkButton().setEnabled(false);
				fButtonInches.setEnabled(false);
				fButtonMillimetres.setEnabled(false);
			}
		} else {
			if (allValid) {
				fPreferencePage.setValid(true);
				fPreferencePage.setErrorMessage(null);
				fButtonInches.setEnabled(true);
				fButtonMillimetres.setEnabled(true);
			}
			else {
				fPreferencePage.setValid(false);
				fPreferencePage.setErrorMessage(LABEL_PRINT_PREFERENCE_PAGE_ERROR_MSG);
				fButtonInches.setEnabled(false);
				fButtonMillimetres.setEnabled(false);
			}
		}
		
	}
		
	/** 
	 * Create part of PSDialog allowing the user to specify page margin values. 
	 */
	private void createGroupMargin(Composite composite) {
		Group group = PageSetupWidgetFactory.createGroupMargin(composite, LABEL_TITLE_GROUP_MARGIN);
		
		fLabelMarginTop = PageSetupWidgetFactory.createLabel(group, LABEL_MARGIN_TOP);
		fTextMarginTop = PageSetupWidgetFactory.createTextMargin(group);
		fLabelUnitMarginTop = PageSetupWidgetFactory.createLabelUnitMargin(group);
		
		fLabelMarginBottom = PageSetupWidgetFactory.createLabel(group, LABEL_MARGIN_BOTTOM);
		fTextMarginBottom = PageSetupWidgetFactory.createTextMargin(group);
		fLabelUnitMarginBottom = PageSetupWidgetFactory.createLabelUnitMargin(group);
		
		fLabelMarginLeft = PageSetupWidgetFactory.createLabel(group, LABEL_MARGIN_LEFT);
		fTextMarginLeft = PageSetupWidgetFactory.createTextMargin(group);
		fLabelUnitMarginLeft = PageSetupWidgetFactory.createLabelUnitMargin(group);
		
		fLabelMarginRight = PageSetupWidgetFactory.createLabel(group, LABEL_MARGIN_RIGHT);
		fTextMarginRight = PageSetupWidgetFactory.createTextMargin(group);
		fLabelUnitMarginRight = PageSetupWidgetFactory.createLabelUnitMargin(group);
		
		fTextMarginTop.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (fTextMarginTop.isFocusControl()) {
					setOkButtonEnableStatus();
				}
			}
		});
		
		fTextMarginBottom.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (fTextMarginBottom.isFocusControl()) {
					setOkButtonEnableStatus();
				}
			}
		});
		
		fTextMarginLeft.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (fTextMarginLeft.isFocusControl()) {
					setOkButtonEnableStatus();
				}
			}
		});
		
		fTextMarginRight.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (fTextMarginRight.isFocusControl()) {
					setOkButtonEnableStatus();
				}
			}
		});
		fControls.add(fLabelMarginTop);
		fControls.add(fTextMarginTop);
		fControls.add(fLabelUnitMarginTop);
		
		fControls.add(fLabelMarginBottom);
		fControls.add(fTextMarginBottom);
		fControls.add(fLabelUnitMarginBottom);
		
		fControls.add(fLabelMarginLeft);
		fControls.add(fTextMarginLeft);
		fControls.add(fLabelUnitMarginLeft);
		
		fControls.add(fLabelMarginRight);
		fControls.add(fTextMarginRight);
		fControls.add(fLabelUnitMarginRight);
	}
	
	/**
	 * Returns a specified control.
	 * 
	 * @param controlType type of control
	 * @return Control control matchin specified controlType
	 */
	public Control getControl(PageSetupControlType controlType) {
		if (controlType.equals(PageSetupControlType.BUTTON_USE_INCHES))
			return fButtonInches;
		else if (controlType.equals(PageSetupControlType.BUTTON_USE_MILLIM))
			return fButtonMillimetres;	
		
		else if (controlType.equals(PageSetupControlType.BUTTON_USE_PORTRAIT))
			return fButtonPortrait;
		else if (controlType.equals(PageSetupControlType.BUTTON_USE_LANDSCAPE))
			return fButtonLandscape;
		
		else if (controlType.equals(PageSetupControlType.COMBO_PAGE_SIZE))
			return fComboSize;
		else if (controlType.equals(PageSetupControlType.TEXT_PAGE_HEIGHT))
			return fTextHeight;
		else if (controlType.equals(PageSetupControlType.TEXT_PAGE_WIDTH))
			return fTextWidth;
		
		else if (controlType.equals(PageSetupControlType.TEXT_MARGIN_TOP))
			return fTextMarginTop;
		else if (controlType.equals(PageSetupControlType.TEXT_MARGIN_BOTTOM))
			return fTextMarginBottom;
		else if (controlType.equals(PageSetupControlType.TEXT_MARGIN_LEFT))
			return fTextMarginLeft;
		else if (controlType.equals(PageSetupControlType.TEXT_MARGIN_RIGHT))
			return fTextMarginRight;
		
		else if (controlType.equals(PageSetupControlType.LABEL_UNIT_PAGE_HEIGHT))
			return fLabelUnitWidth;
		else if (controlType.equals(PageSetupControlType.LABEL_UNIT_PAGE_WIDTH))
			return fLabelUnitHeight;
		else if (controlType.equals(PageSetupControlType.LABEL_UNIT_MARGIN_TOP))
			return fLabelUnitMarginTop;
		else if (controlType.equals(PageSetupControlType.LABEL_UNIT_MARGIN_BOTTOM))
			return fLabelUnitMarginBottom;
		else if (controlType.equals(PageSetupControlType.LABEL_UNIT_MARGIN_LEFT))
			return fLabelUnitMarginLeft;
		else if (controlType.equals(PageSetupControlType.LABEL_UNIT_MARGIN_RIGHT))
			return fLabelUnitMarginRight;
		else
			return null;
	}
	
	/**
	 * After calling this method, all controls will appear as disabled.
	 */
	public void disableAllControls() {
		Control control;
		for (int i = 0; i < fControls.size(); i++) {
			control = (Control) fControls.get(i);
			control.setEnabled(false);
		}
	}
	
	/**
	 * After calling this method, all controls will appear as enabled.
	 */
	public void enableAllControls() {
		Control control;
		for (int i = 0; i < fControls.size(); i++) {
			control = (Control) fControls.get(i);
			control.setEnabled(true);
		}
	}
	
	// INITIALIZER
	private class Initializer {

		private PageSetupConfigBlock fBlockPrint;
		
		public Initializer(PageSetupConfigBlock configBlock) {
			fBlockPrint = configBlock;
		}
		
		public void initPrintControls() {
			initValuesForUnitGroup();
			initValuesForOrientationGroup();
			initValuesForSizeGroup();
			initValuesForMarginGroup();
			initUnitLabels();
		}
		
		private void initValuesForUnitGroup() {
			initRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_INCHES, WorkspaceViewerProperties.PREF_USE_INCHES);
			initRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_MILLIM, WorkspaceViewerProperties.PREF_USE_MILLIM);
		}
		
		private void initValuesForOrientationGroup() {
			initRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_PORTRAIT, WorkspaceViewerProperties.PREF_USE_PORTRAIT);
			initRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_LANDSCAPE, WorkspaceViewerProperties.PREF_USE_LANDSCAPE);
		}
		
		private void initValuesForSizeGroup() {
			initCombo(fBlockPrint, PageSetupControlType.COMBO_PAGE_SIZE, WorkspaceViewerProperties.PREF_PAGE_SIZE);
			initText(fBlockPrint, PageSetupControlType.TEXT_PAGE_HEIGHT, WorkspaceViewerProperties.PREF_PAGE_HEIGHT);
			initText(fBlockPrint, PageSetupControlType.TEXT_PAGE_WIDTH, WorkspaceViewerProperties.PREF_PAGE_WIDTH);
		}
		
		private void initValuesForMarginGroup() {
			initText(fBlockPrint, PageSetupControlType.TEXT_MARGIN_TOP, WorkspaceViewerProperties.PREF_MARGIN_TOP);
			initText(fBlockPrint, PageSetupControlType.TEXT_MARGIN_BOTTOM, WorkspaceViewerProperties.PREF_MARGIN_BOTTOM);
			initText(fBlockPrint, PageSetupControlType.TEXT_MARGIN_LEFT, WorkspaceViewerProperties.PREF_MARGIN_LEFT);
			initText(fBlockPrint, PageSetupControlType.TEXT_MARGIN_RIGHT, WorkspaceViewerProperties.PREF_MARGIN_RIGHT);
		}
		
		private void initUnitLabels() {
			String label;
			
			if (fButtonInches.getSelection()) {
				label = LABEL_INCHES;
				fCurrentUnit = "inc"; //$NON-NLS-1$
			}
			else {
				label = LABEL_MILLIMETRES;
				fCurrentUnit = "mil"; //$NON-NLS-1$
			}
			
			fLabelUnitWidth.setText(label);
			fLabelUnitHeight.setText(label);
			fLabelUnitMarginTop.setText(label);
			fLabelUnitMarginBottom.setText(label);
			fLabelUnitMarginLeft.setText(label);
			fLabelUnitMarginRight.setText(label);
		}
			
		private void initRadioButton(PageSetupConfigBlock block, PageSetupControlType controlType, String key) {
			Button button = (Button) block.getControl(controlType);
			button.setSelection(fStore.getBoolean(key));
		}
		
		private void initText(PageSetupConfigBlock block, PageSetupControlType controlType, String key) {
			Text text = (Text) block.getControl(controlType);
			
			String value = fStore.getString(key);
			try {
				Number num = fNumberFormat.parse(value);
				
				//text.setText(numberFormatter.format(Double.parseDouble(fStore.getString(key))));
				text.setText(fNumberFormat.format(num.doubleValue()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if (isConversionNeeded()) 
				text.setText(fNumberFormat.format(fConvertor.convertToMillimetres(controlType)));
		}
	
		private void initCombo(PageSetupConfigBlock block, PageSetupControlType controlType, String key) {
			Combo combo = (Combo) block.getControl(controlType);
			String pageSize = fStore.getString(key);
			String item;
	
			for (int i = 0; i < combo.getItemCount(); i++) {
				item = combo.getItem(i);
				if (item.startsWith(pageSize)) combo.select(i);
			}
		}
	}

	// PERSISTOR
	private class Persistor {

		private PageSetupConfigBlock fBlockPrint;
		
		public Persistor(PageSetupConfigBlock configBlock) {
			fBlockPrint = configBlock;
		}
		
		public void saveConfigBlockControlValues() {	
			saveValuesFromUnitGroup();
			saveValuesFromOrientationGroup();
			saveValuesFromSizeGroup();
			saveValuesFromMarginGroup();
			
			emptyControlList();
		}
		
		private void saveValuesFromUnitGroup() {
			saveRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_INCHES, WorkspaceViewerProperties.PREF_USE_INCHES);
			saveRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_MILLIM, WorkspaceViewerProperties.PREF_USE_MILLIM);	
		}
		
		private void saveValuesFromOrientationGroup() {
			saveRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_PORTRAIT, WorkspaceViewerProperties.PREF_USE_PORTRAIT);
			saveRadioButton(fBlockPrint, PageSetupControlType.BUTTON_USE_LANDSCAPE, WorkspaceViewerProperties.PREF_USE_LANDSCAPE);
		}
		
		private void saveValuesFromSizeGroup() {
			saveCombo(fBlockPrint, PageSetupControlType.COMBO_PAGE_SIZE, WorkspaceViewerProperties.PREF_PAGE_SIZE);
			saveText(fBlockPrint, PageSetupControlType.TEXT_PAGE_HEIGHT, WorkspaceViewerProperties.PREF_PAGE_HEIGHT);
			saveText(fBlockPrint, PageSetupControlType.TEXT_PAGE_WIDTH, WorkspaceViewerProperties.PREF_PAGE_WIDTH);
		}
		
		private void saveValuesFromMarginGroup() {
			saveText(fBlockPrint, PageSetupControlType.TEXT_MARGIN_TOP, WorkspaceViewerProperties.PREF_MARGIN_TOP);
			saveText(fBlockPrint, PageSetupControlType.TEXT_MARGIN_BOTTOM, WorkspaceViewerProperties.PREF_MARGIN_BOTTOM);
			saveText(fBlockPrint, PageSetupControlType.TEXT_MARGIN_LEFT, WorkspaceViewerProperties.PREF_MARGIN_LEFT);
			saveText(fBlockPrint, PageSetupControlType.TEXT_MARGIN_RIGHT, WorkspaceViewerProperties.PREF_MARGIN_RIGHT);
		}
			
		private void saveText(PageSetupConfigBlock block, PageSetupControlType controlType, String key) {
			if (isConversionNeeded()) 
				fStore.setValue(key, fConvertor.convertToInches(controlType));
				
			else {
				Text text = (Text) block.getControl(controlType);
				fStore.setValue(key, text.getText());
			}
		}
		
		private void saveRadioButton(PageSetupConfigBlock block, PageSetupControlType controlType, String key) {
			Button button = (Button) block.getControl(controlType);
			fStore.setValue(key, button.getSelection());
		}
		
		private void saveCombo(PageSetupConfigBlock block, PageSetupControlType controlType, String key) {
			Combo combo = (Combo) block.getControl(controlType);
			int index = combo.getSelectionIndex();
			fStore.setValue(key, combo.getItem(index));
		}
		
		private void emptyControlList() {
			for (int i = 0; i < fControls.size(); i++) {
				fControls.remove(i);
			}
		}
	}

	// CONVERTOR
	private class Convertor {

		PageSetupConfigBlock fBlockPrint;
		
		public Convertor(PageSetupConfigBlock configBlock) {
			fBlockPrint = configBlock;
		}
		
		public double convertToInches(PageSetupControlType type) { 
			Text text = (Text) fBlockPrint.getControl(type);
			
			Number num = null;
			try {
				num = fNumberFormat.parse(text.getText());
				double valueInMillimetres = num.doubleValue();
				double valueInInches = convertMilimToInches(valueInMillimetres);
				return valueInInches;	
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return -1;
		}
		
		public double convertToMillimetres(PageSetupControlType type) { 
			Text text = (Text) fBlockPrint.getControl(type);
			
			Number num = null;
			try {
				num = fNumberFormat.parse(text.getText());
				double valueInInches = num.doubleValue();
				double valueInMillimetres = convertInchesToMilim(valueInInches);
				return valueInMillimetres;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return -1;
					
		}
		
		public double convertInchesToMilim(double inches) {
			return inches / 0.0394; 
		}
		
		private double convertMilimToInches(double milim) {
			return milim * 0.0394;
		}
	}
	
	// LISTENERS
	private class UnitSelectionListener implements SelectionListener {

		private String fLabel;
		
		public UnitSelectionListener(String label) {
			fLabel = label;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			updateLabels();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) { 
			// empty 
		}
		
		private void updateLabels() {
			fLabelUnitWidth.setText(fLabel);
			fLabelUnitHeight.setText(fLabel);
			fLabelUnitMarginTop.setText(fLabel);
			fLabelUnitMarginBottom.setText(fLabel);
			fLabelUnitMarginLeft.setText(fLabel);
			fLabelUnitMarginRight.setText(fLabel);
		}	
	}
	
	private class InchesSelectionListener extends UnitSelectionListener {

		public InchesSelectionListener(String label) {
			super(label);
		}
		
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button) e.getSource();
			if (b.getSelection()) {
				super.widgetSelected(e);
				if (fCurrentUnit.startsWith("mil")) { //$NON-NLS-1$
					convertValuesToInches();
					fCurrentUnit = "inc"; //$NON-NLS-1$
				}
			}
		}
		
		private void convertValuesToInches() {
			fTextWidth.setText(fNumberFormat.format(fConvertor.convertToInches(PageSetupControlType.TEXT_PAGE_WIDTH)));
			fTextHeight.setText(fNumberFormat.format(fConvertor.convertToInches(PageSetupControlType.TEXT_PAGE_HEIGHT)));
			fTextMarginTop.setText(fNumberFormat.format(fConvertor.convertToInches(PageSetupControlType.TEXT_MARGIN_TOP)));
			fTextMarginBottom.setText(fNumberFormat.format(fConvertor.convertToInches(PageSetupControlType.TEXT_MARGIN_BOTTOM)));
			fTextMarginLeft.setText(fNumberFormat.format(fConvertor.convertToInches(PageSetupControlType.TEXT_MARGIN_LEFT)));
			fTextMarginRight.setText(fNumberFormat.format(fConvertor.convertToInches(PageSetupControlType.TEXT_MARGIN_RIGHT)));
		}
	}
	
	private class MillimetresSelectionListener extends UnitSelectionListener {

		public MillimetresSelectionListener(String label) {
			super(label);
		}
		
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button) e.getSource();
			if (b.getSelection()) { 
				super.widgetSelected(e);
				if (fCurrentUnit.startsWith("inc")) { //$NON-NLS-1$
					convertValuesToMillimetres();
					fCurrentUnit = "mil"; //$NON-NLS-1$
				}
			}
		}
		
		private void convertValuesToMillimetres() {
			fTextWidth.setText(fNumberFormat.format(fConvertor.convertToMillimetres(PageSetupControlType.TEXT_PAGE_WIDTH)));
			fTextHeight.setText(fNumberFormat.format(fConvertor.convertToMillimetres(PageSetupControlType.TEXT_PAGE_HEIGHT)));
			fTextMarginTop.setText(fNumberFormat.format(fConvertor.convertToMillimetres(PageSetupControlType.TEXT_MARGIN_TOP)));
			fTextMarginBottom.setText(fNumberFormat.format(fConvertor.convertToMillimetres(PageSetupControlType.TEXT_MARGIN_BOTTOM)));
			fTextMarginLeft.setText(fNumberFormat.format(fConvertor.convertToMillimetres(PageSetupControlType.TEXT_MARGIN_LEFT)));
			fTextMarginRight.setText(fNumberFormat.format(fConvertor.convertToMillimetres(PageSetupControlType.TEXT_MARGIN_RIGHT)));
		}
	}

	private boolean isConversionNeeded() {
		if (fButtonInches.getSelection())
			return false;
		return true;
	}
}

