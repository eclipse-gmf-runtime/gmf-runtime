package org.eclipse.gmf.tests.runtime.emf.type.ui.internal;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;


/**
 * @author ldamus
 */
public class EmployeeType
	extends AbstractElementTypeEnumerator {

	public static final IElementType DEPARTMENT = getElementType("org.eclipse.gmf.tests.runtime.emf.type.ui.department"); //$NON-NLS-1$

	public static final IElementType EMPLOYEE = getElementType("org.eclipse.gmf.tests.runtime.emf.type.ui.employee"); //$NON-NLS-1$

	public static final IElementType STUDENT = getElementType("org.eclipse.gmf.tests.runtime.emf.type.ui.student"); //$NON-NLS-1$
	
	public static final IElementType OFFICE = getElementType("org.eclipse.gmf.tests.runtime.emf.type.ui.office"); //$NON-NLS-1$

	public static final ISpecializationType MANAGER = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.ui.manager"); //$NON-NLS-1$

	public static final ISpecializationType EXECUTIVE = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.ui.executive"); //$NON-NLS-1$
	
	public static final ISpecializationType TOP_SECRET = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.ui.topSecret"); //$NON-NLS-1$
	
	public static final ISpecializationType SECRET_DEPARTMENT = (ISpecializationType) getElementType("org.eclipse.gmf.tests.runtime.emf.type.ui.secretDepartment"); //$NON-NLS-1$

}
