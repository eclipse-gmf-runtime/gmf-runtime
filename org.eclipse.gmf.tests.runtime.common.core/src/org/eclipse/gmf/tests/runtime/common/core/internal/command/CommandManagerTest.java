/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.common.core.internal.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandManager;
import org.eclipse.gmf.runtime.common.core.command.CommandManagerChangeEvent;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICommandManagerChangeListener;

/**
 * @author khussey
 */
public class CommandManagerTest extends TestCase {

	protected static class Command extends AbstractCommand {

		private final List affectedObjects;

		private final boolean executable;

		private final boolean undoable;

		private final boolean redoable;

		private final int severity;

		public Command(
			String label,
			List affectedObjects,
			boolean executable,
			boolean undoable,
			boolean redoable,
			int severity) {

			super(label);

			this.affectedObjects = affectedObjects;
			this.executable = executable;
			this.undoable = undoable;
			this.redoable = redoable;
			this.severity = severity;
		}

		public Collection getAffectedObjects() {
			return affectedObjects;
		}

		public boolean isExecutable() {
			return executable;
		}

		public boolean isUndoable() {
			return undoable;
		}

		public boolean isRedoable() {
			return redoable;
		}

		protected int getSeverity() {
			return severity;
		}

		protected CommandResult doExecute(IProgressMonitor progressMonitor) {
			return new CommandResult(new Status(getSeverity(), getPluginId(), 0, "doExecute", null)); //$NON-NLS-1$
		}

		protected CommandResult doUndo() {
			return new CommandResult(new Status(getSeverity(), getPluginId(), 0, "doUndo", null)); //$NON-NLS-1$
		}

		protected CommandResult doRedo() {
			return new CommandResult(new Status(getSeverity(), getPluginId(), 0, "doRedo", null)); //$NON-NLS-1$
		}

	}

	protected class CommandManagerChangeListener
		implements ICommandManagerChangeListener {

		private final CommandManager.State state;

		public CommandManagerChangeListener(CommandManager.State state) {
			super();

			this.state = state;
		}

		protected CommandManager.State getState() {
			return state;
		}

		public void commandManagerChanged(CommandManagerChangeEvent event) {
			// can not use this assertion anylonger - the CommandManager will change it's
			// state multiple times during excecution so this assertion will fail 
			//assertSame(getState(),((CommandManager) event.getSource()).getState());

			setCommandManagerChangeEvent(event);
		}
	}

	protected static class Fixture extends CommandManager {

		public Fixture() {
			super();
		}

		protected List getFixtureListeners() {
			return getListeners();
		}

		protected List getFixtureCommands() {
			return getCommands();
		}

		protected int getFixtureUndoIndex() {
			return getUndoIndex();
		}

		protected void setFixtureUndoIndex(int undoIndex) {
			setUndoIndex(undoIndex);
		}

		protected void setFixtureFlushThreshold(int flushThreshold) {
			this.flushThreshold = flushThreshold;
		}

		protected void setFixtureFlushCount(int flushCount) {
			this.flushCount = flushCount;
		}

		protected void fireCommandManagerChange(CommandManagerChangeEvent event) {
			super.fireCommandManagerChange(event);
		}

		protected void addCommand(ICommand command) {
			super.addCommand(command);
		}

		protected void flush() {
			super.flush();
		}

	}

	private CommandManagerChangeEvent commandManagerChangeEvent = null;

	private Fixture fixture = null;

