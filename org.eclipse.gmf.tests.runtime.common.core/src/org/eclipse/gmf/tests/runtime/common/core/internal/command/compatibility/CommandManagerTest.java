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

package org.eclipse.gmf.tests.runtime.common.core.internal.command.compatibility;

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
import org.eclipse.gmf.runtime.common.core.command.CommandManager;
import org.eclipse.gmf.runtime.common.core.command.CommandManagerChangeEvent;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICommandManagerChangeListener;
import org.eclipse.gmf.runtime.common.core.command.compatibility.AbstractCommand;

/**
 * @author khussey
 * @deprecated Obsolete test
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
        
        protected void add(ICommand command) {
            execute(command);
        }

		protected int getFixtureUndoIndex() {
			return getUndoIndex();
		}

		protected void setFixtureUndoIndex(int undoIndex) {
			setUndoIndex(undoIndex);
		}

		protected void setFixtureFlushThreshold(int flushThreshold) {
			setFlushThreshold(flushThreshold);
            
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

        int ft = getFixture().getFlushThreshold();

		setCommandManagerChangeEvent(null);
		try {
			getFixture().setFlushThreshold(0);
			fail();
		} catch (Throwable e) {
			//for our implementation, the Throwable is an AssertionError
			assertNull(getCommandManagerChangeEvent());
			assertEquals(ft, getFixture().getFlushThreshold());
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

        int fc = getFixture().getFlushCount();

		setCommandManagerChangeEvent(null);
		try {
			getFixture().setFlushCount(-1);
			fail();
		} catch (Throwable e) {
			//for our implementation, the Throwable is an AssertionError
			assertNull(getCommandManagerChangeEvent());
			assertEquals(fc, getFixture().getFlushCount());
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

}
