/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.ILock;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.gmf.runtime.emf.core.internal.commands.MSLUndoStack.ActionLockMode;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;

/**
 * @author mgoyal
 * 
 */
/*
 * Issues with adoption of multi threading. a. Load and Unload resource should
* be write protected, else models can be closed during read causing unexpected
* behavior b. Yield when Yielding isn't supported. c. ModelSystemJob isn't
* cancelable but shows up with a cancel button in the blocked job dialog.
*/
class ExecutionLock {
	private static final IJobManager jobManager = Platform.getJobManager();
	// Execution Lock, to be acquired by all operations.
	private ILock executionLock = jobManager.newLock();

	// Write Lock, to be acquired by all write operations and yielding threads.
	private ILock writeLock = jobManager.newLock();

	private static final long ACQUIRE_TIMEOUT = 500;
	private static final long YIELD_TIMEOUT = 1000;



	// Data structure synchronizer for
	// readerThreads
	// yielding flag
	// executionOwner
	// writeOwner
	Object lock = new Object();

	// Waiting reader operations.
	private Set readerThreads = new HashSet();

	// ModelSystemJob for display threads not working like jobs
	private Map threadJobs = new HashMap();

	// flag to indicate that yielding is in progress
	private boolean yielding = false;

	// Execution Owner.
	Thread executionOwner = null;

	// write Owner.
	Thread writeOwner = null;

	// Map of Thread to Stack of rules
	Map threadRule = new HashMap(20);

	/**
	 * @author mgoyal
	 * 
	 * System job to capture the blocked state of the Thread thread.
	 */
	class ModelSystemJob
		extends Job {

		private final Object ruleNotifier = new Object();

		ModelJobRule rule = new ModelJobRule();

		ModelJobRule displayRule = new ModelJobRule();

		private ModelSystemJob(Thread thread) {
			super("Model Job"); //$NON-NLS-1$
			// The Job is a system job.
			setSystem(true);
			setRule(rule);
		}

		/**
		 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		protected IStatus run(IProgressMonitor monitor) {
			// Notifies that the job is running.
			synchronized (ruleNotifier) {
				ruleNotifier.notify();
			}
			//Tries to acquire the execution lock
			acquireExecutionLock();

			boolean isYielding = false;
			synchronized (lock) {
				isYielding = yielding;
			}

			// Releases the execution lock.
			releaseExecutionLock();
			// If Yielding then wait for YIELD_TIMEOUT
			if (isYielding) {
				synchronized (this) {
					try {
						wait(YIELD_TIMEOUT);
					} catch (InterruptedException e) {
						// do nothing, let it finish.
					}
				}
			}

			//			if(monitor.isCanceled())
			//				return new Status(IStatus.CANCEL,
			// MSLPlugin.getDefault().getSymbolicName(), IStatus.OK, "", null);
			// //$NON-NLS-1$
			//			else
			return new Status(IStatus.OK, MSLPlugin.getPluginId(),
				IStatus.OK, "", null); //$NON-NLS-1$
		}

		/**
		 * The rule notifier for this job, 
		 * This can be used to wait on this job.
		 * 
		 * @return Object
		 */
		public Object getRuleNotifier() {
			return ruleNotifier;
		}

		/**
		 * The rule on which other jobs can wait, 
		 * so that they can schedule based on this job.
		 * @return ISchedulingRule rule to wait on.
		 */
		public ISchedulingRule getDisplayRule() {
			return displayRule;
		}
	}

	/**
	 * Pushes a rule on the stack for the current thread
	 * 
	 * @param rule
	 *            Rule to push for this thread
	 */
	private synchronized void pushThreadRule(ISchedulingRule rule) {
		Thread currentThread = Thread.currentThread();
		Stack rules = (Stack) threadRule.get(currentThread);
		if (rules == null) {
			rules = new Stack();
			threadRule.put(currentThread, rules);
		}
		rules.push(rule);
	}

	/**
	 * Pops a thread rule from the stack for the current thread
	 * 
	 * @return Popped rule for this thread
	 */
	private synchronized ISchedulingRule popThreadRule() {
		Thread currentThread = Thread.currentThread();
		Stack rules = (Stack) threadRule.get(currentThread);
		if (rules != null) {
			ISchedulingRule rule = (ISchedulingRule) rules.pop();
			if(rules.isEmpty()) {
				threadRule.remove(currentThread);
			}
			return rule;
		}
		return null;
	}

