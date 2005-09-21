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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeeFactory
 * @generated
 */
public interface EmployeePackage extends EPackage{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "employee"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org.eclipse.gmf.tests.runtime.emf.type.core/Employee.ecore"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "Employee"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EmployeePackage eINSTANCE = org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl <em>Employee</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getEmployee()
	 * @generated
	 */
	int EMPLOYEE = 0;

	/**
	 * The feature id for the '<em><b>Salary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__SALARY = 0;

	/**
	 * The feature id for the '<em><b>Band</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__BAND = 1;

	/**
	 * The feature id for the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__NUMBER = 2;

	/**
	 * The feature id for the '<em><b>Security Clearance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__SECURITY_CLEARANCE = 3;

	/**
	 * The feature id for the '<em><b>Department</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__DEPARTMENT = 4;

	/**
	 * The feature id for the '<em><b>Full Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__FULL_TIME = 5;

	/**
	 * The feature id for the '<em><b>Office</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__OFFICE = 6;

	/**
	 * The number of structural features of the the '<em>Employee</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.OfficeImpl <em>Office</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.OfficeImpl
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getOffice()
	 * @generated
	 */
	int OFFICE = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.DepartmentImpl <em>Department</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.DepartmentImpl
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getDepartment()
	 * @generated
	 */
	int DEPARTMENT = 1;

	/**
	 * The feature id for the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT__NUMBER = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT__NAME = 1;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT__MEMBERS = 2;

	/**
	 * The feature id for the '<em><b>Manager</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT__MANAGER = 3;

	/**
	 * The number of structural features of the the '<em>Department</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT_FEATURE_COUNT = 4;

	/**
	 * The feature id for the '<em><b>Number Of Windows</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFICE__NUMBER_OF_WINDOWS = 0;

	/**
	 * The feature id for the '<em><b>Has Door</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFICE__HAS_DOOR = 1;

	/**
	 * The number of structural features of the the '<em>Office</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFICE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.StudentImpl <em>Student</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.StudentImpl
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getStudent()
	 * @generated
	 */
	int STUDENT = 3;

	/**
	 * The feature id for the '<em><b>Salary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDENT__SALARY = EMPLOYEE__SALARY;

	/**
	 * The feature id for the '<em><b>Band</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDENT__BAND = EMPLOYEE__BAND;

	/**
	 * The feature id for the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDENT__NUMBER = EMPLOYEE__NUMBER;

	/**
	 * The feature id for the '<em><b>Security Clearance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDENT__SECURITY_CLEARANCE = EMPLOYEE__SECURITY_CLEARANCE;

	/**
	 * The feature id for the '<em><b>Department</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDENT__DEPARTMENT = EMPLOYEE__DEPARTMENT;

	/**
	 * The feature id for the '<em><b>Full Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDENT__FULL_TIME = EMPLOYEE__FULL_TIME;

	/**
	 * The feature id for the '<em><b>Office</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDENT__OFFICE = EMPLOYEE__OFFICE;

	/**
	 * The feature id for the '<em><b>Coop</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDENT__COOP = EMPLOYEE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Student</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDENT_FEATURE_COUNT = EMPLOYEE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.LocationImpl <em>Location</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.LocationImpl
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getLocation()
	 * @generated
	 */
	int LOCATION = 4;

	/**
	 * The number of structural features of the the '<em>Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band <em>Band</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getBand()
	 * @generated
	 */
	int BAND = 5;


	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee <em>Employee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Employee</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee
	 * @generated
	 */
	EClass getEmployee();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getSalary <em>Salary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Salary</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getSalary()
	 * @see #getEmployee()
	 * @generated
	 */
	EAttribute getEmployee_Salary();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getBand <em>Band</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Band</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getBand()
	 * @see #getEmployee()
	 * @generated
	 */
	EAttribute getEmployee_Band();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getNumber <em>Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getNumber()
	 * @see #getEmployee()
	 * @generated
	 */
	EAttribute getEmployee_Number();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getOffice <em>Office</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Office</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getOffice()
	 * @see #getEmployee()
	 * @generated
	 */
	EReference getEmployee_Office();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#isSecurityClearance <em>Security Clearance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Security Clearance</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#isSecurityClearance()
	 * @see #getEmployee()
	 * @generated
	 */
	EAttribute getEmployee_SecurityClearance();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getDepartment <em>Department</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Department</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#getDepartment()
	 * @see #getEmployee()
	 * @generated
	 */
	EReference getEmployee_Department();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#isFullTime <em>Full Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Full Time</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee#isFullTime()
	 * @see #getEmployee()
	 * @generated
	 */
	EAttribute getEmployee_FullTime();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office <em>Office</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Office</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office
	 * @generated
	 */
	EClass getOffice();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office#getNumberOfWindows <em>Number Of Windows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Windows</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office#getNumberOfWindows()
	 * @see #getOffice()
	 * @generated
	 */
	EAttribute getOffice_NumberOfWindows();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office#isHasDoor <em>Has Door</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Has Door</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office#isHasDoor()
	 * @see #getOffice()
	 * @generated
	 */
	EAttribute getOffice_HasDoor();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department <em>Department</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Department</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department
	 * @generated
	 */
	EClass getDepartment();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department#getNumber <em>Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department#getNumber()
	 * @see #getDepartment()
	 * @generated
	 */
	EAttribute getDepartment_Number();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department#getName()
	 * @see #getDepartment()
	 * @generated
	 */
	EAttribute getDepartment_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department#getMembers <em>Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Members</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department#getMembers()
	 * @see #getDepartment()
	 * @generated
	 */
	EReference getDepartment_Members();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department#getManager <em>Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Manager</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department#getManager()
	 * @see #getDepartment()
	 * @generated
	 */
	EReference getDepartment_Manager();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Student <em>Student</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Student</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Student
	 * @generated
	 */
	EClass getStudent();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Student#isCoop <em>Coop</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Coop</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Student#isCoop()
	 * @see #getStudent()
	 * @generated
	 */
	EAttribute getStudent_Coop();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Location <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Location</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Location
	 * @generated
	 */
	EClass getLocation();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Band <em>Band</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Band</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Band
	 * @generated
	 */
	EEnum getBand();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EmployeeFactory getEmployeeFactory();

} //EmployeePackage
