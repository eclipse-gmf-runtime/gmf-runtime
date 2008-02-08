/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.resources;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileModificationValidator;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
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
        return okToEdit(files, modificationReason, null);
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
     * @param shell
     *            UI context for UI that could be presented to the user to
     *            determine whether the file may be edited.
     * @return true if it is OK to edit the files.
     * @see org.eclipse.core.resources.IFileModificationValidator#validateEdit
     */
    public boolean okToEdit(final IFile[] files,
			final String modificationReason, final Shell shell) {

		final IStatus fileStatus = validateEdit(files, shell);
		
		if (!fileStatus.isOK()) {
			// Similar to the DefaultUIFileModificationValidator we check if the shell is not null before 
			// displaying messages.
			if(shell != null){
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					public void run() {
						MessageDialog
								.openError(
										shell,
										NLS.bind(CommonUIMessages.FileModificationValidator_EditProblemDialogTitle,
												modificationReason),
										NLS.bind(CommonUIMessages.FileModificationValidator_EditProblemDialogMessage,
												modificationReason,
												fileStatus.getMessage()));
					}
				});
			}
			return false;
		}
		return true;
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
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getShell(),
					CommonUIMessages.FileModificationValidator_SaveProblemDialogTitle,
					NLS
						.bind(
							CommonUIMessages.FileModificationValidator_SaveProblemDialogMessage,
							status.getMessage(), status.getMessage()));
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
			String message = NLS
				.bind(
					CommonUIMessages.FileModificationValidator_FileIsReadOnlyErrorMessage,
					file.getFullPath().toString());
			return new Status(Status.ERROR, CommonUIPlugin.getPluginId(),
				Status.ERROR, message, null);
		} else {
			return new Status(Status.OK, CommonUIPlugin.getPluginId(),
				Status.OK, CommonUIMessages.FileModificationValidator_OK, null);
		}
	}

	/**
	 * Validates changes to the specified array of IFiles using the specified shell as a UI context.
	 * This code delegates the bulk of its processing to the {@link IWorkspace#validateEdit(IFile[], Object)
	 * method, but additionally checks to see if the specified IFiles are out of synchronization with
	 * the filesystem, and if so, returns an error status.
	 * 
	 * @param files the array of files for which edit validation is requested
	 * @param shell a UI context (SWT shell) for the validation.  Typed as object to avoid SWT dependency.
	 * @return IStatus, {@link IStatus#OK} if edit of the specified files may proceed, {@link IStatus#ERROR}
	 * or {@link IStatus#CANCEL} otherwise.
	 */
	public IStatus validateEdit(IFile[] files, Object shell) {
		IStatus status = Status.OK_STATUS;
		if (files == null || files.length == 0) {
			return status;
		}
		Set<IFile> unsynchedFiles = new HashSet<IFile>();
		Map<IFile, ModificationStamp> filesToModificationStamps = new HashMap<IFile, ModificationStamp>();

		for (int i = 0; i < files.length; i++) {
			IFile file = files[i];
			filesToModificationStamps.put(file, new ModificationStamp(file));
			boolean inSync = file.isSynchronized(IResource.DEPTH_ZERO);
			if (!inSync) {
				unsynchedFiles.add(file);
			}
		}
		if (!unsynchedFiles.isEmpty()) {
			status = buildOutOfSyncStatus(unsynchedFiles);
		}

		if (status.isOK()) {
			status = ResourcesPlugin.getWorkspace().validateEdit(files, shell);

			for (Map.Entry<IFile, ModificationStamp> entry : filesToModificationStamps.entrySet()) {
				IFile file = entry.getKey();
				ModificationStamp stamp = entry.getValue();
				if (stamp.hasFileChanged()) {
					unsynchedFiles.add(file);
				}
			}
			if (!unsynchedFiles.isEmpty()) {
				status = buildOutOfSyncStatus(unsynchedFiles);
			}

		}
		return status;
	}

	/**
	 *  Helper method to create a status for out of sync files.
	 *  
	 * @param unsynchedFiles Files that may be out of sync.
	 * @return A status for out of sync files.
	 */
	private IStatus buildOutOfSyncStatus(Set<IFile> unsynchedFiles) {
		StringBuffer buf = new StringBuffer(
				CommonUIMessages.FileModificationValidator_OutOfSyncMessage);
		buf.append("\n"); //$NON-NLS-1$
		for (Iterator<IFile> unsynched = unsynchedFiles.iterator(); unsynched
				.hasNext();) {
			IFile file = unsynched.next();
			buf.append(file.getFullPath().toString());
			buf.append("\n"); //$NON-NLS-1$
		}
		return new Status(IStatus.ERROR, CommonUIPlugin.getPluginId(), 0, buf
				.toString(), null);
	}

	private static class ModificationStamp {

		/**
		 * The file that the modification stamp applies to.
		 */
		private IFile file;
		/**
		 * Last modified date and time of the file
		 */
		private Timestamp lastModified = null;
		/**
		 * The file's length.
		 */
		private long fileLength = 0L;

		public ModificationStamp(IFile file) {
			assert file != null;
			this.file = file;
			IPath path = file.getLocation();
			if (path != null) {
				File ioFile = path.toFile();
				if (ioFile != null) {
					// new timestamp
					lastModified = new Timestamp(ioFile.lastModified());
					lastModified.setNanos(0);
					fileLength = ioFile.length();
				} else {
					lastModified = new Timestamp(file.getModificationStamp());
					lastModified.setNanos(0);
				}
			} else {
				lastModified = new Timestamp(file.getModificationStamp());
				lastModified.setNanos(0);
			}

		}

		public Timestamp getLastModified() {
			return lastModified;
		}

		public long getFileLength() {
			return fileLength;
		}

		public IFile getFile() {
			return file;
		}

		/**
		 * Used in determining if two timestamps are equivalent 
		 */
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj instanceof ModificationStamp) {
				ModificationStamp stamp = (ModificationStamp) obj;
				return file.equals(stamp.getFile())
						&& fileLength == stamp.getFileLength()
						&& lastModified.equals(stamp.getLastModified());
			}

			return false;
		}

		public int hashCode() {
			return file.hashCode() + lastModified.hashCode()
					+ (int) (fileLength ^ (fileLength >>> 32));

		}

		/**
		 * Determines if the file has changed.
		 * 
		 * @return true if the file has changed.
		 */
		public boolean hasFileChanged() {
			IPath path = file.getLocation();
			if (path == null) {
				return false;
			}
			File ioFile = path.toFile();
			if (ioFile == null) {
				return false;
			}

			// new timestamp
			Timestamp newTimestamp = new Timestamp(ioFile.lastModified());
			newTimestamp.setNanos(0);

			return !(lastModified.getTime() == newTimestamp.getTime() && fileLength == ioFile
					.length());
		}
	}
	    
}