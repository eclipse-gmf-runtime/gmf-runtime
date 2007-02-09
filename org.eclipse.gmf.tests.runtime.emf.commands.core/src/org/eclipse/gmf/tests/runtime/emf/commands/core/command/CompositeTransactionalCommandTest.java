/******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.commands.core.command;

import java.util.Arrays;
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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;

/**
 * Tests the {@link CompositeTransactionalCommand}.
 * 
 * @author ldamus
 */
public class CompositeTransactionalCommandTest
    extends TestCase {

    private IOperationHistory history;
    private IProject project;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(CompositeTransactionalCommandTest.class);
    }

    public CompositeTransactionalCommandTest(String name) {
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

        TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE
            .createEditingDomain();

        // create a resource to modify
        Resource res = new ResourceImpl(org.eclipse.emf.common.util.URI.createURI("file:/foo")); //$NON-NLS-1$
        domain.getResourceSet().getResources().add(res);
        
        TransactionalTestCommand child1 = new TransactionalTestCommand(domain, res);
        TestCommand child2 = new TestCommand();
        TransactionalTestCommand child3 = new TransactionalTestCommand(domain, res);

        IUndoContext ctx = new UndoContext();

        CompositeTransactionalCommand c = new CompositeTransactionalCommand(
            domain, "test_execute_undo_redo"); //$NON-NLS-1$

        c.add(child1);
        c.add(child2);
        c.add(child3);

        try {
            c.addContext(ctx);
            history.execute(c, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }

        child1.assertExecuted();
        child2.assertExecuted();
        child3.assertExecuted();

        try {
            assertTrue(history.canUndo(ctx));
            history.undo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }

        child1.assertUndone();
        child2.assertUndone();
        child3.assertUndone();

        try {
            assertTrue(history.canRedo(ctx));
            history.redo(ctx, new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }

        child1.assertRedone();
        child2.assertRedone();
        child3.assertRedone();
    }

    /**
     * Tests that composing results in the same command that combines the
     * contexts and affected files from both children.
     */
    public void test_compose() {

        TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE
            .createEditingDomain();

        CompositeTransactionalCommand c = new CompositeTransactionalCommand(
            domain, "test_compose"); //$NON-NLS-1$

        IUndoContext ctx1 = new UndoContext();
        IUndoContext ctx2 = new UndoContext();

        ICommand child1 = new TestCommand("child1", getFiles("compose1")); //$NON-NLS-1$ //$NON-NLS-2$
        child1.addContext(ctx1);

        ICommand child2 = new TestCommand("child2", getFiles("compose1")); //$NON-NLS-1$ //$NON-NLS-2$
        child1.addContext(ctx2);

        ICommand composition = c.compose(child1);
        composition = composition.compose(child2);

        assertSame(c, composition);

        List affectedFiles = composition.getAffectedFiles();
        assertTrue(affectedFiles.containsAll(child1.getAffectedFiles()));
        assertTrue(affectedFiles.containsAll(child2.getAffectedFiles()));
        
        // should be no duplicates
        assertEquals(1, affectedFiles.size());

        List contexts = Arrays.asList(composition.getContexts());
        assertTrue(contexts.contains(ctx1));
        assertTrue(contexts.contains(ctx2));
    }

    /**
     * Tests that the reduction of a CompositeTransactionalCommand returns its
     * single child if that child is an EMF Operation. Otherwise it returns the
     * composite itself.
     */
    public void test_reduce() {
        TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE
            .createEditingDomain();

        CompositeTransactionalCommand composite = new CompositeTransactionalCommand(
            domain, "test_reduce"); //$NON-NLS-1$

        ICommand child = new TestCommand();
        composite.compose(child);
        ICommand reduction = composite.reduce();

        assertSame(composite, reduction);

        composite.remove(child);

        child = new TransactionalTestCommand(domain, null);
        composite.compose(child);
        reduction = composite.reduce();

        assertSame(child, reduction);
    }

    // 
    // TEST FIXTURES
    //

    protected static class TransactionalTestCommand
        extends AbstractTransactionalCommand {

        private static final String EXECUTED = "executed"; //$NON-NLS-1$

        private final Resource resource;
        private EObject testObject;
        
        private boolean executed;

        private boolean undone;

        private boolean redone;

        public TransactionalTestCommand(TransactionalEditingDomain domain, Resource resource) {
            super(domain, "CompositeTransactionalCommandTest", //$NON-NLS-1$
                null);
            this.resource = resource;
        }

        public TransactionalTestCommand(List affectedFiles) {
            super(TransactionalEditingDomain.Factory.INSTANCE
                .createEditingDomain(), "CompositeTransactionalCommandTest", //$NON-NLS-1$
                affectedFiles);
            this.resource = null;
        }

        protected CommandResult doExecuteWithResult(
                IProgressMonitor progressMonitor, IAdaptable info)
            throws ExecutionException {
        	
        	if (resource != null) {
	        	// change my resource
	        	testObject = EcoreFactory.eINSTANCE.createEPackage();
	        	resource.getContents().add(testObject);
        	}
        	
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
            
            if (resource != null) {
            	// check that the model change was committed
            	assertTrue(resource.getContents().contains(testObject));
            }
        }

        public void assertUndone() {
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            
            if (resource != null) {
            	// check that the model change was undone
            	assertFalse(resource.getContents().contains(testObject));
            } else {
            	assertNull(getCommandResult().getReturnValue());
            }
        }

        public void assertRedone() {
            assertEquals(IStatus.OK, getCommandResult().getStatus()
                .getSeverity());
            
            if (resource != null) {
            	// check that the model change was redone
            	assertTrue(resource.getContents().contains(testObject));
            } else {
            	assertNull(getCommandResult().getReturnValue());
            }
        }
    }

    protected static class TestCommand
        extends AbstractCommand {

        private static final String EXECUTED = "executed"; //$NON-NLS-1$

        private static final String UNDONE = "undone"; //$NON-NLS-1$

        private static final String REDONE = "redone"; //$NON-NLS-1$

        private boolean executed;

        private boolean undone;

        private boolean redone;

        public TestCommand() {
            super("Test Command", null); //$NON-NLS-1$
        }

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
}
