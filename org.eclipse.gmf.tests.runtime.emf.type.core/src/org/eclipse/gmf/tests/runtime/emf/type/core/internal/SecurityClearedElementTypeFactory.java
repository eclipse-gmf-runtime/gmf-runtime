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

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeFactory;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.SpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.GetEditContextCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

/**
 * @author ldamus
 */
public class SecurityClearedElementTypeFactory
	extends AbstractElementTypeFactory {

	private static final String SECURITY_CLEARANCE_PARAM_NAME = "securityClearance"; //$NON-NLS-1$
	
	public static final class CreateSecretEmployeeCommand extends CreateElementCommand {
		
		public CreateSecretEmployeeCommand(CreateElementRequest req) {
			super(req);
		}
		
		protected EObject doDefaultElementCreation() {
			Employee result = (Employee) super.doDefaultElementCreation();
			result.setSecurityClearance(true);
			return result;
		}
	}
	
	public static final class SecurityClearedEditHelper extends AbstractEditHelper {
		public SecurityClearedEditHelper() {
			super();
		}
		
		protected ICommand getEditContextCommand(GetEditContextRequest req) {
			GetEditContextCommand result = null;
			
			if (req.getEditCommandRequest() instanceof CreateElementRequest) {
				CreateElementRequest createRequest = (CreateElementRequest) req.getEditCommandRequest();
				
				if (createRequest.getElementType() == EmployeeType.TOP_SECRET) {
					result = new GetEditContextCommand(req);
					result.setEditContext(EmployeeType.SECRET_DEPARTMENT);
				}
			}
			return result;
		}
		
		protected ICommand getCreateCommand(CreateElementRequest req) {
			
			if (req.getElementType() == EmployeeType.TOP_SECRET) {
				req.setContainmentFeature(EmployeePackage.eINSTANCE.getDepartment_Members());
				return new CreateSecretEmployeeCommand(req);
			}
			return super.getCreateCommand(req);
		}
	}
	
	private static final class SecurityClearedSpecializationType
		extends SpecializationType
		implements ISecurityCleared {

		private final String securityClearance;
		
		private final IEditHelper editHelper = new SecurityClearedEditHelper();

		public SecurityClearedSpecializationType(ISpecializationTypeDescriptor descriptor, String securityClearance) {

			super(descriptor);
			this.securityClearance = securityClearance;
		}

		public String getSecurityClearance() {
			return securityClearance;
		}
		
		public IEditHelper getEditHelper() {
			return editHelper;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeFactory#createSpecializationType(org.eclipse.gmf.runtime.emf.type.core.internal.impl.SpecializationTypeDescriptor)
	 */
	public ISpecializationType createSpecializationType(
			ISpecializationTypeDescriptor descriptor) {

		return new SecurityClearedSpecializationType(descriptor,
			descriptor.getParamValue(SECURITY_CLEARANCE_PARAM_NAME));
	}
}
