/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.type.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;

public class CreateElementRequestTest
	extends TestCase {

	private CreateElementRequest fixture;

	private EmployeePackage employeePkg;

	private EFactory employeeFactory;

	private Department department;

	private Employee employee;

	public CreateElementRequestTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(CreateElementRequestTest.class);
	}

	protected void setUp()
		throws Exception {
		super.setUp();

		employeePkg = EmployeePackage.eINSTANCE;
		employeeFactory = employeePkg.getEFactoryInstance();

		department = (Department) employeeFactory.create(employeePkg
			.getDepartment());
		department.setName("Department"); //$NON-NLS-1$

		employee = (Employee) employeeFactory.create(employeePkg.getEmployee());
	}

	protected void tearDown()
		throws Exception {
		super.tearDown();
	}

	protected CreateElementRequest getFixture() {
		return fixture;
	}

	protected void setFixture(CreateElementRequest fixture) {
		this.fixture = fixture;
	}

	public void test_getEditHelperContext_eObject() {
		// Verifies that the container of a create element request is used
		// by default as the edit helper context, when no overrides are
		// implemented.

		setFixture(new CreateElementRequest(department, EmployeeType.EMPLOYEE));
		Object editHelperContext = getFixture().getEditHelperContext();
		assertEquals(department, editHelperContext);
	}

	public void test_getEditHelperContext_elementType() {
		// Verifies that the edit helper context can be customized by clients

		setFixture(new CreateElementRequest(department, EmployeeType.TOP_SECRET));
		Object editHelperContext = getFixture().getEditHelperContext();
		IElementType elementType = ElementTypeRegistry.getInstance()
			.getElementType(editHelperContext);

		// Edit helper for security cleared employees returns the secret
		// department element type as is edit helper context.
		assertEquals(EmployeeType.SECRET_DEPARTMENT, elementType);

		// Get the edit command and execute it
		ICommand command = elementType.getEditCommand(getFixture());
		assertNotNull(command);
		assertTrue(command.isExecutable());

		command.execute(new NullProgressMonitor());
		CommandResult result = command.getCommandResult();
		assertEquals(IStatus.OK, result.getStatus().getCode());

		Employee topSecretEmployee = null;

		topSecretEmployee = (Employee) result.getReturnValue();

		// Verify that the edit helper override set the correct security
		// clearance value
		assertNotNull(topSecretEmployee);
		assertTrue(topSecretEmployee.isSecurityClearance());
	}

	public void test_invalidateEditHelperContext() {
		// Verifies that the edit helper context request is invalidated when the
		// container is changed

		CreateElementRequest request = new CreateElementRequest(department,
			EmployeeType.EMPLOYEE);
		setFixture(request);
		Object departmentContext = getFixture().getEditHelperContext();
		assertEquals(department, departmentContext);

		request.setContainer(employee);
		Object employeeContext = getFixture().getEditHelperContext();
		assertNotSame(departmentContext, employeeContext);
	}
}
