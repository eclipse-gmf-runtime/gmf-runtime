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

package org.eclipse.gmf.tests.runtime.emf.type.core.employee;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.EcorePackage;

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
 * @model kind="package"
 * @generated
 */
public interface EmployeePackage extends EPackage {
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
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__EANNOTATIONS = EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Salary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__SALARY = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Band</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__BAND = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__NUMBER = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Security Clearance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__SECURITY_CLEARANCE = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Department</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__DEPARTMENT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Full Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__FULL_TIME = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Office</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE__OFFICE = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Employee</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPLOYEE_FEATURE_COUNT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 7;

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
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT__EANNOTATIONS = EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT__NUMBER = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT__NAME = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT__MEMBERS = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Manager</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT__MANAGER = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Department</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPARTMENT_FEATURE_COUNT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFICE__EANNOTATIONS = EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Number Of Windows</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFICE__NUMBER_OF_WINDOWS = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Has Door</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFICE__HAS_DOOR = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Office</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFICE_FEATURE_COUNT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 2;

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
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDENT__EANNOTATIONS = EMPLOYEE__EANNOTATIONS;

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
	 * The number of structural features of the '<em>Student</em>' class.
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
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__EANNOTATIONS = EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;

	/**
	 * The number of structural features of the '<em>Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION_FEATURE_COUNT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.ClientImpl <em>Client</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.ClientImpl
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getClient()
	 * @generated
	 */
	int CLIENT = 5;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLIENT__EANNOTATIONS = EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>First Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLIENT__FIRST_NAME = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Last Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLIENT__LAST_NAME = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Represents</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLIENT__REPRESENTS = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Client</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLIENT_FEATURE_COUNT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.CustomerImpl <em>Customer</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.CustomerImpl
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getCustomer()
	 * @generated
	 */
	int CUSTOMER = 6;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOMER__EANNOTATIONS = EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOMER__NAME = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOMER__ADDRESS = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Representatives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOMER__REPRESENTATIVES = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Subsidiaries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOMER__SUBSIDIARIES = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOMER__PARENT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Customer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOMER_FEATURE_COUNT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.HighSchoolStudentImpl <em>High School Student</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.HighSchoolStudentImpl
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getHighSchoolStudent()
	 * @generated
	 */
	int HIGH_SCHOOL_STUDENT = 7;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_SCHOOL_STUDENT__EANNOTATIONS = STUDENT__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Salary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_SCHOOL_STUDENT__SALARY = STUDENT__SALARY;

