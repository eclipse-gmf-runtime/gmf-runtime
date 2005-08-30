/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;



/**
 * This progress monitor dialog is a modal dialog for monitoring the progress of
 * cancelable model operations.
 * <P>
 * It differs from its superclass
 * {@link org.eclipse.jface.dialogs.ProgressMonitorDialog}in its progress
 * monitor will dispatch pending UI events to the display thread whenever its
 * <code>isCanceled()</code> method is called.
 * <P>
 * Progress of model operations can be measured between the individual
 * operations in a compound command, or by using progress events from the model
 * server.
 * <P>
 * For monitoring the progress of model operations which are not cancelable,
 * {@link org.eclipse.jface.dialogs.ProgressMonitorDialog}can be used.
 * 
 * @author ldamus
 * @author Yasser Lulu
 */

public class DispatchingProgressMonitorDialog
	extends ProgressMonitorDialog {

	/**
	 * Name to use for task when normal task name is empty string.
	 */
	private static String DEFAULT_TASKNAME = JFaceResources
		.getString("ProgressMonitorDialog.message"); //$NON-NLS-1$

	

	

	private static void readAndDispatch(Display display) {
		try {
			while (display.readAndDispatch()) {
				//flush display events & messages
			}
		} catch (Exception ex) {
			Trace.catching(CommonUIPlugin.getDefault(),
				CommonUIDebugOptions.EXCEPTIONS_CATCHING,
				DispatchingProgressMonitorDialog.class, "readAndDispatch", ex); //$NON-NLS-1$			
			Log.error(CommonUIPlugin.getDefault(),
				CommonUIStatusCodes.RESOURCE_FAILURE, " readAndDispatch", ex); //$NON-NLS-1$
		}
	}

	/**
	 * Internal progress monitor implementation. Replaces the implementation in
	 * the superclass so that the isCancelled method first dispatches any
	 * pending UI events (e.g., clicks on the cancel button) to the display.
	 */
	private class DispatchingProgressMonitor
		implements IProgressMonitor {

		private String task;

		private String subTask = StringStatics.BLANK;

		private boolean canceled;

		public void beginTask(String name, int totalWork) {
			if (progressIndicator.isDisposed()) {
				return;
			}

			setTaskName(name);

			if (totalWork == UNKNOWN) {
				progressIndicator.beginAnimatedTask();
			} else {
				progressIndicator.beginTask(totalWork);

			}
		}

		public void done() {
			if (!progressIndicator.isDisposed()) {
				progressIndicator.sendRemainingWork();
				progressIndicator.done();

			}
		}

		public void setTaskName(String name) {
			if (taskLabel.isDisposed()) {
				return;
			}
			if (name == null) {
				task = StringStatics.BLANK;
			} else {
				task = name;
			}

			String label = task;
			if (label.length() <= 0)
				label = DEFAULT_TASKNAME;
			taskLabel.setText(label);
		}

		public boolean isCanceled() {
			// Make sure any pending UI cancel events are processed before
			// we return the cancelled flag
			Display display = getShell().getDisplay();
			readAndDispatch(display);
			return canceled;
		}

		public void setCanceled(boolean b) {
			canceled = b;

		}

		public void subTask(String name) {
			if (subTaskLabel.isDisposed()) {
				return;
			}

			if (name == null) {
				subTask = StringStatics.BLANK;
			} else {
				subTask = name;
			}

			subTaskLabel.setText(subTask);
		}

		public void worked(int work) {
			internalWorked(work);
		}

		public void internalWorked(double work) {
			if (!progressIndicator.isDisposed())
				progressIndicator.worked(work);

		}
	}

	/**
	 * The progress monitor.
	 */
	private DispatchingProgressMonitor dispatchingProgressMonitor = new DispatchingProgressMonitor();

	/**
	 * Creates a progress monitor dialog under the given shell. The dialog has a
	 * standard title and no image. <code>open</code> is non-blocking.
	 * 
	 * @param parent
	 *            the parent shell
	 */
	public DispatchingProgressMonitorDialog(Shell parent) {
		super(parent);
	}

	/**
	 * Runs the given <code>IRunnableWithProgress</code> with the progress
	 * monitor for this progress dialog. The dialog is opened before it is run,
	 * and closed after it completes.
	 * <P>
	 * The <code>runnable</code> is always run on the same thread as this
	 * dialog.
	 * 
	 * @param cancelable
	 *            <code>true</code> if the cancel button should be enabled,
	 *            <code>false</code> otherwise.
	 * @param runnable
	 *            the runnable to execute in the progress dialog
	 * 
	 * @exception InvocationTargetException
	 *                wraps any exception or error which occurs while running
	 *                the runnable
	 * @exception InterruptedException
	 *                propagated by the context if the runnable acknowledges
	 *                cancelation by throwing this exception
	 */
	public void run(boolean cancelable, IRunnableWithProgress runnable)
		throws InvocationTargetException, InterruptedException {
		this.run(false, cancelable, runnable);
	}

	/**
	 * Overrides the superclass method to never allow the runnable to be forked
	 * onto a separate thread. The model server is not thread safe.
	 * <P>
	 * The <code>runnable</code> is always run on the same thread as this
	 * dialog, i.e., <code>fork</code> must be false.
	 * @throws IllegalArgumentException if any of the following occurs:
	 * 	- this method is invoked in a non UI thread 
	 *  - this method is re-entered in a UI thread other than the one used to invoke it first 
	 *  - this method is invoked with a fork parameter set to true
	 * 
	 * @see org.eclipse.jface.dialogs.ProgressMonitorDialog#run(boolean,
	 *      boolean, IRunnableWithProgress)
	 */
	public void run(boolean fork, boolean cancelable,
		IRunnableWithProgress runnable) throws InvocationTargetException,
		InterruptedException {
		if (Display.getCurrent() == null) {
			IllegalArgumentException iae = new IllegalArgumentException(
				"Cannot use a DispatchingProgressMonitorDialog in a non UI Thread");//$NON-NLS-1$
			Trace.throwing(CommonUIPlugin.getDefault(),
				CommonUIDebugOptions.EXCEPTIONS_THROWING,
				DispatchingProgressMonitorDialog.class, "run", iae); //$NON-NLS-1$
			throw iae;
		}
	
		if (fork) {
			IllegalArgumentException iae = new IllegalArgumentException(
				"Cannot fork a thread using the DispatchingProgressMonitorDialog");//$NON-NLS-1$
			Trace.throwing(CommonUIPlugin.getDefault(),
				CommonUIDebugOptions.EXCEPTIONS_THROWING,
				DispatchingProgressMonitorDialog.class, "run", iae); //$NON-NLS-1$
			throw iae;
		}
		super.run(false, cancelable, runnable);
	}

	/**
	 * @see org.eclipse.jface.dialogs.ProgressMonitorDialog#getProgressMonitor()
	 */
	public IProgressMonitor getProgressMonitor() {
		return dispatchingProgressMonitor;
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		cancel.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				cancel.setEnabled(false);
				getProgressMonitor().setCanceled(true);
			}
		});
	}
}

