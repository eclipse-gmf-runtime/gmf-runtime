/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.resources;

import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileModificationValidator;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.team.core.RepositoryProvider;

import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.ResourceManager;

/**
 * A wrapper around the Eclipse IFileModificationValidator.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public class FileModificationValidator {

	/**
	 * IStatus is fileIsReadOnly
	 */
	private static String FILE_IS_READ_ONLY = ResourceManager.getInstance()
		.getString("FileModificationValidator.FileIsReadOnlyErrorMessage"); //$NON-NLS-1$

	/**
	 * IStatus is ok
	 */
	private static String OK = ResourceManager.getInstance().getString(
		"FileModificationValidator.OK"); //$NON-NLS-1$

	/**
	 * title for edit problems dialog
	 */
	private String editProblemDialogTitle = ResourceManager.getInstance()
		.getString("FileModificationValidator.EditProblemDialogTitle"); //$NON-NLS-1$

	/**
	 * part 1 of message for edit problems dialog
	 */
	// Fragmenting the following message so as to not use hard return characters
	// ("\n") in translatable strings
	private String editProblemDialogMessage_part1 = ResourceManager
		.getInstance().getString(
			"FileModificationValidator.EditProblemDialogMessage.part1"); //$NON-NLS-1$

	/**
	 * part 2 of message for edit problems dialog
	 */
	private String editProblemDialogMessage_part2 = ResourceManager
		.getInstance().getString(
			"FileModificationValidator.EditProblemDialogMessage.part2"); //$NON-NLS-1$

	/**
	 * part 3 of message for edit problems dialog
	 */
	private String editProblemDialogMessage_part3 = ResourceManager
		.getInstance().getString(
			"FileModificationValidator.EditProblemDialogMessage.part3"); //$NON-NLS-1$

	/**
	 * title for save problems dialog
	 */
	private String saveProblemDialogTitle = ResourceManager.getInstance()
		.getString("FileModificationValidator.SaveProblemDialogTitle"); //$NON-NLS-1$

	/**
	 * part 1 of message for save problems dialog
	 */
	// Fragmenting the following message so as to not use hard return characters
	// ("\n") in translatable strings
	private String saveProblemDialogMessage_part1 = ResourceManager
		.getInstance().getString(
			"FileModificationValidator.SaveProblemDialogMessage.part1"); //$NON-NLS-1$

	/**
	 * part 2 of message for save problems dialog
	 */
	private String saveProblemDialogMessage_part2 = ResourceManager
		.getInstance().getString(
			"FileModificationValidator.SaveProblemDialogMessage.part2"); //$NON-NLS-1$

	/**
	 * part 3 of message for save problems dialog
	 */
	private String saveProblemDialogMessage_part3 = ResourceManager
		.getInstance().getString(
			"FileModificationValidator.SaveProblemDialogMessage.part3"); //$NON-NLS-1$

	/**
	 * singleton instance of this class
	 */
	private static FileModificationValidator INSTANCE = new FileModificationValidator();

	/**
	 * get the singleton instance of this class
	 * @return singleton instance of the FileModificationValidator class
	 */
	public static FileModificationValidator getInstance() {
		return INSTANCE;
	}

	/**
	 * Private constructor for the singleton instance of this class.
	 */
	private FileModificationValidator() {
		super();
	}

	/**
	 * Validates that the given files can be modified using the Team
	 * validateEdit support.
	 * 
	 * @param files
	 *            files that are to be modified; these files must all exist in
	 *            the workspace.
	 * @param modificationReason
	 *            a String describing the reason for modifying the file, usually
	 *            the command text.
	 * @return true if it is OK to edit the files.
	 * @see org.eclipse.core.resources.IFileModificationValidator#validateEdit
	 */
	public boolean okToEdit(IFile[] files, String modificationReason) {
		IStatus status = ResourcesPlugin.getWorkspace().validateEdit(files,
			Display.getCurrent().getActiveShell());
		if (status.isOK()) {
			return true;
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
				MessageFormat.format(editProblemDialogTitle,
					new Object[] {modificationReason}), MessageFormat.format(
					editProblemDialogMessage_part1,
					new Object[] {modificationReason})
					+ "\n\n" //$NON-NLS-1$
					+ editProblemDialogMessage_part2
					+ "\n" //$NON-NLS-1$
					+ MessageFormat.format(editProblemDialogMessage_part3,
						new Object[] {status.getMessage()}));
			return false;
		}
	}

	/**
	 * Validates that the given file can be saved using the Team validateSave
	 * support.
	 * 
	 * @param file
	 *            the file that is to be saved; this file must exist in the
	 *            workspace.
	 * @return true if it is OK to save the file.
	 * @see org.eclipse.core.resources.IFileModificationValidator#validateSave
	 */
	public boolean okToSave(IFile file) {
		IStatus status = null;
		RepositoryProvider provider = RepositoryProvider.getProvider(file
			.getProject());

		IFileModificationValidator validator = null;

		//if no provider or no validator use the default validator
		if (provider != null) {
			validator = provider.getFileModificationValidator();
		}

		if (validator == null) {
			status = getDefaultStatus(file);
		} else {
			status = validator.validateSave(file);
		}

		if (status.isOK()) {
			return true;
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
				saveProblemDialogTitle, saveProblemDialogMessage_part1
					+ "\n\n" //$NON-NLS-1$
					+ saveProblemDialogMessage_part2
					+ "\n" //$NON-NLS-1$
					+ MessageFormat.format(saveProblemDialogMessage_part3,
						new Object[] {status.getMessage()}));
			return false;
		}
	}

	/**
	 * return the default status for the file.
	 * 
	 * @param file
	 *            the <code>IFile</code> that is to be validated.
	 * @return IStatus OK, otherwise ERROR if the file is read-only.
	 */
	private IStatus getDefaultStatus(IFile file) {
		if (file.isReadOnly()) {
			String message = MessageFormat.format(FILE_IS_READ_ONLY,
				new Object[] {file.getFullPath().toString()});
			return new Status(Status.ERROR, CommonUIPlugin.getPluginId(),
				Status.ERROR, message, null);
		} else {
			return new Status(Status.OK, CommonUIPlugin.getPluginId(),
				Status.OK, OK, null);
		}
	}
}