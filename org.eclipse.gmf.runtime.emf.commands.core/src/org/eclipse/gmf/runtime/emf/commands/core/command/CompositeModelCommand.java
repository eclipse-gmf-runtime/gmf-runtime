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

package org.eclipse.gmf.runtime.emf.commands.core.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.core.command.compatibility.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.commands.core.internal.MSLCommandsDebugOptions;
import org.eclipse.gmf.runtime.emf.commands.core.internal.MSLCommandsPlugin;
import org.eclipse.gmf.runtime.emf.commands.core.internal.MSLCommandsStatusCodes;
import org.eclipse.gmf.runtime.emf.commands.core.internal.l10n.EMFCommandsCoreMessages;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;

/**
 * A composite model command, meaning a model command that is composed of other
 * commands. All composite model commands have an associated undo interval,
 * through which they can be undone or redone.
 * 
 * @author khussey
 * @deprecated Use {@link CompositeTransactionalCommand} instead.
 */
public class CompositeModelCommand
	extends CompositeCommand
	implements IUndoIntervalCommand {
	
	/**
	 * The undo interval title for this composite model command.
	 *  
	 */
	private final String undoIntervalTitle;

	/**
	 * The undo interval for this composite model command.
	 *  
	 */
	private MUndoInterval undoInterval = null;

	/**
	 * Constructs a new composite model command with the specified label and
	 * model operation context, and a default undo interval title and key.
	 * 
	 * @param label
	 *            The label for the new composite model command.
	 *  
	 */
	public CompositeModelCommand(String label) {
		this(label, label);
	}

	/**
	 * Constructs a new composite model command with the specified label, model
	 * operation context, undo interval title, and undo interval key.
	 * 
	 * @param label
	 *            The label for the new composite model command.
	 * @param undoIntervalTitle
	 *            The undo interval title for the new composite model command.
	 *  
	 */
	public CompositeModelCommand(String label, String undoIntervalTitle) {
		this(label, undoIntervalTitle, new ArrayList());
	}

	/**
	 * Constructs a new composite model command with the specified label, model
	 * operation context, and a list of commands
	 * 
	 * @param label
	 *            The label for the new composite model command.
	 * @param commands
	 *            The initial list of commands
	 *  
	 */
	public CompositeModelCommand(String label, List commands) {
		this(label, label, commands);
	}

	/**
	 * Constructs a new composite model command with the specified label, model
	 * operation context, undo interval title, undo interval key and list of
	 * commands.
	 * 
	 * @param label
	 *            The label for the new composite model command.
	 * @param undoIntervalTitle
	 *            The undo interval title for the new composite model command.
	 * @param commands
	 *            The initial list of commands
	 *  
	 */
	public CompositeModelCommand(String label, String undoIntervalTitle,
			List commands) {
		super(label, commands);

		this.undoIntervalTitle = undoIntervalTitle;
	}

	/**
	 * Retrieves the value of the <code>undoIntervalTitle</code> instance
	 * variable.
	 * 
	 * @return The value of the <code>undoIntervalTitle</code> instance
	 *         variable.
	 *  
	 */
	protected final String getUndoIntervalTitle() {
		return undoIntervalTitle;
	}

	/**
	 * Retrieves the value of the <code>undoInterval</code> instance variable.
	 * 
	 * @return The value of the <code>undoInterval</code> instance variable.
	 * @see org.eclipse.gmf.runtime.emf.commands.core.command.IUndoIntervalCommand#getUndoInterval()
	 */
	public final MUndoInterval getUndoInterval() {
		return undoInterval;
	}

	/**
	 * Sets the <code>undoInterval</code> instance variable to the specified
	 * value.
	 * 
	 * @param undoInterval
	 *            The new value for the <code>undoInterval</code> instance
	 *            variable.
	 *  
	 */
	protected final void setUndoInterval(MUndoInterval undoInterval) {
		this.undoInterval = undoInterval;
	}

	/**
	 * Handles the specified exception.
	 * 
	 * @param exception
	 *            The exception to be handled.
	 *  
	 */
	protected void handle(Exception exception) {
		Trace.catching(MSLCommandsPlugin.getDefault(),
			MSLCommandsDebugOptions.EXCEPTIONS_CATCHING, getClass(),
			"handle", exception); //$NON-NLS-1$

		setResult(new CommandResult(new Status(IStatus.ERROR, MSLCommandsPlugin
			.getPluginId(), MSLCommandsStatusCodes.MODEL_COMMAND_FAILURE, String
			.valueOf(exception.getMessage()), exception)));

		Log.log(MSLCommandsPlugin.getDefault(), getCommandResult().getStatus());
	}

	/**
	 * Executes this composite model command in an undo interval.
	 * <P>
	 * The <code>progressMonitor</code> must not have already begun a task.
	 * This method will begin a task on the progress monitor with the amount of
	 * work equal to the number of subcommands in this composite. Each
	 * subcommand will be executed with a {@link SubProgressMonitor}that will
	 * consume one unit of work in <code>progressMonitor</code>.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#execute(IProgressMonitor)
	 */
	public final void execute(final IProgressMonitor progressMonitor) {

		if (!getValidator().okToEdit(this)) {
			setResult(newCancelledCommandResult());
			
		} else {

			final IProgressMonitor monitor = (progressMonitor == null) ? new NullProgressMonitor()
				: progressMonitor;

			final List[] executedCommands = new List[1];

			if (OperationUtil.isUncheckedInProgress()) {

				executedCommands[0] = executeCommands(monitor);

			} else {
				
				final MRunnable runnable = new MRunnable() {

					public Object run() {
						try {
							executedCommands[0] = executeCommands(monitor);

						} finally {

							if (isCanceled()) {
								// Abandon the write action so that
								// validation is not done and events are
								// not fired.
								this.abandon();
								
							}
						}
						return null;
					}
				};

				setUndoInterval(OperationUtil.runInUndoInterval(
					getUndoIntervalTitle(), new Runnable() {

						public void run() {
							try {
								OperationUtil.runAsWrite(runnable);
							} catch (MSLActionAbandonedException e) {
								// set the result status to ERROR
								handleActionAbandoned(e);
							}
						}
					}));
			}

			if (isCanceled()) {
				undoCancelledCommands(executedCommands[0]);
			}
		}	
	}
	
	/**
	 * Handles the action abandoned exception by setting an error result.
	 */
	protected void handleActionAbandoned(MSLActionAbandonedException e) {
		// No need to consider a null undo interval here because
		// such actions are never validated, so the abandon action event should
		// never be fired.
		setResult(new CommandResult(new Status(IStatus.ERROR, getPluginId(),
			MSLCommandsStatusCodes.VALIDATION_FAILURE, 
			EMFCommandsCoreMessages.AbstractModelCommand__ERROR__abandonedActionErrorMessage,
			null)));
		
		Trace.trace(MSLCommandsPlugin.getDefault(), MSLCommandsDebugOptions.MODEL_OPERATIONS, "MSLActionAbandonedException"); //$NON-NLS-1$
	}

	/**
	 * @param monitor
	 */
	private List executeCommands(final IProgressMonitor monitor) {

		setCanceled(false);
		int totalWork = getCommands().size();
		List executedCommands = new ArrayList(totalWork);
		monitor.beginTask(getLabel(), totalWork);

		for (Iterator i = getCommands().iterator(); i.hasNext();) {
			
			SubProgressMonitor subprogressMonitor = new SubProgressMonitor(
				monitor, 1);

			subprogressMonitor.beginTask(getLabel(), 1);

			ICommand nextCommand = (ICommand) i.next();
			nextCommand.execute(subprogressMonitor);
			executedCommands.add(nextCommand);
			
			subprogressMonitor.done();

			if (monitor.isCanceled()) {
				monitor.done();
				setResult(newCancelledCommandResult());
				setCanceled(true);
				return executedCommands;
			}
		}

		setResult(CompositeModelCommand.super.getCommandResult());
		monitor.done();
		return executedCommands;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.internal.command.CompositeCommand#undoCancelledCommands(java.util.List)
	 */
	protected void undoCancelledCommands(List executedCommands) {

		if (getUndoInterval() != null) {
			getUndoInterval().undo();
		}
	}

	/**
	 * Redoes this composite model command via its client.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#redo()
	 *  
	 */
	public void redo() {
		if (!getValidator().okToEdit(this)) {
			setCanceled(true);
			return;
		}

		if (getUndoInterval() != null) {
			try {
				getUndoInterval().redo();
				setResult(newOKCommandResult());
				Trace.trace(MSLCommandsPlugin.getDefault(),
					MSLCommandsDebugOptions.MODEL_OPERATIONS,
					"Composite Model Command - Redo"); //$NON-NLS-1$                
			} catch (Exception e) {
				handle(e);
			}
		}
	}

	/**
	 * Undoes this composite model command via its client.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#undo()
	 *  
	 */
	public void undo() {
		if (!getValidator().okToEdit(this)) {
			setCanceled(true);
			return;
		}

		if (getUndoInterval() != null) {
			try {
				getUndoInterval().undo();
				setResult(newOKCommandResult());
				Trace.trace(MSLCommandsPlugin.getDefault(),
					MSLCommandsDebugOptions.MODEL_OPERATIONS,
					"Composite Model Command - Undo"); //$NON-NLS-1$                
			} catch (Exception e) {
				handle(e);
			}
		}
	}
	
	public ICommand unwrap() {
		
		switch (getCommands().size()) {
			case 0:
				return UnexecutableCommand.INSTANCE;
				
			case 1:
				ICommand command = (ICommand) getCommands().get(0);

				if (command instanceof CompositeModelCommand || command instanceof AbstractModelCommand) {
					return command;
				}
		}
		return this;
	}

}