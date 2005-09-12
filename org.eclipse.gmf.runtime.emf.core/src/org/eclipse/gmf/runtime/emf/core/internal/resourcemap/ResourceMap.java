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
 * Physical mapping of child and parent units for a unit of a logical resource.
 * The resource map has a list of {@link ParentEntry}s with physical-URI
 * references to sub-units and a list of {@link ChildEntry}s with physical-URI
 * references to parent units.  The resource map also has a direct reference
 * to the root resource in the structure, so that each unit individually knows
 * the logical URI of the logical resource that owns it.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap#getRootMap <em>Root Map</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap#getParentEntries <em>Parent Entries</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap#getChildEntries <em>Child Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getResourceMap()
 * @model
 * @generated
 */
public interface ResourceMap extends EObject{
	/**
	 * Returns the value of the '<em><b>Root Map</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the reference to the root resource, via its resource map object.
	 * Only the root resource, itself, does not have this reference.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Root Map</em>' reference.
	 * @see #setRootMap(ResourceMap)
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getResourceMap_RootMap()
	 * @model
	 * @generated
	 */
	ResourceMap getRootMap();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap#getRootMap <em>Root Map</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Sets the root resource map reference.  Only the root resource should
	 * have a <code>null</code> value.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Root Map</em>' reference.
	 * @see #getRootMap()
	 * @generated
	 */
	void setRootMap(ResourceMap value);

	/**
	 * Returns the value of the '<em><b>Parent Entries</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the list of parent entries indicating child units.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Entries</em>' containment reference list.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getResourceMap_ParentEntries()
	 * @model type="org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry" containment="true" ordered="false"
	 * @generated
	 */
	EList getParentEntries();

	/**
	 * Returns the value of the '<em><b>Child Entries</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the list of child entries indicating parent units.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Entries</em>' containment reference list.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getResourceMap_ChildEntries()
	 * @model type="org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry" containment="true" ordered="false"
	 * @generated
	 */
	EList getChildEntries();

	/**
	 * <!-- begin-user-doc -->
	 * Obtains the parent entry for the specified <code>parent</code> element
	 * and containment <code>slot</code>.
	 * 
	 * @param parent a model element
	 * @param slot a containment reference of the <code>parent</code>'s class
	 * @return the matching parent entry object, or <code>null</code> if there
	 *     is none such
	 * <!-- end-user-doc -->
	 * @model parentRequired="true" slotRequired="true"
	 * @generated
	 */
	ParentEntry getParentEntry(EObject parent, EReference slot);

	/**
	 * <!-- begin-user-doc -->
	 * Obtains the resource entry for the specified <code>child</code> element
	 * in whatever parent element it is currently defined.
	 * 
	 * @param child a model element
	 * @return the matching resource entry object, or <code>null</code> if there
	 *     is none such
	 * <!-- end-user-doc -->
	 * @model childRequired="true"
	 * @generated
	 */
	ResourceEntry getResourceEntry(EObject child);

	/**
	 * <!-- begin-user-doc -->
	 * Obtains the resource map of the parent unit of the speified
	 * <code>child</code> element.
	 * 
	 * @param child a model element
	 * @return the parent unit's resource map (as a handle on the parent
	 *     resource), or <code>null</code> if there is none
	 * <!-- end-user-doc -->
	 * @model childRequired="true"
	 * @generated
	 */
	ResourceMap getParentMap(EObject child);

	/**
	 * <!-- begin-user-doc -->
	 * Obtains the child entry for the speified <code>child</code> element.
	 * 
	 * @param child a model element
	 * @return the child entry, or <code>null</code> if there is none
	 * <!-- end-user-doc -->
	 * @model childRequired="true"
	 * @generated
	 */
	ChildEntry getChildEntry(EObject child);

} // ResourceMap
