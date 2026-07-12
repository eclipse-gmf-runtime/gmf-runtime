/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.ui.services.action;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.emf.ui.services.action.AbstractModelActionFilterProvider;
import org.junit.jupiter.api.Test;

/**
 * Tests the AbstractModelActionFilterProvider.
 *
 * @author ldamus
 */
public class AbstractModelActionFilterProviderTest {

	protected static class MyModelActionFilterProvider extends AbstractModelActionFilterProvider {

		private boolean testedAttribute = false;

		private TransactionalEditingDomain editingDomain = TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain();

		@Override
		protected boolean doTestAttribute(Object target, String name, String value) {

			testedAttribute = true;
			return true;
		}

		@Override
		protected boolean doProvides(IOperation operation) {
			return true;
		}

		@Override
		protected TransactionalEditingDomain getEditingDomain(Object target) {
			return editingDomain;
		}

		public boolean didTestAttribute() {
			return testedAttribute;
		}
	}

	/**
	 * Tests that subclasses of AbstractModelActionFilterProviderTest can override
	 * the editing domain getter method.
	 */
	@Test
	public void test_editingDomainOverride_130758() {

		MyModelActionFilterProvider provider = new MyModelActionFilterProvider();

		// pass in an object that will return null in the superclass
		// getEditingDomain implementation
		boolean result = provider.testAttribute(Boolean.FALSE, StringStatics.BLANK, null);

		assertTrue(result);
		assertTrue(provider.didTestAttribute());
	}
}
