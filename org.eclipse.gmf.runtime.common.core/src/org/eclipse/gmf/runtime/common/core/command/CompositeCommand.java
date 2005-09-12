/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.internal.l10n.ResourceManager;

/**
 * A command that is composed of other
 * {@link org.eclipse.gmf.runtime.common.core.command.ICommand}s which can be
 * undone and redone in a single step.
 * <P>
 * A <code>CompositeCommand</code> can only be executed, undone or redone if
 * all of the {@link org.eclipse.gmf.runtime.common.core.command.ICommand}s
 * with which it is composed are executable, undoable or redoable, respectively.
 * <P>
 * When a <code>CompositeCommand</code> is executed, its commands are not
 * executed independently of one another. This means that if one command
 * execution fails, the remaining commands will not be executed.
 * 
 * @author khussey
 * @canBeSeenBy %partners
 */
public class CompositeCommand
	implements ICommand {

	/**
	 * The empty string.
	 */
	protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * The status message for a cancelled command.
	 */
	protected static final String CANCELLED_MESSAGE = ResourceManager
		.getInstance().getString("AbstractCommand._INFO_.cancelOperation"); //$NON-NLS-1$

	/**
	 * The commands of which this composite command is composed.
	 */
	private final List commands = new ArrayList();

	/**
	 * The label for this composite command.
	 */
	private final String label;

	/**
	 * Flag to indicate whether or not this command was canceled in its last
	 * execution.
	 */
	private boolean canceled = false;

	/**
	 * Creates a new composite command with the specified label.
	 * 
	 * @param label
	 *            The label for the new composite command.
	 */
	public CompositeCommand(String label) {
		super();

		this.label = label;
	}

	/**
	 * Creates a new composite command with the specified label and list of
	 * commands.
	 * 
	 * @param label
	 *            The label for the new composite command.
	 * @param commands
	 *            The initial list of commands
	 */
	public CompositeCommand(String label, List commands) {
		super();

		this.label = label;

		assert null != commands : "null commands"; //$NON-NLS-1$

		for (Iterator i = commands.iterator(); i.hasNext();) {
			ICommand command = (ICommand) i.next();
			compose(command);
		}
	}

	/**
	 * Retrieves the commands with which this command has been composed.
	 * 
	 * @return The commands with which this command has been composed.
	 */
	public final List getCommands() {
		return commands;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getLabel()
	 */
	public final String getLabel() {
		if (label == null)
			if (getCommands().isEmpty())
				return null;
		if (label != null)
			return label;
		return ((ICommand) getCommands().get(0)).getLabel();
	}

	/**
	 * Retrieves the composite result of executing, undoing, or redoing this
	 * composite command.
	 * 
	 * @return A command result composed of the results of
	 *         executing, undoing or redoing the commands of which this composite
	 *         command is composed.
	 */
	public CommandResult getCommandResult() {

		if (isCanceled()) {
			return newCancelledCommandResult();
		}

		List statuses = new ArrayList();
		List returnValues = new ArrayList();

		int severity = IStatus.OK;

		String plugin = CommonCorePlugin.getPluginId();
		int code = CommonCoreStatusCodes.OK;
		String message = EMPTY_STRING;
		Throwable exception = null;

		for (Iterator i = getCommands().iterator(); i.hasNext();) {
			ICommand command = (ICommand) i.next();

			CommandResult result = command.getCommandResult();

			if (result == null) {
				// the result can be null if only some of the commands have been
				// executed (e.g., the action was abandoned)
				break;
			}

			IStatus status = result.getStatus();
			statuses.add(result.getStatus());

			if (severity < status.getSeverity()) {
				severity = status.getSeverity();
				plugin = status.getPlugin();
				code = status.getCode();
				message = status.getMessage();
				exception = status.getException();
			}

			Object returnValue = result.getReturnValue();
			if (returnValue != null) {
				if (getClass().isInstance(command)) {
					if (returnValue != null && returnValue instanceof Collection) {
						returnValues.addAll((Collection) returnValue);
					} else {
						returnValues.add(returnValue);
					}
				} else {
					returnValues.add(returnValue);
				}
			}
		}

		return new CommandResult(
			new MultiStatus(plugin, code, (IStatus[]) statuses
				.toArray(new IStatus[] {}), message, exception), returnValues);
	}

	/**
	 * Retrieves the collection of objects that would be affected if this
	 * composite command were executed, undone, or redone.
	 * 
	 * @return A collection containing the affected objects of all of the
	 *         commands of which this composite command is composed.
	 */
	public final Collection getAffectedObjects() {
		List affectedObjects = new ArrayList();

		for (Iterator i = getCommands().iterator(); i.hasNext();) {

			Collection coll = ((ICommand) i.next()).getAffectedObjects();
			if (coll != null) {
				affectedObjects.addAll(coll);
			}
		}

		return affectedObjects;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#involvesReadOnlyNonWorkSpaceFiles()
	 */
	public boolean involvesReadOnlyNonWorkSpaceFiles() {
		for (Iterator i = getCommands().iterator(); i.hasNext();) {
			if (((ICommand) i.next()).involvesReadOnlyNonWorkSpaceFiles())
				return true;
		}
		return false;
	}

	/**
	 * Adds <code>command</code> to the list of commands with which this
	 * composite is composed.
	 * 
	 * @param command
	 *            The command with which to compose this command.
	 * @return <code>this</code>.
	 */
	public final ICommand compose(ICommand command) {
		if (command != null)
			getCommands().add(command);
		return this;
	}

	/**
	 * Answers whether this composite command can
	 * be executed.
	 * 
	 * @return <code>false</code> if any of the commands of which this
	 *         composite command is composed cannot be executed;
	 *         <code>true</code> otherwise.
	 */
	public final boolean isExecutable() {
		if (getCommands().isEmpty())
			return false;
		for (Iterator i = getCommands().iterator(); i.hasNext();) {

			if (!((ICommand) i.next()).isExecutable()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Answers whether this composite command can
	 * be redone.
	 * 
	 * @return <code>false</code> if any of the commands of which this
	 *         composite command is composed cannot be redone; <code>true</code>
	 *         otherwise.
	 */
	public final boolean isRedoable() {
		if (getCommands().isEmpty())
			return false;
		for (Iterator i = getCommands().iterator(); i.hasNext();) {

			if (!((ICommand) i.next()).isRedoable()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Answers whether this composite command can
	 * be undone.
	 * 
	 * @return <code>false</code> if any of the commands of which this
	 *         composite command is composed cannot be undone; <code>true</code>
	 *         otherwise.
	 */
	public final boolean isUndoable() {
		if (getCommands().isEmpty())
			return false;
		for (Iterator i = getCommands().iterator(); i.hasNext();) {

			if (!((ICommand) i.next()).isUndoable()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns whether the composite command has no child commands.
	 * 
	 * @return whether the composite command has no child commands.
	 */
	public final boolean isEmpty() {
		return getCommands().size() == 0;
	}

	/**
	 * Returns the simplest form of this command that is equivalent. This is
	 * useful for removing unnecessary nesting of commands.
	 * <UL>
	 * <LI>if the composite had no sub-commands, it returns <code>null</code>
	 * </LI>
	 * <LI>if the composite had a single command, it returns the single command
	 * </LI>
	 * <LI>otherwise, it returns itself</LI>
	 * </UL>
	 * 
	 * @return the simplest form of this command that is equivalent
	 */
	public ICommand unwrap() {
		switch (commands.size()) {
			case 0:
				return UnexecutableCommand.INSTANCE;
			case 1:
				return (ICommand) commands.get(0);
			default:
				return this;
		}
	}

	/**
	 * Executes this composite command by executing all of the commands with
	 * which it is composed. If one command execution fails, the remaining
	 * commands will not be executed.
	 * <P>
	 * The result of executing this command can be obtained by calling
	 * {@link #getCommandResult()}after the command has been executed.
	 * <P>
	 * The progress of this command execution is measured in the following way:
	 * each of <code>n</code> subcommands is allocated 1 of <code>n</code>
	 * work units from <code>progressMonitor</code>. Each sub-command
	 * execution is given a {@link SubProgressMonitor}and can futher divide its
	 * 1/<code>n</code> th into <code>m</code> work units. Command
	 * execution will stop when the progress monitor is cancelled and a
	 * {@link CommonCoreStatusCodes#CANCELLED}status code will be returned in
	 * the command result. All of the previously executed sub-commands will be
	 * undone as a result of cancelling.
	 * 
	 * @param progressMonitor @see org.eclipse.core.runtime.IProgressMonitor
	 */
	public void execute(IProgressMonitor progressMonitor) {

		if (!getValidator().okToEdit(this)) {
			setCanceled(true);

		} else {

			IProgressMonitor monitor = (progressMonitor == null) ? new NullProgressMonitor()
				: progressMonitor;

			setCanceled(false);
			List executedCommands = new ArrayList(getCommands().size());

			int totalWork = getCommands().size();
			monitor.beginTask(getLabel(), totalWork);

			for (Iterator i = getCommands().iterator(); i.hasNext();) {

				SubProgressMonitor subprogressMonitor = new SubProgressMonitor(
					monitor, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL);

				ICommand nextCommand = (ICommand) i.next();
				nextCommand.execute(subprogressMonitor);

				CommandResult result = nextCommand.getCommandResult();
				if (result != null) {
					if (result.getStatus().getSeverity() == IStatus.ERROR) {
						/*
						 * myee - RATLC00518953: error executing one of the
						 * composed commands: cancel all of the executed
						 * commands
						 */
						undoCancelledCommands(executedCommands);
						return;
					}
				}
				monitor.worked(1);

				if (monitor.isCanceled()) {
					undoCancelledCommands(executedCommands);
					monitor.done();
					setCanceled(true);
					return;
				}
				executedCommands.add(nextCommand);
			}

			monitor.done();

		}
	}

	/**
	 * Cancels the command execution by calling <code>undo()</code> on all of
	 * the undoable commands that were executed before the composite command was
	 * cancelled. The commands are undone in the reverse order of execution.
	 * 
	 * @param executedCommands
	 *            the commands that have been executed and need to be undone.
	 *            This method expects that the commands in the list are in the
	 *            order in which the commands were executed. They will be undone
	 *            in the reverse order.
	 */
	protected void undoCancelledCommands(List executedCommands) {

		Collections.reverse(executedCommands);

		for (Iterator i = executedCommands.iterator(); i.hasNext();) {
			ICommand nextCommand = (ICommand) i.next();
			if (nextCommand.isUndoable()) {
				nextCommand.undo();
			}
		}
	}

	/**
	 * Redoes this composite command by redoing each of the commands of which
	 * this composite command is composed.
	 */
	public void redo() {
		// First check if we have the needed units available.
		// We are forced to do this at the composite command level
		// because some individual commands do not properly set their
		// affectedObject. For example, create a class and notice that
		// the SetBounds command will not have its affected object set
		// even though it clearly modifies a unit.
		if (!getValidator().okToEdit(this)) {
			setCanceled(true);
			return;
		}

		Collections.reverse(getCommands());

		for (Iterator i = getCommands().iterator(); i.hasNext();) {
			((ICommand) i.next()).redo();
		}
	}

	/**
	 * Undoes this composite command by undoing each of the commands of which
	 * this composite command is composed.
	 */
	public void undo() {
		// First check if we have the needed units available.
		// We are forced to do this at the composite command level
		// because some individual commands do not properly set their
		// affectedObject. For example, create a class and notice that
		// the SetBounds command will not have its affected object set
		// even though it clearly modifies a unit.
		if (!getValidator().okToEdit(this)) {
			setCanceled(true);
			return;
		}

		Collections.reverse(getCommands());

		for (Iterator i = getCommands().iterator(); i.hasNext();) {
			((ICommand) i.next()).undo();
		}
	}

	/**
	 * Retrieves the plug-in identifier to be used in command results produced
	 * by this command.
	 * 
	 * @return The plug-in identifier to be used in command results produced by
	 *         this command.
	 */
	protected String getPluginId() {
		return CommonCorePlugin.getPluginId();
	}

	/**
	 * Creates a new command result with an ERROR status, a CANCELLED status
	 * code and no return value.
	 * 
	 * @return A new command result with an ERROR status and a CANCELLED status
	 *         code.
	 */
	protected CommandResult newCancelledCommandResult() {
		return new CommandResult(new Status(IStatus.ERROR, getPluginId(),
			CommonCoreStatusCodes.CANCELLED, CANCELLED_MESSAGE, null), null);
	}

	/**
	 * Sets the canceled state of this command.
	 * 
	 * @param canceled
	 *            <code>true</code> if the command was canceled,
	 *            <code>false</code> otherwise.
	 */
	protected void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	/**
	 * Gets the canceled state of this command.
	 * 
	 * @return <code>true</code> if the command was canceled,
	 *         <code>false</code> otherwise.
	 */
	protected boolean isCanceled() {
		return canceled;
	}

	/**
	 * Return a validator which can be used to check whether the units being
	 * modified by a command are writable.
	 * 
	 * @return CMValidator
	 */
	public CMValidator getValidator() {
		return new CMValidator();
	}

}