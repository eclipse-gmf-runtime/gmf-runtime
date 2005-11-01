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

import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;

/**
 * @author ldamus
 */
public class CreateElementCommandTest
	extends TestCase {

	private CreateElementCommand fixture;

	public CreateElementCommandTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(CreateElementCommandTest.class);
	}

	protected CreateElementCommand getFixture() {
		return fixture;
	}

	protected void setFixture(CreateElementCommand fixture) {
		this.fixture = fixture;
	}

	public void test_isExecutable_noEClassToEdit() {

		CreateElementRequest request = new CreateElementRequest(null,
			EmployeeType.EMPLOYEE, EmployeePackage.eINSTANCE.getDepartment_Members());
		setFixture(new CreateElementCommand(request));

		assertFalse(getFixture().isExecutable());
	}
}
