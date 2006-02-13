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

package org.eclipse.gmf.runtime.emf.commands.core.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.emf.workspace.CompositeEMFOperation;
import org.eclipse.gmf.runtime.common.core.command.CMValidator;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;

/**
 * An undoable operation that is composed of child {@link IUndoableOperation}s
 * that are expected to modify EMF model resources.
 * <p>
 * The operation provides a list of {@link IFile}s that are expected to be
 * modified when the operation is executed, undone or redone. An
 * {@link IOperationApprover} is registered with the
 * {@link OperationHistoryFactory#getOperationHistory()} to validate the
 * modification to these resources.
 * <P>
 * This class is meant to be instantiated by clients.
 * 
 * @author ldamus
 */
public class CompositeTransactionalCommand
    extends CompositeEMFOperation
    implements ICompositeCommand {

    private CommandResult commandResult;

    /**
     * Initializes me with the editing domain in which I am making model changes
     * and a label.
     * 
     * @param domain
     *            my editing domain
     * @param label
     *            my user-readable label
     */
    public CompositeTransactionalCommand(TransactionalEditingDomain domain,
            String label) {
        super(domain, label);
    }

    /**
     * Initializes me with the editing domain, a label, and transaction options.
     * 
     * @param domain
     *            my editing domain
     * @param label
     *            my user-readable label
     * @param options
     *            for the transaction in which I execute myself, or
     *            <code>null</code> for the default options
     */
    public CompositeTransactionalCommand(TransactionalEditingDomain domain,
            String label, Map options) {
        super(domain, label, options);
    }

    /**
     * Initializes me with the editing domain, a label, and child operations.
     * 
     * @param domain
     *            my editing domain
     * @param label
     *            my user-readable label
     * @param children
     *            a list of operations to compose
     */
    public CompositeTransactionalCommand(TransactionalEditingDomain domain,
            String label, List children) {
        super(domain, label, children);
    }

    /**
     * Initializes me with the editing domain, a label, and child operations,
     * and transaction options.
     * 
     * @param domain
     *            my editing domain
     * @param label
     *            my user-readable label
     * @param children
     *            a list of operations to compose
     * @param options
     *            for the transaction in which I execute myself, or
     *            <code>null</code> for the default options
     */
    public CompositeTransactionalCommand(TransactionalEditingDomain domain,
            String label, List children, Map options) {
        super(domain, label, children, options);
    }

    /**
     * Returns the {@link IFile}s for resources that may be modified when the
     * operation is executed, undone or redone.
     */
    public List getAffectedFiles() {

        List result = new ArrayList();

        for (Iterator i = iterator(); i.hasNext();) {
            IUndoableOperation nextOperation = (IUndoableOperation) i.next();

            if (nextOperation instanceof ICommand) {
                List nextAffected = ((ICommand) nextOperation)
                    .getAffectedFiles();

                if (nextAffected != null) {
                    result.addAll(nextAffected);
                }
            }
        }
        return result;
    }

    // Documentation copied from the interface
    public CommandResult getCommandResult() {
        return commandResult;
    }

    /**
     * Sets the command result.
     * 
     * @param result
     *            the new result for this command.
     */
    protected void setResult(CommandResult result) {
        this.commandResult = result;
    }

    /**
     * Returns a list containing all of the return values from
     * <code>ICommand</code> children.
     */
    protected List getReturnValues() {

        List returnValues = new ArrayList();

        for (Iterator i = iterator(); i.hasNext();) {
            IUndoableOperation operation = (IUndoableOperation) i.next();

            if (operation instanceof ICommand) {
                ICommand command = (ICommand) operation;

                CommandResult result = command.getCommandResult();

                if (result != null) {
                    Object returnValue = result.getReturnValue();

                    if (returnValue != null) {

                        if (getClass().isInstance(command)) {
                            // unwrap the values from other composites
                            if (returnValue != null
                                && returnValue instanceof Collection) {
                                returnValues.addAll((Collection) returnValue);

                            } else {
                                returnValues.add(returnValue);
                            }

                        } else {
                            returnValues.add(returnValue);
                        }
                    }
                }
            }
        }

        return returnValues;
    }

    /**
     * Overrides the superclass implementation to set the command result.
     */
    protected IStatus aggregateStatuses(List statuses) {
        IStatus aggregate = super.aggregateStatuses(statuses);
        setResult(new CommandResult(aggregate, getReturnValues()));
        return aggregate;
    }

    // Documentation copied from the interface
    public final ICommand compose(IUndoableOperation operation) {

        if (operation != null) {
            add(operation);
        }
        return this;
    }

    /**
     * Returns the simplest form of this command that is equivalent. This is
     * useful for removing unnecessary nesting of commands.
     * <P>
     * If the composite has a single command, it returns the reduction of that
     * single command. Otherwise, it returns itself.
     * 
     * @return the simplest form of this command that is equivalent
     */
    public ICommand reduce() {
        switch (size()) {
            case 1:
                IUndoableOperation child = (IUndoableOperation) iterator()
                    .next();

                if (child instanceof ICommand
                    && (child instanceof CompositeEMFOperation || child instanceof AbstractEMFOperation)) {
                    // return the single command if is a kind of EMF operation;
                    // otherwise this composite will be returned to preserve the
                    // EMF transaction behaviour.
                    return ((ICommand) child).reduce();
                }
        }
        return this;
    }

    /**
     * Answers whether or not this composite operation has children.
     * 
     * @return <code>true</code> if the operation does not have children,
     *         <code>false</code> otherwise.
     */
    public final boolean isEmpty() {
        return size() < 1;
    }

    //
    // TODO LMDAMUS The following methods to be removed when deprecated API on ICommand
    // is removed. 
    // 

    /**
     * Returns the affected {@link IFile}s.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #getAffectedFiles()} instead.
     */
    public Collection getAffectedObjects() {
        return Collections.EMPTY_LIST;
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
        return null;
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
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #canExecute()} instead.
     */
    public boolean isExecutable() {
        return canExecute();
    }

    /**
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #canRedo()} instead.
     */
    public boolean isRedoable() {
        return canRedo();
    }

    /**
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #canUndo()} instead.
     */
    public boolean isUndoable() {
        return canUndo();
    }

    /**
     * Delegates to {@link #execute(IProgressMonitor, IAdaptable)} and logs any
     * {@link ExecutionException} that occurs.
     * 
     * @deprecated Implemented for backwards compatibility. Use
     *             {@link #execute(IProgressMonitor, IAdaptable)} instead.
     */
    public void execute(IProgressMonitor progressMonitor) {

        try {
            execute(progressMonitor, null);

        } catch (ExecutionException e) {
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
            redo(new NullProgressMonitor(), null);

        } catch (ExecutionException e) {
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
            undo(new NullProgressMonitor(), null);

        } catch (ExecutionException e) {
            handle(e);
        }
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
}
