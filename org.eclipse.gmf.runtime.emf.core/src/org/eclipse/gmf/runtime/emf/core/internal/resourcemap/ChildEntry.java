/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.resourcemap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * An entry in the physical resource map that links a separate element (as a
 * root of a sub-unit) to its parent unit.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry#getChildObject <em>Child Object</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry#getParentMap <em>Parent Map</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getChildEntry()
 * @model
 * @generated
 */
public interface ChildEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Child Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the child object for which I indicate the parent unit.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Object</em>' reference.
	 * @see #setChildObject(EObject)
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getChildEntry_ChildObject()
	 * @model required="true"
	 * @generated
	 */
	EObject getChildObject();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry#getChildObject <em>Child Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Sets the child object for which I indicate the parent unit.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Object</em>' reference.
	 * @see #getChildObject()
	 * @generated
	 */
	void setChildObject(EObject value);

	/**
	 * Returns the value of the '<em><b>Parent Map</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the resource map of the child's parent unit.  This serves as
	 * a handle on the parent unit, itself.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Map</em>' reference.
	 * @see #setParentMap(ResourceMap)
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getChildEntry_ParentMap()
	 * @model required="true"
	 * @generated
	 */
	ResourceMap getParentMap();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry#getParentMap <em>Parent Map</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Sets the resource map of the child's parent unit.  This serves as
	 * a handle on the parent unit, itself.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Map</em>' reference.
	 * @see #getParentMap()
	 * @generated
	 */
	void setParentMap(ResourceMap value);

} // ChildEntry
