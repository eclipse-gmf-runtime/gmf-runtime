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
package org.eclipse.gmf.tests.runtime.emf.type.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;


public class SpecializationTypeTest extends TestCase {

	public SpecializationTypeTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(SpecializationTypeTest.class);
	}

    /**
     * Tests that isSpecializationOf() does not throw a null pointer exception.
     */
    public void test_isSpecializationOf_120765() {

        assertTrue(EmployeeType.MANAGER.isSpecializationOf(EmployeeType.EMPLOYEE));
    }
}
