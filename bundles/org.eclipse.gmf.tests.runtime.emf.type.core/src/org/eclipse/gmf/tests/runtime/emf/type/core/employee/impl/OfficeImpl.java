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

package org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EModelElementImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;

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
public class OfficeImpl extends EModelElementImpl implements Office {
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
	 * The flag representing the value of the '{@link #isHasDoor() <em>Has Door</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasDoor()
	 * @generated
	 * @ordered
	 */
	protected static final int HAS_DOOR_EFLAG = 1 << 8;

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
		return EmployeePackage.Literals.OFFICE;
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
		return (eFlags & HAS_DOOR_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHasDoor(boolean newHasDoor) {
		boolean oldHasDoor = (eFlags & HAS_DOOR_EFLAG) != 0;
		if (newHasDoor) eFlags |= HAS_DOOR_EFLAG; else eFlags &= ~HAS_DOOR_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.OFFICE__HAS_DOOR, oldHasDoor, newHasDoor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EmployeePackage.OFFICE__NUMBER_OF_WINDOWS:
				return Integer.valueOf(getNumberOfWindows());
			case EmployeePackage.OFFICE__HAS_DOOR:
				return isHasDoor() ? Boolean.TRUE : Boolean.FALSE;
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EmployeePackage.OFFICE__NUMBER_OF_WINDOWS:
				setNumberOfWindows(((Integer)newValue).intValue());
				return;
			case EmployeePackage.OFFICE__HAS_DOOR:
				setHasDoor(((Boolean)newValue).booleanValue());
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case EmployeePackage.OFFICE__NUMBER_OF_WINDOWS:
				setNumberOfWindows(NUMBER_OF_WINDOWS_EDEFAULT);
				return;
			case EmployeePackage.OFFICE__HAS_DOOR:
				setHasDoor(HAS_DOOR_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case EmployeePackage.OFFICE__NUMBER_OF_WINDOWS:
				return numberOfWindows != NUMBER_OF_WINDOWS_EDEFAULT;
			case EmployeePackage.OFFICE__HAS_DOOR:
				return ((eFlags & HAS_DOOR_EFLAG) != 0) != HAS_DOOR_EDEFAULT;
		}
		return super.eIsSet(featureID);
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
		result.append((eFlags & HAS_DOOR_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

} //OfficeImpl
