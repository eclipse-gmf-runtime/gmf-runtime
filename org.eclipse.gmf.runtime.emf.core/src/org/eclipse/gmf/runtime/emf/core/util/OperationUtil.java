/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;

/**
 * This class contains a set of utility methods that control the use of
 * undo/redo and eventing features.
 * 
 * @author rafikj
 */
public class OperationUtil {

	/**
	 * Opens an undo interval. An undo interval must be open before starting a
	 * write action.
	 * 
	 * @deprecated Use the {@link #runInUndoInterval(Runnable)} method, instead.
	 */
	public static void openUndoInterval() {
		MEditingDomain.INSTANCE.openUndoInterval();
	}

	/**
	 * Opens an undo interval. An undo interval must be open before starting a
	 * write action.
	 * 
	 * @param label
	 *            The label.
	 * 
	 * @deprecated Use the {@link #runInUndoInterval(String, Runnable)} method, instead.
	 */
	public static void openUndoInterval(String label) {
		MEditingDomain.INSTANCE.openUndoInterval(label,
			MSLConstants.EMPTY_STRING);
	}

	/**
	 * Opens an undo interval. An undo interval must be open before starting a
	 * write action.
	 * 
	 * @param label
	 *            The label.
	 * @param description
	 *            The description.
	 * 
	 * @deprecated Use the {@link #runInUndoInterval(String, String, Runnable)} method, instead.
	 */
	public static void openUndoInterval(String label, String description) {
		MEditingDomain.INSTANCE.openUndoInterval(label, description);
	}

	/**
	 * Closes the currently open undo interval and returns a reference to the
	 * closed interval if interval is not empty. If interval is empty returns
	 * null;
	 * 
	 * @return The undo interval or null if interval is empty.
	 * 
	 * @deprecated Use the {@link #runInUndoInterval(Runnable)} method or a
	 *      variant, instead.
	 */
	public static MUndoInterval closeUndoInterval() {
		return MEditingDomain.INSTANCE.closeUndoInterval();
	}

	/**
	 * Can the current interval be undone?
	 * 
	 * @return True if current interval can be undone.
	 */
	public static boolean canUndoCurrentInterval() {
		return MEditingDomain.INSTANCE.canUndoCurrentInterval();
	}

	/**
	 * Can the current interval be redone?
	 * 
	 * @return True if current interval can be redone.
	 */
	public static boolean canRedoCurrentInterval() {
		return MEditingDomain.INSTANCE.canRedoCurrentInterval();
	}

	/**
	 * Sets can the current interval be undone.
	 */
	public static void setCanUndoCurrentInterval(boolean canUndo) {
		MEditingDomain.INSTANCE.setCanUndoCurrentInterval(canUndo);
	}

	/**
	 * Sets can the current interval be redone.
	 */
	public static void setCanRedoCurrentInterval(boolean canRedo) {
		MEditingDomain.INSTANCE.setCanRedoCurrentInterval(canRedo);
	}

	/**
	 * Runs the runnable instance in an undo interval and will take care of
	 * opening and closing an undo interval.
	 * 
	 * @param runnable
	 *            The runnable.
	 * @return The undo interval.
	 */
	public static MUndoInterval runInUndoInterval(Runnable runnable) {
		return MEditingDomain.INSTANCE.runInUndoInterval(runnable);
	}

	/**
	 * Runs the runnable instance in an undo interval and will take care of
	 * opening and closing an undo interval.
	 * 
	 * @param label
	 *            The label.
	 * @param runnable
	 *            The runnable.
	 * @return The undo interval.
	 */
	public static MUndoInterval runInUndoInterval(String label,
			Runnable runnable) {
		return MEditingDomain.INSTANCE.runInUndoInterval(label, runnable);
	}

	/**
	 * Runs the runnable instance in an undo interval and will take care of
	 * opening and closing an undo interval.
	 * 
	 * @param label
	 *            The label.
	 * @param description
	 *            The description.
	 * @param runnable
	 *            The runnable.
	 * @return The undo interval.
	 */
	public static MUndoInterval runInUndoInterval(String label,
			String description, Runnable runnable) {
		return MEditingDomain.INSTANCE.runInUndoInterval(label, description,
			runnable);
	}

	/**
	 * Checks if there is an open undo interval.
	 * 
	 * @return True if an undo interval is open, false otherwise.
	 */
	public static boolean isUndoIntervalOpen() {
		return MEditingDomain.INSTANCE.isUndoIntervalOpen();
	}

	/**
	 * Starts a read action. Read operations are required for reading models.
	 * 
	 * @deprecated Use the {@link #runAsRead(MRunnable)} method, instead.
	 */
	public static void startRead() {
		MEditingDomain.INSTANCE.startRead();
	}

	/**
	 * Starts a write action. Write operations are required for modifying
	 * models.
	 * 
	 * @deprecated Use the {@link #runAsWrite(MRunnable)} method, instead.
	 */
	public static void startWrite() {
		MEditingDomain.INSTANCE.startWrite();
	}

