/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;

/**
 * Manages the execution of
 * {@link org.eclipse.gmf.runtime.common.core.command.ICommand}s.
 * <P>
 * Applications can channel their command executions through a command manager
 * which will maintain a history of the commands that it has executed that are
 * eligible to be undone and redone. The number of commands that are remembered
 * by the command manager is finite, and defined by the
 * {@link #setFlushThreshold(int)} method. When this threshold is reached the
 * command manager will flush a certain number of commands from its memory
 * (defined by the {@link #setFlushCount(int)} method).
 * <P>
 * A command manager has a state, which is one of CLEARING, EXECUTING, IDLE,
 * REDOING or UNDOING. When the command manager transitions from one state to
 * another, it will notify any registered
 * {@link org.eclipse.gmf.runtime.common.core.command.ICommandManagerChangeListener}s
 * of the operation that just occurred which caused it to change state.
 * <P>
 * The command manager does not support nested command execution. Clients should
 * never ask the command manager to execute a command while it is already in the
 * process of executing a command. An exception will occur in this case.
 * 
 * @author khussey
 * @author ldamus
 * 
 * @deprecated Use the {@link OperationHistoryFactory#getOperationHistory()}
 *             instead.
 * 
 * @canBeSeenBy %partners
 */
public class CommandManager {

	/**
	 * An enumeration of command manager states.
	 * 
	 * @author khussey
	 */
	public static class State extends EnumeratedType {

		private static final long serialVersionUID = 1L;

		/**
		 * The command manager is clearing its list of commands.
		 */
		public static final State CLEARING = new State("Clearing"); //$NON-NLS-1$

		/**
		 * The command manager is executing a command.
		 */
		public static final State EXECUTING = new State("Executing"); //$NON-NLS-1$

		/**
		 * The command manager is idle.
		 */
		public static final State IDLE = new State("Idle"); //$NON-NLS-1$

		/**
		 * The command manager is redoing a command.
		 */
		public static final State REDOING = new State("Redoing"); //$NON-NLS-1$

		/**
		 * The command manager is undoing a command.
		 */
		public static final State UNDOING = new State("Undoing"); //$NON-NLS-1$

		/**
		 * The command manager is flushing down to a command
		 */
		public static final State FLUSHING = new State("Flushing"); //$NON-NLS-1$

		/**
		 * The list of values for this enumerated type.
		 */
		private static final State[] VALUES = { CLEARING, EXECUTING, IDLE,
				REDOING, UNDOING, FLUSHING };

		/**
		 * An internal unique identifier for command manager states.
		 */
		private static int nextOrdinal = 0;

		/**
		 * Constructs a new command manager state with the specified name.
		 * 
		 * @param name
		 *            The name of the new command manager state.
		 */
		private State(String name) {
			super(name, nextOrdinal++);
		}

		/**
		 * Retrieves the list of constants for this enumerated type.
		 * 
		 * @return The list of constants for this enumerated type.
		 * 
		 * @see EnumeratedType#getValues()
		 */
		protected List getValues() {
			return Collections.unmodifiableList(Arrays.asList(VALUES));
		}

	}

	/**
	 * The default flush threshold. This is the maximum number of undoable
	 * commands that will be remembered by this command manager.
	 */
	protected static int DEFAULT_FLUSH_THRESHOLD = 20;

	/**
	 * The default flush count. This is the number of commands that are fluxhed
	 * from this command manager's memory when the flush threshold is exceeded.
	 */
	protected static int DEFAULT_FLUSH_COUNT = 1;

	/**
	 * The default command manager.
	 */
	private static CommandManager commandManager = null;

	/**
	 * The command manager change listeners.
	 */
	private final List listeners = Collections
			.synchronizedList(new ArrayList());

	/**
	 * The state of this command manager.
	 */
	private State state = State.IDLE;

	/**
	 * The currently executing command.
	 */
	private ICommand currentlyExecutingCommand = null;

	/**
	 * My undo context.
	 */
	private IUndoContext undoContext;

	/**
	 * My operation history delegate.
	 */
	private final IOperationHistory delegate;
    
    /**
     * The number of commands to flush from the undo stack.
     */
    protected int flushCount = 0;

