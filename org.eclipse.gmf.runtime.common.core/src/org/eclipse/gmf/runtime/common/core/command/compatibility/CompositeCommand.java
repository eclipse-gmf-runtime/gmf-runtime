/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.command.compatibility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CMValidator;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;

/**
 * Old implementation of the CompositeCommand for backwards compatibility.
 * 
 * @deprecated Use
 *             {@link org.eclipse.gmf.runtime.common.core.command.CompositeCommand}
 *             instead.
 * 
 * @author ldamus
 */
public class CompositeCommand
    extends org.eclipse.gmf.runtime.common.core.command.CompositeCommand
    implements ICompositeCommand {
    
    /**
     * The empty string.
     * 
     * @deprecated Use {@link StringStatics#BLANK} instead.
     */
    protected static final String EMPTY_STRING = ""; //$NON-NLS-1$
    
    /**
     * Flag to indicate whether or not this command was canceled in its last
     * execution.
     * @deprecated not needed
     */
    private boolean canceled = false;
    
    /**
     * The commands of which this composite command is composed.
     */
    private final List commands = new ArrayList();

    /**
     * Initializes me with a label.
     * 
     * @param label
     *            a user-readable label
     */
    public CompositeCommand(String label) {
        this(label, null);
    }

    /**
     * Initializes me with a label and a list of child operations.
     * 
     * @param label
     *            a user-readable label
     * @param children
     *            a list of child {@link IUndoableOperation}s
     */
    public CompositeCommand(String label, List children) {
        super(label, children);

        assert null != commands : "null commands"; //$NON-NLS-1$

        for (Iterator i = commands.iterator(); i.hasNext();) {
            ICommand command = (ICommand) i.next();
            compose(command);
        }
    }
    
    protected List getChildren() {
        return commands;
    }
    
    /**
     * Sets the canceled state of this command.
     * 
     * @param canceled
     *            <code>true</code> if the command was canceled,
     *            <code>false</code> otherwise.
     * @deprecated not needed
     */
    protected void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    /**
     * Gets the canceled state of this command.
     * 
     * @return <code>true</code> if the command was canceled,
     *         <code>false</code> otherwise.
     * @deprecated not needed
     */
    protected boolean isCanceled() {
        return canceled;
    }
    
    /**
     * Retrieves the collection of objects that would be affected if this
     * composite command were executed, undone, or redone.
     * 
     * @return A collection containing the affected objects of all of the
     *         commands of which this composite command is composed.
     * @deprecated Use {@link #getAffectedFiles()} instead.
     */
    public final Collection getAffectedObjects() {
        List affectedObjects = new ArrayList();

        for (Iterator i = getCommands().iterator(); i.hasNext();) {

            Collection coll = ((ICommand) i.next()).getAffectedObjects();
            if (coll != null) {
                affectedObjects.addAll(coll);
            }
        }

        return affectedObjects;
    }
    
    public List getAffectedFiles() {
        return (List) getAffectedObjects();
    }
    
    /**
     * Returns the simplest form of this command that is equivalent. This is
     * useful for removing unnecessary nesting of commands.
     * <UL>
     * <LI>if the composite had no sub-commands, it returns <code>null</code>
     * </LI>
     * <LI>if the composite had a single command, it returns the single command
     * </LI>
     * <LI>otherwise, it returns itself</LI>
     * </UL>
     * 
     * @return the simplest form of this command that is equivalent
     * @deprecated Use {@link #reduce()} instead.
     */
    public ICommand unwrap() {
        switch (getCommands().size()) {
            case 0:
                return UnexecutableCommand.INSTANCE;
            case 1:
                return (ICommand) getCommands().get(0);
            default:
                return this;
        }
    }
    
    public ICommand reduce() {
        return unwrap();
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
     * Executes this composite command by executing all of the commands with
     * which it is composed. If one command execution fails, the remaining
     * commands will not be executed.
     * <P>
     * The result of executing this command can be obtained by calling
     * {@link #getCommandResult()}after the command has been executed.
     * <P>
     * The progress of this command execution is measured in the following way:
     * each of <code>n</code> subcommands is allocated 1 of <code>n</code>
     * work units from <code>progressMonitor</code>. Each sub-command
     * execution is given a {@link SubProgressMonitor}and can futher divide its
     * 1/<code>n</code> th into <code>m</code> work units. Command
     * execution will stop when the progress monitor is cancelled and a
     * {@link CommonCoreStatusCodes#CANCELLED}status code will be returned in
     * the command result. All of the previously executed sub-commands will be
     * undone as a result of cancelling.
     * 
     * @param progressMonitor @see org.eclipse.core.runtime.IProgressMonitor
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #execute(IProgressMonitor, IAdaptable)} instead.
     */
    public void execute(IProgressMonitor progressMonitor) {

        if (!getValidator().okToEdit(this)) {
            setCanceled(true);

        } else {

            IProgressMonitor monitor = (progressMonitor == null) ? new NullProgressMonitor()
                : progressMonitor;

            setCanceled(false);
            List executedCommands = new ArrayList(getCommands().size());

            int totalWork = getCommands().size();
            monitor.beginTask(getLabel(), totalWork);

            for (Iterator i = getCommands().iterator(); i.hasNext();) {

                SubProgressMonitor subprogressMonitor = new SubProgressMonitor(
                    monitor, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL);

                ICommand nextCommand = (ICommand) i.next();
                nextCommand.execute(subprogressMonitor);

                CommandResult result = nextCommand.getCommandResult();
                if (result != null) {
                    if (result.getStatus().getSeverity() == IStatus.ERROR) {
                        /*
                         * myee - RATLC00518953: error executing one of the
                         * composed commands: cancel all of the executed
                         * commands
                         */
                        undoCancelledCommands(executedCommands);
                        setResult(getCommandResult());
                        return;
                    }
                }
                monitor.worked(1);

                if (monitor.isCanceled()) {
                    undoCancelledCommands(executedCommands);
                    monitor.done();
                    setCanceled(true);
                    return;
                }
                executedCommands.add(nextCommand);
            }

            monitor.done();

        }
        setResult(getCommandResult());
    }
    
    /**
     * Retrieves the composite result of executing, undoing, or redoing this
     * composite command.
     * 
     * @return A command result composed of the results of
     *         executing, undoing or redoing the commands of which this composite
     *         command is composed.
     */
    public CommandResult getCommandResult() {

        if (isCanceled()) {
            return newCancelledCommandResult();
        }

        List statuses = new ArrayList();
        List returnValues = new ArrayList();

        int severity = IStatus.OK;

        String plugin = CommonCorePlugin.getPluginId();
        int code = CommonCoreStatusCodes.OK;
        String message = EMPTY_STRING;
        Throwable exception = null;

        for (Iterator i = getCommands().iterator(); i.hasNext();) {
            ICommand command = (ICommand) i.next();

            CommandResult result = command.getCommandResult();

            if (result == null) {
                // the result can be null if only some of the commands have been
                // executed (e.g., the action was abandoned)
                break;
            }

            IStatus status = result.getStatus();
            statuses.add(result.getStatus());

            if (severity < status.getSeverity()) {
                severity = status.getSeverity();
                plugin = status.getPlugin();
                code = status.getCode();
                message = status.getMessage();
                exception = status.getException();
            }

            Object returnValue = result.getReturnValue();
            if (returnValue != null) {
                if (getClass().isInstance(command)) {
                    if (returnValue != null && returnValue instanceof Collection) {
                        returnValues.addAll((Collection) returnValue);
                    } else {
                        returnValues.add(returnValue);
                    }
                } else {
                    returnValues.add(returnValue);
                }
            }
        }

        return new CommandResult(
            new MultiStatus(plugin, code, (IStatus[]) statuses
                .toArray(new IStatus[] {}), message, exception), returnValues);
    }

    /**
     * Cancels the command execution by calling <code>undo()</code> on all of
     * the undoable commands that were executed before the composite command was
     * cancelled. The commands are undone in the reverse order of execution.
     * 
     * @param executedCommands
     *            the commands that have been executed and need to be undone.
     *            This method expects that the commands in the list are in the
     *            order in which the commands were executed. They will be undone
     *            in the reverse order.
     * @deprecated Implemented for backwards compatibility. No replacement.
     */
    protected void undoCancelledCommands(List executedCommands) {

        Collections.reverse(executedCommands);

        for (Iterator i = executedCommands.iterator(); i.hasNext();) {
            ICommand nextCommand = (ICommand) i.next();
            if (nextCommand.isUndoable()) {
                nextCommand.undo();
            }
        }
    }

    /**
     * Redoes this composite command by redoing each of the commands of which
     * this composite command is composed.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #redo(IProgressMonitor, IAdaptable)} instead.
     */
    public void redo() {
        // First check if we have the needed units available.
        // We are forced to do this at the composite command level
        // because some individual commands do not properly set their
        // affectedObject. For example, create a class and notice that
        // the SetBounds command will not have its affected object set
        // even though it clearly modifies a unit.
        if (!getValidator().okToEdit(this)) {
            setCanceled(true);
            return;
        }

        Collections.reverse(getCommands());

        for (Iterator i = getCommands().iterator(); i.hasNext();) {
            ((ICommand) i.next()).redo();
        }
    }

    /**
     * Undoes this composite command by undoing each of the commands of which
     * this composite command is composed.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #undo(IProgressMonitor, IAdaptable)} instead.
     */
    public void undo() {
        // First check if we have the needed units available.
        // We are forced to do this at the composite command level
        // because some individual commands do not properly set their
        // affectedObject. For example, create a class and notice that
        // the SetBounds command will not have its affected object set
        // even though it clearly modifies a unit.
        if (!getValidator().okToEdit(this)) {
            setCanceled(true);
            return;
        }

        Collections.reverse(getCommands());

        for (Iterator i = getCommands().iterator(); i.hasNext();) {
            ((ICommand) i.next()).undo();
        }
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
     * Retrieves the commands with which this command has been composed.
     * 
     * @return The commands with which this command has been composed.
     * @deprecated Use {@link #getChildren()} instead.
     */
    public final List getCommands() {
        return commands;
    }
    
    /**
     * Answers whether this composite command can
     * be executed.
     * 
     * @return <code>false</code> if any of the commands of which this
     *         composite command is composed cannot be executed;
     *         <code>true</code> otherwise.
     * @deprecated Use {@link #canExecute()} instead.
     */
    public final boolean isExecutable() {
        if (getCommands().isEmpty())
            return false;
        for (Iterator i = getCommands().iterator(); i.hasNext();) {

            if (!((ICommand) i.next()).isExecutable()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Answers whether this composite command can
     * be redone.
     * 
     * @return <code>false</code> if any of the commands of which this
     *         composite command is composed cannot be redone; <code>true</code>
     *         otherwise.
     * @deprecated Use {@link #canRedo()} instead.
     */
    public final boolean isRedoable() {
        if (getCommands().isEmpty())
            return false;
        for (Iterator i = getCommands().iterator(); i.hasNext();) {

            if (!((ICommand) i.next()).isRedoable()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Answers whether this composite command can
     * be undone.
     * 
     * @return <code>false</code> if any of the commands of which this
     *         composite command is composed cannot be undone; <code>true</code>
     *         otherwise.
     * @deprecated Use {@link #canUndo()} instead.
     */
    public final boolean isUndoable() {
        if (getCommands().isEmpty())
            return false;
        for (Iterator i = getCommands().iterator(); i.hasNext();) {

            if (!((ICommand) i.next()).isUndoable()) {
                return false;
            }
        }

        return true;
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
