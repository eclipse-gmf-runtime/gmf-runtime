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

package org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl;

import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Office</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.OfficeImpl#getNumberOfWindows <em>Number Of Windows</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.OfficeImpl#isHasDoor <em>Has Door</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OfficeImpl extends EObjectImpl implements Office {
	/**
	 * The default value of the '{@link #getNumberOfWindows() <em>Number Of Windows</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfWindows()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_WINDOWS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumberOfWindows() <em>Number Of Windows</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfWindows()
	 * @generated
	 * @ordered
	 */
	protected int numberOfWindows = NUMBER_OF_WINDOWS_EDEFAULT;

	/**
	 * The default value of the '{@link #isHasDoor() <em>Has Door</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasDoor()
	 * @generated
	 * @ordered
	 */
	protected static final boolean HAS_DOOR_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isHasDoor() <em>Has Door</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasDoor()
	 * @generated
	 * @ordered
	 */
	protected boolean hasDoor = HAS_DOOR_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OfficeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return EmployeePackage.eINSTANCE.getOffice();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumberOfWindows() {
		return numberOfWindows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumberOfWindows(int newNumberOfWindows) {
		int oldNumberOfWindows = numberOfWindows;
		numberOfWindows = newNumberOfWindows;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.OFFICE__NUMBER_OF_WINDOWS, oldNumberOfWindows, numberOfWindows));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isHasDoor() {
		return hasDoor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHasDoor(boolean newHasDoor) {
		boolean oldHasDoor = hasDoor;
		hasDoor = newHasDoor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.OFFICE__HAS_DOOR, oldHasDoor, hasDoor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case EmployeePackage.OFFICE__NUMBER_OF_WINDOWS:
				return new Integer(getNumberOfWindows());
			case EmployeePackage.OFFICE__HAS_DOOR:
				return isHasDoor() ? Boolean.TRUE : Boolean.FALSE;
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case EmployeePackage.OFFICE__NUMBER_OF_WINDOWS:
				setNumberOfWindows(((Integer)newValue).intValue());
				return;
			case EmployeePackage.OFFICE__HAS_DOOR:
				setHasDoor(((Boolean)newValue).booleanValue());
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case EmployeePackage.OFFICE__NUMBER_OF_WINDOWS:
				setNumberOfWindows(NUMBER_OF_WINDOWS_EDEFAULT);
				return;
			case EmployeePackage.OFFICE__HAS_DOOR:
				setHasDoor(HAS_DOOR_EDEFAULT);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case EmployeePackage.OFFICE__NUMBER_OF_WINDOWS:
				return numberOfWindows != NUMBER_OF_WINDOWS_EDEFAULT;
			case EmployeePackage.OFFICE__HAS_DOOR:
				return hasDoor != HAS_DOOR_EDEFAULT;
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (numberOfWindows: "); //$NON-NLS-1$
		result.append(numberOfWindows);
		result.append(", hasDoor: "); //$NON-NLS-1$
		result.append(hasDoor);
		result.append(')');
		return result.toString();
	}

} //OfficeImpl
