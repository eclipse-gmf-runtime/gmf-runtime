/******************************************************************************
 * Copyright (c) 2006,2007 IBM Corporation and others.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.AbstractEMFTypeTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeeFactory;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the SetValueCommand.
 * 
 * @author ldamus, mmostafa
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
    
    /**
     * Most probably the problem is in the SetValueCommand#canExecute, line 89
     * @see SetValueCommand
     */
    public void testSetValueCommandForManyFeatureUsingList(){
        EStructuralFeature members = EmployeePackage.eINSTANCE.getDepartment_Members();
        Employee e1 = EmployeeFactory.eINSTANCE.createEmployee();
        Employee e2 = EmployeeFactory.eINSTANCE.createEmployee();
        List list = new LinkedList(Arrays.asList(new Employee[] {e1, e2}));
        SetRequest usingNewListInstance = new SetRequest(department, members, list);
        SetValueCommand cmd = new SetValueCommand(usingNewListInstance);
        verifyExecution(cmd,list);
    }
    
    /**
     * Most probably the problem is in the SetValueCommand#canExecute, line 89
     * @see SetValueCommand
     */
    public void testSetValueCommandForManyFeatureUsingEmptyList(){
        EStructuralFeature members = EmployeePackage.eINSTANCE.getDepartment_Members();
        Employee e1 = EmployeeFactory.eINSTANCE.createEmployee();
        Employee e2 = EmployeeFactory.eINSTANCE.createEmployee();
        List list = new LinkedList(Arrays.asList(new Employee[] {e1, e2}));
        SetRequest usingNewListInstance = new SetRequest(department, members, list);
        SetValueCommand cmd = new SetValueCommand(usingNewListInstance);
        verifyExecution(cmd,list);
        List list2 = new ArrayList();
        SetRequest usingNewEmptyListInstance = new SetRequest(department, members, list2);
        SetValueCommand cmd2 = new SetValueCommand(usingNewEmptyListInstance);
        verifyExecution(cmd2,list2);
    }
    
    /**
     * Most probably the problem is in the SetValueCommand#canExecute, line 89
     * @see SetValueCommand
     */
    public void testSetValueCommandForManyFeatureUsingList_VerifyOldValuesRemoved(){
        EStructuralFeature members = EmployeePackage.eINSTANCE.getDepartment_Members();
        Employee e1 = EmployeeFactory.eINSTANCE.createEmployee();
        Employee e2 = EmployeeFactory.eINSTANCE.createEmployee();
        List list = new LinkedList(Arrays.asList(new Employee[] {e1, e2}));
        SetRequest usingNewListInstance = new SetRequest(department, members, list);
        SetValueCommand cmd = new SetValueCommand(usingNewListInstance);
        verifyExecution(cmd,list);
        Employee e3 = EmployeeFactory.eINSTANCE.createEmployee();
        Employee e4 = EmployeeFactory.eINSTANCE.createEmployee();
        List list2 = new LinkedList(Arrays.asList(new Employee[] {e3, e4}));
        SetRequest usingNewEmptyListInstance = new SetRequest(department, members, list2);
        SetValueCommand cmd2 = new SetValueCommand(usingNewEmptyListInstance);
        verifyExecution(cmd2,list2);
    }
 
    /**
     * Most probably the problem is in the SetValueCommand#canExecute, line 89
     * @see SetValueCommand
     */
    public void testSetValueCommandForManyFeatureUsingSingleValue(){
        EStructuralFeature members = EmployeePackage.eINSTANCE.getDepartment_Members();
        Employee e1 = EmployeeFactory.eINSTANCE.createEmployee();
        Employee e2 = EmployeeFactory.eINSTANCE.createEmployee();
        SetRequest usingSingleValue1 = new SetRequest(department, members, e1);
        SetValueCommand cmd1 = new SetValueCommand(usingSingleValue1);
        verifyExecution(cmd1,e1,1);
        SetRequest usingSingleValue2 = new SetRequest(department, members, e2);
        SetValueCommand cmd2 = new SetValueCommand(usingSingleValue2);
        verifyExecution(cmd2,e2,2);
        
    }
    
    private void verifyExecution(SetValueCommand cmd, List list) {
        try {
            assertTrue("Cannot Execute command",cmd.canExecute()); //$NON-NLS-1$
            cmd.execute(new NullProgressMonitor(), null);
            List list2 = department.getMembers();
            assertTrue("UnexpectedSize", list2.size()==list.size()); //$NON-NLS-1$
            for (Iterator itr = list.iterator(); itr.hasNext();) {
                Object element = itr.next();
                assertTrue("Element not added", list2.contains(element)); //$NON-NLS-1$
            }
        } catch (ExecutionException e) {
            assertTrue("failed to exectue the command",false); //$NON-NLS-1$
            e.printStackTrace();
        }
    }
    
    private void verifyExecution(SetValueCommand cmd, Object object,int size) {
        try {
            assertTrue("Cannot Execute command",cmd.canExecute()); //$NON-NLS-1$
            cmd.execute(new NullProgressMonitor(), null);
            List list2 = department.getMembers();
            assertTrue("UnexpectedSize", list2.size()==size); //$NON-NLS-1$
            assertTrue("Element not added", list2.contains(object)); //$NON-NLS-1$
        } catch (ExecutionException e) {
            assertTrue("failed to exectue the command",false); //$NON-NLS-1$
            e.printStackTrace();
        }
    }
}
