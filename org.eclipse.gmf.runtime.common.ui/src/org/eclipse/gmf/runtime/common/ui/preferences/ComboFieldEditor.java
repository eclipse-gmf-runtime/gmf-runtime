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

package org.eclipse.gmf.runtime.common.ui.preferences;

import java.util.ArrayList;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * Constructs a field editor with a label and a drop down combo box.
 * 
 * @author Wayne Diu, wdiu
 */
public class ComboFieldEditor
	extends FieldEditor {

	/**
	 * Whether the label and combo should be on separate lines
	 */
	protected boolean separateLine = false;

	/**
	 * Whether you can type into the combo box
	 */
	protected boolean readOnly = false;

	/**
	 * Indent of controls
	 */
	protected int indent = 0;

	/**
	 * Width of combo box.
	 */
	protected int width = 0;

	/**
	 * The combo box control.
	 */
	protected Combo combo = null;

	/*
	 * static final types for determining how preferences are saved.
	 */
	/** STRING_TYPE saves the actual string. */
	public static final int STRING_TYPE = 0;

	/** INT_TYPE saves the index. */
	public static final int INT_TYPE = 1;

	/** INT_TYPE_INDEXED saves an associated value for the index value */
	public static final int INT_TYPE_INDEXED = 2;

	/**
	 * The type that was chosen for saving preferences
	 */
	protected int type;

	/**
	 * Index of list contains value of preference to store
	 */
	protected ArrayList indexToValue = new ArrayList();

	/**
	 * True for automatically loading and storing and false for manual loading
	 * and storing of values in the combo box.
	 */
	public boolean autoStorage = true;

	/**
	 * Constructor to create the combo field editor
	 * 
	 * @param name
	 *            String containing unique name of field editor
	 * @param labelText
	 *            String containing text to display
	 * @param parent
	 *            Composite the parent composite that contains this field editor
	 */
	public ComboFieldEditor(String name, String labelText, Composite parent) {
		this(name, labelText, parent, 0, false, STRING_TYPE, 0, false);
	}

	/**
	 * Constructor to create the combo field editor
	 * 
	 * @param name
	 *            String containing unique name of field editor
	 * @param labelText
	 *            String containing text to display
	 * @param parent
	 *            Composite the parent composite that contains this field editor
	 * @param aType
	 *            int, either INT_TYPE or STRING_TYPE which describes the way
	 *            that we will be saving preferences. INT_TYPE will save
	 *            according to the index and STRING_TYPE will save the actual
	 *            string.
	 * @param aSeparateLine
	 *            boolean. Set to true to make labelText and the combo appear on
	 *            separate lines. Set to false to make labelText appear to the
	 *            left of the combo box.
	 * @param anIndent
	 *            How much to indent on the left side.
	 * @param aWidth
	 *            is the width of the combo.
	 * @param aReadOnly
	 *            true to be read only, false to be read write
	 */
	public ComboFieldEditor(String name, String labelText, Composite parent,
			int aType, boolean aSeparateLine, int anIndent, int aWidth,
			boolean aReadOnly) {
		this.separateLine = aSeparateLine;
		this.indent = anIndent;
		this.width = aWidth;
		this.readOnly = aReadOnly;
		this.type = aType;
		init(name, labelText);
		createControl(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns(int numColumns) {
		/* empty method body */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite,
	 *      int)
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		GridData labelGridData = new GridData();

		getLabelControl(parent).setLayoutData(labelGridData);
		labelGridData.horizontalIndent = indent;

		getLabelControl(parent);

		GridData gd = new GridData();
		//We don't need to decrease the number of columns when
		//it's going on a separate line.
		if (separateLine) {
			gd.horizontalSpan = numColumns;
		} else {
			gd.horizontalSpan = numColumns - 1;
		}

		//Width is affected only if it was specified and
		//not zero
		if (width == 0) {
			gd.horizontalAlignment = GridData.FILL;
			gd.grabExcessHorizontalSpace = true;
		} else {
			gd.widthHint = width;
		}

		//we must indent only if it's a separate line
		if (separateLine) {
			gd.horizontalIndent = indent;
		}

		int flags = SWT.CLIP_CHILDREN | SWT.CLIP_SIBLINGS | SWT.DROP_DOWN;
		if (readOnly) {
			flags = flags | SWT.READ_ONLY;
		}
		combo = new Combo(parent, flags);
		combo.setLayoutData(gd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	protected void doLoad() {
		if (!autoStorage)
			return;
		if (combo != null) {
			if (type == STRING_TYPE) {
				combo.setText(getPreferenceStore().getString(
					getPreferenceName()));
			} else if (type == INT_TYPE) {
				combo.select(getPreferenceStore().getInt(getPreferenceName()));
			} else if (type == INT_TYPE_INDEXED) {
				int storeValue = getPreferenceStore().getInt(
					getPreferenceName());
				int index = indexToValue.indexOf(Integer.valueOf(storeValue));
				combo.select(index);
			}
			//else, we have a problem because the
			//type is not recognized and assert didn't catch it!
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault() {
		if (!autoStorage)
			return;
		if (combo != null) {
			if (type == STRING_TYPE) {
				combo.setText(getPreferenceStore().getDefaultString(
					getPreferenceName()));
			} else if (type == INT_TYPE) {
				combo.select(getPreferenceStore().getDefaultInt(
					getPreferenceName()));
			} else if (type == INT_TYPE_INDEXED) {
				int storeValue = getPreferenceStore().getDefaultInt(
					getPreferenceName());
				int index = indexToValue.indexOf(Integer.valueOf(storeValue));
				combo.select(index);
			}
			//else, we have a problem because the
			//type is not recognized
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore() {
		if (!autoStorage)
			return;

		//otherwise, make sure either save as string or int
		if (type == STRING_TYPE) {
			getPreferenceStore().setValue(getPreferenceName(), combo.getText());
		} else if (type == INT_TYPE) {
			getPreferenceStore().setValue(getPreferenceName(),
				combo.getSelectionIndex());
		} else if (type == INT_TYPE_INDEXED) {
			//get the value we want to save, for the selected
			//index
			Integer storeValue = (Integer) indexToValue.get(combo
				.getSelectionIndex());
			getPreferenceStore().setValue(getPreferenceName(),
				storeValue.intValue());
		}
	}

	/**
	 * Returns this field editor's current value.
	 * 
	 * @return the value of the combo box as a String
	 */
	public String getStringValue() {
		return combo.getText();
	}

	/**
	 * Returns the number of controls in this editor
	 * 
	 * @return int 2 since there's the combo and the label
	 */
	public int getNumberOfControls() {
		return 2;
	}

	/**
	 * Returns the actual combo box contained in the field editor.
	 * 
	 * @return combo which is the actual combo box.
	 */
	public Combo getComboControl() {
		return combo;
	}

	/**
	 * Returns whether or not the given string is already in the combo box. You
	 * could call this method to check to see if the string is already there
	 * before inserting if you do not want duplicates.
	 * 
	 * @param value
	 *            the String to check
	 * @return boolean true if it exists, false if it doesn't
	 */
	public boolean existsInCombo(String value) {
		//make sure it's not already in there.
		for (int i = 0; i < combo.getItemCount(); i++) {
			//get out of function since not going to
			//add something that's already there
			if (combo.getItem(i).compareToIgnoreCase(value) == 0)
				return true;
		}
		return false;
	}

	/**
	 * Sets the automatic loading and storing of contents for the combo box.
	 * Default is true.
	 * 
	 * @param anAutoStorage
	 *            true for automatically loading and storing and false for
	 *            manual loading and storing
	 */
	public void setAutoStorage(boolean anAutoStorage) {
		this.autoStorage = anAutoStorage;
	}

	/**
	 * Adds an item to the combo. Also stores the value to be saved to the
	 * preference store when the index is the selection. (As opposed to saving
	 * the index value.)
	 * 
	 * @param stringValue
	 *            String value of the item
	 * @param storeValue
	 *            int value of the item
	 */
	public void addIndexedItemToCombo(String stringValue, int storeValue) {
		assert type == INT_TYPE_INDEXED : "type is not equal to INT_TYPE_INDEXED"; //$NON-NLS-1$
		
		combo.add(stringValue);
		indexToValue.add(Integer.valueOf(storeValue));

	}

}