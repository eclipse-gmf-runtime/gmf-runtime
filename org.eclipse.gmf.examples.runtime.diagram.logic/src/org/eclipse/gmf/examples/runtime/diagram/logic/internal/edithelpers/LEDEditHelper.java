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
import org.eclipse.gmf.examples.runtime.diagram.logic.model.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;

/**
 * Edit helper for logic LED elements.
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class LEDEditHelper
	extends LogicElementEditHelper {

	/**
	 * Gets a command to configure an LED.
	 */
	protected ICommand getConfigureCommand(final ConfigureRequest req) {

		return new ConfigureLogicElementCommand(req, SemanticPackage.eINSTANCE
			.getLED()) {

			protected CommandResult doExecute(IProgressMonitor progressMonitor) {
				LED oLED = (LED) req.getElementToConfigure();

				createInputTerminal(oLED, "A", progressMonitor); //$NON-NLS-1$
				createInputTerminal(oLED, "B", progressMonitor); //$NON-NLS-1$
				createInputTerminal(oLED, "C", progressMonitor); //$NON-NLS-1$
				createInputTerminal(oLED, "D", progressMonitor); //$NON-NLS-1$

				createOutputTerminal(oLED, "1", progressMonitor); //$NON-NLS-1$
				createOutputTerminal(oLED, "2", progressMonitor); //$NON-NLS-1$
				createOutputTerminal(oLED, "3", progressMonitor); //$NON-NLS-1$
				createOutputTerminal(oLED, "4", progressMonitor); //$NON-NLS-1$

				return newOKCommandResult(oLED);
			}
		};
	}
}