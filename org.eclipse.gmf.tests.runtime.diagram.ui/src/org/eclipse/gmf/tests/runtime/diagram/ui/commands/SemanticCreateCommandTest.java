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
package org.eclipse.gmf.tests.runtime.diagram.ui.commands;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SemanticCreateCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * @author ldamus
 */
public class SemanticCreateCommandTest
	extends TestCase {

	private SemanticCreateCommand fixture;

	public SemanticCreateCommandTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(SemanticCreateCommandTest.class);
	}

	protected SemanticCreateCommand getFixture() {
		return fixture;
	}

	protected void setFixture(SemanticCreateCommand fixture) {
		this.fixture = fixture;
	}

	public void test_wrapCompositeModelCommand() {

		String commandLabel = "test_wrapCompositeModelCommand"; //$NON-NLS-1$

		// Create:
		// EtoolsProxyCommand(CompositeModelCommand(AbstractCommand))
		ICommand command = new AbstractCommand(commandLabel) {

			protected CommandResult doExecute(IProgressMonitor progressMonitor) {
				return newOKCommandResult();
			};
		};

		CompositeModelCommand compositeModelCommand = new CompositeModelCommand(
			commandLabel);
		compositeModelCommand.compose(command);
		EtoolsProxyCommand proxyCommand = new EtoolsProxyCommand(
			compositeModelCommand);

		// Now wrap this in a compound command
		CompoundCommand compoundCommand = new CompoundCommand();
		compoundCommand.add(proxyCommand);

		// Create the test fixture
		CreateElementRequest createRequest = new CreateElementRequest(null,
			null);
		CreateElementRequestAdapter requestAdapter = new CreateElementRequestAdapter(
			createRequest);
		
		setFixture(new SemanticCreateCommand(requestAdapter, compoundCommand));
		
		// Execute the test fixture
		getFixture().execute(new NullProgressMonitor());
		
		CommandResult result = getFixture().getCommandResult();
		assertTrue(result.getStatus().isOK());
		
		// Should return the request adapter
		assertSame(requestAdapter, result.getReturnValue());
	}
}
