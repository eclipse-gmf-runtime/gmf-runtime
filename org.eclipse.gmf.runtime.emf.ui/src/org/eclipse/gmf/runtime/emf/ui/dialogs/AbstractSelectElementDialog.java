/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog which solicits the user to select model elements in an explorer like
 * tree viewer.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:anthonyh@ca.ibm.com">anthonyh@ca.ibm.com </a>
 */
public abstract class AbstractSelectElementDialog
	extends Dialog {

	/**
	 * Constructor for the dialog.
	 * 
	 * @param parentShell The parent shell.
	 */
	public AbstractSelectElementDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Gets the user selected elements.
	 * 
	 * @return List the user selected elements
	 */
	public abstract List getSelectedElements();

	/**
	 * Is this dialog in single or multi-select mode?
	 * 
	 * @return isMultiSelectable
	 */
	public abstract boolean isMultiSelectable();
}
