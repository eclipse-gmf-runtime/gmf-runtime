/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.TestsPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;

/**
 * @author choang
 *
 *         Abst
 */
public abstract class AbstractTestAction extends ActionDelegate implements IWorkbenchWindowActionDelegate {

	private ArrayList failures = new ArrayList(4);

	/**
	 * @see IWorkbenchWindowActionDelegate#dispose()
	 */
	@Override
	public void dispose() {
		// empty block
	}

	/**
	 * Returns the test suite to be executed by the action
	 *
	 */
	public abstract Class getTestSuiteClass();

	/**
	 * @see IWorkbenchWindowActionDelegate#init(IWorkbenchWindow)
	 */
	@Override
	public void init(IWorkbenchWindow arg0) {
		// empty block
	}

	/**
	 * Return the testplugin. Subclasses may override to return their specific test
	 * plugin
	 */
	protected Plugin getTestPlugin() {
		return TestsPlugin.getDefault();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction arg0) {
		failures.clear();
		Class testSuiteClass = getTestSuiteClass();
		if (testSuiteClass == null) {
			return;
		}

		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
				.selectors(DiscoverySelectors.selectClass(testSuiteClass)).build();
		SummaryGeneratingListener listener = new SummaryGeneratingListener();
		Launcher launcher = LauncherFactory.create();
		launcher.registerTestExecutionListeners(listener);
		launcher.execute(request);

		TestExecutionSummary summary = listener.getSummary();
		System.out.println("Test results: " + summary.getTestsFailedCount() + " failures."); //$NON-NLS-1$ //$NON-NLS-2$
		for (Failure failure : summary.getFailures()) {
			failures.add(failure);
			System.out.println(failure);
		}

		logTestResults();
	}

	public void logTestResults() {
		Plugin plugin = getTestPlugin();
		Log.info(plugin, IStatus.INFO, "Test Results:"); //$NON-NLS-1$
		List results = getFailures();
		for (Object result : results) {
			Failure entry = (Failure) result;
			try {
				Log.error(plugin, IStatus.ERROR, entry.toString(), entry.getException());
			} catch (Exception e) {
				Log.error(plugin, IStatus.ERROR, entry.toString());
			}
		}
	}

	/** Returns the list of test errors and failures. */
	public List getFailures() {
		return failures;
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection arg1) {
		action.setEnabled(true);
	}

}
