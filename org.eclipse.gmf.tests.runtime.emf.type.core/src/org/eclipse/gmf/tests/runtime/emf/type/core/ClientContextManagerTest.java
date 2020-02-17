/******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation, Christian W. Damus, and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Christian W. Damus - bug 457888
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.emf.type.core;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.type.core.ClientContext;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextAddedEvent;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextManager;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextManagerAdapter;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextRemovedEvent;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IClientContextManagerListener;
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

		final IClientContext newContext = new ClientContext(
				"ClientContextManagerTest.context", new NullElementMatcher()); //$NON-NLS-1$

		final boolean[] notified = { false };
		final IClientContextManagerListener listener = new ClientContextManagerAdapter() {
			@Override
			public void clientContextAdded(ClientContextAddedEvent event) {
				notified[0] = true;
				assertEquals(newContext.getId(), event.getClientContextId());
				assertSame(newContext, event.getClientContext());
			}
		};
		ClientContextManager.getInstance().addClientContextManagerListener(listener);

		try {
			ClientContextManager.getInstance().registerClientContext(newContext);
		} finally {
			ClientContextManager.getInstance().removeClientContextManagerListener(listener);
		}

		IClientContext contextFromManager = ClientContextManager.getInstance()
				.getClientContext("ClientContextManagerTest.context"); //$NON-NLS-1$

		assertSame(newContext, contextFromManager);
		
		assertTrue(notified[0]);
	}

	/**
	 * Tests that a dynamically-registered client context can be removed.
	 * 
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=457888
	 */
	public void test_deregisterClientContext_457888() {
		final IClientContext newContext = new ClientContext(
				"ClientContextManagerTest.contextToRemove", new NullElementMatcher()); //$NON-NLS-1$

		// Register it, now
		ClientContextManager.getInstance().registerClientContext(newContext);

		final boolean[] notified = { false };
		final IClientContextManagerListener listener = new ClientContextManagerAdapter() {
			@Override
			public void clientContextRemoved(ClientContextRemovedEvent event) {
				notified[0] = true;
				assertEquals(newContext.getId(), event.getClientContextId());
				assertSame(newContext, event.getClientContext());
			}
		};
		ClientContextManager.getInstance().addClientContextManagerListener(listener);

		try {
			assertTrue(ClientContextManager.getInstance().deregisterClientContext(newContext));
		} finally {
			ClientContextManager.getInstance().removeClientContextManagerListener(listener);
		}

		IClientContext contextFromManager = ClientContextManager.getInstance().getClientContext(
				"ClientContextManagerTest.contextToRemove"); //$NON-NLS-1$

		assertNull(contextFromManager);
		assertTrue(notified[0]);
	}

	/**
	 * Tests that the default client context cannot be removed.
	 * 
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=457888
	 */
	public void test_deregisterDefaultContext_457888() {
		final IClientContext default_ = ClientContextManager.getDefaultClientContext();

		final boolean[] notified = { false };
		final IClientContextManagerListener listener = new ClientContextManagerAdapter() {
			@Override
			public void clientContextRemoved(ClientContextRemovedEvent event) {
				notified[0] = true;
			}
		};
		ClientContextManager.getInstance().addClientContextManagerListener(listener);

		try {
			assertFalse(ClientContextManager.getInstance().deregisterClientContext(default_));
		} finally {
			ClientContextManager.getInstance().removeClientContextManagerListener(listener);
		}

		assertFalse(notified[0]);
	}

	/**
	 * Tests that a statically-registered client context cannot be removed.
	 * 
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=457888
	 */
	public void test_deregisterStaticContext_457888() {
		final IClientContext context = ClientContextManager.getInstance().getClientContext(
				"org.eclipse.gmf.tests.runtime.emf.type.core.ClientContext1"); //$NON-NLS-1$

		final boolean[] notified = { false };
		final IClientContextManagerListener listener = new ClientContextManagerAdapter() {
			@Override
			public void clientContextRemoved(ClientContextRemovedEvent event) {
				notified[0] = true;
			}
		};
		ClientContextManager.getInstance().addClientContextManagerListener(listener);

		try {
			assertFalse(ClientContextManager.getInstance().deregisterClientContext(context));
		} finally {
			ClientContextManager.getInstance().removeClientContextManagerListener(listener);
		}

		assertFalse(notified[0]);
	}
}
