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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.SpecializationTypeDescriptor;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;


public class SpecializationTypeDescriptorTest extends TestCase {

	private SpecializationTypeDescriptor fixture;

	public SpecializationTypeDescriptorTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(SpecializationTypeDescriptorTest.class);
	}

	protected SpecializationTypeDescriptor getFixture() {
		return fixture;
	}

	protected void setFixture(SpecializationTypeDescriptor fixture) {
		this.fixture = fixture;
	}

	public void test_specializationTypeConstructor() {

		setFixture(new SpecializationTypeDescriptor(EmployeeType.MANAGER));
		
		assertEquals(EmployeeType.MANAGER.getDisplayName(), getFixture().getName());
		assertEquals(EmployeeType.MANAGER.getId(), getFixture().getId());
		assertEquals(EmployeeType.MANAGER.getIconURL(), getFixture().getIconURL());
		assertEquals(EmployeeType.MANAGER.getEContainerDescriptor(), getFixture().getContainerDescriptor());
		assertEquals(EmployeeType.MANAGER.getEditHelperAdvice(), getFixture().getEditHelperAdvice());
		assertEquals(EmployeeType.MANAGER, getFixture().getElementType());
		assertEquals(EmployeeType.MANAGER.getMatcher(), getFixture().getMatcher());
		assertEquals(EmployeeType.MANAGER.getSpecializedTypeIds(), getFixture().getSpecializationTypeIds());
		assertEquals(EmployeeType.MANAGER.getSpecializedTypes(), getFixture().getSpecializedTypes());
	}
}
