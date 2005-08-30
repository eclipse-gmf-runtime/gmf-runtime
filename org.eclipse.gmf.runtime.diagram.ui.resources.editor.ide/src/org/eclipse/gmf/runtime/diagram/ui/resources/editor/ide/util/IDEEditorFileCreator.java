/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.ContainerGenerator;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.EditorFileCreator;

/**
 * A file creator that creates a file in an IDE dependent
 * WorkspaceModifyOperation.
 * 
 * @author wdiu, Wayne Diu, refactored from EditorFileCreator
 */
public abstract class IDEEditorFileCreator
	extends EditorFileCreator {

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
					monitor.beginTask(ResourceManager.getI18NString("FileCreator.TaskTitle"), 2000);//$NON-NLS-1$

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