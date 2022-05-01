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
package org.eclipse.gmf.tests.runtime.emf.type.core.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.AbstractEMFTypeTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author ldamus
 */
public class CreateElementCommandTest
    extends AbstractEMFTypeTest {

    private Department department;

    private CreateElementCommand fixture;

    public CreateElementCommandTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(CreateElementCommandTest.class, "CreateElementCommand Test Suite"); //$NON-NLS-1$
    }

    protected void doModelSetup(Resource resource) {
        department = (Department) getEmployeeFactory().create(
            getEmployeePackage().getDepartment());
        department.setName("Department"); //$NON-NLS-1$
        resource.getContents().add(department);
    }

    protected CreateElementCommand getFixture() {
        return fixture;
    }

    protected void setFixture(CreateElementCommand fixture) {
        this.fixture = fixture;
    }

    public void test_isExecutable_noEClassToEdit() {

        CreateElementRequest request = new CreateElementRequest(
            getEditingDomain(), null, EmployeeType.EMPLOYEE,
            EmployeePackage.eINSTANCE.getDepartment_Members());
        setFixture(new CreateElementCommand(request));

        assertFalse(getFixture().canExecute());
    }
    
    /**
	 * Verifies that the status from configuring the new element is reported in
	 * the command result of the create element command.
	 */
    public void test_configureStatusPropagated_139736() {

		// First create an executive with no configuration failure
		CreateElementRequest request = new CreateElementRequest(
				getEditingDomain(), department, EmployeeType.EXECUTIVE,
				EmployeePackage.eINSTANCE.getDepartment_Members());

		CreateElementCommand command = new CreateElementCommand(request);

		IStatus status = null;

		try {
			status = command.execute(new NullProgressMonitor(), null);

		} catch (ExecutionException e) {
			fail("Command execution failed: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}

		assertEquals(IStatus.OK, command.getCommandResult().getStatus()
				.getSeverity());
		assertEquals(IStatus.OK, status.getSeverity());

		// Now create an executive whose configuration returns a warning status
		request = new CreateElementRequest(getEditingDomain(), department,
				EmployeeType.EXECUTIVE, EmployeePackage.eINSTANCE
						.getDepartment_Members());

		request.setParameter("fail_configuration", Boolean.TRUE); //$NON-NLS-1$

		command = new CreateElementCommand(request);
		status = null;

		try {
			status = command.execute(new NullProgressMonitor(), null);

		} catch (ExecutionException e) {
			fail("Command execution failed: " + e.getLocalizedMessage()); //$NON-NLS-1$
		}

		assertEquals(IStatus.WARNING, command.getCommandResult().getStatus()
				.getSeverity());
		assertEquals(IStatus.WARNING, status.getSeverity());
	}
}
