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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
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
public class NotInheritedEditHelperAdvice
	extends AbstractEditHelperAdvice {

	public static class NotInheritedBeforeConfigureCommand
		extends ConfigureElementCommand {

		public NotInheritedBeforeConfigureCommand(ConfigureRequest request) {
			super(request);
		}

		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
		    throws ExecutionException {

			return null;
		}
	}

	public static class NotInheritedAfterConfigureCommand
		extends ConfigureElementCommand {

		public NotInheritedAfterConfigureCommand(final ConfigureRequest request) {
			super(request);
		}

		protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
                IAdaptable info)
            throws ExecutionException {
            Employee employee = (Employee) getElementToEdit();

            Office office = EmployeeEditHelper.createOffice(getEditingDomain(),
                employee, monitor);
            office.setHasDoor(false);
            office.setNumberOfWindows(1);
            return null;
        }
	}

	protected ICommand getBeforeConfigureCommand(ConfigureRequest request) {
		return new NotInheritedBeforeConfigureCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterConfigureCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.ConfigureRequest)
	 */
	protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
		return new NotInheritedAfterConfigureCommand(request);
	}
}
