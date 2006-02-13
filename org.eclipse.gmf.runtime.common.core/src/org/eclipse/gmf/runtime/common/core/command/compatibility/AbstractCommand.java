/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.command.compatibility;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CMValidator;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;

/**
 * An abstract superclass for GMF {@link IUndoableOperation}s that do not
 * modify EMF model resources.
 * <p>
 * The operation provides a list of {@link IFile}s that are expected to be
 * modified when the operation is executed, undone or redone. An
 * {@link IOperationApprover} is registered with the
 * {@link OperationHistoryFactory#getOperationHistory()} to validate the
 * modification to these resources.
 * <p>
 * This class is meant to be extended by clients.
 * 
 * @author khussey
 * @author ldamus
 * 
 * @see org.eclipse.gmf.runtime.common.core.command.ICommand
 * @canBeSeenBy %partners
 */
public abstract class AbstractCommand
    extends org.eclipse.gmf.runtime.common.core.command.AbstractCommand {

    /**
     * The empty string.
     * 
     * @deprecated Use {@link StringStatics#BLANK} instead.
     */
    protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

    /**
     * Initializes me with a label.
     * 
     * @param label
     *            the operation label
     */
    public AbstractCommand(String label) {
        this(label, null);
    }

    /**
     * Initializes me with a label and a list of {@link IFile}s that anticipate
     * modifying when I am executed, undone or redone.
     * 
     * @param label
     *            the operation label
     * @param affectedFiles
     *            the list of affected {@link IFile}s; may be <code>null</code>
     */
    public AbstractCommand(String label, List affectedFiles) {
        super(label, affectedFiles);
        
        // initialize the command result
        setResult(CommandResult.newOKCommandResult());
    }

    public boolean canExecute() {
        return isExecutable();
    }

    public boolean canRedo() {
        return isRedoable();
    }

    public boolean canUndo() {
        return isUndoable();
    }

    /**
     * Returns the affected {@link IFile}s.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #getAffectedFiles()} instead.
     */
    public Collection getAffectedObjects() {
        return Collections.EMPTY_LIST;
    }
    
    public List getAffectedFiles() {
        return (List) getAffectedObjects();
    }

    /**
     * Returns <code>null</code>.
     * 
     * @deprecated File validation is now done through a
     *             {@link IOperationApprover} registered with with the
     *             {@link OperationHistoryFactory#getOperationHistory()}. No
     *             need to return a validator for backwards compatilibity.
     */
    public CMValidator getValidator() {
        return new CMValidator();
    }

    /**
     * Returns <code>false</code>.
     * 
     * @deprecated File validation is now done through a
     *             {@link IOperationApprover} registered with with the
     *             {@link OperationHistoryFactory#getOperationHistory()}. No
     *             need to calculate the answer for backwards compatilibity.
     */
    public boolean involvesReadOnlyNonWorkSpaceFiles() {
        return false;
    }

    /**
     * Delegates to {@link #canExecute()}.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #canExecute()} instead.
     */
    public boolean isExecutable() {
        return true;
    }

    /**
     * Delegates to {@link #canRedo()}.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #canRedo()} instead.
     */
    public boolean isRedoable() {
        return false;
    }

    /**
     * Delegates to {@link #canUndo()}.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #canUndo()} instead.
     */
    public boolean isUndoable() {
        return false;
    }

    /**
     * Delegates to {@link #execute(IProgressMonitor, IAdaptable)} and logs any
     * {@link ExecutionException} that occurs.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #execute(IProgressMonitor, IAdaptable)} instead.
     */
    public void execute(IProgressMonitor progressMonitor) {

        IProgressMonitor monitor = (progressMonitor == null) ? new NullProgressMonitor()
            : progressMonitor;

        try {
            if (getValidator().okToEdit(this)) {
                setResult(doExecute(monitor));
            } else {
                // We are not going to do the undo/redo.
                // We do not want the stack affected so we must return an
                // appropriate result.
                // This way the caller will know that the undo/redo was not
                // sucessful and will
                // not adjust the stack.
                setResult(newCancelledCommandResult());
            }
        } catch (Exception e) {
            handle(e);
        }
    }

    /**
     * Delegates to {@link #redo(IProgressMonitor, IAdaptable)} and logs any
     * {@link ExecutionException} that occurs.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #redo(IProgressMonitor, IAdaptable)} instead.
     */
    public void redo() {
        try {
            // First check if we have access to the units to be modified.
            if (getValidator().okToEdit(this)) {
                setResult(doRedo());
            } else {
                // We are not going to do the undo/redo.
                // We do not want the stack affected so we must return an
                // appropriate result.
                // This way the caller will know that the undo/redo was not
                // sucessful and will
                // not adjust the stack.
                setResult(newCancelledCommandResult());
            }
        } catch (Exception e) {
            handle(e);
        }
    }

    /**
     * Delegates to {@link #undo(IProgressMonitor, IAdaptable)} and logs any
     * {@link ExecutionException} that occurs.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #undo(IProgressMonitor, IAdaptable)} instead.
     */
    public void undo() {
        try {
            // First check if we have access to the units to be modified.
            if (getValidator().okToEdit(this)) {
                setResult(doUndo());
            } else {
                // We are not going to do the undo/redo.
                // We do not want the stack affected so we must return an
                // appropriate result.
                // This way the caller will know that the undo/redo was not
                // sucessful and will
                // not adjust the stack.
                setResult(newCancelledCommandResult());
            }
        } catch (Exception e) {
            handle(e);
        }
    }

    /**
     * Retrieves the plug-in identifier to be used in command results produced
     * by this command.
     * 
     * @return The plug-in identifier to be used in command results produced by
     *         this command.
     * @deprecated Subclasses should use the plugin ID from their plugin.
     */
    protected String getPluginId() {
        return CommonCorePlugin.getPluginId();
    }

    /**
     * Handles the specified exception by logging, tracing and setting the
     * command result to an error.
     * 
     * @param exception
     *            The exception to be handled.
     * @deprecated Supports deprecated implementation
     */
    protected void handle(Exception exception) {
        Trace.catching(CommonCorePlugin.getDefault(),
            CommonCoreDebugOptions.EXCEPTIONS_CATCHING, getClass(),
            "handle", exception); //$NON-NLS-1$

        setResult(CommandResult.newErrorCommandResult(exception));

        Log.log(CommonCorePlugin.getDefault(), getCommandResult().getStatus());
    }

    /**
     * Performs the actual work of executing this command. Subclasses must
     * implement this method to perform some operation.
     * 
     * @param progressMonitor
     *            The object that monitors the progress of this command
     *            execution. May be
     *            {@link org.eclipse.core.runtime.NullProgressMonitor}if the
     *            command should be executed without monitoring its progress.
     * @return The result of executing this command.
     * @deprecated Subclasses must implement
     *             {@link #doExecuteWithResult(IProgressMonitor, IAdaptable)}
     *             instead.
     */
    protected CommandResult doExecute(IProgressMonitor progressMonitor) {
        return newOKCommandResult();
    }

    /**
     * Performs the actual work of redoing this command. Subclasses must
     * override this method if the command is to be redoable.
     * 
     * @return The result of redoing this command.
     * @exception UnsupportedOperationException
     *                If this command isn't redoable.
     * @deprecated Subclasses must implement
     *             {@link #doRedoWithResult(IProgressMonitor, IAdaptable)}
     *             instead.
     */
    protected CommandResult doRedo() {
        UnsupportedOperationException uoe = new UnsupportedOperationException();
        Trace.throwing(CommonCorePlugin.getDefault(),
            CommonCoreDebugOptions.EXCEPTIONS_THROWING, getClass(),
            "doRedo", uoe); //$NON-NLS-1$
        throw uoe;
    }

    /**
     * Performs the actual work of undoing this command. Subclasses must
     * override this method if the command is to be undoable.
     * 
     * @return The result of undoing this command.
     * @exception UnsupportedOperationException
     *                If this command isn't undoable.
     * @deprecated Subclasses must implement
     *             {@link #doUndoWithResult(IProgressMonitor, IAdaptable)}
     *             instead.
     */
    protected CommandResult doUndo() {
        UnsupportedOperationException uoe = new UnsupportedOperationException();
        Trace.throwing(CommonCorePlugin.getDefault(),
            CommonCoreDebugOptions.EXCEPTIONS_THROWING, getClass(),
            "doUndo", uoe); //$NON-NLS-1$
        throw uoe;
    }

    protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        execute(progressMonitor);
        return getCommandResult();
    }

    protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {
        redo();
        return getCommandResult();
    }

    protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {
        undo();
        return getCommandResult();
    }

    /**
     * Creates a new command result with an OK status.
     * 
     * @return A new command result with an OK status.
     * @deprecated Use {@link CommandResult#newOKCommandResult()} instead.
     */
    protected CommandResult newOKCommandResult() {
        return CommandResult.newOKCommandResult();
    }

    /**
     * Creates a new command result with an OK status and the specified return
     * value.
     * 
     * @return A new command result with an OK status.
     * @param returnValue
     *            The return value for the new command result.
     * @deprecated Use {@link CommandResult#newOKCommandResult(Object)} instead.
     */
    protected CommandResult newOKCommandResult(Object returnValue) {
        return CommandResult.newOKCommandResult(returnValue);
    }

    /**
     * Creates a new command result with an ERROR status , a CANCELLED status
     * code and no return value.
     * 
     * @return A new command result with an ERROR status.
     * @deprecated Use {@link CommandResult#newCancelledCommandResult()}
     *             instead.
     */
    protected CommandResult newCancelledCommandResult() {
        return CommandResult.newCancelledCommandResult();
    }

    /**
     * Creates a new command result with an ERROR status, a COMMAND_FAILURE
     * status code, and no return value.
     * 
     * @param errorMessage
     *            error message
     * @return A new command result with an ERROR status.
     * @deprecated Use {@link CommandResult#newErrorCommandResult(String)}
     *             instead.
     */
    protected CommandResult newErrorCommandResult(String errorMessage) {
        return CommandResult.newErrorCommandResult(errorMessage);
    }

    /**
     * Creates a new command result with an WARNING status, a OK status code,
     * and no return value.
     * 
     * @param warningMessage
     *            the warning
     * @param returnValue
     *            the return value for the new command result
     * @return A new command result with a WARNING status.
     * @deprecated Use
     *             {@link CommandResult#newWarningCommandResult(String, Object)}
     *             instead.
     */
    protected CommandResult newWarningCommandResult(String warningMessage,
            Object returnValue) {
        return CommandResult.newWarningCommandResult(warningMessage,
            returnValue);
    }

}
