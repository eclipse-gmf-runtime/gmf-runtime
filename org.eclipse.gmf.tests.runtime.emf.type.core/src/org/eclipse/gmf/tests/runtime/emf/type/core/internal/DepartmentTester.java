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

import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;


/**
 * @author ldamus
 */
public class DepartmentTester
	extends PropertyTester {

	/* (non-Javadoc)
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		
		if (receiver instanceof Department) {
			Department department = (Department) receiver;
			
			if (property.equals("departmentName")) { //$NON-NLS-1$
				return department.getName().equals(expectedValue);
			}
		}
		return false;
	}

}
