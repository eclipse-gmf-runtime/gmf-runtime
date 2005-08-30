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
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Gate;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
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

			protected CommandResult doExecute(IProgressMonitor progressMonitor) {

				Gate oGate = (Gate) req.getElementToConfigure();

				createInputTerminal(oGate, "A", progressMonitor); //$NON-NLS-1$
				createInputTerminal(oGate, "B", progressMonitor); //$NON-NLS-1$
				createOutputTerminal(oGate, "1", progressMonitor); //$NON-NLS-1$

				return newOKCommandResult(oGate);
			}
		};
	}

}