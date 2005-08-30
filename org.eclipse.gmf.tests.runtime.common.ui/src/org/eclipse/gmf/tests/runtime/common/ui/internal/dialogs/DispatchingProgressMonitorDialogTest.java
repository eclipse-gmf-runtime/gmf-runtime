/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005. All Rights Reserved.               |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.common.ui.internal.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.ui.dialogs.DispatchingProgressMonitorDialog;

/**
 * @author Yasser Lulu 
 */
public class DispatchingProgressMonitorDialogTest
	extends TestCase {

	private static final String PDE_PERSPECTIVE_ID = "org.eclipse.pde.ui.PDEPerspective";//$NON-NLS-1$

	public static Test suite() {
		return new TestSuite(DispatchingProgressMonitorDialogTest.class);
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			//ignore
		}
	}

	private class DisplayRunnable
		implements Runnable {

		private Object data;//just to know who posted this guy for debugging

		public DisplayRunnable(Object data) {
			runnables.add(this);
			runnablesCounter++;
			this.data = data;
		}

		public void run() {
			if (getRandomBoolean()) {
				try {
					ResourcesPlugin.getWorkspace().run(
						new IWorkspaceRunnable() {

							public void run(IProgressMonitor progressMonitor)
								throws CoreException {
								runnables.remove(DisplayRunnable.this);
							}
						}, new NullProgressMonitor());
				} catch (CoreException e) {
					//ignore
				}
			} else {
				runnables.remove(this);
			}

			if (threadCounter < 100) {
				launchThreads();
			}

			//			runnables.remove(this);
			sleep(getRandomNumber(4) * 1000);
		}
		/**
		 * @return Returns the data.
		 */
		public Object getData() {
			return data;
		}
	}

	private class ThreadRunnable
		implements Runnable {

		private Runnable displayRunnable;

		private boolean synchronous;

		public ThreadRunnable() {
			this.displayRunnable = new DisplayRunnable(this);
			this.synchronous = getRandomBoolean();
		}

		public void run() {
			if (synchronous) {
				getDisplay().syncExec(displayRunnable);
			} else {
				getDisplay().asyncExec(displayRunnable);
			}
		}

		public String getDisplayRunnableType() {
			return (synchronous) ? "Sync" : "Async";//$NON-NLS-2$//$NON-NLS-1$
		}
	}

	private class WorkspaceThreadRunnable
		extends ThreadRunnable {

		public void run() {
			try {
				ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {

					public void run(IProgressMonitor progressMonitor)
						throws CoreException {
						WorkspaceThreadRunnable.super.run();
					}
				}, new NullProgressMonitor());
			} catch (CoreException e) {
				//ignore
			}
		}
	}

	private class TestRunnableWithProgress
		implements IRunnableWithProgress {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void run(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {
			launchThreads();
			monitor.beginTask(
				"TestRunnableWithProgress:  R_" + runnablesCounter //$NON-NLS-1$
					+ "    T_" + threadCounter, IProgressMonitor.UNKNOWN);//$NON-NLS-1$
			monitor.isCanceled();
			showViews();
			sleep(getRandomNumber(1000));
			monitor.isCanceled();
			getDisplay().timerExec(getRandomNumber(4000), new ThreadRunnable());
			monitor.isCanceled();
			sleep(getRandomNumber(1000));
			closeViews();
			monitor.isCanceled();
			new ThreadRunnable().run();
			monitor.isCanceled();
			sleep(getRandomNumber(1000));
			monitor.done();
			launchThreads();
		}
	}

	private void showViews() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage();
		Iterator it = viewIds.iterator();
		while (it.hasNext()) {
			try {
				IViewPart part = page.showView((String) it.next());
				page.activate(part);
				sleep(1000);
			} catch (PartInitException e) {
				//ignore
			}
		}
	}

	private void closeViews() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage();
		IViewReference[] viewRefs = page.getViewReferences();
		for (int i = 0; i < viewRefs.length; i++) {
			if (viewIds.contains(viewRefs[i].getId())) {
				page.hideView(viewRefs[i]);
			}
		}
	}

	private class TestRunnableWithProgresslaunchingDialog
		extends TestRunnableWithProgress {

		public void run(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {
			super.run(monitor);
			new DispatchingProgressMonitorDialog(getDisplay().getActiveShell())
				.run(true, new TestRunnableWithProgress());
		}
	}

	private Set runnables = Collections.synchronizedSet(new HashSet());

	private List viewIds = new ArrayList();

	private int runnablesCounter;

	private int threadCounter;

	private Display display;

	private ThreadGroup threadGroup;

	public void test_DispatchingProgressMonitorDialog() {
		try {
			new ThreadRunnable().run();
			doTestDispatchingProgressMonitorDialog();
			doTestDispatchingProgressMonitorDialog();
			doTestDispatchingProgressMonitorDialog();
			checkResult();
		} catch (Exception ex) {
			Assert.assertTrue(ex.getMessage(), false);
		}
	}

	private int tryToWaitTillDone() {
		int counter = 0;
		int size = runnables.size();
		while ((counter++ < size) && (runnables.size() > 0)) {
			readAndDispatch();
			sleep(1000);
		}
		return counter;
	}

	private void checkResult() {
		sleep(100);
		tryToWaitTillDone();
		readAndDispatch();
		sleep(100);
		Assert.assertTrue(runnables.isEmpty()); //$NON-NLS-1$
		if (anyThreadAlive()) {
			Assert.assertTrue(false);
		}
	}

	private boolean anyThreadAlive() {
		boolean threadAlive = false;
		int activeCount = threadGroup.activeCount();
		if (activeCount > 0) {
			Thread[] activeThreads = new Thread[activeCount];
			threadGroup.enumerate(activeThreads);
			for (int i = 0; i < activeThreads.length; ++i) {
				if ((activeThreads[i] != null) && (activeThreads[i].isAlive())) {
					try {
						readAndDispatch();
						sleep(10);
						threadAlive |= (activeThreads[i].getName().startsWith(
							"Sync") || activeThreads[i].getName().startsWith("Async"));//$NON-NLS-2$//$NON-NLS-1$						
					} catch (Exception ex) {
						//ignore
					}
				}
			}
		}
		return threadAlive;
	}

	private void readAndDispatch() {
		try {
			while (getDisplay().readAndDispatch()) {
				//ignore
			}
		} catch (Exception ex) {
			//ignore
		}
	}

	private void doTestDispatchingProgressMonitorDialog() throws Exception {
		DispatchingProgressMonitorDialog dialog = new DispatchingProgressMonitorDialog(
			getDisplay().getActiveShell());
		launchThreads();
		dialog.run(true, new TestRunnableWithProgresslaunchingDialog());
	}

	private int getRandomNumber(int max) {
		return ((int) (Math.random() * max)) + 1;
	}

	private boolean getRandomBoolean() {
		return ((getRandomNumber(100) % 2) == 0);
	}

	private void launchThreads() {
		int num = 0;
		while (++num <= 4) {
			ThreadRunnable runnable = (getRandomBoolean()) ? new ThreadRunnable()
				: new WorkspaceThreadRunnable();
			Thread t = new Thread(threadGroup, runnable, runnable
				.getDisplayRunnableType()
				+ "_" + runnablesCounter);//$NON-NLS-1$
			t.start();
			threadCounter++;
			sleep(10);
		}
	}

	private Display getDisplay() {
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	protected void setUp() throws Exception {
		super.setUp();
		PlatformUI.getWorkbench().showPerspective(PDE_PERSPECTIVE_ID,
			PlatformUI.getWorkbench().getActiveWorkbenchWindow(),
			ResourcesPlugin.getWorkspace().getRoot());
		viewIds.add(IPageLayout.ID_PROP_SHEET);
		viewIds.add(IPageLayout.ID_RES_NAV);
		viewIds.add(IPageLayout.ID_BOOKMARKS);
		viewIds.add(IPageLayout.ID_TASK_LIST);
		threadGroup = new ThreadGroup(
			"DispatchingProgressMonitorDialogTest Thread Group");//$NON-NLS-1$
	}

}
