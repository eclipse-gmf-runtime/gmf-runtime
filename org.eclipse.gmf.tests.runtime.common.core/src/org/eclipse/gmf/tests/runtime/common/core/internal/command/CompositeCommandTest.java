/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.common.core.internal.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.gmf.runtime.common.core.command.CMValidator;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.tests.runtime.common.core.CommonCoreTestsStatusCodes;

/**
 * @author khussey
 *
 */
public class CompositeCommandTest extends TestCase {

	protected static class ProgressMonitor implements IProgressMonitor {

		private int totalWorked = 0;

		public void beginTask(String name, int totalWork) {
			setTaskName(name);
		}

		public void done() {
			// Do nothing here.
		}

		public void setTaskName(String name) {
			// Do nothing here.
		}

		public boolean isCanceled() {
			return false;
		}

		public void setCanceled(boolean b) {
			// Do nothing here.
		}

		public void subTask(String name) {
			// Do nothing here.
		}

		public void worked(int work) {
			internalWorked(work);
		}

		public void internalWorked(double work) {
			totalWorked = totalWorked + 1;
		}
		public int getTotalWorked() {
			return totalWorked;
		}
	}

	protected static class CanceledProgressMonitor
		implements IProgressMonitor {

		private int totalWorked = 0;
		
		private int commandCount = 0;
		
		public CanceledProgressMonitor() {
			super();
		}

		public void beginTask(String name, int totalWork) {
			setTaskName(name);
		}

		public void done() {
			// Do nothing here.
		}

		public void setTaskName(String name) {
			// Do nothing here.
		}

		public boolean isCanceled() {
			if (commandCount == 2) {
				return true;
			}
			commandCount++;
			return false;
		}

		public void setCanceled(boolean b) {
			// Do nothing here.
		}

		public void subTask(String name) {
			// Do nothing here.
		}

		public void worked(int work) {
			internalWorked(work);
		}

		public void internalWorked(double work) {
			totalWorked = totalWorked + 1;
		}

		public int getTotalWorked() {
			return totalWorked;
		}
	}

	protected static class Command implements ICommand {

		private final String label;

		private final Collection affectedObjects;

		private CommandResult commandResult = null;

		private final boolean executable;

		private final boolean redoable;

		private final boolean undoable;

		private boolean undone = false;

		public Command(
			String label,
			Collection affectedObjects,
			CommandResult commandResult) {

			this(label, affectedObjects, commandResult, true, true, true);
		}

		public Command(
			String label,
			Collection affectedObjects,
			CommandResult commandResult,
			boolean executable,
			boolean redoable,
			boolean undoable) {

			super();

			this.label = label;
			this.affectedObjects = affectedObjects;
			this.commandResult = commandResult;
			this.executable = executable;
			this.redoable = redoable;
			this.undoable = undoable;
		}

		public String getLabel() {
			return label;
		}

		public Collection getAffectedObjects() {
			return affectedObjects;
		}


		public boolean involvesReadOnlyNonWorkSpaceFiles() {
			return false;
		}

		public CMValidator getValidator() {
			return new CMValidator();
		}		
		
		public CommandResult getCommandResult() {
			return commandResult;
		}

		protected void setCommandResult(CommandResult commandResult) {
			this.commandResult = commandResult;
		}

		public ICommand compose(ICommand command) {
			return this;
		}

		public boolean isExecutable() {
			return executable;
		}

		public boolean isRedoable() {
			return redoable;
		}

		public boolean isUndoable() {
			return undoable;
		}

		protected void setCommandResult() {
			setCommandResult(
				new CommandResult(
					new Status(
						IStatus.OK,
						CommonCorePlugin.getPluginId(),
						CommonCoreTestsStatusCodes.OK,
						getLabel(),
						null),
					new Date()));

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				// Do nothing here.
			}
		}

		public void execute(IProgressMonitor progressMonitor) {
			if (!progressMonitor.isCanceled()) {
				setCommandResult();
			}
		}

		public void redo() {
			setCommandResult();
		}

		public void undo() {
			setUndone(true);
			setCommandResult();
		}

		public boolean isUndone() {
			return undone;
		}

