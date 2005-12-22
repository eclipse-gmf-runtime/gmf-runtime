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

package org.eclipse.gmf.runtime.common.ui.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandManager;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.custom.BusyIndicator;

/**
 * Responsible for managing the running of actions. All
 * actions (delegates and handlers) channel their run requests through an action
 * manager. An action manager keeps track of the action that was last run and
 * fires events to interested listeners whenever an action is run.
 * 
 * @author khussey
 */
public class ActionManager {

	/**
	 * The empty string.
	 */
	protected static final String EMPTY_STRING = ""; //$NON-NLS-1$
	/**
	 * A string containing only a space character.
	 */
	protected static final String SPACE = " "; //$NON-NLS-1$

	/**
	 * The default action manager.
	 */
	private static ActionManager actionManager = null;

	/**
	 * The command manager with which this action manager is associated.
	 */
	private final CommandManager commandManager;

	/**
	 * The last action that was run.
	 */
	private IActionWithProgress action = null;

	/**
	 * The action manager change listeners.
	 */
	private final List listeners =
		Collections.synchronizedList(new ArrayList());

	/**
	 * Constructs a new action manager for the specified command manager.
	 * 
	 * @param commandManager The command manager for this action manager.
	 */
	public ActionManager(CommandManager commandManager) {
		super();

		assert null != commandManager;

		this.commandManager = commandManager;
	}

	/**
	 * Retrieves the default action manager.
	 * 
	 * @return The default action manager.
	 */
	public static ActionManager getDefault() {
		if (null == actionManager) {
			actionManager = new ActionManager(CommandManager.getDefault());
		}

		return actionManager;
	}

	/**
	 * Retrieves the value of the <code>commandManager</code> instance variable.
	 * 
	 * @return The value of the <code>commandManager</code> instance variable.
	 */
	public final CommandManager getCommandManager() {
		return commandManager;
	}

	/**
	 * Retrieves the value of the <code>action</code> instance variable.
	 * 
	 * @return The value of the <code>action</code> instance variable.
	 */
	protected final IActionWithProgress getAction() {
		return action;
	}

	/**
	 * Sets the <code>action</code> instance variable to the specified value.
	 * 
	 * @param action The new value for the <code>action</code> instance
	 *                variable.
	 */
	protected final void setAction(IActionWithProgress action) {
		this.action = action;
	}

	/**
	 * Retrieves the value of the <code>listeners</code> instance variable.
	 * 
	 * @return The value of the <code>listeners</code> instance varible.
	 */
	protected final List getListeners() {
		return listeners;
	}

	/**
	 * Adds the specified listener to the list of action manager change
	 * listeners for this action manager.
	 * 
	 * @param listener The listener to be added.
	 */
	public void addActionManagerChangeListener(IActionManagerChangeListener listener) {
		assert null != listener;

		getListeners().add(listener);
	}

	/**
	 * Removes the specified listener from the list of action manager change
	 * listeners for this action manager.
	 * 
	 * @param listener The listener to be removed.
	 */
	public void removeActionManagerChangeListener(IActionManagerChangeListener listener) {
		assert null != listener;

		getListeners().remove(listener);
	}

	/**
	 * Notifies the listeners for this action manager that the specified
	 * event has occurred.
	 * 
	 * @param event The action manager change event to be fired.
	 */
	protected void fireActionManagerChange(ActionManagerChangeEvent event) {
		assert null != event;

		List targets = null;
		synchronized (getListeners()) {
			targets = new ArrayList(getListeners());
		}

		for (Iterator i = targets.iterator(); i.hasNext();) {
			((IActionManagerChangeListener) i.next()).actionManagerChanged(
				event);
		}
	}

	/**
	 * Clears this action manager by discarding the last action that was run.
	 */
	public void clear() {
		setAction(null);

		fireActionManagerChange(new ActionManagerChangeEvent(this));
	}

