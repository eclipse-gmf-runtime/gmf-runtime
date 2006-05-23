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

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.type.core.ClientContext;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextManager;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.NullElementMatcher;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;

public class ClientContextManagerTest extends AbstractEMFTypeTest {

	private IClientContext clientContext1;

	private IClientContext clientContext2;

	private IClientContext unboundClientContext;

	private Department department;

	private Employee employee;

	public ClientContextManagerTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(ClientContextManagerTest.class,
				"ClientContextManager Test Suite"); //$NON-NLS-1$
	}

	protected void doModelSetupWithContext(Resource resource) {
		department = (Department) getEmployeeFactory().create(
				getEmployeePackage().getDepartment());
		department.setName("Department"); //$NON-NLS-1$
		resource.getContents().add(department);

		employee = (Employee) getEmployeeFactory().create(
				getEmployeePackage().getEmployee());
		employee.setNumber(1);
		department.getMembers().add(employee);
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

	public void test_getBinding_type() {

		IClientContext context = ClientContextManager.getInstance().getBinding(
				EmployeeType.CONTEXT_EMPLOYEE);

		assertTrue(context.isMultiClientContext());
		assertEquals(2, context.getChildren().size());
		assertTrue(context.getChildren().contains(getClientContext1()));
		assertTrue(context.getChildren().contains(getClientContext2()));
	}

	public void test_getClientContext() {

		String unboundID = "org.eclipse.gmf.tests.runtime.emf.type.core.UnboundClientContext"; //$NON-NLS-1$

		IClientContext context = ClientContextManager.getInstance()
				.getClientContext(unboundID);

		assertNotNull(context);
		assertTrue(context.getId().equals(unboundID));
		assertTrue(context.getMatcher() instanceof NullElementMatcher);
	}

	public void test_getClientContextFor() {

		IClientContext context = ClientContextManager.getInstance()
				.getClientContextFor(employee);

		assertTrue(context.isMultiClientContext());
		assertEquals(2, context.getChildren().size());
		assertTrue(context.getChildren().contains(getClientContext1()));
		assertTrue(context.getChildren().contains(getClientContext2()));
	}

	public void test_getClientContexts() {

		Set contexts = ClientContextManager.getInstance().getClientContexts();

		assertTrue(contexts.contains(getClientContext1()));
		assertTrue(contexts.contains(getClientContext2()));
		assertTrue(contexts.contains(getUnboundClientContext()));

	}

	public void test_registerClientContext() {

		IClientContext newContext = new ClientContext(
				"ClientContextManagerTest.context", new NullElementMatcher()); //$NON-NLS-1$

		ClientContextManager.getInstance().registerClientContext(newContext);

		IClientContext contextFromManager = ClientContextManager.getInstance()
				.getClientContext("ClientContextManagerTest.context"); //$NON-NLS-1$

		assertSame(newContext, contextFromManager);
	}
}
