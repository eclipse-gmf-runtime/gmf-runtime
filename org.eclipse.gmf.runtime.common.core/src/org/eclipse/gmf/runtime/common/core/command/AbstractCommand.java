/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.command;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;

import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;

/**
 * An abstract implementation of the
 * {@link org.eclipse.gmf.runtime.common.core.command.ICommand} interface.
 * Logging, exception handling and file edit validation are done in a uniform
 * way in the {@link #execute(IProgressMonitor)} method.
 * <P>
 * Concrete subclasses must implement the <code>doExecute(IProgressMonitor)</code>
 * method to perform their task.
 * <P>
 * By default this command is neither redoable nor undoable. Subclasses must
 * override the {@link #isUndoable()}and {@link #isRedoable()} methods to allow
 * the command to be undone or redone and implement the {@link #redo()}and
 * {@link #undo()}methods.
 * 
 * @author khussey
 * 
 * @see org.eclipse.gmf.runtime.common.core.command.ICommand
 * @canBeSeenBy %partners
 */
public abstract class AbstractCommand implements ICommand {

	/**
	 * The empty string.
	 */
	protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * The status message for a cancelled command.
	 */
	protected static final String CANCELLED_MESSAGE = ResourceManager.getInstance().getString("AbstractCommand._INFO_.cancelOperation"); //$NON-NLS-1$
	
	/**
	 * The label for this command.
	 */
	private final String label;

	/**
	 * The result of executing, undoing, or redoing this command.
	 */
	private CommandResult result = null;

