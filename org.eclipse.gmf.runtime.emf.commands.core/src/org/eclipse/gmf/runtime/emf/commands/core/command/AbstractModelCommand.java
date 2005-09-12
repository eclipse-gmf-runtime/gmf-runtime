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

package org.eclipse.gmf.runtime.emf.commands.core.command;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.commands.core.internal.MSLCommandsDebugOptions;
import org.eclipse.gmf.runtime.emf.commands.core.internal.MSLCommandsPlugin;
import org.eclipse.gmf.runtime.emf.commands.core.internal.MSLCommandsStatusCodes;
import org.eclipse.gmf.runtime.emf.commands.core.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;

/**
 * The abstract parent of all concrete commands that perform model operations.
 * Concrete subclasses must provide a definition of the <code>doExecute()</code>
 * method to perform some operation. All model commands have an associated undo 
 * interval, through which they can be undone or redone.
 * <p>
 * By default, all model commands are redoable and undoable. When asked to undo
 * or redo itself, a model command will delegate to its undo interval to 
 * undo or redo. If, however, its undo interval is <code>null</code>  the model 
 * command will resort to its <code>doUndo()</code> or
 * <code>doRedo()</code> method to perform the undo or redo. Model commands with
 * custom undo and redo behavior (i.e. EMF commands) can thus be defined by
 * overriding the <code>doUndo()</code> and <code>doRedo()</code> methods and
 * ensuring that the undo interval ID returned by the model service provider is
 * invalid for such commands.
 * 
 * @author khussey
 * 
 * @see org.eclipse.gmf.runtime.emf.core.internal.MUndoInterval
 */
