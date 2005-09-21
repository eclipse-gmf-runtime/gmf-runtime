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

import org.eclipse.gmf.runtime.emf.type.core.IMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.MetamodelTypeDescriptor;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;


public class MetamodelTypeDescriptorTest extends TestCase {

	private MetamodelTypeDescriptor fixture;

	public MetamodelTypeDescriptorTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(MetamodelTypeDescriptorTest.class);
	}

	protected MetamodelTypeDescriptor getFixture() {
		return fixture;
	}

	protected void setFixture(MetamodelTypeDescriptor fixture) {
		this.fixture = fixture;
	}

	public void test_metamodelTypeConstructor() {

		setFixture(new MetamodelTypeDescriptor((IMetamodelType) EmployeeType.STUDENT));
		
		assertEquals(EmployeeType.STUDENT.getDisplayName(), getFixture().getName());
		assertEquals(EmployeeType.STUDENT.getId(), getFixture().getId());
		assertEquals(EmployeeType.STUDENT.getIconURL(), getFixture().getIconURL());
		assertEquals(EmployeeType.STUDENT.getEClass(), getFixture().getEClass());
		assertEquals(EmployeeType.STUDENT.getEditHelper(), getFixture().getEditHelper());
		assertEquals(EmployeeType.STUDENT, getFixture().getElementType());
	}
}