	/**
	 * Creates a new command with the specified label.
	 * 
	 * @param label The label for the new command.
	 */
	protected AbstractCommand(String label) {
		super();

		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 */
	public final CommandResult getCommandResult() {
		return result;
	}

	/**
	 * Sets the <code>result</code> instance variable to the specified value.
	 * 
	 * @param result The new value for the <code>result</code> instance
	 *                variable.
	 */
	protected final void setResult(CommandResult result) {
		this.result = result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		return Collections.EMPTY_LIST;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#involvesReadOnlyNonWorkSpaceFiles()
	 */
	public boolean involvesReadOnlyNonWorkSpaceFiles() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getValidator()
	 */
	public CMValidator getValidator() {
		return new CMValidator();
	}
	
	/**
	 * Retrieves the plug-in identifier to be used in command results produced
	 * by this command.
	 * 
	 * @return The plug-in identifier to be used in command results produced by
	 *          this command.
	 */
	protected String getPluginId() {
		return CommonCorePlugin.getPluginId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#compose(org.eclipse.gmf.runtime.common.core.command.ICommand)
	 */
	public ICommand compose(ICommand command) {
		assert null != command : "null command"; //$NON-NLS-1$

		return new CompositeCommand(getLabel()).compose(this).compose(command);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isExecutable()
	 */
	public boolean isExecutable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isRedoable()
	 */
	public boolean isRedoable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isUndoable()
	 */
	public boolean isUndoable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void execute(IProgressMonitor progressMonitor) {

		IProgressMonitor monitor =
			(progressMonitor == null)
				? new NullProgressMonitor()
				: progressMonitor;

		try {
			if (getValidator().okToEdit(this)) { 
				setResult(doExecute(monitor));
			} else {
				// We are not going to do the undo/redo.
				// We do not want the stack affected so we must return an appropriate result.
				// This way the caller will know that the undo/redo was not sucessful and will
				// not adjust the stack.
				setResult(newCancelledCommandResult());	
			}
		} catch (Exception e) {
			handle(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#redo()
	 */
	public void redo() {

		try {
			// First check if we have access to the units to be modified.
			if (getValidator().okToEdit(this)) {
				setResult(doRedo());
			} else {
				// We are not going to do the undo/redo.
				// We do not want the stack affected so we must return an appropriate result.
				// This way the caller will know that the undo/redo was not sucessful and will
				// not adjust the stack.
				setResult(newCancelledCommandResult());
			}
		} catch (Exception e) {
			handle(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void undo() {

		try {
			// First check if we have access to the units to be modified.
			if (getValidator().okToEdit(this)) {
				setResult(doUndo());
			} else {
				// We are not going to do the undo/redo.
				// We do not want the stack affected so we must return an appropriate result.
				// This way the caller will know that the undo/redo was not sucessful and will
				// not adjust the stack.
				setResult(newCancelledCommandResult());
			}
		} catch (Exception e) {
			handle(e);
		}
	}

	/**
	 * Creates a new command result with an OK status.
	 * 
	 * @return A new command result with an OK status.
	 * 
	 */
	protected CommandResult newOKCommandResult() {
		return new CommandResult(
			new Status(
				IStatus.OK,
				getPluginId(),
				CommonCoreStatusCodes.OK,
				EMPTY_STRING,
				null));
	}

	/**
	 * Creates a new command result with an OK status and the specified return
	 * value.
	 * 
	 * @return A new command result with an OK status.
	 * @param returnValue The return value for the new command result.
	 * 
	 */
	protected CommandResult newOKCommandResult(Object returnValue) {
		return new CommandResult(
			new Status(
				IStatus.OK,
				getPluginId(),
				CommonCoreStatusCodes.OK,
				EMPTY_STRING,
				null),
			returnValue);
	}
	
	/**
	 * Creates a new command result with an ERROR status , a CANCELLED status
	 * code and no return value.
	 * 
	 * @return A new command result with an ERROR status.
	 */
	protected CommandResult newCancelledCommandResult() {
		return new CommandResult(
			new Status(
				IStatus.ERROR,
				getPluginId(),
				CommonCoreStatusCodes.CANCELLED,
				CANCELLED_MESSAGE,
				null),
			null);
	}
	
	/**
	 * Creates a new command result with an ERROR status, a COMMAND_FAILURE
	 * status code, and no return value.
	 * 
	 * @param errorMessage error message
	 * @return A new command result with an ERROR status.
	 */
	protected CommandResult newErrorCommandResult(String errorMessage) {
		return new CommandResult(
			new Status(
				IStatus.ERROR,
				getPluginId(),
				CommonCoreStatusCodes.COMMAND_FAILURE,
				errorMessage,
				null));
	}
	
	/**
	 * Creates a new command result with an WARNING status, a OK
	 * status code, and no return value.
	 * 
	 * @param warningMessage the warning
	 * @param returnValue the return value for the new command result
	 * @return A new command result with a WARNING status.
	 */
	protected CommandResult newWarningCommandResult(String warningMessage,
			Object returnValue) {
		return new CommandResult(new Status(IStatus.WARNING, getPluginId(),
			CommonCoreStatusCodes.OK, warningMessage, null), returnValue);
	}
	
	/**
	 * Handles the specified exception.
	 * 
	 * @param exception
	 *            The exception to be handled.
	 */
	protected void handle(Exception exception) {
		Trace.catching(CommonCorePlugin.getDefault(), CommonCoreDebugOptions.EXCEPTIONS_CATCHING, getClass(), "handle", exception); //$NON-NLS-1$

		setResult(
			new CommandResult(
				new Status(
					IStatus.ERROR,
					getPluginId(),
					CommonCoreStatusCodes.COMMAND_FAILURE,
					String.valueOf(exception.getMessage()),
					exception)));

		Log.log(CommonCorePlugin.getDefault(), getCommandResult().getStatus());
	}

	/**
	 * Performs the actual work of executing this command. Subclasses must
	 * implement this method to perform some operation.
	 * 
	 * @param progressMonitor
	 *            The object that monitors the progress of this command
	 *            execution. May be
	 *            {@link org.eclipse.core.runtime.NullProgressMonitor}if the
	 *            command should be executed without monitoring its progress.
	 * @return The result of executing this command.
	 */
	protected abstract CommandResult doExecute(IProgressMonitor progressMonitor);

	/**
	 * Performs the actual work of redoing this command. Subclasses must
	 * override this method if the command is to be redoable.
	 * 
	 * @return The result of redoing this command.
	 * @exception UnsupportedOperationException
	 *                If this command isn't redoable.
	 */
	protected CommandResult doRedo() {
		UnsupportedOperationException uoe = new UnsupportedOperationException();
		Trace.throwing(CommonCorePlugin.getDefault(), CommonCoreDebugOptions.EXCEPTIONS_THROWING, getClass(), "doRedo", uoe); //$NON-NLS-1$
		throw uoe;
	}

	/**
	 * Performs the actual work of undoing this command. Subclasses must
	 * override this method if the command is to be undoable.
	 * 
	 * @return The result of undoing this command.
	 * @exception UnsupportedOperationException
	 *                If this command isn't undoable.
	 */
	protected CommandResult doUndo() {
		UnsupportedOperationException uoe = new UnsupportedOperationException();
		Trace.throwing(CommonCorePlugin.getDefault(), CommonCoreDebugOptions.EXCEPTIONS_THROWING, getClass(), "doUndo", uoe); //$NON-NLS-1$
		throw uoe;
	}

}
