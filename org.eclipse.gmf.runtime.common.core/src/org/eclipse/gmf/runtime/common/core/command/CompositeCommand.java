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

package org.eclipse.gmf.runtime.common.core.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.ICompositeOperation;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.internal.l10n.CommonCoreMessages;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;

/**
 * An undoable command that is composed of child {@link IUndoableOperation}s
 * that are not known to modify EMF model resources, but can contain
 * model-affecting children. Execute, undo, redo and dispose result in execute,
 * undo, redo and dispose on each child operation. The operation provides a list
 * of {@link IFile}s that may be modified when the operation is executed,
 * undone or redone.
 * <P>
 * The children are explicitly composed by a client before the composite is
 * executed. Children cannot be added or removed after the composite has been
 * executed.
 * <P>
 * The undo contexts of the composite are a union of the undo contexts of its
 * children.
 * <P>
 * If a child command returns a cancel or an error status during execution, undo
 * or redo, the remaining child commands are not processed and those that have
 * already been executed are rolled back.
 * 
 * @author ldamus
 */
public class CompositeCommand
    extends AbstractCommand
    implements ICompositeCommand {

    private final List children;

    private boolean executed;

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
        super(label, null);

        if (children != null) {
            this.children = new ArrayList(children);
        } else {
            this.children = new ArrayList();
        }
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

    /**
     * Obtains my nested operations. Note that the return result is mutable and
     * is identical to my child-operation storage, so subclasses should be
     * careful of adding or removing contents. This should ordinarily be done
     * only via the {@link #add(IUndoableOperation)} and
     * {@link #remove(IUndoableOperation)} methods because these maintain the
     * undo contexts (or, equivalently, using the iterators).
     * 
     * @return my list of children
     * 
     * @see #add(IUndoableOperation)
     * @see #remove(IUndoableOperation)
     * @see #iterator()
     * @see #listIterator(int)
     */
    protected List getChildren() {
        return children;
    }

    // Documentation copied from interface
    public int size() {
        return getChildren().size();
    }

    /**
     * Adds a child operation to me. This should only be done before I am
     * executed. Has no effect if I already contain this operation as a child.
     * 
     * @param operation
     *            a new child operation
     * 
     * @throws IllegalStateException
     *             if I have already been successfully executed
     */
    public void add(IUndoableOperation operation) {

        assertNotExecuted();

        if (!getChildren().contains(operation)) {
            getChildren().add(operation);
            didAdd(operation);
        }
    }

    /**
     * Updates my undo contexts for the addition of a new child operation.
     * 
     * @param operation
     *            a new child operation
     */
    private void didAdd(IUndoableOperation operation) {
        IUndoContext[] childContexts = operation.getContexts();

        for (int i = 0; i < childContexts.length; i++) {

            if (!hasContext(childContexts[i])) {
                addContext(childContexts[i]);
            }
        }
    }

    /**
     * Removes a child operation from me. This should only be done before I am
     * executed. Has no effect if I do not contain this operation as a child.
     * <p>
     * <b>Note</b> that I do not dispose an operation when it is removed from
     * me. Although this is specified in the contract of the
     * {@link ICompositeOperation} interface, this would not be correct, as I
     * did not create that operation.
     * </p>
     * 
     * @param operation
     *            a child operation to remove
     * 
     * @throws IllegalStateException
     *             if I have already been successfully executed
     */
    public void remove(IUndoableOperation operation) {

        assertNotExecuted();

        if (getChildren().remove(operation)) {
            didRemove(operation);
        }
    }

    /**
     * Updates my undo contexts for the removal of a child operation.
     * 
     * @param operation
     *            the child operation that was removed
     */
    private void didRemove(IUndoableOperation operation) {
        IUndoContext[] childContexts = operation.getContexts();

        for (int i = 0; i < childContexts.length; i++) {

            if (!anyChildHasContext(childContexts[i])) {
                removeContext(childContexts[i]);
            }
        }
    }

    /**
     * Queries whether any of my children has the specified context.
     * 
     * @param ctx
     *            a context
     * 
     * @return <code>false</code> if none of my children has the specified
     *         context; <code>true</code>, otherwise
     */
    private boolean anyChildHasContext(IUndoContext ctx) {
        boolean result = false;

        for (Iterator iter = iterator(); !result && iter.hasNext();) {
            result = ((IUndoableOperation) iter.next()).hasContext(ctx);
        }

        return result;
    }

    /**
     * I can execute if I am not empty and all of my children can execute.
     */
    public boolean canExecute() {
        boolean result = !isEmpty() && super.canExecute();

        for (Iterator iter = iterator(); result && iter.hasNext();) {
            result = ((IUndoableOperation) iter.next()).canExecute();
        }

        return result;
    }

    /**
     * I can redo if I am not empty and all my children can all be redone.
     */
    public boolean canRedo() {
        boolean result = !isEmpty() && super.canRedo();

        for (Iterator iter = iterator(); result && iter.hasNext();) {
            result = ((IUndoableOperation) iter.next()).canRedo();
        }

        return result;
    }

    /**
     * I can undo if I am not empty and all my children can all be undone.
     */
    public boolean canUndo() {
        boolean result = !isEmpty() && super.canUndo();

        for (Iterator iter = iterator(); result && iter.hasNext();) {
            result = ((IUndoableOperation) iter.next()).canUndo();
        }

        return result;
    }

    /**
     * Disposes of each of my children.
     */
    public void dispose() {

        for (Iterator iter = iterator(); iter.hasNext();) {
            IUndoableOperation nextOperation = (IUndoableOperation) iter.next();
            nextOperation.dispose();
        }
    }

    /**
     * Adds <code>command</code> to the list of commands with which this
     * composite is composed.
     * 
     * @param operation
     *            The command with which to compose this command.
     * @return <code>this</code>.
     */
    public final ICommand compose(IUndoableOperation operation) {

        if (operation != null) {
            add(operation);
        }
        return this;
    }
    
	/**
	 * Appends a command onto a (possibly) existing composeite of commands.
	 * 
	 * @param command an existing command, which may be a composite, a single
	 *     command, or <code>null</code>
	 * @param next a command to append to the composite (may also be
	 *     <code>null</code>, which produces no effect)
	 * 
	 * @return the new composite, which is just <code>next</code> if
	 *     <code>command</code> was <code>null</code>
	 */
	public static ICommand compose(ICommand command, ICommand next) {
		if (command == null) {
			return next;
		} else if (next != null) {
			return command.compose(next);
		} else {
			return command;
		}
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

                if (child instanceof ICommand) {
                    return ((ICommand) child).reduce();
                }
        }
        return this;
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
     * Implements the execution logic by sequential execution of my children.
     */
    protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

        List result = new ArrayList(size());

        progressMonitor.beginTask(getLabel(), size());

        try {

            for (ListIterator iter = listIterator(); iter.hasNext();) {
                IUndoableOperation next = (IUndoableOperation) iter.next();

                try {
                    IStatus status = next.execute(new SubProgressMonitor(
                        progressMonitor, 1), info);
                    result.add(status);
                    int severity = status.getSeverity();

                    if (severity == IStatus.CANCEL || severity == IStatus.ERROR) {

                        // Undo the operation to date, excluding the current
                        // child, and don't proceed
                        Trace
                            .trace(
                                CommonCorePlugin.getDefault(),
                                "Composite operation execution recovery: child command status is CANCEL or ERROR."); //$NON-NLS-1$
                        // back-track over the operation that failed
                        iter.previous();
                        unwindFailedExecute(iter, info);
                        break;

                    } else if (progressMonitor.isCanceled()) {
                        // Undo the operation to date, including the current
                        // child, and don't proceed
                        Trace
                            .trace(CommonCorePlugin.getDefault(),
                                "Composite operation redo recovery: child command monitor is cancelled."); //$NON-NLS-1$

                        CommandResult cancelResult = CommandResult
                            .newCancelledCommandResult();
                        result.add(cancelResult.getStatus());

                        unwindFailedExecute(iter, info);
                        break;

                    } else {
                        progressMonitor.worked(1);
                        executed = true;
                    }

                } catch (ExecutionException e) {
                    // Undo the operation to date, and re-throw the exception
                    // back-track over the operation that failed
                    iter.previous();
                    unwindFailedExecute(iter, info);
                    Trace.throwing(CommonCorePlugin.getDefault(),
                        CommonCoreDebugOptions.EXCEPTIONS_THROWING,
                        CompositeCommand.class, "execute", e); //$NON-NLS-1$
                    throw e;
                }
            }

        } finally {
            progressMonitor.done();
        }

        return new CommandResult(aggregateStatuses(result), getReturnValues());
    }

    /**
     * Undoes the previous operations in the iterator.
     * 
     * @param iter
     *            the execution iterator
     * @param info
     *            the execution info
     */
    private void unwindFailedExecute(ListIterator iter, IAdaptable info) {

        while (iter.hasPrevious()) {
            // unwind the child operations
            IUndoableOperation prev = (IUndoableOperation) iter.previous();
            if (!prev.canUndo()) {
                // Can't unwind
                Log.error(CommonCorePlugin.getDefault(),
                    CommonCoreStatusCodes.EXECUTE_RECOVERY_FAILED,
                    CommonCoreMessages.bind(
                        CommonCoreMessages.executeRecoveryFailed,
                        CommonCoreMessages.cannotUndoExecuted));
                break;
            }

            try {
                prev.undo(new NullProgressMonitor(), info);

            } catch (ExecutionException inner) {
                Log.error(CommonCorePlugin.getDefault(),
                    CommonCoreStatusCodes.EXECUTE_RECOVERY_FAILED,
                    CommonCoreMessages.bind(
                        CommonCoreMessages.executeRecoveryFailed, inner
                            .getLocalizedMessage()));
                break;
            }
        }
    }

    /**
     * I redo by asking my children to redo, in forward order.
     */
    protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {

        final List result = new ArrayList(size());

        progressMonitor.beginTask(getLabel(), size());

        try {

            for (ListIterator iter = listIterator(); iter.hasNext();) {
                IUndoableOperation next = (IUndoableOperation) iter.next();

                try {

                    IStatus status = next.redo(new SubProgressMonitor(
                        progressMonitor, 1), info);
                    result.add(status);
                    int severity = status.getSeverity();

                    if (severity == IStatus.CANCEL || severity == IStatus.ERROR) {
                        // Undo the operation to date, excluding the current
                        // child, and don't proceed
                        Trace
                            .trace(CommonCorePlugin.getDefault(),
                                "Composite operation redo recovery: child command status is CANCEL or ERROR."); //$NON-NLS-1$
                        // back-track over the operation that failed
                        iter.previous();
                        unwindFailedRedo(iter, info);
                        break;

                    } else if (progressMonitor.isCanceled()) {
                        // Undo the operation to date, including the current
                        // child, and don't proceed
                        Trace
                            .trace(CommonCorePlugin.getDefault(),
                                "Composite operation redo recovery: child command monitor is cancelled."); //$NON-NLS-1$

                        CommandResult cancelResult = CommandResult
                            .newCancelledCommandResult();
                        result.add(cancelResult.getStatus());

                        unwindFailedRedo(iter, info);
                        break;

                    } else {
                        progressMonitor.worked(1);
                        executed = true;
                    }

                } catch (ExecutionException e) {
                    // Undo the operation to date, and re-throw the exception
                    // back-track over the operation that failed
                    iter.previous();
                    unwindFailedRedo(iter, info);
                    Trace.throwing(CommonCorePlugin.getDefault(),
                        CommonCoreDebugOptions.EXCEPTIONS_THROWING,
                        CompositeCommand.class, "redo", e); //$NON-NLS-1$
                    throw e;
                }
            }

        } finally {
            progressMonitor.done();
        }

        return new CommandResult(aggregateStatuses(result), getReturnValues());
    }

    /**
     * Undoes the previous operations in the iterator.
     * 
     * @param iter
     *            the execution iterator
     * @param info
     *            the execution info
     */
    private void unwindFailedRedo(ListIterator iter, IAdaptable info) {

        while (iter.hasPrevious()) {
            // unwind the child operations
            IUndoableOperation prev = (IUndoableOperation) iter.previous();
            if (!prev.canUndo()) {
                // Can't unwind
                Log.error(CommonCorePlugin.getDefault(),
                    CommonCoreStatusCodes.REDO_RECOVERY_FAILED,
                    CommonCoreMessages.bind(
                        CommonCoreMessages.redoRecoveryFailed,
                        CommonCoreMessages.cannotUndo));
                break;
            }

            try {
                prev.undo(new NullProgressMonitor(), info);

            } catch (ExecutionException inner) {
                Log.error(CommonCorePlugin.getDefault(),
                    CommonCoreStatusCodes.REDO_RECOVERY_FAILED,
                    CommonCoreMessages.bind(
                        CommonCoreMessages.redoRecoveryFailed, inner
                            .getLocalizedMessage()));
                break;
            }
        }
    }

    /**
     * I undo by asking my children to undo, in reverse order.
     */
    protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {

        final List result = new ArrayList(size());

        progressMonitor.beginTask(getLabel(), size());

        try {

            for (ListIterator iter = listIterator(size()); iter.hasPrevious();) {
                IUndoableOperation prev = (IUndoableOperation) iter.previous();

                try {
                    IStatus status = prev.undo(new SubProgressMonitor(
                        progressMonitor, 1), info);
                    result.add(status);
                    int severity = status.getSeverity();

                    if (severity == IStatus.CANCEL || severity == IStatus.ERROR) {
                        // Redo the operation to date, excluding the current
                        // child, and don't proceed
                        Trace
                            .trace(CommonCorePlugin.getDefault(),
                                "Composite operation undo recovery: child command status is CANCEL or ERROR."); //$NON-NLS-1$
                        // back-track over the operation that failed or was
                        // cancelled
                        iter.next();
                        unwindFailedUndo(iter, info);
                        break;

                    } else if (progressMonitor.isCanceled()) {
                        // Redo the operation to date, including the current
                        // child, and don't proceed
                        Trace
                            .trace(CommonCorePlugin.getDefault(),
                                "Composite operation undo recovery: child command monitor is cancelled."); //$NON-NLS-1$

                        CommandResult cancelResult = CommandResult
                            .newCancelledCommandResult();
                        result.add(cancelResult.getStatus());

                        unwindFailedUndo(iter, info);
                        break;

                    } else {
                        progressMonitor.worked(1);
                        executed = true;
                    }

                } catch (ExecutionException e) {
                    // Redo the operation to date, and re-throw the exception
                    // back-track over the operation that failed
                    iter.next();
                    unwindFailedUndo(iter, info);
                    Trace.throwing(CommonCorePlugin.getDefault(),
                        CommonCoreDebugOptions.EXCEPTIONS_THROWING,
                        CompositeCommand.class, "undo", e); //$NON-NLS-1$
                    throw e;
                }
            }

        } finally {
            progressMonitor.done();
        }

        return new CommandResult(aggregateStatuses(result), getReturnValues());
    }

    /**
     * Redoes the next operations in the iterator.
     * 
     * @param iter
     *            the execution iterator
     * @param info
     *            the execution info
     */
    private void unwindFailedUndo(ListIterator iter, IAdaptable info) {

        while (iter.hasNext()) {
            // unwind the child operations
            IUndoableOperation next = (IUndoableOperation) iter.next();
            if (!next.canRedo()) {
                // Can't unwind
                Log.error(CommonCorePlugin.getDefault(),
                    CommonCoreStatusCodes.UNDO_RECOVERY_FAILED,
                    CommonCoreMessages.bind(
                        CommonCoreMessages.undoRecoveryFailed,
                        CommonCoreMessages.cannotRedo));
                break;
            }

            try {
                next.redo(new NullProgressMonitor(), info);

            } catch (ExecutionException inner) {
                Log.error(CommonCorePlugin.getDefault(),
                    CommonCoreStatusCodes.UNDO_RECOVERY_FAILED,
                    CommonCoreMessages.bind(
                        CommonCoreMessages.undoRecoveryFailed, inner
                            .getLocalizedMessage()));
                break;
            }
        }
    }

    /**
     * Creates a suitable aggregate from these statuses. If there are no
     * statuses to aggregate, then an OK status is returned. If there is a
     * single status to aggregate, then it is returned. Otherwise, a
     * multi-status is returned with the provided statuses as children.
     * 
     * @param statuses
     *            the statuses to aggregate. May have zero, one, or more
     *            elements (all must be {@link IStatus}es)
     * 
     * @return the multi-status
     */
    protected IStatus aggregateStatuses(List statuses) {
        final IStatus result;

        if (statuses.isEmpty()) {
            result = Status.OK_STATUS;
        } else if (statuses.size() == 1) {
            result = ((IStatus) statuses.get(0));
        } else {
            // find the most severe status, to use its plug-in, code, and
            // message
            IStatus[] statusArray = (IStatus[]) statuses
                .toArray(new IStatus[statuses.size()]);

            IStatus worst = statusArray[0];
            for (int i = 1; i < statusArray.length; i++) {
                if (statusArray[i].getSeverity() > worst.getSeverity()) {
                    worst = statusArray[i];
                }
            }

            result = new MultiStatus(worst.getPlugin(), worst.getCode(),
                statusArray, worst.getMessage(), null);
        }

        return result;
    }

    /**
     * Answers whether or not I have been executed.
     * 
     * @return <code>true</code> if I have been executed, <code>false</code>
     *         otherwise.
     */
    protected final boolean isExecuted() {
        return executed;
    }

    /**
     * Asserts that I have not yet been executed. Changes to my children are not
     * permitted after I have been executed.
     */
    protected final void assertNotExecuted() {
        if (isExecuted()) {
            IllegalStateException exc = new IllegalStateException(
                "Operation already executed"); //$NON-NLS-1$
            Trace.throwing(CommonCorePlugin.getDefault(),
                CommonCoreDebugOptions.EXCEPTIONS_THROWING,
                CompositeCommand.class, "assertNotExecuted", exc); //$NON-NLS-1$
            throw exc;
        }
    }

    /**
     * Returns a list containing all of the affected files from
     * <code>ICommand</code> children.
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

    /**
     * Obtains an iterator to traverse my child operations. Removing children
     * via this iterator correctly maintains my undo contexts.
     * 
     * @return an iterator of my children
     */
    public Iterator iterator() {
        return new ChildIterator();
    }

    /**
     * Obtains an iterator to traverse my child operations in either direction.
     * Adding and removing children via this iterator correctly maintains my
     * undo contexts.
     * <p>
     * <b>Note</b> that, unlike list iterators generally, this implementation
     * does not permit the addition of an operation that I already contain (the
     * composite does not permit duplicates). Moreover, only
     * {@link IUndoableOperation}s may be added, otherwise
     * <code>ClassCastException</code>s will result.
     * </p>
     * 
     * @return an iterator of my children
     */
    public ListIterator listIterator() {
        return new ChildListIterator(0);
    }

    /**
     * Obtains an iterator to traverse my child operations in either direction,
     * starting from the specified <code>index</code>. Adding and removing
     * children via this iterator correctly maintains my undo contexts.
     * <p>
     * <b>Note</b> that, unlike list iterators generally, this implementation
     * does not permit the addition of an operation that I already contain (the
     * composite does not permit duplicates). Moreover, only
     * {@link IUndoableOperation}s may be added, otherwise
     * <code>ClassCastException</code>s will result.
     * </p>
     * 
     * @param index
     *            the index in my children at which to start iterating
     * 
     * @return an iterator of my children
     */
    public ListIterator listIterator(int index) {
        return new ChildListIterator(index);
    }

    /**
     * Custom iterator implementation that maintains my undo contexts correctly
     * when elements are removed.
     * 
     * @author ldamus
     */
    private class ChildIterator
        implements Iterator {

        protected Object last;

        protected final ListIterator iter;

        ChildIterator() {
            this(0);
        }

        ChildIterator(int index) {
            iter = getChildren().listIterator(index);
        }

        public void remove() {
            assertNotExecuted();

            iter.remove();
            didRemove((IUndoableOperation) last);
            last = null;
        }

        public Object next() {
            last = iter.next();
            return last;
        }

        public boolean hasNext() {
            return iter.hasNext();
        }
    }

    /**
     * Custom list-iterator implementation that maintains my undo contexts
     * correctly, as well as uniqueness of the list contents.
     * 
     * @author ldamus
     */
    private class ChildListIterator
        extends ChildIterator
        implements ListIterator {

        ChildListIterator(int index) {
            super(index);
        }

        public void add(Object o) {
            assertNotExecuted();

            if (!getChildren().contains(o)) {
                iter.add(o);
                didAdd((IUndoableOperation) o);
            }
        }

        public void set(Object o) {
            assertNotExecuted();

            if (!getChildren().contains(o)) {
                didRemove((IUndoableOperation) last);
                iter.set(o);
                last = o;
                didAdd((IUndoableOperation) o);
            }
        }

        public int previousIndex() {
            return iter.previousIndex();
        }

        public int nextIndex() {
            return iter.nextIndex();
        }

        public Object previous() {
            last = iter.previous();
            return last;
        }

        public boolean hasPrevious() {
            return iter.hasPrevious();
        }
    }
}
