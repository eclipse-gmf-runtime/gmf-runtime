/**
 * <copyright>
 * </copyright>
 *
 * $Id: CustomerImpl.java,v 1.1 2006/03/07 02:40:36 ldamus Exp $
 */
package org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EModelElementImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Customer</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.CustomerImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.CustomerImpl#getAddress <em>Address</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.CustomerImpl#getRepresentatives <em>Representatives</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.CustomerImpl#getSubsidiaries <em>Subsidiaries</em>}</li>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.core.employee.impl.CustomerImpl#getParent <em>Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CustomerImpl extends EModelElementImpl implements Customer {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getAddress() <em>Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddress()
	 * @generated
	 * @ordered
	 */
	protected static final String ADDRESS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAddress() <em>Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddress()
	 * @generated
	 * @ordered
	 */
	protected String address = ADDRESS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRepresentatives() <em>Representatives</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepresentatives()
	 * @generated
	 * @ordered
	 */
	protected EList representatives = null;

	/**
	 * The cached value of the '{@link #getSubsidiaries() <em>Subsidiaries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubsidiaries()
	 * @generated
	 * @ordered
	 */
	protected EList subsidiaries = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CustomerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return EmployeePackage.Literals.CUSTOMER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.CUSTOMER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAddress(String newAddress) {
		String oldAddress = address;
		address = newAddress;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.CUSTOMER__ADDRESS, oldAddress, address));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getRepresentatives() {
		if (representatives == null) {
			representatives = new EObjectContainmentWithInverseEList(Client.class, this, EmployeePackage.CUSTOMER__REPRESENTATIVES, EmployeePackage.CLIENT__REPRESENTS);
		}
		return representatives;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getSubsidiaries() {
		if (subsidiaries == null) {
			subsidiaries = new EObjectContainmentWithInverseEList(Customer.class, this, EmployeePackage.CUSTOMER__SUBSIDIARIES, EmployeePackage.CUSTOMER__PARENT);
		}
		return subsidiaries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Customer getParent() {
		if (eContainerFeatureID != EmployeePackage.CUSTOMER__PARENT) return null;
		return (Customer)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParent(Customer newParent) {
		if (newParent != eInternalContainer() || (eContainerFeatureID != EmployeePackage.CUSTOMER__PARENT && newParent != null)) {
			if (EcoreUtil.isAncestor(this, newParent))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParent != null)
				msgs = ((InternalEObject)newParent).eInverseAdd(this, EmployeePackage.CUSTOMER__SUBSIDIARIES, Customer.class, msgs);
			msgs = eBasicSetContainer((InternalEObject)newParent, EmployeePackage.CUSTOMER__PARENT, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmployeePackage.CUSTOMER__PARENT, newParent, newParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EmployeePackage.CUSTOMER__REPRESENTATIVES:
				return ((InternalEList)getRepresentatives()).basicAdd(otherEnd, msgs);
			case EmployeePackage.CUSTOMER__SUBSIDIARIES:
				return ((InternalEList)getSubsidiaries()).basicAdd(otherEnd, msgs);
			case EmployeePackage.CUSTOMER__PARENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return eBasicSetContainer(otherEnd, EmployeePackage.CUSTOMER__PARENT, msgs);
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
			case EmployeePackage.CUSTOMER__REPRESENTATIVES:
				return ((InternalEList)getRepresentatives()).basicRemove(otherEnd, msgs);
			case EmployeePackage.CUSTOMER__SUBSIDIARIES:
				return ((InternalEList)getSubsidiaries()).basicRemove(otherEnd, msgs);
			case EmployeePackage.CUSTOMER__PARENT:
				return eBasicSetContainer(null, EmployeePackage.CUSTOMER__PARENT, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID) {
			case EmployeePackage.CUSTOMER__PARENT:
				return eInternalContainer().eInverseRemove(this, EmployeePackage.CUSTOMER__SUBSIDIARIES, Customer.class, msgs);
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
			case EmployeePackage.CUSTOMER__NAME:
				return getName();
			case EmployeePackage.CUSTOMER__ADDRESS:
				return getAddress();
			case EmployeePackage.CUSTOMER__REPRESENTATIVES:
				return getRepresentatives();
			case EmployeePackage.CUSTOMER__SUBSIDIARIES:
				return getSubsidiaries();
			case EmployeePackage.CUSTOMER__PARENT:
				return getParent();
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
			case EmployeePackage.CUSTOMER__NAME:
				setName((String)newValue);
				return;
			case EmployeePackage.CUSTOMER__ADDRESS:
				setAddress((String)newValue);
				return;
			case EmployeePackage.CUSTOMER__REPRESENTATIVES:
				getRepresentatives().clear();
				getRepresentatives().addAll((Collection)newValue);
				return;
			case EmployeePackage.CUSTOMER__SUBSIDIARIES:
				getSubsidiaries().clear();
				getSubsidiaries().addAll((Collection)newValue);
				return;
			case EmployeePackage.CUSTOMER__PARENT:
				setParent((Customer)newValue);
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
			case EmployeePackage.CUSTOMER__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EmployeePackage.CUSTOMER__ADDRESS:
				setAddress(ADDRESS_EDEFAULT);
				return;
			case EmployeePackage.CUSTOMER__REPRESENTATIVES:
				getRepresentatives().clear();
				return;
			case EmployeePackage.CUSTOMER__SUBSIDIARIES:
				getSubsidiaries().clear();
				return;
			case EmployeePackage.CUSTOMER__PARENT:
				setParent((Customer)null);
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
			case EmployeePackage.CUSTOMER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EmployeePackage.CUSTOMER__ADDRESS:
				return ADDRESS_EDEFAULT == null ? address != null : !ADDRESS_EDEFAULT.equals(address);
			case EmployeePackage.CUSTOMER__REPRESENTATIVES:
				return representatives != null && !representatives.isEmpty();
			case EmployeePackage.CUSTOMER__SUBSIDIARIES:
				return subsidiaries != null && !subsidiaries.isEmpty();
			case EmployeePackage.CUSTOMER__PARENT:
				return getParent() != null;
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
		result.append(" (name: "); //$NON-NLS-1$
		result.append(name);
		result.append(", address: "); //$NON-NLS-1$
		result.append(address);
		result.append(')');
		return result.toString();
	}

} //CustomerImpl
