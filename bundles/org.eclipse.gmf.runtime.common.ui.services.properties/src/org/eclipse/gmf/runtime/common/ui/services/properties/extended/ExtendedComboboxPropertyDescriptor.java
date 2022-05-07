/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Tauseef A. Israr
 * Created on: Sep 9, 2002
 */
public class ExtendedComboboxPropertyDescriptor
	extends ExtendedPropertyDescriptor {

	/**
	* The list of possible values to display in the combo box
	*/
	private String[] values;

	/** The combo box item which maps to an empty string in the text box */
	final private String emptyItem;

	/**
	 * Whether the whether the text field in the combobox is editable
	 */
	private final boolean isTextEditable;

	/**
	 * Convenience constructor for ExtendedComboboxPropertyDescriptor
	 * 
	 * @param id the id of the property
	 * @param displayName the name to display for the property
	 * @param valuesArray the list of possible values to display in the combo box
	 */
	public ExtendedComboboxPropertyDescriptor(
		Object id,
		String displayName,
		String[] valuesArray) {

		this(id, displayName, valuesArray, null, false);
	}

	/**
	 * Creates an property descriptor with the given id, display name, and list
	 * of value labels to display in the combo box cell editor.
	 * 
	 * @param id the id of the property
	 * @param displayName the name to display for the property
	 * @param valuesArray the list of possible values to display in the combo box
	 * @param emptyItem the combo box item which maps to an empty string in the text box
	 * @param isTextEditable whether the text field in the combobox is editable
	 */
	public ExtendedComboboxPropertyDescriptor(
		Object id,
		String displayName,
		String[] valuesArray,
		String emptyItem,
		boolean isTextEditable) {

		super(id, displayName);
		values = valuesArray;
		this.emptyItem = emptyItem;
		this.isTextEditable = isTextEditable;
	}

	/**
	 * The <code>ComboBoxPropertyDescriptor</code> implementation of this 
	 * <code>IPropertyDescriptor</code> method creates and returns a new
	 * <code>ComboBoxCellEditor</code>.
	 * <p>
	 * The editor is configured with the current validator if there is one.
	 * </p>
	 */
	public CellEditor createPropertyEditor(Composite parent) {
		if (isReadOnly())
			return null;

		int style = isTextEditable ? SWT.NONE : SWT.READ_ONLY;

		CellEditor editor =
			new ExtendedComboBoxCellEditor(parent, values, emptyItem, style);

		if (isReadOnly()) {
			Control control = editor.getControl();
			control.setEnabled(false);
		}
		editor.setValidator(getValidator());
		return editor;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertyDescriptor#getLabelProvider()
	 */
	public ILabelProvider getLabelProvider() {
		return new LabelProvider() {
			public String getText(Object object) {
				if (object instanceof Integer) {
					int index = ((Integer) object).intValue();
					if (0 <= index && index < values.length)
						return values[index];
				}
				return object.toString();

			}
		};
	}

	//	/**
	//	 * @see org.eclipse.ui.views.properties.IPropertyDescriptor#isCompatibleWith(IPropertyDescriptor)
	//	 */
	//	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
	//		assert null != anotherProperty;
	//
	//		IExtendedPropertyDescriptor xdePropertyDescriptor =
	//			(IExtendedPropertyDescriptor) anotherProperty;
	//		if (anotherProperty.getId().equals(this.getId())) {
	//
	//			Object value1 = getPropertySource().getPropertyValue(getId());
	//			Object value2 =
	//				xdePropertyDescriptor.getPropertySource().getPropertyValue(
	//					getId());
	//
	//			if (!value1.equals(value2)) {
	//				setDirtyFlag(true);
	//				
	//			}
	//			return true;
	//		}
	//		return false;
	//	}

}
