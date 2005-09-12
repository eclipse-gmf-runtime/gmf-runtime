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

package org.eclipse.gmf.runtime.emf.commands.core.edithelpers;

import java.util.List;

import org.eclipse.core.runtime.IStatus;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand;
import org.eclipse.gmf.runtime.emf.commands.core.commands.MSLCreateElementCommand;
import org.eclipse.gmf.runtime.emf.commands.core.commands.MSLCreateRelationshipCommand;
import org.eclipse.gmf.runtime.emf.commands.core.commands.MSLDestroyElementCommand;
import org.eclipse.gmf.runtime.emf.commands.core.commands.MSLMoveElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;

/**
 * Edit helper for modifying model elements using the MSL action protocol.
 * <P>
 * Provides default commands for create, delete, move, and set that use the
 * basic EMF APIs to do their work.
 * <P>
 * Clients using MSL should subclass this edit helper when contributing new
 * element types. Each command will execute using the MSL action protocol.
 * <P>
 * This edit helper is contributed to the <code>ElementTypeRegistry</code> for
 * all <code>EModelElement</code> s that don't specify their own element type.
 * 
 * @author ldamus
 */
public class MSLEditHelper
	extends AbstractEditHelper {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper#createCompositeCommand(java.lang.String)
	 */
	protected CompositeCommand createCompositeCommand(IEditCommandRequest req) {

		return new CompositeModelCommand(req.getLabel()) {

			public CommandResult getCommandResult() {
				CommandResult result = super.getCommandResult();

				if (result != null && result.getStatus().getSeverity() == IStatus.OK) {
					Object returnObject = null;

					if (result.getReturnValue() instanceof List) {
						List returnValue = (List) result.getReturnValue();

						if (returnValue.size() > 0) {
							returnObject = returnValue.get(0);
						}

					} else {
						returnObject = result.getReturnValue();
					}
					result = new CommandResult(result.getStatus(), returnObject);
				}
				return result;
			};
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper#getMoveCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.MoveRequest)
	 */
	protected ICommand getMoveCommand(MoveRequest req) {
		return new MSLMoveElementCommand(req);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper#getDestroyElementCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.DestroyElementRequest)
	 */
	protected ICommand getDestroyElementCommand(
			DestroyElementRequest req) {
		return new MSLDestroyElementCommand(req);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper#getCreateCommand(org.eclipse.gmf.runtime.emf.type.core.edithelper.CreateElementRequest)
	 */
	protected ICommand getCreateCommand(CreateElementRequest req) {
		return new MSLCreateElementCommand(req);
	}
	
	protected ICommand getCreateRelationshipCommand(CreateRelationshipRequest req) {
		return new MSLCreateRelationshipCommand(req);
	}
}