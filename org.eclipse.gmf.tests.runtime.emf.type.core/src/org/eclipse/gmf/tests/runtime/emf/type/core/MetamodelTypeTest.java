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
package org.eclipse.gmf.tests.runtime.emf.type.core;

import java.util.Arrays;

import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.MetamodelType;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class MetamodelTypeTest extends TestCase {

	private MetamodelType fixture;

	public MetamodelTypeTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(MetamodelTypeTest.class, "MetamodelType Tests"); //$NON-NLS-1$
	}

	protected MetamodelType getFixture() {
		return fixture;
	}

	protected void setFixture(MetamodelType fixture) {
		this.fixture = fixture;
	}

	/**
	 * Verifies that the super types of an element type that extends
	 * EModelElement will include the type for EObject.
	 */
	public void test_metamodelType_298661() {

		setFixture((MetamodelType) ElementTypeRegistry.getInstance().getType(
				"org.eclipse.gmf.tests.runtime.emf.type.core.298661.student")); //$NON-NLS-1$

		MetamodelType eObjectType = (MetamodelType) ElementTypeRegistry
				.getInstance()
				.getType(
						"org.eclipse.gmf.tests.runtime.emf.type.core.298661.eObject"); //$NON-NLS-1$

		assertTrue("Missing EObject supertype", Arrays.asList(
				getFixture().getAllSuperTypes()).contains(eObjectType));
	}
}
