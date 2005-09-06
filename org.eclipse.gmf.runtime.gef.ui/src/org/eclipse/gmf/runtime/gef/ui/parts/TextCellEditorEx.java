/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.parts;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Extends the TextCellEditor to provide a convenient method
 * that will permitting the set of the value and doing the necessary
 * process to update the state of the cell editor and also notify
 * all lisnteners listening on changes in the cell editor value.
 * 
 * @canBeSeenBy %partners
 */
public class TextCellEditorEx extends TextCellEditor {
	
	private Object originalValue;
	private boolean deactivationLock = false;

	/**
	 */
	public TextCellEditorEx() {
		// empty
	}
	
	/**
	 * @param parent the parent control
	 */
	public TextCellEditorEx(Composite parent) {
		super(parent);
	}
	/**
	 * Creates a new text string cell editor parented under the given control.
	 * The cell editor value is the string itself, which is initially the empty string. 
	 * Initially, the cell editor has no cell validator.
	 *
	 * @param parent the parent control
	 * @param style the style bits
	 */
	public TextCellEditorEx(Composite parent, int style) {
		super(parent, style);
	}
	
	/**
	 * This will be used when an edit has occurred by a ModifyEvent has been been send.
	 * Will call #setValue(Object) but will also call editOccured(null)
	 * to make sure that the dirty flag is set probably and that any listeners
	 * are informed about the changed.
	 * @param value Value to set the cell editor to.
	 * 
	 * Note:  This happens address defect RATLC00522324.  For our topgraphical edit parts
	 * we delagate the direct edit request to a primary edit part and set focus on that.  The issue
	 * is that if the user has typed in an initial character when setting focus
	 * to the edit part, which typically is a TextCompartmentEditPart then
	 * setting that intial value does not fire the necessary change events that
	 * need to occur in order for that value to be recongnized.  If you don't
	 * use this method then the result is that if you just type in the initial character
	 * and that is it then the text compartment loses focus then the value will not
	 * be saved.  This is because setting the value of the cell doesn't think its value
	 * has changed since the first character is not recongized as a change.
	 */
	public void setValueAndProcessEditOccured(Object value){
		setValue(value);
		// do the processing to ensure if we exit the cell then
		// value will be applied.
		editOccured(null); 
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	protected void doSetValue(Object value) {
		if (originalValue == null)
			originalValue = value;
		super.doSetValue(value);
	}
	
	/**
	 * @return boolean value specifying whether or not the value has been changed
	 */
	public boolean hasValueChanged() {
		if (getValue() == null)
			return originalValue != null;
		return !getValue().equals(originalValue);
	}

	/* 
	 * Runs super deactivate unless it has been locked
	 * and otherwise unlocks deactivation
	 * @see org.eclipse.jface.viewers.CellEditor#deactivate()
	 */
	public void deactivate() {
		if (! isDeactivationLocked())
			super.deactivate();
		setDeactivationLock(false);
	}

	/**
	 * Returns true if deactivation has been locked
	 * @return
	 */
	public boolean isDeactivationLocked() {
		return deactivationLock;
	}

	
	/**
	 * Sets deactivation lock so that the cell editor 
	 * does not perform deactivate
	 * @param deactivationLock
	 */
	public void setDeactivationLock(boolean deactivationLock) {
		this.deactivationLock = deactivationLock;
	}

}
