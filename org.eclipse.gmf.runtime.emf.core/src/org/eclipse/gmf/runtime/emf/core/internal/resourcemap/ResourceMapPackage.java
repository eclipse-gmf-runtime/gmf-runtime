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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the logical resource physical mapping metamodel.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMapFactory
 * @model kind="package"
 * @generated
 */
public interface ResourceMapPackage extends EPackage{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "resourcemap"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.ibm.com/RMP-MSL/7.0.0/ResourceMap"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "resmap"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ResourceMapPackage eINSTANCE = org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapImpl <em>Resource Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapImpl
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapPackageImpl#getResourceMap()
	 * @generated
	 */
	int RESOURCE_MAP = 0;

	/**
	 * The feature id for the '<em><b>Root Map</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_MAP__ROOT_MAP = 0;

	/**
	 * The feature id for the '<em><b>Parent Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_MAP__PARENT_ENTRIES = 1;

	/**
	 * The feature id for the '<em><b>Child Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_MAP__CHILD_ENTRIES = 2;

	/**
	 * The number of structural features of the the '<em>Resource Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_MAP_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceEntryImpl <em>Resource Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceEntryImpl
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapPackageImpl#getResourceEntry()
	 * @generated
	 */
	int RESOURCE_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Resource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ENTRY__RESOURCE = 0;

	/**
	 * The feature id for the '<em><b>Child Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ENTRY__CHILD_POSITION = 1;

	/**
	 * The feature id for the '<em><b>Child Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ENTRY__CHILD_OBJECT = 2;

	/**
	 * The feature id for the '<em><b>Parent Entry</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ENTRY__PARENT_ENTRY = 3;

	/**
	 * The number of structural features of the the '<em>Resource Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ENTRY_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ParentEntryImpl <em>Parent Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ParentEntryImpl
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapPackageImpl#getParentEntry()
	 * @generated
	 */
	int PARENT_ENTRY = 2;

	/**
	 * The feature id for the '<em><b>Parent Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARENT_ENTRY__PARENT_OBJECT = 0;

	/**
	 * The feature id for the '<em><b>Child Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARENT_ENTRY__CHILD_SLOT = 1;

	/**
	 * The feature id for the '<em><b>Resource Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARENT_ENTRY__RESOURCE_ENTRIES = 2;

	/**
	 * The feature id for the '<em><b>Resource Map</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARENT_ENTRY__RESOURCE_MAP = 3;

	/**
	 * The number of structural features of the the '<em>Parent Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARENT_ENTRY_FEATURE_COUNT = 4;


	/**
	 * The meta object id for the '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ChildEntryImpl <em>Child Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ChildEntryImpl
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.impl.ResourceMapPackageImpl#getChildEntry()
	 * @generated
	 */
	int CHILD_ENTRY = 3;

	/**
	 * The feature id for the '<em><b>Child Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHILD_ENTRY__CHILD_OBJECT = 0;

	/**
	 * The feature id for the '<em><b>Parent Map</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHILD_ENTRY__PARENT_MAP = 1;

	/**
	 * The number of structural features of the the '<em>Child Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHILD_ENTRY_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap <em>Resource Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Map</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap
	 * @generated
	 */
	EClass getResourceMap();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap#getRootMap <em>Root Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Root Map</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap#getRootMap()
	 * @see #getResourceMap()
	 * @generated
	 */
	EReference getResourceMap_RootMap();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap#getParentEntries <em>Parent Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parent Entries</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap#getParentEntries()
	 * @see #getResourceMap()
	 * @generated
	 */
	EReference getResourceMap_ParentEntries();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap#getChildEntries <em>Child Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Child Entries</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceMap#getChildEntries()
	 * @see #getResourceMap()
	 * @generated
	 */
	EReference getResourceMap_ChildEntries();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry <em>Resource Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Entry</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry
	 * @generated
	 */
	EClass getResourceEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getResource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Resource</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getResource()
	 * @see #getResourceEntry()
	 * @generated
	 */
	EAttribute getResourceEntry_Resource();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getChildPosition <em>Child Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Child Position</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getChildPosition()
	 * @see #getResourceEntry()
	 * @generated
	 */
	EAttribute getResourceEntry_ChildPosition();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getChildObject <em>Child Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Child Object</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getChildObject()
	 * @see #getResourceEntry()
	 * @generated
	 */
	EReference getResourceEntry_ChildObject();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getParentEntry <em>Parent Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parent Entry</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ResourceEntry#getParentEntry()
	 * @see #getResourceEntry()
	 * @generated
	 */
	EReference getResourceEntry_ParentEntry();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry <em>Parent Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parent Entry</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry
	 * @generated
	 */
	EClass getParentEntry();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getParentObject <em>Parent Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parent Object</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getParentObject()
	 * @see #getParentEntry()
	 * @generated
	 */
	EReference getParentEntry_ParentObject();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getChildSlot <em>Child Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Child Slot</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getChildSlot()
	 * @see #getParentEntry()
	 * @generated
	 */
	EReference getParentEntry_ChildSlot();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getResourceEntries <em>Resource Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Resource Entries</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getResourceEntries()
	 * @see #getParentEntry()
	 * @generated
	 */
	EReference getParentEntry_ResourceEntries();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getResourceMap <em>Resource Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Resource Map</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ParentEntry#getResourceMap()
	 * @see #getParentEntry()
	 * @generated
	 */
	EReference getParentEntry_ResourceMap();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry <em>Child Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Child Entry</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry
	 * @generated
	 */
	EClass getChildEntry();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry#getChildObject <em>Child Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Child Object</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry#getChildObject()
	 * @see #getChildEntry()
	 * @generated
	 */
	EReference getChildEntry_ChildObject();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry#getParentMap <em>Parent Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parent Map</em>'.
	 * @see org.eclipse.gmf.runtime.emf.core.internal.resourcemap.ChildEntry#getParentMap()
	 * @see #getChildEntry()
	 * @generated
	 */
	EReference getChildEntry_ParentMap();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ResourceMapFactory getResourceMapFactory();

} //ResourceMapPackage
