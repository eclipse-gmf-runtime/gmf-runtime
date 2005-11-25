/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter;

import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Sort/Filter dialog.  The OK button applies all changes from each 
 * page starting with the ROOT page. 
 * 
 * @author jcorchis
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class SortFilterDialog extends PreferenceDialog {
	
	/** dialog title prefix */
	private final String title = DiagramUIMessages.SortFilterDialog_title;
	
	/**
	 * CollectionEditorDialog constructor
	 * @param parentShell 	the parent shell
	 */
	public SortFilterDialog(Shell parentShell) {
		super(parentShell, new SortFilterPageManager());
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);
		getShell().setText(title);
		getTreeViewer().expandAll();
		return control;
	}
	
	/**
	 * Overridden to give access to the root node to show it's
	 * child pages.
	 * @param node the tree preference node
	 * @return boolean
	 */
	public boolean showPage(IPreferenceNode node) {
		return super.showPage(node);
	}
	
	/**
	 * Get the name of the selected item preference.  Overridden to 
	 * always set the preference page to the root node.
	 */
	protected String getSelectedNodePreference() {
		return null;
	}	
	

}
