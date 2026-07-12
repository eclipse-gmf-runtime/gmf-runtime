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
package org.eclipse.gmf.tests.runtime.emf.type.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;
import org.junit.jupiter.api.Test;

public class SpecializationTypeTest {

	/**
	 * Tests that isSpecializationOf() does not throw a null pointer exception.
	 */
	@Test
	public void test_isSpecializationOf_120765() {

		assertTrue(EmployeeType.MANAGER.isSpecializationOf(EmployeeType.EMPLOYEE));
	}
}