	/**
	 * Pops a thread rule from the stack for the current thread
	 * 
	 * @return ISchedulingRule next rule on the stack for this thread
	 */
	private synchronized ISchedulingRule peekThreadRule() {
		Thread currentThread = Thread.currentThread();
		Stack rules = (Stack) threadRule.get(currentThread);
		if (rules != null) {
			return (ISchedulingRule) rules.peek();
		}
		return null;

	}

	/**
	 * Rule depth for the current thread. This can't be used as indicator for
	 * nested actions
	 * 
	 * @return int number of the rules on the stack.
	 */
	private synchronized int ruleDepth() {
		Thread currentThread = Thread.currentThread();
		Stack rules = (Stack) threadRule.get(currentThread);
		if (rules != null) {
			return rules.size();
		}
		return -1;
	}

	/**
	 * Gets the thread job for the current thread.
	 * 
	 * @return
	 */
	private synchronized ModelSystemJob createModelThreadJob() {
		Thread currentThread = Thread.currentThread();
		ModelSystemJob job = (ModelSystemJob) threadJobs.get(currentThread);
		if (job == null) {
			job = new ModelSystemJob(currentThread);
			threadJobs.put(currentThread, job);
		}
		return job;
	}
	
	/**
	 * Releases the Job for the Current Thread.
	 * 
	 * @return Released Job
	 */
	private synchronized ModelSystemJob releaseModelThreadJob() {
		Thread currentThread = Thread.currentThread();
		ModelSystemJob job = (ModelSystemJob) threadJobs.remove(currentThread);
		return job;
	}

	/**
	 * Acquires the Execution Lock normally. Can't be interrupted
	 * 
	 * @return
	 */
	private void acquireExecutionLock() {
		boolean acquired = false;
		while (!acquired) {
			try {
				acquired = executionLock.acquire(ACQUIRE_TIMEOUT);
			} catch (InterruptedException e) {
				acquired = false;
			}
		}

		assert acquired;
		pushThreadRule((ISchedulingRule) executionLock);
	}

	/**
	 * Acquires the execution lock normally except for Display thread.
	 * 
	 * @return
	 */
	private void acquireExecutionLockAsJob() {
		// Only try the special acquiring procedure for Display thread and when
		// no begin rule is done on the display thread.
		boolean acquired = false;
		if (jobManager.currentJob() == null) {
			// Try acquiring it quickly, if successful return
			try {
				acquired = executionLock.acquire(1);
			} catch (InterruptedException e) {
				acquired = false;
			}

			// loop until the lock is acquired.
			ModelSystemJob systemJob = createModelThreadJob();
			boolean scheduleJob = true;
			while (!acquired) {
				if (scheduleJob) {
					Object ruleNotifier = systemJob.getRuleNotifier();
					synchronized (ruleNotifier) {
						// wait for the system job to send notification after
						// running.
						systemJob.schedule();
						try {
							ruleNotifier.wait();
						} catch (InterruptedException e) {
							continue;
						}
					}
				}
				// Wait on the Display rule of the system job. This ensures that
				// the display thread
				// blocks with a Blocking dialog, until the system job finishes.
				// This will throw
				// OperationCanceledException if the system job is canceled.
				try {
					jobManager.beginRule(
						systemJob.getDisplayRule(), null);
					jobManager
						.endRule(systemJob.getDisplayRule());
					scheduleJob = true;
					if (systemJob.getResult().getSeverity() == IStatus.OK) {
						// if the system job finishes gracefully, try to acquire
						// the lock quickly.
						try {
							acquired = executionLock.acquire(10);
						} catch (InterruptedException e) {
							acquired = false;
						}
						//				} else {
						//					// if the system job is canceled, throw
						// OperationCanceledException.
						//					throw new OperationCanceledException();
					}
				} catch (OperationCanceledException e) {
					jobManager
						.endRule(systemJob.getDisplayRule());
					acquired = false;
					scheduleJob = false;
				}
			}
			releaseModelThreadJob();
		} else {
			while (!acquired) {
				try {
					acquired = executionLock.acquire(ACQUIRE_TIMEOUT);
				} catch (InterruptedException e) {
					acquired = false;
				}
			}
		}

		assert acquired;

		pushThreadRule((ISchedulingRule) executionLock);
	}

