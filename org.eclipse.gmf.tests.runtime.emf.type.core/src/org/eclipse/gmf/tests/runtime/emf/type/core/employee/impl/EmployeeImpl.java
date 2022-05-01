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
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EModelElementImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Employee</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl#getSalary <em>Salary</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl#getBand <em>Band</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl#getNumber <em>Number</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl#isSecurityClearance <em>Security Clearance</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl#getDepartment <em>Department</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl#isFullTime <em>Full Time</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.EmployeeImpl#getOffice <em>Office</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EmployeeImpl extends EModelElementImpl implements Employee {
	/**
	 * The default value of the '{@link #getSalary() <em>Salary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalary()
	 * @generated
	 * @ordered
	 */
	protected static final int SALARY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSalary() <em>Salary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalary()
	 * @generated
	 * @ordered
	 */
	protected int salary = SALARY_EDEFAULT;

	/**
	 * The default value of the '{@link #getBand() <em>Band</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBand()
	 * @generated
	 * @ordered
	 */
	protected static final Band BAND_EDEFAULT = Band.JUNIOR_LITERAL;

	/**
	 * The cached value of the '{@link #getBand() <em>Band</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBand()
	 * @generated
	 * @ordered
	 */
	protected Band band = BAND_EDEFAULT;

	/**
	 * The default value of the '{@link #getNumber() <em>Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumber()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumber() <em>Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumber()
	 * @generated
	 * @ordered
	 */
	protected int number = NUMBER_EDEFAULT;

	/**
	 * The default value of the '{@link #isSecurityClearance() <em>Security Clearance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSecurityClearance()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SECURITY_CLEARANCE_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isSecurityClearance() <em>Security Clearance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSecurityClearance()
	 * @generated
	 * @ordered
	 */
	protected static final int SECURITY_CLEARANCE_EFLAG = 1 << 8;

	/**
	 * The default value of the '{@link #isFullTime() <em>Full Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFullTime()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FULL_TIME_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isFullTime() <em>Full Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFullTime()
	 * @generated
	 * @ordered
	 */
	protected static final int FULL_TIME_EFLAG = 1 << 9;

	/**
	 * The cached value of the '{@link #getOffice() <em>Office</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOffice()
	 * @generated
	 * @ordered
	 */
	protected Office office = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EmployeeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return EmployeePackage.Literals.EMPLOYEE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSalary() {
		return salary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSalary(int newSalary) {
		int oldSalary = salary;
		salary = newSalary;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.EMPLOYEE__SALARY, oldSalary, salary));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Band getBand() {
		return band;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBand(Band newBand) {
		Band oldBand = band;
		band = newBand == null ? BAND_EDEFAULT : newBand;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.EMPLOYEE__BAND, oldBand, band));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumber(int newNumber) {
		int oldNumber = number;
		number = newNumber;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.EMPLOYEE__NUMBER, oldNumber, number));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Office getOffice() {
		return office;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOffice(Office newOffice, NotificationChain msgs) {
		Office oldOffice = office;
		office = newOffice;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EmployeePackage.EMPLOYEE__OFFICE, oldOffice, newOffice);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOffice(Office newOffice) {
		if (newOffice != office) {
			NotificationChain msgs = null;
			if (office != null)
				msgs = ((InternalEObject)office).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EmployeePackage.EMPLOYEE__OFFICE, null, msgs);
			if (newOffice != null)
				msgs = ((InternalEObject)newOffice).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EmployeePackage.EMPLOYEE__OFFICE, null, msgs);
			msgs = basicSetOffice(newOffice, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.EMPLOYEE__OFFICE, newOffice, newOffice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EmployeePackage.EMPLOYEE__DEPARTMENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetDepartment((Department)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EmployeePackage.EMPLOYEE__DEPARTMENT:
				return basicSetDepartment(null, msgs);
			case EmployeePackage.EMPLOYEE__OFFICE:
				return basicSetOffice(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case EmployeePackage.EMPLOYEE__DEPARTMENT:
				return eInternalContainer().eInverseRemove(this, EmployeePackage.DEPARTMENT__MEMBERS, Department.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EmployeePackage.EMPLOYEE__SALARY:
				return Integer.valueOf(getSalary());
			case EmployeePackage.EMPLOYEE__BAND:
				return getBand();
			case EmployeePackage.EMPLOYEE__NUMBER:
				return Integer.valueOf(getNumber());
			case EmployeePackage.EMPLOYEE__SECURITY_CLEARANCE:
				return isSecurityClearance() ? Boolean.TRUE : Boolean.FALSE;
			case EmployeePackage.EMPLOYEE__DEPARTMENT:
				return getDepartment();
			case EmployeePackage.EMPLOYEE__FULL_TIME:
				return isFullTime() ? Boolean.TRUE : Boolean.FALSE;
			case EmployeePackage.EMPLOYEE__OFFICE:
				return getOffice();
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
			case EmployeePackage.EMPLOYEE__SALARY:
				setSalary(((Integer)newValue).intValue());
				return;
			case EmployeePackage.EMPLOYEE__BAND:
				setBand((Band)newValue);
				return;
			case EmployeePackage.EMPLOYEE__NUMBER:
				setNumber(((Integer)newValue).intValue());
				return;
			case EmployeePackage.EMPLOYEE__SECURITY_CLEARANCE:
				setSecurityClearance(((Boolean)newValue).booleanValue());
				return;
			case EmployeePackage.EMPLOYEE__DEPARTMENT:
				setDepartment((Department)newValue);
				return;
			case EmployeePackage.EMPLOYEE__FULL_TIME:
				setFullTime(((Boolean)newValue).booleanValue());
				return;
			case EmployeePackage.EMPLOYEE__OFFICE:
				setOffice((Office)newValue);
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
			case EmployeePackage.EMPLOYEE__SALARY:
				setSalary(SALARY_EDEFAULT);
				return;
			case EmployeePackage.EMPLOYEE__BAND:
				setBand(BAND_EDEFAULT);
				return;
			case EmployeePackage.EMPLOYEE__NUMBER:
				setNumber(NUMBER_EDEFAULT);
				return;
			case EmployeePackage.EMPLOYEE__SECURITY_CLEARANCE:
				setSecurityClearance(SECURITY_CLEARANCE_EDEFAULT);
				return;
			case EmployeePackage.EMPLOYEE__DEPARTMENT:
				setDepartment((Department)null);
				return;
			case EmployeePackage.EMPLOYEE__FULL_TIME:
				setFullTime(FULL_TIME_EDEFAULT);
				return;
			case EmployeePackage.EMPLOYEE__OFFICE:
				setOffice((Office)null);
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
			case EmployeePackage.EMPLOYEE__SALARY:
				return salary != SALARY_EDEFAULT;
			case EmployeePackage.EMPLOYEE__BAND:
				return band != BAND_EDEFAULT;
			case EmployeePackage.EMPLOYEE__NUMBER:
				return number != NUMBER_EDEFAULT;
			case EmployeePackage.EMPLOYEE__SECURITY_CLEARANCE:
				return ((eFlags & SECURITY_CLEARANCE_EFLAG) != 0) != SECURITY_CLEARANCE_EDEFAULT;
			case EmployeePackage.EMPLOYEE__DEPARTMENT:
				return getDepartment() != null;
			case EmployeePackage.EMPLOYEE__FULL_TIME:
				return ((eFlags & FULL_TIME_EFLAG) != 0) != FULL_TIME_EDEFAULT;
			case EmployeePackage.EMPLOYEE__OFFICE:
				return office != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSecurityClearance() {
		return (eFlags & SECURITY_CLEARANCE_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSecurityClearance(boolean newSecurityClearance) {
		boolean oldSecurityClearance = (eFlags & SECURITY_CLEARANCE_EFLAG) != 0;
		if (newSecurityClearance) eFlags |= SECURITY_CLEARANCE_EFLAG; else eFlags &= ~SECURITY_CLEARANCE_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.EMPLOYEE__SECURITY_CLEARANCE, oldSecurityClearance, newSecurityClearance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Department getDepartment() {
		if (eContainerFeatureID() != EmployeePackage.EMPLOYEE__DEPARTMENT) return null;
		return (Department)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDepartment(Department newDepartment, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newDepartment, EmployeePackage.EMPLOYEE__DEPARTMENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDepartment(Department newDepartment) {
		if (newDepartment != eInternalContainer() || (eContainerFeatureID() != EmployeePackage.EMPLOYEE__DEPARTMENT && newDepartment != null)) {
			if (EcoreUtil.isAncestor(this, newDepartment))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDepartment != null)
				msgs = ((InternalEObject)newDepartment).eInverseAdd(this, EmployeePackage.DEPARTMENT__MEMBERS, Department.class, msgs);
			msgs = basicSetDepartment(newDepartment, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.EMPLOYEE__DEPARTMENT, newDepartment, newDepartment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFullTime() {
		return (eFlags & FULL_TIME_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFullTime(boolean newFullTime) {
		boolean oldFullTime = (eFlags & FULL_TIME_EFLAG) != 0;
		if (newFullTime) eFlags |= FULL_TIME_EFLAG; else eFlags &= ~FULL_TIME_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.EMPLOYEE__FULL_TIME, oldFullTime, newFullTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (salary: "); //$NON-NLS-1$
		result.append(salary);
		result.append(", band: "); //$NON-NLS-1$
		result.append(band);
		result.append(", number: "); //$NON-NLS-1$
		result.append(number);
		result.append(", securityClearance: "); //$NON-NLS-1$
		result.append((eFlags & SECURITY_CLEARANCE_EFLAG) != 0);
		result.append(", fullTime: "); //$NON-NLS-1$
		result.append((eFlags & FULL_TIME_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

} //EmployeeImpl
