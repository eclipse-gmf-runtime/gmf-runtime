/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.commands.core.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand;
import org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval;
import org.eclipse.gmf.runtime.emf.core.internal.commands.MSLAbstractCommand;
import org.eclipse.gmf.runtime.emf.core.internal.commands.MSLCompoundCommand;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import org.eclipse.gmf.tests.runtime.common.core.internal.command.CompositeCommandTest;
import org.eclipse.gmf.tests.runtime.emf.core.EmfMslTestsStatusCodes;

/**
 * @author khussey
 *  
 */
public class CompositeModelCommandTest
	extends CompositeCommandTest {

	protected static class UndoInterval
		extends MSLCompoundCommand
		implements MUndoInterval {

		private int undoIntervalsRedone = 0;

		private int undoIntervalsUndone = 0;

		public UndoInterval() {
			super(null);
		}

		/*
		 * (non-Javadoc) Since the test is only testing the composite model
		 * command and not the MSL Action Manager, always return true.
		 * Otherwise, manually inserted commands cannot be undone.
		 */
		public boolean canUndo() {
			return true;
		}

		/*
		 * (non-Javadoc) Since the test is only testing the composite model
		 * command and not the MSL Action Manager, always return true.
		 * Otherwise, manually inserted commands cannot be redone.
		 */
		public boolean canRedo() {
			return true;
		}

		/*
		 * (non-Javadoc) Must increment the counter for the test
		 */
		public void redo() {
			//since we are not testing MSLUndoInterval, this is sufficient
			super.redo();
			undoIntervalsRedone++;
		}

		/*
		 * (non-Javadoc) Must increment the counter for the test
		 */
		public void undo() {
			//since we are not testing MSLUndoInterval, this is sufficient
			super.undo();
			undoIntervalsUndone++;
		}

		protected int getUndoIntervalsRedone() {
			return undoIntervalsRedone;
		}

		protected int getUndoIntervalsUndone() {
			return undoIntervalsUndone;
		}

		public List getCommandList() {
			return getCommands();
		}

		public void flush() {
			// since we are not testing MSLUndoInterval, this is sufficient
		}
	}

	protected static class Fixture
		extends CompositeModelCommand
		implements CompositeCommandTest.IFixtureCompositeCommand {

		public Fixture(String label) {
			super(label);
		}

		public List getFixtureCommands() {
			return getCommands();
		}

		protected void setFixtureUndoInterval(MUndoInterval undoInterval) {
			super.setUndoInterval(undoInterval);
		}
	}

	/*
	 * (non-Javadoc) based on superclass command
	 */
	protected static class EMFCommand
		extends MSLAbstractCommand {

		String label = null;

		ArrayList result = null;

		protected EMFCommand(String label) {
			super(null);
			this.label = label;
		}

		public void execute() {
			setCommandResult();
		}

		public void redo() {
			setCommandResult();
		}

		public void undo() {
			setCommandResult();
		}

		public Collection getResult() {
			return result;
		}

		public CommandResult getFirstResult() {
			if (result == null)
				return null;
			return (CommandResult) (result.get(0));
		}

		protected void setCommandResult() {
			result = new ArrayList();
			result.add(new CommandResult(new Status(IStatus.OK,
				CommonCorePlugin.getPluginId(), EmfMslTestsStatusCodes.OK,
				getLabel(), null), new Date()));

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				//do nothing in test
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getType()
		 */
		public Type getType() {
			return null;
		}

	}
	
	/**
	 * Command that checks that a write action is already open when it is
	 * executed. Used to verify that the CompositeModelCommand opens a write
	 * action before executing its composed commands.
	 */
	protected class MyAbstractCommand
		extends AbstractCommand {

		private AbstractModelCommand myModelCommand = new AbstractModelCommand(
			getLabel(), null) {

			protected CommandResult doExecute(IProgressMonitor progressMonitor) {
				return null;
			}
		};

		public MyAbstractCommand(String label) {
			super(label);
		}

		protected CommandResult doExecute(IProgressMonitor progressMonitor) {
			assertTrue(
				"Expect write action to have been opened by the containing CompositeModelCommand.", OperationUtil.canWrite()); //$NON-NLS-1$
			myModelCommand.execute(progressMonitor);
			return newOKCommandResult();
		}

		public AbstractModelCommand getModelCommand() {
			return myModelCommand;
		}
	};

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(CompositeModelCommandTest.class);
	}

	public CompositeModelCommandTest(String name) {
		super(name);
	}

	protected void setUp() {
		setFixture(new Fixture(getName()));

	}

	public void test_execute() {
		
		super.test_execute();
		
		MyAbstractCommand modelCommand1 = new MyAbstractCommand("Model Command 1"); //$NON-NLS-1$
		MyAbstractCommand modelCommand2 = new MyAbstractCommand("Model Command 2"); //$NON-NLS-1$

		CompositeModelCommand compositeCommand = new CompositeModelCommand("Composite Model Command Test"); //$NON-NLS-1$
		compositeCommand.compose(modelCommand1);
		compositeCommand.compose(modelCommand2);
		
		compositeCommand.execute(new NullProgressMonitor());
		
		// Verify that the composed commands used the same undo interval
		assertNull(modelCommand1.getModelCommand().getUndoInterval());
		assertNull(modelCommand2.getModelCommand().getUndoInterval());
		assertNotNull(compositeCommand.getUndoInterval());
	}

	public void test_redo() {
		assertEquals(null, ((Fixture) getFixture()).getUndoInterval());

		UndoInterval undoInterval = new UndoInterval();

		((Fixture) getFixture()).setFixtureUndoInterval(undoInterval);

		EMFCommand command0 = new EMFCommand(getName());
		EMFCommand command1 = new EMFCommand(getName());

		((UndoInterval) ((Fixture) getFixture()).getUndoInterval())
			.append(command1);
		((UndoInterval) ((Fixture) getFixture()).getUndoInterval())
			.append(command0);

		try {
			getFixture().redo();
		} catch (Exception e) {
			fail();
		}

		//the next assert is different from the superclass implementation

		//command 1 is on the stack first, and command 0 goes above it.

		//for an undo, the top of the stack gets executed first
		//for a redo, the bottom of the stack gets executed first
		assertTrue(((Date) command1.getFirstResult().getReturnValue())
			.getTime() < ((Date) command0.getFirstResult().getReturnValue())
			.getTime());

		assertSame(command1, ((UndoInterval) ((Fixture) getFixture())
			.getUndoInterval()).getCommandList().get(0));
		assertSame(command0, ((UndoInterval) ((Fixture) getFixture())
			.getUndoInterval()).getCommandList().get(1));

		assertEquals(undoInterval, ((Fixture) getFixture()).getUndoInterval());

		assertEquals(1, ((UndoInterval) ((Fixture) getFixture())
			.getUndoInterval()).getUndoIntervalsRedone());
	}

	public void test_undo() {
		assertEquals(null, ((Fixture) getFixture()).getUndoInterval());

		UndoInterval undoInterval = new UndoInterval();
		((Fixture) getFixture()).setFixtureUndoInterval(undoInterval);

		EMFCommand command0 = new EMFCommand(getName());
		EMFCommand command1 = new EMFCommand(getName());

		((UndoInterval) ((Fixture) getFixture()).getUndoInterval())
			.append(command0);
		((UndoInterval) ((Fixture) getFixture()).getUndoInterval())
			.append(command1);

		try {
			getFixture().undo();
		} catch (Exception e) {
			fail();
		}

		//see test_redo method for more comments

		//command 0 is at the bottom of the stack and commnd 1 is at the top
		//for an undo, command1 should be executed first
		assertTrue(((Date) command1.getFirstResult().getReturnValue())
			.getTime() < ((Date) command0.getFirstResult().getReturnValue())
			.getTime());

		assertSame(command0, ((UndoInterval) ((Fixture) getFixture())
			.getUndoInterval()).getCommandList().get(0));
		assertSame(command1, ((UndoInterval) ((Fixture) getFixture())
			.getUndoInterval()).getCommandList().get(1));

		assertEquals(undoInterval, ((Fixture) getFixture()).getUndoInterval());

		assertEquals(1, ((UndoInterval) ((Fixture) getFixture())
			.getUndoInterval()).getUndoIntervalsUndone());
	}

	public void test_getCommandResult() {
		// Override the superclass test so that it's not executed for this
		// subclass. The getCommandResult() method for CompositeModelCommand
		// is a simple accessor and doesn't require testing.
	}

}