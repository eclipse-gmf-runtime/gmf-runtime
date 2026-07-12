/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.SpecializationTypeDescriptor;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;
import org.junit.jupiter.api.Test;

public class SpecializationTypeDescriptorTest {

	private SpecializationTypeDescriptor fixture;

	protected SpecializationTypeDescriptor getFixture() {
		return fixture;
	}

	protected void setFixture(SpecializationTypeDescriptor fixture) {
		this.fixture = fixture;
	}

	@Test
	public void test_specializationTypeConstructor() {

		setFixture(new SpecializationTypeDescriptor(EmployeeType.MANAGER));

		assertEquals(EmployeeType.MANAGER.getDisplayName(), getFixture().getName());
		assertEquals(EmployeeType.MANAGER.getId(), getFixture().getId());
		assertEquals(EmployeeType.MANAGER.getIconURL(), getFixture().getIconURL());
		assertEquals(EmployeeType.MANAGER.getEContainerDescriptor(), getFixture().getContainerDescriptor());
		assertEquals(EmployeeType.MANAGER.getEditHelperAdvice(),
				getFixture().getEditHelperAdviceDescriptor().getEditHelperAdvice());
		assertEquals(EmployeeType.MANAGER.getEditHelperAdvice(), getFixture().getEditHelperAdvice());
		assertEquals(EmployeeType.MANAGER, getFixture().getElementType());
		assertEquals(EmployeeType.MANAGER.getMatcher(), getFixture().getMatcher());
		assertEquals(EmployeeType.MANAGER.getSpecializedTypeIds(), getFixture().getSpecializationTypeIds());
		assertEquals(EmployeeType.MANAGER.getSpecializedTypes(), getFixture().getSpecializedTypes());
	}
}
