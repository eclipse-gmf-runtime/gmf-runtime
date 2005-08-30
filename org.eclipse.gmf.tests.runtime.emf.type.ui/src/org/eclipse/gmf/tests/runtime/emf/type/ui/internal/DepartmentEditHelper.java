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