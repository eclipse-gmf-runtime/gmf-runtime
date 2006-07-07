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
package org.eclipse.gmf.tests.runtime.emf.type.core.internal;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;


/**
 * @author ldamus
 */
public class EmployeeType
	extends AbstractElementTypeEnumerator {

	// These types have no client context bound to them
	public static final IElementType DEPARTMENT = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.department"); //$NON-NLS-1$

	public static final IElementType EMPLOYEE = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.employee"); //$NON-NLS-1$

	public static final IElementType STUDENT = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.student"); //$NON-NLS-1$
	
	public static final IElementType HIGH_SCHOOL_STUDENT = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.highSchoolStudent"); //$NON-NLS-1$
	
	public static final IElementType OFFICE = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.office"); //$NON-NLS-1$

	public static final ISpecializationType MANAGER = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.manager"); //$NON-NLS-1$

	public static final ISpecializationType EXECUTIVE = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.executive"); //$NON-NLS-1$
	
	public static final ISpecializationType TOP_SECRET = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.topSecret"); //$NON-NLS-1$
	
	public static final ISpecializationType SECRET_DEPARTMENT = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.secretDepartment"); //$NON-NLS-1$

	// These types have a client context bound to them
	public static final IElementType CONTEXT_DEPARTMENT = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.department"); //$NON-NLS-1$

	public static final IElementType CONTEXT_EMPLOYEE = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.employee"); //$NON-NLS-1$

	public static final IElementType CONTEXT_STUDENT = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.student"); //$NON-NLS-1$
	
	public static final IElementType CONTEXT_OFFICE = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.office"); //$NON-NLS-1$

	public static final ISpecializationType CONTEXT_MANAGER = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.manager"); //$NON-NLS-1$

	public static final ISpecializationType CONTEXT_EXECUTIVE = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.executive"); //$NON-NLS-1$
	
	public static final ISpecializationType CONTEXT_TOP_SECRET = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.topSecret"); //$NON-NLS-1$
	
	public static final ISpecializationType CONTEXT_SECRET_DEPARTMENT = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.context.secretDepartment"); //$NON-NLS-1$

}
