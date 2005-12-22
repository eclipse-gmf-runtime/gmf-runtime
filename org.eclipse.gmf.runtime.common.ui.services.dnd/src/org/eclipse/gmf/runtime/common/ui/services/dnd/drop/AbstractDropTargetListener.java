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

package org.eclipse.gmf.runtime.common.ui.services.dnd.drop;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartSite;

import org.eclipse.gmf.runtime.common.core.command.CommandManager;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDPlugin;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDStatusCodes;

/**
 * Abstract parent of all the drop target listeners
 * 
 * @author Vishy Ramaswamy
 */
public abstract class AbstractDropTargetListener
	implements IDropTargetListener {

	/**
	 * Attribute for the drop target context.
	 */
	private IDropTargetContext context = null;

	/**
	 * Attribute for the current transfer agent.
	 */
	private ITransferAgent currentAgent = null;

	/**
	 * Attribute for the current event.
	 */
	private IDropTargetEvent currentEvent = null;

	/**
	 * Attribute for the supporting transfer ids.
	 */
	private final List transferIds = new Vector();

	/**
	 * Enumerated type for work indicator type
	 */
	public static class WorkIndicatorType
		extends EnumeratedType {

		private static final long serialVersionUID = 1L;

		private static int nextOrdinal = 0;

		/** None work indicator type. */
		public static final WorkIndicatorType NONE = new WorkIndicatorType(
			"None"); //$NON-NLS-1$

		/** Busy work indicator type. */
		public static final WorkIndicatorType BUSY = new WorkIndicatorType(
			"Busy"); //$NON-NLS-1$

		/** Progress monitor indicator type. */
		public static final WorkIndicatorType PROGRESS_MONITOR = new WorkIndicatorType(
			"Progress Monitor"); //$NON-NLS-1$

		/** Cancelable progress monitor indicator type. */
		public static final WorkIndicatorType CANCELABLE_PROGRESS_MONITOR = new WorkIndicatorType(
			"Cancelable Progress Monitor"); //$NON-NLS-1$

		/**
		 * The list of values for this enumerated type.
		 */
		private static final WorkIndicatorType[] VALUES = {NONE, BUSY,
			PROGRESS_MONITOR, CANCELABLE_PROGRESS_MONITOR};

		/**
		 * Constructor for WorkIndicatorType.
		 * 
		 * @param name
		 *            The name for the WorkIndicatorType
		 * @param ordinal
		 *            The ordinal for theWorkIndicatorType
		 */
		protected WorkIndicatorType(String name, int ordinal) {
			super(name, ordinal);
		}

		/**
		 * Constructor for WorkIndicatorType.
		 * 
		 * @param name
		 *            The name for the WorkIndicatorType
		 */
		private WorkIndicatorType(String name) {
			this(name, nextOrdinal++);
		}

		/**
		 * Retrieves the list of constants for this enumerated type.
		 * 
		 * @return The list of constants for this enumerated type.
		 */
		protected List getValues() {
			return Collections.unmodifiableList(Arrays.asList(VALUES));
		}
	}

	/**
	 * Constructor for AbstractDropTargetListener.
	 * 
	 * @param transferIdArray
	 *            The transfer agent ids
	 */
	public AbstractDropTargetListener(String[] transferIdArray) {
		super();

		assert null!=transferIdArray : "transferIdArray cannot be null"; //$NON-NLS-1$
		assert transferIdArray.length > 0 : "transferIdArray cannot be empty"; //$NON-NLS-1$

		this.transferIds.addAll(Arrays.asList(transferIdArray));
	}

	/**
	 * Default Constructor for AbstractDropTargetListener.
	 *  
	 */
	public AbstractDropTargetListener() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetListener#getSupportingTransferIds()
	 */
	public final String[] getSupportingTransferIds() {
		return (String[]) transferIds.toArray(new String[transferIds.size()]);
	}

	/**
	 * Add transfer id to the list of transferIds.
	 * 
	 * @param transferId
	 *            String id to add
	 */
	public final void addSupportingTransferId(String transferId) {
		assert null != transferId : "transferId cannot be null"; //$NON-NLS-1$
		
		if (!transferIds.contains(transferId)) {
			transferIds.add(transferId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
		/* method not implemented */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragLeave(DropTargetEvent event) {
		/* method not implemented */
		currentAgent = null;
		currentEvent = null;
		context = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {
		/* method not implemented */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event) {
		/* method not implemented */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public final void drop(DropTargetEvent event) {
		/* Check the target and data */
		// Fix for RATLC00528158 - Linux: Cannot DnD Tables or Database from
		// Data Definition View to Class Diagram
		// Removed check "event.data == null"
		if (getContext().getCurrentTarget() == null) {
			event.detail = DND.DROP_NONE;
			return;
		}

		/* Get the command */
		final ICommand command = getExecutableContext(event);

		/* Get the command manager */
		final CommandManager manager = (CommandManager) getContext()
			.getActivePart().getAdapter(CommandManager.class);

		/* Check the manager and command */
		if (manager == null || command == null) {
			event.detail = DND.DROP_NONE;
			return;
		}

		WorkIndicatorType type = getWorkIndicatorType();

		if (type == WorkIndicatorType.PROGRESS_MONITOR) {
			runCommandInProgressMonitorDialog(command, false);

		} else if (type == WorkIndicatorType.CANCELABLE_PROGRESS_MONITOR) {
			runCommandInProgressMonitorDialog(command, true);

		} else if (type == WorkIndicatorType.BUSY) {
			/* display hour glass cursor */
			BusyIndicator.showWhile(null, new Runnable() {

				public void run() {
					manager.execute(command, new NullProgressMonitor());

				}
			});
		} else {
			manager.execute(command, new NullProgressMonitor());
		}

		/* Set the event detail */
		event.detail = (command.getCommandResult().getStatus().isOK()) ? event.detail
			: DND.DROP_NONE;

		currentAgent = null;
		currentEvent = null;
		context = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
		/* method not implemented */
	}

	/**
	 * Returns the context.
	 * 
	 * @return IDropTargetContext
	 */
	protected final IDropTargetContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetListener#getExecutableContext(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public ICommand getExecutableContext(DropTargetEvent event) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetListener#setFeedback(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void setFeedback(DropTargetEvent event) {
		event.feedback |= DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
		switch (getContext().getRelativeLocation()) {
			case IDropTargetContext.LOCATION_BEFORE:
				event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
				break;
			case IDropTargetContext.LOCATION_AFTER:
				event.feedback |= DND.FEEDBACK_INSERT_AFTER;
				break;
			case IDropTargetContext.LOCATION_ON:
			default:
				event.feedback |= DND.FEEDBACK_SELECT;
				break;
		}
	}

	/**
	 * Returns whether the listener can support handling drop operations on the
	 * current target context and the current event.
	 * 
	 * @return true or false
	 */
	public abstract boolean canSupport();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetListener#canSupport(org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetContext,
	 *      org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetEvent,
	 *      org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent)
	 */
	public final boolean canSupport(IDropTargetContext cntxt,
			IDropTargetEvent currEvent, ITransferAgent currAgent) {
		/* Set the context */
		this.context = cntxt;
		/* Set the event */
		this.currentEvent = currEvent;
		/* Set the agent */
		this.currentAgent = currAgent;

		return canSupport();
	}

	/**
	 * Returns the current event.
	 * 
	 * @return IDropTargetEvent
	 */
	protected final IDropTargetEvent getCurrentEvent() {
		return currentEvent;
	}

	/**
	 * Returns the current transfer agent.
	 * 
	 * @return ITransferAgent
	 */
	protected final ITransferAgent getCurrentAgent() {
		return currentAgent;
	}

	/**
	 * Returns the current shell.
	 * 
	 * @return Shell
	 */
	protected final Shell getShell() {
		IWorkbenchPartSite site = getContext().getActivePart().getSite();

		return site != null ? site.getShell()
			: null;
	}

	/**
	 * Gets type of work indicator (progress monitor, hourglass, or none).
	 * 
	 * @return type of work indicator
	 */
	protected WorkIndicatorType getWorkIndicatorType() {
		return WorkIndicatorType.BUSY;
	}

	/**
	 * Runs <code>command</code> in the context of a progress monitor dialog.
	 * The command runs in the same thread as the dialog. The cancel button on
	 * the dialog is enabled if <code>cancelable</code> is <code>true</code>.
	 * 
	 * @param command
	 *            the command to run
	 * @param cancelable
	 *            <code>true</code> if the progress monitor should have an
	 *            enabled cancel button, <code>false</code> otherwise.
	 * 
	 * @exception RuntimeException
	 *                if any exception or error occurs while running the action
	 */
	private void runCommandInProgressMonitorDialog(final ICommand command,
			boolean cancelable) {

		/* Get the command manager */
		final CommandManager manager = (CommandManager) getContext()
			.getActivePart().getAdapter(CommandManager.class);

		IRunnableWithProgress runnable = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) {
				manager.execute(command, monitor);

			}
		};
		runInProgressMonitorDialog(runnable, cancelable);
	}

	/**
	 * Runs <code>runnable</code> in a progress monitor dialog. The runnable
	 * runs in the same thread as the dialog. The cancel button on the dialog is
	 * enabled if <code>cancelable</code> is <code>true</code>.
	 * 
	 * @param runnable
	 *            the runnable to run in the context of the progress dialog
	 * @param cancelable
	 *            <code>true</code> if the progress monitor should have an
	 *            enabled cancel button, <code>false</code> otherwise.
	 * 
	 * @exception RuntimeException
	 *                if any exception or error occurs while running the
	 *                runnable
	 */
	private void runInProgressMonitorDialog(IRunnableWithProgress runnable,
			boolean cancelable) {

		try {
			if (System.getProperty("RUN_PROGRESS_IN_UI_HACK") != null) { //$NON-NLS-1$
				new ProgressMonitorDialog(null).run(false, cancelable, runnable);
			} else {
				new ProgressMonitorDialog(null).run(true, cancelable, runnable);
			}

		} catch (InvocationTargetException ite) {
			Trace.catching(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.EXCEPTIONS_CATCHING,
				getClass(), "runInProgressMonitorDialog", ite); //$NON-NLS-1$
			Log.error(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDStatusCodes.SERVICE_FAILURE,
				"runInProgressMonitorDialog", ite); //$NON-NLS-1$

			RuntimeException cre = new RuntimeException(ite
				.getTargetException());

			Trace.throwing(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.EXCEPTIONS_THROWING,
				getClass(), "runInProgressMonitorDialog", cre); //$NON-NLS-1$
			throw cre;

		} catch (InterruptedException ie) {
			Trace.catching(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.EXCEPTIONS_CATCHING,
				getClass(), "runInProgressMonitorDialog", ie); //$NON-NLS-1$
		}
	}

}