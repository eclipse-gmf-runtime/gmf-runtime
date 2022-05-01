/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.emf.type.core.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.type.core.commands.DeferredSetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.AbstractEMFTypeTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the DeferredSetValueCommand.
 * 
 * @author ldamus
 */
public class DeferredSetValueCommandTest extends AbstractEMFTypeTest {

	private Department department;

	private Employee manager;

	private Office managerOffice;

	public DeferredSetValueCommandTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(DeferredSetValueCommandTest.class);
	}

	protected void doModelSetup(Resource resource) {
		department = (Department) getEmployeeFactory().create(
				getEmployeePackage().getDepartment());
		department.setName("Department"); //$NON-NLS-1$
		resource.getContents().add(department);

		manager = (Employee) getEmployeeFactory().create(
				getEmployeePackage().getEmployee());

		managerOffice = (Office) getEmployeeFactory().create(
				getEmployeePackage().getOffice());
	}

	/**
	 * Tests that a DeferredSetValueCommand is executable when it has no
	 * elementToEdit.
	 */
	public void test_isExecutable() {

		// command is executable when no elementToEdit in the request
		SetRequest request = new SetRequest(getEditingDomain(), null,
				EmployeePackage.eINSTANCE.getDepartment_Manager(), manager);

		TestDeferredSetValueCommand command = new TestDeferredSetValueCommand(
				request);
		assertTrue(command.canExecute());

		// command is not executable with invalid elementToEdit
		command.setOwner(managerOffice);
		assertFalse(command.canExecute());

		// command is executable with valid elementToEdit
		command.setOwner(department);
		assertTrue(command.canExecute());

		// execute the command
		try {
			assertNull(department.getManager());
			command.execute(new NullProgressMonitor(), null);
			assertSame(manager, department.getManager());

		} catch (ExecutionException e) {
			fail("unexpected execution exception"); //$NON-NLS-1$
		}
	}

	// Test fixtures

	private class TestDeferredSetValueCommand extends DeferredSetValueCommand {

		private EObject owner;

		public TestDeferredSetValueCommand(SetRequest request) {
			super(request);
		}

		protected EObject getElementToEdit() {
			return owner;
		}

		public void setOwner(EObject owner) {
			this.owner = owner;
		}
	}
}