	/**
	 * The feature id for the '<em><b>Band</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_SCHOOL_STUDENT__BAND = STUDENT__BAND;

	/**
	 * The feature id for the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_SCHOOL_STUDENT__NUMBER = STUDENT__NUMBER;

	/**
	 * The feature id for the '<em><b>Security Clearance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_SCHOOL_STUDENT__SECURITY_CLEARANCE = STUDENT__SECURITY_CLEARANCE;

	/**
	 * The feature id for the '<em><b>Department</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_SCHOOL_STUDENT__DEPARTMENT = STUDENT__DEPARTMENT;

	/**
	 * The feature id for the '<em><b>Full Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_SCHOOL_STUDENT__FULL_TIME = STUDENT__FULL_TIME;

	/**
	 * The feature id for the '<em><b>Office</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_SCHOOL_STUDENT__OFFICE = STUDENT__OFFICE;

	/**
	 * The feature id for the '<em><b>Coop</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_SCHOOL_STUDENT__COOP = STUDENT__COOP;

	/**
	 * The number of structural features of the '<em>High School Student</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_SCHOOL_STUDENT_FEATURE_COUNT = STUDENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band <em>Band</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getBand()
	 * @generated
	 */
	int BAND = 8;


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
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Location
	 * @generated
	 */
	EClass getLocation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client <em>Client</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Client</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client
	 * @generated
	 */
	EClass getClient();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getFirstName <em>First Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>First Name</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getFirstName()
	 * @see #getClient()
	 * @generated
	 */
	EAttribute getClient_FirstName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getLastName <em>Last Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Name</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getLastName()
	 * @see #getClient()
	 * @generated
	 */
	EAttribute getClient_LastName();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getRepresents <em>Represents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Represents</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getRepresents()
	 * @see #getClient()
	 * @generated
	 */
	EReference getClient_Represents();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer <em>Customer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Customer</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer
	 * @generated
	 */
	EClass getCustomer();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getName()
	 * @see #getCustomer()
	 * @generated
	 */
	EAttribute getCustomer_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getAddress <em>Address</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Address</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getAddress()
	 * @see #getCustomer()
	 * @generated
	 */
	EAttribute getCustomer_Address();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getRepresentatives <em>Representatives</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Representatives</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getRepresentatives()
	 * @see #getCustomer()
	 * @generated
	 */
	EReference getCustomer_Representatives();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getSubsidiaries <em>Subsidiaries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Subsidiaries</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getSubsidiaries()
	 * @see #getCustomer()
	 * @generated
	 */
	EReference getCustomer_Subsidiaries();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parent</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getParent()
	 * @see #getCustomer()
	 * @generated
	 */
	EReference getCustomer_Parent();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.HighSchoolStudent <em>High School Student</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>High School Student</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.HighSchoolStudent
	 * @generated
	 */
	EClass getHighSchoolStudent();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band <em>Band</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Band</em>'.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band
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

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals  {
		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl <em>Employee</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getEmployee()
		 * @generated
		 */
		EClass EMPLOYEE = eINSTANCE.getEmployee();

		/**
		 * The meta object literal for the '<em><b>Salary</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EMPLOYEE__SALARY = eINSTANCE.getEmployee_Salary();

		/**
		 * The meta object literal for the '<em><b>Band</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EMPLOYEE__BAND = eINSTANCE.getEmployee_Band();

		/**
		 * The meta object literal for the '<em><b>Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EMPLOYEE__NUMBER = eINSTANCE.getEmployee_Number();

		/**
		 * The meta object literal for the '<em><b>Security Clearance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EMPLOYEE__SECURITY_CLEARANCE = eINSTANCE.getEmployee_SecurityClearance();

		/**
		 * The meta object literal for the '<em><b>Department</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EMPLOYEE__DEPARTMENT = eINSTANCE.getEmployee_Department();

		/**
		 * The meta object literal for the '<em><b>Full Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EMPLOYEE__FULL_TIME = eINSTANCE.getEmployee_FullTime();

		/**
		 * The meta object literal for the '<em><b>Office</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EMPLOYEE__OFFICE = eINSTANCE.getEmployee_Office();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.DepartmentImpl <em>Department</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.DepartmentImpl
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getDepartment()
		 * @generated
		 */
		EClass DEPARTMENT = eINSTANCE.getDepartment();

		/**
		 * The meta object literal for the '<em><b>Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEPARTMENT__NUMBER = eINSTANCE.getDepartment_Number();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEPARTMENT__NAME = eINSTANCE.getDepartment_Name();

		/**
		 * The meta object literal for the '<em><b>Members</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPARTMENT__MEMBERS = eINSTANCE.getDepartment_Members();

		/**
		 * The meta object literal for the '<em><b>Manager</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPARTMENT__MANAGER = eINSTANCE.getDepartment_Manager();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.OfficeImpl <em>Office</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.OfficeImpl
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getOffice()
		 * @generated
		 */
		EClass OFFICE = eINSTANCE.getOffice();

		/**
		 * The meta object literal for the '<em><b>Number Of Windows</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OFFICE__NUMBER_OF_WINDOWS = eINSTANCE.getOffice_NumberOfWindows();

		/**
		 * The meta object literal for the '<em><b>Has Door</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OFFICE__HAS_DOOR = eINSTANCE.getOffice_HasDoor();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.StudentImpl <em>Student</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.StudentImpl
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getStudent()
		 * @generated
		 */
		EClass STUDENT = eINSTANCE.getStudent();

		/**
		 * The meta object literal for the '<em><b>Coop</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDENT__COOP = eINSTANCE.getStudent_Coop();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.LocationImpl <em>Location</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.LocationImpl
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getLocation()
		 * @generated
		 */
		EClass LOCATION = eINSTANCE.getLocation();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.ClientImpl <em>Client</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.ClientImpl
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getClient()
		 * @generated
		 */
		EClass CLIENT = eINSTANCE.getClient();

		/**
		 * The meta object literal for the '<em><b>First Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLIENT__FIRST_NAME = eINSTANCE.getClient_FirstName();

		/**
		 * The meta object literal for the '<em><b>Last Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLIENT__LAST_NAME = eINSTANCE.getClient_LastName();

		/**
		 * The meta object literal for the '<em><b>Represents</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLIENT__REPRESENTS = eINSTANCE.getClient_Represents();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.CustomerImpl <em>Customer</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.CustomerImpl
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getCustomer()
		 * @generated
		 */
		EClass CUSTOMER = eINSTANCE.getCustomer();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CUSTOMER__NAME = eINSTANCE.getCustomer_Name();

		/**
		 * The meta object literal for the '<em><b>Address</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CUSTOMER__ADDRESS = eINSTANCE.getCustomer_Address();

		/**
		 * The meta object literal for the '<em><b>Representatives</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CUSTOMER__REPRESENTATIVES = eINSTANCE.getCustomer_Representatives();

		/**
		 * The meta object literal for the '<em><b>Subsidiaries</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CUSTOMER__SUBSIDIARIES = eINSTANCE.getCustomer_Subsidiaries();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CUSTOMER__PARENT = eINSTANCE.getCustomer_Parent();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.HighSchoolStudentImpl <em>High School Student</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.HighSchoolStudentImpl
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getHighSchoolStudent()
		 * @generated
		 */
		EClass HIGH_SCHOOL_STUDENT = eINSTANCE.getHighSchoolStudent();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band <em>Band</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band
		 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeePackageImpl#getBand()
		 * @generated
		 */
		EEnum BAND = eINSTANCE.getBand();

	}

} //EmployeePackage
