/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.parts;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

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
		// ICommandProxy to the execute method of the ICommand

		final IProgressMonitor progressMonitor = new MyProgressMonitor();
		ICommand iCommand = new AbstractCommand(getLabel(), null) {
			protected CommandResult doExecuteWithResult(IProgressMonitor pm, IAdaptable info) throws ExecutionException {
				assertEquals(progressMonitor, progressMonitor);
				return CommandResult.newOKCommandResult();
			}
            protected CommandResult doRedoWithResult(IProgressMonitor pm, IAdaptable info)
                throws ExecutionException {
                return null;
            }
            protected CommandResult doUndoWithResult(IProgressMonitor pm, IAdaptable info)
                throws ExecutionException {
                return null;
            }
		};
		ICommandProxy proxyCommand = new ICommandProxy(iCommand);

		DiagramEditDomain domain = new DiagramEditDomain(null);
				
		setFixture(new DiagramCommandStack(domain));
		getFixture().execute(proxyCommand, progressMonitor);
	}
}
