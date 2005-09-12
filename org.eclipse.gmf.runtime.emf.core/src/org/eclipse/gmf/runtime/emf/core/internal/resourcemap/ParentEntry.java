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

package org.eclipse.gmf.runtime.emf.core.internal.resourcemap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * An entry in the physical resource map for an element in the unit that has
 * one or more separate children in some containment reference.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getParentObject <em>Parent Object</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getChildSlot <em>Child Slot</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getResourceEntries <em>Resource Entries</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getResourceMap <em>Resource Map</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getParentEntry()
 * @model
 * @generated
 */
public interface ParentEntry extends EObject{
	/**
	 * Returns the value of the '<em><b>Parent Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the model element that is the parent of some separate children.
	 * For the parent entry representing the logical resource, itself (whose
	 * roots may be separate), the parent is <code>null</code>.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Object</em>' reference.
	 * @see #setParentObject(EObject)
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getParentEntry_ParentObject()
	 * @model
	 * @generated
	 */
	EObject getParentObject();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getParentObject <em>Parent Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Sets the model element that is the parent of some separate children.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Object</em>' reference.
	 * @see #getParentObject()
	 * @generated
	 */
	void setParentObject(EObject value);

	/**
	 * Returns the value of the '<em><b>Child Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the containment reference of the
	 * {@linkplain #getParentObject() parent} element that contains one or more
	 * separate children.
	 * For the parent entry representing the logical resource, itself (whose
	 * roots may be separate), the child slot is <code>null</code>.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Slot</em>' reference.
	 * @see #setChildSlot(EReference)
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getParentEntry_ChildSlot()
	 * @model
	 * @generated
	 */
	EReference getChildSlot();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getChildSlot <em>Child Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Sets the containment reference of the
	 * {@linkplain #getParentObject() parent} element that contains one or more
	 * separate children.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Slot</em>' reference.
	 * @see #getChildSlot()
	 * @generated
	 */
	void setChildSlot(EReference value);

	/**
	 * Returns the value of the '<em><b>Resource Entries</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the list of entries for the separate children of the parent in
	 * the slot.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Entries</em>' containment reference list.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getParentEntry_ResourceEntries()
	 * @model type="org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry" containment="true" required="true"
	 * @generated
	 */
	EList getResourceEntries();

	/**
	 * Returns the value of the '<em><b>Resource Map</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the resource map that contains me.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Map</em>' reference.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getParentEntry_ResourceMap()
	 * @model transient="true" changeable="false" volatile="true"
	 * @generated
	 */
	ResourceMap getResourceMap();

	/**
	 * <!-- begin-user-doc -->
	 * Obtains the resource entry within me for the specified <code>child</code>
	 * of my parent element.
	 * 
	 * @param child a child of my parent element
	 * @return the resource entry, or <code>null</code> if the
	 *    <code>child</code> is not containment by this parent in this slot or
	 *    if it is not separate
	 * <!-- end-user-doc -->
	 * @model childRequired="true"
	 * @generated
	 */
	ResourceEntry getResourceEntry(EObject child);

} // ParentEntry
