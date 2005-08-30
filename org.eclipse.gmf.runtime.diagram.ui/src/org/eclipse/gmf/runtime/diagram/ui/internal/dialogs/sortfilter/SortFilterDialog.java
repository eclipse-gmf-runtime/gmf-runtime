/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;

/**
 * Sort/Filter dialog.  The OK button applies all changes from each 
 * page starting with the ROOT page. 
 * 
 * @author jcorchis
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class SortFilterDialog extends PreferenceDialog {
	
	/** dialog title prefix */
	private final String title = PresentationResourceManager.getInstance().getString("SortFilterDialog.title");//$NON-NLS-1$
	
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
