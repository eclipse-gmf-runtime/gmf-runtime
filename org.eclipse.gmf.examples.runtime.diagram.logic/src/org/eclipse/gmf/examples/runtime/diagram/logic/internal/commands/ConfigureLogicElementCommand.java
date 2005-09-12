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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Element;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.util.LogicSemanticType;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.ConfigureElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;

/**
 * Abstract superclass for commands that configure <code>Element</code> s in
 * the logic model with input and output terminals.
 * 
 * @author ldamus
 */
public abstract class ConfigureLogicElementCommand
	extends ConfigureElementCommand {

	/**
	 * The input terminal feature.
	 */
	private static final EReference TERMINALS = SemanticPackage.eINSTANCE
		.getElement_Terminals();

	/**
	 * Constructs a new configure command for logic elements.
	 * 
	 * @param request
	 *            the configure request
	 * @param configurableType
	 *            the kind of element that can be configured by this command
	 *            instance.
	 */
	protected ConfigureLogicElementCommand(ConfigureRequest request,
			EClass configurableType) {

		super(request);
		setEClass(configurableType);
	}

	/**
	 * Creates an input terminal in <code>logicElement</code>.
	 * 
	 * @param logicElement
	 *            the logic element
	 * @param id
	 *            the terminal identifier
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 */
	protected void createInputTerminal(Element logicElement, String id,
			IProgressMonitor progressMonitor) {

		createTerminal(LogicSemanticType.INPUT_TERMINAL, logicElement, id,
			progressMonitor);
	}

	/**
	 * Creates an output terminal in <code>logicElement</code>.
	 * 
	 * @param logicElement
	 *            the logic element
	 * @param id
	 *            the terminal identifier
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 */
	protected void createOutputTerminal(Element logicElement, String id,
			IProgressMonitor progressMonitor) {

		createTerminal(LogicSemanticType.OUTPUT_TERMINAL, logicElement, id,
			progressMonitor);
	}

	/**
	 * Creates an input/output terminal in <code>logicElement</code>.
	 * 
	 * @param logicElement
	 *            the logic element
	 * @param id
	 *            the terminal identifier
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 */
	protected void createInputOutputTerminal(Element logicElement, String id,
			IProgressMonitor progressMonitor) {

		createTerminal(LogicSemanticType.INPUT_OUTPUT_TERMINAL, logicElement,
			id, progressMonitor);
	}

	/**
	 * Creates a new terminal in the <code>logicElement</code>, and sets its
	 * identifier to <code>id</code>.
	 * 
	 * @param elementType
	 *            the type of terminal to create
	 * @param logicElement
	 *            the logic element
	 * @param id
	 *            the terminal identifier
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 */
	private void createTerminal(IElementType elementType, Element logicElement,
			String id, IProgressMonitor progressMonitor) {

		Terminal terminal = createTerminal(elementType, logicElement,
			progressMonitor);

		if (terminal != null) {
			setTerminalId(elementType, terminal, id, progressMonitor);
		}
	}

	/**
	 * Creates a new terminal in the <code>containmentFeature</code> of
	 * <code>logicElement</code>
	 * 
	 * 
	 * @param elementType
	 *            the type of terminal to create
	 * @param logicElement
	 *            the logic element
	 * 
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 * @return the new terminal element, or <code>null</code> if it wasn't
	 *         created
	 */
	private Terminal createTerminal(IElementType elementType,
			Element logicElement, IProgressMonitor progressMonitor) {

		CreateElementRequest createRequest = new CreateElementRequest(
			logicElement, elementType, TERMINALS);

		ICommand createCommand = getElementType().getEditCommand(createRequest);

		if (createCommand != null && createCommand.isExecutable()) {
			createCommand.execute(progressMonitor);
			CommandResult commandResult = createCommand.getCommandResult();

			if (isOK(commandResult)) {
				Object result = commandResult.getReturnValue();

				if (result  instanceof Terminal) {
					return (Terminal) result;
				}
			}
		}
		return null;
	}

	/**
	 * Sets the <code>terminal</code> identifier to <code>id</code>.
	 * 
	 * @param elementType
	 *            the type of terminal
	 * @param terminal
	 *            the terminal element
	 * @param id
	 *            the terminal identifier
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 */
	private void setTerminalId(IElementType elementType, Terminal terminal,
			String id, IProgressMonitor progressMonitor) {

		SetRequest setRequest = new SetRequest(terminal,
			SemanticPackage.eINSTANCE.getTerminal_Id(), id);

		ICommand setCommand = elementType.getEditCommand(setRequest);

		if (setCommand != null && setCommand.isExecutable()) {
			setCommand.execute(progressMonitor);
		}
	}

}