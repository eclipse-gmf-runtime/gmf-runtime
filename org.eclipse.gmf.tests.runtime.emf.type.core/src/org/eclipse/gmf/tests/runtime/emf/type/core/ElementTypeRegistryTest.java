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

import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextManager;
import org.eclipse.gmf.runtime.emf.type.core.EditHelperContext;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeAddedEvent;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IEditHelperContext;
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
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.DefaultMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.HighSchoolStudent;
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
	extends AbstractEMFTypeTest {
	
	private class MySpecializationAdvice extends AbstractEditHelperAdvice {
		public MySpecializationAdvice() {
			super();
		}
		protected ICommand getBeforeCreateCommand(CreateElementRequest request) {
			return super.getBeforeCreateCommand(request);
		}
	}

	private ElementTypeRegistry fixture = null;

    private IClientContext clientContext;
    
    private IClientContext unboundClientContext;

	// Model elements
	private Department department;

	private Department executiveDepartment;

	private Department financeDepartment;

	private Employee employee;

	private Employee financeEmployee;

	private Employee financeManager;

	private Student student;
	
	private HighSchoolStudent highSchoolStudent;

	private Office employeeOffice;

	private Office studentOffice;

	private Employee manager;

	private Office managerOffice;

	private Employee executive;

	private Office executiveOffice;
	
	// Model elements in resource with context
	private Department cDepartment;

	private Department cExecutiveDepartment;

	private Department cFinanceDepartment;

	private Employee cEmployee;

	private Employee cFinanceEmployee;

	private Employee cFinanceManager;

	private Student cStudent;

	private Office cEmployeeOffice;

	private Office cStudentOffice;

	private Employee cManager;

	private Office cManagerOffice;

	private Employee cExecutive;

	private Office cExecutiveOffice;
	

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

    protected void doModelSetup(Resource resource) {
		setFixture(ElementTypeRegistry.getInstance());

		department = (Department) getEmployeeFactory().create(getEmployeePackage()
			.getDepartment());
		department.setName("Department"); //$NON-NLS-1$
		resource.getContents().add(department);

		executiveDepartment = (Department) getEmployeeFactory().create(getEmployeePackage()
			.getDepartment());
		executiveDepartment.setName("ExecutiveDepartment"); //$NON-NLS-1$
		resource.getContents().add(executiveDepartment);

		financeDepartment = (Department) getEmployeeFactory().create(getEmployeePackage()
			.getDepartment());
		financeDepartment.setName("Finance"); //$NON-NLS-1$
		resource.getContents().add(financeDepartment);

		employee = (Employee) getEmployeeFactory().create(getEmployeePackage().getEmployee());
		employee.setNumber(1);
		department.getMembers().add(employee);

		employeeOffice = (Office) getEmployeeFactory().create(getEmployeePackage()
			.getOffice());
		employee.setOffice(employeeOffice);

		financeEmployee = (Employee) getEmployeeFactory().create(getEmployeePackage()
			.getEmployee());
		financeEmployee.setDepartment(financeDepartment);

		financeManager = (Employee) getEmployeeFactory().create(getEmployeePackage()
			.getEmployee());
		financeDepartment.setManager(financeManager);

		Office financeManagerOffice = (Office) getEmployeeFactory()
			.create(getEmployeePackage().getOffice());
		financeManagerOffice.setNumberOfWindows(1);
		financeManagerOffice.setHasDoor(false);
		financeManager.setOffice(financeManagerOffice);

		student = (Student) getEmployeeFactory().create(getEmployeePackage().getStudent());
		student.setNumber(2);
		department.getMembers().add(student);
		
		
		studentOffice = (Office) getEmployeeFactory()
			.create(getEmployeePackage().getOffice());
		student.setOffice(studentOffice);

		manager = (Employee) getEmployeeFactory().create(getEmployeePackage().getEmployee());
		department.setManager(manager);

		managerOffice = (Office) getEmployeeFactory()
			.create(getEmployeePackage().getOffice());
		managerOffice.setNumberOfWindows(1);
		managerOffice.setHasDoor(false);
		manager.setOffice(managerOffice);

		executive = (Employee) getEmployeeFactory()
			.create(getEmployeePackage().getEmployee());
		executiveDepartment.setManager(executive);

		executiveOffice = (Office) getEmployeeFactory().create(getEmployeePackage()
			.getOffice());
		executiveOffice.setNumberOfWindows(1);
		executiveOffice.setHasDoor(true);
		executive.setOffice(executiveOffice);
		
		highSchoolStudent = (HighSchoolStudent) getEmployeeFactory().create(
				getEmployeePackage().getHighSchoolStudent());

	}
    
    protected void doModelSetupWithContext(Resource resource) {
		setFixture(ElementTypeRegistry.getInstance());

		cDepartment = (Department) getEmployeeFactory().create(getEmployeePackage()
			.getDepartment());
		cDepartment.setName("DepartmentWithContext"); //$NON-NLS-1$
		resource.getContents().add(cDepartment);

		cExecutiveDepartment = (Department) getEmployeeFactory().create(getEmployeePackage()
			.getDepartment());
		cExecutiveDepartment.setName("ExecutiveDepartmentWithContext"); //$NON-NLS-1$
		resource.getContents().add(cExecutiveDepartment);

		cFinanceDepartment = (Department) getEmployeeFactory().create(getEmployeePackage()
			.getDepartment());
		cFinanceDepartment.setName("FinanceWithContext"); //$NON-NLS-1$
		resource.getContents().add(cFinanceDepartment);

		cEmployee = (Employee) getEmployeeFactory().create(getEmployeePackage().getEmployee());
		cEmployee.setNumber(1);
		cDepartment.getMembers().add(cEmployee);

		cEmployeeOffice = (Office) getEmployeeFactory().create(getEmployeePackage()
			.getOffice());
		cEmployee.setOffice(cEmployeeOffice);

		cFinanceEmployee = (Employee) getEmployeeFactory().create(getEmployeePackage()
			.getEmployee());
		cFinanceEmployee.setDepartment(cFinanceDepartment);

		cFinanceManager = (Employee) getEmployeeFactory().create(getEmployeePackage()
			.getEmployee());
		cFinanceDepartment.setManager(cFinanceManager);

		Office financeManagerOffice = (Office) getEmployeeFactory()
			.create(getEmployeePackage().getOffice());
		financeManagerOffice.setNumberOfWindows(1);
		financeManagerOffice.setHasDoor(false);
		cFinanceManager.setOffice(financeManagerOffice);

		cStudent = (Student) getEmployeeFactory().create(getEmployeePackage().getStudent());
		cStudent.setNumber(2);
		cDepartment.getMembers().add(cStudent);

		cStudentOffice = (Office) getEmployeeFactory()
			.create(getEmployeePackage().getOffice());
		cStudent.setOffice(cStudentOffice);

		cManager = (Employee) getEmployeeFactory().create(getEmployeePackage().getEmployee());
		cDepartment.setManager(cManager);

		cManagerOffice = (Office) getEmployeeFactory()
			.create(getEmployeePackage().getOffice());
		cManagerOffice.setNumberOfWindows(1);
		cManagerOffice.setHasDoor(false);
		cManager.setOffice(cManagerOffice);

		cExecutive = (Employee) getEmployeeFactory()
			.create(getEmployeePackage().getEmployee());
		cExecutiveDepartment.setManager(cExecutive);

		cExecutiveOffice = (Office) getEmployeeFactory().create(getEmployeePackage()
			.getOffice());
		cExecutiveOffice.setNumberOfWindows(1);
		cExecutiveOffice.setHasDoor(true);
		cExecutive.setOffice(cExecutiveOffice);
		
	}
    
    protected ElementTypeRegistry getFixture() {
		return fixture;
	}

	protected void setFixture(ElementTypeRegistry fixture) {
		this.fixture = fixture;
	}
	
	protected IClientContext getClientContext() {
		if (clientContext == null) {
			clientContext = ClientContextManager
					.getInstance()
					.getClientContext(
							"org.eclipse.gmf.tests.runtime.emf.type.core.ClientContext1"); //$NON-NLS-1$
		}
		return clientContext;
	}
	
	protected IClientContext getUnboundClientContext() {
		if (unboundClientContext == null) {
			unboundClientContext = ClientContextManager
					.getInstance()
					.getClientContext(
							"org.eclipse.gmf.tests.runtime.emf.type.core.UnboundClientContext"); //$NON-NLS-1$
		}
		return unboundClientContext;
	}

	
	/**
	 * Verifies that the #getSpecializationsOf API returns the correct
	 * specializations.
	 */
	public void test_getSpecializationsOf_151097() {

		ISpecializationType[] specializations = ElementTypeRegistry
				.getInstance().getSpecializationsOf(
						"org.eclipse.gmf.tests.runtime.emf.type.core.employee"); // $NON_NLS_1$

		assertEquals(3, specializations.length);
		for (int i = 0; i < specializations.length; i++) {
			if (specializations[i].getId().equals("org.eclipse.gmf.tests.runtime.emf.type.core.manager") //$NON-NLS-1$
				&& specializations[i].getClass().equals("org.eclipse.gmf.tests.runtime.emf.type.core.topSecret") //$NON-NLS-1$
				&& specializations[i].getClass().equals("org.eclipse.gmf.tests.runtime.emf.type.core.executive")) { //$NON-NLS-1$
				fail("expected manager, top-secret and executive specializations"); //$NON-NLS-1$
			}
		}
	}

	public void test_getAllTypesMatching_eObject_metamodel() {

		IElementType[] officeMatches = getFixture().getAllTypesMatching(
			employeeOffice);
		assertTrue(officeMatches.length == 1);
		assertTrue(officeMatches[0] == EmployeeType.OFFICE);
	}
	
	public void test_getAllTypesMatching_eObject_metamodel_withContext() {

		// context inferred
		IElementType[] officeMatches = getFixture().getAllTypesMatching(
			cEmployeeOffice);
		assertTrue(officeMatches.length == 1);
		assertTrue(officeMatches[0] == EmployeeType.CONTEXT_OFFICE);
		
		// context explicit
		officeMatches = getFixture().getAllTypesMatching(
				cEmployeeOffice, getClientContext());
		assertTrue(officeMatches.length == 1);
		assertTrue(officeMatches[0] == EmployeeType.CONTEXT_OFFICE);
	}
	
	public void test_getAllTypesMatching_eObject_metamodel_unboundContext() {

		IElementType[] officeMatches = getFixture().getAllTypesMatching(
				cEmployeeOffice, getUnboundClientContext());
		assertTrue(officeMatches.length == 1);
		assertTrue(officeMatches[0] == DefaultMetamodelType.getInstance());
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

	public void test_getAllTypesMatching_eObject_metamodelAndSpecializations_withContext() {

		// context inferred
		IElementType[] managerMatches = getFixture().getAllTypesMatching(
			cManager);
		assertEquals(3, managerMatches.length);
		List managerMatchList = Arrays.asList(managerMatches);
		assertTrue(managerMatchList.contains(EmployeeType.CONTEXT_MANAGER));
		assertTrue(managerMatchList.contains(EmployeeType.CONTEXT_TOP_SECRET));
		// The metamodel type should be last.
		assertEquals(EmployeeType.CONTEXT_EMPLOYEE, managerMatches[2]);
		
		// context explicit
		managerMatches = getFixture().getAllTypesMatching(
			cManager, getClientContext());
		assertEquals(3, managerMatches.length);
		managerMatchList = Arrays.asList(managerMatches);
		assertTrue(managerMatchList.contains(EmployeeType.CONTEXT_MANAGER));
		assertTrue(managerMatchList.contains(EmployeeType.CONTEXT_TOP_SECRET));
		// The metamodel type should be last.
		assertEquals(EmployeeType.CONTEXT_EMPLOYEE, managerMatches[2]);
	}

	public void test_getAllTypesMatching_eObject_metamodelAndSpecializations_unboundContext() {

		IElementType[] managerMatches = getFixture().getAllTypesMatching(
			cManager, getUnboundClientContext());
		assertEquals(1, managerMatches.length);
		assertTrue(managerMatches[0] == DefaultMetamodelType.getInstance());
	}

	public void test_getContainedTypes_metamodel() {

		IElementType[] officeMatches = getFixture().getContainedTypes(employee,
			EmployeePackage.eINSTANCE.getEmployee_Office());
		assertEquals(1, officeMatches.length);
		List officeMatchList = Arrays.asList(officeMatches);
		assertTrue(officeMatchList.contains(EmployeeType.OFFICE));
	}
	
	public void test_getContainedTypes_metamodel_withContext() {

		// context inferred
		IElementType[] officeMatches = getFixture().getContainedTypes(cEmployee,
			EmployeePackage.eINSTANCE.getEmployee_Office());
		assertEquals(1, officeMatches.length);
		List officeMatchList = Arrays.asList(officeMatches);
		assertTrue(officeMatchList.contains(EmployeeType.CONTEXT_OFFICE));
		
		// context explicit
		officeMatches = getFixture().getContainedTypes(cEmployee,
				EmployeePackage.eINSTANCE.getEmployee_Office(), getClientContext());
		assertEquals(1, officeMatches.length);
		officeMatchList = Arrays.asList(officeMatches);
		assertTrue(officeMatchList.contains(EmployeeType.CONTEXT_OFFICE));
	}
	
	public void test_getContainedTypes_metamodel_unboundContext() {

		IElementType[] officeMatches = getFixture().getContainedTypes(cEmployee,
			EmployeePackage.eINSTANCE.getEmployee_Office(), getUnboundClientContext());
		assertEquals(0, officeMatches.length);
	}

	public void test_getContainedTypes_metamodelAndSpecializations_departmentMembers() {

		IElementType[] memberMatches = getFixture().getContainedTypes(
			department, EmployeePackage.eINSTANCE.getDepartment_Members());
		List memberMatchList = Arrays.asList(memberMatches);
		List expected = Arrays.asList(new Object[] {EmployeeType.EMPLOYEE,
			EmployeeType.STUDENT, EmployeeType.HIGH_SCHOOL_STUDENT, EmployeeType.TOP_SECRET});

		assertEquals(expected.size(), memberMatches.length);
		assertTrue(memberMatchList.containsAll(expected));
	}
	
	public void test_getContainedTypes_metamodelAndSpecializations_departmentMembers_withContext() {

		// context inferred
		IElementType[] memberMatches = getFixture().getContainedTypes(
			cDepartment, EmployeePackage.eINSTANCE.getDepartment_Members());
		List memberMatchList = Arrays.asList(memberMatches);
		List expected = Arrays.asList(new Object[] {EmployeeType.CONTEXT_EMPLOYEE,
			EmployeeType.CONTEXT_STUDENT, EmployeeType.CONTEXT_TOP_SECRET});

		assertEquals(expected.size(), memberMatches.length);
		assertTrue(memberMatchList.containsAll(expected));
		
		// context explicit
		memberMatches = getFixture().getContainedTypes(
			cDepartment, EmployeePackage.eINSTANCE.getDepartment_Members(), getClientContext());
		memberMatchList = Arrays.asList(memberMatches);
		expected = Arrays.asList(new Object[] {EmployeeType.CONTEXT_EMPLOYEE,
			EmployeeType.CONTEXT_STUDENT, EmployeeType.CONTEXT_TOP_SECRET});

		assertEquals(expected.size(), memberMatches.length);
		assertTrue(memberMatchList.containsAll(expected));
	}
	
	public void test_getContainedTypes_metamodelAndSpecializations_departmentMembers_unboundContext() {

		IElementType[] memberMatches = getFixture().getContainedTypes(
			cDepartment, EmployeePackage.eINSTANCE.getDepartment_Members(), getUnboundClientContext());
		
		assertEquals(0, memberMatches.length);
	}

	public void test_getContainedTypes_metamodelAndSpecializations_departmentManager() {

		IElementType[] managerMatches = getFixture().getContainedTypes(
			department, EmployeePackage.eINSTANCE.getDepartment_Manager());
		List managerMatchList = Arrays.asList(managerMatches);
		List expected = Arrays.asList(new Object[] {EmployeeType.EMPLOYEE,
			EmployeeType.STUDENT, EmployeeType.HIGH_SCHOOL_STUDENT, EmployeeType.MANAGER, EmployeeType.EXECUTIVE,
			EmployeeType.TOP_SECRET});

		assertEquals(expected.size(), managerMatches.length);
		assertTrue(managerMatchList.containsAll(expected));
	}
	
	public void test_getContainedTypes_metamodelAndSpecializations_departmentManager_withContext() {

		// context inferred
		IElementType[] managerMatches = getFixture().getContainedTypes(
			cDepartment, EmployeePackage.eINSTANCE.getDepartment_Manager());
		List managerMatchList = Arrays.asList(managerMatches);
		List expected = Arrays.asList(new Object[] {EmployeeType.CONTEXT_EMPLOYEE,
			EmployeeType.CONTEXT_STUDENT, EmployeeType.CONTEXT_MANAGER, EmployeeType.CONTEXT_EXECUTIVE,
			EmployeeType.CONTEXT_TOP_SECRET});

		assertEquals(expected.size(), managerMatches.length);
		assertTrue(managerMatchList.containsAll(expected));
		
		// context explicit
		managerMatches = getFixture().getContainedTypes(
			cDepartment, EmployeePackage.eINSTANCE.getDepartment_Manager(), getClientContext());
		managerMatchList = Arrays.asList(managerMatches);
		expected = Arrays.asList(new Object[] {EmployeeType.CONTEXT_EMPLOYEE,
			EmployeeType.CONTEXT_STUDENT, EmployeeType.CONTEXT_MANAGER, EmployeeType.CONTEXT_EXECUTIVE,
			EmployeeType.CONTEXT_TOP_SECRET});

		assertEquals(expected.size(), managerMatches.length);
		assertTrue(managerMatchList.containsAll(expected));	
	}
	
	public void test_getContainedTypes_metamodelAndSpecializations_departmentManager_unboundContext() {

		IElementType[] managerMatches = getFixture().getContainedTypes(
			cDepartment, EmployeePackage.eINSTANCE.getDepartment_Manager(), getUnboundClientContext());

		assertEquals(0, managerMatches.length);
	}

	public void test_getEditHelperAdvice_noAdvice() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(studentOffice);
		assertEquals(0, advice.length);
	}
	
	public void test_getEditHelperAdvice_noAdvice_withContext() {

		// context inferred
		IEditHelperAdvice[] advice = getNonWildcardAdvice(cStudentOffice);
		assertEquals(0, advice.length);
		
		// context explicit
		advice = getNonWildcardAdvice(cStudentOffice, getClientContext());
		assertEquals(0, advice.length);
	}
	
	public void test_getEditHelperAdvice_noAdvice_unboundContext() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(cStudentOffice, getUnboundClientContext());
		assertEquals(0, advice.length);
	}

	public void test_getEditHelperAdvice_eObject_directAdvice() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(financeEmployee);
		assertEquals(2, advice.length);

		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance and not inherited helper advice"); //$NON-NLS-1$
			}
		}
	}
	
	public void test_getEditHelperAdvice_eObject_directAdvice_withContext() {

		// context inferred
		IEditHelperAdvice[] advice = getNonWildcardAdvice(cFinanceEmployee);
		assertEquals(2, advice.length);

		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance and not inherited helper advice"); //$NON-NLS-1$
			}
		}
		
		// context explicit
		advice = getNonWildcardAdvice(cFinanceEmployee, getClientContext());
		assertEquals(2, advice.length);

		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance and not inherited helper advice"); //$NON-NLS-1$
			}
		}
	}
	
	public void test_getEditHelperAdvice_eObject_directAdvice_unboundContext() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(cFinanceEmployee, getUnboundClientContext());
		assertEquals(0, advice.length);
	}

	public void test_getEditHelperAdvice_eObject_indirectAdvice() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(financeManager);
		assertEquals(3, advice.length);

		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != ManagerEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance, manager and not inherited edit helper advice"); //$NON-NLS-1$
			}
		}
	}
	
	public void test_getEditHelperAdvice_eObject_indirectAdvice_withContext() {

		// context inferred
		IEditHelperAdvice[] advice = getNonWildcardAdvice(cFinanceManager);
		assertEquals(3, advice.length);

		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != ManagerEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance, manager and not inherited edit helper advice"); //$NON-NLS-1$
			}
		}
		
		// context explicit
		advice = getNonWildcardAdvice(cFinanceManager, getClientContext());
		assertEquals(3, advice.length);

		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != ManagerEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance, manager and not inherited edit helper advice"); //$NON-NLS-1$
			}
		}
	}
	
	public void test_getEditHelperAdvice_eObject_indirectAdvice_unboundContext() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(cFinanceManager, getUnboundClientContext());
		assertEquals(0, advice.length);
	}

	public void test_getEditHelperAdvice_elementType_directMatch() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(EmployeeType.EMPLOYEE);
		assertEquals(2, advice.length);
		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance and notInherited edit helper advice"); //$NON-NLS-1$
			}
		}
	}
	
	public void test_getEditHelperAdvice_elementType_directMatch_withContext() {

		// context inferred
		IEditHelperAdvice[] advice = getNonWildcardAdvice(EmployeeType.CONTEXT_EMPLOYEE);
		assertEquals(2, advice.length);
		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance and notInherited edit helper advice"); //$NON-NLS-1$
			}
		}
		
		// context explicit
		advice = getNonWildcardAdvice(EmployeeType.CONTEXT_EMPLOYEE, getClientContext());
		assertEquals(2, advice.length);
		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance and notInherited edit helper advice"); //$NON-NLS-1$
			}
		}
	}
	
	public void test_getEditHelperAdvice_elementType_directMatch_unboundContext() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(EmployeeType.CONTEXT_EMPLOYEE, getUnboundClientContext());
		assertEquals(0, advice.length);
	}

	public void test_getEditHelperAdvice_elementType_inheritedMatches() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(EmployeeType.EXECUTIVE);
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
	
	public void test_getEditHelperAdvice_elementType_inheritedMatches_withContext() {

		// context inferred
		IEditHelperAdvice[] advice = getNonWildcardAdvice(EmployeeType.CONTEXT_EXECUTIVE);
		assertEquals(4, advice.length);
		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != ManagerEditHelperAdvice.class
				&& advice[i].getClass() != ExecutiveEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance, manager, executive and not-inherited edit helper advice"); //$NON-NLS-1$
			}
		}
		
		// context explicit
		advice = getNonWildcardAdvice(EmployeeType.CONTEXT_EXECUTIVE, getClientContext());
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
	
	public void test_getEditHelperAdvice_elementType_inheritedMatches_unboundContext() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(EmployeeType.CONTEXT_EXECUTIVE, getUnboundClientContext());
		assertEquals(0, advice.length);
	}

	public void test_getEditHelperAdvice_elementType_noInheritedMatches() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(EmployeeType.STUDENT);
		assertEquals(1, advice.length);
		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class) {
				fail("expected finance edit helper advice"); //$NON-NLS-1$
			}
		}
	}
	
	public void test_getEditHelperAdvice_elementType_noInheritedMatches_withContext() {

		// context inferred
		IEditHelperAdvice[] advice = getNonWildcardAdvice(EmployeeType.CONTEXT_STUDENT);
		assertEquals(1, advice.length);
		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class) {
				fail("expected finance edit helper advice"); //$NON-NLS-1$
			}
		}
		
		// context explicit
		advice = getNonWildcardAdvice(EmployeeType.CONTEXT_STUDENT, getClientContext());
		assertEquals(1, advice.length);
		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class) {
				fail("expected finance edit helper advice"); //$NON-NLS-1$
			}
		}
	}
	
	public void test_getEditHelperAdvice_elementType_noInheritedMatches_unboundContext() {

		IEditHelperAdvice[] advice = getNonWildcardAdvice(EmployeeType.CONTEXT_STUDENT, getUnboundClientContext());
		assertEquals(0, advice.length);
	}


	public void test_getEditHelperAdvice_editHelperContext_withEObject() {
		
		IEditHelperContext context = new EditHelperContext(cFinanceManager,
				getClientContext());
		
		IEditHelperAdvice[] advice = getNonWildcardAdvice(context);
		assertEquals(3, advice.length);

		for (int i = 0; i < advice.length; i++) {
			if (advice[i].getClass() != FinanceEditHelperAdvice.class
				&& advice[i].getClass() != ManagerEditHelperAdvice.class
				&& advice[i].getClass() != NotInheritedEditHelperAdvice.class) {
				fail("expected finance, manager and not inherited edit helper advice"); //$NON-NLS-1$
			}
		}
	}
	
	public void test_getEditHelperAdvice_editHelperContext_withElementType() {
		IEditHelperContext context = new EditHelperContext(
				EmployeeType.CONTEXT_EXECUTIVE, getClientContext());
		
		IEditHelperAdvice[] advice = getNonWildcardAdvice(context);
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
	
	public void test_getEditHelperAdvice_editHelperContext_noClientContext() {
		IEditHelperContext context = new EditHelperContext(
				EmployeeType.CONTEXT_EXECUTIVE, null);
		
		IEditHelperAdvice[] advice = getNonWildcardAdvice(context);
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
			getEmployeePackage().getDepartment());
		
		assertNotNull(metamodelType);
		assertEquals(EmployeeType.DEPARTMENT, metamodelType);
	}
	
	public void test_getElementType_eClass_withContext() {
		
		// context explicit
		IElementType metamodelType = getFixture().getElementType(
			getEmployeePackage().getDepartment(), getClientContext());
		
		assertNotNull(metamodelType);
		assertEquals(EmployeeType.CONTEXT_DEPARTMENT, metamodelType);
	}
	
	public void test_getElementType_eClass_unboundContext() {
		
		IElementType metamodelType = getFixture().getElementType(
			getEmployeePackage().getDepartment(), getUnboundClientContext());
		
		assertSame(DefaultMetamodelType.getInstance(), metamodelType);
	}

	public void test_getElementType_eObject() {
		
		IElementType metamodelType = getFixture().getElementType(
			financeManager);
		
		assertNotNull(metamodelType);
		assertEquals(EmployeeType.EMPLOYEE, metamodelType);
	}
	
	public void test_getElementType_eObject_withContext() {
		
		// context inferred
		IElementType metamodelType = getFixture().getElementType(
			cFinanceManager);
		
		assertNotNull(metamodelType);
		assertEquals(EmployeeType.CONTEXT_EMPLOYEE, metamodelType);
		
		// context explicit
		metamodelType = getFixture().getElementType(
			cFinanceManager, getClientContext());
		
		assertNotNull(metamodelType);
		assertEquals(EmployeeType.CONTEXT_EMPLOYEE, metamodelType);
	}
	
	public void test_getElementType_eObject_unboundContext() {
		
		IElementType metamodelType = getFixture().getElementType(
			cFinanceManager, getUnboundClientContext());
		
		assertSame(DefaultMetamodelType.getInstance(), metamodelType);
	}
	
	public void test_getElementType_eObject_eClass() {

		MetamodelType eClassType = new MetamodelType(
				"dynamic.org.eclipse.gmf.tests.runtime.emf.type.core.eclass", null, null, //$NON-NLS-1$
				EcorePackage.eINSTANCE.getEClass(), null);

		// EClass type conflicts with the one in the ECore example editor
		getClientContext().bindId(
				"dynamic.org.eclipse.gmf.tests.runtime.emf.type.core.eclass"); //$NON-NLS-1$

		boolean wasRegistered = getFixture().register(eClassType);
		EObject myEClassInstance = EcoreFactory.eINSTANCE.createEClass();

		IElementType metamodelType = getFixture().getElementType(
				myEClassInstance, getClientContext());
		assertNotNull(metamodelType);

		if (wasRegistered) {
			assertSame(eClassType, metamodelType);
		}
	}
	

	public void test_getElementType_overridesEditHelper() {

		IElementType elementType = getFixture().getElementType(
				EmployeeType.TOP_SECRET);
		assertNotNull(elementType);
		assertEquals(EmployeeType.TOP_SECRET, elementType);

		assertTrue(elementType.getEditHelper() instanceof SecurityClearedElementTypeFactory.SecurityClearedEditHelper);
	}
	
	public void test_getElementType_overridesEditHelper_withContext() {

		IElementType elementType = getFixture().getElementType(
				EmployeeType.CONTEXT_TOP_SECRET);
		assertNotNull(elementType);
		assertEquals(EmployeeType.CONTEXT_TOP_SECRET, elementType);

		assertTrue(elementType.getEditHelper() instanceof SecurityClearedElementTypeFactory.SecurityClearedEditHelper);
	}
	

	public void test_getElementType_metamodelType() {
		IElementType metamodelType = getFixture().getElementType(EmployeeType.STUDENT);
		assertNotNull(metamodelType);
		assertEquals(EmployeeType.STUDENT, metamodelType);
	}
	
	public void test_getElementType_metamodelType_withContext() {
		IElementType metamodelType = getFixture().getElementType(EmployeeType.CONTEXT_STUDENT);
		assertNotNull(metamodelType);
		assertEquals(EmployeeType.CONTEXT_STUDENT, metamodelType);
	}

	public void test_getElementType_specializationType() {
		IElementType specializationType = getFixture()
				.getElementType(EmployeeType.MANAGER);
		assertNotNull(specializationType);
		assertEquals(EmployeeType.MANAGER, specializationType);
	}

	public void test_getElementType_specializationType_withContext() {
		IElementType specializationType = getFixture()
				.getElementType(EmployeeType.CONTEXT_MANAGER);
		assertNotNull(specializationType);
		assertEquals(EmployeeType.CONTEXT_MANAGER, specializationType);
	}

	public void test_getElementType_editHelperContext_withEObject() {
		
		IEditHelperContext context = new EditHelperContext(cFinanceManager,
				getClientContext());
		
		IElementType type = getFixture().getElementType(context);
		assertNotNull(type);
		assertEquals(EmployeeType.CONTEXT_EMPLOYEE, type);
	}
	
	/**
	 * Verifies that the element type in the IEditHelperContext will be used
	 * regardless of the client context specified in the IEditHelperContext.
	 */
	public void test_getElementType_editHelperContext_withElementType() {
		IEditHelperContext context = new EditHelperContext(
				EmployeeType.CONTEXT_STUDENT, ClientContextManager.getDefaultClientContext());
		IElementType type = getFixture().getElementType(context);
		assertNotNull(type);
		assertEquals(EmployeeType.CONTEXT_STUDENT, type);
	}
	
	public void test_getElementType_editHelperContext_noClientContext() {
		IEditHelperContext context = new EditHelperContext(financeManager, null);
		
		IElementType type = getFixture().getElementType(context);
		assertNotNull(type);
		assertEquals(EmployeeType.EMPLOYEE, type);
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
		
        RecordingCommand recordingCommand = new RecordingCommand(getEditingDomain()) {
            protected void doExecute() {
                department.setManager(null);
            };
        };
        
        try {
            ((TransactionalCommandStack) getEditingDomain().getCommandStack()).execute(recordingCommand,
                null);

        } catch (RollbackException re) {
            fail("setUp() failed:" + re.getLocalizedMessage()); //$NON-NLS-1$
        } catch (InterruptedException ie) {
            fail("setUp() failed:" + ie.getLocalizedMessage()); //$NON-NLS-1$
        }
		
		assertNull(department.getManager());
		
		CreateElementRequest createRequest = new CreateElementRequest(getEditingDomain(),
			department, nullSpecialization);
		
		createRequest.setParameter("MANAGER", manager); //$NON-NLS-1$
		
		IElementType elementType = ElementTypeRegistry.getInstance()
			.getElementType(createRequest.getEditHelperContext());
		
		ICommand command = elementType.getEditCommand(createRequest);
		
		assertNotNull(command);
		assertTrue(command.canExecute());
		
        try {
            command.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }
		assertSame(manager, department.getManager());
		
		assertNull(command.getCommandResult().getReturnValue());
	}
	
	/**
	 * Verifies that the original metamodel type array is not reversed by the
	 * #getAllTypesMatching method.
	 */
	public void test_getAllTypesMatching_146097() {

		IElementType[] superTypes = EmployeeType.HIGH_SCHOOL_STUDENT
				.getAllSuperTypes();
		assertEquals(2, superTypes.length);
		assertEquals(superTypes[0], EmployeeType.EMPLOYEE);
		assertEquals(superTypes[1], EmployeeType.STUDENT);

		IElementType[] highSchoolStudentMatches = getFixture()
				.getAllTypesMatching(highSchoolStudent);
		assertTrue(highSchoolStudentMatches.length == 3);
		assertTrue(highSchoolStudentMatches[0] == EmployeeType.HIGH_SCHOOL_STUDENT);
		assertTrue(highSchoolStudentMatches[1] == EmployeeType.STUDENT);
		assertTrue(highSchoolStudentMatches[2] == EmployeeType.EMPLOYEE);

		// check that the super types array was not reversed by the call to
		// #getAllSuperTypes
		assertEquals(superTypes[0], EmployeeType.EMPLOYEE);
		assertEquals(superTypes[1], EmployeeType.STUDENT);
	}
}
