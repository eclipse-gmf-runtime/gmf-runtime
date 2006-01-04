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
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.edithelpers;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
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

			protected CommandResult doExecute(IProgressMonitor progressMonitor) {

				Circuit circuit = (Circuit) request.getElementToConfigure();

				// Input two 1-bit binary numbers
				InputOutputTerminal a = createInputOutputTerminal(circuit,
						"A", progressMonitor); //$NON-NLS-1$
				InputOutputTerminal b = createInputOutputTerminal(circuit,
						"B", progressMonitor); //$NON-NLS-1$

				// Sum of the the two bits
				InputOutputTerminal s = createInputOutputTerminal(circuit,
						"1", progressMonitor); //$NON-NLS-1$

				// Carry
				InputOutputTerminal c = createInputOutputTerminal(circuit,
						"2", progressMonitor); //$NON-NLS-1$

				// The half adder contains an XOR gate and an AND gate
				Gate xorGate = createGate(circuit, LogicSemanticType.XORGATE,
						progressMonitor);
				Gate andGate = createGate(circuit, LogicSemanticType.ANDGATE,
						progressMonitor);

				// Inputs A and B are directed into the XOR gate
				List xorInputs = xorGate.getInputTerminals();
				createWire(a, (InputTerminal) xorInputs.get(0), progressMonitor);
				createWire(b, (InputTerminal) xorInputs.get(1), progressMonitor);

				// The same inputs A and B are also directed into the AND gate
				List andInputs = andGate.getInputTerminals();
				createWire(a, (InputTerminal) andInputs.get(0), progressMonitor);
				createWire(b, (InputTerminal) andInputs.get(1), progressMonitor);

				// The XOR gate produces the sum bit
				List xorOutputs = xorGate.getOutputTerminals();
				createWire((OutputTerminal) xorOutputs.get(0), s,
						progressMonitor);

				// The AND gate produces the carry bit
				List andOutputs = andGate.getOutputTerminals();
				createWire((OutputTerminal) andOutputs.get(0), c,
						progressMonitor);

				return newOKCommandResult(circuit);
			}
		};
	}
}
