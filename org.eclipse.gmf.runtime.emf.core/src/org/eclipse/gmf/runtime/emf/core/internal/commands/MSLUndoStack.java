/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLRuntimeException;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.exceptions.MSLActionProtocolException;
import org.eclipse.gmf.runtime.emf.core.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLStatusCodes;
import org.eclipse.gmf.runtime.emf.core.internal.util.ConstraintStatusAdapter;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;
import org.eclipse.gmf.runtime.emf.core.internal.validation.ActionAbandonedNotification;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

/**
 * The MSL listens to EMF notifications and populates the command stack on the
 * fly. The following class manages the command stack.
 * 
 * @author rafikj
 */
public class MSLUndoStack {

	// the editing domain this stack is associtaed with.
	private MSLEditingDomain domain = null;

	// currently open undo interval.
	private MSLUndoInterval currentInterval = null;

	// currently open write action.
	private MSLCompoundCommand currentAction = null;

	// the list of undo intervals: the command stack.
	private ArrayList intervals = new ArrayList();

	// action nesting.
	private ArrayList actions = new ArrayList();
	
	// thread
	private ArrayList threads = new ArrayList();

	// unchecked action nesting level.
	private int uncheckedCount = 0;
	
	// is an unchecked in progress?
	private boolean uncheckedInProgress = false;

	// write action nesting level.
	private int writeCount = 0;

	// is a write in progress?
	private boolean writeInProgress = false;

	// is an undo in progress?
	private boolean undoInProgress = false;

	// is a redo in progress?
	private boolean redoInProgress = false;

	// is an abandon in progress?
	private boolean abandonInProgress = false;

	private ExecutionLock executionLock = new ExecutionLock();
	// types of actions.
	public static class ActionLockMode {

		private int nextOridnal = 0;

		private int ordinal = nextOridnal;

		private ActionLockMode() {
			super();
			nextOridnal++;
		}

		public boolean equals(ActionLockMode other) {
			return this.ordinal == other.ordinal;
		}

		public int hashCode() {
			return ordinal;
		}

		public final static ActionLockMode NONE = new ActionLockMode();

		public final static ActionLockMode READ = new ActionLockMode();

		public final static ActionLockMode WRITE = new ActionLockMode();

		public final static ActionLockMode UNCHECKED = new ActionLockMode();
	}

	/**
	 * Constructor.
	 */
	public MSLUndoStack(MSLEditingDomain domain) {
		super();
		this.domain = domain;
	}

	/**
	 * Opens an undo interval.
	 */
	public void openUndoInterval(String label, String description) {

		acquire(ActionLockMode.WRITE);
		
		// do some protocol validation.
		if (currentInterval != null) {

			RuntimeException e = new MSLActionProtocolException(
				"interval already open"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"openUndoInterval", e); //$NON-NLS-1$

			release();
			throw e;

		} else if (currentAction != null) {

			RuntimeException e = new MSLActionProtocolException(
				"write action already open"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"openUndoInterval", e); //$NON-NLS-1$

			release();
			throw e;

		} else if (isReadActionAtTop()) {
			RuntimeException e = new MSLActionProtocolException(
				"read action in progress"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"openUndoInterval", e); //$NON-NLS-1$
			release();
			throw e;
		} else {
			currentInterval = new MSLUndoInterval(domain, label, description);
		}
	}

	/**
	 * Closes currently open undo interval.
	 */
	public MSLUndoInterval closeUndoInterval() {

		try {
			MSLUndoInterval interval = null;

			// do some protocol validation.
			if (currentInterval == null) {

				RuntimeException e = new MSLActionProtocolException(
					"need interval to close"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"closeUndoInterval", e); //$NON-NLS-1$

				throw e;

			} else if (currentAction != null) {

				RuntimeException e = new MSLActionProtocolException(
					"write action still open"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"closeUndoInterval", e); //$NON-NLS-1$

				throw e;

			} else
				interval = currentInterval;

			currentInterval = null;

			if (!interval.isUndoable())
				flushInterval(interval);

			else {

				intervals.add(interval);

				if (!interval.isEmpty())
					domain.getEventBroker().fireEvent(interval, EventTypes.CREATE);

				flushAutomatically();
			}

			return interval;
		} finally {
			release();
		}
	}

