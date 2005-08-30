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
import org.eclipse.emf.ecore.resource.Resource;

/**
 * <!-- begin-user-doc -->
 * An entry in a {@link ParentEntry} in the physical resource map, that
 * indicates which separate child of the parent element fits into the
 * containment reference at what position.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getResource <em>Resource</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getChildPosition <em>Child Position</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getChildObject <em>Child Object</em>}</li>
 *   <li>{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getParentEntry <em>Parent Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getResourceEntry()
 * @model
 * @generated
 */
public interface ResourceEntry extends EObject{
	/**
	 * Returns the value of the '<em><b>Resource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the transient reference to the sub-unit resource containing the
	 * separate child element.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource</em>' attribute.
	 * @see #setResource(Resource)
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getResourceEntry_Resource()
	 * @model transient="true"
	 * @generated
	 */
	Resource getResource();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getResource <em>Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Sets the transient reference to the sub-unit resource containing the
	 * separate child element.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource</em>' attribute.
	 * @see #getResource()
	 * @generated
	 */
	void setResource(Resource value);

	/**
	 * Returns the value of the '<em><b>Child Position</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Gets the index in the containment reference at which this child resides.
	 * The index is <code>-1</code> (the default value) for scalar references.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Position</em>' attribute.
	 * @see #setChildPosition(int)
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getResourceEntry_ChildPosition()
	 * @model default="-1"
	 * @generated
	 */
	int getChildPosition();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getChildPosition <em>Child Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Sets the index in the containment reference at which this child resides,
	 * or <code>-1</code> (the default value) for scalar references.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Position</em>' attribute.
	 * @see #getChildPosition()
	 * @generated
	 */
	void setChildPosition(int value);

	/**
	 * Returns the value of the '<em><b>Child Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the separate child object.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Object</em>' reference.
	 * @see #setChildObject(EObject)
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getResourceEntry_ChildObject()
	 * @model required="true"
	 * @generated
	 */
	EObject getChildObject();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getChildObject <em>Child Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Sets the separate child object.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Object</em>' reference.
	 * @see #getChildObject()
	 * @generated
	 */
	void setChildObject(EObject value);

	/**
	 * Returns the value of the '<em><b>Parent Entry</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Obtains the parent entry that contains me.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Entry</em>' reference.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage#getResourceEntry_ParentEntry()
	 * @model transient="true" changeable="false" volatile="true"
	 * @generated
	 */
	ParentEntry getParentEntry();

} // ResourceEntry
