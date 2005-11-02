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


package org.eclipse.gmf.runtime.diagram.ui.internal.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import org.eclipse.gmf.runtime.common.ui.preferences.CheckBoxFieldEditor;
import org.eclipse.gmf.runtime.common.ui.preferences.ComboFieldEditor;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.figures.DiagramColorConstants;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.preferences.AppearancePreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;

/**
 * The appearance preference page used for the Modeling preferences.
 * 
 * TODO: Move to Modeler / Visualizer common plugin.
 * 
 * @author cmahoney
 */
public class ModelingAppearancePreferencePage
	extends AppearancePreferencePage {

	private String STYLE_GROUPBOX_LABEL = DiagramResourceManager
		.getI18NString("GeneralPreferencePage.styleGroupBox.label");//$NON-NLS-1$

	private String STYLE_COMBO_LABEL = DiagramResourceManager
		.getI18NString("GeneralPreferencePage.styleComboBox.label");//$NON-NLS-1$

	private String DEFAULT_LABEL = DiagramResourceManager
		.getI18NString("GeneralPreferencePage.style.default");//$NON-NLS-1$

	private String CLASSIC_LABEL = DiagramResourceManager
		.getI18NString("GeneralPreferencePage.style.classic");//$NON-NLS-1$

	private String COMMENT_FILL_COLOR_LABEL = DiagramResourceManager
		.getI18NString("GeneralPreferencePage.commentFillColor.label"); //$NON-NLS-1$

	private String COMMENT_LINE_COLOR_LABEL = DiagramResourceManager
		.getI18NString("GeneralPreferencePage.commentLineColor.label"); //$NON-NLS-1$

	private String CONSTRAINT_FILL_COLOR_LABEL = DiagramResourceManager
		.getI18NString("GeneralPreferencePage.constraintFillColor.label"); //$NON-NLS-1$

	private String CONSTRAINT_LINE_COLOR_LABEL = DiagramResourceManager
		.getI18NString("GeneralPreferencePage.constraintLineColor.label"); //$NON-NLS-1$

	private String GLOBAL_GROUPBOX_LABEL = DiagramResourceManager
		.getI18NString("GeneralPreferencePage.globalGroupBox.label");//$NON-NLS-1$

	private String SHOW_DROPSHADOW_LABEL = DiagramResourceManager
		.getI18NString("DiagramsPreferencePage.showDropShadow.label"); //$NON-NLS-1$

	private String SHOW_BOLD_NAME_LABEL = DiagramResourceManager
		.getI18NString("DiagramsPreferencePage.showBoldName.label"); //$NON-NLS-1$

	private String SHOW_GRADIENT_LABEL = DiagramResourceManager
		.getI18NString("DiagramsPreferencePage.showGradient.label"); //$NON-NLS-1$

	private ColorFieldEditor commentFillColorEditor = null;

	private ColorFieldEditor commentLineColorEditor = null;

	private ColorFieldEditor constraintFillColorEditor = null;

	private ColorFieldEditor constraintLineColorEditor = null;

	private Combo styleCombo;

	private ComboFieldEditor styleDisplayField;

	private CheckBoxFieldEditor showDropShadowEditor = null;

	private CheckBoxFieldEditor showBoldNameEditor = null;

	private CheckBoxFieldEditor showGradientEditor = null;

	/**
	 * Creates a new instance and initializes the preference store.
	 */
	public ModelingAppearancePreferencePage() {
		super();
		setPreferenceStore(DiagramUIPlugin.getInstance().getPreferenceStore());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.preferences.AbstractPreferencePage#addFields(org.eclipse.swt.widgets.Composite)
	 */
	protected void addFields(Composite parent) {
		Composite main = createPageLayout(parent);

		createStyleGroup(main);
		createFontAndColorGroup(main);
		createGlobalGroup(main);
	}

	protected void addFontAndColorFields(Composite composite) {
		super.addFontAndColorFields(composite);

		commentFillColorEditor = new ColorFieldEditor(
			IPreferenceConstants.PREF_COMMENT_FILL_COLOR,
			COMMENT_FILL_COLOR_LABEL, composite);
		addField(commentFillColorEditor);

		commentLineColorEditor = new ColorFieldEditor(
			IPreferenceConstants.PREF_COMMENT_LINE_COLOR,
			COMMENT_LINE_COLOR_LABEL, composite);
		addField(commentLineColorEditor);

		constraintFillColorEditor = new ColorFieldEditor(
			IPreferenceConstants.PREF_CONSTRAINT_FILL_COLOR,
			CONSTRAINT_FILL_COLOR_LABEL, composite);
		addField(constraintFillColorEditor);

		constraintLineColorEditor = new ColorFieldEditor(
			IPreferenceConstants.PREF_CONSTRAINT_LINE_COLOR,
			CONSTRAINT_LINE_COLOR_LABEL, composite);
		addField(constraintLineColorEditor);
	}

	/**
	 * Create the style group for the preference page.
	 * 
	 * @param parent
	 * @return composite styleGroup
	 */
	protected Composite createStyleGroup(Composite parent) {

		Group styleGroup = new Group(parent, SWT.NONE);
		styleGroup.setText(STYLE_GROUPBOX_LABEL);
		styleGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		styleGroup.setLayout(new GridLayout(2, false));

		Composite composite = new Composite(styleGroup, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		composite.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.GRAB_HORIZONTAL
			| GridData.HORIZONTAL_ALIGN_FILL);
		composite.setLayoutData(gridData);

		styleDisplayField = new ComboFieldEditor(STYLE_COMBO_LABEL,
			STYLE_COMBO_LABEL, composite, ComboFieldEditor.INT_TYPE, false, 0,
			0, true);
		addField(styleDisplayField);
		styleCombo = styleDisplayField.getComboControl();
		styleCombo.add(DEFAULT_LABEL);
		styleCombo.add(CLASSIC_LABEL);

		styleDisplayField.getComboControl().addSelectionListener(
			new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// do nothing
				}

				public void widgetSelected(SelectionEvent e) {
					if (styleDisplayField.getComboControl().getSelectionIndex() == 1)
						setClassicDefaults();
					else
						setAuroraDefaults();
				}
			});

		return styleGroup;
	}

	/**
	 * Create the global group for the preference page.
	 * 
	 * @param parent
	 * @return composite globalGroup
	 */
	protected Composite createGlobalGroup(Composite parent) {

		Group globalGroup = new Group(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		globalGroup.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		globalGroup.setLayoutData(gridData);
		globalGroup.setText(GLOBAL_GROUPBOX_LABEL);

		Composite composite = new Composite(globalGroup, SWT.NONE);

		// only support dropshadow in win32 - other OS's don't support the
		// transparency rops that we need. TBD - try this again after migration
		// to
		// SWT 2.1 to see it's fixed.
		String platform = SWT.getPlatform();
		if (platform.equals(new String("win32"))) { //$NON-NLS-1$ 
			showDropShadowEditor = new CheckBoxFieldEditor(
				IPreferenceConstants.PREF_SHOW_DROPSHADOW,
				SHOW_DROPSHADOW_LABEL, composite);
			addField(showDropShadowEditor);
		}

		showBoldNameEditor = new CheckBoxFieldEditor(
			IPreferenceConstants.PREF_BOLD_NAME, SHOW_BOLD_NAME_LABEL,
			composite);
		addField(showBoldNameEditor);

		showGradientEditor = new CheckBoxFieldEditor(
			IPreferenceConstants.PREF_SHOW_GRADIENT, SHOW_GRADIENT_LABEL,
			composite);
		addField(showGradientEditor);

		return globalGroup;
	}

	/**
	 * Initializes the default preference values for this preference store.
	 * 
	 * @param store
	 */
	public static void initDefaults(IPreferenceStore store) {
		
		AppearancePreferencePage.initDefaults(store);
		
		Color commentFillColor = DiagramColorConstants.diagramLightBlue;
		PreferenceConverter.setDefault(store,
			IPreferenceConstants.PREF_COMMENT_FILL_COLOR, commentFillColor
				.getRGB());

		Color commentLineColor = DiagramColorConstants.diagramBlue;
		PreferenceConverter.setDefault(store,
			IPreferenceConstants.PREF_COMMENT_LINE_COLOR, commentLineColor
				.getRGB());

		Color constraintFillColor = DiagramColorConstants.diagramLightRed;
		PreferenceConverter.setDefault(store,
			IPreferenceConstants.PREF_CONSTRAINT_FILL_COLOR,
			constraintFillColor.getRGB());

		Color constraintLineColor = DiagramColorConstants.diagramRed;
		PreferenceConverter.setDefault(store,
			IPreferenceConstants.PREF_CONSTRAINT_LINE_COLOR,
			constraintLineColor.getRGB());

		setDefaultGlobalPreferences(store);

	}

	/**
	 * Set the rose classic color defaults for the line and fill colors.
	 */
	protected void setClassicDefaults() {
		Color fillColor = DiagramColorConstants.diagramLightGoldYellow;
		getFillColorEditor().getColorSelector().setColorValue(
			fillColor.getRGB());

		Color lineColor = DiagramColorConstants.diagramBurgundyRed;
		getLineColorEditor().getColorSelector().setColorValue(
			lineColor.getRGB());

		FontData fontData = new FontData("Arial", 10, SWT.NORMAL);//$NON-NLS-1$
		getDefaultFontEditor().setFont(fontData);

		showDropShadowEditor.getCheckbox().setSelection(false);
		showBoldNameEditor.getCheckbox().setSelection(false);
		showGradientEditor.getCheckbox().setSelection(false);
	}

	/**
	 * Set the aurora defaults for the line and fill colors.
	 */
	protected void setAuroraDefaults() {
		Color fillColor = DiagramColorConstants.white;
		getFillColorEditor().getColorSelector().setColorValue(
			fillColor.getRGB());

		Color lineColor = DiagramColorConstants.diagramGray;
		getLineColorEditor().getColorSelector().setColorValue(
			lineColor.getRGB());

		FontData fontDataArray[] = JFaceResources.getDefaultFont()
			.getFontData();
		FontData fontData = fontDataArray[0];
		fontData.setHeight(9);
		getDefaultFontEditor().setFont(fontData);

		// Only support dropshadow in win32 - other OS's don't support the
		// transparency rops that we need. TBD - try this again after migration
		// to SWT 2.1 to see it's fixed.
		boolean bSupportDropshadow = true;
		String platform = SWT.getPlatform();
		if (!platform.equals(new String("win32"))) { //$NON-NLS-1$ 
			bSupportDropshadow = false;
		}
		showDropShadowEditor.getCheckbox().setSelection(bSupportDropshadow);

		showBoldNameEditor.getCheckbox().setSelection(true);
		showGradientEditor.getCheckbox().setSelection(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		setDefaultGlobalPreferences(getPreferenceStore());
		super.performDefaults();
	}

	/**
	 * Set the default font for this preference store.
	 * 
	 * @param store
	 *            IPreferenceStore
	 */
	static private void setDefaultGlobalPreferences(IPreferenceStore store) {

		// Only support dropshadow in win32 - other OS's don't support the
		// transparency rops that we need. TBD - try this again after migration
		// to SWT 2.1 to see it's fixed.
		boolean bSupportDropshadow = true;
		String platform = SWT.getPlatform();
		if (!platform.equals(new String("win32"))) { //$NON-NLS-1$ 
			bSupportDropshadow = false;
		}
		store.setDefault(IPreferenceConstants.PREF_SHOW_DROPSHADOW,
			bSupportDropshadow);
		store.setDefault(IPreferenceConstants.PREF_BOLD_NAME, true);
		store.setDefault(IPreferenceConstants.PREF_SHOW_GRADIENT, true);
	}
}
