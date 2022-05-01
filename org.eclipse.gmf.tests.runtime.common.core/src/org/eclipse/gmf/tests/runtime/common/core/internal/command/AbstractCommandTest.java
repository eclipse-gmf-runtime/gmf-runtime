/******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
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
public class AbstractCommandTest
    extends TestCase {

    private IOperationHistory history;
    private IProject project;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(AbstractCommandTest.class);
    }

    public AbstractCommandTest(String name) {
        super(name);
    }

    protected void setUp()
        throws Exception {
        super.setUp();

        history = OperationHistoryFactory.getOperationHistory();

        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        project = root.getProject("AbstractCommandTest"); //$NON-NLS-1$
        project.create(null);
        project.open(null);
    }

    protected void tearDown()
        throws Exception {
        super.tearDown();

        project.close(new NullProgressMonitor());
        project.delete(true, true, new NullProgressMonitor());
        project = null;
        history = null;
    }

    private List getFiles(String str) {
        IFile file = project.getFile(str);
        return Collections.singletonList(file);
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

        ICommand c1 = new TestCommand(name, getFiles("compose1")); //$NON-NLS-1$
        c1.addContext(ctx1);

        ICommand c2 = new TestCommand(name, getFiles("compose2")); //$NON-NLS-1$
        c2.addContext(ctx2);

        List threeFiles = new ArrayList(3);
        threeFiles.addAll(c1.getAffectedFiles());
        threeFiles.addAll(c2.getAffectedFiles());
        threeFiles.add(getFiles("compose3")); //$NON-NLS-1$
        ICommand c3 = new TestCommand(name, threeFiles);

        ICommand composition = c1.compose(c2).compose(c3);

        List affectedFiles = composition.getAffectedFiles();
        assertTrue(affectedFiles.containsAll(c1.getAffectedFiles()));
        assertTrue(affectedFiles.containsAll(c2.getAffectedFiles()));
        
        // should be no duplicates
        assertEquals(3, affectedFiles.size());

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
        List affectedFiles = getFiles("null:/AbstractCommandTest"); //$NON-NLS-1$

        c = new TestCommand(fixtureName, affectedFiles);

        assertEquals(affectedFiles.size(), c.getAffectedFiles().size());
        assertTrue(c.getAffectedFiles().containsAll(affectedFiles));
    }
    
    /**
	 * Verifies that no exceptions are thrown when a command is executed, undone
	 * or redone that has no command result.
	 */
    public void test_noCommandResult_146064() {
    	 String name = "test_noCommandResult_146064"; //$NON-NLS-1$
         TestCommand c = new TestCommandNoResults(name, null);
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
	 * Verifies that subclasses of AbstractCommand don't need to check whether
	 * or not the progress monitor input to #doExecute, #doUndo and #doRedo is
	 * null.
	 */
	public void test_nullMonitor_149057() {
		TestCommandWithProgress c = new TestCommandWithProgress(
				"test_nullMonitor_149057");  //$NON-NLS-1$
		
		IUndoContext ctx = new ObjectUndoContext(this);

        try {
            c.addContext(ctx);
            history.execute(c, null, null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Should not have thrown: " + e.getCause()); //$NON-NLS-1$
        }

        c.assertExecuted();

        try {
            assertTrue(history.canUndo(ctx));
            history.undo(ctx, null, null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Should not have thrown: " + e.getCause()); //$NON-NLS-1$
        }

        c.assertUndone();

        try {
            assertTrue(history.canRedo(ctx));
            history.redo(ctx, null, null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Should not have thrown: " + e.getCause()); //$NON-NLS-1$
        }

        c.assertRedone();
	}

    // 
    // TEST FIXTURES
    //

    protected static class TestCommand
        extends AbstractCommand {

        private static final String EXECUTED = "executed"; //$NON-NLS-1$

        private static final String UNDONE = "undone"; //$NON-NLS-1$

        private static final String REDONE = "redone"; //$NON-NLS-1$

        protected boolean executed;

        protected boolean undone;

        protected boolean redone;

        public TestCommand(String label, List affectedFiles) {
            super(label, affectedFiles);
        }

        protected CommandResult doExecuteWithResult(
                IProgressMonitor progressMonitor, IAdaptable info)
            throws ExecutionException {
            executed = true;
            undone = false;
            redone = false;
            return CommandResult.newOKCommandResult(EXECUTED);
        }

        protected CommandResult doRedoWithResult(
                IProgressMonitor progressMonitor, IAdaptable info)
            throws ExecutionException {
            executed = false;
            undone = false;
            redone = true;
            return CommandResult.newOKCommandResult(REDONE);
        }

        protected CommandResult doUndoWithResult(
                IProgressMonitor progressMonitor, IAdaptable info)
            throws ExecutionException {
            executed = false;
            undone = true;
            redone = false;
            return CommandResult.newOKCommandResult(UNDONE);
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
            assertTrue(undone);
            assertFalse(executed);
            assertFalse(redone);
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertSame(UNDONE, getCommandResult().getReturnValue());
        }

        public void assertRedone() {
            assertTrue(redone);
            assertFalse(undone);
            assertFalse(executed);
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertSame(REDONE, getCommandResult().getReturnValue());
        }
    }
    
    protected static class TestCommandNoResults extends TestCommand {

		public TestCommandNoResults(String label, List affectedFiles) {
			super(label, affectedFiles);
		}

		protected CommandResult doExecuteWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {

			super.doExecuteWithResult(progressMonitor, info);
			return null;
		}

		protected CommandResult doRedoWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {
			super.doRedoWithResult(progressMonitor, info);
			return null;
		}

		protected CommandResult doUndoWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {
			super.doUndoWithResult(progressMonitor, info);
			return null;
		}
		
        public void assertExecuted() {
            assertTrue(executed);
            assertFalse(undone);
            assertFalse(redone);
            assertNull(getCommandResult());
        }

        public void assertUndone() {
            assertTrue(undone);
            assertFalse(executed);
            assertFalse(redone);
            assertNull(getCommandResult());
        }

        public void assertRedone() {
            assertTrue(redone);
            assertFalse(undone);
            assertFalse(executed);
            assertNull(getCommandResult());
        }
	}
    
    protected static class TestCommandWithProgress extends TestCommand {

		public TestCommandWithProgress(String label) {
			super(label, null);
		}

		protected CommandResult doExecuteWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {
			progressMonitor.worked(1);
			return super.doExecuteWithResult(progressMonitor, info);
		}

		protected CommandResult doRedoWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {
			progressMonitor.worked(1);
			return super.doRedoWithResult(progressMonitor, info);
		}

		protected CommandResult doUndoWithResult(
				IProgressMonitor progressMonitor, IAdaptable info)
				throws ExecutionException {
			progressMonitor.worked(1);
			return super.doUndoWithResult(progressMonitor, info);
		}
	}

}
