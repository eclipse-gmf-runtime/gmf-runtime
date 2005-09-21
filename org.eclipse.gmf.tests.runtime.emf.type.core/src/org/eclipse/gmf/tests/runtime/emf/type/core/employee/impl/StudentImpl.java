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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Student;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Student</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.StudentImpl#isCoop <em>Coop</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StudentImpl extends EmployeeImpl implements Student {
	/**
	 * The default value of the '{@link #isCoop() <em>Coop</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCoop()
	 * @generated
	 * @ordered
	 */
	protected static final boolean COOP_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCoop() <em>Coop</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCoop()
	 * @generated
	 * @ordered
	 */
	protected boolean coop = COOP_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StudentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return EmployeePackage.eINSTANCE.getStudent();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isCoop() {
		return coop;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCoop(boolean newCoop) {
		boolean oldCoop = coop;
		coop = newCoop;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.STUDENT__COOP, oldCoop, coop));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case EmployeePackage.STUDENT__DEPARTMENT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, EmployeePackage.STUDENT__DEPARTMENT, msgs);
				default:
					return eDynamicInverseAdd(otherEnd, featureID, baseClass, msgs);
			}
		}
		if (eContainer != null)
			msgs = eBasicRemoveFromContainer(msgs);
		return eBasicSetContainer(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case EmployeePackage.STUDENT__DEPARTMENT:
					return eBasicSetContainer(null, EmployeePackage.STUDENT__DEPARTMENT, msgs);
				case EmployeePackage.STUDENT__OFFICE:
					return basicSetOffice(null, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eBasicRemoveFromContainer(NotificationChain msgs) {
		if (eContainerFeatureID >= 0) {
			switch (eContainerFeatureID) {
				case EmployeePackage.STUDENT__DEPARTMENT:
					return eContainer.eInverseRemove(this, EmployeePackage.DEPARTMENT__MEMBERS, Department.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return eContainer.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case EmployeePackage.STUDENT__SALARY:
				return new Integer(getSalary());
			case EmployeePackage.STUDENT__BAND:
				return getBand();
			case EmployeePackage.STUDENT__NUMBER:
				return new Integer(getNumber());
			case EmployeePackage.STUDENT__SECURITY_CLEARANCE:
				return isSecurityClearance() ? Boolean.TRUE : Boolean.FALSE;
			case EmployeePackage.STUDENT__DEPARTMENT:
				return getDepartment();
			case EmployeePackage.STUDENT__FULL_TIME:
				return isFullTime() ? Boolean.TRUE : Boolean.FALSE;
			case EmployeePackage.STUDENT__OFFICE:
				return getOffice();
			case EmployeePackage.STUDENT__COOP:
				return isCoop() ? Boolean.TRUE : Boolean.FALSE;
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
			case EmployeePackage.STUDENT__SALARY:
				setSalary(((Integer)newValue).intValue());
				return;
			case EmployeePackage.STUDENT__BAND:
				setBand((Band)newValue);
				return;
			case EmployeePackage.STUDENT__NUMBER:
				setNumber(((Integer)newValue).intValue());
				return;
			case EmployeePackage.STUDENT__SECURITY_CLEARANCE:
				setSecurityClearance(((Boolean)newValue).booleanValue());
				return;
			case EmployeePackage.STUDENT__DEPARTMENT:
				setDepartment((Department)newValue);
				return;
			case EmployeePackage.STUDENT__FULL_TIME:
				setFullTime(((Boolean)newValue).booleanValue());
				return;
			case EmployeePackage.STUDENT__OFFICE:
				setOffice((Office)newValue);
				return;
			case EmployeePackage.STUDENT__COOP:
				setCoop(((Boolean)newValue).booleanValue());
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
			case EmployeePackage.STUDENT__SALARY:
				setSalary(SALARY_EDEFAULT);
				return;
			case EmployeePackage.STUDENT__BAND:
				setBand(BAND_EDEFAULT);
				return;
			case EmployeePackage.STUDENT__NUMBER:
				setNumber(NUMBER_EDEFAULT);
				return;
			case EmployeePackage.STUDENT__SECURITY_CLEARANCE:
				setSecurityClearance(SECURITY_CLEARANCE_EDEFAULT);
				return;
			case EmployeePackage.STUDENT__DEPARTMENT:
				setDepartment((Department)null);
				return;
			case EmployeePackage.STUDENT__FULL_TIME:
				setFullTime(FULL_TIME_EDEFAULT);
				return;
			case EmployeePackage.STUDENT__OFFICE:
				setOffice((Office)null);
				return;
			case EmployeePackage.STUDENT__COOP:
				setCoop(COOP_EDEFAULT);
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
			case EmployeePackage.STUDENT__SALARY:
				return salary != SALARY_EDEFAULT;
			case EmployeePackage.STUDENT__BAND:
				return band != BAND_EDEFAULT;
			case EmployeePackage.STUDENT__NUMBER:
				return number != NUMBER_EDEFAULT;
			case EmployeePackage.STUDENT__SECURITY_CLEARANCE:
				return securityClearance != SECURITY_CLEARANCE_EDEFAULT;
			case EmployeePackage.STUDENT__DEPARTMENT:
				return getDepartment() != null;
			case EmployeePackage.STUDENT__FULL_TIME:
				return fullTime != FULL_TIME_EDEFAULT;
			case EmployeePackage.STUDENT__OFFICE:
				return office != null;
			case EmployeePackage.STUDENT__COOP:
				return coop != COOP_EDEFAULT;
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
		result.append(" (coop: "); //$NON-NLS-1$
		result.append(coop);
		result.append(')');
		return result.toString();
	}

} //StudentImpl
