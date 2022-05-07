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

package org.eclipse.gmf.tests.runtime.common.core.internal.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Tests the {@link AbstractCommand}.
 * 
 * @author ldamus
 */
public class CompositeCommandTest
    extends TestCase {

    private IOperationHistory history;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(CompositeCommandTest.class);
    }

    public CompositeCommandTest(String name) {
        super(name);
    }

    protected void setUp()
        throws Exception {
        super.setUp();
        history = OperationHistoryFactory.getOperationHistory();
    }

    /**
     * Records a failure due to an exception that should not have been thrown.
     * 
     * @param e
     *            the exception
     */
    protected void fail(Exception e) {
        e.printStackTrace();
        fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }

    /**
     * Tests that the undo contexts of the composite correctly aggregate the
     * contexts of the children that it contains.
     */
    public void test_contexts() {
        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        IUndoContext ctx1 = new UndoContext();
        IUndoContext ctx2 = new UndoContext();
        IUndoContext ctx3 = new UndoContext();

        ICommand child1 = new TestCommand();
        ICommand child2 = new TestCommand();
        ICommand child3 = new TestCommand();

        // configure some contexts
        child1.addContext(ctx1);
        child2.addContext(ctx2);
        child2.addContext(ctx1);
        child3.addContext(ctx3);

        // no contexts, yet
        assertEquals(Collections.EMPTY_LIST, Arrays.asList(composite
            .getContexts()));

        composite.add(child1);
        assertEquals(Arrays.asList(new IUndoContext[] {ctx1}), Arrays
            .asList(composite.getContexts()));

        // note that we don't get ctx1 twice
        composite.add(child2);
        assertEquals(Arrays.asList(new IUndoContext[] {ctx1, ctx2}), Arrays
            .asList(composite.getContexts()));

        composite.add(child3);
        assertEquals(Arrays.asList(new IUndoContext[] {ctx1, ctx2, ctx3}),
            Arrays.asList(composite.getContexts()));

        // still have ctx1, but not ctx2
        composite.remove(child2);
        assertEquals(Arrays.asList(new IUndoContext[] {ctx1, ctx3}), Arrays
            .asList(composite.getContexts()));

        composite.remove(child1);
        assertEquals(Arrays.asList(new IUndoContext[] {ctx3}), Arrays
            .asList(composite.getContexts()));

        composite.remove(child3);
        assertEquals(Collections.EMPTY_LIST, Arrays.asList(composite
            .getContexts()));
    }

    /**
     * Tests that the undo contexts of the composite correctly aggregate the
     * contexts of the children that it contains, when manipulating the children
     * using a list iterator.
     */
    public void test_contexts_listIterator() {
        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        IUndoContext ctx1 = new UndoContext();
        IUndoContext ctx2 = new UndoContext();
        IUndoContext ctx3 = new UndoContext();

        ICommand child1 = new TestCommand();
        ICommand child2 = new TestCommand();
        ICommand child3 = new TestCommand();

        // configure some contexts
        child1.addContext(ctx1);
        child2.addContext(ctx2);
        child2.addContext(ctx1);
        child3.addContext(ctx3);

        ListIterator iter = composite.listIterator();

        // no contexts, yet
        assertEquals(Collections.EMPTY_LIST, Arrays.asList(composite
            .getContexts()));

        iter.add(child1);
        assertEquals(Arrays.asList(new IUndoContext[] {ctx1}), Arrays
            .asList(composite.getContexts()));

        // note that we don't get ctx1 twice
        iter.add(child2);
        assertEquals(Arrays.asList(new IUndoContext[] {ctx1, ctx2}), Arrays
            .asList(composite.getContexts()));

        iter.add(child3);
        assertEquals(Arrays.asList(new IUndoContext[] {ctx1, ctx2, ctx3}),
            Arrays.asList(composite.getContexts()));

        // still have ctx1, but not ctx2 when we remove child2
        iter.previous();
        iter.previous();
        iter.remove();
        assertEquals(Arrays.asList(new IUndoContext[] {ctx1, ctx3}), Arrays
            .asList(composite.getContexts()));

        // removing child1
        iter.previous();
        iter.remove();
        assertEquals(Arrays.asList(new IUndoContext[] {ctx3}), Arrays
            .asList(composite.getContexts()));

        // removing child3
        iter.next();
        iter.remove();
        assertEquals(Collections.EMPTY_LIST, Arrays.asList(composite
            .getContexts()));
    }

    /**
     * Tests the aggregation of canUndo() from child operations.
     */
    public void test_canUndo() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$
        CompositeCommand composite2 = new CompositeCommand("Composite"); //$NON-NLS-1$

        composite.add(new TestCommand());
        composite.add(new TestCommand());
        composite.add(composite2);
        composite.add(new TestCommand());

        composite2.add(new TestCommand());
        composite2.add(new TestCommand(true, false)); // can't undo this one
        composite2.add(new TestCommand());

        composite.addContext(ctx);
        assertTrue(composite.canExecute());

        try {
            history.execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        assertFalse(composite.canUndo());
        assertFalse(history.canUndo(ctx));
    }

    /**
     * Tests the aggregation of canRedo() from child operations.
     */
    public void test_canRedo() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$
        CompositeCommand composite2 = new CompositeCommand("Composite"); //$NON-NLS-1$

        composite.add(new TestCommand());
        composite.add(new TestCommand());
        composite.add(composite2);
        composite.add(new TestCommand());

        composite2.add(new TestCommand());
        composite2.add(new TestCommand(true, true, false)); // can undo but not
        // redo
        composite2.add(new TestCommand());

        composite.addContext(ctx);
        assertTrue(composite.canExecute());

        try {
            history.execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        assertTrue(composite.canUndo());
        assertTrue(history.canUndo(ctx));

        try {
            history.undo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        assertFalse(composite.canRedo());
        assertFalse(history.canRedo(ctx));
    }

    public void test_execute_undo_redo() {

        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        TestCommand child1 = new TestCommand();
        TestCommand child2 = new TestCommand();
        TestCommand child3 = new TestCommand();

        composite.add(child1);
        composite.add(child2);
        composite.add(child3);

        try {
            composite.addContext(ctx);
            history.execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        child1.assertExecuted();
        child2.assertExecuted();
        child3.assertExecuted();

        CommandResult result = composite.getCommandResult();

        IStatus status = result.getStatus();
        assertTrue(status.isOK());

        Object returnValue = result.getReturnValue();
        assertTrue(returnValue instanceof Collection);

        Collection collection = (Collection) returnValue;
        assertEquals(3, collection.size());
        assertTrue(collection.contains(child1.getCommandResult()
            .getReturnValue()));
        assertTrue(collection.contains(child2.getCommandResult()
            .getReturnValue()));
        assertTrue(collection.contains(child3.getCommandResult()
            .getReturnValue()));

        try {
            assertTrue(history.canUndo(ctx));
            history.undo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        child1.assertUndone();
        child2.assertUndone();
        child3.assertUndone();

        result = composite.getCommandResult();

        status = result.getStatus();
        assertTrue(status.isOK());

        returnValue = result.getReturnValue();
        assertTrue(returnValue instanceof Collection);

        collection = (Collection) returnValue;
        assertEquals(3, collection.size());
        assertTrue(collection.contains(child1.getCommandResult()
            .getReturnValue()));
        assertTrue(collection.contains(child2.getCommandResult()
            .getReturnValue()));
        assertTrue(collection.contains(child3.getCommandResult()
            .getReturnValue()));

        try {
            assertTrue(history.canRedo(ctx));
            history.redo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        child1.assertRedone();
        child2.assertRedone();
        child3.assertRedone();

        result = composite.getCommandResult();

        status = result.getStatus();
        assertTrue(status.isOK());

        returnValue = result.getReturnValue();
        assertTrue(returnValue instanceof Collection);

        collection = (Collection) returnValue;
        assertEquals(3, collection.size());
        assertTrue(collection.contains(child1.getCommandResult()
            .getReturnValue()));
        assertTrue(collection.contains(child2.getCommandResult()
            .getReturnValue()));
        assertTrue(collection.contains(child3.getCommandResult()
            .getReturnValue()));
    }

    /**
     * Tests error detection during execution.
     */
    public void test_execute_error() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        MarkerOperation marker1 = new MarkerOperation();
        composite.add(marker1);

        FailCancelOperation c = new FailCancelOperation(ERROR_STATUS,
            Status.OK_STATUS, Status.OK_STATUS, false);
        composite.add(c);

        MarkerOperation marker2 = new MarkerOperation();
        composite.add(marker2);

        IStatus status = null;

        try {
            composite.addContext(ctx);
            status = history
                .execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were executed and which rolled back
        assertTrue(marker1.wasExecuted);
        assertFalse(marker2.wasExecuted);
        assertTrue(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.ERROR, status.getSeverity());
    }

    /**
     * Tests cancel-status detection during execution.
     */
    public void test_execute_cancel() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        MarkerOperation marker1 = new MarkerOperation();
        composite.add(marker1);

        FailCancelOperation op = new FailCancelOperation(Status.CANCEL_STATUS,
            Status.OK_STATUS, Status.OK_STATUS, false);
        composite.add(op);

        MarkerOperation marker2 = new MarkerOperation();
        composite.add(marker2);

        IStatus status = null;

        try {
            composite.addContext(ctx);
            status = history
                .execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were executed and which rolled back
        assertTrue(marker1.wasExecuted);
        assertFalse(marker2.wasExecuted);
        assertTrue(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.CANCEL, status.getSeverity());
    }

    /**
     * Tests monitor-cancel detection during execution.
     */
    public void test_execute_cancelMonitor() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        MarkerOperation marker1 = new MarkerOperation();
        composite.add(marker1);

        FailCancelOperation op = new FailCancelOperation(Status.CANCEL_STATUS,
            Status.OK_STATUS, Status.OK_STATUS, true);
        composite.add(op);

        MarkerOperation marker2 = new MarkerOperation();
        composite.add(marker2);

        IStatus status = null;

        try {
            composite.addContext(ctx);
            status = history
                .execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were executed and which rolled back
        assertTrue(marker1.wasExecuted);
        assertFalse(marker2.wasExecuted);
        assertTrue(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.CANCEL, status.getSeverity());
    }

    /**
     * Tests error detection during undo.
     */
    public void test_undo_error() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        MarkerOperation marker1 = new MarkerOperation();
        composite.add(marker1);

        FailCancelOperation op = new FailCancelOperation(Status.OK_STATUS,
            ERROR_STATUS, Status.OK_STATUS, false);
        composite.add(op);

        MarkerOperation marker2 = new MarkerOperation();
        composite.add(marker2);

        IStatus status = null;

        try {
            composite.addContext(ctx);
            status = history
                .execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were executed and which rolled back
        assertTrue(marker1.wasExecuted);
        assertTrue(marker2.wasExecuted);
        assertFalse(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.OK, status.getSeverity());

        marker1.reset();
        marker2.reset();

        try {
            status = history.undo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were undone and which were redone
        assertFalse(marker1.wasUndone);
        assertTrue(marker2.wasUndone);
        assertTrue(marker2.wasRedone);

        // check overall operation status
        assertEquals(IStatus.ERROR, status.getSeverity());
    }

    /**
     * Tests cancel-status detection during undo.
     */
    public void test_undo_cancel() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        MarkerOperation marker1 = new MarkerOperation();
        composite.add(marker1);

        FailCancelOperation op = new FailCancelOperation(Status.OK_STATUS,
            Status.CANCEL_STATUS, Status.OK_STATUS, false);
        composite.add(op);

        MarkerOperation marker2 = new MarkerOperation();
        composite.add(marker2);

        IStatus status = null;

        try {
            composite.addContext(ctx);
            status = history
                .execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were executed and which rolled back
        assertTrue(marker1.wasExecuted);
        assertTrue(marker2.wasExecuted);
        assertFalse(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.OK, status.getSeverity());

        marker1.reset();
        marker2.reset();

        try {
            status = history.undo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were undone and which were redone
        assertFalse(marker1.wasUndone);
        assertTrue(marker2.wasUndone);
        assertTrue(marker2.wasRedone);

        // check overall operation status
        assertEquals(IStatus.CANCEL, status.getSeverity());
    }

    /**
     * Tests monitor-cancel detection during undo.
     */
    public void test_undo_cancelMonitor() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        MarkerOperation marker1 = new MarkerOperation();
        composite.add(marker1);

        FailCancelOperation op = new FailCancelOperation(Status.OK_STATUS,
            Status.CANCEL_STATUS, Status.OK_STATUS, true);
        composite.add(op);

        MarkerOperation marker2 = new MarkerOperation();
        composite.add(marker2);

        IStatus status = null;

        try {
            composite.addContext(ctx);
            status = history
                .execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were executed and which rolled back
        assertTrue(marker1.wasExecuted);
        assertTrue(marker2.wasExecuted);
        assertFalse(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.OK, status.getSeverity());

        marker1.reset();
        marker2.reset();

        try {
            status = history.undo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were undone and which were redone
        assertFalse(marker1.wasUndone);
        assertTrue(marker2.wasUndone);
        assertTrue(marker2.wasRedone);

        // check overall operation status
        assertEquals(IStatus.CANCEL, status.getSeverity());
    }

    /**
     * Tests error detection during redo.
     */
    public void test_redo_error() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        MarkerOperation marker1 = new MarkerOperation();
        composite.add(marker1);

        FailCancelOperation op = new FailCancelOperation(Status.OK_STATUS,
            Status.OK_STATUS, ERROR_STATUS, false);
        composite.add(op);

        MarkerOperation marker2 = new MarkerOperation();
        composite.add(marker2);

        IStatus status = null;

        try {
            composite.addContext(ctx);
            status = history
                .execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were executed and which rolled back
        assertTrue(marker1.wasExecuted);
        assertTrue(marker2.wasExecuted);
        assertFalse(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.OK, status.getSeverity());

        marker1.reset();
        marker2.reset();

        try {
            status = history.undo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were undone and which were redone
        assertTrue(marker1.wasUndone);
        assertTrue(marker2.wasUndone);
        assertFalse(marker2.wasRedone);

        // check overall operation status
        assertEquals(IStatus.OK, status.getSeverity());

        marker1.reset();
        marker2.reset();

        try {
            status = history.redo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were undone and which were redone
        assertTrue(marker1.wasRedone);
        assertFalse(marker2.wasRedone);
        assertTrue(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.ERROR, status.getSeverity());
    }

    /**
     * Tests cancel-status detection during redo.
     */
    public void test_redo_cancel() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        MarkerOperation marker1 = new MarkerOperation();
        composite.add(marker1);

        FailCancelOperation op = new FailCancelOperation(Status.OK_STATUS,
            Status.OK_STATUS, Status.CANCEL_STATUS, false);
        composite.add(op);

        MarkerOperation marker2 = new MarkerOperation();
        composite.add(marker2);

        IStatus status = null;

        try {
            composite.addContext(ctx);
            status = history
                .execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were executed and which rolled back
        assertTrue(marker1.wasExecuted);
        assertTrue(marker2.wasExecuted);
        assertFalse(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.OK, status.getSeverity());

        marker1.reset();
        marker2.reset();

        try {
            status = history.undo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were undone and which were redone
        assertTrue(marker1.wasUndone);
        assertTrue(marker2.wasUndone);
        assertFalse(marker2.wasRedone);

        // check overall operation status
        assertEquals(IStatus.OK, status.getSeverity());

        marker1.reset();
        marker2.reset();

        try {
            status = history.redo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were undone and which were redone
        assertTrue(marker1.wasRedone);
        assertFalse(marker2.wasRedone);
        assertTrue(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.CANCEL, status.getSeverity());
    }

    /**
     * Tests monitor-cancel detection during redo.
     */
    public void test_redo_cancelMonitor() {
        IUndoContext ctx = new UndoContext();

        CompositeCommand composite = new CompositeCommand("Composite"); //$NON-NLS-1$

        MarkerOperation marker1 = new MarkerOperation();
        composite.add(marker1);

        FailCancelOperation op = new FailCancelOperation(Status.OK_STATUS,
            Status.OK_STATUS, Status.CANCEL_STATUS, true);
        composite.add(op);

        MarkerOperation marker2 = new MarkerOperation();
        composite.add(marker2);

        IStatus status = null;

        try {
            composite.addContext(ctx);
            status = history
                .execute(composite, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were executed and which rolled back
        assertTrue(marker1.wasExecuted);
        assertTrue(marker2.wasExecuted);
        assertFalse(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.OK, status.getSeverity());

        marker1.reset();
        marker2.reset();

        try {
            status = history.undo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were undone and which were redone
        assertTrue(marker1.wasUndone);
        assertTrue(marker2.wasUndone);
        assertFalse(marker2.wasRedone);

        // check overall operation status
        assertEquals(IStatus.OK, status.getSeverity());

        marker1.reset();
        marker2.reset();

        try {
            status = history.redo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e);
        }

        // check which markers were undone and which were redone
        assertTrue(marker1.wasRedone);
        assertFalse(marker2.wasRedone);
        assertTrue(marker1.wasUndone);

        // check overall operation status
        assertEquals(IStatus.CANCEL, status.getSeverity());
    }

    //
    // TEST FIXTURES
    //

    private static IStatus ERROR_STATUS = new Status(IStatus.ERROR,
        "error", 1, "no message", null); //$NON-NLS-1$ //$NON-NLS-2$

    protected static class TestCommand
        extends AbstractCommand {

        private boolean executed;

        private boolean undone;

        private boolean redone;

        private final boolean isExecutable;

        private final boolean isUndoable;

        private final boolean isRedoable;

        public TestCommand() {
            this(true, true, true, null);
        }

        public TestCommand(List affectedFiles) {
            this(true, true, true, affectedFiles);
        }

        public TestCommand(boolean isExecutable) {
            this(isExecutable, true, true, null);
        }

        public TestCommand(boolean isExecutable, boolean isUndoable) {
            this(isExecutable, isUndoable, true, null);
        }

        public TestCommand(boolean isExecutable, boolean isUndoable,
                boolean isRedoable) {
            this(isExecutable, isUndoable, isRedoable, null);
        }

        public TestCommand(boolean isExecutable, boolean isUndoable,
                boolean isRedoable, List affectedFiles) {
            super("TestCommand", affectedFiles); //$NON-NLS-1$

            this.isExecutable = isExecutable;
            this.isUndoable = isUndoable;
            this.isRedoable = isRedoable;
        }

        public boolean canExecute() {
            return isExecutable;
        }

        public boolean canUndo() {
            return isUndoable;
        }

        public boolean canRedo() {
            return isRedoable;
        }

        protected CommandResult doExecuteWithResult(
                IProgressMonitor progressMonitor, IAdaptable info)
            throws ExecutionException {
            executed = true;
            undone = false;
            redone = false;
            return CommandResult.newOKCommandResult(this);
        }

        protected CommandResult doRedoWithResult(
                IProgressMonitor progressMonitor, IAdaptable info)
            throws ExecutionException {
            executed = false;
            undone = false;
            redone = true;
            return CommandResult.newOKCommandResult(this);
        }

        protected CommandResult doUndoWithResult(
                IProgressMonitor progressMonitor, IAdaptable info)
            throws ExecutionException {
            executed = false;
            undone = true;
            redone = false;
            return CommandResult.newOKCommandResult(this);
        }

        public void assertExecuted() {
            assertTrue(executed);
            assertFalse(undone);
            assertFalse(redone);
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertSame(this, getCommandResult().getReturnValue());
        }

        public void assertUndone() {
            assertTrue(undone);
            assertFalse(executed);
            assertFalse(redone);
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertSame(this, getCommandResult().getReturnValue());
        }

        public void assertRedone() {
            assertTrue(redone);
            assertFalse(undone);
            assertFalse(executed);
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertSame(this, getCommandResult().getReturnValue());
        }

    }

    private static class FailCancelOperation
        extends AbstractOperation {

        private IStatus executeStatus;

        private IStatus undoStatus;

        private IStatus redoStatus;

        private boolean cancelMonitor;

        FailCancelOperation(IStatus exec, IStatus undo, IStatus redo,
                boolean cancel) {
            super("Fail/Cancel Operation"); //$NON-NLS-1$
            this.executeStatus = exec;
            this.undoStatus = undo;
            this.redoStatus = redo;
            this.cancelMonitor = cancel;
        }

        public IStatus execute(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
            if ((executeStatus.getSeverity() == IStatus.CANCEL)
                && cancelMonitor) {
                monitor.setCanceled(true);
                return Status.OK_STATUS;
            }

            return executeStatus;
        }

        public IStatus undo(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
            if ((undoStatus.getSeverity() == IStatus.CANCEL) && cancelMonitor) {
                monitor.setCanceled(true);
                return Status.OK_STATUS;
            }

            return undoStatus;
        }

        public IStatus redo(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
            if ((redoStatus.getSeverity() == IStatus.CANCEL) && cancelMonitor) {
                monitor.setCanceled(true);
                return Status.OK_STATUS;
            }

            return redoStatus;
        }
    }

    static class MarkerOperation
        extends AbstractOperation {

        boolean wasExecuted;

        boolean wasUndone;

        boolean wasRedone;

        MarkerOperation() {
            super("Marker operation"); //$NON-NLS-1$
        }

        void reset() {
            wasExecuted = false;
            wasUndone = false;
            wasRedone = false;
        }

        public IStatus execute(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
            wasExecuted = true;
            return Status.OK_STATUS;
        }

        public IStatus undo(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
            wasUndone = true;
            return Status.OK_STATUS;
        }

        public IStatus redo(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
            wasRedone = true;
            return Status.OK_STATUS;
        }
    }
}