	/**
	 * Starts an unchecked action. Unchecked operations are required for
	 * modifying models outside of any undo interval. These should be used only
	 * in extreme situations for making modifications outside of undo intervals.
	 * 
	 * @deprecated Use the {@link #runAsUnchecked(MRunnable)} method, instead.
	 */
	public static void startUnchecked() {
		MEditingDomain.INSTANCE.startUnchecked();
	}

	/**
	 * Completes the currently open action and adds it to the currently open
	 * undo interval if applicable.
	 * 
	 * @deprecated Use the {@link #runAsRead(MRunnable)} or
	 *     {@link #runAsWrite(MRunnable)} method, instead.
	 */
	public static void complete() {
		MEditingDomain.INSTANCE.complete();
	}

	/**
	 * <p>
	 * Completes the currently open action and adds it to the currently open
	 * undo interval if applicable.
	 * </p>
	 * <p>
	 * The resulting status indicates warnings or informational messages from
	 * validation. Note that, if errors are reported by validation, then the
	 * action is automatically abandoned and an
	 * {@link MSLActionAbandonedException}is thrown instead of a status being
	 * returned. Thus, clients are required to handle the situation in which the
	 * action is abandoned because the model changes that they expected to have
	 * applied will not have been effected.
	 * </p>
	 * 
	 * @return a status object containing any warnings or informational messages
	 *         produced by validation. This may be a multi-status if there are
	 *         multiple validation messages
	 * 
	 * @throws MSLActionAbandonedException
	 *             if live constraints find errors
	 * 
	 * @deprecated Use the {@link #runAsWrite(MRunnable)} method, instead.
	 */
	public static IStatus completeAndValidate()
		throws MSLActionAbandonedException {
		return MEditingDomain.INSTANCE.completeAndValidate();
	}

	/**
	 * Abandons and discards the currently open action.
	 * 
	 * @deprecated Use the {@link MRunnable#abandon()} method, instead.
	 */
	public static void abandon() {
		MEditingDomain.INSTANCE.abandon();
	}

	/**
	 * Runs the runnable instance in a read action and will take care of
	 * starting and completing the read action.
	 * 
	 * @param runnable
	 *            The runnable.
	 * @throws MSLActionAbandonedException
	 *             if constraints fail.
	 * 
	 * @deprecated Use the {@link #runAsRead(MRunnable)} method, instead.
	 */
	public static void runAsRead(final Runnable runnable)
		throws MSLActionAbandonedException {
		MEditingDomain.INSTANCE.runAsRead(new MRunnable() {
			/* (non-Javadoc)
			 * @see org.eclipse.gmf.runtime.emf.core.internal.MRunnable#run()
			 */
			public Object run() {
				runnable.run();
				return null;
			}
		});
	}

	/**
	 * Runs the runnable instance in a read action and will take care of
	 * starting and completing the read action.
	 * 
	 * @param runnable
	 *            The runnable.
	 */
	public static Object runAsRead(MRunnable runnable) {
		return MEditingDomain.INSTANCE.runAsRead(runnable);
	}

	/**
	 * Runs the runnable instance in a write action and will take care of
	 * starting and completing the write action.
	 * 
	 * @param runnable
	 *            The runnable.
	 * @throws MSLActionAbandonedException
	 *             if constraints fail.
	 * 
	 * @deprecated Use the {@link #runAsWrite(MRunnable)} method, instead.
	 */
	public static void runAsWrite(final Runnable runnable)
		throws MSLActionAbandonedException {

		MEditingDomain.INSTANCE.runAsWrite(new MRunnable() {
			
			/* (non-Javadoc)
			 * @see org.eclipse.gmf.runtime.emf.core.internal.MRunnable#run()
			 */
			public Object run() {
				runnable.run();
				return null;
			}
		});
	}

	/**
	 * <p>
	 * Runs the runnable instance in a write action and will take care of
	 * starting and completing the write action.
	 * </p>
	 * <p>
	 * Note that if this method does not need to start a write action (because
	 * one is already in progress), then it will not attempt to complete it,
	 * either. In such cases, the <code>runnable</code>'s status will be
	 * {@link MRunnable#setStatus(IStatus) set}to an OK status because no
	 * validation is performed.
	 * </p>
	 * <p>
	 * At any point during the execution of the <code>runnable</code>, it may
	 * be {@linkplain MRunnable#abandon() abandoned}. In this case, its status
	 * will be set to a {@link IStatus#CANCEL}value and the write action will
	 * be abandoned when the <code>runnable</code> returns.
	 * </p>
	 * 
	 * @param runnable
	 *            The runnable. The runnable's status is assigned according to
	 *            the results of live validation
	 * 
	 * @throws MSLActionAbandonedException
	 *             if the action is abandoned because live validation detects
	 *             errors
	 * 
	 * @see MRunnable#abandon()
	 */
	public static Object runAsWrite(MRunnable runnable)
		throws MSLActionAbandonedException {
		return MEditingDomain.INSTANCE.runAsWrite(runnable);
	}

