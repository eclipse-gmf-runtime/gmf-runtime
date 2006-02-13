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

package org.eclipse.gmf.tests.runtime.emf.commands.core.command;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;

/**
 * Tests the {@link AbstractTransactionalCommand}.
 * 
 * @author ldamus
 */
public class AbstractTransactionalCommandTest
    extends TestCase {

    private IOperationHistory history;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(AbstractTransactionalCommandTest.class);
    }

    public AbstractTransactionalCommandTest(String name) {
        super(name);
    }

    protected void setUp()
        throws Exception {
        super.setUp();
        history = OperationHistoryFactory.getOperationHistory();
    }

    private List getFiles(String str) {
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IFile[] files = workspaceRoot.findFilesForLocationURI(URI.create(str));
        return Arrays.asList(files);
    }

    /**
     * Tests that commands can be executed, undone and redone through the
     * operation history.
     */
    public void test_execute_undo_redo() {
        String name = "test_execute_undo_redo"; //$NON-NLS-1$
        TestCommand c = new TestCommand(name, null);
        IUndoContext ctx = new ObjectUndoContext(this);

        try {
            c.addContext(ctx);
            history.execute(c, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }

        c.assertExecuted();

        try {
            assertTrue(history.canUndo(ctx));
            history.undo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }

        c.assertUndone();

        try {
            assertTrue(history.canRedo(ctx));
            history.redo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }

        c.assertRedone();
    }

    /**
     * Tests that composing two commands results in a command that combines the
     * contexts and affected files from both children.
     */
    public void test_compose() {
        String name = "test_compose"; //$NON-NLS-1$

        IUndoContext ctx1 = new UndoContext();
        IUndoContext ctx2 = new UndoContext();

        ICommand c1 = new TestCommand(name, getFiles("null:/compose1")); //$NON-NLS-1$
        c1.addContext(ctx1);

        ICommand c2 = new TestCommand(name, getFiles("null:/compose2")); //$NON-NLS-1$
        c2.addContext(ctx2);

        ICommand composition = c1.compose(c2);

        List affectedFiles = composition.getAffectedFiles();
        assertTrue(affectedFiles.containsAll(c1.getAffectedFiles()));
        assertTrue(affectedFiles.containsAll(c2.getAffectedFiles()));
        assertEquals(c1.getAffectedFiles().size()
            + c2.getAffectedFiles().size(), affectedFiles.size());

        List contexts = Arrays.asList(composition.getContexts());
        assertTrue(contexts.contains(ctx1));
        assertTrue(contexts.contains(ctx2));
    }

    /**
     * Tests that the reduction of an AbstractCommand returns itself.
     */
    public void test_reduce() {
        String name = "test_reduce"; //$NON-NLS-1$

        ICommand c = new TestCommand(name, null);
        ICommand reduction = c.reduce();

        assertSame(c, reduction);
    }

    /**
     * Tests that the affected files set in the command constructor are
     * available through the getAffectedFiles() method.
     */
    public void test_getAffectedFiles() {
        String fixtureName = "test_getAffectedFiles"; //$NON-NLS-1$

        // no affected files
        ICommand c = new TestCommand(fixtureName, null);
        assertTrue(c.getAffectedFiles().isEmpty());

        // an affected file
        List affectedFiles = getFiles("null:/AbstractTransactionalCommandTest"); //$NON-NLS-1$

        c = new TestCommand(fixtureName, affectedFiles);

        assertEquals(affectedFiles.size(), c.getAffectedFiles().size());
        assertTrue(c.getAffectedFiles().containsAll(affectedFiles));
    }

    // 
    // TEST FIXTURES
    //

    protected static class TestCommand
        extends AbstractTransactionalCommand {

        private static final String EXECUTED = "executed"; //$NON-NLS-1$

        private boolean executed;

        private boolean undone;

        private boolean redone;

        public TestCommand(String label, List affectedFiles) {
            super(TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain(), label, affectedFiles);
        }

        protected CommandResult doExecuteWithResult(
                IProgressMonitor progressMonitor, IAdaptable info)
            throws ExecutionException {
            executed = true;
            undone = false;
            redone = false;
            return CommandResult.newOKCommandResult(EXECUTED);
        }

        public void assertExecuted() {
            assertTrue(executed);
            assertFalse(undone);
            assertFalse(redone);
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertSame(EXECUTED, getCommandResult().getReturnValue());
        }

        public void assertUndone() {
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertNull(getCommandResult().getReturnValue());
        }

        public void assertRedone() {
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertNull(getCommandResult().getReturnValue());
        }
    }
}
