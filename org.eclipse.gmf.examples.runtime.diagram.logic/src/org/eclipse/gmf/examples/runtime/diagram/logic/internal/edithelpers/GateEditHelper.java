/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.edithelpers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands.ConfigureLogicElementCommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Gate;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;

/**
 * Edit helper for logic gate elements.
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class GateEditHelper
	extends LogicElementEditHelper {

	/**
	 * Gets a command to configure a gate.
	 */
	protected ICommand getConfigureCommand(final ConfigureRequest req) {

		return new ConfigureLogicElementCommand(req, SemanticPackage.eINSTANCE
			.getGate()) {

			protected CommandResult doExecuteWithResult(
                    IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {

				Gate oGate = (Gate) req.getElementToConfigure();

				createInputTerminal(oGate, "A", monitor); //$NON-NLS-1$
				createInputTerminal(oGate, "B", monitor); //$NON-NLS-1$
				createOutputTerminal(oGate, "1", monitor); //$NON-NLS-1$

				return CommandResult.newOKCommandResult(oGate);
			}
		};
	}

}