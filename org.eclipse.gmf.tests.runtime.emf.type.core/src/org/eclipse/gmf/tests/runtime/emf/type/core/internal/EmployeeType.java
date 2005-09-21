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

	public static final IElementType DEPARTMENT = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.department"); //$NON-NLS-1$

	public static final IElementType EMPLOYEE = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.employee"); //$NON-NLS-1$

	public static final IElementType STUDENT = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.student"); //$NON-NLS-1$
	
	public static final IElementType OFFICE = getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.office"); //$NON-NLS-1$

	public static final ISpecializationType MANAGER = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.manager"); //$NON-NLS-1$

	public static final ISpecializationType EXECUTIVE = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.executive"); //$NON-NLS-1$
	
	public static final ISpecializationType TOP_SECRET = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.topSecret"); //$NON-NLS-1$
	
	public static final ISpecializationType SECRET_DEPARTMENT = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.core.secretDepartment"); //$NON-NLS-1$

}
