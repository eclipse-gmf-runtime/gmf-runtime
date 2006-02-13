/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;

public class AbstractEditHelperTest
    extends AbstractEMFTypeTest {

    private CreateElementRequest fixture;

    private Department department;

    public AbstractEditHelperTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(AbstractEditHelperTest.class);
    }

    protected void doModelSetup() {

        department = (Department) getEmployeeFactory().create(
            getEmployeePackage().getDepartment());
        department.setName("Department"); //$NON-NLS-1$

        getResource().getContents().add(department);

    }

    protected CreateElementRequest getFixture() {
        return fixture;
    }

    protected void setFixture(CreateElementRequest fixture) {
        this.fixture = fixture;
    }

    public void test_defaultContainmentFeature() {
        // Verifies that if a containment feature is specified in the element
        // type XML, it is used as the default

        CreateElementRequest request = new CreateElementRequest(
            getEditingDomain(), department, EmployeeType.MANAGER);
        EmployeeType.DEPARTMENT.getEditCommand(request);
        assertSame(getEmployeePackage().getDepartment_Manager(), request
            .getContainmentFeature());
    }

    public void test_replaceDefaultEditCommand() {

        CreateElementRequest request = new CreateElementRequest(
            getEditingDomain(), department, EmployeeType.MANAGER);

        // Get the default command
        ICommand command = EmployeeType.DEPARTMENT.getEditCommand(request);
        assertNotNull(command);

        // Replace the default command
        request.setParameter(IEditCommandRequest.REPLACE_DEFAULT_COMMAND,
            Boolean.TRUE);
        command = EmployeeType.DEPARTMENT.getEditCommand(request);
        assertNull(command);
    }
}