	/**
	 * Constructs a new command manager.
	 */
	public CommandManager() {
		super();
		delegate = OperationHistoryFactory.getOperationHistory();
	}
    
    /**
     * Initializes me with an operation history.
     */
    public CommandManager(IOperationHistory operationHistory) {
        super();
        delegate = operationHistory;
    }

	/**
	 * Retrieves the default command manager.
	 * 
	 * @return The default command manager.
	 */
	public static CommandManager getDefault() {
		if (null == commandManager) {
			commandManager = new CommandManager();
			commandManager.setFlushThreshold(DEFAULT_FLUSH_THRESHOLD);
			commandManager.setFlushCount(DEFAULT_FLUSH_COUNT);
		}

		return commandManager;
	}
    
    /**
     * Returns my delegate operation history.
     * 
     * @return my delegate operation history
     */
    public final IOperationHistory getOperationHistory() {
        return delegate;
    }

	/**
	 * Retrieves the list of commands being managed.
	 * 
	 * @return The list of commands being managed.
	 */
	protected final List getCommands() {

		List result = new ArrayList();

		IUndoableOperation[] undoHistory = delegate
				.getUndoHistory(getUndoContext());

		result.addAll(Arrays.asList(undoHistory));

		IUndoableOperation[] redoHistory = delegate
				.getRedoHistory(getUndoContext());

		result.addAll(Arrays.asList(redoHistory));

		return result;
	}

	/**
	 * Retrieves the list of clisteners listening for changes to this command
	 * manager.
	 * 
	 * @return The listeners.
	 */
	protected final List getListeners() {
		return listeners;
	}

	/**
	 * Retrieves the state of this command manager.
	 * 
	 * @return The state of this command manager.
	 */
	public final State getState() {
		return state;
	}

	/**
	 * Sets the state of this command manager.
	 * 
	 * @param state
	 *            The new state.
	 */
	protected final void setState(State state) {
		this.state = state;
	}

	/**
	 * Retrieves the index of the command that can be undone.
	 * 
	 * @return The index of the command that can be undone.
	 * 
	 * @deprecated Undo index no longer maintained. Returns 0.
	 */
	protected final int getUndoIndex() {
		return 0;
	}

	/**
	 * Sets the index of the command that can be undone.
	 * 
	 * @param undoIndex
	 *            The new index of the command that can be undone.
	 * @deprecated Undo index no longer maintained. Does nothing.
	 */
	protected final void setUndoIndex(int undoIndex) {
		// does nothing
	}

	/**
	 * Retrieves the maximum number of undoable commands that will be remembered
	 * by this command manager.
	 * 
	 * @return The maximum number of undoable commands that will be remembered
	 *         by this command manager.
	 */
	public final int getFlushThreshold() {
		return delegate.getLimit(getUndoContext());
	}

