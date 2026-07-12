/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.type.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;
import org.junit.jupiter.api.Test;

public class AbstractEditHelperTest extends AbstractEMFTypeTest {

	private Department department;

	@Override
	protected void doModelSetup(Resource resource) {

		department = (Department) getEmployeeFactory().create(getEmployeePackage().getDepartment());
		department.setName("Department"); //$NON-NLS-1$

		resource.getContents().add(department);

	}

	@Test
	public void test_defaultContainmentFeature() {
		// Verifies that if a containment feature is specified in the element
		// type XML, it is used as the default

		CreateElementRequest request = new CreateElementRequest(getEditingDomain(), department, EmployeeType.MANAGER);
		EmployeeType.DEPARTMENT.getEditCommand(request);
		assertSame(getEmployeePackage().getDepartment_Manager(), request.getContainmentFeature());
	}

	@Test
	public void test_replaceDefaultEditCommand() {

		CreateElementRequest request = new CreateElementRequest(getEditingDomain(), department, EmployeeType.MANAGER);

		// Get the default command
		ICommand command = EmployeeType.DEPARTMENT.getEditCommand(request);
		assertNotNull(command);

		// Replace the default command
		request.setParameter(IEditCommandRequest.REPLACE_DEFAULT_COMMAND, Boolean.TRUE);
		command = EmployeeType.DEPARTMENT.getEditCommand(request);
		assertNull(command);
	}

	/**
	 * Tests that the command returned by an edit helper to create a new
	 * relationship is executable if the source or target has not yet been
	 * specified.
	 */
	@Test
	public void test_incompleteCreateRelationshipRequest_117922() {

		// no target
		CreateRelationshipRequest request = new CreateRelationshipRequest(getEditingDomain(), department, department,
				null, EmployeeType.MANAGER);

		ICommand command = EmployeeType.DEPARTMENT.getEditCommand(request);
		assertNotNull(command);
		assertTrue(command.canExecute());

		// no source AND no target
		request = new CreateRelationshipRequest(getEditingDomain(), department, null, null, EmployeeType.MANAGER);

		command = EmployeeType.DEPARTMENT.getEditCommand(request);
		assertTrue(command == null || !command.canExecute());
	}

	/**
	 * Tests the canEdit() method on an edit helper.
	 */
	@Test
	public void test_canEdit_122771() {

		// allowed to create a manager in a department
		CreateElementRequest request = new CreateElementRequest(getEditingDomain(), department, EmployeeType.MANAGER);

		boolean canEdit = EmployeeType.DEPARTMENT.getEditHelper().canEdit(request);
		assertTrue(canEdit);

		canEdit = EmployeeType.DEPARTMENT.canEdit(request);
		assertTrue(canEdit);

		// not allowed to create an office in a department
		request = new CreateElementRequest(getEditingDomain(), department, EmployeeType.OFFICE);

		canEdit = EmployeeType.DEPARTMENT.getEditHelper().canEdit(request);
		assertFalse(canEdit);

		canEdit = EmployeeType.DEPARTMENT.canEdit(request);
		assertFalse(canEdit);
	}

	/**
	 * Tests that the edit helper is consulted to approve edit requests.
	 */
	@Test
	public void test_approveRequest_133160() {

		// Request is approved by the DepartmentEditHelper
		SetRequest setRequest = new SetRequest(department, getEmployeePackage().getDepartment_Number(),
				Integer.valueOf(123456));

		boolean canEdit = EmployeeType.DEPARTMENT.getEditHelper().canEdit(setRequest);
		assertTrue(canEdit);
		Object parameter = setRequest.getParameter("approved"); //$NON-NLS-1$
		assertSame(Boolean.TRUE, parameter);

		// reset the parameter
		setRequest.setParameter("approved", null); //$NON-NLS-1$

		ICommand command = EmployeeType.DEPARTMENT.getEditHelper().getEditCommand(setRequest);
		assertNotNull(command);
		assertTrue(command.canExecute());
		parameter = setRequest.getParameter("approved"); //$NON-NLS-1$
		assertSame(Boolean.TRUE, parameter);

		// Request is not approved by the DepartmentEditHelper
		setRequest = new SetRequest(department, getEmployeePackage().getDepartment_Number(), Integer.valueOf(0));

		canEdit = EmployeeType.DEPARTMENT.getEditHelper().canEdit(setRequest);
		assertFalse(canEdit);
		parameter = setRequest.getParameter("approved"); //$NON-NLS-1$
		assertSame(Boolean.FALSE, parameter);

		// reset the parameter
		setRequest.setParameter("approved", null); //$NON-NLS-1$

		command = EmployeeType.DEPARTMENT.getEditHelper().getEditCommand(setRequest);
		assertNull(command);
		parameter = setRequest.getParameter("approved"); //$NON-NLS-1$
		assertSame(Boolean.FALSE, parameter);
	}
}
