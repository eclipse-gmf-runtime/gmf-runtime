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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This test should be run as a JUnit Plugin Test
 *
 * @author ldamus
 */
public class DiagramCommandStackTest {

	private static class MyProgressMonitor implements IProgressMonitor {
		@Override
		public void beginTask(String name, int totalWork) {
			// do nothing
		}

		@Override
		public void done() {
			// do nothing
		}

		@Override
		public void internalWorked(double work) {
			// do nothing
		}

		@Override
		public boolean isCanceled() {
			return false;
		}

		@Override
		public void setCanceled(boolean value) {
			// do nothing
		}

		@Override
		public void setTaskName(String name) {
			// do nothing
		}

		@Override
		public void subTask(String name) {
			// do nothing
		}

		@Override
		public void worked(int work) {
			// do nothing
		}
	}

	private DiagramCommandStack fixture = null;

	private String getLabel() {
		return "DiagramCommandStackTest"; //$NON-NLS-1$
	}

	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() {
	}

	protected DiagramCommandStack getFixture() {
		return fixture;
	}

	protected void setFixture(DiagramCommandStack fixture) {
		this.fixture = fixture;
	}

	@Test
	public void test_execute() {

		// Verify that the progress monitor is transfered from the
		// ICommandProxy to the execute method of the ICommand

		final IProgressMonitor progressMonitor = new MyProgressMonitor();
		ICommand iCommand = new AbstractCommand(getLabel(), null) {
			@Override
			protected CommandResult doExecuteWithResult(IProgressMonitor pm, IAdaptable info)
					throws ExecutionException {
				assertEquals(progressMonitor, progressMonitor);
				return CommandResult.newOKCommandResult();
			}

			@Override
			protected CommandResult doRedoWithResult(IProgressMonitor pm, IAdaptable info) throws ExecutionException {
				return null;
			}

			@Override
			protected CommandResult doUndoWithResult(IProgressMonitor pm, IAdaptable info) throws ExecutionException {
				return null;
			}
		};
		ICommandProxy proxyCommand = new ICommandProxy(iCommand);

		DiagramEditDomain domain = new DiagramEditDomain(null);

		setFixture(new DiagramCommandStack(domain));
		getFixture().execute(proxyCommand, progressMonitor);
	}
}