	/**
	 * Runs the specified action.
	 * 
	 * @param theAction The action to be run.
	 * @exception UnsupportedOperationException If the action cannot be run.
	 * @exception RuntimeException if any exception or error occurs 
	 * 									   while running the action
	 */
	public void run(final IActionWithProgress theAction) {
		if (!theAction.isRunnable()) {
			UnsupportedOperationException uoe =
				new UnsupportedOperationException();
			Trace.throwing(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_THROWING, getClass(), "run", uoe); //$NON-NLS-1$
			throw uoe;
		}
		
		boolean setup = theAction.setup();
		if (!setup) {
			// The setup did not occur (e.g. the user cancelled
			// a dialog presented in the setup). Do not proceed.
			return;
		}

		IActionWithProgress.WorkIndicatorType type =
			theAction.getWorkIndicatorType();

		if (type == IActionWithProgress.WorkIndicatorType.PROGRESS_MONITOR) {
			runActionInProgressMonitorDialog(theAction, false);

		} else if (
			type
				== IActionWithProgress
					.WorkIndicatorType
					.CANCELABLE_PROGRESS_MONITOR) {
			runActionInProgressMonitorDialog(theAction, true);

		} else if (type == IActionWithProgress.WorkIndicatorType.BUSY) {
			// display hourglass cursor
			BusyIndicator.showWhile(null, new Runnable() {
				public void run() {
					theAction.run(new NullProgressMonitor());
				}
			});
		} else {
			theAction.run(new NullProgressMonitor());
		}

		setAction(theAction);

		fireActionManagerChange(new ActionManagerChangeEvent(this, theAction));
		Trace.trace(CommonUIPlugin.getDefault(), CommonUIDebugOptions.ACTIONS_RUN, "Action '" + String.valueOf(getAction()) + "' run."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Runs <code>runnable</code> in a progress monitor dialog. The runnable runs in
	 * the same thread as the dialog. The cancel button on the dialog is enabled
	 * if <code>cancelable</code> is <code>true</code>. 
	 * 
	 * @param runnable the runnable to run in the context of the progress dialog
	 * @param cancelable <code>true</code> if the progress monitor should have
	 * 					  an enabled cancel button, <code>false</code> otherwise.
	 * 
	 * @exception RuntimeException if any exception or error occurs 
	 * 									   while running the runnable
	 */
	private void runInProgressMonitorDialog(
		IRunnableWithProgress runnable,
		boolean cancelable) {

		try {
			if (System.getProperty("RUN_PROGRESS_IN_UI_HACK") != null) { //$NON-NLS-1$
				new ProgressMonitorDialog(null).run(false, cancelable, runnable);
			} else {
				new ProgressMonitorDialog(null).run(true, cancelable, runnable);
			}

		} catch (InvocationTargetException ite) {
			Trace.catching(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_CATCHING, getClass(), "run", ite); //$NON-NLS-1$
			Log.error(CommonUIPlugin.getDefault(), CommonUIStatusCodes.SERVICE_FAILURE, "run", ite); //$NON-NLS-1$

			RuntimeException cre =
				new RuntimeException(ite.getTargetException());

			Trace.throwing(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_THROWING, getClass(), "run", cre); //$NON-NLS-1$
			throw cre;

		} catch (InterruptedException ie) {
			Trace.catching(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_CATCHING, getClass(), "run", ie); //$NON-NLS-1$
		}
	}

	/**
	 * Runs <code>action</code> in the context of a progress monitor dialog.
	 * The action runs in the same thread as the dialog. The cancel button on
	 * the dialog is enabled if <code>cancelable</code> is <code>true</code>. 
	 * 
	 * @param act the action to repeat
	 * @param cancelable <code>true</code> if the progress monitor should have
	 * 					  an enabled cancel button, <code>false</code> otherwise.
	 * 
	 * @exception RuntimeException if any exception or error occurs 
	 * 									   while running the action
	 */
	private void runActionInProgressMonitorDialog(
		final IActionWithProgress act,
		boolean cancelable) {

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				act.run(monitor);
			}
		};
		runInProgressMonitorDialog(runnable, cancelable);
	}
}
