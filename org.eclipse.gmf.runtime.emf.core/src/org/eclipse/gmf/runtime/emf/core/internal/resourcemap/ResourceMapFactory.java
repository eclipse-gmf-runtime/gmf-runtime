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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the logical resource physical mapping metamodel.
 * It provides a create method for each non-abstract class of the metamodel.
 * <!-- end-user-doc -->
 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapPackage
 * @generated
 */
public interface ResourceMapFactory extends EFactory{
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ResourceMapFactory eINSTANCE = new org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapFactoryImpl();

	/**
	 * Returns a new object of class '<em>Resource Map</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Map</em>'.
	 * @generated
	 */
	ResourceMap createResourceMap();

	/**
	 * Returns a new object of class '<em>Resource Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Entry</em>'.
	 * @generated
	 */
	ResourceEntry createResourceEntry();

	/**
	 * Returns a new object of class '<em>Parent Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parent Entry</em>'.
	 * @generated
	 */
	ParentEntry createParentEntry();

	/**
	 * Returns a new object of class '<em>Child Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Child Entry</em>'.
	 * @generated
	 */
	ChildEntry createChildEntry();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ResourceMapPackage getResourceMapPackage();

} //ResourceMapFactory
