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

import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeAddedEvent;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IElementTypeFactory;
import org.eclipse.gmf.runtime.emf.type.core.IElementTypeRegistryListener;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.MetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.SpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.DefaultElementTypeFactory;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Student;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.ExecutiveEditHelperAdvice;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.FinanceEditHelperAdvice;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.ManagerEditHelperAdvice;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.NotInheritedEditHelperAdvice;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.SecurityClearedElementTypeFactory;

/**
 * @author ldamus
 */
public class ElementTypeRegistryTest
	extends TestCase {
	
	private class MySpecializationAdvice extends AbstractEditHelperAdvice {
		public MySpecializationAdvice() {
			super();
		}
		protected ICommand getBeforeCreateCommand(CreateElementRequest request) {
			return super.getBeforeCreateCommand(request);
		}
	}

	private ElementTypeRegistry fixture = null;

	private EmployeePackage employeePkg;

	private EFactory employeeFactory;

	// Model elements
	private Department department;

	private Department executiveDepartment;

	private Department financeDepartment;

	private Employee employee;

	private Employee financeEmployee;

	private Employee financeManager;

	private Student student;

	private Office employeeOffice;

	private Office studentOffice;

	private Employee manager;

	private Office managerOffice;

	private Employee executive;

	private Office executiveOffice;

	/**
	 * Constructor for CreateDiagramCommandTest.
	 * 
	 * @param name
	 */
	public ElementTypeRegistryTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(ElementTypeRegistryTest.class);
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp()
		throws Exception {
		super.setUp();

		setFixture(ElementTypeRegistry.getInstance());

		employeePkg = EmployeePackage.eINSTANCE;
		employeeFactory = employeePkg.getEFactoryInstance();

		department = (Department) employeeFactory.create(employeePkg
			.getDepartment());
		department.setName("Department"); //$NON-NLS-1$

		executiveDepartment = (Department) employeeFactory.create(employeePkg
			.getDepartment());
		executiveDepartment.setName("ExecutiveDepartment"); //$NON-NLS-1$

		financeDepartment = (Department) employeeFactory.create(employeePkg
			.getDepartment());
		financeDepartment.setName("Finance"); //$NON-NLS-1$

		employee = (Employee) employeeFactory.create(employeePkg.getEmployee());
		employee.setNumber(1);
		department.getMembers().add(employee);

		employeeOffice = (Office) employeeFactory.create(employeePkg
			.getOffice());
		employee.setOffice(employeeOffice);

		financeEmployee = (Employee) employeeFactory.create(employeePkg
			.getEmployee());
		financeEmployee.setDepartment(financeDepartment);

		financeManager = (Employee) employeeFactory.create(employeePkg
			.getEmployee());
		financeDepartment.setManager(financeManager);

		Office financeManagerOffice = (Office) employeeFactory
			.create(employeePkg.getOffice());
		financeManagerOffice.setNumberOfWindows(1);
		financeManagerOffice.setHasDoor(false);
		financeManager.setOffice(financeManagerOffice);

		student = (Student) employeeFactory.create(employeePkg.getStudent());
		student.setNumber(2);
		department.getMembers().add(student);

		studentOffice = (Office) employeeFactory
			.create(employeePkg.getOffice());
		student.setOffice(studentOffice);

		manager = (Employee) employeeFactory.create(employeePkg.getEmployee());
		department.setManager(manager);

		managerOffice = (Office) employeeFactory
			.create(employeePkg.getOffice());
		managerOffice.setNumberOfWindows(1);
		managerOffice.setHasDoor(false);
		manager.setOffice(managerOffice);

		executive = (Employee) employeeFactory
			.create(employeePkg.getEmployee());
		executiveDepartment.setManager(executive);

		executiveOffice = (Office) employeeFactory.create(employeePkg
			.getOffice());
		executiveOffice.setNumberOfWindows(1);
		executiveOffice.setHasDoor(true);
		executive.setOffice(executiveOffice);
		
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown()
		throws Exception {
		super.tearDown();
	}

	protected ElementTypeRegistry getFixture() {
		return fixture;
	}

	protected void setFixture(ElementTypeRegistry fixture) {
		this.fixture = fixture;
	}

	public void test_getAllTypesMatching_eObject_metamodel() {

		IElementType[] officeMatches = getFixture().getAllTypesMatching(
			employeeOffice);
		assertTrue(officeMatches.length == 1);
		assertTrue(officeMatches[0] == EmployeeType.OFFICE);
	}

	public void test_getAllTypesMatching_eObject_metamodelAndSpecializations() {

		IElementType[] managerMatches = getFixture().getAllTypesMatching(
			manager);
		assertEquals(3, managerMatches.length);
		List managerMatchList = Arrays.asList(managerMatches);
		assertTrue(managerMatchList.contains(EmployeeType.MANAGER));
		assertTrue(managerMatchList.contains(EmployeeType.TOP_SECRET));
		// The metamodel type should be last.
		assertEquals(EmployeeType.EMPLOYEE, managerMatches[2]);
	}

	public void test_getContainedTypes_metamodel() {

		IElementType[] officeMatches = getFixture().getContainedTypes(employee,
			EmployeePackage.eINSTANCE.getEmployee_Office());
		assertEquals(1, officeMatches.length);
		List officeMatchList = Arrays.asList(officeMatches);
		assertTrue(officeMatchList.contains(EmployeeType.OFFICE));
	}

	public void test_getContainedTypes_metamodelAndSpecializations_departmentMembers() {

		IElementType[] memberMatches = getFixture().getContainedTypes(
			department, EmployeePackage.eINSTANCE.getDepartment_Members());
		List memberMatchList = Arrays.asList(memberMatches);
		List expected = Arrays.asList(new Object[] {EmployeeType.EMPLOYEE,
			EmployeeType.STUDENT, EmployeeType.TOP_SECRET});

		assertEquals(expected.size(), memberMatches.length);
		assertTrue(memberMatchList.containsAll(expected));
	}

	public void test_getContainedTypes_metamodelAndSpecializations_departmentManager() {

		IElementType[] managerMatches = getFixture().getContainedTypes(
			department, EmployeePackage.eINSTANCE.getDepartment_Manager());
		List managerMatchList = Arrays.asList(managerMatches);
		List expected = Arrays.asList(new Object[] {EmployeeType.EMPLOYEE,
			EmployeeType.STUDENT, EmployeeType.MANAGER, EmployeeType.EXECUTIVE,
			EmployeeType.TOP_SECRET});

		assertEquals(expected.size(), managerMatches.length);
		assertTrue(managerMatchList.containsAll(expected));
	}

	public void test_getEditHelperAdvice_noAdvice() {

		IEditHelperAdvice[] advice = getFixture().getEditHelperAdvice(
			studentOffice);
		assertEquals(0, advice.length);
	}

	public void test_getEditHelperAdvice_eObject_directAdvice() {

		IEditHelperAdvice[] advice = getFixture().getEditHelperAdvice(
			financeEmployee);
		assertEquals(2, advice.length);

		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance and not inherited helper advice"); //$NON-NLS-1$
			}
		}
	}

	public void test_getEditHelperAdvice_eObject_indirectAdvice() {

		IEditHelperAdvice[] advice = getFixture().getEditHelperAdvice(
			financeManager);
		assertEquals(3, advice.length);

		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != ManagerEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance, manager and not inherited edit helper advice"); //$NON-NLS-1$
			}
		}
	}

	public void test_getEditHelperAdvice_elementType_directMatch() {

		IEditHelperAdvice[] advice = getFixture().getEditHelperAdvice(
			EmployeeType.EMPLOYEE);
		assertEquals(2, advice.length);
		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance and notInherited edit helper advice"); //$NON-NLS-1$
			}
		}
	}

	public void test_getEditHelperAdvice_elementType_inheritedMatches() {

		IEditHelperAdvice[] advice = getFixture().getEditHelperAdvice(
			EmployeeType.EXECUTIVE);
		assertEquals(4, advice.length);
		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != ManagerEditHelperAdvice.class
				&& advice[i].getClass() != ExecutiveEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance, manager, executive and not-inherited edit helper advice"); //$NON-NLS-1$
			}
		}
	}

	public void test_getEditHelperAdvice_elementType_noInheritedMatches() {

		IEditHelperAdvice[] advice = getFixture().getEditHelperAdvice(
			EmployeeType.STUDENT);
		assertEquals(1, advice.length);
		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class) {
				fail("expected finance edit helper advice"); //$NON-NLS-1$
			}
		}
	}

	public void test_getElementTypeFactory_none() {

		IElementTypeFactory factory = getFixture().getElementTypeFactory(
			"noName"); //$NON-NLS-1$
		assertNull(factory);
	}

	public void test_getElementTypeFactory_default() {
		IElementTypeFactory factory = getFixture().getElementTypeFactory(
			"org.eclipse.gmf.runtime.emf.type.core.IElementType"); //$NON-NLS-1$
		assertNotNull(factory);
		assertEquals(DefaultElementTypeFactory.class, factory.getClass());
	}

	public void test_getElementTypeFactory_custom() {
		IElementTypeFactory factory = getFixture().getElementTypeFactory(
			"org.eclipse.gmf.tests.runtime.emf.type.core.internal.ISecurityCleared"); //$NON-NLS-1$
		assertNotNull(factory);
		assertEquals(SecurityClearedElementTypeFactory.class, factory
			.getClass());
	}

	public void test_getElementType_eClass() {
		
		IElementType metamodelType = getFixture().getElementType(
			employeePkg.getDepartment());
		
		assertNotNull(metamodelType);
		assertEquals(EmployeeType.DEPARTMENT, metamodelType);
	}

	public void test_getElementType_eObject() {
		
		IElementType metamodelType = getFixture().getElementType(
			financeManager);
		
		assertNotNull(metamodelType);
		assertEquals(EmployeeType.EMPLOYEE, metamodelType);
	}
	
	public void test_getElementType_eObject_eClass() {

		MetamodelType eClassType = new MetamodelType(
			"org.eclipse.gmf.tests.runtime.emf.type.core.eclass", null, null, //$NON-NLS-1$
			EcorePackage.eINSTANCE.getEClass(), null);

		getFixture().register(eClassType);

		EObject myEClassInstance = EcoreFactory.eINSTANCE.createEClass();

		IElementType metamodelType = getFixture().getElementType(
			myEClassInstance);
		assertNotNull(metamodelType);
		assertSame(eClassType, metamodelType);
	}
	

	public void test_getElementType_overridesEditHelper() {

		IElementType elementType = getFixture().getElementType(
				EmployeeType.TOP_SECRET);
		assertNotNull(elementType);
		assertEquals(EmployeeType.TOP_SECRET, elementType);

		assertTrue(elementType.getEditHelper() instanceof SecurityClearedElementTypeFactory.SecurityClearedEditHelper);
	}
	

	public void test_getElementType_metamodelType() {
		IElementType metamodelType = getFixture().getElementType(EmployeeType.STUDENT);
		assertNotNull(metamodelType);
		assertEquals(EmployeeType.STUDENT, metamodelType);
	}
	

	public void test_getElementType_specializationType() {
		IElementType specializationType = getFixture()
				.getElementType(EmployeeType.MANAGER);
		assertNotNull(specializationType);
		assertEquals(EmployeeType.MANAGER, specializationType);
	}

	public void test_getType_metamodel() {
		IElementType studentType = getFixture().getType(
			EmployeeType.STUDENT.getId());
		assertNotNull(studentType);
		assertEquals(EmployeeType.STUDENT.getId(), studentType.getId());
	}

	public void test_getType_specialization() {
		IElementType managerType = getFixture().getType(
			EmployeeType.MANAGER.getId());
		assertNotNull(managerType);
		assertEquals(EmployeeType.MANAGER.getId(), managerType.getId());
	}

	public void test_duplicateId_notRegistered() {
		IElementType employeeType = getFixture().getType(
			"org.eclipse.gmf.tests.runtime.emf.type.core.employee"); //$NON-NLS-1$
		assertFalse(employeeType.getDisplayName().equals("DuplicateEmployee")); //$NON-NLS-1$
	}

	public void test_duplicateEClass_notRegistered() {
		IElementType employeeType = getFixture().getType(
			"org.eclipse.gmf.tests.runtime.emf.type.ui.employee2"); //$NON-NLS-1$
		assertNull(employeeType);
	}

	public void test_multipleMetatmodelTypes_notRegistered() {
		IElementType employeeType = getFixture().getType(
			"org.eclipse.gmf.tests.runtime.emf.type.ui.multipleMetamodelTypes"); //$NON-NLS-1$
		assertNull(employeeType);
	}

	public void test_noSuchType_notRegistered() {
		IElementType employeeType = getFixture().getType(
			"org.eclipse.gmf.tests.runtime.emf.type.ui.SpecializesNoSuchType"); //$NON-NLS-1$
		assertNull(employeeType);
	}

	public void test_invalidMetatmodel_notRegistered() {
		IElementType employeeType = getFixture().getType(
			"org.eclipse.gmf.tests.runtime.emf.type.ui.noMetamodel"); //$NON-NLS-1$
		assertNull(employeeType);
	}
	
	public void test_register_specializationType() {

		IEditHelperAdvice specialAdvice = new MySpecializationAdvice();
		
		String id = "dynamic.specialization.type"; //$NON-NLS-1$
		final ISpecializationType dynamicSpecializationType = new SpecializationType(id, null, id,
			new IElementType[] {EmployeeType.EMPLOYEE}, null, null, specialAdvice);
		
		final boolean[] listenerNotified = new boolean[] {false};
		IElementTypeRegistryListener listener = new IElementTypeRegistryListener() {

			public void elementTypeAdded(
					ElementTypeAddedEvent elementTypeAddedEvent) {
				listenerNotified[0] = true;
				assertEquals(dynamicSpecializationType.getId(), elementTypeAddedEvent
					.getElementTypeId());
			}
		};
		
		ElementTypeRegistry.getInstance().addElementTypeRegistryListener(listener);
		 
		boolean result = ElementTypeRegistry.getInstance().register(dynamicSpecializationType);
		
		// Check that the element type was registered
		assertTrue(result);
		assertTrue(listenerNotified[0]);
		assertSame(dynamicSpecializationType, getFixture().getType(id));
		
		// Check that the advice can be retrieved
		IEditHelperAdvice[] advice = getFixture().getEditHelperAdvice(
			dynamicSpecializationType);
		assertTrue(Arrays.asList(advice).contains(specialAdvice));
		
		ElementTypeRegistry.getInstance().removeElementTypeRegistryListener(listener);
	}
	

	
	public void test_register_metamodelType() {
		
		String id = "dynamic.metamodel.type"; //$NON-NLS-1$
		final IMetamodelType dynamicMetamodelType = new MetamodelType(id, null, id, EmployeePackage.eINSTANCE.getLocation(), null);
		
		final boolean[] listenerNotified = new boolean[] {false};
		IElementTypeRegistryListener listener = new IElementTypeRegistryListener() {

			public void elementTypeAdded(
					ElementTypeAddedEvent elementTypeAddedEvent) {
				listenerNotified[0] = true;
				assertEquals(dynamicMetamodelType.getId(), elementTypeAddedEvent
					.getElementTypeId());
			}
		};
		
		ElementTypeRegistry.getInstance().addElementTypeRegistryListener(listener);
		
		boolean result = ElementTypeRegistry.getInstance().register(dynamicMetamodelType);
		
		assertTrue(result);
		assertTrue(listenerNotified[0]);
		assertSame(dynamicMetamodelType, ElementTypeRegistry.getInstance().getType(id));
		
		ElementTypeRegistry.getInstance().removeElementTypeRegistryListener(listener);
	}
	
	public void test_nullElementType_specialization() {
		IElementType nullSpecialization = getFixture().getType(
			"org.eclipse.gmf.tests.runtime.emf.type.core.nullSpecialization"); //$NON-NLS-1$
		assertNotNull(nullSpecialization);
		
		department.setManager(null);
		assertNull(department.getManager());
		
		CreateElementRequest createRequest = new CreateElementRequest(
			department, nullSpecialization);
		
		createRequest.setParameter("MANAGER", manager); //$NON-NLS-1$
		
		IElementType elementType = ElementTypeRegistry.getInstance()
			.getElementType(createRequest.getEditHelperContext());
		
		ICommand command = elementType.getEditCommand(createRequest);
		
		assertNotNull(command);
		assertTrue(command.isExecutable());
		
		command.execute(new NullProgressMonitor());
		assertSame(manager, department.getManager());
		
		assertNull(command.getCommandResult().getReturnValue());
	}
}
