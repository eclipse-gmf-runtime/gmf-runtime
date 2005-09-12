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

import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Map</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapImpl#getRootMap <em>Root Map</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapImpl#getParentEntries <em>Parent Entries</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapImpl#getChildEntries <em>Child Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResourceMapImpl extends EObjectImpl implements ResourceMap {
	/**
	 * The cached value of the '{@link #getRootMap() <em>Root Map</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRootMap()
	 * @generated
	 * @ordered
	 */
	protected ResourceMap rootMap = null;

	/**
	 * The cached value of the '{@link #getParentEntries() <em>Parent Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentEntries()
	 * @generated
	 * @ordered
	 */
	protected EList parentEntries = null;

	/**
	 * The cached value of the '{@link #getChildEntries() <em>Child Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildEntries()
	 * @generated
	 * @ordered
	 */
	protected EList childEntries = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResourceMapImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ResourceMapPackage.eINSTANCE.getResourceMap();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceMap getRootMap() {
		if (rootMap != null && rootMap.eIsProxy()) {
			ResourceMap oldRootMap = rootMap;
			rootMap = (ResourceMap)eResolveProxy((InternalEObject)rootMap);
			if (rootMap != oldRootMap) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ResourceMapPackage.RESOURCE_MAP__ROOT_MAP, oldRootMap, rootMap));
			}
		}
		return rootMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceMap basicGetRootMap() {
		return rootMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRootMap(ResourceMap newRootMap) {
		ResourceMap oldRootMap = rootMap;
		rootMap = newRootMap;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ResourceMapPackage.RESOURCE_MAP__ROOT_MAP, oldRootMap, rootMap));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getParentEntries() {
		if (parentEntries == null) {
			parentEntries = new EObjectContainmentEList(ParentEntry.class, this, ResourceMapPackage.RESOURCE_MAP__PARENT_ENTRIES);
		}
		return parentEntries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getChildEntries() {
		if (childEntries == null) {
			childEntries = new EObjectContainmentEList(ChildEntry.class, this, ResourceMapPackage.RESOURCE_MAP__CHILD_ENTRIES);
		}
		return childEntries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ParentEntry getParentEntry(EObject parent, EReference slot) {
		ParentEntry result = null;
		
		for (Iterator iter = getParentEntries().iterator();
				(result == null) && iter.hasNext();) {
			
			ParentEntry next = (ParentEntry) iter.next();
			
			if ((next.getParentObject() == parent) && (next.getChildSlot() == slot)) {
				result = next;
			}
		}
		
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ResourceEntry getResourceEntry(EObject child) {
		ResourceEntry result = null;
		
		// do not attempt to find the correct parent entry by the child's
		//   eContainer and eContainmentFeature, as it may not have either
		//   of these at this moment, or they may be different than expected
		for (Iterator iter = getParentEntries().iterator();
				(result == null) && iter.hasNext();) {
			
			ParentEntry parentEntry = (ParentEntry) iter.next();
			
			result = parentEntry.getResourceEntry(child);
		}
		
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ResourceMap getParentMap(EObject child) {
		ResourceMap result = null;
		ChildEntry childEntry = getChildEntry(child);
		
		if (childEntry != null) {
			result = childEntry.getParentMap();
		}
		
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ChildEntry getChildEntry(EObject child) {
		ChildEntry result = null;
		
		for (Iterator iter = getChildEntries().iterator();
				(result == null) && iter.hasNext();) {
			
			ChildEntry next = (ChildEntry) iter.next();
			
			if (next.getChildObject() == child) {
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
				case ResourceMapPackage.RESOURCE_MAP__PARENT_ENTRIES:
					return ((InternalEList)getParentEntries()).basicRemove(otherEnd, msgs);
				case ResourceMapPackage.RESOURCE_MAP__CHILD_ENTRIES:
					return ((InternalEList)getChildEntries()).basicRemove(otherEnd, msgs);
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
			case ResourceMapPackage.RESOURCE_MAP__ROOT_MAP:
				if (resolve) return getRootMap();
				return basicGetRootMap();
			case ResourceMapPackage.RESOURCE_MAP__PARENT_ENTRIES:
				return getParentEntries();
			case ResourceMapPackage.RESOURCE_MAP__CHILD_ENTRIES:
				return getChildEntries();
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
			case ResourceMapPackage.RESOURCE_MAP__ROOT_MAP:
				setRootMap((ResourceMap)newValue);
				return;
			case ResourceMapPackage.RESOURCE_MAP__PARENT_ENTRIES:
				getParentEntries().clear();
				getParentEntries().addAll((Collection)newValue);
				return;
			case ResourceMapPackage.RESOURCE_MAP__CHILD_ENTRIES:
				getChildEntries().clear();
				getChildEntries().addAll((Collection)newValue);
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
			case ResourceMapPackage.RESOURCE_MAP__ROOT_MAP:
				setRootMap((ResourceMap)null);
				return;
			case ResourceMapPackage.RESOURCE_MAP__PARENT_ENTRIES:
				getParentEntries().clear();
				return;
			case ResourceMapPackage.RESOURCE_MAP__CHILD_ENTRIES:
				getChildEntries().clear();
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
			case ResourceMapPackage.RESOURCE_MAP__ROOT_MAP:
				return rootMap != null;
			case ResourceMapPackage.RESOURCE_MAP__PARENT_ENTRIES:
				return parentEntries != null && !parentEntries.isEmpty();
			case ResourceMapPackage.RESOURCE_MAP__CHILD_ENTRIES:
				return childEntries != null && !childEntries.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

} //ResourceMapImpl
