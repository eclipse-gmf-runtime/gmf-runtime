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

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands.ConfigureLogicElementCommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Gate;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.InputOutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.OutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.util.LogicSemanticType;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Edit helper advice for the half adder circuit specialization.
 * 
 * @author ldamus
 */
public class HalfAdderAdvice extends AbstractEditHelperAdvice {

	/**
	 * Returns a command that will configure a Circuit to become a Half Adder.
	 * This command will replace the default Circuit configuration command.
	 * 
	 * @return the half adder configuration command
	 */
	protected ICommand getBeforeConfigureCommand(final ConfigureRequest request) {

		// The Half Adder advice completely replaces the default circuit
		// configuration.
		request.setParameter(IEditCommandRequest.REPLACE_DEFAULT_COMMAND,
				Boolean.TRUE);

		return new ConfigureLogicElementCommand(request,
				SemanticPackage.eINSTANCE.getCircuit()) {

			protected CommandResult doExecuteWithResult(
                    IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {

				Circuit circuit = (Circuit) request.getElementToConfigure();

				// Input two 1-bit binary numbers
				InputOutputTerminal a = createInputOutputTerminal(circuit,
						"A", monitor); //$NON-NLS-1$
				InputOutputTerminal b = createInputOutputTerminal(circuit,
						"B", monitor); //$NON-NLS-1$

				// Sum of the the two bits
				InputOutputTerminal s = createInputOutputTerminal(circuit,
						"1", monitor); //$NON-NLS-1$

				// Carry
				InputOutputTerminal c = createInputOutputTerminal(circuit,
						"2", monitor); //$NON-NLS-1$

				// The half adder contains an XOR gate and an AND gate
				Gate xorGate = createGate(circuit, LogicSemanticType.XORGATE,
                    monitor);
				Gate andGate = createGate(circuit, LogicSemanticType.ANDGATE,
                    monitor);

				// Inputs A and B are directed into the XOR gate
				List xorInputs = xorGate.getInputTerminals();
				createWire(a, (InputTerminal) xorInputs.get(0), monitor);
				createWire(b, (InputTerminal) xorInputs.get(1), monitor);

				// The same inputs A and B are also directed into the AND gate
				List andInputs = andGate.getInputTerminals();
				createWire(a, (InputTerminal) andInputs.get(0), monitor);
				createWire(b, (InputTerminal) andInputs.get(1), monitor);

				// The XOR gate produces the sum bit
				List xorOutputs = xorGate.getOutputTerminals();
				createWire((OutputTerminal) xorOutputs.get(0), s,
                    monitor);

				// The AND gate produces the carry bit
				List andOutputs = andGate.getOutputTerminals();
				createWire((OutputTerminal) andOutputs.get(0), c,
                    monitor);

				return CommandResult.newOKCommandResult(circuit);
			}
		};
	}
}
