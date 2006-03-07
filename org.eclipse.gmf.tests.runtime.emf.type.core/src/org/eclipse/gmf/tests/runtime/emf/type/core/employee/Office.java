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

package org.eclipse.gmf.tests.runtime.emf.type.core.employee;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Office</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office#getNumberOfWindows <em>Number Of Windows</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office#isHasDoor <em>Has Door</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getOffice()
 * @model
 * @generated
 */
public interface Office extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Number Of Windows</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Windows</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number Of Windows</em>' attribute.
	 * @see #setNumberOfWindows(int)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getOffice_NumberOfWindows()
	 * @model required="true"
	 * @generated
	 */
	int getNumberOfWindows();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office#getNumberOfWindows <em>Number Of Windows</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number Of Windows</em>' attribute.
	 * @see #getNumberOfWindows()
	 * @generated
	 */
	void setNumberOfWindows(int value);

	/**
	 * Returns the value of the '<em><b>Has Door</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Has Door</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Has Door</em>' attribute.
	 * @see #setHasDoor(boolean)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getOffice_HasDoor()
	 * @model required="true"
	 * @generated
	 */
	boolean isHasDoor();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office#isHasDoor <em>Has Door</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Has Door</em>' attribute.
	 * @see #isHasDoor()
	 * @generated
	 */
	void setHasDoor(boolean value);

} // Office
