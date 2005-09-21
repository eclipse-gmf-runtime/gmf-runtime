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

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

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
}
