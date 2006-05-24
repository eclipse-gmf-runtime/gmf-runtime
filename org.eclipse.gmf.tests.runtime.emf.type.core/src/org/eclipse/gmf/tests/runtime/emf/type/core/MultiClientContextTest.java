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

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.type.core.ClientContext;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextManager;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.MultiClientContext;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;

public class MultiClientContextTest extends AbstractEMFTypeTest {

	private MultiClientContext multiContext;

	private IClientContext childContext1;

	private IClientContext childContext2;

	private IClientContext clientContext1;

	private IClientContext clientContext2;

	private IClientContext unboundClientContext;

	private Department department;

	private Employee employee_101;

	private Employee employee_102;

	public MultiClientContextTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(MultiClientContextTest.class,
				"MultiClientContext Test Suit"); //$NON-NLS-1$
	}

	protected void doModelSetupWithContext(Resource resource) {
		department = (Department) getEmployeeFactory().create(
				getEmployeePackage().getDepartment());
		department.setName("MultiClientContextDepartment"); //$NON-NLS-1$
		resource.getContents().add(department);

		employee_101 = (Employee) getEmployeeFactory().create(
				getEmployeePackage().getEmployee());
		employee_101.setNumber(101);
		department.getMembers().add(employee_101);

		employee_102 = (Employee) getEmployeeFactory().create(
				getEmployeePackage().getEmployee());
		employee_102.setNumber(102);
		department.getMembers().add(employee_102);

		childContext1 = new ClientContext("childContext1", //$NON-NLS-1$
				new IElementMatcher() {
					public boolean matches(EObject eObject) {
						EObject container = eObject.eContainer();

						return container instanceof Department
								&& ((Department) container).getName().equals(
										"MultiClientContextDepartment"); //$NON-NLS-1$
					};
				});

		childContext2 = new ClientContext(
				"childContext2", new IElementMatcher() { //$NON-NLS-1$
					public boolean matches(EObject eObject) {
						return eObject instanceof Employee
								&& ((Employee) eObject).getNumber() == 101;
					};
				});

		multiContext = new MultiClientContext(Collections.singletonList(childContext1));
	}

	protected IClientContext getClientContext1() {
		if (clientContext1 == null) {
			clientContext1 = ClientContextManager
					.getInstance()
					.getClientContext(
							"org.eclipse.gmf.tests.runtime.emf.type.core.ClientContext1"); //$NON-NLS-1$
		}
		return clientContext1;
	}

	protected IClientContext getClientContext2() {
		if (clientContext2 == null) {
			clientContext2 = ClientContextManager
					.getInstance()
					.getClientContext(
							"org.eclipse.gmf.tests.runtime.emf.type.core.ClientContext2"); //$NON-NLS-1$
		}
		return clientContext2;
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

	public void test_bindId() {

		String typeID = "org.eclipse.gmf.tests.runtime.emf.type.example1.employee"; //$NON-NLS-1$
		IElementType type = ElementTypeRegistry.getInstance().getType(typeID);

		assertFalse(multiContext.includes(type));

		multiContext.bindId(typeID);

		assertTrue(multiContext.includes(type));
	}

	public void test_bindPattern() {

		String typeID = "org.eclipse.gmf.tests.runtime.emf.type.example2.employee"; //$NON-NLS-1$
		IElementType type = ElementTypeRegistry.getInstance().getType(typeID);

		assertFalse(multiContext.includes(type));

		Pattern pattern = Pattern
				.compile("org.eclipse.gmf.tests.runtime.emf.type.example2.*"); //$NON-NLS-1$

		multiContext.bindPattern(pattern);

		assertTrue(multiContext.includes(type));
	}

	public void test_getMatcher() {

		assertTrue(multiContext.getMatcher().matches(employee_101));
		assertTrue(multiContext.getMatcher().matches(employee_102));

		Collection children = multiContext.getChildren();
		assertEquals(1, children.size());

		multiContext.add(childContext2);

		children = multiContext.getChildren();
		assertEquals(2, children.size());
		assertTrue(children.contains(childContext1));
		assertTrue(children.contains(childContext2));

		assertTrue(multiContext.getMatcher().matches(employee_101));
		assertFalse(multiContext.getMatcher().matches(employee_102));
	}
}
