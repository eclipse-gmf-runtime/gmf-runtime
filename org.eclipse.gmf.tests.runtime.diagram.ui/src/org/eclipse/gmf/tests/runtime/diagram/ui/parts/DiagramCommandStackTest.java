/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.diagram.ui.parts;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;

/**
 * This test should be run as a JUnit Plugin Test
 * 
 * @author ldamus
 */
public class DiagramCommandStackTest extends TestCase {

	private static class MyProgressMonitor implements IProgressMonitor {
		public void beginTask(String name, int totalWork) {
			// do nothing
		}
		public void done() {
			// do nothing
		}
		public void internalWorked(double work) {
			// do nothing
		}
		public boolean isCanceled() {
			return false;
		}
		public void setCanceled(boolean value) {
			// do nothing
		}
		public void setTaskName(String name) {
			// do nothing
		}
		public void subTask(String name) {
			// do nothing
		}
		public void worked(int work) {
			// do nothing
		}
	}

	private DiagramCommandStack fixture = null;

	public DiagramCommandStackTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(DiagramCommandStackTest.class);
	}

	private String getLabel() {
		return "DiagramCommandStackTest"; //$NON-NLS-1$
	}
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected DiagramCommandStack getFixture() {
		return fixture;
	}

	protected void setFixture(DiagramCommandStack fixture) {
		this.fixture = fixture;
	}

	public void test_execute() {

		// Verify that the progress monitor is transfered from the
		// EtoolsProxyCommand to the execute method of the ICommand

		final IProgressMonitor progressMonitor = new MyProgressMonitor();
		ICommand iCommand = new AbstractCommand(getLabel()) {
			protected CommandResult doExecute(IProgressMonitor pm) {
				assertEquals(progressMonitor, pm);
				return newOKCommandResult();
			}
		};
		EtoolsProxyCommand proxyCommand = new EtoolsProxyCommand(iCommand);

		DiagramEditDomain domain = new DiagramEditDomain(null);
				
		setFixture(new DiagramCommandStack(domain));
		getFixture().execute(proxyCommand, progressMonitor);
	}
}
