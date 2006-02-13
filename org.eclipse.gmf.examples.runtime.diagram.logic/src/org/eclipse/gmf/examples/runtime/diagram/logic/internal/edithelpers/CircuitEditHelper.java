/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.edithelpers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands.ConfigureLogicElementCommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;

/**
 * Edit helper for logic circuit elements.
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class CircuitEditHelper
	extends ContainerElementEditHelper {

	/**
	 * Gets a command to configure a circuit.
	 */
	protected ICommand getConfigureCommand(final ConfigureRequest req) {

		return new ConfigureLogicElementCommand(req, SemanticPackage.eINSTANCE
			.getCircuit()) {

			protected CommandResult doExecuteWithResult(
                    IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {

				Circuit oCircuit = (Circuit) req.getElementToConfigure();

				createInputOutputTerminal(oCircuit, "A", monitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "B", monitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "C", monitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "D", monitor); //$NON-NLS-1$

				createInputOutputTerminal(oCircuit, "1", monitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "2", monitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "3", monitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "4", monitor); //$NON-NLS-1$

				return CommandResult.newOKCommandResult(oCircuit);
			}
		};
	}
}