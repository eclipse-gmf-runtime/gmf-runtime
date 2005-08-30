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

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Employee;

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