	/**
	 * Releases the execution lock
	 */
	private void releaseExecutionLock() {
		ISchedulingRule lastRule = popThreadRule();
		if (lastRule == executionLock)
			executionLock.release();
	}

	/**
	 * Acquires the write lock, Can't be interrupted
	 * 
	 * @return
	 */
	private void acquireWriteLock() {
		boolean acquired = false;
		while (!acquired) {
			try {
				acquired = writeLock.acquire(ACQUIRE_TIMEOUT);
			} catch (InterruptedException e) {
				acquired = false;
			}
		}

		assert acquired;
		pushThreadRule((ISchedulingRule) writeLock);
	}

	/**
	 * Releases the write lock
	 */
	private void releaseWriteLock() {
		ISchedulingRule lastRule = popThreadRule();
		if (lastRule == writeLock)
			writeLock.release();
	}

	/**
	 * Acquires the appropriate locks for a given ActionLockMode
	 * 
	 * @param lockMode
	 *            <code>ActionLockMode</code> for which the locks are sought.
	 */
	public void acquire(ActionLockMode lockMode) {
		if (lockMode == ActionLockMode.READ
			|| lockMode == ActionLockMode.UNCHECKED) {
			// Acquire the execution lock in case of read and unchecked.
			synchronized (lock) {
				readerThreads.add(Thread.currentThread());
			}
			acquireExecutionLock();
			synchronized (lock) {
				readerThreads.remove(Thread.currentThread());
				executionOwner = Thread.currentThread();
			}
		} else if (lockMode == ActionLockMode.WRITE) {
			// Acquire both executionlock and write lock for write
			boolean loop = true;
			while (loop) {
				loop = false;

				acquireExecutionLockAsJob();
				boolean releaseExecutionLock = false;
				synchronized (lock) {
					releaseExecutionLock = yielding;
				}
				if (releaseExecutionLock) {
					releaseExecutionLock();
					acquireWriteLock();
					releaseWriteLock();
					loop = true;

				}
			}
			acquireWriteLock();
			synchronized (lock) {
				executionOwner = Thread.currentThread();
				writeOwner = Thread.currentThread();
			}
		}
	}

	/**
	 * 
	 * Releases the lock last acquired by current thread.
	 */
	public void release() {
		synchronized (lock) {
			if (executionOwner == Thread.currentThread()) {
				ISchedulingRule rule = peekThreadRule();
				if (rule == writeLock) {
					releaseWriteLock();
					if (writeLock.getDepth() == 0)
						writeOwner = null;
				}
				releaseExecutionLock();
				if (ruleDepth() == 0) {
					executionOwner = null;
				}
			}
		}
	}

	/**
	 * Blocking call to yield for read.
	 * Only yields if the current thread isn't writing,
	 * and there are read operations waiting and the calling
	 * thread hasn't acquired the execution lock by virtue of
	 * some other thread yielding.
	 */
	public void yieldForRead() {
		synchronized (lock) {
			if (yielding || executionOwner != Thread.currentThread()
				|| writeOwner == Thread.currentThread()
				|| readerThreads.size() == 0) {
				return;
			}
			yielding = true;
		}
		boolean writeAcquired = false;
		try {
			writeAcquired = writeLock.acquire(YIELD_TIMEOUT);
		} catch (InterruptedException e) {
			writeAcquired = false;
		}
		if (!writeAcquired)
			return;

		int depth = ruleDepth();
		synchronized (lock) {
			for (int i = 0; i < depth; i++)
				release();
			executionOwner = null;
		}
		synchronized (this) {
			try {
				wait(YIELD_TIMEOUT);
			} catch (InterruptedException e) {
				// ignore this.
			}
		}

		for (int i = 0; i < depth; i++)
			acquire(ActionLockMode.READ);

		synchronized (lock) {
			executionOwner = Thread.currentThread();
			yielding = false;
		}
		writeLock.release();
	}
}
