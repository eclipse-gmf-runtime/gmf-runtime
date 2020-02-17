/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.EditorMessages;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.ContainerGenerator;

/**
 * A file creator that creates a file in an IDE dependent
 * WorkspaceModifyOperation.
 * 
 * @author wdiu, Wayne Diu, refactored from DiagramFileCreator
 */
public abstract class IDEEditorFileCreator
	extends DiagramFileCreator {

	/**
	 * Creates the file, taking other file operations in the workspace into
	 * account.
	 * 
	 * @param fileHandle
	 *            the IFile that will be created.
	 * @param contents
	 *            InputStream with the initial contents for the new IFile
	 * @param runContext
	 *            the IRunnableContext that the operation to create the file
	 *            will be run in.
	 * @throws InterruptedException
	 *             if the create file operation is interrupted
	 * @throws InvocationTargetException
	 *             which may be caused by the create file operation
	 */
	protected void createFile(final IFile fileHandle,
			final InputStream contents, final IRunnableContext runContext)
		throws InterruptedException, InvocationTargetException {

		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

			protected void execute(IProgressMonitor monitor)
				throws CoreException, InterruptedException {
				try {
					monitor.beginTask(EditorMessages.FileCreator_TaskTitle, 2000);

					assert fileHandle.getFullPath().segmentCount() >= 2;

					IPath containerPath = fileHandle.getFullPath()
						.removeLastSegments(1);

					ContainerGenerator generator = new ContainerGenerator(
						containerPath);
					generator.generateContainer(new SubProgressMonitor(monitor,
						1000));
					createFile(fileHandle, contents, new SubProgressMonitor(
						monitor, 1000));
				} finally {
					monitor.done();
				}
			}
		};

		runContext.run(true, true, op);

	}

}