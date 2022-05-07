/******************************************************************************
 * Copyright (c) 2006, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.internal.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.operations.IOperationApprover2;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.FileModificationValidator;
import org.eclipse.gmf.runtime.common.core.command.ICommand;

/**
 * Approves operations that implement the {@link ICommand} interface by checking
 * that their affected {@link IFile}s can be modified.
 * 
 * @author ldamus
 */
public class FileModificationApprover
    implements IOperationApprover2 {

    /**
     * Approves file modification for {@link ICommand}s.
     */
    public IStatus proceedExecuting(IUndoableOperation operation,
            IOperationHistory history, IAdaptable info) {

        if (operation instanceof ICommand) {
            return approveFileModification((ICommand) operation);
        }

        return Status.OK_STATUS;
    }

    /**
     * Approves file modification for {@link ICommand}s.
     */
    public IStatus proceedRedoing(IUndoableOperation operation,
            IOperationHistory history, IAdaptable info) {

        if (operation instanceof ICommand) {
            return approveFileModification((ICommand) operation);
        }

        return Status.OK_STATUS;
    }

    /**
     * Approves file modification for {@link ICommand}s.
     */
    public IStatus proceedUndoing(IUndoableOperation operation,
            IOperationHistory history, IAdaptable info) {

        if (operation instanceof ICommand) {
            return approveFileModification((ICommand) operation);
        }

        return Status.OK_STATUS;
    }

    /**
     * Checks that affected {@link IFile}s can be modified.
     * 
     * @return the approval status
     */
    private IStatus approveFileModification(ICommand fileModifier) {

        List files = new ArrayList();

        for (Iterator i = fileModifier.getAffectedFiles().iterator(); i
            .hasNext();) {
            IFile nextFile = (IFile) i.next();
            
            if ( nextFile == null ) 
                continue;
            
            if (nextFile.exists()) {
                // the file is in the workspace
                files.add(nextFile);

            } else {
                // the file is not in the workspace
            	IPath path = nextFile.getRawLocation();
            	if (path == null) {
					// cancel if we can't find the file
                    setCommandResult(fileModifier, Status.CANCEL_STATUS);
					return Status.CANCEL_STATUS;
				}
				File file = path.toFile();
				if (file != null && file.exists() && !file.canWrite()) {
					// cancel if we find a read-only file outside the
					// workspace
                    setCommandResult(fileModifier, Status.CANCEL_STATUS);
					return Status.CANCEL_STATUS;
				}	
            }
        }

        IStatus status = FileModificationValidator
            .approveFileModification((IFile[]) files.toArray(new IFile[] {}));
        
        if (!status.isOK()) {
            setCommandResult(fileModifier, status);
        }
        
        return status;
    }
    
    /**
     * Sets the command result of the specified command to a CommandResult
     * having the specified status.
     * 
     * @param command ICommand to set the CommandResult for
     * @param status IStatus of the CommandResult that will be set on the
     * command
     */
    private void setCommandResult(ICommand command, IStatus status) {
        if (command instanceof ICommandWithSettableResult) {
            ((ICommandWithSettableResult) command).internalSetResult(new CommandResult(status));
        }
    }
}
