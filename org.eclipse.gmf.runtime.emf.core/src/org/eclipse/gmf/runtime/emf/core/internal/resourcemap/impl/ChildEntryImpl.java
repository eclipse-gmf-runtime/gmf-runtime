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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Child Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ChildEntryImpl#getChildObject <em>Child Object</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ChildEntryImpl#getParentMap <em>Parent Map</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ChildEntryImpl extends EObjectImpl implements ChildEntry {
	/**
	 * The cached value of the '{@link #getChildObject() <em>Child Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildObject()
	 * @generated
	 * @ordered
	 */
	protected EObject childObject = null;

	/**
	 * The cached value of the '{@link #getParentMap() <em>Parent Map</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentMap()
	 * @generated
	 * @ordered
	 */
	protected ResourceMap parentMap = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChildEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ResourceMapPackage.eINSTANCE.getChildEntry();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getChildObject() {
		if (childObject != null && childObject.eIsProxy()) {
			EObject oldChildObject = childObject;
			childObject = eResolveProxy((InternalEObject)childObject);
			if (childObject != oldChildObject) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ResourceMapPackage.CHILD_ENTRY__CHILD_OBJECT, oldChildObject, childObject));
			}
		}
		return childObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetChildObject() {
		return childObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChildObject(EObject newChildObject) {
		EObject oldChildObject = childObject;
		childObject = newChildObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ResourceMapPackage.CHILD_ENTRY__CHILD_OBJECT, oldChildObject, childObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceMap getParentMap() {
		if (parentMap != null && parentMap.eIsProxy()) {
			ResourceMap oldParentMap = parentMap;
			parentMap = (ResourceMap)eResolveProxy((InternalEObject)parentMap);
			if (parentMap != oldParentMap) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ResourceMapPackage.CHILD_ENTRY__PARENT_MAP, oldParentMap, parentMap));
			}
		}
		return parentMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceMap basicGetParentMap() {
		return parentMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentMap(ResourceMap newParentMap) {
		ResourceMap oldParentMap = parentMap;
		parentMap = newParentMap;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ResourceMapPackage.CHILD_ENTRY__PARENT_MAP, oldParentMap, parentMap));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case ResourceMapPackage.CHILD_ENTRY__CHILD_OBJECT:
				if (resolve) return getChildObject();
				return basicGetChildObject();
			case ResourceMapPackage.CHILD_ENTRY__PARENT_MAP:
				if (resolve) return getParentMap();
				return basicGetParentMap();
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
			case ResourceMapPackage.CHILD_ENTRY__CHILD_OBJECT:
				setChildObject((EObject)newValue);
				return;
			case ResourceMapPackage.CHILD_ENTRY__PARENT_MAP:
				setParentMap((ResourceMap)newValue);
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
			case ResourceMapPackage.CHILD_ENTRY__CHILD_OBJECT:
				setChildObject((EObject)null);
				return;
			case ResourceMapPackage.CHILD_ENTRY__PARENT_MAP:
				setParentMap((ResourceMap)null);
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
			case ResourceMapPackage.CHILD_ENTRY__CHILD_OBJECT:
				return childObject != null;
			case ResourceMapPackage.CHILD_ENTRY__PARENT_MAP:
				return parentMap != null;
		}
		return eDynamicIsSet(eFeature);
	}

} //ChildEntryImpl
