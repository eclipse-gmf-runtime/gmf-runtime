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

package org.eclipse.gmf.tests.runtime.emf.type.core.internal;

import org.eclipse.core.expressions.PropertyTester;

import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;

/**
 * @author ldamus
 */
public class OfficeTester
	extends PropertyTester {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object,
	 *      java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		if (receiver instanceof Employee) {
			Employee employee = (Employee) receiver;

			if (property.equals("officeHasDoor")) { //$NON-NLS-1$
				if (employee.getOffice() != null && employee.getOffice().isHasDoor()) {
					return expectedValue.equals("true"); //$NON-NLS-1$
				}

			} else if (property.equals("officeWindowCount")) { //$NON-NLS-1$
				return employee.getOffice() != null && employee.getOffice().getNumberOfWindows() == (Integer
					.getInteger((String) expectedValue)).intValue();
			}
		}
		return false;
	}
}
