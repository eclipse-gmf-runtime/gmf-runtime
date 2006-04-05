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

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

/**
 * @author ldamus
 */
public class DepartmentEditHelper
	extends AbstractEditHelper {

	public static class DepartmentCreateCommand
		extends CreateElementCommand {

		public DepartmentCreateCommand(CreateElementRequest req) {
			super(req);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper#getCreateCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.CreateElementRequest)
	 */
	protected ICommand getCreateCommand(CreateElementRequest req) {
		return new DepartmentCreateCommand(req);
	}
    
    protected boolean approveRequest(IEditCommandRequest req) {
        if (req instanceof SetRequest) {
            SetRequest setRequest = (SetRequest) req;
            EStructuralFeature feature = setRequest.getFeature();
            
            if (feature == EmployeePackage.eINSTANCE.getDepartment_Number()) {
                Object value = setRequest.getValue();
                
                if (value instanceof Integer) {
                    if (((Integer) value).intValue() == 0) {
                        return false;
                    }
                }
            }
        }
        return super.approveRequest(req);
    }
    
    protected void configureRequest(IEditCommandRequest req) {
        if (req instanceof SetRequest) {
            SetRequest setRequest = (SetRequest) req;
            EStructuralFeature feature = setRequest.getFeature();
            
            if (feature == EmployeePackage.eINSTANCE.getDepartment_Number()) {
                Object value = setRequest.getValue();
                
                if (value instanceof Integer) {
                    if (((Integer) value).intValue() == 0) {
                        // set a parameter
                        req.setParameter("approved", Boolean.FALSE); //$NON-NLS-1$
                        return;
                    }
                }
            }
        }
        req.setParameter("approved", Boolean.TRUE); //$NON-NLS-1$
    }
}
