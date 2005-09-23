/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.ObjectAdapter;
import org.eclipse.gmf.runtime.common.ui.dialogs.PopupDialog;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.ElementTypeLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu;
import org.eclipse.gmf.runtime.emf.ui.dialogs.AbstractSelectElementDialog;
import org.eclipse.gmf.runtime.emf.ui.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * <p>
 * A command that pops up a menu which can allow the user to select whether they
 * want to create a new type, select an existing element, or leave it
 * unspecified.
 * </p>
 * 
 * <p>
 * The content can be customized, one or more of the above options are
 * permitted. The constants <code>UNSPECIFIED</code> and
 * <code>SELECT_EXISTING</code> can be used as the content of a menu item.
 * </p>
 * 
 * <p>
 * The displayed strings can be customized with a custom label provider.
 * </p>
 * 
 * <p>
 * The options are:
 * <li>Unspecified</li>
 * <li>Select Existing Element</li>
 * <li>Create New Type A</li>
 * <li>Create New Type B</li>
 * 
 * <p>
 * If a "Select Existing" menu item is chosen, an additional dialog appears
 * allowing the user to choose an element.
 * 
 * <p>
 * The <code>getResultAdapter()</code> method returns an adaptable to the
 * result.
 * </p>
 * 
 * @author cmahoney
 */
