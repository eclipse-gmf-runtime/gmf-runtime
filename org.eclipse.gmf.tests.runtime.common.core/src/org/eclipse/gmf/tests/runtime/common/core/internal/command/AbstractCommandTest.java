/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.core.internal.command;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;

/**
 * @author khussey
 */
public class AbstractCommandTest extends TestCase {

	protected static class Fixture extends AbstractCommand {

		public Fixture(String label) {
			super(label);
		}

		protected CommandResult doExecute(IProgressMonitor progressMonitor) {
			return newOKCommandResult(getLabel());
		}

	}

	private AbstractCommand fixture = null;

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(AbstractCommandTest.class);
	}

	public AbstractCommandTest(String name) {
		super(name);
	}

	protected AbstractCommand getFixture() {
		return fixture;
	}

	protected void setFixture(AbstractCommand fixture) {
		this.fixture = fixture;
	}

	protected void setUp() {
		setFixture(new Fixture(getName()));
	}

	public void test_compose() {
		ICommand command = getFixture().compose(new Fixture(getName()));

		assertTrue(CompositeCommand.class.isInstance(command));
		assertEquals(getFixture().getLabel(), command.getLabel());
	}

	public void test_execute() {
		try {
			getFixture().execute(new NullProgressMonitor());
		} catch (Exception e) {
			fail();
		}

		assertEquals(
			getFixture().getLabel(),
			getFixture().getCommandResult().getReturnValue());
		assertTrue(getFixture().getCommandResult().getStatus().isOK());
		assertEquals(
			0,
			getFixture().getCommandResult().getStatus().getMessage().length());
	}

	/*
	 * MYee - commented out because QE didn't want exceptions to be logged in the log file 
	 */
	public void test_redo() {
		//		try {
		//			getFixture().redo();
		//		} catch (Exception e) {
		//			fail();
		//		}
		//
		//		assertNull(getFixture().getCommandResult().getReturnValue());
		//		assertTrue(
		//			IStatus.ERROR
		//				== getFixture().getCommandResult().getStatus().getSeverity());
		//		assertTrue(
		//			UnsupportedOperationException.class.isInstance(
		//				getFixture().getCommandResult().getStatus().getException()));
	}
	//
	public void test_undo() {
		//		try {
		//			getFixture().undo();
		//		} catch (Exception e) {
		//			fail();
		//		}
		//
		//		assertNull(getFixture().getCommandResult().getReturnValue());
		//		assertTrue(
		//			IStatus.ERROR
		//				== getFixture().getCommandResult().getStatus().getSeverity());
		//		assertTrue(
		//			UnsupportedOperationException.class.isInstance(
		//				getFixture().getCommandResult().getStatus().getException()));
	}

}