	private Exception exception = null;

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(CommandManagerTest.class);
	}

	public CommandManagerTest(String name) {
		super(name);
	}

	protected CommandManagerChangeEvent getCommandManagerChangeEvent() {
		return commandManagerChangeEvent;
	}

	protected void setCommandManagerChangeEvent(CommandManagerChangeEvent commandManagerChangeEvent) {
		this.commandManagerChangeEvent = commandManagerChangeEvent;
	}

	protected Fixture getFixture() {
		return fixture;
	}

	protected void setFixture(Fixture fixture) {
		this.fixture = fixture;
	}

	protected Exception getException() {
		return exception;
	}

	protected void setException(Exception exception) {
		this.exception = exception;
	}

	protected void setUp() {
		setFixture(new Fixture());
	}

	public void test_setFlushThreshold() {
		getFixture().getFixtureListeners().add(
			new CommandManagerChangeListener(CommandManager.State.IDLE));

		assertEquals(Integer.MAX_VALUE, getFixture().getFlushThreshold());

		setCommandManagerChangeEvent(null);
		try {
			getFixture().setFlushThreshold(0);
			fail();
		} catch (Throwable e) {
			//for our implementation, the Throwable is an AssertionError
			assertNull(getCommandManagerChangeEvent());
			assertEquals(Integer.MAX_VALUE, getFixture().getFlushThreshold());
		}

		try {
			getFixture().setFlushThreshold(1);

			assertNotNull(getCommandManagerChangeEvent());
			assertNull(getCommandManagerChangeEvent().getCommand());
			assertEquals(1, getFixture().getFlushThreshold());
		} catch (Exception e) {
			fail();
		}
	}

	public void test_setFlushCount() {
		getFixture().getFixtureListeners().add(
			new CommandManagerChangeListener(CommandManager.State.IDLE));

		assertEquals(0, getFixture().getFlushCount());

		setCommandManagerChangeEvent(null);
		try {
			getFixture().setFlushCount(-1);
			fail();
		} catch (Throwable e) {
			//for our implementation, the Throwable is an AssertionError
			assertNull(getCommandManagerChangeEvent());
			assertEquals(0, getFixture().getFlushCount());
		}

		try {
			getFixture().setFlushCount(Integer.MAX_VALUE);

			assertNotNull(getCommandManagerChangeEvent());
			assertNull(getCommandManagerChangeEvent().getCommand());
			assertEquals(Integer.MAX_VALUE, getFixture().getFlushCount());
		} catch (Exception e) {
			fail();
		}
	}

	public void test_add_remove_CommandManagerChangeListener() {
		ICommandManagerChangeListener listener =
			new CommandManagerChangeListener(CommandManager.State.IDLE);

		assertTrue(getFixture().getFixtureListeners().isEmpty());

		try {
			getFixture().addCommandManagerChangeListener(null);
			fail();
		} catch (Throwable e) {
			//for our implementation, the Throwable is an AssertionError
			assertTrue(getFixture().getFixtureListeners().isEmpty());
		}

		try {
			getFixture().addCommandManagerChangeListener(listener);

			assertTrue(!getFixture().getFixtureListeners().isEmpty());
			assertSame(listener, getFixture().getFixtureListeners().get(0));
		} catch (Exception e) {
			fail();
		}

		setCommandManagerChangeEvent(null);
		getFixture().fireCommandManagerChange(
			new CommandManagerChangeEvent(getFixture()));

		assertNotNull(getCommandManagerChangeEvent());
		assertSame(getFixture(), getCommandManagerChangeEvent().getSource());

		try {
			getFixture().removeCommandManagerChangeListener(null);
			fail();
		} catch (Throwable e) {
			//for our implementation, the Throwable is an AssertionError
			assertTrue(!getFixture().getFixtureListeners().isEmpty());
			assertSame(listener, getFixture().getFixtureListeners().get(0));
		}

		try {
			getFixture().removeCommandManagerChangeListener(listener);

			assertTrue(getFixture().getFixtureListeners().isEmpty());
		} catch (Exception e) {
			fail();
		}

		setCommandManagerChangeEvent(null);
		getFixture().fireCommandManagerChange(
			new CommandManagerChangeEvent(getFixture()));

		assertNull(getCommandManagerChangeEvent());
	}

	public void test_fireCommandManagerChange() {
		final int count = 99;

		final ICommandManagerChangeListener[] listeners =
			new ICommandManagerChangeListener[count];

		for (int i = 0; i < count; i++) {
			listeners[i] = new ICommandManagerChangeListener() {
				public void commandManagerChanged(CommandManagerChangeEvent event) {
					// Nothing to do
				}
			};
		}

		Thread addThread = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < count; i++) {
					getFixture().addCommandManagerChangeListener(listeners[i]);

					if (null != getException()) {
						break;
					}
				}
			}
		});
		addThread.start();

		Thread fireThread = new Thread(new Runnable() {
			public void run() {
				CommandManagerChangeEvent event =
					new CommandManagerChangeEvent(getFixture());

				try {
					for (int i = 0; i < count; i++) {
						getFixture().fireCommandManagerChange(event);

						try {
							Thread.sleep(1);
						} catch (InterruptedException ie) {
							// Nothing to do
						}

					}
				} catch (Exception e) {
					setException(e);
				}
			}
		});
		fireThread.start();

		Thread removeThread = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < count; i++) {
					getFixture().removeCommandManagerChangeListener(
						listeners[i]);

					if (null != getException()) {
						break;
					}
				}
			}
		});
		removeThread.start();

		try {
			fireThread.join();
		} catch (InterruptedException ie) {
			setException(ie);
		}

		if (null != getException()) {
			fail();
		}
	}

	public void test_addCommand() {
		getFixture().setFixtureFlushThreshold(1);
		getFixture().setFixtureFlushCount(1);

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));

		assertEquals(1, getFixture().getFixtureCommands().size());
		assertEquals(-1, getFixture().getFixtureUndoIndex());

		try {
			getFixture().addCommand(
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					true,
					true,
					IStatus.OK));

			fail();
		} catch (Throwable e) {
			//for our implementation, the Throwable is an AssertionError
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(-1, getFixture().getFixtureUndoIndex());
		}

		getFixture().getFixtureCommands().remove(0);

		assertEquals(0, getFixture().getFixtureCommands().size());
		assertEquals(-1, getFixture().getFixtureUndoIndex());

		try {
			getFixture().addCommand(
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					true,
					true,
					IStatus.OK));

			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		} catch (Exception e) {
			fail();
		}

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));

		assertEquals(2, getFixture().getFixtureCommands().size());
		assertEquals(0, getFixture().getFixtureUndoIndex());

		try {
			getFixture().addCommand(
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					true,
					true,
					IStatus.OK));

			fail();
		} catch (Throwable e) {
			//for our implementation, the Throwable is an AssertionError
			assertEquals(2, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		}

		getFixture().getFixtureCommands().remove(1);

		assertEquals(1, getFixture().getFixtureCommands().size());
		assertEquals(0, getFixture().getFixtureUndoIndex());

		try {
			ICommand command =
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					true,
					true,
					IStatus.OK);
			getFixture().addCommand(command);

			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
			assertSame(command, getFixture().getFixtureCommands().get(0));
		} catch (Exception e) {
			fail();
		}
	}

	public void test_flush_old() {

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().setFixtureUndoIndex(3);

		assertEquals(5, getFixture().getFixtureCommands().size());
		assertEquals(3, getFixture().getFixtureUndoIndex());

		getFixture().flush();
		assertEquals(5, getFixture().getFixtureCommands().size());
		assertEquals(3, getFixture().getFixtureUndoIndex());

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().setFixtureFlushCount(1);
		getFixture().flush();
		assertEquals(5, getFixture().getFixtureCommands().size());
		assertEquals(2, getFixture().getFixtureUndoIndex());

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().setFixtureFlushCount(2);
		getFixture().flush();
		assertEquals(4, getFixture().getFixtureCommands().size());
		assertEquals(0, getFixture().getFixtureUndoIndex());

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().setFixtureFlushCount(2);
		getFixture().flush();
		assertEquals(3, getFixture().getFixtureCommands().size());
		assertEquals(-1, getFixture().getFixtureUndoIndex());
	}

	public void test_flush_new() {
		ICommand commandToFlushTo =
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK);

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(commandToFlushTo);
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().setFixtureUndoIndex(3);

		assertEquals(5, getFixture().getFixtureCommands().size());
		assertEquals(3, getFixture().getFixtureUndoIndex());

		getFixture().flush(commandToFlushTo);
		assertEquals(3, getFixture().getFixtureCommands().size());
		assertEquals(1, getFixture().getFixtureUndoIndex());
		
		commandToFlushTo =
					new Command(
						getName(),
						Collections.EMPTY_LIST,
						true,
						true,
						true,
						IStatus.OK);		
						
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(
			commandToFlushTo);
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		
		getFixture().setFixtureUndoIndex(4);
		
		assertEquals(6, getFixture().getFixtureCommands().size());
		assertEquals(4, getFixture().getFixtureUndoIndex());

		getFixture().flush(commandToFlushTo);
		assertEquals(1, getFixture().getFixtureCommands().size());
		assertEquals(-1, getFixture().getFixtureUndoIndex());
		
	}
	
	public void test_undo_redo_multiple() {
		ICommand commandToUndoTo =
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK);
		ICommand commandToRedoTo =
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK);
		
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(commandToUndoTo);
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(commandToRedoTo);
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
	
		getFixture().setFixtureUndoIndex(3);

		assertEquals(5, getFixture().getFixtureCommands().size());
		assertEquals(3, getFixture().getFixtureUndoIndex());

		getFixture().undo(commandToUndoTo);

		assertEquals(5, getFixture().getFixtureCommands().size());
		assertEquals(0, getFixture().getFixtureUndoIndex());
		
		getFixture().redo(commandToRedoTo);
		
		assertEquals(5, getFixture().getFixtureCommands().size());
		assertEquals(3, getFixture().getFixtureUndoIndex());
	}

	public void test_canRedo() {
		assertTrue(!getFixture().canRedo());

		ICommand command =
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK);
		getFixture().getFixtureCommands().add(command);

		assertTrue(getFixture().canRedo());

		getFixture().getFixtureCommands().remove(command);

		assertTrue(!getFixture().canRedo());
	}

	public void test_canUndo() {
		assertTrue(!getFixture().canUndo());

		ICommand command =
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK);
		getFixture().getFixtureCommands().add(command);
		getFixture().setFixtureUndoIndex(0);

		assertTrue(getFixture().canUndo());

		getFixture().getFixtureCommands().remove(command);
		getFixture().setFixtureUndoIndex(-1);

		assertTrue(!getFixture().canUndo());
	}

	public void test_clear() {
		getFixture().getFixtureListeners().add(
			new CommandManagerChangeListener(CommandManager.State.CLEARING));

		assertEquals(0, getFixture().getFixtureCommands().size());

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().setFixtureUndoIndex(0);

		assertEquals(2, getFixture().getFixtureCommands().size());
		assertEquals(0, getFixture().getFixtureUndoIndex());

		setCommandManagerChangeEvent(null);
		getFixture().clear();

		assertNotNull(getCommandManagerChangeEvent());
		assertNull(getCommandManagerChangeEvent().getCommand());
		assertEquals(0, getFixture().getFixtureCommands().size());
		assertEquals(-1, getFixture().getFixtureUndoIndex());
	}

	public void test_execute() {
		getFixture().getFixtureListeners().add(new CommandManagerChangeListener(CommandManager.State.EXECUTING));

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().setFixtureUndoIndex(0);
		assertEquals(2, getFixture().getFixtureCommands().size());
		assertEquals(0, getFixture().getFixtureUndoIndex());

		setCommandManagerChangeEvent(null);
		try {
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			getFixture().execute(
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					false,
					false,
					false,
					IStatus.ERROR));
			fail();
		} catch (UnsupportedOperationException uoe) {
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNull(getCommandManagerChangeEvent());
			assertEquals(2, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		}

		try {
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result =
				getFixture().execute(
					new Command(
						getName(),
						Collections.EMPTY_LIST,
						true,
						false,
						false,
						IStatus.ERROR));

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNull(getCommandManagerChangeEvent());
			assertEquals(IStatus.ERROR, result.getStatus().getSeverity());
			assertEquals(2, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		try {
			ICommand command =
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					false,
					false,
					IStatus.WARNING);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().execute(command);

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.WARNING, result.getStatus().getSeverity());
			assertEquals(2, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		setCommandManagerChangeEvent(null);
		try {
			ICommand command =
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					false,
					false,
					IStatus.INFO);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().execute(command);

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.INFO, result.getStatus().getSeverity());
			assertEquals(2, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
			new Path("/foo/bar")); //$NON-NLS-1$
		
		setCommandManagerChangeEvent(null);
		try {
			
			ICommand command =
				new Command(
					getName(),
					Arrays.asList(new Object[] {file}),
					true,
					false,
					false,
					IStatus.OK);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().execute(command);

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.OK, result.getStatus().getSeverity());
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(-1, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		setCommandManagerChangeEvent(null);
		try {
			ICommand command =
				new Command(
					getName(),
					Arrays.asList(new Object[] {file}),
					true,
					true,
					false,
					IStatus.OK);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().execute(command);

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.OK, result.getStatus().getSeverity());
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
			assertSame(command, getFixture().getFixtureCommands().get(0));
		} catch (UnsupportedOperationException uoe) {
			fail();
		}
	}

	public void test_getRedoLabel() {
		assertNull(getFixture().getRedoLabel());

		ICommand command =
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK);
		getFixture().getFixtureCommands().add(command);
		assertEquals(getName(), getFixture().getRedoLabel());

		getFixture().getFixtureCommands().remove(command);
		assertNull(getFixture().getRedoLabel());

	}

	public void test_getUndoLabel() {
		assertNull(getFixture().getUndoLabel());

		ICommand command =
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK);
		getFixture().getFixtureCommands().add(command);
		getFixture().setFixtureUndoIndex(0);
		assertEquals(getName(), getFixture().getUndoLabel());

		getFixture().getFixtureCommands().remove(command);
		getFixture().setFixtureUndoIndex(-1);
		assertNull(getFixture().getUndoLabel());
	}

	public void test_redo() {
		getFixture().getFixtureListeners().add(new CommandManagerChangeListener(CommandManager.State.REDOING));

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		getFixture().setFixtureUndoIndex(0);
		assertEquals(1, getFixture().getFixtureCommands().size());
		assertEquals(0, getFixture().getFixtureUndoIndex());

		setCommandManagerChangeEvent(null);
		try {
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			getFixture().redo();
			fail();
		} catch (UnsupportedOperationException uoe) {
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNull(getCommandManagerChangeEvent());
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		}

		try {
			ICommand command =
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					false,
					true,
					IStatus.ERROR);
			getFixture().getFixtureCommands().add(command);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().redo();

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNull(getCommandManagerChangeEvent());
			assertEquals(IStatus.ERROR, result.getStatus().getSeverity());
			assertEquals(2, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		try {
			ICommand command =
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					false,
					true,
					IStatus.WARNING);
			getFixture().getFixtureCommands().add(1, command);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().redo();

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.WARNING, result.getStatus().getSeverity());
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		setCommandManagerChangeEvent(null);
		try {
			ICommand command =
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					false,
					true,
					IStatus.INFO);
			getFixture().getFixtureCommands().add(command);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().redo();

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.INFO, result.getStatus().getSeverity());
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		setCommandManagerChangeEvent(null);
		try {
			ICommand command =
				new Command(
					getName(),
					Arrays.asList(new Object[] { new Integer(0)}),
					true,
					false,
					true,
					IStatus.OK);
			getFixture().getFixtureCommands().add(command);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().redo();

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.OK, result.getStatus().getSeverity());
			assertEquals(0, getFixture().getFixtureCommands().size());
			assertEquals(-1, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		setCommandManagerChangeEvent(null);
		try {
			ICommand command =
				new Command(
					getName(),
					Arrays.asList(new Object[] { new Integer(0)}),
					true,
					true,
					true,
					IStatus.OK);
			getFixture().getFixtureCommands().add(command);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().redo();

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.OK, result.getStatus().getSeverity());
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
			assertSame(command, getFixture().getFixtureCommands().get(0));
		} catch (UnsupportedOperationException uoe) {
			fail();
		}
	}

	public void test_undo() {
		getFixture().getFixtureListeners().add(new CommandManagerChangeListener(CommandManager.State.UNDOING));

		getFixture().getFixtureCommands().add(
			new Command(
				getName(),
				Collections.EMPTY_LIST,
				true,
				true,
				true,
				IStatus.OK));
		assertEquals(1, getFixture().getFixtureCommands().size());
		assertEquals(-1, getFixture().getFixtureUndoIndex());

		setCommandManagerChangeEvent(null);
		try {
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			getFixture().undo();
			fail();
		} catch (UnsupportedOperationException uoe) {
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNull(getCommandManagerChangeEvent());
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(-1, getFixture().getFixtureUndoIndex());
		}

		try {
			ICommand command =
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					true,
					false,
					IStatus.ERROR);
			getFixture().getFixtureCommands().add(0, command);
			getFixture().setFixtureUndoIndex(0);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().undo();

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNull(getCommandManagerChangeEvent());
			assertEquals(IStatus.ERROR, result.getStatus().getSeverity());
			assertEquals(2, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		try {
			ICommand command =
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					true,
					false,
					IStatus.WARNING);
			getFixture().getFixtureCommands().add(1, command);
			getFixture().setFixtureUndoIndex(1);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().undo();

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.WARNING, result.getStatus().getSeverity());
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		setCommandManagerChangeEvent(null);
		try {
			ICommand command =
				new Command(
					getName(),
					Collections.EMPTY_LIST,
					true,
					true,
					false,
					IStatus.INFO);
			getFixture().getFixtureCommands().add(command);
			getFixture().setFixtureUndoIndex(1);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().undo();

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.INFO, result.getStatus().getSeverity());
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(0, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		setCommandManagerChangeEvent(null);
		try {
			ICommand command =
				new Command(
					getName(),
					Arrays.asList(new Object[] { new Integer(0)}),
					true,
					true,
					false,
					IStatus.OK);
			getFixture().getFixtureCommands().add(command);
			getFixture().setFixtureUndoIndex(1);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().undo();

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.OK, result.getStatus().getSeverity());
			assertEquals(0, getFixture().getFixtureCommands().size());
			assertEquals(-1, getFixture().getFixtureUndoIndex());
		} catch (UnsupportedOperationException uoe) {
			fail();
		}

		setCommandManagerChangeEvent(null);
		try {
			ICommand command =
				new Command(
					getName(),
					Arrays.asList(new Object[] { new Integer(0)}),
					true,
					true,
					true,
					IStatus.OK);
			getFixture().getFixtureCommands().add(command);
			getFixture().setFixtureUndoIndex(0);
			assertSame(CommandManager.State.IDLE, getFixture().getState());
			CommandResult result = getFixture().undo();

			assertSame(CommandManager.State.IDLE, getFixture().getState());
			assertNotNull(getCommandManagerChangeEvent());
			assertSame(command, getCommandManagerChangeEvent().getCommand());
			assertEquals(IStatus.OK, result.getStatus().getSeverity());
			assertEquals(1, getFixture().getFixtureCommands().size());
			assertEquals(-1, getFixture().getFixtureUndoIndex());
			assertSame(command, getFixture().getFixtureCommands().get(0));
		} catch (UnsupportedOperationException uoe) {
			fail();
		}
	}

}
