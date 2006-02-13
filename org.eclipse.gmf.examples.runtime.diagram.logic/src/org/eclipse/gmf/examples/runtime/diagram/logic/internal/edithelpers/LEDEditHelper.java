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

			protected CommandResult doExecuteWithResult(
                    IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {
                
				LED oLED = (LED) req.getElementToConfigure();

				createInputTerminal(oLED, "A", monitor); //$NON-NLS-1$
				createInputTerminal(oLED, "B", monitor); //$NON-NLS-1$
				createInputTerminal(oLED, "C", monitor); //$NON-NLS-1$
				createInputTerminal(oLED, "D", monitor); //$NON-NLS-1$

				createOutputTerminal(oLED, "1", monitor); //$NON-NLS-1$
				createOutputTerminal(oLED, "2", monitor); //$NON-NLS-1$
				createOutputTerminal(oLED, "3", monitor); //$NON-NLS-1$
				createOutputTerminal(oLED, "4", monitor); //$NON-NLS-1$

				return CommandResult.newOKCommandResult(oLED);
			}
		};
	}
}