	/**
	 * Undo the given interval.
	 */
	public void undoThroughInterval(final MSLUndoInterval interval) {

		acquire(ActionLockMode.WRITE);
		try {
			// do some protocol validation.
			if (currentInterval != null) {

				RuntimeException e = new MSLActionProtocolException(
					"interval already open"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"undoThroughInterval", e); //$NON-NLS-1$

				throw e;

			} else if (currentAction != null) {

				RuntimeException e = new MSLActionProtocolException(
					"write action still open"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"undoThroughInterval", e); //$NON-NLS-1$

				throw e;

			} else if ((undoInProgress) || (redoInProgress) || (abandonInProgress)) {

				RuntimeException e = new MSLActionProtocolException(
					"modify in progress"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"undoThroughInterval", e); //$NON-NLS-1$

				throw e;

			} else if (!found(interval, false)) {

				RuntimeException e = new MSLActionProtocolException(
					"interval not found"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"undoThroughInterval", e); //$NON-NLS-1$

				throw e;
			}

			try {

				// redo the interval in an unchecked action.
				final List intervalsToUndo = new ArrayList();

				boolean undone = false;

				while (!undone) {

					MSLUndoInterval last = (MSLUndoInterval) intervals.remove(intervals.size() - 1);

					intervalsToUndo.add(last);

					intervals.add(0,last);

					if (last == interval)
						undone = true;
				}

				domain.runAsUnchecked(new MRunnable() {

					public Object run() {

						undoInProgress = true;

						Iterator i = intervalsToUndo.iterator();

						while (i.hasNext())
							((MSLUndoInterval) i.next()).undoCommand();

						undoInProgress = false;

						return null;
					}
				});

			} catch (Exception e) {

				undoInProgress = false;

				domain.getEventBroker().clearEvents();

				RuntimeException newE = null;

				if (e instanceof MSLRuntimeException)
					newE = (MSLRuntimeException) e;
				else
					newE = new MSLRuntimeException(e);

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"undoThroughInterval", newE); //$NON-NLS-1$

				throw newE;
			}

			if (!interval.isRedoable())
				flushInterval(interval);

			domain.getEventBroker().fireEvents();
		} finally {
			release();
		}
	}

	/**
	 * Redo the given interval.
	 */
	public void redoThroughInterval(final MSLUndoInterval interval) {

		acquire(ActionLockMode.WRITE);
		
		try {
			// do some protocol validation.
			if (currentInterval != null) {
	
				RuntimeException e = new MSLActionProtocolException(
					"interval already open"); //$NON-NLS-1$
	
				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"redoThroughInterval", e); //$NON-NLS-1$
	
				throw e;
	
			} else if (currentAction != null) {
	
				RuntimeException e = new MSLActionProtocolException(
					"write action still open"); //$NON-NLS-1$
	
				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"redoThroughInterval", e); //$NON-NLS-1$
	
				throw e;
	
			} else if ((undoInProgress) || (redoInProgress) || (abandonInProgress)) {
	
				RuntimeException e = new MSLActionProtocolException(
					"modify in progress"); //$NON-NLS-1$
	
				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"redoThroughInterval", e); //$NON-NLS-1$
	
				throw e;
	
			} else if (!found(interval, false)) {
	
				RuntimeException e = new MSLActionProtocolException(
					"interval not found"); //$NON-NLS-1$
	
				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"redoThroughInterval", e); //$NON-NLS-1$
	
				throw e;
			}
	
			// undo the interval in an unchecked action.
	
			try {
	
				final List intervalsToRedo = new ArrayList();
	
				boolean done = false;
	
				while (!done) {
	
					MSLUndoInterval first = (MSLUndoInterval) intervals
						.remove(0);
	
					intervals.add(first);
	
					intervalsToRedo.add(first);
	
					if (first == interval)
						done = true;
				}
	
				domain.runAsUnchecked(new MRunnable() {
	
					public Object run() {
	
						redoInProgress = true;
	
						Iterator i = intervalsToRedo.iterator();
	
						while (i.hasNext())
							((MSLUndoInterval) i.next()).redoCommand();
	
						redoInProgress = false;
	
						return null;
					}
				});
	
			} catch (Exception e) {
	
				redoInProgress = false;
	
				domain.getEventBroker().clearEvents();
	
				RuntimeException newE = null;
	
				if (e instanceof MSLRuntimeException)
					newE = (MSLRuntimeException) e;
				else
					newE = new MSLRuntimeException(e);
	
				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"redoThroughInterval", newE); //$NON-NLS-1$
	
				throw newE;
			}
	
			if (!interval.isUndoable())
				flushInterval(interval);
	
			domain.getEventBroker().fireEvents();
		} finally {
			release();
		}
	}

	/**
	 * Is there an open undo interval?
	 */
	public boolean isUndoIntervalOpen() {
		acquire(ActionLockMode.READ);
		try {
			return (currentInterval != null);
		} finally {
			release();
		}
	}

	/**
	 * Can the current interval be undone?
	 * 
	 * @return True if current interval can be undone.
	 */
	public boolean canUndoCurrentInterval() {
		acquire(ActionLockMode.READ);

		try {
			// do some protocol validation.
			if (currentInterval == null) {

				RuntimeException e = new MSLActionProtocolException(
					"need interval to check"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"canUndoCurrentInterval", e); //$NON-NLS-1$

				throw e;
			}

			return currentInterval.isUndoable();
		} finally {
			release();
		}
	}

	/**
	 * Can the current interval be redone?
	 * 
	 * @return True if current interval can be redone.
	 */
	public boolean canRedoCurrentInterval() {
		acquire(ActionLockMode.READ);

		try {
			// do some protocol validation.
			if (currentInterval == null) {

				RuntimeException e = new MSLActionProtocolException(
					"need interval to check"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"canRedoCurrentInterval", e); //$NON-NLS-1$

				throw e;
			}

			return currentInterval.isRedoable();
		} finally {
			release();
		}
	}

	/**
	 * Sets can the current interval be undone.
	 */
	public void setCanUndoCurrentInterval(boolean canUndo) {

		acquire(ActionLockMode.UNCHECKED);
		try {
			// do some protocol validation.
			if (currentInterval == null) {
	
				RuntimeException e = new MSLActionProtocolException(
					"need interval to check"); //$NON-NLS-1$
	
				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"setCanUndoCurrentInterval", e); //$NON-NLS-1$
	
				throw e;
			}
	
			currentInterval.setUndoable(canUndo);
		} finally {
			release();
		}
	}

	/**
	 * Sets can the current interval be redone.
	 */
	public void setCanRedoCurrentInterval(boolean canRedo) {

		acquire(ActionLockMode.UNCHECKED);
		try {
			// do some protocol validation.
			if (currentInterval == null) {
	
				RuntimeException e = new MSLActionProtocolException(
					"need interval to check"); //$NON-NLS-1$
	
				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"setCanRedoCurrentInterval", e); //$NON-NLS-1$
	
				throw e;
			}
	
			currentInterval.setRedoable(canRedo);
		} finally {
			release();
		}
	}
	
	/**
	 * Yields for other read actions on other threads. Only the actions with
	 * read actions open (NO WRITE) can yield. This is a blocking call.
	 */
	public void yieldForReads() {
		executionLock.yieldForRead();
	}

	/**
	 * Start an action.
	 */
	public void startAction(ActionLockMode lockMode) {
		// Try to acquire the lock before even attempting to do something.
		acquire(lockMode);

		// do some protocol validation.
		if (lockMode == ActionLockMode.UNCHECKED) {

			uncheckedCount++;

			uncheckedInProgress = true;

			actions.add(0,lockMode);
			threads.add(0,Thread.currentThread());

		} else if ((undoInProgress) || (redoInProgress) || (abandonInProgress)) {

			RuntimeException e = new MSLActionProtocolException(
				"modify in progress"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"startAction", e); //$NON-NLS-1$
			
			// Release the lock before throwing the exception.
			release();

			throw e;

		} else if (lockMode == ActionLockMode.WRITE) {

			if (isReadActionAtTop()) {
				RuntimeException e = new MSLActionProtocolException(
					"read action in progress"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"startAction", e); //$NON-NLS-1$

				release();
				throw e;

			} else if (currentInterval == null) {

				RuntimeException e = new MSLActionProtocolException(
					"need open interval to start write action"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"startAction", e); //$NON-NLS-1$

				release();
				throw e;

			} else if (currentAction != null) {

				RuntimeException e = new MSLActionProtocolException(
					"write action already open"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"startAction", e); //$NON-NLS-1$

				release();
				throw e;
			}

			else {

				writeCount++;

				writeInProgress = true;

				currentAction = new MSLCompoundCommand(domain);

				actions.add(0,lockMode);
				threads.add(0,Thread.currentThread());
			}

		} else if (lockMode == ActionLockMode.READ) {
			
			actions.add(0,lockMode);
			threads.add(0,Thread.currentThread());
			
		} else {

			RuntimeException e = new MSLActionProtocolException(
				"write action already open"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"startAction", e); //$NON-NLS-1$

			release();
			throw e;
		}
	}

	/**
	 * Complete currently open action.
	 */
	public void completeAction() {

		boolean fireEvents = false;
		MCommand firedActionCommand = null;

		try {
			// do some protocol validation.
			if (actions.isEmpty()) {

				RuntimeException e = new MSLActionProtocolException(
					"no action is complete"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"completeAction", e); //$NON-NLS-1$

				throw e;

			} else {

				ActionLockMode lockMode = (ActionLockMode) actions.remove(0);
				Object currentThread = threads.remove(0);
				if(currentThread != Thread.currentThread()) {
					RuntimeException e = new MSLActionProtocolException(
					"Thread completing the action doesn't match the starting one"); //$NON-NLS-1$

					Trace.throwing(MSLPlugin.getDefault(),
						MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
						"completeAction", e); //$NON-NLS-1$

					throw e;
				}

				if (lockMode == ActionLockMode.UNCHECKED) {

					uncheckedCount--;

					if (uncheckedCount == 0)
						uncheckedInProgress = false;

					fireEvents = !(writeInProgress || uncheckedInProgress
						|| undoInProgress || redoInProgress || abandonInProgress);

				} else if ((undoInProgress) || (redoInProgress)
					|| (abandonInProgress)) {

					RuntimeException e = new MSLActionProtocolException(
						"modify in progress"); //$NON-NLS-1$

					Trace.throwing(MSLPlugin.getDefault(),
						MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
						"completeAction", e); //$NON-NLS-1$

					throw e;

				} else if (lockMode == ActionLockMode.WRITE) {

					if (currentAction == null) {

						RuntimeException e = new MSLActionProtocolException(
							"no action to complete"); //$NON-NLS-1$

						Trace.throwing(MSLPlugin.getDefault(),
							MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
							"completeAction", e); //$NON-NLS-1$

						throw e;

					} else {

						if (!currentAction.isEmpty())
							currentInterval.append(currentAction);

						firedActionCommand = currentAction;
						currentAction = null;

						writeCount--;

						if (writeCount == 0)
							writeInProgress = false;

						fireEvents = !(writeInProgress || uncheckedInProgress);
					}
				} else if (lockMode != ActionLockMode.READ) {
				RuntimeException e = new MSLActionProtocolException(
					"unknown action lock mode"); //$NON-NLS-1$
	
				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"completeAction", e); //$NON-NLS-1$
	
				throw e;
			
			}

			}

			// fire events after action is complete.
			if (fireEvents) {
				if(firedActionCommand != null)
					domain.getEventBroker().addEvent(firedActionCommand, EventTypes.CREATE);
				domain.getEventBroker().fireEvents();
			}
		} finally {
			release();
		}
	}

	/**
	 * Complete and validate currently open action.
	 */
	public IStatus completeAndValidateAction()
		throws MSLActionAbandonedException {

		IStatus status = null;

		try {

			// check constraints.
			status = checkAction();

		} catch (Exception e) {

			abandonAction();

			RuntimeException newE = null;

			if (e instanceof MSLRuntimeException)
				newE = (MSLRuntimeException) e;
			else
				newE = new MSLRuntimeException(e);

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"completeAndValidateAction", newE); //$NON-NLS-1$

			throw newE;
		}

		if (status == null)
			status = new Status(IStatus.ERROR, MSLPlugin.getPluginId(),
				MSLStatusCodes.VALIDATOR_PROTOCOL_ERROR, ResourceManager
					.getI18NString("validation.nullStatus"), null); //$NON-NLS-1$

		if (status.getSeverity() >= IStatus.ERROR) {

			abandonAction();

			MSLActionAbandonedException e = new MSLActionAbandonedException(
				status);

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"completeAndValidateAction", e); //$NON-NLS-1$

			throw e;

		}

		completeAction();

		return status;
	}

	/**
	 * Check if current action violates some constraints.
	 */
	public IStatus checkAction() {

		// do some protocol validation.
		if (actions.isEmpty()) {

			RuntimeException e = new MSLActionProtocolException(
				"no action is check"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"checkAction", e); //$NON-NLS-1$

			throw e;

		} else if ((undoInProgress) || (redoInProgress) || (abandonInProgress)) {

			RuntimeException e = new MSLActionProtocolException(
				"modify in progress"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"checkAction", e); //$NON-NLS-1$

			throw e;

		} else {

			ActionLockMode lockMode = (ActionLockMode) actions.get(0);

			if (lockMode == ActionLockMode.WRITE) {

				if (currentAction == null) {

					RuntimeException e = new MSLActionProtocolException(
						"no action is check"); //$NON-NLS-1$

					Trace.throwing(MSLPlugin.getDefault(),
						MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
						"checkAction", e); //$NON-NLS-1$

					throw e;

				} else {
					List notifications = domain.getEventBroker().getEventsToValidate();
					
					if (currentAction.isAbandon()) {
						List oldNotifications = notifications;
						
						notifications = new java.util.ArrayList();
						notifications.add(new ActionAbandonedNotification(
								currentAction.getAbandonCommand().getStatus()));
						notifications.addAll(oldNotifications);
					}
					
					// We will now call upon the EMF Validation service
					//  to validate this action and return the status of
					//  that validation.
					IValidator liveValidator = ModelValidationService
						.getInstance().newValidator(EvaluationMode.LIVE);
					
					IStatus validationStatus = ConstraintStatusAdapter
						.wrapStatus(liveValidator.validate(notifications));
					
					return validationStatus;
				}
			}
		}

		return Status.OK_STATUS;
	}

	/**
	 * Abandon currently open action without sending events.
	 */
	public void abandonAction() {

		try {
			MSLCompoundCommand action = null;

			boolean fireEvents = false;

			// do some protocol validation.
			if (actions.isEmpty()) {

				RuntimeException e = new MSLActionProtocolException(
					"no action to abandon"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"abandonAction", e); //$NON-NLS-1$

				throw e;
			}

			else if ((undoInProgress) || (redoInProgress) || (abandonInProgress)) {

				RuntimeException e = new MSLActionProtocolException(
					"modify in progress"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
					"abandonAction", e); //$NON-NLS-1$

				throw e;

			} else {

				ActionLockMode lockMode = (ActionLockMode) actions.remove(0);
				Object currentThread = threads.remove(0);
				if(currentThread != Thread.currentThread()) {
					RuntimeException e = new MSLActionProtocolException(
					"Thread abandoning the action doesn't match the starting one"); //$NON-NLS-1$

					Trace.throwing(MSLPlugin.getDefault(),
						MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
						"abandonAction", e); //$NON-NLS-1$

					throw e;
				}

				if (lockMode == ActionLockMode.UNCHECKED) {

					uncheckedCount--;

					if (uncheckedCount == 0)
						uncheckedInProgress = false;

					fireEvents = !(writeInProgress || uncheckedInProgress);

				} else

				if (lockMode == ActionLockMode.WRITE) {

					if (currentAction == null) {

						RuntimeException e = new MSLActionProtocolException(
							"no action to abandon"); //$NON-NLS-1$

						Trace.throwing(MSLPlugin.getDefault(),
							MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
							"abandonAction", e); //$NON-NLS-1$

						throw e;

					} else {

						action = currentAction;

						currentAction = null;

						writeCount--;

						if (writeCount == 0)
							writeInProgress = false;

						fireEvents = !(writeInProgress || uncheckedInProgress);
					}
				}
			}

			if ((action != null) && (!action.isEmpty())) {

				try {

					abandonInProgress = true;

					action.undo();

					abandonInProgress = false;

				} catch (Exception e) {

					abandonInProgress = false;

					domain.getEventBroker().clearEvents();

					RuntimeException newE = null;

					if (e instanceof MSLRuntimeException)
						newE = (MSLRuntimeException) e;
					else
						newE = new MSLRuntimeException(e);

					Trace.throwing(MSLPlugin.getDefault(),
						MSLDebugOptions.EXCEPTIONS_THROWING, getClass(),
						"abandonAction", newE); //$NON-NLS-1$

					throw newE;
				}
			}

			domain.getEventBroker().clearObjectEvents();

			if (fireEvents)
				domain.getEventBroker().fireEvents();
		} finally {
			// Release the lock in all scenarios
			release();
		}
	}

	/**
	 * Empty undo stack.
	 */
	public void flushAll() {

		acquire(ActionLockMode.WRITE);
		try {
			if (!intervals.isEmpty()) {

				MSLUndoInterval firstInterval = (MSLUndoInterval) intervals
					.get(0);
				MSLUndoInterval lastInterval = (MSLUndoInterval) intervals
					.get(intervals.size() - 1);

				for (ListIterator i = intervals.listIterator(); i.hasNext();) {
					((MSLUndoInterval) i.next()).dispose();
					i.remove();
				}

				if (firstInterval != null)
					domain.getEventBroker().fireEvent(firstInterval,
						EventTypes.DESTROY);

				if ((firstInterval != lastInterval) && (lastInterval != null))
					domain.getEventBroker().fireEvent(lastInterval,
						EventTypes.DESTROY);
			}
		} finally {
			release();
		}
	}

	/**
	 * Get the type of the currently open action.
	 */
	public ActionLockMode getActionLockMode() {

		acquire(ActionLockMode.READ);
		
		try {
			if (actions.isEmpty())
				return ActionLockMode.NONE;
			else
				return (ActionLockMode) actions.get(0);
		} finally {
			release();
		}
	}

	/**
	 * Get the type of the currently open action for the current thread.
	 */
	public ActionLockMode getCurrentThreadActionLockMode() {

		acquire(ActionLockMode.READ);
		
		try {
			if(!threads.isEmpty() && threads.get(0) == Thread.currentThread()) {
				if (actions.isEmpty())
					return ActionLockMode.NONE;
				else {
					return (ActionLockMode) actions.get(0);
				}
			} else {
				return ActionLockMode.NONE;
			}
		} finally {
			release();
		}
	}

	/**
	 * Is the top of the action stack a read action?
	 */
	private boolean isReadActionAtTop() {
		return getActionLockMode() == ActionLockMode.READ;
	}

	/**
	 * Is there a write action in progress?
	 */
	public boolean isWriteActionInProgress() {
		acquire(ActionLockMode.READ);
		try {
			return writeInProgress;
		} finally {
			release();
		}
	}

	/**
	 * Is there a write action in progress?
	 */
	public boolean isUncheckedActionInProgress() {
		acquire(ActionLockMode.READ);
		try {
			return uncheckedInProgress;
		} finally {
			release();
		}
	}

	/**
	 * Is there an undo in progress?
	 */
	public boolean isUndoInProgress() {
		acquire(ActionLockMode.READ);
		try {
			return undoInProgress;
		} finally {
			release();
		}
	}

	/**
	 * Is there a redo in progress?
	 */
	public boolean isRedoInProgress() {
		acquire(ActionLockMode.READ);
		try {
			return redoInProgress;
		} finally {
			release();
		}
	}

	/**
	 * Is there an abandon in progress?
	 */
	public boolean isAbandonInProgress() {
		acquire(ActionLockMode.READ);
		try {
			return abandonInProgress;
		} finally {
			release();
		}
	}

	/**
	 * Is therea modify in progress?
	 */
	public boolean isModifyInProgress() {
		acquire(ActionLockMode.READ);
		try {
			return (writeInProgress || uncheckedInProgress || undoInProgress
					|| redoInProgress || abandonInProgress);
		} finally {
			release();
		}
	}

	/**
	 * Add a command to the currently open write action.
	 */
	public void add(MCommand command) {

		acquire(ActionLockMode.UNCHECKED);
		
		try {
			if (uncheckedInProgress)
				return;

			if (writeInProgress) {

				currentAction.append(command);
				return;
			}

			Exception exception = null;

			try {

				abandonInProgress = true;

				command.undo();

				abandonInProgress = false;

			} catch (Exception e) {

				exception = e;

				abandonInProgress = false;

				domain.getEventBroker().clearEvents();
			}

			RuntimeException newE = null;

			if (exception == null)
				newE = new MSLActionProtocolException("need write action to modify"); //$NON-NLS-1$
			else
				newE = new MSLActionProtocolException(exception);

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "add", newE); //$NON-NLS-1$

			throw newE;
		} finally {
			release();
		}
	}

	/**
	 * Check for stack size limits and flush undo stack if necessary.
	 */
	private void flushAutomatically() {

		int count = intervals.size();

		if ((MSLConstants.MAX_STACK_DEPTH != -1)
			&& (count > MSLConstants.MAX_STACK_DEPTH)) {

			MSLUndoInterval interval = null;

			for (int i = 0; (i < MSLConstants.INTERVAL_FLUSH_COUNT)
				&& (i < count); i++) {
				interval = (MSLUndoInterval) intervals.remove(0);
				interval.dispose();
			}

			if (interval != null)
				domain.getEventBroker().fireEvent(interval, EventTypes.DESTROY);
		}
	}

	/**
	 * Remove undo interval and all intervals occuring before it.
	 */
	private void flushInterval(MSLUndoInterval interval) {

		acquire(ActionLockMode.UNCHECKED);
		try {
			if (found(interval, false)) {
	
				if (interval.canUndo()) {
	
					for (ListIterator i = intervals.listIterator(); i.hasNext();) {
	
						MSLUndoInterval next = (MSLUndoInterval) i.next();
	
						if (next.canUndo()) {
							i.remove();
							next.dispose();
						}
	
						if (next == interval)
							break;
					}
	
				} else if (interval.canRedo()) {
	
					for (ListIterator i = intervals.listIterator(intervals.size()); i
						.hasPrevious();) {
	
						MSLUndoInterval previous = (MSLUndoInterval) i.previous();
	
						if (previous.canRedo()) {
							i.remove();
							previous.dispose();
						}
	
						if (previous == interval)
							break;
					}
				}
	
				domain.getEventBroker().fireEvent(interval, EventTypes.DESTROY);
			}
		} finally {
			release();
		}
	}

	/**
	 * Finds an undo interval in the undo stack.
	 */
	private boolean found(MSLUndoInterval interval, boolean forward) {

		if (interval == null)
			return false;

		int count = intervals.size();

		if (intervals.isEmpty())
			return false;

		if (forward) {

			ListIterator i = intervals.listIterator();

			while (i.hasNext())
				if (i.next() == interval)
					return true;

		} else {

			ListIterator i = intervals.listIterator(count);

			while (i.hasPrevious())
				if (i.previous() == interval)
					return true;
		}

		return false;
	}

	/**
	 * An implementation of the MUndoInterval interface.
	 */
	private class MSLUndoInterval
		extends MSLCompoundCommand
		implements MUndoInterval {

		private String label = null;

		private String description = null;

		private boolean undone = false;

		private boolean undoable = true;

		private boolean redoable = true;

		public MSLUndoInterval(MSLEditingDomain domain, String label,
				String description) {

			super(domain);

			this.label = label;
			this.description = description;
		}

		public String getLabel() {
			return (label == null) ? MSLConstants.EMPTY_STRING
				: label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getDescription() {
			return (description == null) ? MSLConstants.EMPTY_STRING
				: description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean canUndo() {
			return !undone;
		}

		public boolean canRedo() {
			return undone;
		}

		public void undo() {

			if (!undone) {

				undone = true;

				undoThroughInterval(this);

			} else {

				RuntimeException e = new MSLActionProtocolException(
					"interval already undone"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "undo", e); //$NON-NLS-1$

				throw e;
			}
		}

		public void redo() {

			if (undone) {

				undone = false;

				redoThroughInterval(this);

			} else {

				RuntimeException e = new MSLActionProtocolException(
					"interval already redone"); //$NON-NLS-1$

				Trace.throwing(MSLPlugin.getDefault(),
					MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "redo", e); //$NON-NLS-1$

				throw e;
			}
		}

		public void undoCommand() {
			super.undo();
		}

		public void redoCommand() {
			super.redo();
		}

		public void flush() {
			flushInterval(this);
		}

		public MCommand unwrap() {
			return this;
		}

		public boolean isUndoable() {
			return undoable;
		}

		public boolean isRedoable() {
			return redoable;
		}

		public void setUndoable(boolean undoable) {
			this.undoable = undoable;
		}

		public void setRedoable(boolean redoable) {
			this.redoable = redoable;
		}
	}
	
	/**
	 * Acquires the execution lock
	 */
	public void acquire(ActionLockMode lockMode) {
		executionLock.acquire(lockMode);
	}
	
	public void release() {
		executionLock.release();
	}
}