public abstract class AbstractModelCommand
	extends AbstractCommand
	implements IUndoIntervalCommand {

	/**
	 * Error message to display when the write action has been abandoned
	 * because a live constraint has been violated.
	 */
	private final static String ABANDONED_ACTION_ERROR = ResourceManager
		.getInstance().getString("AbstractModelCommand._ERROR_.abandonedActionErrorMessage"); //$NON-NLS-1$

	/**
	 * Element or file used to determine affected objects.  See getAffectedObjects() 
	 */
	Object affectedObjects;

	/**
	 * The undo interval for this model command.
	 */
	private MUndoInterval undoInterval = null;

	/**
	 * Constructs a new model command with the specified label and model
	 * operation context.
	 * 
	 * @param label The label for the new model command.
	 * @param affectedObjects The model operation context for the new model command.
	 */
	protected AbstractModelCommand(String label, Object affectedObjects) {

		super(label);

		this.affectedObjects = affectedObjects;
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
	 * Retrieves the plug-in identifier to be used in command results produced
	 * by this model command.
	 * 
	 * @return The plug-in identifier to be used in command results produced by
	 *          this model command.
	 */
	protected String getPluginId() {
		return MSLCommandsPlugin.getPluginId();
	}

	/**
	 * Sets the <code>undoIntervalId</code> instance variable to the specified
	 * value.
	 * 
	 * @param undoInterval The new value for the <code>undoIntervalId</code>
	 *                        instance variable.
	 */
	protected final void setUndoInterval(MUndoInterval undoInterval) {
		this.undoInterval = undoInterval;
	}

	/**
	 * Retrieves the undo interval title for this model command. By default, the
	 * label for this command is used as the undo interval title.
	 * 
	 * @return The undo interval title for this model command.
	 */
	protected String getUndoIntervalTitle() {
		return getLabel();
	}

	/**
	 * Handles the specified exception.
	 * 
	 * @param exception The exception to be handled.
	 * 
	 */
	protected void handle(Exception exception) {
		Trace.catching(MSLCommandsPlugin.getDefault(),
			MSLCommandsDebugOptions.EXCEPTIONS_CATCHING, getClass(), "handle", //$NON-NLS-1$
			exception);

		setResult(new CommandResult(new Status(IStatus.ERROR, getPluginId(),
			MSLCommandsStatusCodes.MODEL_COMMAND_FAILURE, String.valueOf(exception
				.getMessage()), exception)));

		Log.log(MSLCommandsPlugin.getDefault(), getCommandResult().getStatus());
	}

	/**
	 * Composes this model command with the specified command by creating a
	 * composite model command composed of this model command and the specified
	 * command.
	 * 
	 * @return A composite model command composed of this model command and the
	 *          specified command.
	 * @param command The command with which to compose this model command.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#compose(ICommand)
	 * 
	 */
	public final ICommand compose(ICommand command) {
		assert command != null: "command is null"; //$NON-NLS-1$

		return new CompositeModelCommand(getLabel(), getUndoIntervalTitle())
			.compose(this).compose(command);
	}

	/**
	 * Retrieves a Boolean indicating whether this model command can be redone.
	 * 
	 * @return <code>true</code>.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isRedoable()
	 */
	public boolean isRedoable() {
		return true;
	}

	/**
	 * Retrieves a Boolean indicating whether this model command can be undone.
	 * 
	 * @return <code>true</code>.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isUndoable()
	 */
	public boolean isUndoable() {
		return true;
	}

	/**
	 * Executes this model command as a write action (in an undo interval).
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#execute(IProgressMonitor)
	 */
	public final void execute(final IProgressMonitor progressMonitor) {
		
		if (OperationUtil.isUncheckedInProgress()) {

			AbstractModelCommand.super.execute(progressMonitor);

		} else {

			final MRunnable runnable = new MRunnable() {

				public Object run() {
					AbstractModelCommand.super.execute(progressMonitor);
					return null;
				}
			};

			setUndoInterval(OperationUtil.runInUndoInterval(
				getUndoIntervalTitle(), new Runnable() {

					public void run() {
						try {
							OperationUtil.runAsWrite(runnable);
						} catch (MSLActionAbandonedException e) {
							handleActionAbandoned(e);
						}
					}
				}));

			if (progressMonitor.isCanceled()) {
				// Undo the model command if the monitor was canceled.
				undo();
			}
		}

		cleanup();
	}

	/**
	 * Redoes this model command via its client.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#redo()
	 */
	public void redo() {
		if (!getValidator().okToEdit(this)) {
			setResult(newCancelledCommandResult());
			return;
		}

		if (getUndoInterval() != null) {
			try {
				getUndoInterval().redo();
				setResult(newOKCommandResult());
				Trace.trace(MSLCommandsPlugin.getDefault(),
					MSLCommandsDebugOptions.MODEL_OPERATIONS,
					"Abstract Model Command - Redo"); //$NON-NLS-1$
			} catch (Exception e) {
				handle(e);
			}
		}
	}

	/**
	 * Undoes this model command via its client.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#undo()
	 */
	public void undo() {
		if (!getValidator().okToEdit(this)) {
			setResult(newCancelledCommandResult());
			return;
		}

		if (getUndoInterval() != null) {
			try {
				getUndoInterval().undo();
				setResult(newOKCommandResult());
				Trace.trace(MSLCommandsPlugin.getDefault(),
					MSLCommandsDebugOptions.MODEL_OPERATIONS,
					"Abstract Model Command - Undo"); //$NON-NLS-1$
			} catch (Exception e) {
				handle(e);
			}
		}
	}

	/**
	 * Retrieves the collection of objects that would be affected if this
	 * command were executed, undone, or redone.
	 * 
	 * @return An empty collection by default.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 * 
	 */
	public Collection getAffectedObjects() {
		if (null != affectedObjects)
			return getWorkspaceFilesFor(affectedObjects);
		return Collections.EMPTY_LIST;
	}	
	
	/**
	 * Check if the affected objects involves non-workspace files.
	 * 
	 * @return true if the command involves non-workspace files, false otherwise 
	 */
	public boolean involvesReadOnlyNonWorkSpaceFiles()
	{
		if (null != affectedObjects)
			return involvesReadOnlyNonWorkSpaceFiles(affectedObjects);
		
		return false;
		
	}
	
	/**
	 * Check if the affected objects involves non-workspace files.
	 * If the operation does not involve files at all then we will
	 * return false.
	 * 
	 * @return true if the command involves non-workspace files, false otherwise
	 */
	private boolean involvesReadOnlyNonWorkSpaceFiles(Object obj)
	{
		IPath path = null;
		if (obj instanceof IFile) {
			path = ((IFile) obj).getFullPath();

		} else if ((obj instanceof EObject) || (obj instanceof Resource)) {
			Resource resource = null;
			if (obj instanceof EObject) {
				resource = ((EObject) obj).eResource();
			} else {
				resource = (Resource) obj;
			}
			
			// Take into account the case where there is no underlying
			// disk file behind the EObject.
			if (resource != null) {
				String szPath = ResourceUtil.getFilePath(resource);
				path = new Path(szPath);
			}
		}
		
		// If this operation does not involve modification to a resource then we permit it to execute.
		// If a developer has stored invalid data in the getAffectedObject() attribute we will allow the
		// command to proceed.  (A stack trace has already been dumped (see AbstractCommand.execute())
		if (path == null || path.isEmpty() || path.segmentCount() <= 0)
			return false;
		
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		
		if (null == workspaceRoot.getFileForLocation(path)) {
			// This does involve a non workspace file
			File file = path.toFile();
			// Check if it exists because if file is in memory, we allow the modification
			if ( (null != file) && (file.exists()) && (!file.canWrite()) ) {
				return true;
			}
		}
			
		return false;
	}
	
	
	/**
	 * Returns the file for specified object.
	 * 
	 * @param obj the object for which a file is to be retrieved
	 * @return collection of workspace files
	 */
	protected static Collection getWorkspaceFilesFor(Object obj) {
		IFile file = null;
		
		if (obj instanceof EObjectAdapter) {
			obj = ((EObjectAdapter) obj).getRealObject();
		}
		
		if (obj instanceof EObject) {
			file = EObjectUtil.getWorkspaceFile((EObject) obj);
		} else if (obj instanceof IFile) {
			file = (IFile) obj;
		} else if (obj instanceof Resource) {
			Resource resource = (Resource) obj;
			if (resource != null) {
				String szPath = ResourceUtil.getFilePath(resource);
				IPath path = new Path(szPath);

				IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace()
				.getRoot();

				file = workspaceRoot.getFileForLocation(path);
			}
		}
				
		return (file != null) ? Collections.singletonList(file) : Collections.EMPTY_LIST;
	}

	/**
	 * Handles the action abandoned exception by setting an error result.
	 * 
	 * @param e an exception that is to be handled 
	 */
	protected void handleActionAbandoned(MSLActionAbandonedException e) {
		// No need to consider a null undo interval here because
		// such actions are never validated, so the abandon action event should
		// never be fired.
		setResult(new CommandResult(new Status(IStatus.ERROR, getPluginId(),
			MSLCommandsStatusCodes.VALIDATION_FAILURE, ABANDONED_ACTION_ERROR,
			null)));
		
		Trace.trace(MSLCommandsPlugin.getDefault(), MSLCommandsDebugOptions.MODEL_OPERATIONS, "MSLActionAbandonedException"); //$NON-NLS-1$
	}

	/**
	 * Use this method to cleanup any cache in the command.
	 * 
	 * This method is invoked at the end of execute.
	 */
	protected void cleanup() {
		// overriding classes can use this to cleanup 
	}
}
