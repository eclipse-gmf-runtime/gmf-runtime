/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.ui.services.properties.internal.l10n.PSFResourceManager;

/**
 * @author Tauseef A. Israr
 * Created on: Sep 9, 2002
 */
public class ExtendedTextPropertyDescriptor
	extends ExtendedPropertyDescriptor {

	/**
	 * The title of the dialog box.
	 */
	private static String DIALOG_TITLE = PSFResourceManager.getI18NString("ExtendedTextPropertyDescriptor.PropertiesViewErrorDialog.Title"); //$NON-NLS-1$ 

	/**
	 * Constructor for ExtendedTextPropertyDescriptor.
	 * @param id
	 * @param displayName
	 */
	public ExtendedTextPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertyDescriptor#createPropertyEditor(Composite)
	 */
	public CellEditor createPropertyEditor(Composite parent) {
		if (isReadOnly())
			return null;

		TextCellEditor editor = new TextCellEditor(parent) {

			/**
			* Processes a modify event that occurred in this text cell editor.
			* This framework method performs validation and sets the error message
			* accordingly, and then reports a change via <code>fireEditorValueChanged</code>.
			* Subclasses should call this method at appropriate times. Subclasses
			* may extend or reimplement.
			*
			* @param e the SWT modify event
			*/
			protected void editOccured(ModifyEvent e) {
				String value = text.getText();
				if (value == null)
					value = ""; //$NON-NLS-1$
				Object typedValue = value;
				boolean oldValidState = isValueValid();
				boolean newValidState = isCorrect(typedValue);
				if (typedValue == null && newValidState)
					assert (false) : "Validator isn't limiting the cell editor's type range"; //$NON-NLS-1$
				if (!newValidState) {
					// try to insert the current value into the error message.
					setErrorMessage(
						MessageFormat.format(
							getErrorMessage(),
							new Object[] { value }));
				}

				valueChanged(oldValidState, newValidState);

			}

			/**
			* Processes a key release event that occurred in this cell editor.
			* <p>
			* The default implementation of this framework method interprets
			* the ESC key as canceling editing, and the RETURN key
			* as applying the current value.
			* </p>
			*
			* @param keyEvent the key event
			*/
			protected void keyReleaseOccured(KeyEvent keyEvent) {
				if (keyEvent.character == '\u001b') { // Escape character
					fireCancelEditor();
					return;
				} else if (keyEvent.character == '\r') { // Return key
					String value = text.getText();
					if (value == null) {
						value = ""; //$NON-NLS-1$
					}
					boolean newValidState = isCorrect(value);
					if (value == null && newValidState)
						assert (false) : "Validator isn't limiting the cell editor's type range"; //$NON-NLS-1$
					if (!newValidState) {
						MessageDialog.openError(
							Display.getCurrent().getActiveShell(),
							DIALOG_TITLE,
							getErrorMessage());
						fireCancelEditor();
					} else {
						fireApplyEditorValue();
						deactivate();
					}
					return;
				}
			}
		};

		editor.setValidator(this.getValidator());
		return editor;

	}

}
