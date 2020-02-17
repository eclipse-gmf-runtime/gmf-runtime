/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.logic.semantic.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage
 * @generated
 */
public class SemanticSwitch {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static SemanticPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SemanticSwitch() {
		if (modelPackage == null) {
			modelPackage = SemanticPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public Object doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch((EClass)eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case SemanticPackage.LED: {
				LED led = (LED)theEObject;
				Object result = caseLED(led);
				if (result == null) result = caseElement(led);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.ELEMENT: {
				Element element = (Element)theEObject;
				Object result = caseElement(element);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.WIRE: {
				Wire wire = (Wire)theEObject;
				Object result = caseWire(wire);
				if (result == null) result = caseElement(wire);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.CIRCUIT: {
				Circuit circuit = (Circuit)theEObject;
				Object result = caseCircuit(circuit);
				if (result == null) result = caseContainerElement(circuit);
				if (result == null) result = caseElement(circuit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.GATE: {
				Gate gate = (Gate)theEObject;
				Object result = caseGate(gate);
				if (result == null) result = caseElement(gate);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.FLOW_CONTAINER: {
				FlowContainer flowContainer = (FlowContainer)theEObject;
				Object result = caseFlowContainer(flowContainer);
				if (result == null) result = caseContainerElement(flowContainer);
				if (result == null) result = caseElement(flowContainer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.AND_GATE: {
				AndGate andGate = (AndGate)theEObject;
				Object result = caseAndGate(andGate);
				if (result == null) result = caseGate(andGate);
				if (result == null) result = caseElement(andGate);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.OR_GATE: {
				OrGate orGate = (OrGate)theEObject;
				Object result = caseOrGate(orGate);
				if (result == null) result = caseGate(orGate);
				if (result == null) result = caseElement(orGate);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.XOR_GATE: {
				XORGate xorGate = (XORGate)theEObject;
				Object result = caseXORGate(xorGate);
				if (result == null) result = caseGate(xorGate);
				if (result == null) result = caseElement(xorGate);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.MODEL: {
				Model model = (Model)theEObject;
				Object result = caseModel(model);
				if (result == null) result = caseContainerElement(model);
				if (result == null) result = caseElement(model);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.CONTAINER_ELEMENT: {
				ContainerElement containerElement = (ContainerElement)theEObject;
				Object result = caseContainerElement(containerElement);
				if (result == null) result = caseElement(containerElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.TERMINAL: {
				Terminal terminal = (Terminal)theEObject;
				Object result = caseTerminal(terminal);
				if (result == null) result = caseElement(terminal);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.OUTPUT_TERMINAL: {
				OutputTerminal outputTerminal = (OutputTerminal)theEObject;
				Object result = caseOutputTerminal(outputTerminal);
				if (result == null) result = caseTerminal(outputTerminal);
				if (result == null) result = caseElement(outputTerminal);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.INPUT_TERMINAL: {
				InputTerminal inputTerminal = (InputTerminal)theEObject;
				Object result = caseInputTerminal(inputTerminal);
				if (result == null) result = caseTerminal(inputTerminal);
				if (result == null) result = caseElement(inputTerminal);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SemanticPackage.INPUT_OUTPUT_TERMINAL: {
				InputOutputTerminal inputOutputTerminal = (InputOutputTerminal)theEObject;
				Object result = caseInputOutputTerminal(inputOutputTerminal);
				if (result == null) result = caseOutputTerminal(inputOutputTerminal);
				if (result == null) result = caseInputTerminal(inputOutputTerminal);
				if (result == null) result = caseTerminal(inputOutputTerminal);
				if (result == null) result = caseElement(inputOutputTerminal);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>LED</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>LED</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseLED(LED object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseElement(Element object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Wire</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Wire</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseWire(Wire object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Circuit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Circuit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseCircuit(Circuit object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Gate</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Gate</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseGate(Gate object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Flow Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Flow Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFlowContainer(FlowContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>And Gate</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>And Gate</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAndGate(AndGate object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Or Gate</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Or Gate</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseOrGate(OrGate object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>XOR Gate</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>XOR Gate</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseXORGate(XORGate object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseModel(Model object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Container Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Container Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseContainerElement(ContainerElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Terminal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Terminal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTerminal(Terminal object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Output Terminal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Output Terminal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseOutputTerminal(OutputTerminal object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Input Terminal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Input Terminal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseInputTerminal(InputTerminal object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Input Output Terminal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Input Output Terminal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseInputOutputTerminal(InputOutputTerminal object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public Object defaultCase(EObject object) {
		return null;
	}

} //SemanticSwitch
