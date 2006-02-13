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

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.ConfigureElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;

/**
 * @author ldamus
 */
public class ExecutiveEditHelperAdvice
	extends ManagerEditHelperAdvice {

	public static class BeforeExecutiveConfigureCommand
		extends ConfigureElementCommand {

		public BeforeExecutiveConfigureCommand(ConfigureRequest req) {
			super(req);
		}

		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
		    throws ExecutionException {
			return null;
		}
	}

	public static class AfterExecutiveConfigureCommand
		extends ConfigureElementCommand {

		public AfterExecutiveConfigureCommand(ConfigureRequest req) {
			super(req);
		}

		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
		    throws ExecutionException {
            
			Employee employee = (Employee) getRequest().getEditHelperContext();
			Office office = employee.getOffice();
			office.setHasDoor(true);
			
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getBeforeConfigureCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.ConfigureRequest)
	 */
	protected ICommand getBeforeConfigureCommand(ConfigureRequest request) {
		return new BeforeExecutiveConfigureCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterConfigureCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.ConfigureRequest)
	 */
	protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
		return new AfterExecutiveConfigureCommand(request);
	}
}
