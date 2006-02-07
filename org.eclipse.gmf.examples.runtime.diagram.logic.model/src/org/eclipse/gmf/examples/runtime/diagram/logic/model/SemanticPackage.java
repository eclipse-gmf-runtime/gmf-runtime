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

package org.eclipse.gmf.examples.runtime.diagram.logic.model;

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
 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticFactory
 * @generated
 */
public interface SemanticPackage extends EPackage{
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
	SemanticPackage eINSTANCE = org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.ElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.ElementImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getElement()
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
	 * The number of structural features of the the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.LEDImpl <em>LED</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.LEDImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getLED()
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
	 * The number of structural features of the the '<em>LED</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LED_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.WireImpl <em>Wire</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.WireImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getWire()
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
	 * The number of structural features of the the '<em>Wire</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRE_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.ContainerElementImpl <em>Container Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.ContainerElementImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getContainerElement()
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
	 * The number of structural features of the the '<em>Container Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_ELEMENT_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.CircuitImpl <em>Circuit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.CircuitImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getCircuit()
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
	 * The number of structural features of the the '<em>Circuit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CIRCUIT_FEATURE_COUNT = CONTAINER_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.GateImpl <em>Gate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.GateImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getGate()
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
	 * The number of structural features of the the '<em>Gate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GATE_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.FlowContainerImpl <em>Flow Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.FlowContainerImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getFlowContainer()
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
	 * The number of structural features of the the '<em>Flow Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_CONTAINER_FEATURE_COUNT = CONTAINER_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.AndGateImpl <em>And Gate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.AndGateImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getAndGate()
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
	 * The number of structural features of the the '<em>And Gate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AND_GATE_FEATURE_COUNT = GATE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.OrGateImpl <em>Or Gate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.OrGateImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getOrGate()
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
	 * The number of structural features of the the '<em>Or Gate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OR_GATE_FEATURE_COUNT = GATE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.XORGateImpl <em>XOR Gate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.XORGateImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getXORGate()
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
	 * The number of structural features of the the '<em>XOR Gate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XOR_GATE_FEATURE_COUNT = GATE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.ModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.ModelImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getModel()
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
	 * The number of structural features of the the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_FEATURE_COUNT = CONTAINER_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.TerminalImpl <em>Terminal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.TerminalImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getTerminal()
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
	 * The number of structural features of the the '<em>Terminal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINAL_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 1;


	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.OutputTerminalImpl <em>Output Terminal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.OutputTerminalImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getOutputTerminal()
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
	 * The number of structural features of the the '<em>Output Terminal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_TERMINAL_FEATURE_COUNT = TERMINAL_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.InputTerminalImpl <em>Input Terminal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.InputTerminalImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getInputTerminal()
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
	 * The number of structural features of the the '<em>Input Terminal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_TERMINAL_FEATURE_COUNT = TERMINAL_FEATURE_COUNT + 0;


	/**
	 * The meta object id for the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.InputOutputTerminalImpl <em>Input Output Terminal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.InputOutputTerminalImpl
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.SemanticPackageImpl#getInputOutputTerminal()
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
	 * The number of structural features of the the '<em>Input Output Terminal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_OUTPUT_TERMINAL_FEATURE_COUNT = OUTPUT_TERMINAL_FEATURE_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.LED <em>LED</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>LED</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.LED
	 * @generated
	 */
	EClass getLED();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.LED#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.LED#getValue()
	 * @see #getLED()
	 * @generated
	 */
	EAttribute getLED_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Element <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Element
	 * @generated
	 */
	EClass getElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Element#getTerminals <em>Terminals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Terminals</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Element#getTerminals()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_Terminals();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Element#getOutputTerminals <em>Output Terminals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Output Terminals</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Element#getOutputTerminals()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_OutputTerminals();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Element#getInputTerminals <em>Input Terminals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Input Terminals</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Element#getInputTerminals()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_InputTerminals();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire <em>Wire</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Wire</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire
	 * @generated
	 */
	EClass getWire();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire#getSource()
	 * @see #getWire()
	 * @generated
	 */
	EReference getWire_Source();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire#getTarget()
	 * @see #getWire()
	 * @generated
	 */
	EReference getWire_Target();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Circuit <em>Circuit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Circuit</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Circuit
	 * @generated
	 */
	EClass getCircuit();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Gate <em>Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Gate</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Gate
	 * @generated
	 */
	EClass getGate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.FlowContainer <em>Flow Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Flow Container</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.FlowContainer
	 * @generated
	 */
	EClass getFlowContainer();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.AndGate <em>And Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>And Gate</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.AndGate
	 * @generated
	 */
	EClass getAndGate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.OrGate <em>Or Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Or Gate</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.OrGate
	 * @generated
	 */
	EClass getOrGate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.XORGate <em>XOR Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>XOR Gate</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.XORGate
	 * @generated
	 */
	EClass getXORGate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Model <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Model
	 * @generated
	 */
	EClass getModel();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.ContainerElement <em>Container Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Container Element</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.ContainerElement
	 * @generated
	 */
	EClass getContainerElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.ContainerElement#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.ContainerElement#getChildren()
	 * @see #getContainerElement()
	 * @generated
	 */
	EReference getContainerElement_Children();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal <em>Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Terminal</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal
	 * @generated
	 */
	EClass getTerminal();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal#getId()
	 * @see #getTerminal()
	 * @generated
	 */
	EAttribute getTerminal_Id();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.OutputTerminal <em>Output Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Terminal</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.OutputTerminal
	 * @generated
	 */
	EClass getOutputTerminal();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal <em>Input Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Terminal</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal
	 * @generated
	 */
	EClass getInputTerminal();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.InputOutputTerminal <em>Input Output Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Output Terminal</em>'.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.InputOutputTerminal
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

} //SemanticPackage
