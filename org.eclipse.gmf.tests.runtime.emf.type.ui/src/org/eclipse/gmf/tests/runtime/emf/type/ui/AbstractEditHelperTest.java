/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.emf.type.ui;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.ui.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.ui.internal.EmployeeType;

public class AbstractEditHelperTest
	extends TestCase {

	private CreateElementRequest fixture;

	private EmployeePackage employeePkg;

	private EFactory employeeFactory;

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

	protected void setUp()
		throws Exception {
		super.setUp();

		employeePkg = EmployeePackage.eINSTANCE;
		employeeFactory = employeePkg.getEFactoryInstance();

		department = (Department) employeeFactory.create(employeePkg
			.getDepartment());
		department.setName("Department"); //$NON-NLS-1$
	}

	protected void tearDown()
		throws Exception {
		super.tearDown();
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

		CreateElementRequest request = new CreateElementRequest(department,
			EmployeeType.MANAGER);
		EmployeeType.DEPARTMENT.getEditCommand(request);
		assertSame(employeePkg.getDepartment_Manager(), request
			.getContainmentFeature());
	}
}
