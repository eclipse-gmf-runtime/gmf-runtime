/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parent Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ParentEntryImpl#getParentObject <em>Parent Object</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ParentEntryImpl#getChildSlot <em>Child Slot</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ParentEntryImpl#getResourceEntries <em>Resource Entries</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ParentEntryImpl#getResourceMap <em>Resource Map</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParentEntryImpl extends EObjectImpl implements ParentEntry {
	/**
	 * The cached value of the '{@link #getParentObject() <em>Parent Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentObject()
	 * @generated
	 * @ordered
	 */
	protected EObject parentObject = null;

	/**
	 * The cached value of the '{@link #getChildSlot() <em>Child Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildSlot()
	 * @generated
	 * @ordered
	 */
	protected EReference childSlot = null;

	/**
	 * The cached value of the '{@link #getResourceEntries() <em>Resource Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourceEntries()
	 * @generated
	 * @ordered
	 */
	protected EList resourceEntries = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParentEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ResourceMapPackage.eINSTANCE.getParentEntry();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getParentObject() {
		if (parentObject != null && parentObject.eIsProxy()) {
			EObject oldParentObject = parentObject;
			parentObject = eResolveProxy((InternalEObject)parentObject);
			if (parentObject != oldParentObject) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ResourceMapPackage.PARENT_ENTRY__PARENT_OBJECT, oldParentObject, parentObject));
			}
		}
		return parentObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetParentObject() {
		return parentObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentObject(EObject newParentObject) {
		EObject oldParentObject = parentObject;
		parentObject = newParentObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ResourceMapPackage.PARENT_ENTRY__PARENT_OBJECT, oldParentObject, parentObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChildSlot() {
		if (childSlot != null && childSlot.eIsProxy()) {
			EReference oldChildSlot = childSlot;
			childSlot = (EReference)eResolveProxy((InternalEObject)childSlot);
			if (childSlot != oldChildSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ResourceMapPackage.PARENT_ENTRY__CHILD_SLOT, oldChildSlot, childSlot));
			}
		}
		return childSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference basicGetChildSlot() {
		return childSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChildSlot(EReference newChildSlot) {
		EReference oldChildSlot = childSlot;
		childSlot = newChildSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ResourceMapPackage.PARENT_ENTRY__CHILD_SLOT, oldChildSlot, childSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getResourceEntries() {
		if (resourceEntries == null) {
			resourceEntries = new EObjectContainmentEList(ResourceEntry.class, this, ResourceMapPackage.PARENT_ENTRY__RESOURCE_ENTRIES);
		}
		return resourceEntries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceMap getResourceMap() {
		ResourceMap resourceMap = basicGetResourceMap();
		return resourceMap == null ? null : (ResourceMap)eResolveProxy((InternalEObject)resourceMap);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ResourceMap basicGetResourceMap() {
		if (eContainer() instanceof ResourceMap) {
			return (ResourceMap) eContainer();
		}
		
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ResourceEntry getResourceEntry(EObject child) {
		ResourceEntry result = null;
		
		for (Iterator iter = getResourceEntries().iterator();
				(result == null) && iter.hasNext();) {
			
			ResourceEntryImpl next = (ResourceEntryImpl) iter.next();
			
			if (next.basicGetChildObject() == child) {
				result = next;
			}
		}
		
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case ResourceMapPackage.PARENT_ENTRY__RESOURCE_ENTRIES:
					return ((InternalEList)getResourceEntries()).basicRemove(otherEnd, msgs);
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
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case ResourceMapPackage.PARENT_ENTRY__PARENT_OBJECT:
				if (resolve) return getParentObject();
				return basicGetParentObject();
			case ResourceMapPackage.PARENT_ENTRY__CHILD_SLOT:
				if (resolve) return getChildSlot();
				return basicGetChildSlot();
			case ResourceMapPackage.PARENT_ENTRY__RESOURCE_ENTRIES:
				return getResourceEntries();
			case ResourceMapPackage.PARENT_ENTRY__RESOURCE_MAP:
				if (resolve) return getResourceMap();
				return basicGetResourceMap();
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
			case ResourceMapPackage.PARENT_ENTRY__PARENT_OBJECT:
				setParentObject((EObject)newValue);
				return;
			case ResourceMapPackage.PARENT_ENTRY__CHILD_SLOT:
				setChildSlot((EReference)newValue);
				return;
			case ResourceMapPackage.PARENT_ENTRY__RESOURCE_ENTRIES:
				getResourceEntries().clear();
				getResourceEntries().addAll((Collection)newValue);
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
			case ResourceMapPackage.PARENT_ENTRY__PARENT_OBJECT:
				setParentObject((EObject)null);
				return;
			case ResourceMapPackage.PARENT_ENTRY__CHILD_SLOT:
				setChildSlot((EReference)null);
				return;
			case ResourceMapPackage.PARENT_ENTRY__RESOURCE_ENTRIES:
				getResourceEntries().clear();
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
			case ResourceMapPackage.PARENT_ENTRY__PARENT_OBJECT:
				return parentObject != null;
			case ResourceMapPackage.PARENT_ENTRY__CHILD_SLOT:
				return childSlot != null;
			case ResourceMapPackage.PARENT_ENTRY__RESOURCE_ENTRIES:
				return resourceEntries != null && !resourceEntries.isEmpty();
			case ResourceMapPackage.PARENT_ENTRY__RESOURCE_MAP:
				return basicGetResourceMap() != null;
		}
		return eDynamicIsSet(eFeature);
	}

} //ParentEntryImpl
