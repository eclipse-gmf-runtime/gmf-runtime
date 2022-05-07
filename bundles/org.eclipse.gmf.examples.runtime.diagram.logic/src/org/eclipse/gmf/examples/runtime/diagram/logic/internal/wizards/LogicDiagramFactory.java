/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.wizards;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.impl.EMFCommandTransaction;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.CircuitEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.TerminalEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.AndGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Model;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OrGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.XORGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.util.LogicSemanticType;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * Factory for creating contents of example logic diagrams.
 * 
 * @author mgobeil
 */
public class LogicDiagramFactory {

	public static void CreateFourBitAdder(IGraphicalEditPart diagramEditPart,
			IProgressMonitor progressMonitor) {

		// create logic elements
		Circuit fullAdder1 = createFullAdder(new Point(20, 120), new Dimension(
				140, 230), diagramEditPart, progressMonitor);
		Circuit fullAdder2 = createFullAdder(new Point(170, 120),
				new Dimension(140, 230), diagramEditPart, progressMonitor);
		Circuit fullAdder3 = createFullAdder(new Point(320, 120),
				new Dimension(140, 230), diagramEditPart, progressMonitor);
		Circuit halfAdder = createHalfAdder(new Point(470, 120), new Dimension(
				70, 230), diagramEditPart, progressMonitor);

		// chain carry bits
		connect(outputTerminal(fullAdder1, 3), inputTerminal(fullAdder2, 4),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(fullAdder2, 3), inputTerminal(fullAdder3, 4),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(fullAdder3, 3), inputTerminal(halfAdder, 4),
				diagramEditPart, progressMonitor);

		// create and connect led input 1
		final LED ledInput1 = createElement(LogicSemanticType.LED, LED.class,
				new Point(150, 20), diagramEditPart, progressMonitor);
		connect(outputTerminal(ledInput1, 3), inputTerminal(fullAdder1, 0),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(ledInput1, 2), inputTerminal(fullAdder2, 0),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(ledInput1, 1), inputTerminal(fullAdder3, 0),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(ledInput1, 0), inputTerminal(halfAdder, 0),
				diagramEditPart, progressMonitor);

		// create and connect led input 2
		final LED ledInput2 = createElement(LogicSemanticType.LED, LED.class,
				new Point(350, 20), diagramEditPart, progressMonitor);
		connect(outputTerminal(ledInput2, 3), inputTerminal(fullAdder1, 2),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(ledInput2, 2), inputTerminal(fullAdder2, 2),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(ledInput2, 1), inputTerminal(fullAdder3, 2),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(ledInput2, 0), inputTerminal(halfAdder, 3),
				diagramEditPart, progressMonitor);

		// create and connect led output
		LED ledOutput = createElement(LogicSemanticType.LED, LED.class,
				new Point(250, 400), diagramEditPart, progressMonitor);
		connect(outputTerminal(fullAdder1, 7), inputTerminal(ledOutput, 3),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(fullAdder2, 7), inputTerminal(ledOutput, 2),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(fullAdder3, 7), inputTerminal(ledOutput, 1),
				diagramEditPart, progressMonitor);
		connect(outputTerminal(halfAdder, 7), inputTerminal(ledOutput, 0),
				diagramEditPart, progressMonitor);

		// set the input values
		org.eclipse.emf.common.command.Command cmd = new org.eclipse.emf.common.command.AbstractCommand() {
			public void execute() {
				ledInput1.setValue(3);
				ledInput2.setValue(7);
			}

			public void redo() {
				execute();
			}
		};
		EMFCommandTransaction trans = new EMFCommandTransaction(cmd,
				(InternalTransactionalEditingDomain) diagramEditPart
						.getEditingDomain(), null);
		try {
			trans.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		trans.getCommand().execute();
		try {
			trans.commit();
		} catch (RollbackException e) {
			e.printStackTrace();
		}
	}

	public static Circuit createFullAdder(Point location, Dimension size,
			IGraphicalEditPart containerEditPart,
			IProgressMonitor progressMonitor) {
		Circuit circuit = createElement(LogicSemanticType.CIRCUIT,
				Circuit.class, location, size, containerEditPart,
				progressMonitor);
		CircuitEditPart circuitEditPart = getEditPart(circuit,
				CircuitEditPart.class, containerEditPart);

		Circuit halfAdder1 = createHalfAdder(new Point(10, 10),
				circuitEditPart, progressMonitor);
		Circuit halfAdder2 = createHalfAdder(new Point(50, 100),
				circuitEditPart, progressMonitor);
		OrGate orGate = createElement(LogicSemanticType.ORGATE, OrGate.class,
				new Point(40, 180), circuitEditPart, progressMonitor);
		;

		// input to first half adder
		connect(outputTerminal(circuit, 0), inputTerminal(halfAdder1, 0),
				circuitEditPart, progressMonitor);
		connect(outputTerminal(circuit, 2), inputTerminal(halfAdder1, 3),
				circuitEditPart, progressMonitor);

		// input to second half adder
		connect(outputTerminal(circuit, 3), inputTerminal(halfAdder2, 3),
				circuitEditPart, progressMonitor);
		connect(outputTerminal(halfAdder1, 7), inputTerminal(halfAdder2, 0),
				circuitEditPart, progressMonitor);

		// input to OR gate
		connect(outputTerminal(halfAdder1, 4), inputTerminal(orGate, 0),
				circuitEditPart, progressMonitor);
		connect(outputTerminal(halfAdder2, 4), inputTerminal(orGate, 1),
				circuitEditPart, progressMonitor);

		// output of circuit
		connect(outputTerminal(orGate, 0), inputTerminal(circuit, 4),
				circuitEditPart, progressMonitor);
		connect(outputTerminal(halfAdder2, 7), inputTerminal(circuit, 7),
				circuitEditPart, progressMonitor);

		return circuit;
	}

	public static Circuit createHalfAdder(Point location,
			IGraphicalEditPart containerEditPart,
			IProgressMonitor progressMonitor) {
		return createHalfAdder(location, new Dimension(70, 70),
				containerEditPart, progressMonitor);
	}

	public static Circuit createHalfAdder(Point location, Dimension size,
			IGraphicalEditPart containerEditPart,
			IProgressMonitor progressMonitor) {
		Circuit circuit = createElement(LogicSemanticType.CIRCUIT,
				Circuit.class, location, size, containerEditPart,
				progressMonitor);
		CircuitEditPart circuitEditPart = getEditPart(circuit,
				CircuitEditPart.class, containerEditPart);

		AndGate andGate = createElement(LogicSemanticType.ANDGATE,
				AndGate.class, new Point(10, 20), circuitEditPart,
				progressMonitor);
		;
		XORGate xorGate = createElement(LogicSemanticType.XORGATE,
				XORGate.class, new Point(40, 20), circuitEditPart,
				progressMonitor);
		;

		connect(outputTerminal(circuit, 0), inputTerminal(andGate, 0),
				circuitEditPart, progressMonitor);
		connect(outputTerminal(circuit, 3), inputTerminal(andGate, 1),
				circuitEditPart, progressMonitor);
		connect(outputTerminal(circuit, 0), inputTerminal(xorGate, 0),
				circuitEditPart, progressMonitor);
		connect(outputTerminal(circuit, 3), inputTerminal(xorGate, 1),
				circuitEditPart, progressMonitor);

		connect(outputTerminal(andGate, 0), inputTerminal(circuit, 4),
				circuitEditPart, progressMonitor);
		connect(outputTerminal(xorGate, 0), inputTerminal(circuit, 7),
				circuitEditPart, progressMonitor);

		return circuit;
	}

	private static <T> T createElement(IElementType elementType,
			Class<T> elementTypeClass, Point location,
			IGraphicalEditPart container, IProgressMonitor progressMonitor) {

		return createElement(elementType, elementTypeClass, location, null,
				container, progressMonitor);
	}

	private static <T> T createElement(IElementType elementType,
			Class<T> elementTypeClass, Point location, Dimension size,
			IGraphicalEditPart container, IProgressMonitor progressMonitor) {

		if (container instanceof CircuitEditPart) {
			container = (IGraphicalEditPart) container
					.getChildBySemanticHint(LogicConstants.LOGIC_SHAPE_COMPARTMENT);
		}

		CreateViewRequest createRequest = CreateViewRequestFactory
				.getCreateShapeRequest(elementType, container
						.getDiagramPreferencesHint());

		createRequest.setLocation(location);
		createRequest.setSize(size);

		Command command = container.getCommand(createRequest);

		container.getDiagramEditDomain().getDiagramCommandStack().execute(
				command, progressMonitor);

		List<IAdaptable> newObject = (List<IAdaptable>) createRequest
				.getNewObject();
		ViewAndElementDescriptor viewAndElementDescriptor = (ViewAndElementDescriptor) newObject
				.get(0);
		return (T) viewAndElementDescriptor.getElementAdapter().getAdapter(
				elementTypeClass);
	}

	private static void connect(OutputTerminal outputTerminal,
			InputTerminal inputTerminal, IGraphicalEditPart container,
			IProgressMonitor progressMonitor) {
		CreateRelationshipRequest createRequest = new CreateRelationshipRequest(
				container.getEditingDomain(), outputTerminal, inputTerminal,
				LogicSemanticType.WIRE);

		IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(createRequest.getEditHelperContext());

		ICommand createCommand = elementType.getEditCommand(createRequest);

		try {
			createCommand.execute(progressMonitor, null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		// HAAAACK! why won't the above command create a view when the parent is
		// a Model instead of a Circuit?
		if (createRequest.getContainer() instanceof Model) {

			// get matching editparts for semantic terminals
			TerminalEditPart outputTerminalEditPart = getEditPart(
					outputTerminal, TerminalEditPart.class, container);
			TerminalEditPart inputTerminalEditPart = getEditPart(inputTerminal,
					TerminalEditPart.class, container);

			// create matching notational wire connection
			Command command = CreateConnectionViewRequest.getCreateCommand(
					(Wire) createCommand.getCommandResult().getReturnValue(),
					outputTerminalEditPart, inputTerminalEditPart, container
							.getDiagramPreferencesHint());
			container.getDiagramEditDomain().getDiagramCommandStack().execute(
					command, progressMonitor);
		}
	}

	private static OutputTerminal outputTerminal(Element element, int terminal) {
		return (OutputTerminal) element.getOutputTerminals().get(terminal);
	}

	private static InputTerminal inputTerminal(Element element, int terminal) {
		return (InputTerminal) element.getInputTerminals().get(terminal);
	}

	private static <T> T getEditPart(EObject modelElement,
			Class<T> editPartType, IGraphicalEditPart container) {
		if (container instanceof CircuitEditPart) {
			container = (IGraphicalEditPart) container
					.getChildBySemanticHint(LogicConstants.LOGIC_SHAPE_COMPARTMENT);
		}

		DiagramGraphicalViewer viewer = (DiagramGraphicalViewer) container
				.getRoot().getViewer();
		String proxyId = EMFCoreUtil.getProxyID(modelElement);
		return (T) viewer.findEditPartsForElement(proxyId, editPartType).get(0);
	}
}
