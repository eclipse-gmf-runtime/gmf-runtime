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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.ConfigureElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Band;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

/**
 * @author ldamus
 */
public class FinanceEditHelperAdvice
	extends AbstractEditHelperAdvice {

	public static class BeforeFinanceConfigureCommand
		extends ConfigureElementCommand {

		public BeforeFinanceConfigureCommand(ConfigureRequest req) {
			super(req);
		}

		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
		    throws ExecutionException {

			return null;
		}
	}

	public static class AfterFinanceConfigureCommand
		extends ConfigureElementCommand {

		public AfterFinanceConfigureCommand(ConfigureRequest req) {
			super(req);
		}

		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
		    throws ExecutionException {

			Employee employee = (Employee) getElementToEdit();
			employee.getDepartment().setName("Finance"); //$NON-NLS-1$
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getBeforeConfigureCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.ConfigureRequest)
	 */
	protected ICommand getBeforeConfigureCommand(ConfigureRequest request) {
		return new BeforeFinanceConfigureCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterConfigureCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.ConfigureRequest)
	 */
	protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
		return new AfterFinanceConfigureCommand(request);
	}
    
    public boolean approveRequest(IEditCommandRequest request) {

        if (request instanceof SetRequest) {
            SetRequest setRequest = (SetRequest) request;
            EStructuralFeature feature = setRequest.getFeature();
            
            if (feature == EmployeePackage.eINSTANCE.getEmployee_Band()
                && setRequest.getValue() == Band.DIRECTOR_LITERAL) {
                // arbitrarily no directors in finance
                return false;
            }
        }
        return super.approveRequest(request);
    }
    
    public void configureRequest(IEditCommandRequest request) {

        if (request instanceof SetRequest) {
            SetRequest setRequest = (SetRequest) request;
            EStructuralFeature feature = setRequest.getFeature();
            
            if (feature == EmployeePackage.eINSTANCE.getEmployee_Band()
                && setRequest.getValue() == Band.DIRECTOR_LITERAL) {
                // set a parameter
                request.setParameter("approved", Boolean.FALSE); //$NON-NLS-1$
                return;
            }
        }
        request.setParameter("approved", Boolean.TRUE); //$NON-NLS-1$
    }
    
}