	/**
	 * Sets the maximum number of undoable commands that will be remembered by
	 * this command manager.
	 * 
	 * @param flushThreshold
	 *            The new maximum number of undoable commands that will be
	 *            remembered by this command manager.
	 */
	public final void setFlushThreshold(int flushThreshold) {
		assert (0 < flushThreshold);

		delegate.setLimit(getUndoContext(), flushThreshold);

		fireCommandManagerChange(new CommandManagerChangeEvent(this));
		Trace
				.trace(
						CommonCorePlugin.getDefault(),
						CommonCoreDebugOptions.COMMANDS_ADMIN,
						"Command manager flush threshold set to " + String.valueOf(flushThreshold) + "."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Retrieves the number of undoable commands that are flushed from this
	 * command manager's memory when the flush threshold is exceeded.
	 * 
	 * @return The number of undoable commands that are flushed from this
	 *         command manager's memory when the flush threshold is exceeded.
	 * @deprecated Flush count is fixed at 1.
	 */
	public final int getFlushCount() {
        return flushCount;
	}

	/**
	 * Sets the number of undoable commands that are flushed from this command
	 * manager's memory when the flush threshold is exceeded.
	 * 
	 * @param flushCount
	 *            The new number of undoable commands that are flushed from this
	 *            command manager's memory when the flush threshold is exceeded.
	 * @deprecated Flush count is fixed at 1. This method has no effect.
	 */
	public final void setFlushCount(int flushCount) {
        assert (0 <= flushCount);
        
        this.flushCount = flushCount;
        
        fireCommandManagerChange(new CommandManagerChangeEvent(this));
        Trace
            .trace(
                CommonCorePlugin.getDefault(),
                CommonCoreDebugOptions.COMMANDS_ADMIN,
                "Command manager flush count set to " + String.valueOf(flushCount) + "."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Adds the specified <code>listener</code> to the list of command manager
	 * change listeners for this command manager.
	 * 
	 * @param listener
	 *            The listener to be added.
	 */
	public void addCommandManagerChangeListener(
			ICommandManagerChangeListener listener) {

		assert null != listener : "null listener"; //$NON-NLS-1$
		getListeners().add(listener);
	}

	/**
	 * Removes the specified <code>listener</code> from the list of command
	 * manager change listeners for this command manager.
	 * 
	 * @param listener
	 *            The listener to be removed.
	 */
	public void removeCommandManagerChangeListener(
			ICommandManagerChangeListener listener) {

		assert null != listener : "null listener"; //$NON-NLS-1$
		getListeners().remove(listener);
	}

	/**
	 * Notifies the listeners for this command manager that the specified event
	 * has occurred.
	 * 
	 * @param event
	 *            The command manager change event to be fired.
	 */
	protected void fireCommandManagerChange(CommandManagerChangeEvent event) {
		assert null != event : "null event"; //$NON-NLS-1$

		List targets = null;
		synchronized (getListeners()) {
			targets = new ArrayList(getListeners());
		}

		for (Iterator i = targets.iterator(); i.hasNext();) {
			((ICommandManagerChangeListener) i.next())
					.commandManagerChanged(event);
		}
	}

	/**
	 * Delegates to the operation history.
	 */
	protected void addCommand(ICommand command) {
        
	    // Add the command manager context
        command.addContext(getUndoContext());
        
		delegate.add(command);
	}

	/**
	 * Clears the redo stack.
	 */
	protected void clearRedoCommands() {

		if (canRedo()) {
			State originalState = getState();

			try {

				setState(State.CLEARING);

				IUndoableOperation firstCleared = delegate
						.getRedoOperation(getUndoContext());
				clearRedo();

				if (firstCleared instanceof ICommand) {
					fireCommandManagerChange(new CommandManagerChangeEvent(
							this, (ICommand) firstCleared));
				}

				Trace.trace(CommonCorePlugin.getDefault(),
						CommonCoreDebugOptions.COMMANDS_ADMIN,
						"Command manager redo cleared."); //$NON-NLS-1$

			} finally {
				setState(originalState);
			}
		}
	}

	/**
	 * Clears the redo stack starting at a specified command.
	 * 
	 * @param command
	 *            the command to clear
	 */
	protected void clearRedoCommands(ICommand command) {

		// clear the entire redo stack
		delegate.dispose(getUndoContext(), false, true, false);
	}

	/**
	 * Clears the redo stack starting at <code>index</code> and clearing to
	 * the top (end) of the command list.
	 * 
	 * @param index
	 *            the starting index
	 * @deprecated Clears the entire redo stack.
	 */
	protected void clearRedo(int index) {
		delegate.dispose(getUndoContext(), false, true, false);
	}

	/**
	 * Clears the redo stack.
	 */
	public void clearRedo() {
		delegate.dispose(getUndoContext(), false, true, false);
	}

	/**
	 * Clears the redo stack starting at <code>command</code>, clearing to
	 * the top of the stack.
	 * 
	 * @param command
	 *            the command to be cleared
	 * @deprecated Clears the entire redo stack.
	 */
	public void clearRedoCommand(ICommand command) {
		State originalState = getState();
		try {
			setState(State.CLEARING);

			clearRedoCommands(command);

			fireCommandManagerChange(new CommandManagerChangeEvent(this,
					command));
			Trace.trace(CommonCorePlugin.getDefault(),
					CommonCoreDebugOptions.COMMANDS_ADMIN,
					"Command manager redo cleared."); //$NON-NLS-1$

		} finally {
			setState(originalState);
		}
	}

	/**
	 * Clears the undo stack.
	 */
	protected void clearUndoCommands() {
		delegate.dispose(getUndoContext(), true, false, false);
	}

	/**
	 * Removes commands on the command stack down to, and including, the given
	 * <code>command</code>.
	 * 
	 * @param command
	 *            the command to flush down to
	 * @deprecated Flushes the entire undo stack.
	 */
	protected void flushCommands(ICommand command) {

		flush(1);
	}

	/**
	 * Clears this command manager by emptying its list of undoable and redoable
	 * commands.
	 */
	public final void clear() {
		try {
			setState(State.CLEARING);

			clearRedoCommands();
			clearUndoCommands();

			fireCommandManagerChange(new CommandManagerChangeEvent(this));
			Trace.trace(CommonCorePlugin.getDefault(),
					CommonCoreDebugOptions.COMMANDS_ADMIN,
					"Command manager cleared."); //$NON-NLS-1$

		} finally {
			setState(State.IDLE);
		}
	}

	/**
	 * Removes commands on the command stack down to, and including, the given
	 * <code>command</code>.
	 * 
	 * @param command
	 *            the command to flush down to
	 * @deprecated Flushes the entire undo stack.
	 */
	public final void flush(ICommand command) {

		flushCommands(command);
	}

	/**
	 * Removes <code>flushCount</code> commands from the bottom of the undo
	 * stack.
	 * 
	 * @deprecated Flushes the entire undo stack.
	 */
	protected void flush() {
		flush(getFlushCount());
	}

	/**
	 * Removes <code>count</code> commands from the bottom of the undo stack.
	 * 
	 * @param count
	 *            number of commands to remove from the bottom of the undo stack
	 * @deprecated Flushes the entire undo stack.
	 */
	protected void flush(int count) {

		if (count < 1) {
			return;
		}

		State originalState = getState();

		try {
			setState(State.FLUSHING);

			IUndoableOperation command = delegate
					.getUndoOperation(getUndoContext());

			delegate.dispose(getUndoContext(), true, false, false);

			if (command instanceof ICommand) {
				fireCommandManagerChange(new CommandManagerChangeEvent(this,
						(ICommand) command));
			}

			Trace.trace(CommonCorePlugin.getDefault(),
					CommonCoreDebugOptions.COMMANDS_ADMIN,
					"Command manager flushed up to " + command.getLabel()); //$NON-NLS-1$

		} finally {
			setState(originalState);
		}
	}

	/**
	 * Delegates to the operation history.
	 */
	public boolean canRedo() {
		return delegate.canRedo(getUndoContext());
	}

	/**
	 * Delegates to the operation history.
	 */
	public boolean canUndo() {
		return delegate.canUndo(getUndoContext());
	}

	/**
	 * Retrieves a label for the command that can be redone.
	 * 
	 * @return A label for the command that can be redone.
	 */
	public String getRedoLabel() {
		IUndoableOperation redoOperation = delegate
				.getRedoOperation(getUndoContext());

		if (redoOperation != null) {
			return redoOperation.getLabel();
		}
		return null;
	}

	/**
	 * Retrieves a label for the command that can be undone.
	 * 
	 * @return A label for the command that can be undone.
	 */
	public String getUndoLabel() {
		IUndoableOperation undoOperation = delegate
				.getUndoOperation(getUndoContext());

		if (undoOperation != null) {
			return undoOperation.getLabel();
		}
		return null;
	}

	/**
	 * Executes the specified <code>command</code> without monitoring its
	 * progress.
	 * <P>
	 * The command manager does not support nested command execution. Clients
	 * should never ask the command manager to execute a command while it is
	 * already in the process of executing a command. An exception will occur in
	 * this case.
	 * 
	 * @param command
	 *            The command to be executed.
	 * @return The result of executing the command.
	 * 
	 * @exception UnsupportedOperationException
	 *                If the command cannot be executed.
	 * @exception IllegalStateException
	 *                If the command manager is already in the EXECUTING state.
	 */
	public CommandResult execute(ICommand command) {
		return execute(command, new NullProgressMonitor());
	}

	/**
	 * Executes the specified command while monitoring its progress.
	 * <P>
	 * The <code>progressMonitor</code> must not be <code>null</code>. To
	 * execute a command without a progress, use {@link #execute(ICommand)}.
	 * <P>
	 * The command manager does not support nested command execution. Clients
	 * should never ask the command manager to execute a command while it is
	 * already in the process of executing a command. An exception will occur in
	 * this case.
	 * 
	 * @param command
	 *            The command to be executed.
	 * @param progressMonitor
	 *            The object that monitors the progress of this command
	 *            execution.
	 * @return The result of executing the command.
	 * @exception UnsupportedOperationException
	 *                If the command cannot be executed.
	 * @exception IllegalStateException
	 *                If the command manager is already in the EXECUTING state.
	 */
	public CommandResult execute(ICommand command,
			IProgressMonitor progressMonitor) {

		assert null != progressMonitor;

		if (getState() == State.EXECUTING) {
			throw new IllegalStateException(
					"Command Manager cannot perform the nested command execution while it is already in the process of executing a command."); //$NON-NLS-1$
		}
        
        // Add the command manager context
        command.addContext(getUndoContext());

		if (!command.canExecute()) {
			UnsupportedOperationException uoe = new UnsupportedOperationException(
					"Trying to execute an unexecutable command (" + command.getLabel() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			Trace.throwing(CommonCorePlugin.getDefault(),
					CommonCoreDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"execute", uoe); //$NON-NLS-1$
			throw uoe;
		}

		Log.info(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.OK,
				"Executing command '" + command.getLabel() + "'..."); //$NON-NLS-1$ //$NON-NLS-2$

		CommandResult result = null;

		try {
			setState(State.EXECUTING);
			setCurrentlyExecutingCommand(command);

			delegate.execute(command, progressMonitor, null);

			result = command.getCommandResult();

			if (IStatus.ERROR != result.getStatus().getSeverity()) {

				if (!command.canUndo()) {

					if (!command.getAffectedFiles().isEmpty()) {
						clearUndoCommands();
					}
				}

				fireCommandManagerChange(new CommandManagerChangeEvent(this,
						command));

			} else if (CommonCoreStatusCodes.CANCELLED == result.getStatus()
					.getCode()) {
				// RATLC00529649 - temporary fix - clear redo command when a
				// command is cancelled
				clearRedoCommand(command);
			}

		} catch (ExecutionException e) {
			Log
					.error(
							CommonCorePlugin.getDefault(),
							CommonCoreStatusCodes.COMMAND_FAILURE,
							"Command execution failed: '" + command.getLabel() + "'.", e); //$NON-NLS-1$ //$NON-NLS-2$

		} finally {
			setCurrentlyExecutingCommand(null);
			setState(State.IDLE);
		}

		Trace.trace(CommonCorePlugin.getDefault(),
				CommonCoreDebugOptions.COMMANDS_EXECUTE,
				"Command '" + String.valueOf(command) + "' executed."); //$NON-NLS-1$ //$NON-NLS-2$

		return result;
	}

	/**
	 * Returns the command that is currently being executed, if I am in the
	 * EXECUTING state, or <code>null</code> if I am not executing.
	 * 
	 * @return the command that I am currently executing
	 */
	public ICommand getCurrentlyExecutingCommand() {
		if (getState() == State.EXECUTING) {
			return currentlyExecutingCommand;
		}
		return null;
	}

	/**
	 * Sets the command that I am currently executing if my state is EXECUTING.
	 * Does nothing if I am not in the EXECUTING state.
	 * 
	 * @param command
	 *            the currently executing command
	 */
	private void setCurrentlyExecutingCommand(ICommand command) {
		if (getState() == State.EXECUTING) {
			currentlyExecutingCommand = command;
		}
	}

	/**
	 * Redoes the command on the top of the redo stack.
	 * 
	 * @return The result of redoing the command.
	 * @exception UnsupportedOperationException
	 *                If a command cannot be redone.
	 */
	public CommandResult redo() {

		if (getState() == State.REDOING) {
			// RATLC00138974- Ignore redo() requests while we are already
			// redoing.
			return null;
		}

		if (!canRedo()) {
			UnsupportedOperationException uoe = new UnsupportedOperationException();
			Trace.throwing(CommonCorePlugin.getDefault(),
					CommonCoreDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"redo", uoe); //$NON-NLS-1$
			throw uoe;
		}

		ICommand command = null;
		CommandResult result = null;

		try {
			setState(State.REDOING);

			command = (ICommand) delegate.getRedoOperation(getUndoContext());
			Log.info(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.OK,
					"Redoing command '" + command.getLabel() + "'..."); //$NON-NLS-1$ //$NON-NLS-2$
			delegate.redo(getUndoContext(), new NullProgressMonitor(), null);
			result = command.getCommandResult();

			if (IStatus.ERROR != result.getStatus().getSeverity()) {

				if (!command.canUndo()) {

					if (!command.getAffectedFiles().isEmpty()) {
						clearUndoCommands();
					}
				}

				fireCommandManagerChange(new CommandManagerChangeEvent(this,
						command));
			}
		} catch (ExecutionException e) {
			Log.error(CommonCorePlugin.getDefault(),
					CommonCoreStatusCodes.COMMAND_FAILURE,
					"Command redo failed: '" + command.getLabel() + "'.", e); //$NON-NLS-1$ //$NON-NLS-2$

		} finally {
			setState(State.IDLE);
		}

		Trace.trace(CommonCorePlugin.getDefault(),
				CommonCoreDebugOptions.COMMANDS_REDO,
				"Command '" + String.valueOf(command) + "' redone."); //$NON-NLS-1$ //$NON-NLS-2$
		return result;
	}

	/**
	 * Undoes the command on the top of the undo stack.
	 * 
	 * @return The result of undoing the command.
	 * @exception UnsupportedOperationException
	 *                If a command cannot be undone.
	 */
	public CommandResult undo() {

		if (getState() == State.UNDOING) {
			// RATLC00138974 - Ignore undo() requests while we are already
			// undoing.
			return null;
		}

		if (!canUndo()) {
			UnsupportedOperationException uoe = new UnsupportedOperationException();
			Trace.throwing(CommonCorePlugin.getDefault(),
					CommonCoreDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"undo", uoe); //$NON-NLS-1$
			throw uoe;
		}

		ICommand command = null;
		CommandResult result = null;

		try {
			setState(State.UNDOING);

			command = (ICommand) delegate.getUndoOperation(getUndoContext());
			Log.info(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.OK,
					"Undoing command '" + command.getLabel() + "'..."); //$NON-NLS-1$ //$NON-NLS-2$

			delegate.undo(getUndoContext(), new NullProgressMonitor(), null);

			result = command.getCommandResult();

			if (IStatus.ERROR != result.getStatus().getSeverity()) {

				if (!command.canRedo()) {

					if (!command.getAffectedFiles().isEmpty()) {
						clearRedoCommands();
					}
				}

				fireCommandManagerChange(new CommandManagerChangeEvent(this,
						command));
			}

		} catch (ExecutionException e) {
			Log.error(CommonCorePlugin.getDefault(),
					CommonCoreStatusCodes.COMMAND_FAILURE,
					"Command undo failed: '" + command.getLabel() + "'.", e); //$NON-NLS-1$ //$NON-NLS-2$

		} finally {
			setState(State.IDLE);
		}

		Trace.trace(CommonCorePlugin.getDefault(),
				CommonCoreDebugOptions.COMMANDS_UNDO,
				"Command '" + String.valueOf(command) + "' undone."); //$NON-NLS-1$ //$NON-NLS-2$

		return result;
	}

	/**
	 * Undoes the every command down to, and including, the given command.
	 * 
	 * @param command
	 *            the command to undo to
	 */
	public void undo(ICommand command) {

		List undoHistory = Arrays.asList(delegate
				.getUndoHistory(getUndoContext()));

		int index = undoHistory.indexOf(command);
		assert (index != -1);

		for (int i = 0; i <= index; i++) {
			undo();
		}
	}

	/**
	 * Redoes the every command up to, and including, the given command.
	 * 
	 * @param command
	 *            the command to redo to
	 */
	public void redo(ICommand command) {

		List redoHistory = Arrays.asList(delegate
				.getRedoHistory(getUndoContext()));

		int index = redoHistory.indexOf(command);
		assert (index != -1);

		for (int i = 0; i <= index; i++) {
			redo();
		}
	}

	/**
	 * Gets my undo context. I add my context to all commands executed through
	 * me.
	 * 
	 * @return my undo context
	 */
	public IUndoContext getUndoContext() {
		if (undoContext == null) {
			undoContext = new ObjectUndoContext(this);
		}
		return undoContext;
	}

}