public class CreateOrSelectElementCommand
	extends PopupMenuCommand {

	/**
	 * Add this to the content list of the popup menu to add an 'unspecified'
	 * option.
	 */
	public static final String UNSPECIFIED = ResourceManager
		.getI18NString("CreateOrSelectElementCommand.PopupMenu.UnspecifiedMenuItem.Text"); //$NON-NLS-1$

	/**
	 * Add this to the content list of the popup menu to add a 'select existing'
	 * option.
	 */
	public static final String SELECT_EXISTING = ResourceManager
		.getI18NString("CreateOrSelectElementCommand.PopupMenu.SelectExistingElementMenuItem.Text"); //$NON-NLS-1$

	/**
	 * Add this to the content list of the popup menu to add a 'create without binding'
	 * option.
	 */
	public static final String CREATE_WITHOUT_BINDING = ResourceManager
		.getI18NString("CreateOrSelectElementCommand.PopupMenu.CreateWithoutBindingMenuItem.Text"); //$NON-NLS-1$

	/**
	 * The default label provider for the the menu items used in this command.
	 * Adds the "Create new " text to the objects of type
	 * <code>IElementType</code>.
	 */
	static public class LabelProvider
		extends ElementTypeLabelProvider {

		/**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object object) {
			String text = super.getText(object);
			if (object instanceof IElementType) {
				return MessageFormat
					.format(
						ResourceManager
							.getI18NString("CreateOrSelectElementCommand.PopupMenu.CreateMenuItem.Text"), //$NON-NLS-1$
						new Object[] {text});
			}
			return text;
		}
	}

	/**
	 * The result to be returned from which the new element or type info can be
	 * retrieved.
	 */
	private ObjectAdapter resultAdapter = new ObjectAdapter();

	/** The dialog to be used if "Select Existing Element" is chosen */
	private AbstractSelectElementDialog selectElementDialog;

	/**
	 * Creates a new <code>CreateOrSelectElementCommand</code> that uses a
	 * popup menu to prompt for the selection.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param content
	 *            The list of items making up the content of the popup menu.
	 *            This can include element types (<code>IElementType</code>),
	 *            <code>UNSPECIFIED</code>, and <code>SELECT_EXISTING</code>.
	 */
	public CreateOrSelectElementCommand(Shell parentShell, List content) {
		this(parentShell, content, POPUP_MENU);
	}

	/**
	 * Creates a new <code>CreateOrSelectElementCommand</code>.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param content
	 *            The list of items making up the content of the popup menu.
	 *            This can include element types (<code>IElementType</code>),
	 *            <code>UNSPECIFIED</code>, and <code>SELECT_EXISTING</code>.
	 * @param style
	 *            The kind of prompt to use for the selection. One of
	 *            {@link org.eclipse.gmf.runtime.diagram.ui.commands.PopupMenuCommand#POPUP_MENU}
	 *            or
	 *            {@link org.eclipse.gmf.runtime.diagram.ui.commands.PopupMenuCommand#POPUP_DIALOG}
	 */
	public CreateOrSelectElementCommand(Shell parentShell, List content,
			int style) {
		this(ResourceManager
			.getI18NString("CreateOrSelectElementCommand.Label"), //$NON-NLS-1$
			parentShell, content, style);
	}

	/**
	 * Creates a new <code>CreateOrSelectElementCommand</code> that uses a
	 * popup menu to prompt for the selection.
	 * 
	 * @param label
	 *            the command label
	 * @param parentShell
	 *            the parent shell
	 * @param content
	 *            The list of items making up the content of the popup menu.
	 *            This can include element types (<code>IElementType</code>),
	 *            <code>UNSPECIFIED</code>, and <code>SELECT_EXISTING</code>.
	 */
	public CreateOrSelectElementCommand(String label, Shell parentShell,
			List content) {
		this(label, parentShell, content, POPUP_MENU);
	}

	/**
	 * Creates a new <code>CreateOrSelectElementCommand</code>.
	 * 
	 * @param label
	 *            the command label
	 * @param parentShell
	 *            the parent shell
	 * @param content
	 *            The list of items making up the content of the popup menu.
	 *            This can include element types (<code>IElementType</code>),
	 *            <code>UNSPECIFIED</code>, and <code>SELECT_EXISTING</code>.
	 * @param style
	 *            The kind of prompt to use for the selection. One of
	 *            {@link org.eclipse.gmf.runtime.diagram.ui.commands.PopupMenuCommand#POPUP_MENU}
	 *            or
	 *            {@link org.eclipse.gmf.runtime.diagram.ui.commands.PopupMenuCommand#POPUP_DIALOG}
	 */
	public CreateOrSelectElementCommand(String label, Shell parentShell,
			List content, int style) {
		super(label, parentShell);

		if (style == POPUP_DIALOG) {
			setPopupDialog(new PopupDialog(parentShell, content,
				getLabelProvider()));
		} else {
			setPopupMenu(new PopupMenu(content, getLabelProvider()));
		}
	}

	/**
	 * Creates a new <code>CreateOrSelectElementCommand</code>.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param popupMenu
	 *            the popup menu
	 */
	public CreateOrSelectElementCommand(Shell parentShell, PopupMenu popupMenu) {
		super(ResourceManager
			.getI18NString("CreateOrSelectElementCommand.Label"), //$NON-NLS-1$
			parentShell, popupMenu);
	}

	/**
	 * Pops up the dialog with the content provided. If the user selects 'select
	 * existing', then the select elements dialog also appears.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		CommandResult cmdResult = super.doExecute(progressMonitor);
		if (!cmdResult.getStatus().isOK()) {
			return cmdResult;
		}

		Object result = cmdResult.getReturnValue();
		if (result != null) {
			if (result.equals(SELECT_EXISTING)) {
				AbstractSelectElementDialog dialog = getSelectElementDialog();
				Assert.isNotNull(dialog);

				if (dialog.open() != Window.OK) {
					// user cancelled gesture
					return newCancelledCommandResult();
				}
				List selectedElements = dialog.getSelectedElements();
				if (selectedElements == null) {
					// user cancelled gesture
					progressMonitor.setCanceled(true);
					return newCancelledCommandResult();
				} else if (dialog.isMultiSelectable()) {
					resultAdapter.setObject(selectedElements);
					return newOKCommandResult(selectedElements);
				} else {
					resultAdapter.setObject(selectedElements.get(0));
					return newOKCommandResult(selectedElements.get(0));
				}
			} else {
				resultAdapter.setObject(result);
			}
		}
		return cmdResult;
	}

	/**
	 * Gets the selectElementDialog.
	 * 
	 * @return Returns the selectElementDialog.
	 */
	protected AbstractSelectElementDialog getSelectElementDialog() {
		return selectElementDialog;
	}

	/**
	 * Sets the selectElementDialog.
	 * 
	 * @param dialog
	 *            The dialog to set.
	 */
	public void setSelectElementDialog(AbstractSelectElementDialog dialog) {
		this.selectElementDialog = dialog;
	}

	/**
	 * Gets the resultAdapter.
	 * 
	 * @return Returns the resultAdapter.
	 */
	public ObjectAdapter getResultAdapter() {
		return resultAdapter;
	}

	/**
	 * Gets the label provider that is to be used to display each item in the popup menu.
	 * 
	 * @return the label provider
	 */
	protected ILabelProvider getLabelProvider() {
		return new LabelProvider();
	}
}