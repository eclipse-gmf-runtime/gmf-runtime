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
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;


/**
 * @author ldamus
 */
public class OfficeEditHelper
	extends AbstractEditHelper {

	public static class OfficeConfigureCommand
		extends ConfigureElementCommand {

		public OfficeConfigureCommand(ConfigureRequest req) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper#getConfigureCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.ConfigureRequest)
	 */
	protected ICommand getConfigureCommand(ConfigureRequest req) {
		return new OfficeConfigureCommand(req);
	}
}
