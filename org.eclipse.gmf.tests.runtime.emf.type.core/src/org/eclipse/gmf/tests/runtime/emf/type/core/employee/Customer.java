/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Customer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getAddress <em>Address</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getRepresentatives <em>Representatives</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getSubsidiaries <em>Subsidiaries</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getParent <em>Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getCustomer()
 * @model
 * @generated
 */
public interface Customer extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getCustomer_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Address</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Address</em>' attribute.
	 * @see #setAddress(String)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getCustomer_Address()
	 * @model
	 * @generated
	 */
	String getAddress();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getAddress <em>Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Address</em>' attribute.
	 * @see #getAddress()
	 * @generated
	 */
	void setAddress(String value);

	/**
	 * Returns the value of the '<em><b>Representatives</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getRepresents <em>Represents</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Representatives</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Representatives</em>' containment reference list.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getCustomer_Representatives()
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getRepresents
	 * @model type="org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client" opposite="represents" containment="true"
	 * @generated
	 */
	EList getRepresentatives();

	/**
	 * Returns the value of the '<em><b>Subsidiaries</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Subsidiaries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Subsidiaries</em>' containment reference list.
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getCustomer_Subsidiaries()
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getParent
	 * @model type="org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer" opposite="parent" containment="true"
	 * @generated
	 */
	EList getSubsidiaries();

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getSubsidiaries <em>Subsidiaries</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(Customer)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getCustomer_Parent()
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getSubsidiaries
	 * @model opposite="subsidiaries"
	 * @generated
	 */
	Customer getParent();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(Customer value);

} // Customer
