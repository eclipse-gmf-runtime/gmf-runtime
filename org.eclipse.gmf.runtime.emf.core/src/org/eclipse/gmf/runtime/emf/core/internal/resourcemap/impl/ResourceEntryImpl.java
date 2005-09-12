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
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry;
import org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceEntryImpl#getResource <em>Resource</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceEntryImpl#getChildPosition <em>Child Position</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceEntryImpl#getChildObject <em>Child Object</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceEntryImpl#getParentEntry <em>Parent Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResourceEntryImpl extends EObjectImpl implements ResourceEntry {
	/**
	 * The default value of the '{@link #getResource() <em>Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResource()
	 * @generated
	 * @ordered
	 */
	protected static final Resource RESOURCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getResource() <em>Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResource()
	 * @generated
	 * @ordered
	 */
	protected Resource resource = RESOURCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getChildPosition() <em>Child Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildPosition()
	 * @generated
	 * @ordered
	 */
	protected static final int CHILD_POSITION_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getChildPosition() <em>Child Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildPosition()
	 * @generated
	 * @ordered
	 */
	protected int childPosition = CHILD_POSITION_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResourceEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ResourceMapPackage.eINSTANCE.getResourceEntry();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResource(Resource newResource) {
		Resource oldResource = resource;
		resource = newResource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ResourceMapPackage.RESOURCE_ENTRY__RESOURCE, oldResource, resource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getChildPosition() {
		return childPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChildPosition(int newChildPosition) {
		int oldChildPosition = childPosition;
		childPosition = newChildPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ResourceMapPackage.RESOURCE_ENTRY__CHILD_POSITION, oldChildPosition, childPosition));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ResourceMapPackage.RESOURCE_ENTRY__CHILD_OBJECT, oldChildObject, childObject));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ResourceMapPackage.RESOURCE_ENTRY__CHILD_OBJECT, oldChildObject, childObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParentEntry getParentEntry() {
		ParentEntry parentEntry = basicGetParentEntry();
		return parentEntry == null ? null : (ParentEntry)eResolveProxy((InternalEObject)parentEntry);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ParentEntry basicGetParentEntry() {
		if (eContainer() instanceof ParentEntry) {
			return (ParentEntry) eContainer();
		}
		
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case ResourceMapPackage.RESOURCE_ENTRY__RESOURCE:
				return getResource();
			case ResourceMapPackage.RESOURCE_ENTRY__CHILD_POSITION:
				return new Integer(getChildPosition());
			case ResourceMapPackage.RESOURCE_ENTRY__CHILD_OBJECT:
				if (resolve) return getChildObject();
				return basicGetChildObject();
			case ResourceMapPackage.RESOURCE_ENTRY__PARENT_ENTRY:
				if (resolve) return getParentEntry();
				return basicGetParentEntry();
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
			case ResourceMapPackage.RESOURCE_ENTRY__RESOURCE:
				setResource((Resource)newValue);
				return;
			case ResourceMapPackage.RESOURCE_ENTRY__CHILD_POSITION:
				setChildPosition(((Integer)newValue).intValue());
				return;
			case ResourceMapPackage.RESOURCE_ENTRY__CHILD_OBJECT:
				setChildObject((EObject)newValue);
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
			case ResourceMapPackage.RESOURCE_ENTRY__RESOURCE:
				setResource(RESOURCE_EDEFAULT);
				return;
			case ResourceMapPackage.RESOURCE_ENTRY__CHILD_POSITION:
				setChildPosition(CHILD_POSITION_EDEFAULT);
				return;
			case ResourceMapPackage.RESOURCE_ENTRY__CHILD_OBJECT:
				setChildObject((EObject)null);
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
			case ResourceMapPackage.RESOURCE_ENTRY__RESOURCE:
				return RESOURCE_EDEFAULT == null ? resource != null : !RESOURCE_EDEFAULT.equals(resource);
			case ResourceMapPackage.RESOURCE_ENTRY__CHILD_POSITION:
				return childPosition != CHILD_POSITION_EDEFAULT;
			case ResourceMapPackage.RESOURCE_ENTRY__CHILD_OBJECT:
				return childObject != null;
			case ResourceMapPackage.RESOURCE_ENTRY__PARENT_ENTRY:
				return basicGetParentEntry() != null;
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
		result.append(" (resource: "); //$NON-NLS-1$
		result.append(resource);
		result.append(", childPosition: "); //$NON-NLS-1$
		result.append(childPosition);
		result.append(')');
		return result.toString();
	}

} //ResourceEntryImpl
