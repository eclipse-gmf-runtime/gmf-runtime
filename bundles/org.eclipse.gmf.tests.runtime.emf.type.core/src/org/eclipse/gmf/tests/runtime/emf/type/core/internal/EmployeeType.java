/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.emf.type.core.internal;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;


/**
 * @author ldamus
 */
public class EmployeeType
	extends AbstractElementTypeEnumerator {

	// These types have no client context bound to them
	public static final IMetamodelType DEPARTMENT = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.department"); //$NON-NLS-1$

	public static final IMetamodelType EMPLOYEE = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.employee"); //$NON-NLS-1$

	public static final IMetamodelType STUDENT = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.student"); //$NON-NLS-1$
	
	public static final IMetamodelType HIGH_SCHOOL_STUDENT = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.highSchoolStudent"); //$NON-NLS-1$
	
	public static final IMetamodelType OFFICE = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.office"); //$NON-NLS-1$
	
	public static final IMetamodelType CUSTOMER = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.customer"); //$NON-NLS-1$
	
	public static final IMetamodelType CLIENT = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.client"); //$NON-NLS-1$

	public static final ISpecializationType MANAGER = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.manager"); //$NON-NLS-1$

	public static final ISpecializationType EXECUTIVE = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.executive"); //$NON-NLS-1$
	
	public static final ISpecializationType TOP_SECRET = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.topSecret"); //$NON-NLS-1$
	
	public static final ISpecializationType SECRET_DEPARTMENT = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.secretDepartment"); //$NON-NLS-1$

	// These types have a client context bound to them
	public static final IMetamodelType CONTEXT_DEPARTMENT = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.department"); //$NON-NLS-1$

	public static final IMetamodelType CONTEXT_EMPLOYEE = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.employee"); //$NON-NLS-1$

	public static final IMetamodelType CONTEXT_STUDENT = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.student"); //$NON-NLS-1$
	
	public static final IMetamodelType CONTEXT_OFFICE = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.office"); //$NON-NLS-1$

	public static final IMetamodelType CONTEXT_CUSTOMER = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.customer"); //$NON-NLS-1$

	public static final IMetamodelType CONTEXT_CLIENT = (IMetamodelType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.client"); //$NON-NLS-1$

	public static final ISpecializationType CONTEXT_MANAGER = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.manager"); //$NON-NLS-1$

	public static final ISpecializationType CONTEXT_EXECUTIVE = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.executive"); //$NON-NLS-1$
	
	public static final ISpecializationType CONTEXT_TOP_SECRET = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.topSecret"); //$NON-NLS-1$
	
	public static final ISpecializationType CONTEXT_SECRET_DEPARTMENT = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.secretDepartment"); //$NON-NLS-1$

	
	public static IMetamodelType[] METAMODEL_TYPES = new IMetamodelType[] {
			DEPARTMENT, EMPLOYEE, STUDENT, HIGH_SCHOOL_STUDENT, OFFICE,
			CUSTOMER, CLIENT };

	public static IMetamodelType[] METAMODEL_TYPES_WITH_CONTEXT = new IMetamodelType[] {
			CONTEXT_DEPARTMENT, CONTEXT_EMPLOYEE, CONTEXT_STUDENT,
			CONTEXT_OFFICE, CONTEXT_CUSTOMER, CONTEXT_CLIENT };

	public static ISpecializationType[] SPECIALIZATION_TYPES = new ISpecializationType[] {
			MANAGER, EXECUTIVE, TOP_SECRET, SECRET_DEPARTMENT };

	public static ISpecializationType[] SPECIALIZATION_TYPES_WITH_CONTEXT = new ISpecializationType[] {
			CONTEXT_MANAGER, CONTEXT_EXECUTIVE, CONTEXT_TOP_SECRET,
			CONTEXT_SECRET_DEPARTMENT };

}
