/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Sublcasses BooleanFieldEditor to expose the checkbox control. The checkbox
 * control must be used to set the context sensitive help.
 * 
 * @author wdiu, Wayne Diu
 */
public class CheckBoxFieldEditor
	extends BooleanFieldEditor {

	/**
	 * The parent that will contain the checkbox field editor
	 */
	private Composite parent;

	/**
	 * Creates a boolean field editor in the default style.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param label
	 *            the label text string of the field editor
	 * @param aParent
	 *            the parent of the field editor's control
	 */
	public CheckBoxFieldEditor(String name, String label, Composite aParent) {
		super(name, label, DEFAULT, aParent);
		this.parent = aParent;
	}

	/**
	 * Returns the checkbox control.
	 * 
	 * @return Button, the checkbox control
	 */
	public Button getCheckbox() {
		return getChangeControl(parent);
	}

	/**
	 * Get the parent composite.
	 * 
	 * @return the parent composite.
	 */
	public Composite getParent() {
		return parent;
	}
}