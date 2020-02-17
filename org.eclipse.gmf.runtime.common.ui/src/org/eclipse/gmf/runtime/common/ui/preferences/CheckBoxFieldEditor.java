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