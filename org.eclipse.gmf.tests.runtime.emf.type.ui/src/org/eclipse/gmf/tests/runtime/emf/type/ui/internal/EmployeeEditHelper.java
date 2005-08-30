/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.emf.type.ui.internal;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.ConfigureElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Office;

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

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
		 */
		protected CommandResult doExecute(IProgressMonitor progressMonitor) {
			return null;
		}
	}
	
	public static Office createOffice(Employee employee,
			IProgressMonitor progressMonitor) {
		
		Office office = null;
		CreateElementRequest request = new CreateElementRequest(employee,
			EmployeeType.OFFICE);
		IElementType type = ElementTypeRegistry.getInstance()
			.getElementType(employee);
		ICommand command = type.getEditCommand(request);
		
		if (command != null && command.isExecutable()) {
			command.execute(progressMonitor);
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