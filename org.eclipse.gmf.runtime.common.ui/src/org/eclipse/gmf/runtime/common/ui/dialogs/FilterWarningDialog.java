/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.gmf.runtime.common.ui.internal.l10n.ResourceManager;

/**
 * A dialog class that would be displayed to warn the user when adding an
 * element to the ME while the ME's filtering settings hide such element. It
 * allows the user to decide whether to override the filtering settings in order
 * to display the element.
 * 
 * @author Yasser Lulu
 *  
 */
public class FilterWarningDialog
	extends Dialog {

	/**
	 * The dialog's title
	 */
	private static final String FILTER_WARNING_DLG_TITLE = ResourceManager
		.getI18NString("FilterWarningDialog.title"); //$NON-NLS-1$;

	/**
	 * Dialog message - modify filter options to show element
	 */
	private static final String MODIFY_FILTER_OPTIONS = ResourceManager
		.getI18NString("FilterWarningDialog.modifyOptions"); //$NON-NLS-1$

	/**
	 * Dialog message - don't show message again
	 */
	private static final String DONT_SHOW_FILTER_WARNING_DLG = ResourceManager
		.getI18NString("FilterWarningDialog.dontShowAgain"); //$NON-NLS-1$

	/**
	 * Dialog message - element filtered
	 */
	private static final String FILTER_WARNING_DLG_MESSAGE = ResourceManager
		.getI18NString("FilterWarningDialog.filteredOut"); //$NON-NLS-1$

	/**
	 * the ok button
	 */
	private Button ok_button;

	/**
	 * the modify filter settings checkbox
	 */
	private Button modify_checkbox;

	/**
	 * the don't show this filter dialog checkbox
	 */
	private Button dontshow_checkbox;

	/**
	 * a boolean that stores the checked status of the dontshow_checkbox
	 */
	private boolean dontshowAgain;

	/**
	 * a boolean that stores the checked status of the modify_checkbox
	 */
	private boolean modifyFilterSettings;

	/**
	 * constructor
	 * 
	 * @param parentShell
	 *            thge dialog's parent shell
	 */
	public FilterWarningDialog(Shell parentShell) {
		super(parentShell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(FILTER_WARNING_DLG_TITLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createButtonBar(Composite parent) {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite outer = (Composite) super.createDialogArea(parent);
		outer.setSize(outer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		GridLayout layout = new GridLayout(4, true);
		outer.setLayout(layout);

		StyledText text = new StyledText(outer, SWT.MULTI | SWT.WRAP
			| SWT.READ_ONLY);
		text.setCaret(null);
		text.setFont(parent.getFont());
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		data.grabExcessHorizontalSpace = true;
		data.horizontalSpan = 3;
		data.verticalSpan = 2;
		data.widthHint = convertWidthInCharsToPixels(60);
		data.heightHint = convertHeightInCharsToPixels(4);
		text.setText(FILTER_WARNING_DLG_MESSAGE);
		text.setLayoutData(data);
		text.setCursor(null);
		text.setBackground(outer.getBackground());

		ok_button = new Button(outer, SWT.PUSH);
		ok_button.setText(IDialogConstants.OK_LABEL);
		GridData ok_button_data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
			| GridData.GRAB_HORIZONTAL);
		ok_button_data.heightHint = convertVerticalDLUsToPixels(IDialogConstants.BUTTON_HEIGHT);
		ok_button_data.widthHint = Math.max(
			convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH),
			ok_button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);

		ok_button.setLayoutData(ok_button_data);
		ok_button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				okPressed();
			}
		});
		ok_button.setFont(parent.getFont());

		modify_checkbox = new Button(outer, SWT.CHECK);
		modify_checkbox.setText(MODIFY_FILTER_OPTIONS);
		GridData modify_checkbox_data = new GridData();
		modify_checkbox_data.horizontalAlignment = GridData.FILL;
		modify_checkbox_data.verticalAlignment = GridData.BEGINNING;
		modify_checkbox_data.grabExcessHorizontalSpace = true;
		modify_checkbox_data.horizontalSpan = 3;
		modify_checkbox_data.verticalSpan = 1;
		modify_checkbox.setLayoutData(modify_checkbox_data);
		modify_checkbox.setSelection(true);
		modify_checkbox.setFont(parent.getFont());

		dontshow_checkbox = new Button(outer, SWT.CHECK);
		dontshow_checkbox.setText(DONT_SHOW_FILTER_WARNING_DLG);
		GridData dontshow_checkbox_data = new GridData();
		dontshow_checkbox_data.horizontalAlignment = GridData.FILL;
		dontshow_checkbox_data.verticalAlignment = GridData.BEGINNING;
		dontshow_checkbox_data.grabExcessHorizontalSpace = true;
		dontshow_checkbox_data.horizontalSpan = 3;
		dontshow_checkbox_data.verticalSpan = 1;
		dontshow_checkbox.setLayoutData(dontshow_checkbox_data);
		dontshow_checkbox.setFont(parent.getFont());

		return outer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		dontshowAgain = dontshow_checkbox.getSelection();
		modifyFilterSettings = modify_checkbox.getSelection();
		super.okPressed();
	}

	/**
	 * Returns the dontshowAgain check-box status.
	 * 
	 * @return boolean true if selected (checked) false otherwise
	 */
	public boolean isDontShowAgainSelected() {
		return dontshowAgain;
	}

	/**
	 * Returns the modifyFilterSettings check-box status.
	 * 
	 * @return boolean true if selected (checked) false otherwise
	 */
	public boolean isModifyFilterSettingsSelected() {
		return modifyFilterSettings;
	}
}