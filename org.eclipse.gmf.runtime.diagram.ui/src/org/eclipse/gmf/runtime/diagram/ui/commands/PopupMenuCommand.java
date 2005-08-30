/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              	   |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.ui.dialogs.PopupDialog;
import org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu;

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

		super(label);
		setParentShell(parentShell);
	}

	/**
	 * Creates a new <code>PopupMenuCommand</code>.
	 * @param label the command label
	 * @param parentShell the parent shell
	 * @param popupMenu the popup menu
	 */
	public PopupMenuCommand(String label, Shell parentShell, PopupMenu popupMenu) {

		super(label);
		setParentShell(parentShell);
		setPopupMenu(popupMenu);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		if (getPopupMenu() != null) {
			if (getPopupMenu().show(getParentShell()) == false) {
				// user cancelled gesture
				progressMonitor.setCanceled(true);
				return newCancelledCommandResult();
			}
			return newOKCommandResult(getPopupMenu().getResult());
			
		} else if (getPopupDialog() != null) {
			if (getPopupDialog().open() == Dialog.CANCEL
				|| getPopupDialog().getResult() == null
				|| getPopupDialog().getResult().length <= 0) {
				
				// user cancelled dialog
				progressMonitor.setCanceled(true);
				return newCancelledCommandResult();
			}
			return newOKCommandResult(getPopupDialog().getResult()[0]);
		}
		
		return newOKCommandResult();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doRedo()
	 */
	protected CommandResult doRedo() {
		return newOKCommandResult();

	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doUndo()
	 */
	protected CommandResult doUndo() {
		return newOKCommandResult();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isRedoable()
	 */
	public boolean isRedoable() {
		return true;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isUndoable()
	 */
	public boolean isUndoable() {
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
