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
 * A representation of the model object '<em><b>Employee</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getSalary <em>Salary</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getBand <em>Band</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getNumber <em>Number</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#isSecurityClearance <em>Security Clearance</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getDepartment <em>Department</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#isFullTime <em>Full Time</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getOffice <em>Office</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getEmployee()
 * @model
 * @generated
 */
public interface Employee extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Salary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Salary</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Salary</em>' attribute.
	 * @see #setSalary(int)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getEmployee_Salary()
	 * @model required="true"
	 * @generated
	 */
	int getSalary();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getSalary <em>Salary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Salary</em>' attribute.
	 * @see #getSalary()
	 * @generated
	 */
	void setSalary(int value);

	/**
	 * Returns the value of the '<em><b>Band</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Band</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Band</em>' attribute.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band
	 * @see #setBand(Band)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getEmployee_Band()
	 * @model required="true"
	 * @generated
	 */
	Band getBand();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getBand <em>Band</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Band</em>' attribute.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band
	 * @see #getBand()
	 * @generated
	 */
	void setBand(Band value);

	/**
	 * Returns the value of the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number</em>' attribute.
	 * @see #setNumber(int)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getEmployee_Number()
	 * @model required="true"
	 * @generated
	 */
	int getNumber();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getNumber <em>Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number</em>' attribute.
	 * @see #getNumber()
	 * @generated
	 */
	void setNumber(int value);

	/**
	 * Returns the value of the '<em><b>Office</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Office</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Office</em>' containment reference.
	 * @see #setOffice(Office)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getEmployee_Office()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Office getOffice();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getOffice <em>Office</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Office</em>' containment reference.
	 * @see #getOffice()
	 * @generated
	 */
	void setOffice(Office value);

	/**
	 * Returns the value of the '<em><b>Security Clearance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Security Clearance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Security Clearance</em>' attribute.
	 * @see #setSecurityClearance(boolean)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getEmployee_SecurityClearance()
	 * @model required="true"
	 * @generated
	 */
	boolean isSecurityClearance();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#isSecurityClearance <em>Security Clearance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Security Clearance</em>' attribute.
	 * @see #isSecurityClearance()
	 * @generated
	 */
	void setSecurityClearance(boolean value);

	/**
	 * Returns the value of the '<em><b>Department</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department#getMembers <em>Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Department</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Department</em>' container reference.
	 * @see #setDepartment(Department)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getEmployee_Department()
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department#getMembers
	 * @model opposite="members" required="true"
	 * @generated
	 */
	Department getDepartment();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getDepartment <em>Department</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Department</em>' container reference.
	 * @see #getDepartment()
	 * @generated
	 */
	void setDepartment(Department value);

	/**
	 * Returns the value of the '<em><b>Full Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Full Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Full Time</em>' attribute.
	 * @see #setFullTime(boolean)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getEmployee_FullTime()
	 * @model required="true"
	 * @generated
	 */
	boolean isFullTime();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#isFullTime <em>Full Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Full Time</em>' attribute.
	 * @see #isFullTime()
	 * @generated
	 */
	void setFullTime(boolean value);

} // Employee
