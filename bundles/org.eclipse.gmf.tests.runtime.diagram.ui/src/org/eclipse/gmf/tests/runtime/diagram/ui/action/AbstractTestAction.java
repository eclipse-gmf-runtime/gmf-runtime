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
import java.util.Enumeration;
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

import junit.framework.Test;
import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;



/**
 * @author choang
 *
 * Abst
 */
public abstract class AbstractTestAction extends ActionDelegate implements IWorkbenchWindowActionDelegate {
	
	private ArrayList failures = new ArrayList(4);

	/**
	 * @see IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
		// empty block
	}

	/**
	 * Returns the test suite to be executed by the action
	 * 
	 */
	public abstract Test getTestSuite();
	
	/**
	 * @see IWorkbenchWindowActionDelegate#init(IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow arg0) {
		//empty block
	}

	/** 
	 * Return the testplugin.   Subclasses may override to return their specific
	 * test plugin
	 */
	protected Plugin getTestPlugin() {
		return TestsPlugin.getDefault();
	}
	
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction arg0) {
		failures.clear();
		TestSuite suite = new TestSuite();
		
		Test test = getTestSuite();
		if (test == null)
			return;
		suite.addTest(test);
		
		
		TestResult result = new TestResult();
		suite.run(result);
		System.out.println("Test results: " + result.errorCount() + " errors, " + result.failureCount() + " failures."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$;
		Enumeration en = result.errors();
		while (en.hasMoreElements()) {
			Object e = en.nextElement();
			failures.add(e);
			System.out.println(e);
		}
		en = result.failures();
		while (en.hasMoreElements()) {
			Object e = en.nextElement();
			failures.add(e);
			System.out.println(e);
		}
		
		logTestResults();
	}
	
	public void logTestResults() {
		Plugin plugin = getTestPlugin();
		Log.info( plugin, IStatus.INFO, "Test Results:" ); //$NON-NLS-1$
		List results = getFailures();
		for ( int i = 0; i < results.size(); i++ ) {
			Object entry = results.get(i);
			try {
				Log.error( plugin, IStatus.ERROR, entry.toString(), ((TestFailure)entry).thrownException());
			}
			catch( Exception e ) {
				Log.error( plugin, IStatus.ERROR, entry.toString() );
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
	public void selectionChanged(IAction action, ISelection arg1) {
		action.setEnabled(true);
	}

}
