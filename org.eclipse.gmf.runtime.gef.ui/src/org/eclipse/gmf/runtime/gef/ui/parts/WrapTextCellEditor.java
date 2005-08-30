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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * A cell editor that manages a multiline wrappable text entry field.
 * The cell editor's value is the text string itself.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * 
 * @canBeSeenBy %partners
 */
public class WrapTextCellEditor extends TextCellEditorEx {

	/**
	 * Default MultiLineTextCellEditor style
	 * specify no borders on text widget as cell outline in table already
	 * provides the look of a border.
	 */
	private static final int defaultStyle = SWT.WRAP | SWT.MULTI;

	/**
	 * Creates a new text string cell editor with no control
	 * The cell editor value is the string itself, which is initially the empty
	 * string. Initially, the cell editor has no cell validator.
	 */
	public WrapTextCellEditor() {
		setStyle(defaultStyle);
	}
	/**
	 * Creates a new text string cell editor parented under the given control.
	 * The cell editor value is the string itself, which is initially the empty string. 
	 * Initially, the cell editor has no cell validator.
	 *
	 * @param parent the parent control
	 */
	public WrapTextCellEditor(Composite parent) {
		this(parent, defaultStyle);
	}
	/**
	 * Creates a new text string cell editor parented under the given control.
	 * The cell editor value is the string itself, which is initially the empty string. 
	 * Initially, the cell editor has no cell validator.
	 *
	 * @param parent the parent control
	 * @param style the style bits
	 */
	public WrapTextCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt.events.KeyEvent)
	 */
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		// The super behavior of this method is to apply the cell editor value
		// if the 'Return' key is pressed with the 'CTRL' key. Otherwise, the
		// 'Return' key is used to insert a new line. This is exactly opposite
		// to what we expect in this editor and that is why we are reversing it.
		if (keyEvent.character == '\r') {
	        if ((keyEvent.stateMask & SWT.CTRL) != 0)
	        	keyEvent.stateMask &= ~SWT.CTRL; 
	        else
	        	keyEvent.stateMask |= SWT.CTRL;
		}
		super.keyReleaseOccured(keyEvent);
	}
}
