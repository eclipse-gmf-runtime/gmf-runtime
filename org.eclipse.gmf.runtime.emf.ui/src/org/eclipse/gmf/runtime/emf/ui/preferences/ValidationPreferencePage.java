/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import org.eclipse.gmf.runtime.common.ui.preferences.CheckBoxFieldEditor;
import org.eclipse.gmf.runtime.common.ui.preferences.ComboFieldEditor;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;
import org.eclipse.gmf.runtime.emf.ui.internal.l10n.ResourceManager;


/**
 * The preference page for Modeler validation controls.
 *
 * @author Christian W. Damus (cdamus)
 */
public class ValidationPreferencePage 
		extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	/* Define an offset used to add spacing in the combobox so that international
	 * characters aren't truncated. */
	private static final int OFFSET = 5;

	
	private static final String LIVE_VALIDATION_GROUP_LABEL =
		ResourceManager.getI18NString("Validation.liveValidationGroupLabel"); //$NON-NLS-1$
	private static final String LIVE_VALIDATION_DESTINATION_PROMPT =
		ResourceManager.getI18NString("Validation.liveValidationDestinationPrompt"); //$NON-NLS-1$
	private static final String LIVE_VALIDATION_DESTINATION_DIALOG =
		ResourceManager.getI18NString("Validation.liveValidationDestination.dialogComboItem"); //$NON-NLS-1$
	private static final String LIVE_VALIDATION_DESTINATION_CONSOLE =
		ResourceManager.getI18NString("Validation.liveValidationDestination.consoleComboItem"); //$NON-NLS-1$
	private static final String LIVE_VALIDATION_SHOW_CONSOLE_PROMPT =
		ResourceManager.getI18NString("Validation.liveValidationShowConsolePrompt"); //$NON-NLS-1$
	private static final String LIVE_VALIDATION_WARN_DIALOG_PROMPT =
		ResourceManager.getI18NString("Validation.liveValidationWarnDialogPrompt"); //$NON-NLS-1$
	
	private ComboFieldEditor liveProblemDisplayField;
	private Combo liveProblemDisplayCombo;
	
	private CheckBoxFieldEditor warningsInDialog;
	private CheckBoxFieldEditor showOutputView;
	
	/**
	 * Initializes me.
	 */
	public ValidationPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
	}

	/**
     * The field editors for this preference page are inserted in this method.
     */
    protected void createFieldEditors() {
        final Composite parent = getFieldEditorParent();
        Composite panel = new Composite(parent, SWT.NONE);
        GridLayout panelLayout = new GridLayout();
        panelLayout.numColumns = 1;
        panel.setLayout(panelLayout);
        
		GridData blockData = new GridData();
		blockData.grabExcessHorizontalSpace = true;
		blockData.horizontalAlignment = GridData.FILL;
		blockData.horizontalSpan = 1;
		panel.setLayoutData(blockData);

		// create groups
		createLiveValidationProblemsGroup(panel);        
    }
    
    
	/**
	 * Extends the inherited method to set up the enablement of the checkboxes
	 * according to the current preference settings.
	 */
	protected void initialize() {
		super.initialize();
		
        // initialize the enablement of the checkboxes after the current
		//   preferences have been applied to the controls
        setCheckboxesEnablement();
    }

    /**
     * Create the "Live validation problems" group of the preference page.
     * 
     * @param parent the parent composite
     */
    private void createLiveValidationProblemsGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(LIVE_VALIDATION_GROUP_LABEL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        group.setLayout(layout);
        
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        group.setLayoutData(data);

        SelectionListener checkBoxUpdater = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// the "Show Output view ..." checkbox only applies when problems
				//    go to the output view
				setCheckboxesEnablement();
			}};
		
        Composite block = new Composite(group, SWT.NONE);
        liveProblemDisplayField = new ComboFieldEditor(
        	IPreferenceConstants.VALIDATION_LIVE_PROBLEMS_DISPLAY,
			LIVE_VALIDATION_DESTINATION_PROMPT,
			block,
			ComboFieldEditor.INT_TYPE,
			false,
			0,
			0,
			true);
        addField(liveProblemDisplayField);
        liveProblemDisplayCombo = liveProblemDisplayField.getComboControl();
        liveProblemDisplayCombo.add(LIVE_VALIDATION_DESTINATION_DIALOG);
        liveProblemDisplayCombo.add(LIVE_VALIDATION_DESTINATION_CONSOLE);
        data = new GridData();
        Point size = liveProblemDisplayCombo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        data.widthHint = size.x + OFFSET;
        data.grabExcessHorizontalSpace = true;
        liveProblemDisplayCombo.setLayoutData(data);
        liveProblemDisplayCombo.addSelectionListener(checkBoxUpdater);

        block = new Composite(group, SWT.NONE);
        warningsInDialog =
            new CheckBoxFieldEditor(
                IPreferenceConstants.VALIDATION_LIVE_WARNINGS_IN_DIALOG,
                LIVE_VALIDATION_WARN_DIALOG_PROMPT,
                block);
        addField(warningsInDialog);
        warningsInDialog.getCheckbox().addSelectionListener(checkBoxUpdater);
        
        block = new Composite(group, SWT.NONE);
        showOutputView =
            new CheckBoxFieldEditor(
                IPreferenceConstants.VALIDATION_LIVE_SHOW_CONSOLE,
                LIVE_VALIDATION_SHOW_CONSOLE_PROMPT,
                block);
        addField(showOutputView);
	}
    
    /**
     * Sets the enablement of the "Show warnings in dialog ..." and
     * "Show Output view ..." checkboxes according to
     * whether the Output view is the destination of live validation problems.
     */
    void setCheckboxesEnablement() {
    	final Button warningsInDialogCheckbox = warningsInDialog.getCheckbox();
    	
    	warningsInDialogCheckbox.setEnabled(
    		liveProblemDisplayCombo.getSelectionIndex()
				== ValidationLiveProblemsDestination.DIALOG.getOrdinal());
    	
    	if (warningsInDialogCheckbox.isEnabled()
    			&& !warningsInDialogCheckbox.getSelection()) {
    		showOutputView.getCheckbox().setEnabled(true);
    	} else {
    		showOutputView.getCheckbox().setEnabled(
    			liveProblemDisplayCombo.getSelectionIndex()
					== ValidationLiveProblemsDestination.CONSOLE.getOrdinal());
    	}
    }

    /**
     * This method must be implemented to obtain the correct
     * location of the preference store, as it is called by
     * getPreferenceStore().
     * 
     * @return IPreferenceStore the returned preference store
     */
    protected IPreferenceStore doGetPreferenceStore() {
        return MslUIPlugin.getDefault().getPreferenceStore();
    }

	/**
     * When the user clicks OK, save the values in the field
     * editors by calling storeValues() and also in the preference
     * store.
     * This is the same as what is done in apply.
     * @return true since the ok function completed successfully.
     * If the saving does not complete successfully, for this
     * preference page, it was because of something the user
     * cannot fix, so return true anyway otherwise the page
     * will not close and becomes annoying.
     * <P>Looking at the samples, I don't think they expect this
     * to be false.
     */
    public boolean performOk() {
        super.performOk();
        MslUIPlugin.getDefault().savePluginPreferences();

        return true;
    }

	/* (non-Javadoc)
	 * Redefines/Implements/Extends the inherited method.
	 */
	public void init(IWorkbench workbench) {
		// Nothing to do in this implementation
	}
}
