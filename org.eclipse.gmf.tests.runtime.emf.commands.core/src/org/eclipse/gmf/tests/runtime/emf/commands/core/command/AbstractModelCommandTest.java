/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.emf.commands.core.command;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand;
import org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import org.eclipse.gmf.tests.runtime.common.core.internal.command.AbstractCommandTest;

/**
 * @author khussey
 * 
 */
public class AbstractModelCommandTest
	extends AbstractCommandTest {

	protected static class UndoInterval
		implements MUndoInterval {

		private final String label;
		private final String description;
		
		private int undoIntervalsRedone = 0;

		private int undoIntervalsUndone = 0;

		public UndoInterval(String label, String description) {
			this.label = label;
			this.description = description;
		}

		public String getLabel() {
			return label;
		}

		public String getDescription() {
			return description;
		}
		
		public boolean canUndo() {
			return true;
		}

		public boolean canRedo() {
			return true;
		}

		public void undo() {
			undoIntervalsUndone++;
		}

		public void redo() {
			undoIntervalsRedone++;
		}

		public boolean isEmpty() {
		    return false;
		}
		
		public void flush() {
			// not interested in flush
		}

		protected int getUndoIntervalsRedone() {
			return undoIntervalsRedone;
		}

		protected int getUndoIntervalsUndone() {
			return undoIntervalsUndone;
		}
	}

	static class Fixture
		extends AbstractModelCommand {

		private boolean undoIntervalOpen = false;

		private boolean writeInProgress = false;

		public Fixture(String label) {
			super(label, null);
		}

		protected void setFixtureUndoInterval(MUndoInterval undoInterval) {
			super.setUndoInterval(undoInterval);
		}

		protected CommandResult doExecute(IProgressMonitor progressMonitor) {
			undoIntervalOpen = OperationUtil.isUndoIntervalOpen();
			writeInProgress = OperationUtil.canWrite();
			return newOKCommandResult(getLabel());
		}

		public boolean isUndoIntervalOpenDuringExecution() {
			return undoIntervalOpen;
		}

		public boolean isWriteInProgressDuringExecution() {
			return writeInProgress;
		}

	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(AbstractModelCommandTest.class);
	}

	public AbstractModelCommandTest(String name) {
		super(name);
	}

	protected void setUp() {
		setFixture(new Fixture(getName()));
	}

	public void test_compose() {
		ICommand command = getFixture().compose(new Fixture(getName()));

		assertTrue(CompositeModelCommand.class.isInstance(command));
		assertEquals(getFixture().getLabel(), command.getLabel());
	}

	public void test_execute() {

		assertTrue(!((Fixture) getFixture())
			.isUndoIntervalOpenDuringExecution());

		assertTrue(!((Fixture) getFixture()).isWriteInProgressDuringExecution());

		super.test_execute();

		assertTrue(((Fixture) getFixture()).isUndoIntervalOpenDuringExecution());

		assertTrue(((Fixture) getFixture()).isWriteInProgressDuringExecution());
	}

	public void test_redo() {
		assertEquals(null, ((Fixture) getFixture()).getUndoInterval());

		((Fixture) getFixture()).setFixtureUndoInterval(null);
		super.test_redo();

		assertEquals(null, ((Fixture) getFixture()).getUndoInterval());

		((Fixture) getFixture()).setFixtureUndoInterval(new UndoInterval(
			"test_redo", "test_redo")); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			getFixture().redo();
		} catch (Exception e) {
			fail();
		}

		assertNull(getFixture().getCommandResult().getReturnValue());
		assertTrue(getFixture().getCommandResult().getStatus().isOK());
		assertNull(getFixture().getCommandResult().getStatus().getException());

		assertEquals(1, ((UndoInterval) ((Fixture) getFixture())
			.getUndoInterval()).getUndoIntervalsRedone());
	}

	public void test_undo() {
		assertEquals(null, ((Fixture) getFixture()).getUndoInterval());

		((Fixture) getFixture()).setFixtureUndoInterval(null);
		super.test_undo();

		assertEquals(null, ((Fixture) getFixture()).getUndoInterval());

		((Fixture) getFixture()).setFixtureUndoInterval(new UndoInterval(
			"test_undo", "test_undo")); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			getFixture().undo();
		} catch (Exception e) {
			fail();
		}

		assertNull(getFixture().getCommandResult().getReturnValue());
		assertTrue(getFixture().getCommandResult().getStatus().isOK());
		assertNull(getFixture().getCommandResult().getStatus().getException());

		assertEquals(1, ((UndoInterval) ((Fixture) getFixture())
			.getUndoInterval()).getUndoIntervalsUndone());
	}

}
