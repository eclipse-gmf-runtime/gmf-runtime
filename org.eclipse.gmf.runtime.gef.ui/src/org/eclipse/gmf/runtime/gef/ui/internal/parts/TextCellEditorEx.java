/******************************************************************************
 * Copyright (c) 2002, 2004, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Svyatoslav Kovalsky (Montages) - contribution for bugzilla 371888
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.parts;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Extends the TextCellEditor to provide a convenient method
 * that will permitting the set of the value and doing the necessary
 * process to update the state of the cell editor and also notify
 * all listeners listening on changes in the cell editor value.
 */
public class TextCellEditorEx extends TextCellEditor implements CellEditorEx {

	private CellEditorExDelegate myExDelegate;

	public TextCellEditorEx() {
		// empty
	}

	/**
	 * @param parent
	 *            the parent control
	 */
	public TextCellEditorEx(Composite parent) {
		super(parent);
	}

	/**
	 * Creates a new text string cell editor parented under the given control.
	 * The cell editor value is the string itself, which is initially the empty
	 * string. Initially, the cell editor has no cell validator.
	 * 
	 * @param parent
	 *            the parent control
	 * @param style
	 *            the style bits
	 */
	public TextCellEditorEx(Composite parent, int style) {
		super(parent, style);
	}

	private CellEditorExDelegate getExDelegate() {
		if (myExDelegate == null) {
			myExDelegate = new CellEditorExDelegate(this);
		}
		return myExDelegate;
	}

	/**
	 * This will be used when an edit has occurred by a ModifyEvent has been
	 * been send. Will call #setValue(Object) but will also call
	 * editOccured(null) to make sure that the dirty flag is set probably and
	 * that any listeners are informed about the changed.
	 * 
	 * @param value
	 *            Value to set the cell editor to.
	 * 
	 *            Note: This happens address defect RATLC00522324. For our
	 *            topgraphical edit parts we delagate the direct edit request to
	 *            a primary edit part and set focus on that. The issue is that
	 *            if the user has typed in an initial character when setting
	 *            focus to the edit part, which typically is a
	 *            TextCompartmentEditPart then setting that intial value does
	 *            not fire the necessary change events that need to occur in
	 *            order for that value to be recongnized. If you don't use this
	 *            method then the result is that if you just type in the initial
	 *            character and that is it then the text compartment loses focus
	 *            then the value will not be saved. This is because setting the
	 *            value of the cell doesn't think its value has changed since
	 *            the first character is not recongized as a change.
	 */
	public void setValueAndProcessEditOccured(Object value) {
		setValue(value);
		// do the processing to ensure if we exit the cell then
		// value will be applied.
		editOccured(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	protected void doSetValue(Object value) {
		getExDelegate().setOriginalValue(value);
		super.doSetValue(value);
	}

	/**
	 * @return boolean value specifying whether or not the value has been
	 *         changed
	 */
	public boolean hasValueChanged() {
		return getExDelegate().hasValueChanged();
	}

	/*
	 * Runs super deactivate unless it has been locked and otherwise unlocks
	 * deactivation
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#deactivate()
	 */
	public void deactivate() {
		if (!getExDelegate().unlockDeactivation()) {
			super.deactivate();
		}
	}

	/**
	 * Returns true if deactivation has been locked
	 * @return
	 */
	public boolean isDeactivationLocked() {
		return getExDelegate().isDeactivationLocked();
	}

	/**
	 * Sets deactivation lock so that the cell editor does not perform
	 * deactivate
	 * 
	 * @param deactivationLock
	 */
	public void setDeactivationLock(boolean deactivationLock) {
		getExDelegate().setDeactivationLock(deactivationLock);
	}
}
