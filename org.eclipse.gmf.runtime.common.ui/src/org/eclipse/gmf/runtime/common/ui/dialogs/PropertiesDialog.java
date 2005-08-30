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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.gmf.runtime.common.ui.internal.l10n.ResourceManager;

/**
 * A Preference Dialog which has a Close button in place of OK/Cancel buttons,
 * and titled "Properties" in place of "Preferences"
 * 
 * @author Michael Yee
 */
public class PropertiesDialog
	extends PreferenceDialog {

	/** the close button */
	private Button closeButton;

	/** return code constant (value 2) indicating that the window was canceled. */
	static public final int CLOSE = 2;

	/**
	 * PropertiesDialog constructor
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param manager
	 *            the preference manager
	 */
	public PropertiesDialog(Shell parentShell, PreferenceManager manager) {
		super(parentShell, manager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateButtons()
	 */
	public void updateButtons() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		// create close button
		closeButton = createButton(parent, IDialogConstants.CLOSE_ID,
			IDialogConstants.CLOSE_LABEL, true);
		getShell().setDefaultButton(closeButton);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);

		// set title to "Properties"
		getShell().setText(
			ResourceManager.getI18NString("PropertiesDialog.title")); //$NON-NLS-1$

		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.CLOSE_ID) {
			close();
			return;
		}
	}
}