/******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter;

import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter.SortFilterPage;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Sort/Filter dialog.  The OK button applies all changes from each 
 * page starting with the ROOT page. 
 * 
 * @author jcorchis
 */
public class SortFilterDialog extends PreferenceDialog {
	
	/** dialog title prefix */
	private final String title = DiagramUIMessages.SortFilterDialog_title;
	private CommandStack commandStack = null;
	
	/**
	 * CollectionEditorDialog constructor
	 * @param parentShell 	the parent shell
	 */
	public SortFilterDialog(Shell parentShell, CommandStack commandStack) {
		super(parentShell, new SortFilterPageManager());
		this.commandStack = commandStack;
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferenceDialog#okPressed()
	 */
	protected void okPressed() {
		SafeRunnable.run(new SafeRunnable() {
			private boolean errorOccurred;

			
			 /* (non-Javadoc)
			 * 
			 * @see org.eclipse.core.runtime.ISafeRunnable#run()
			 */
			 
			public void run() {
				getButton(IDialogConstants.OK_ID).setEnabled(false);
				errorOccurred = false;
				boolean hasFailedOK = false;
				try {
					// Notify all the pages and give them a chance to abort
					Iterator nodes = getPreferenceManager().getElements(PreferenceManager.PRE_ORDER)
							.iterator();
					CompoundCommand cc = new CompoundCommand();
					while (nodes.hasNext()) {
						IPreferenceNode node = (IPreferenceNode) nodes.next();
						IPreferencePage page = node.getPage();
						if (page != null) {
							if (page instanceof SortFilterPage) {
								Command cmd = ((SortFilterPage)page).getCommand();
								if (cmd != null && cmd.canExecute())
									cc.add(cmd);
							}
							else if (!page.performOk()){
								hasFailedOK = true;
								return;
							}
						}
					}
					if (cc.canExecute())
						commandStack.execute(cc);
				} catch (Exception e) {
					handleException(e);
				} finally {
					//Don't bother closing if the OK failed
					if(hasFailedOK){
						setReturnCode(FAILED);
						getButton(IDialogConstants.OK_ID).setEnabled(true);
						return;
					}
					
					if (!errorOccurred) {
						//Give subclasses the choice to save the state of the
					    //preference pages.
						handleSave();
					}
					setReturnCode(OK);
					close();
				}
			}

			
			 /* (non-Javadoc)
			 * 
			 * @see org.eclipse.core.runtime.ISafeRunnable#handleException(java.lang.Throwable)
			 */
			 
			public void handleException(Throwable e) {
				errorOccurred = true;
				
				Policy.getLog().log(new Status(IStatus.ERROR, Policy.JFACE, 0, e.toString(), e));

				setSelectedNodePreference(null);
				String message = JFaceResources.getString("SafeRunnable.errorMessage"); //$NON-NLS-1$
				MessageDialog.openError(getShell(), JFaceResources.getString("Error"), message); //$NON-NLS-1$

			}
		});
	}

}
