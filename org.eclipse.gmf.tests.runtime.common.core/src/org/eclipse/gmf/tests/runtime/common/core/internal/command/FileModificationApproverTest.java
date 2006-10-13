/******************************************************************************
 * Copyright (c) 2006, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.core.internal.command;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.internal.command.FileModificationApprover;

/**
 * Tests the {@link FileModificationApprover}.
 * 
 * @author ldamus
 */
public class FileModificationApproverTest
    extends TestCase {

    private IOperationHistory history;
    private IProject project;
    private IFile file;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(FileModificationApproverTest.class);
    }

    public FileModificationApproverTest(String name) {
        super(name);
    }

    protected void setUp()
        throws Exception {

        super.setUp();

        history = OperationHistoryFactory.getOperationHistory();

        try {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            project = root.getProject("FileModificationApproverTest"); //$NON-NLS-1$

            project.create(null);
            project.open(null);

            file = project.getFile("test.txt"); //$NON-NLS-1$
            InputStream contents = new ByteArrayInputStream(new byte[0]);

            file.create(contents, false, new NullProgressMonitor());

        } catch (CoreException e) {
            fail(e);
        }
    }
    
    protected void tearDown()
        throws Exception {
        
        super.tearDown();
        
        file.delete(true, new NullProgressMonitor());
        project.close(new NullProgressMonitor());
        project.delete(true, true, new NullProgressMonitor());
        project = null;
        file = null;
        history = null;
    }
    
    private void fail(Exception e) {
        e.printStackTrace();
        fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }

    /**
     * Tests that commands that affect read-only files cannot be executed, undone and redone through the
     * operation history.
     */
    public void test_execute_undo_redo() {
        
        TestCommand c = new TestCommand("test_execute_undo_redo", Collections.singletonList(file)); //$NON-NLS-1$
        IUndoContext ctx = new UndoContext();
        c.addContext(ctx);
        

        ResourceAttributes attributes = file.getResourceAttributes();

        
        // Execute fails when file is read-only
        try {
            attributes.setReadOnly(true);
            file.setResourceAttributes(attributes);
            
            history.execute(c, new NullProgressMonitor(), null);
            
        } catch (CoreException e) {
            fail(e);
        } catch (ExecutionException e) {
            fail(e);
        }

        c.assertNotExecuted();
        c.reset();
        
        // Execute succeeds when file is writable
        try {
            attributes.setReadOnly(false);
            file.setResourceAttributes(attributes);
            
            history.execute(c, new NullProgressMonitor(), null);
            
        } catch (CoreException e) {
            fail(e);
        } catch (ExecutionException e) {
            fail(e);
        }

        c.assertExecuted();
        c.reset();
        
        // Undo fails when file is read-only
        try {
            attributes.setReadOnly(true);
            file.setResourceAttributes(attributes);
            
            assertTrue(history.canUndo(ctx));
            
            history.undo(ctx, new NullProgressMonitor(), null);
            
        } catch (CoreException e) {
            fail(e);
        } catch (ExecutionException e) {
            fail(e);
        }

        c.assertNotUndone();
        c.reset();
        
        // Undo succeeds when file is writable
        try {
            attributes.setReadOnly(false);
            file.setResourceAttributes(attributes);
            
            assertTrue(history.canUndo(ctx));
            
            history.undo(ctx, new NullProgressMonitor(), null);
            
        } catch (CoreException e) {
            fail(e);
        } catch (ExecutionException e) {
            fail(e);
        }

        c.assertUndone();
        c.reset();
        
        // Redo fails when file is read-only
        try {
            attributes.setReadOnly(true);
            file.setResourceAttributes(attributes);
            
            assertTrue(history.canRedo(ctx));
            
            history.redo(ctx, new NullProgressMonitor(), null);
            
        } catch (CoreException e) {
            fail(e);
        } catch (ExecutionException e) {
            fail(e);
        }

        c.assertNotRedone();
        c.reset();
        
        
        // Redo succeeds when file is writable
        try {
            attributes.setReadOnly(false);
            file.setResourceAttributes(attributes);
            
            assertTrue(history.canRedo(ctx));
            
            history.redo(ctx, new NullProgressMonitor(), null);
            
        } catch (CoreException e) {
            fail(e);
        } catch (ExecutionException e) {
            fail(e);
        }

        c.assertRedone();
        c.reset();
 
    }

    // 
    // TEST FIXTURES
    //

    protected static class TestCommand
        extends AbstractCommand {

        private static final String EXECUTED = "executed"; //$NON-NLS-1$

        private static final String UNDONE = "undone"; //$NON-NLS-1$

        private static final String REDONE = "redone"; //$NON-NLS-1$

        private boolean executed;

        private boolean undone;

        private boolean redone;

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
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertSame(EXECUTED, getCommandResult().getReturnValue());
        }
        
        public void assertNotExecuted() {
            assertFalse(executed);
            //our validator should have marked it invalid
            assertFalse(getCommandResult().getStatus().isOK());
        }

        public void assertUndone() {
            assertTrue(undone);
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertSame(UNDONE, getCommandResult().getReturnValue());
        }
        
        public void assertNotUndone() {
            assertFalse(undone);
            //our validator should have marked it invalid
            assertFalse(getCommandResult().getStatus().isOK());
        }

        public void assertRedone() {
            assertTrue(redone);
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            assertSame(REDONE, getCommandResult().getReturnValue());
        }
        
        public void assertNotRedone() {
            assertFalse(redone);
            //our validator should have marked it invalid
            assertFalse(getCommandResult().getStatus().isOK());
        }
        
        public void reset() {
            executed = false;
            undone = false;
            redone = false;
            setResult(null);
        }
    }
}
