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

package org.eclipse.gmf.tests.runtime.emf.type.core.internal;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;

/**
 * @author ldamus
 */
public class ManagerMatcher
	implements IElementMatcher {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementMatcher#matches(org.eclipse.emf.ecore.EObject)
	 */
	public boolean matches(EObject eObject) {
		
		if (eObject instanceof Employee) {
			Employee employee = (Employee) eObject;
			
			if (employee.getOffice().getNumberOfWindows() == 1
				&& employee.getOffice().isHasDoor() == false) {
				return true;
			}
		}
		return false;
	}

}
