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
package org.eclipse.gmf.examples.runtime.diagram.logic.semantic;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticFactory
 * @model kind="package"
 * @generated
 */
public interface SemanticPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "semantic"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/gmf/examples/runtime/1.0.0/logicsemantic"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "semantic"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SemanticPackage eINSTANCE = org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ElementImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getElement()
	 * @generated
	 */
	int ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__TERMINALS = 0;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__OUTPUT_TERMINALS = 1;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__INPUT_TERMINALS = 2;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.LEDImpl <em>LED</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.LEDImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getLED()
	 * @generated
	 */
	int LED = 0;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LED__TERMINALS = ELEMENT__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LED__OUTPUT_TERMINALS = ELEMENT__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LED__INPUT_TERMINALS = ELEMENT__INPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LED__VALUE = ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>LED</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LED_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.WireImpl <em>Wire</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.WireImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getWire()
	 * @generated
	 */
	int WIRE = 2;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRE__TERMINALS = ELEMENT__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRE__OUTPUT_TERMINALS = ELEMENT__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRE__INPUT_TERMINALS = ELEMENT__INPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRE__SOURCE = ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRE__TARGET = ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Wire</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRE_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ContainerElementImpl <em>Container Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ContainerElementImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getContainerElement()
	 * @generated
	 */
	int CONTAINER_ELEMENT = 10;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_ELEMENT__TERMINALS = ELEMENT__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_ELEMENT__OUTPUT_TERMINALS = ELEMENT__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_ELEMENT__INPUT_TERMINALS = ELEMENT__INPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_ELEMENT__CHILDREN = ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Container Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_ELEMENT_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.CircuitImpl <em>Circuit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.CircuitImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getCircuit()
	 * @generated
	 */
	int CIRCUIT = 3;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CIRCUIT__TERMINALS = CONTAINER_ELEMENT__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CIRCUIT__OUTPUT_TERMINALS = CONTAINER_ELEMENT__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CIRCUIT__INPUT_TERMINALS = CONTAINER_ELEMENT__INPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CIRCUIT__CHILDREN = CONTAINER_ELEMENT__CHILDREN;

	/**
	 * The number of structural features of the '<em>Circuit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CIRCUIT_FEATURE_COUNT = CONTAINER_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.GateImpl <em>Gate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.GateImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getGate()
	 * @generated
	 */
	int GATE = 4;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GATE__TERMINALS = ELEMENT__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GATE__OUTPUT_TERMINALS = ELEMENT__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GATE__INPUT_TERMINALS = ELEMENT__INPUT_TERMINALS;

	/**
	 * The number of structural features of the '<em>Gate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GATE_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.FlowContainerImpl <em>Flow Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.FlowContainerImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getFlowContainer()
	 * @generated
	 */
	int FLOW_CONTAINER = 5;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_CONTAINER__TERMINALS = CONTAINER_ELEMENT__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_CONTAINER__OUTPUT_TERMINALS = CONTAINER_ELEMENT__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_CONTAINER__INPUT_TERMINALS = CONTAINER_ELEMENT__INPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_CONTAINER__CHILDREN = CONTAINER_ELEMENT__CHILDREN;

	/**
	 * The number of structural features of the '<em>Flow Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_CONTAINER_FEATURE_COUNT = CONTAINER_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.AndGateImpl <em>And Gate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.AndGateImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getAndGate()
	 * @generated
	 */
	int AND_GATE = 6;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AND_GATE__TERMINALS = GATE__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AND_GATE__OUTPUT_TERMINALS = GATE__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AND_GATE__INPUT_TERMINALS = GATE__INPUT_TERMINALS;

	/**
	 * The number of structural features of the '<em>And Gate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AND_GATE_FEATURE_COUNT = GATE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.OrGateImpl <em>Or Gate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.OrGateImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getOrGate()
	 * @generated
	 */
	int OR_GATE = 7;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OR_GATE__TERMINALS = GATE__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OR_GATE__OUTPUT_TERMINALS = GATE__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OR_GATE__INPUT_TERMINALS = GATE__INPUT_TERMINALS;

	/**
	 * The number of structural features of the '<em>Or Gate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OR_GATE_FEATURE_COUNT = GATE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.XORGateImpl <em>XOR Gate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.XORGateImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getXORGate()
	 * @generated
	 */
	int XOR_GATE = 8;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XOR_GATE__TERMINALS = GATE__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XOR_GATE__OUTPUT_TERMINALS = GATE__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XOR_GATE__INPUT_TERMINALS = GATE__INPUT_TERMINALS;

	/**
	 * The number of structural features of the '<em>XOR Gate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XOR_GATE_FEATURE_COUNT = GATE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ModelImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getModel()
	 * @generated
	 */
	int MODEL = 9;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__TERMINALS = CONTAINER_ELEMENT__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__OUTPUT_TERMINALS = CONTAINER_ELEMENT__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__INPUT_TERMINALS = CONTAINER_ELEMENT__INPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__CHILDREN = CONTAINER_ELEMENT__CHILDREN;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_FEATURE_COUNT = CONTAINER_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.TerminalImpl <em>Terminal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.TerminalImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getTerminal()
	 * @generated
	 */
	int TERMINAL = 11;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINAL__TERMINALS = ELEMENT__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINAL__OUTPUT_TERMINALS = ELEMENT__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINAL__INPUT_TERMINALS = ELEMENT__INPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINAL__ID = ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Terminal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINAL_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.OutputTerminalImpl <em>Output Terminal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.OutputTerminalImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getOutputTerminal()
	 * @generated
	 */
	int OUTPUT_TERMINAL = 12;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_TERMINAL__TERMINALS = TERMINAL__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_TERMINAL__OUTPUT_TERMINALS = TERMINAL__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_TERMINAL__INPUT_TERMINALS = TERMINAL__INPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_TERMINAL__ID = TERMINAL__ID;

	/**
	 * The number of structural features of the '<em>Output Terminal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_TERMINAL_FEATURE_COUNT = TERMINAL_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.InputTerminalImpl <em>Input Terminal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.InputTerminalImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getInputTerminal()
	 * @generated
	 */
	int INPUT_TERMINAL = 13;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_TERMINAL__TERMINALS = TERMINAL__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_TERMINAL__OUTPUT_TERMINALS = TERMINAL__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_TERMINAL__INPUT_TERMINALS = TERMINAL__INPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_TERMINAL__ID = TERMINAL__ID;

	/**
	 * The number of structural features of the '<em>Input Terminal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_TERMINAL_FEATURE_COUNT = TERMINAL_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.InputOutputTerminalImpl <em>Input Output Terminal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.InputOutputTerminalImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getInputOutputTerminal()
	 * @generated
	 */
	int INPUT_OUTPUT_TERMINAL = 14;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_OUTPUT_TERMINAL__TERMINALS = OUTPUT_TERMINAL__TERMINALS;

	/**
	 * The feature id for the '<em><b>Output Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_OUTPUT_TERMINAL__OUTPUT_TERMINALS = OUTPUT_TERMINAL__OUTPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Input Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_OUTPUT_TERMINAL__INPUT_TERMINALS = OUTPUT_TERMINAL__INPUT_TERMINALS;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_OUTPUT_TERMINAL__ID = OUTPUT_TERMINAL__ID;

	/**
	 * The number of structural features of the '<em>Input Output Terminal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_OUTPUT_TERMINAL_FEATURE_COUNT = OUTPUT_TERMINAL_FEATURE_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED <em>LED</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>LED</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED
	 * @generated
	 */
	EClass getLED();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED#getValue()
	 * @see #getLED()
	 * @generated
	 */
	EAttribute getLED_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element
	 * @generated
	 */
	EClass getElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element#getTerminals <em>Terminals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Terminals</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element#getTerminals()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_Terminals();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element#getOutputTerminals <em>Output Terminals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Output Terminals</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element#getOutputTerminals()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_OutputTerminals();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element#getInputTerminals <em>Input Terminals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Input Terminals</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element#getInputTerminals()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_InputTerminals();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire <em>Wire</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Wire</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire
	 * @generated
	 */
	EClass getWire();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire#getSource()
	 * @see #getWire()
	 * @generated
	 */
	EReference getWire_Source();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire#getTarget()
	 * @see #getWire()
	 * @generated
	 */
	EReference getWire_Target();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Circuit <em>Circuit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Circuit</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Circuit
	 * @generated
	 */
	EClass getCircuit();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Gate <em>Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Gate</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Gate
	 * @generated
	 */
	EClass getGate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.FlowContainer <em>Flow Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Flow Container</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.FlowContainer
	 * @generated
	 */
	EClass getFlowContainer();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.AndGate <em>And Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>And Gate</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.AndGate
	 * @generated
	 */
	EClass getAndGate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OrGate <em>Or Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Or Gate</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OrGate
	 * @generated
	 */
	EClass getOrGate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.XORGate <em>XOR Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>XOR Gate</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.XORGate
	 * @generated
	 */
	EClass getXORGate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Model <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Model
	 * @generated
	 */
	EClass getModel();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.ContainerElement <em>Container Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Container Element</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.ContainerElement
	 * @generated
	 */
	EClass getContainerElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.ContainerElement#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.ContainerElement#getChildren()
	 * @see #getContainerElement()
	 * @generated
	 */
	EReference getContainerElement_Children();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal <em>Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Terminal</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal
	 * @generated
	 */
	EClass getTerminal();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal#getId()
	 * @see #getTerminal()
	 * @generated
	 */
	EAttribute getTerminal_Id();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OutputTerminal <em>Output Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Terminal</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OutputTerminal
	 * @generated
	 */
	EClass getOutputTerminal();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputTerminal <em>Input Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Terminal</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputTerminal
	 * @generated
	 */
	EClass getInputTerminal();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputOutputTerminal <em>Input Output Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Output Terminal</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputOutputTerminal
	 * @generated
	 */
	EClass getInputOutputTerminal();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SemanticFactory getSemanticFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.LEDImpl <em>LED</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.LEDImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getLED()
		 * @generated
		 */
		EClass LED = eINSTANCE.getLED();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LED__VALUE = eINSTANCE.getLED_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ElementImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getElement()
		 * @generated
		 */
		EClass ELEMENT = eINSTANCE.getElement();

		/**
		 * The meta object literal for the '<em><b>Terminals</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__TERMINALS = eINSTANCE.getElement_Terminals();

		/**
		 * The meta object literal for the '<em><b>Output Terminals</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__OUTPUT_TERMINALS = eINSTANCE.getElement_OutputTerminals();

		/**
		 * The meta object literal for the '<em><b>Input Terminals</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__INPUT_TERMINALS = eINSTANCE.getElement_InputTerminals();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.WireImpl <em>Wire</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.WireImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getWire()
		 * @generated
		 */
		EClass WIRE = eINSTANCE.getWire();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WIRE__SOURCE = eINSTANCE.getWire_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WIRE__TARGET = eINSTANCE.getWire_Target();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.CircuitImpl <em>Circuit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.CircuitImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getCircuit()
		 * @generated
		 */
		EClass CIRCUIT = eINSTANCE.getCircuit();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.GateImpl <em>Gate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.GateImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getGate()
		 * @generated
		 */
		EClass GATE = eINSTANCE.getGate();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.FlowContainerImpl <em>Flow Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.FlowContainerImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getFlowContainer()
		 * @generated
		 */
		EClass FLOW_CONTAINER = eINSTANCE.getFlowContainer();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.AndGateImpl <em>And Gate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.AndGateImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getAndGate()
		 * @generated
		 */
		EClass AND_GATE = eINSTANCE.getAndGate();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.OrGateImpl <em>Or Gate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.OrGateImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getOrGate()
		 * @generated
		 */
		EClass OR_GATE = eINSTANCE.getOrGate();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.XORGateImpl <em>XOR Gate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.XORGateImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getXORGate()
		 * @generated
		 */
		EClass XOR_GATE = eINSTANCE.getXORGate();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ModelImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getModel()
		 * @generated
		 */
		EClass MODEL = eINSTANCE.getModel();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ContainerElementImpl <em>Container Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ContainerElementImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getContainerElement()
		 * @generated
		 */
		EClass CONTAINER_ELEMENT = eINSTANCE.getContainerElement();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTAINER_ELEMENT__CHILDREN = eINSTANCE.getContainerElement_Children();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.TerminalImpl <em>Terminal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.TerminalImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getTerminal()
		 * @generated
		 */
		EClass TERMINAL = eINSTANCE.getTerminal();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TERMINAL__ID = eINSTANCE.getTerminal_Id();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.OutputTerminalImpl <em>Output Terminal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.OutputTerminalImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getOutputTerminal()
		 * @generated
		 */
		EClass OUTPUT_TERMINAL = eINSTANCE.getOutputTerminal();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.InputTerminalImpl <em>Input Terminal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.InputTerminalImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getInputTerminal()
		 * @generated
		 */
		EClass INPUT_TERMINAL = eINSTANCE.getInputTerminal();

		/**
		 * The meta object literal for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.InputOutputTerminalImpl <em>Input Output Terminal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.InputOutputTerminalImpl
		 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.SemanticPackageImpl#getInputOutputTerminal()
		 * @generated
		 */
		EClass INPUT_OUTPUT_TERMINAL = eINSTANCE.getInputOutputTerminal();

	}

} //SemanticPackage
