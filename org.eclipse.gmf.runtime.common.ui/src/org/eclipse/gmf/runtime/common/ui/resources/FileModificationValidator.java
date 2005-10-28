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
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.ui.PlatformUI;

/**
 * A wrapper around the Eclipse IFileModificationValidator.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public class FileModificationValidator {

	/**
	 * singleton instance of this class
	 */
	private static FileModificationValidator INSTANCE = new FileModificationValidator();

	/**
	 * get the singleton instance of this class
	 * 
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
	public boolean okToEdit(final IFile[] files, final String modificationReason) {
		final boolean result[] = new boolean[] {false};

		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

			public void run() {
				IStatus status = ResourcesPlugin.getWorkspace().validateEdit(
					files,
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getShell());
				if (status.isOK()) {
					result[0] = true;
				} else {

					MessageDialog
						.openError(
							PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getShell(),
							MessageFormat
								.format(
									CommonUIMessages.FileModificationValidator_EditProblemDialogTitle,
									new Object[] {modificationReason}),
							MessageFormat
								.format(
									CommonUIMessages.FileModificationValidator_EditProblemDialogMessage_part1,
									new Object[] {modificationReason})
								+ "\n\n" //$NON-NLS-1$
								+ CommonUIMessages.FileModificationValidator_EditProblemDialogMessage_part2
								+ "\n" //$NON-NLS-1$
								+ MessageFormat
									.format(
										CommonUIMessages.FileModificationValidator_EditProblemDialogMessage_part3,
										new Object[] {status.getMessage()}));
					result[0] = false;
				}
			}
		});
		return result[0];
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

		// if no provider or no validator use the default validator
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
			MessageDialog
				.openError(
					Display.getCurrent().getActiveShell(),
					CommonUIMessages.FileModificationValidator_SaveProblemDialogTitle,
					CommonUIMessages.FileModificationValidator_SaveProblemDialogMessage_part1
						+ "\n\n" //$NON-NLS-1$
						+ CommonUIMessages.FileModificationValidator_SaveProblemDialogMessage_part2
						+ "\n" //$NON-NLS-1$
						+ MessageFormat
							.format(
								CommonUIMessages.FileModificationValidator_SaveProblemDialogMessage_part3,
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
			String message = MessageFormat
				.format(
					CommonUIMessages.FileModificationValidator_FileIsReadOnlyErrorMessage,
					new Object[] {file.getFullPath().toString()});
			return new Status(Status.ERROR, CommonUIPlugin.getPluginId(),
				Status.ERROR, message, null);
		} else {
			return new Status(Status.OK, CommonUIPlugin.getPluginId(),
				Status.OK, CommonUIMessages.FileModificationValidator_OK, null);
		}
	}
}