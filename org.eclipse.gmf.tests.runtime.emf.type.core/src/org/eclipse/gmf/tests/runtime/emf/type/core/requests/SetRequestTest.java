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
package org.eclipse.gmf.tests.runtime.emf.type.core.requests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.AbstractEMFTypeTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

/**
 * Tests the SetRequest.
 * 
 * @author ldamus
 */
public class SetRequestTest extends AbstractEMFTypeTest {

	private Department department;

	public SetRequestTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(SetRequestTest.class);
	}

	protected void doModelSetup(Resource resource) {
		department = (Department) getEmployeeFactory().create(
				getEmployeePackage().getDepartment());
		resource.getContents().add(department);
	}

	/**
	 * Tests that a SetValueCommand can be instantiated with a SetRequest whose
	 * elementToEdit is null.
	 */
	public void test_deferredElementToEdit_152302() {

		SetRequest request = new SetRequest(getEditingDomain(), null,
				EmployeePackage.eINSTANCE.getDepartment_Name(),
				"test_deferredElementToEdit_152302"); //$NON-NLS-1$

		try {
			SetValueCommand command = new SetValueCommand(request);
			assertFalse(command.canExecute());

		} catch (Exception e) {
			fail("expected to be able to instantiate the SetValueCommand without an elementToEdit"); //$NON-NLS-1$
		}
	}
}
