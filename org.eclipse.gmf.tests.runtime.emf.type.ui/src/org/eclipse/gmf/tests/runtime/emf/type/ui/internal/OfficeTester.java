/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.emf.type.ui.internal;

import org.eclipse.core.expressions.PropertyTester;

import org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Employee;

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