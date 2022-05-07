/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.command;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.util.Trace;

/**
 * A command does its work once, and can never be undone or redone. It does not do
 * any work that invalidates the undo history in a linear undo model.
 * <P>
 * By default, it is assigned the <code>NULL_CONTEXT</code> when it is
 * constructed, and no other contexts can be added to it or removed from it.
 * <P>
 * This class is meant to be subclassed by clients who have work to do in a
 * command that should have no effect on the operation history.
 * 
 * @author ldamus
 */
public abstract class OneTimeCommand
    extends AbstractCommand {

    /**
     * The null undo context.
     */
    protected final static IUndoContext NULL_CONTEXT = new UndoContext();

    /**
     * Initializes me with a label.
     * 
     * @param label
     *            the operation label
     */
    public OneTimeCommand(String label) {
        this(label, null);
    }

    /**
     * Initializes me with a label and a list of {@link IFile}s that anticipate
     * modifying when I am executed.
     * 
     * @param label
     *            the operation label
     * @param affectedFiles
     *            the list of affected {@link IFile}s; may be <code>null</code>
     */
    public OneTimeCommand(String label, List affectedFiles) {
        super(label, affectedFiles);

        // add a 'null' context to the operation (prevents memory leak in
        // operation history)
        super.addContext(NULL_CONTEXT);
    }

    /**
     * Does nothing. The context will not be added to the operation.
     */
    public final void addContext(IUndoContext context) {
        // ignore the request to add a context
    }

    /**
     * Does nothing. The context will not be removed from the operation.
     */
    public final void removeContext(IUndoContext context) {
        // ignore the request to remove a context
    }

    /**
     * Not undoable. Returns <code>false</code>.
     */
    public final boolean canUndo() {
        return false;
    }

    /**
     * Not redoable. Returns <code>false</code>.
     */
    public final boolean canRedo() {
        return false;
    }

    /**
     * Not undoable. Throws an ExecutionException.
     */
    protected final CommandResult doUndoWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

        ExecutionException e = new ExecutionException("undo not supported"); //$NON-NLS-1$
        Trace.throwing(CommonCorePlugin.getDefault(),
            CommonCoreDebugOptions.EXCEPTIONS_THROWING, getClass(),
            "doUndoWithResult", e); //$NON-NLS-1$

        throw e;
    }

    /**
     * Not redoable. Throws an ExecutionException.
     */
    protected final CommandResult doRedoWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

        ExecutionException e = new ExecutionException("redo not supported"); //$NON-NLS-1$
        Trace.throwing(CommonCorePlugin.getDefault(),
            CommonCoreDebugOptions.EXCEPTIONS_THROWING, getClass(),
            "doRedoWithResult", e); //$NON-NLS-1$

        throw e;
    }
}
