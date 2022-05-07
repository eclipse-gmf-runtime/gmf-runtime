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

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.ConfigureElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;

/**
 * @author ldamus
 */
public class EmployeeEditHelper
	extends AbstractEditHelper {

	public static class EmployeeConfigureCommand
		extends ConfigureElementCommand {

		public EmployeeConfigureCommand(ConfigureRequest req) {
			super(req);
		}

		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
		    throws ExecutionException {
			return null;
		}
	}
	
	public static Office createOffice(TransactionalEditingDomain editingDomain, Employee employee,
			IProgressMonitor progressMonitor) throws ExecutionException {
		
		Office office = null;
		CreateElementRequest request = new CreateElementRequest(editingDomain, employee,
			EmployeeType.OFFICE);
		IElementType type = ElementTypeRegistry.getInstance()
			.getElementType(employee);
		ICommand command = type.getEditCommand(request);
		
		if (command != null && command.canExecute()) {
		    command.execute(progressMonitor, null);
		}

		CommandResult officeResult = command.getCommandResult();

		if (officeResult.getStatus().getCode() == IStatus.OK) {

			Object returnValue = officeResult.getReturnValue();
			
			if (returnValue instanceof List && ((List) returnValue).size() > 0) {
				office = (Office) ((List) returnValue).get(0);
				
			} else {
				office = (Office) returnValue;
			}
		}
		return office;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper#getConfigureCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.ConfigureRequest)
	 */
	protected ICommand getConfigureCommand(ConfigureRequest req) {
		return new EmployeeConfigureCommand(req);
	}

}