	/**
	 * Runs the runnable instance in an unchecked action and will take care of
	 * starting and completing the unchecked action.
	 * 
	 * @param runnable
	 *            The runnable.
	 * 
	 * @deprecated Use the {@link #runAsUnchecked(MRunnable)} method, instead.
	 */
	public static void runAsUnchecked(final Runnable runnable) {

		MEditingDomain.INSTANCE.runAsUnchecked(new MRunnable() {
			/* (non-Javadoc)
			 * @see org.eclipse.gmf.runtime.emf.core.internal.MRunnable#run()
			 */
			public Object run() {
				runnable.run();
				return null;
			}
		});
	}

	/**
	 * Runs the runnable instance in an unchecked action and will take care of
	 * starting and completing the unchecked action.
	 * 
	 * @param runnable
	 *            The runnable.
	 */
	public static Object runAsUnchecked(MRunnable runnable) {
		return MEditingDomain.INSTANCE.runAsUnchecked(runnable);
	}

	/**
	 * Runs the runnable instance without sending events.
	 * 
	 * @param runnable
	 *            The runnable.
	 */
	public static Object runSilent(MRunnable runnable) {
		return MEditingDomain.INSTANCE.runSilent(runnable);
	}

	/**
	 * Runs the runnable instance without semantic procedures.
	 * 
	 * @param runnable
	 *            The runnable.
	 */
	public static Object runWithNoSemProcs(MRunnable runnable) {
		return MEditingDomain.INSTANCE.runWithNoSemProcs(runnable);
	}

	/**
	 * Runs the runnable instance without validation.
	 * 
	 * @param runnable
	 *            The runnable.
	 */
	public static Object runUnvalidated(MRunnable runnable) {
		return MEditingDomain.INSTANCE.runUnvalidated(runnable);
	}

	/**
	 * Runs the runnable instance with options. This method could be used to
	 * combine the effects of runSilent, runUnchecked, runUnvalidated and
	 * runWithNoSemProcs.
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.edit.MRunOption
	 * 
	 * @param runnable
	 *            The runnable.
	 * @param options
	 *            The run options.
	 */
	public static Object runWithOptions(MRunnable runnable, int options) {
		return MEditingDomain.INSTANCE.runWithOptions(runnable, options);
	}

	/**
	 * Checks if one can read, i.e., a read or write action is in progress.
	 * 
	 * @return True if a read or write action is in progress.
	 */
	public static boolean canRead() {
		return MEditingDomain.INSTANCE.canRead();
	}

	/**
	 * Checks if one can write, i.e., a write action is in progress.
	 * 
	 * @return True if a write action is in progress.
	 */
	public static boolean canWrite() {
		return MEditingDomain.INSTANCE.canWrite();
	}

	/**
	 * Checks if a write action is in progress.
	 * 
	 * @return True if a write action is in progress.
	 */
	public static boolean isWriteInProgress() {
		return MEditingDomain.INSTANCE.isWriteInProgress();
	}

	/**
	 * Checks if an unchecked action is in progress.
	 * 
	 * @return True if an unchecked action is in progress.
	 */
	public static boolean isUncheckedInProgress() {
		return MEditingDomain.INSTANCE.isUncheckedInProgress();
	}

	/**
	 * Checks if given notification is caused by undo.
	 * 
	 * @param notification
	 *            The notification.
	 * @return True if notification is caused by undo.
	 */
	public static boolean isUndoNotification(Notification notification) {
		return MEditingDomain.INSTANCE.isUndoNotification(notification);
	}

	/**
	 * Checks if given notification is caused by redo.
	 * 
	 * @param notification
	 *            The notification.
	 * @return True if notification is caused by redo.
	 */
	public static boolean isRedoNotification(Notification notification) {
		return MEditingDomain.INSTANCE.isRedoNotification(notification);
	}

	/**
	 * Sends notification to registered listeners.
	 * 
	 * @param notification
	 *            The notification.
	 */
	public static void sendNotification(Notification notification) {
		MEditingDomain.INSTANCE.sendNotification(notification);
	}

	/**
	 * Sends notification to registered listeners.
	 * 
	 * @param notifier
	 *            The notifier.
	 * @param eventType
	 *            The event type.
	 */

	public static void sendNotification(Object notifier, int eventType) {
		MEditingDomain.INSTANCE.sendNotification(notifier, eventType);
	}

	/**
	 * Yields for other read actions on other threads. Only the actions with
	 * read actions open (NO WRITE) can yield. This is a blocking call.
	 */
	public static void yieldForReads() {
		MEditingDomain.INSTANCE.yieldForReads();
	}

	private OperationUtil() {
		// private
	}
}