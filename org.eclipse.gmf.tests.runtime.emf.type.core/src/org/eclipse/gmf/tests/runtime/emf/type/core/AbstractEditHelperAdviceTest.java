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

package org.eclipse.gmf.tests.runtime.emf.type.core;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;

public class AbstractEditHelperAdviceTest
    extends AbstractEMFTypeTest {

    private Department department;
    
    private Employee financeEmployee;

    public AbstractEditHelperAdviceTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(AbstractEditHelperAdviceTest.class,
            "AbstractEditHelperAdvice Test Suite"); //$NON-NLS-1$
    }

    protected void doModelSetup(Resource resource) {

        department = (Department) getEmployeeFactory().create(
            getEmployeePackage().getDepartment());
        department.setName("Finance"); //$NON-NLS-1$

        resource.getContents().add(department);
        
        financeEmployee = (Employee) getEmployeeFactory().create(
            getEmployeePackage().getEmployee());
        
        department.getMembers().add(financeEmployee);
    }
    
    /**
     * Tests that edit helper advice is consulted to approve edit requests.
     */
    public void test_approveRequest_133160() {
        
        // Request is approved by the FinanceEditHelperAdvice
        SetRequest setRequest = new SetRequest(financeEmployee,
            getEmployeePackage().getEmployee_Band(), Band.SENIOR_LITERAL);
        
        boolean canEdit = EmployeeType.EMPLOYEE.getEditHelper().canEdit(setRequest);
        assertTrue(canEdit);
        Object parameter = setRequest.getParameter("approved"); //$NON-NLS-1$
        assertSame(Boolean.TRUE, parameter);
        
        // reset the parameter
        setRequest.setParameter("approved", null); //$NON-NLS-1$
        
        ICommand command = EmployeeType.EMPLOYEE.getEditHelper().getEditCommand(setRequest);
        assertNotNull(command);
        assertTrue(command.canExecute());
        parameter = setRequest.getParameter("approved"); //$NON-NLS-1$
        assertSame(Boolean.TRUE, parameter);
        
        
        // Request is not approved by the FinanceEditHelperAdvice
        setRequest = new SetRequest(financeEmployee,
            getEmployeePackage().getEmployee_Band(), Band.DIRECTOR_LITERAL);
        
        canEdit = EmployeeType.EMPLOYEE.getEditHelper().canEdit(setRequest);
        assertFalse(canEdit);
        parameter = setRequest.getParameter("approved"); //$NON-NLS-1$
        assertSame(Boolean.FALSE, parameter);
        
        // reset the parameter
        setRequest.setParameter("approved", null); //$NON-NLS-1$
        
        command = EmployeeType.EMPLOYEE.getEditHelper().getEditCommand(setRequest);
        assertNull(command);
        parameter = setRequest.getParameter("approved"); //$NON-NLS-1$
        assertSame(Boolean.FALSE, parameter);
    }
}
