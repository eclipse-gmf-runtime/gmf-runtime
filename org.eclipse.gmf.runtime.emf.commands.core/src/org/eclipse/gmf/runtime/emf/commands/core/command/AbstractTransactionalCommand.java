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
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.CompositeChangeDescription;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.common.core.command.CMValidator;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;

/**
 * An abstract superclass for GMF {@link IUndoableOperation}s that modify EMF
 * model resources.
 * <p>
 * The operation provides a list of {@link IFile}s that are expected to be
 * modified when the operation is executed, undone or redone. An
 * {@link IOperationApprover} is registered with the
 * {@link OperationHistoryFactory#getOperationHistory()} to validate the
 * modification to these resources.
 * <p>
 * Subclasses must return the command execution result in their implementation
 * of {@link #doExecuteWithResult(IProgressMonitor, IAdaptable)}.
 * <p>
 * This class is meant to be extended by clients.
 * 
 * @author ldamus
 */
public abstract class AbstractTransactionalCommand
    extends AbstractEMFOperation
    implements ICommand {

    /**
     * Convenience method to get a list of workspaces files associated with
     * <code>eObject</code>.
     * 
     * @param eObject
     *            the model object, may be <code>null</code>
     * @return the list of {@link IFile}s
     */
    protected static List getWorkspaceFiles(EObject eObject) {
        List result = new ArrayList();

        if (eObject != null) {
            Resource resource = eObject.eResource();
            
            if (resource != null) {
                IFile file = WorkspaceSynchronizer.getFile(resource);
    
                if (file != null) {
                    result.add(file);
                }
            }
        }

        return result;
    }

    /**
     * Convenience method to get a list of workspaces files associated with
     * {@link EObject}s in <code>eObject</code>.
     * 
     * @param eObjects
     *            the list of model object
     * @return the list of {@link IFile}s
     */
    protected static List getWorkspaceFiles(List eObjects) {
        List result = new ArrayList();

        for (Iterator i = eObjects.iterator(); i.hasNext();) {
            Object next = i.next();

            if (next instanceof EObject) {
                Resource resource = ((EObject) next).eResource();
                
                if (resource != null) {
                    IFile file = WorkspaceSynchronizer.getFile(resource);
    
                    if (file != null) {
                        result.add(file);
                    }
                }
            }
        }

        return result;
    }

    private final List affectedFiles;

    private CommandResult commandResult;

    /**
     * Initializes me with the editing domain in which I am making model
     * changes, a label, and a list of {@link IFile}s that I anticipate
     * modifying when I am executed, undone or redone.
     * 
     * @param domain
     *            my editing domain
     * @param label
     *            my user-readable label
     * @param affectedFiles
     *            the list of affected {@link IFile}s; may be <code>null</code>
     */
    public AbstractTransactionalCommand(TransactionalEditingDomain domain,
            String label, List affectedFiles) {
        this(domain, label, null, affectedFiles);
    }

    /**
     * Initializes me with the editing domain, a label, transaction options, and
     * a list of {@link IFile}s that anticipate modifying when I am executed,
     * undone or redone.
     * 
     * @param domain
     *            my editing domain
     * @param label
     *            my user-readable label
     * @param options
     *            for the transaction in which I execute myself, or
     *            <code>null</code> for the default options
     * @param affectedFiles
     *            the list of affected {@link IFile}s; may be <code>null</code>
     */
    public AbstractTransactionalCommand(TransactionalEditingDomain domain,
            String label, Map options, List affectedFiles) {
        super(domain, label, options);

        if (affectedFiles == null) {
            this.affectedFiles = new ArrayList();

        } else {
            this.affectedFiles = affectedFiles;
        }
    }

    /**
     * Returns the {@link IFile}s for resources that may be modified when the
     * operation is executed, undone or redone.
     */
    public List getAffectedFiles() {
        return affectedFiles;
    }

    // Documentation copied from the interface
    public final CommandResult getCommandResult() {
        return commandResult;
    }

    /**
     * Sets the command result.
     * 
     * @param result
     *            the new result for this command.
     */
    protected final void setResult(CommandResult result) {
        this.commandResult = result;
    }

    // Documentation copied from the interface
    public ICommand compose(IUndoableOperation operation) {

        if (operation != null) {

            return new CompositeTransactionalCommand(getEditingDomain(), getLabel())
                .compose(this).compose(operation);
        }
        return this;
    }

    // Documentation copied from the interface
    public ICommand reduce() {
        return this;
    }

    /**
     * Implemented by subclasses to perform the model changes.  These changes
     * are applied by manipulation of the EMF metamodel's API, <em>not</em>
     * by executing commands on the editing domain's command stack.
     * 
     * @param monitor the progress monitor provided by the operation history
     * @param info the adaptable provided by the operation history
     * 
     * @return the result of the execution
     * 
     * @throws ExecutionException if, for some reason, I fail to complete
     *     the operation
     */
    protected abstract CommandResult doExecuteWithResult(
            IProgressMonitor monitor, IAdaptable info)
        throws ExecutionException;

    protected void didUndo(Transaction tx) {
    	// We will amalgamate any change description that were added by the DiagramEditingDomain's
    	//  special post-commit listener. See DiagramEditingDomainFactory for more details.
    	if (tx.getChangeDescription() != null && !tx.getChangeDescription().isEmpty()) {
    		((CompositeChangeDescription)getChange()).add(tx.getChangeDescription());
    	}
    }
    
    protected void didRedo(Transaction tx) {
    	// We will amalgamate any change description that were added by the DiagramEditingDomain's
    	//  special post-commit listener. See DiagramEditingDomainFactory for more details.
    	if (tx.getChangeDescription() != null && !tx.getChangeDescription().isEmpty()) {
    		((CompositeChangeDescription)getChange()).add(tx.getChangeDescription());
    	}
    }
    
    /**
     * Delegates to {@link #doExecuteWithResult(IProgressMonitor, IAdaptable)}
     * to perform the model changes. Sets the command result and calls
     * {@link #cleanup()} to give subclasses a chance to dispose of any objects
     * that were required for the execution but will not be required for undo or
     * redo.
     */
    protected IStatus doExecute(IProgressMonitor monitor, IAdaptable info)
        throws ExecutionException {

        CommandResult result = doExecuteWithResult(monitor, info);
        setResult(result);
        cleanup();
        return result != null ? result.getStatus()
            : Status.OK_STATUS;
    }

    /**
     * Overrides superclass to set the command result.
     */
    protected IStatus doUndo(IProgressMonitor monitor, IAdaptable info)
        throws ExecutionException {

        IStatus status = super.doUndo(monitor, info);
        CommandResult result = new CommandResult(status);
        setResult(result);

        return status;
    }

    /**
     * Overrides superclass to set the command result.
     */
    protected IStatus doRedo(IProgressMonitor monitor, IAdaptable info)
        throws ExecutionException {

        IStatus status = super.doRedo(monitor, info);
        CommandResult result = new CommandResult(status);
        setResult(result);

        return status;
    }

    /**
     * Subclasses may implement this method to dispose of objects that were
     * required for execution, but are no longer require to undo or redo this
     * operation.
     * <P>
     * This method is invoked at the end of
     * {@link #doExecute(IProgressMonitor, IAdaptable)}.
     */
    protected void cleanup() {
        // subclasses can use this to cleanup
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
