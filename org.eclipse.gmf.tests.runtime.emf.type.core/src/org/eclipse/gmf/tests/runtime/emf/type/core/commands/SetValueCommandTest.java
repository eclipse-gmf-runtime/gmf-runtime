/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.emf.type.core.commands;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.AbstractEMFTypeTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;

/**
 * Tests the SetValueCommand.
 * 
 * @author ldamus
 */
public class SetValueCommandTest
    extends AbstractEMFTypeTest {

    private Department department;

    private Employee manager;

    private Office managerOffice;

    public SetValueCommandTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(SetValueCommandTest.class);
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
     * Tests that a SetValueCommand is not exectable if the element, feature and
     * new value are not compatible with each other.
     */
    public void test_isExecutable() {

        // correct type for feature
        SetRequest request = new SetRequest(getEditingDomain(), department,
            EmployeePackage.eINSTANCE.getDepartment_Manager(), manager);

        SetValueCommand command = new SetValueCommand(request);
        assertTrue(command.canExecute());

        // null value in simple feature
        request = new SetRequest(getEditingDomain(), department,
            EmployeePackage.eINSTANCE.getDepartment_Manager(), null);

        command = new SetValueCommand(request);
        assertTrue(command.canExecute());

        // incorrect type for feature
        request = new SetRequest(getEditingDomain(), department,
            EmployeePackage.eINSTANCE.getDepartment_Manager(), managerOffice);

        command = new SetValueCommand(request);
        assertFalse(command.canExecute());

        // incorrect feature for element
        request = new SetRequest(getEditingDomain(), department,
            EmployeePackage.eINSTANCE.getEmployee_Office(), manager);

        command = new SetValueCommand(request);
        assertFalse(command.canExecute());

        // null value in many feature
        request = new SetRequest(getEditingDomain(), department,
            EmployeePackage.eINSTANCE.getDepartment_Members(), null);

        command = new SetValueCommand(request);
        assertFalse(command.canExecute());

        // null value in simple feature, but feature does not belong to element
        request = new SetRequest(getEditingDomain(), manager,
            EmployeePackage.eINSTANCE.getDepartment_Manager(), null);

        command = new SetValueCommand(request);
        assertFalse(command.canExecute());

        // non-changeable feature
        EReference managerFeature = EmployeePackage.eINSTANCE
            .getDepartment_Manager();
        managerFeature.setChangeable(false);

        request = new SetRequest(getEditingDomain(), department,
            managerFeature, manager);

        command = new SetValueCommand(request);
        assertFalse(command.canExecute());
    }
}
