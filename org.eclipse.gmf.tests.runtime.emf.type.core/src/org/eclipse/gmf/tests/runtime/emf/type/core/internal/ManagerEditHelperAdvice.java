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

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.ConfigureElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Office;

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
