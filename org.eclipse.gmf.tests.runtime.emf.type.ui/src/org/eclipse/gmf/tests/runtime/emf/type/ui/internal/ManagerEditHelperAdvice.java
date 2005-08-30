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

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.ConfigureElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Office;

/**
 * @author ldamus
 */
public class ManagerEditHelperAdvice
	extends AbstractEditHelperAdvice {

	public static class ManagerBeforeConfigureCommand
		extends ConfigureElementCommand {

		public ManagerBeforeConfigureCommand(ConfigureRequest request) {
			super(request);
		}

		protected CommandResult doExecute(IProgressMonitor progressMonitor) {
			return null;
		}
	}

	public static class ManagerAfterConfigureCommand
		extends ConfigureElementCommand {

		public ManagerAfterConfigureCommand(ConfigureRequest request) {
			super(request);
		}

		protected CommandResult doExecute(IProgressMonitor progressMonitor) {
			Employee employee = (Employee) getRequest().getEditHelperContext();

			Office office = EmployeeEditHelper.createOffice(employee, progressMonitor);
			office.setHasDoor(false);
			office.setNumberOfWindows(1);
			return null;
		}
	}

	protected ICommand getBeforeConfigureCommand(ConfigureRequest request) {
		return new ManagerBeforeConfigureCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterConfigureCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.ConfigureRequest)
	 */
	protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
		return new ManagerAfterConfigureCommand(request);
	}
}