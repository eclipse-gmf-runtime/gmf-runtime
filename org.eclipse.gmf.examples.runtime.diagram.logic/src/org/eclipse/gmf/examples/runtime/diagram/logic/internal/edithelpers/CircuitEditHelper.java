/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.edithelpers;

import org.eclipse.core.runtime.IProgressMonitor;

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

			protected CommandResult doExecute(IProgressMonitor progressMonitor) {

				Circuit oCircuit = (Circuit) req.getElementToConfigure();

				createInputOutputTerminal(oCircuit, "A", progressMonitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "B", progressMonitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "C", progressMonitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "D", progressMonitor); //$NON-NLS-1$

				createInputOutputTerminal(oCircuit, "1", progressMonitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "2", progressMonitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "3", progressMonitor); //$NON-NLS-1$
				createInputOutputTerminal(oCircuit, "4", progressMonitor); //$NON-NLS-1$

				return newOKCommandResult(oCircuit);
			}
		};
	}
}