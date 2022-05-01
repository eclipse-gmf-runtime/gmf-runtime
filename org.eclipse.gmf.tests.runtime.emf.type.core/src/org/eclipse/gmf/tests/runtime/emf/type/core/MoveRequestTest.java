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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class MoveRequestTest
    extends AbstractEMFTypeTest {

    private MoveRequest fixture;

    private Department department1;

    private Department department2;

    private Employee employee1;

    private Employee employee2;

    private Employee manager;

    public MoveRequestTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(MoveRequestTest.class);
    }

    protected void doModelSetup(Resource resource) {

        department1 = (Department) getEmployeeFactory().create(
            getEmployeePackage().getDepartment());
        department1.setName("Department1"); //$NON-NLS-1$
        resource.getContents().add(department1);

        department2 = (Department) getEmployeeFactory().create(
            getEmployeePackage().getDepartment());
        department2.setName("Department2"); //$NON-NLS-1$
        resource.getContents().add(department2);

        employee1 = (Employee) getEmployeeFactory().create(
            getEmployeePackage().getEmployee());
        department1.getMembers().add(employee1);

        employee2 = (Employee) getEmployeeFactory().create(
            getEmployeePackage().getEmployee());
        department1.getMembers().add(employee2);

        manager = (Employee) getEmployeeFactory().create(
            getEmployeePackage().getEmployee());
        department1.setManager(manager);
    }

    protected MoveRequest getFixture() {
        return fixture;
    }

    protected void setFixture(MoveRequest fixture) {
        this.fixture = fixture;
    }

    public void test_move_singleElement_noFeature() {

        assertSame(department1, employee1.eContainer());
        assertSame(getEmployeePackage().getDepartment_Members(), employee1
            .eContainmentFeature());

        setFixture(new MoveRequest(getEditingDomain(), department2, employee1));

        IElementType elementType = ElementTypeRegistry.getInstance()
            .getElementType(getFixture().getEditHelperContext());

        ICommand command = elementType.getEditCommand(getFixture());
        try {
            command.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }

        assertSame(department2, employee1.eContainer());
        assertSame(getEmployeePackage().getDepartment_Members(), employee1
            .eContainmentFeature());
    }

    public void test_move_singleElement_differentFeatureInSameContainer() {

        assertSame(department1, employee1.eContainer());
        assertSame(getEmployeePackage().getDepartment_Members(), employee1
            .eContainmentFeature());

        setFixture(new MoveRequest(getEditingDomain(), department1,
            getEmployeePackage().getDepartment_Manager(), employee1));

        IElementType elementType = ElementTypeRegistry.getInstance()
            .getElementType(getFixture().getEditHelperContext());

        ICommand command = elementType.getEditCommand(getFixture());
        try {
            command.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }

        assertSame(department1, employee1.eContainer());
        assertSame(getEmployeePackage().getDepartment_Manager(), employee1
            .eContainmentFeature());
    }

    public void test_move_singleElement_featureInNewContainer() {

        assertSame(department1, employee1.eContainer());
        assertSame(getEmployeePackage().getDepartment_Members(), employee1
            .eContainmentFeature());

        setFixture(new MoveRequest(getEditingDomain(), department2,
            getEmployeePackage().getDepartment_Manager(), employee1));

        IElementType elementType = ElementTypeRegistry.getInstance()
            .getElementType(getFixture().getEditHelperContext());

        ICommand command = elementType.getEditCommand(getFixture());
        try {
            command.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }

        assertSame(department2, employee1.eContainer());
        assertSame(getEmployeePackage().getDepartment_Manager(), employee1
            .eContainmentFeature());
    }

    public void test_move_manyElements_noFeatures() {

        assertSame(department1, employee1.eContainer());
        assertSame(department1, employee2.eContainer());
        assertSame(department1, manager.eContainer());

        assertSame(getEmployeePackage().getDepartment_Members(), employee1
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Members(), employee2
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Manager(), manager
            .eContainmentFeature());

        List elementsToMove = new ArrayList();
        elementsToMove.add(employee1);
        elementsToMove.add(employee2);
        elementsToMove.add(manager);

        setFixture(new MoveRequest(getEditingDomain(), department2,
            elementsToMove));

        IElementType elementType = ElementTypeRegistry.getInstance()
            .getElementType(getFixture().getEditHelperContext());

        ICommand command = elementType.getEditCommand(getFixture());
        try {
            command.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }

        assertSame(department2, employee1.eContainer());
        assertSame(department2, employee2.eContainer());
        assertSame(department2, manager.eContainer());

        assertSame(getEmployeePackage().getDepartment_Members(), employee1
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Members(), employee2
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Manager(), manager
            .eContainmentFeature());
    }

    public void test_move_manyElements_someFeatures() {

        assertSame(department1, employee1.eContainer());
        assertSame(department1, employee2.eContainer());
        assertSame(department1, manager.eContainer());

        assertSame(getEmployeePackage().getDepartment_Members(), employee1
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Members(), employee2
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Manager(), manager
            .eContainmentFeature());

        Map elementsToMove = new HashMap();
        elementsToMove.put(employee1, getEmployeePackage()
            .getDepartment_Manager());
        elementsToMove.put(employee2, null);
        elementsToMove.put(manager, getEmployeePackage()
            .getDepartment_Members());

        setFixture(new MoveRequest(getEditingDomain(), department2,
            elementsToMove));

        IElementType elementType = ElementTypeRegistry.getInstance()
            .getElementType(getFixture().getEditHelperContext());

        ICommand command = elementType.getEditCommand(getFixture());
        try {
            command.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }

        assertSame(department2, employee1.eContainer());
        assertSame(department2, employee2.eContainer());
        assertSame(department2, manager.eContainer());

        assertSame(getEmployeePackage().getDepartment_Manager(), employee1
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Members(), employee2
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Members(), manager
            .eContainmentFeature());
    }

    public void test_move_manyElements_featuresInNewContainer() {

        assertSame(department1, employee1.eContainer());
        assertSame(department1, employee2.eContainer());
        assertSame(department1, manager.eContainer());

        assertSame(getEmployeePackage().getDepartment_Members(), employee1
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Members(), employee2
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Manager(), manager
            .eContainmentFeature());

        Map elementsToMove = new HashMap();
        elementsToMove.put(employee1, getEmployeePackage()
            .getDepartment_Manager());
        elementsToMove.put(employee2, getEmployeePackage()
            .getDepartment_Members());
        elementsToMove.put(manager, getEmployeePackage()
            .getDepartment_Members());

        setFixture(new MoveRequest(getEditingDomain(), department2,
            elementsToMove));

        IElementType elementType = ElementTypeRegistry.getInstance()
            .getElementType(getFixture().getEditHelperContext());

        ICommand command = elementType.getEditCommand(getFixture());
        try {
            command.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }

        assertSame(department2, employee1.eContainer());
        assertSame(department2, employee2.eContainer());
        assertSame(department2, manager.eContainer());

        assertSame(getEmployeePackage().getDepartment_Manager(), employee1
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Members(), employee2
            .eContainmentFeature());
        assertSame(getEmployeePackage().getDepartment_Members(), manager
            .eContainmentFeature());
    }
}
