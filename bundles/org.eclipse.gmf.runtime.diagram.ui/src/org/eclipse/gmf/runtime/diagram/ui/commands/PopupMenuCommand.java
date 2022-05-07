/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.ui.dialogs.PopupDialog;
import org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

/**
 * A command that pops up a <code>PopupMenu</code> or a
 * <code>PopupDialog</code> when it executes. The result from the
 * <code>PopupMenu</code> or <code>PopupDialog</code> is retrieved via
 * <code>getCommandResult().getReturnValue()</code>.
 * 
 * @author cmahoney
 */
public class PopupMenuCommand
	extends AbstractCommand {
	/**
	 * The popup menu style for this command.
	 */
	public static final int POPUP_MENU = 1;
	
	/**
	 * The popup dialog style for this command.
	 */
	public static final int POPUP_DIALOG = 2;

	/** The parent shell for this menu. */
	private Shell parentShell;

	/**
	 * The popup menu to appear when this command is executed if the popup menu
	 * is not <code>null</code>.
	 */
	private PopupMenu popupMenu;
	
	/**
	 * The dialog to appear when this command is executed, if the dialog
	 * is not <code>null</code>.
	 */
	private PopupDialog popupDialog;

	/**
	 * Creates a new <code>PopupMenuCommand</code>.
	 * @param label the command label
	 * @param parentShell the parent shell
	 */
	public PopupMenuCommand(String label, Shell parentShell) {

		super(label, null);
		setParentShell(parentShell);
	}

	/**
	 * Creates a new <code>PopupMenuCommand</code>.
	 * @param label the command label
	 * @param parentShell the parent shell
	 * @param popupMenu the popup menu
	 */
	public PopupMenuCommand(String label, Shell parentShell, PopupMenu popupMenu) {

		super(label, null);
		setParentShell(parentShell);
		setPopupMenu(popupMenu);
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		if (getPopupMenu() != null) {
			if (getPopupMenu().show(getParentShell()) == false) {
				// user cancelled gesture
				progressMonitor.setCanceled(true);
				return CommandResult.newCancelledCommandResult();
			}
			return CommandResult.newOKCommandResult(getPopupMenu().getResult());
			
		} else if (getPopupDialog() != null) {
			if (getPopupDialog().open() == Dialog.CANCEL
				|| getPopupDialog().getResult() == null
				|| getPopupDialog().getResult().length <= 0) {
				
				// user cancelled dialog
				progressMonitor.setCanceled(true);
				return CommandResult.newCancelledCommandResult();
			}
			return CommandResult.newOKCommandResult(getPopupDialog().getResult()[0]);
		}
		
		return CommandResult.newOKCommandResult();
	}

    
    protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		return CommandResult.newOKCommandResult();

	}

    
    protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		return CommandResult.newOKCommandResult();
	}

    public boolean canRedo() {
		return true;
	}
    
    public boolean canUndo() {
		return true;
	}

	/**
	 * Gets the parentShell.
	 * @return Returns the parentShell.
	 */
	protected Shell getParentShell() {
		return parentShell;
	}

	/**
	 * Sets the parentShell.
	 * @param parentShell The parentShell to set.
	 */
	public void setParentShell(Shell parentShell) {
		this.parentShell = parentShell;
	}

	/**
	 * Gets the popupMenu.
	 * @return Returns the popupMenu.
	 */
	protected PopupMenu getPopupMenu() {
		return popupMenu;
	}

	/**
	 * Sets the popupMenu. Sets the popup dialog to null.
	 * @param popupMenu The popupMenu to set.
	 */
	public void setPopupMenu(PopupMenu popupMenu) {
		this.popupMenu = popupMenu;
		this.popupDialog = null;
	}
	
	/**
	 * Gets the popupDialog.
	 * @return Returns the popupDialog.
	 */
	protected PopupDialog getPopupDialog() {
		return popupDialog;
	}

	/**
	 * Sets the popupDialog. Sets the popup menu to null.
	 * @param popupDialog The popupDialog to set.
	 */
	public void setPopupDialog(PopupDialog popupDialog) {
		this.popupDialog = popupDialog;
		this.popupMenu = null;
	}

}
