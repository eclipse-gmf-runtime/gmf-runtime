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

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Client</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getFirstName <em>First Name</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getLastName <em>Last Name</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getRepresents <em>Represents</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getClient()
 * @model
 * @generated
 */
public interface Client extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>First Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>First Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>First Name</em>' attribute.
	 * @see #setFirstName(String)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getClient_FirstName()
	 * @model
	 * @generated
	 */
	String getFirstName();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getFirstName <em>First Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>First Name</em>' attribute.
	 * @see #getFirstName()
	 * @generated
	 */
	void setFirstName(String value);

	/**
	 * Returns the value of the '<em><b>Last Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Name</em>' attribute.
	 * @see #setLastName(String)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getClient_LastName()
	 * @model
	 * @generated
	 */
	String getLastName();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getLastName <em>Last Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Name</em>' attribute.
	 * @see #getLastName()
	 * @generated
	 */
	void setLastName(String value);

	/**
	 * Returns the value of the '<em><b>Represents</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getRepresentatives <em>Representatives</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Represents</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Represents</em>' container reference.
	 * @see #setRepresents(Customer)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage#getClient_Represents()
	 * @see org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer#getRepresentatives
	 * @model opposite="representatives"
	 * @generated
	 */
	Customer getRepresents();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client#getRepresents <em>Represents</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Represents</em>' container reference.
	 * @see #getRepresents()
	 * @generated
	 */
	void setRepresents(Customer value);

} // Client
