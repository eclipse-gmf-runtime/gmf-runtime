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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

public class MoveRequestTest extends TestCase {

	private MoveRequest fixture;

	private EmployeePackage employeePkg;

	private EFactory employeeFactory;

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

	protected void setUp() throws Exception {
		super.setUp();

		employeePkg = EmployeePackage.eINSTANCE;
		employeeFactory = employeePkg.getEFactoryInstance();

		department1 = (Department) employeeFactory.create(employeePkg
				.getDepartment());
		department1.setName("Department1"); //$NON-NLS-1$

		department2 = (Department) employeeFactory.create(employeePkg
				.getDepartment());
		department2.setName("Department2"); //$NON-NLS-1$

		employee1 = (Employee) employeeFactory
				.create(employeePkg.getEmployee());
		department1.getMembers().add(employee1);
		
		employee2 = (Employee) employeeFactory
		.create(employeePkg.getEmployee());
		department1.getMembers().add(employee2);
		
		manager = (Employee) employeeFactory
		.create(employeePkg.getEmployee());
		department1.setManager(manager);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected MoveRequest getFixture() {
		return fixture;
	}

	protected void setFixture(MoveRequest fixture) {
		this.fixture = fixture;
	}

	public void test_move_singleElement_noFeature() {

		assertSame(department1, employee1.eContainer());
		assertSame(employeePkg.getDepartment_Members(), employee1.eContainmentFeature());

		setFixture(new MoveRequest(department2, employee1));

		IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(getFixture().getEditHelperContext());

		ICommand command = elementType.getEditCommand(getFixture());
		command.execute(new NullProgressMonitor());

		assertSame(department2, employee1.eContainer());
		assertSame(employeePkg.getDepartment_Members(), employee1.eContainmentFeature());
	}
	
	public void test_move_singleElement_differentFeatureInSameContainer() {

		assertSame(department1, employee1.eContainer());
		assertSame(employeePkg.getDepartment_Members(), employee1.eContainmentFeature());

		setFixture(new MoveRequest(department1, employeePkg.getDepartment_Manager(), employee1));

		IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(getFixture().getEditHelperContext());

		ICommand command = elementType.getEditCommand(getFixture());
		command.execute(new NullProgressMonitor());

		assertSame(department1, employee1.eContainer());
		assertSame(employeePkg.getDepartment_Manager(), employee1.eContainmentFeature());
	}
	
	public void test_move_singleElement_featureInNewContainer() {

		assertSame(department1, employee1.eContainer());
		assertSame(employeePkg.getDepartment_Members(), employee1.eContainmentFeature());

		setFixture(new MoveRequest(department2, employeePkg.getDepartment_Manager(), employee1));

		IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(getFixture().getEditHelperContext());

		ICommand command = elementType.getEditCommand(getFixture());
		command.execute(new NullProgressMonitor());

		assertSame(department2, employee1.eContainer());
		assertSame(employeePkg.getDepartment_Manager(), employee1.eContainmentFeature());
	}
	
	public void test_move_manyElements_noFeatures() {

		assertSame(department1, employee1.eContainer());
		assertSame(department1, employee2.eContainer());
		assertSame(department1, manager.eContainer());
		
		assertSame(employeePkg.getDepartment_Members(), employee1.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Members(), employee2.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Manager(), manager.eContainmentFeature());

		List elementsToMove = new ArrayList();
		elementsToMove.add(employee1);
		elementsToMove.add(employee2);
		elementsToMove.add(manager);
		
		setFixture(new MoveRequest(department2, elementsToMove));

		IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(getFixture().getEditHelperContext());

		ICommand command = elementType.getEditCommand(getFixture());
		command.execute(new NullProgressMonitor());

		assertSame(department2, employee1.eContainer());
		assertSame(department2, employee2.eContainer());
		assertSame(department2, manager.eContainer());
		
		assertSame(employeePkg.getDepartment_Members(), employee1.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Members(), employee2.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Manager(), manager.eContainmentFeature());
	}
	
	public void test_move_manyElements_someFeatures() {

		assertSame(department1, employee1.eContainer());
		assertSame(department1, employee2.eContainer());
		assertSame(department1, manager.eContainer());
		
		assertSame(employeePkg.getDepartment_Members(), employee1.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Members(), employee2.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Manager(), manager.eContainmentFeature());

		Map elementsToMove = new HashMap();
		elementsToMove.put(employee1, employeePkg.getDepartment_Manager());
		elementsToMove.put(employee2, null);
		elementsToMove.put(manager, employeePkg.getDepartment_Members());
		
		setFixture(new MoveRequest(department2, elementsToMove));

		IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(getFixture().getEditHelperContext());

		ICommand command = elementType.getEditCommand(getFixture());
		command.execute(new NullProgressMonitor());

		assertSame(department2, employee1.eContainer());
		assertSame(department2, employee2.eContainer());
		assertSame(department2, manager.eContainer());
		
		assertSame(employeePkg.getDepartment_Manager(), employee1.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Members(), employee2.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Members(), manager.eContainmentFeature());
	}
	
	public void test_move_manyElements_featuresInNewContainer() {

		assertSame(department1, employee1.eContainer());
		assertSame(department1, employee2.eContainer());
		assertSame(department1, manager.eContainer());
		
		assertSame(employeePkg.getDepartment_Members(), employee1.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Members(), employee2.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Manager(), manager.eContainmentFeature());

		Map elementsToMove = new HashMap();
		elementsToMove.put(employee1, employeePkg.getDepartment_Manager());
		elementsToMove.put(employee2, employeePkg.getDepartment_Members());
		elementsToMove.put(manager, employeePkg.getDepartment_Members());
		
		setFixture(new MoveRequest(department2, elementsToMove));

		IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(getFixture().getEditHelperContext());

		ICommand command = elementType.getEditCommand(getFixture());
		command.execute(new NullProgressMonitor());

		assertSame(department2, employee1.eContainer());
		assertSame(department2, employee2.eContainer());
		assertSame(department2, manager.eContainer());
		
		assertSame(employeePkg.getDepartment_Manager(), employee1.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Members(), employee2.eContainmentFeature());
		assertSame(employeePkg.getDepartment_Members(), manager.eContainmentFeature());
	}
}
