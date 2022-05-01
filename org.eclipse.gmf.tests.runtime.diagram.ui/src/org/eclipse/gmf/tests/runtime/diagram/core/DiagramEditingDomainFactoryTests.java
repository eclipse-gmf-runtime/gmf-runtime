/******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.diagram.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.DiagramEditingDomainFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Unit tests for the {@link DiagramEditingDomainFactory}.
 * 
 * @author ldamus
 */
public class DiagramEditingDomainFactoryTests extends TestCase {

	public DiagramEditingDomainFactoryTests(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(DiagramEditingDomainFactoryTests.class);
	}

	/**
	 * Tests that the editing domain created by the
	 * {@link DiagramEditingDomainFactory} is configured only once.
	 */
	public void test_getEditingDomain_254621() {

		DiagramEditingDomainFactoryFixture fixture = new DiagramEditingDomainFactoryFixture();

		// createEditingDomain
		TransactionalEditingDomain domain = fixture.createEditingDomain();
		assertEquals(1, fixture.getConfigureCount(domain));

		// createEditingDomain(IOperationHistory)
		domain = fixture.createEditingDomain(OperationHistoryFactory
				.getOperationHistory());
		assertEquals(1, fixture.getConfigureCount(domain));

		// createEditingDomain(ResourceSet)
		domain = fixture.createEditingDomain(new ResourceSetImpl());
		assertEquals(1, fixture.getConfigureCount(domain));

		// createEditingDomain(ResourceSet, IOperationHistory)
		domain = fixture.createEditingDomain(new ResourceSetImpl(),
				OperationHistoryFactory.getOperationHistory());
		assertEquals(1, fixture.getConfigureCount(domain));
	}

	/**
	 * Fixture that keeps track of how many times each editing domain is
	 * configured.
	 */
	protected static class DiagramEditingDomainFactoryFixture extends
			DiagramEditingDomainFactory {

		private Map<TransactionalEditingDomain, Integer> configureCount = new HashMap<TransactionalEditingDomain, Integer>();

		@Override
		protected void configure(TransactionalEditingDomain domain) {
			super.configure(domain);

			Integer count = configureCount.get(domain);

			if (count == null) {
				count = Integer.valueOf(1);

			} else {
				count = count + 1;
			}

			configureCount.put(domain, count);
		}

		public int getConfigureCount(TransactionalEditingDomain domain) {
			Integer count = configureCount.get(domain);
			return count == null ? 0 : count;
		}
	}
}