		private void setUndone(boolean b) {
			undone = b;
		}
	}

	protected interface IFixtureCompositeCommand {

		List getFixtureCommands();

	}

	protected static class Fixture
		extends CompositeCommand
		implements IFixtureCompositeCommand {

		public Fixture(String label) {
			super(label);
		}

		public List getFixtureCommands() {
			return getCommands();
		}

	}

	private CompositeCommand fixture = null;

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(CompositeCommandTest.class);
	}

	public CompositeCommandTest(String name) {
		super(name);
	}

	protected CompositeCommand getFixture() {
		return fixture;
	}

	protected void setFixture(CompositeCommand fixture) {
		this.fixture = fixture;
	}

	protected void setUp() {
		setFixture(new Fixture(getName()));
	}

	public void test_getAffectedObjects() {
		assertEquals(0, getFixture().getAffectedObjects().size());

		Integer zero = new Integer(0);

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(
				getName(),
				Arrays.asList(new Integer[] { zero }),
				null));

		assertEquals(1, getFixture().getAffectedObjects().size());
		assertTrue(getFixture().getAffectedObjects().contains(zero));

		Integer one = new Integer(1);

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(getName(), Arrays.asList(new Integer[] { one }), null));

		assertEquals(2, getFixture().getAffectedObjects().size());
		assertTrue(getFixture().getAffectedObjects().contains(zero));
		assertTrue(getFixture().getAffectedObjects().contains(one));
	}

	public void test_getCommandResult() {
		assertEquals(
			IStatus.OK,
			getFixture().getCommandResult().getStatus().getSeverity());
		assertEquals(
			CommonCorePlugin.getPluginId(),
			getFixture().getCommandResult().getStatus().getPlugin());
		assertEquals(
			CommonCoreTestsStatusCodes.OK,
			getFixture().getCommandResult().getStatus().getCode());
		assertEquals(
			0,
			getFixture().getCommandResult().getStatus().getMessage().length());
		assertNull(getFixture().getCommandResult().getStatus().getException());

		assertEquals(
			0,
			((Collection)getFixture().getCommandResult().getReturnValue()).size());

		IStatus ok =
			new Status(
				IStatus.OK,
				CommonCorePlugin.getPluginId(),
				CommonCoreTestsStatusCodes.OK,
				getName(),
				null);
		Integer zero = new Integer(0);

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(getName(), null, new CommandResult(ok, zero)));

		assertEquals(
			IStatus.OK,
			getFixture().getCommandResult().getStatus().getSeverity());
		assertEquals(
			CommonCorePlugin.getPluginId(),
			getFixture().getCommandResult().getStatus().getPlugin());
		assertEquals(
			CommonCoreTestsStatusCodes.OK,
			getFixture().getCommandResult().getStatus().getCode());
		assertEquals(
			0,
			getFixture().getCommandResult().getStatus().getMessage().length());
		assertNull(getFixture().getCommandResult().getStatus().getException());

		assertEquals(
			1,
			((Collection)getFixture().getCommandResult().getReturnValue()).size());
		assertEquals(
			zero,
			((List)getFixture().getCommandResult().getReturnValue()).get(0));

		IStatus info =
			new Status(
				IStatus.INFO,
				CommonCorePlugin.getPluginId(),
				CommonCoreTestsStatusCodes.OK,
				getName(),
				null);
		Integer one = new Integer(1);

		((Fixture) getFixture()).getFixtureCommands().add(
			new Command(getName(), null, new CommandResult(info, one)));

		assertEquals(
			IStatus.INFO,
			getFixture().getCommandResult().getStatus().getSeverity());
		assertEquals(
			CommonCorePlugin.getPluginId(),
			getFixture().getCommandResult().getStatus().getPlugin());
		assertEquals(
			CommonCoreTestsStatusCodes.OK,
			getFixture().getCommandResult().getStatus().getCode());
		assertEquals(
			getName(),
			getFixture().getCommandResult().getStatus().getMessage());
		assertNull(getFixture().getCommandResult().getStatus().getException());

		assertEquals(
			2,
			((Collection)getFixture().getCommandResult().getReturnValue()).size());
		assertEquals(
			zero,
			((List)getFixture().getCommandResult().getReturnValue()).get(0));
		assertEquals(
			one,
			((List)getFixture().getCommandResult().getReturnValue()).get(1));

		IStatus warning =
			new Status(
				IStatus.WARNING,
				CommonCorePlugin.getPluginId(),
				CommonCoreTestsStatusCodes.COMMAND_FAILURE,
				getName(),
				null);
		Integer two = new Integer(2);

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(getName(), null, new CommandResult(warning, two)));

		assertEquals(
			IStatus.WARNING,
			getFixture().getCommandResult().getStatus().getSeverity());
		assertEquals(
			CommonCorePlugin.getPluginId(),
			getFixture().getCommandResult().getStatus().getPlugin());
		assertEquals(
			CommonCoreTestsStatusCodes.COMMAND_FAILURE,
			getFixture().getCommandResult().getStatus().getCode());
		assertEquals(
			getName(),
			getFixture().getCommandResult().getStatus().getMessage());
		assertNull(getFixture().getCommandResult().getStatus().getException());

		assertEquals(
			3,
			((Collection)getFixture().getCommandResult().getReturnValue()).size());
		assertEquals(
			zero,
			((List)getFixture().getCommandResult().getReturnValue()).get(0));
		assertEquals(
			one,
			((List)getFixture().getCommandResult().getReturnValue()).get(1));
		assertEquals(
			two,
			((List)getFixture().getCommandResult().getReturnValue()).get(2));

		IStatus error =
			new Status(
				IStatus.ERROR,
				CommonCorePlugin.getPluginId(),
				CommonCoreTestsStatusCodes.COMMAND_FAILURE,
				getName(),
				new Exception());
		Integer three = new Integer(3);

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(getName(), null, new CommandResult(error, three)));

		assertEquals(
			IStatus.ERROR,
			getFixture().getCommandResult().getStatus().getSeverity());
		assertEquals(
			CommonCorePlugin.getPluginId(),
			getFixture().getCommandResult().getStatus().getPlugin());
		assertEquals(
			CommonCoreTestsStatusCodes.COMMAND_FAILURE,
			getFixture().getCommandResult().getStatus().getCode());
		assertEquals(
			getName(),
			getFixture().getCommandResult().getStatus().getMessage());
		assertNotNull(
			getFixture().getCommandResult().getStatus().getException());

		assertEquals(
			4,
			((Collection)getFixture().getCommandResult().getReturnValue()).size());
		assertEquals(
			zero,
			((List)getFixture().getCommandResult().getReturnValue()).get(0));
		assertEquals(
			one,
			((List)getFixture().getCommandResult().getReturnValue()).get(1));
		assertEquals(
			two,
			((List)getFixture().getCommandResult().getReturnValue()).get(2));
		assertEquals(
			three,
			((List)getFixture().getCommandResult().getReturnValue()).get(3));
	}

	public void test_compose() {
		assertEquals(0, ((IFixtureCompositeCommand) getFixture()).getFixtureCommands().size());

		ICommand command0 = new Command(getName(), null, null);
		ICommand composite = getFixture().compose(command0);

		assertSame(getFixture(), composite);
		assertEquals(1, ((IFixtureCompositeCommand) getFixture()).getFixtureCommands().size());
		assertSame(
			command0,
			((IFixtureCompositeCommand) getFixture()).getFixtureCommands().get(
				0));

		ICommand command1 = new Command(getName(), null, null);
		composite = composite.compose(command1);

		assertSame(getFixture(), composite);
		assertEquals(2, ((IFixtureCompositeCommand) getFixture()).getFixtureCommands().size());
		assertSame(
			command1,
			((IFixtureCompositeCommand) getFixture()).getFixtureCommands().get(
				1));
	}

	public void test_isExecutable() {
		assertFalse(getFixture().isExecutable());

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(getName(), null, null, true, true, true));

		assertTrue(getFixture().isExecutable());

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(getName(), null, null, false, true, true));

		assertTrue(!getFixture().isExecutable());
	}

	public void test_isRedoable() {
		assertFalse(getFixture().isRedoable());

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(getName(), null, null, true, true, true));

		assertTrue(getFixture().isRedoable());

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(getName(), null, null, true, false, true));

		assertTrue(!getFixture().isRedoable());
	}

	public void test_isUndoable() {
		assertFalse(getFixture().isUndoable());

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(getName(), null, null, true, true, true));

		assertTrue(getFixture().isUndoable());

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			new Command(getName(), null, null, true, true, false));

		assertTrue(!getFixture().isUndoable());
	}

	public void test_execute() {

		// Test a non-canceled command execution

		ICommand command0 = new Command(getName(), null, null);
		ICommand command1 = new Command(getName(), null, null);

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			command0);
		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			command1);

		ProgressMonitor progressMonitor = new ProgressMonitor();

		getFixture().execute(progressMonitor);

		// Verify that the subprogress monitor caused the parent progress monitor's
		// work to increase by 2
		assertEquals(2, progressMonitor.getTotalWorked());

		// Verify that both commands were executed
		assertEquals(
			2,
			((Collection)getFixture().getCommandResult().getReturnValue()).size());

		assertTrue(
			((Date) command0.getCommandResult().getReturnValue()).getTime()
				< ((Date) command1.getCommandResult().getReturnValue())
					.getTime());

	}

	public void test_redo() {
		ICommand command0 = new Command(getName(), null, null);
		ICommand command1 = new Command(getName(), null, null);

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			command1);
		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			command0);

		getFixture().redo();

		assertTrue(
			((Date) command0.getCommandResult().getReturnValue()).getTime()
				< ((Date) command1.getCommandResult().getReturnValue())
					.getTime());

		assertSame(
			command0,
			((IFixtureCompositeCommand) getFixture()).getFixtureCommands().get(0));
		assertSame(
			command1,
			((IFixtureCompositeCommand) getFixture()).getFixtureCommands().get(1));
	}

	public void test_undo() {
		ICommand command0 = new Command(getName(), null, null);
		ICommand command1 = new Command(getName(), null, null);

		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			command0);
		((IFixtureCompositeCommand) getFixture()).getFixtureCommands().add(
			command1);

		getFixture().undo();

		assertTrue(
			((Date) command0.getCommandResult().getReturnValue()).getTime()
				> ((Date) command1.getCommandResult().getReturnValue())
					.getTime());

		assertSame(
			command0,
			((IFixtureCompositeCommand) getFixture()).getFixtureCommands().get(
				1));
		assertSame(
			command1,
			((IFixtureCompositeCommand) getFixture()).getFixtureCommands().get(
				0));
	}

}
